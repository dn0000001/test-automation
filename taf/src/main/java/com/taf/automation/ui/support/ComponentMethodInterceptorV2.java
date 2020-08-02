package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Utils;
import net.jodah.failsafe.Failsafe;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ui.auto.core.data.ComponentData;
import ui.auto.core.pagecomponent.PageComponent;
import ui.auto.core.pagecomponent.PageObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This is an enhanced version of the ComponentMethodInterceptor that handles the JavascriptException thrown by geckodriver
 * sometimes instead of the StaleElementReferenceException.
 */
public class ComponentMethodInterceptorV2 implements MethodInterceptor {
    private ElementLocator locator;

    public ComponentMethodInterceptorV2(ElementLocator locator) {
        this.locator = locator;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();

        // Skip methods belonging to ComponentData interface
        for (Method dataMethod : ComponentData.class.getMethods()) {
            if (dataMethod.getName().equals(methodName)) {
                return proxy.invokeSuper(obj, args);
            }
        }

        // Skip page objects
        if (obj != null && PageObject.class.isAssignableFrom(obj.getClass())) {
            return proxy.invokeSuper(obj, args);
        }

        // Skip finalize method
        if (!"finalize".equals(methodName)) {
            PageComponent pageComponent = (PageComponent) obj;
            Field coreElement = FieldUtils.getField(PageComponent.class, "coreElement", true);
            WebElement currentCoreElement = (WebElement) coreElement.get(pageComponent);
            WebElement newCoreElement = locator.findElement();
            boolean staleElement = isStale(currentCoreElement);
            if (currentCoreElement == null || staleElement || !currentCoreElement.equals(newCoreElement)) {
                Failsafe.with(Utils.getRetryAtleastOncePolicy()).run(() -> initComponent(pageComponent));
            }
        }

        return proxy.invokeSuper(obj, args);
    }

    private boolean isStale(WebElement currentCoreElement) {
        if (currentCoreElement == null) {
            return false;
        }

        try {
            currentCoreElement.isDisplayed(); //This should trigger exception if element is detached from DOM
            return false;
        } catch (StaleElementReferenceException | JavascriptException | NoSuchElementException e) {
            return true;
        }
    }

    @SuppressWarnings("squid:S00112")
    private void initComponent(PageComponent pageComponent) throws Exception {
        WebElement newCoreElement = locator.findElement();
        Method m = PageComponent.class.getDeclaredMethod("initComponent", WebElement.class);
        m.setAccessible(true);
        m.invoke(pageComponent, newCoreElement);
    }

}
