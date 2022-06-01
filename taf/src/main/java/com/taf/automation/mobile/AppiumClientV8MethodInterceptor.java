package com.taf.automation.mobile;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;
import ui.auto.core.pagecomponent.PageComponent;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Based on the code from WidgetMethodInterceptor but updated to work with the Appium Client V8
 */
public class AppiumClientV8MethodInterceptor implements MethodInterceptor {
    private By locator;
    private PageComponentContext context;

    public AppiumClientV8MethodInterceptor(By locator, PageComponentContext context) {
        this.locator = locator;
        this.context = context;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();

        //Skip methods belonging to Object class and ComponentData interface
        Set<Method> skippedMethods = new HashSet<>();
        skippedMethods.addAll(Arrays.asList(Object.class.getDeclaredMethods()));
        skippedMethods.addAll(Arrays.asList(ComponentData.class.getMethods()));
        for (Method dataMethod : skippedMethods) {
            if (dataMethod.getName().equals(methodName) || methodName.equals("init")) {
                return proxy.invokeSuper(obj, args);
            }
        }

        PageComponent pageComponent = (PageComponent) obj;
        WebDriverWait wait = new WebDriverWait(context.getDriver(), Duration.ofSeconds(context.getAjaxTimeOut()));
        WebElement coreElement = wait.ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        if (coreElement != null) {
            initComponent(pageComponent, coreElement);
        }

        return proxy.invokeSuper(obj, args);
    }

    @SuppressWarnings({"squid:S00112", "java:S3011"})
    private void initComponent(PageComponent pageComponent, WebElement coreElement) throws Exception {
        Method m = PageComponent.class.getDeclaredMethod("initComponent", WebElement.class);
        m.setAccessible(true);
        m.invoke(pageComponent, coreElement);
    }

}
