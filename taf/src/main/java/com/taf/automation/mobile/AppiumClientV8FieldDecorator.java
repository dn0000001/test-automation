package com.taf.automation.mobile;

import com.taf.automation.ui.support.util.Utils;
import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.AndroidFindAll;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AndroidFindByAllSet;
import io.appium.java_client.pagefactory.AndroidFindByChainSet;
import io.appium.java_client.pagefactory.AndroidFindBySet;
import io.appium.java_client.pagefactory.AndroidFindBys;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import io.appium.java_client.pagefactory.WindowsBy;
import io.appium.java_client.pagefactory.WindowsFindAll;
import io.appium.java_client.pagefactory.WindowsFindBy;
import io.appium.java_client.pagefactory.WindowsFindByAllSet;
import io.appium.java_client.pagefactory.WindowsFindByChainSet;
import io.appium.java_client.pagefactory.WindowsFindBySet;
import io.appium.java_client.pagefactory.WindowsFindBys;
import io.appium.java_client.pagefactory.iOSBy;
import io.appium.java_client.pagefactory.iOSXCUITBy;
import io.appium.java_client.pagefactory.iOSXCUITFindAll;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindByAllSet;
import io.appium.java_client.pagefactory.iOSXCUITFindByChainSet;
import io.appium.java_client.pagefactory.iOSXCUITFindBySet;
import io.appium.java_client.pagefactory.iOSXCUITFindBys;
import net.sf.cglib.proxy.Enhancer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByChained;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.InitPage;
import ui.auto.core.pagecomponent.PageComponent;
import ui.auto.core.pagecomponent.PageObject;

import java.lang.reflect.Field;
import java.util.Map;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.unpackWebDriverFromSearchContext;
import static java.util.Optional.ofNullable;

public class AppiumClientV8FieldDecorator extends AppiumFieldDecorator {
    private PageObject page;
    private PageComponentContext context;

    public AppiumClientV8FieldDecorator(PageComponentContext context, PageObject page) {
        super(context.getDriver());
        this.context = context;
        this.page = page;
    }

    @SuppressWarnings("java:S3011")
    @Override
    public Object decorate(ClassLoader ignored, Field field) {
        field.setAccessible(true);
        if (PageComponent.class.isAssignableFrom(field.getType())) {
            return initializeAsPageComponent(field);
        }

        // PageObject as Web Component
        if (PageObject.class.isAssignableFrom(field.getType()) && hasSupposedAnnotationPresent(field)) {
            initializeAsPageObject(field);
        }

        return super.decorate(ignored, field);
    }

    @SuppressWarnings("java:S112")
    private Object initializeAsPageComponent(Field field) {
        WebDriver webDriver = unpackWebDriverFromSearchContext(this.context.getDriver());
        HasSessionDetails hasSessionDetails = ofNullable(webDriver).map(driver -> {
            if (!HasSessionDetails.class.isAssignableFrom(driver.getClass())) {
                return null;
            }

            return (HasSessionDetails) driver;
        }).orElse(null);

        String platform = null;
        String automation = null;
        if (hasSessionDetails != null) {
            platform = hasSessionDetails.getPlatformName();
            automation = hasSessionDetails.getAutomationName();
        }

        DefaultElementByBuilder builder = new DefaultElementByBuilder(platform, automation);
        builder.setAnnotated(field);
        By by = builder.buildBy();
        if (by == null) {
            return null;
        }

        if (page.getLocator() != null) {
            by = new ByChained(page.getLocator(), by);
        }

        ComponentData componentData;
        try {
            componentData = (ComponentData) field.get(page);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String dataValue = null;
        String initialValue = null;
        String expectedValue = null;
        Map<String, String> customData = null;
        if (componentData != null) {
            dataValue = componentData.getData(DataTypes.Data, false);
            initialValue = componentData.getData(DataTypes.Initial, false);
            expectedValue = componentData.getData(DataTypes.Expected, false);
            customData = componentData.getCustomData();
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(field.getType());
        enhancer.setCallback(new AppiumClientV8MethodInterceptor(by, context));
        Object componentProxy = enhancer.create();
        ((ComponentData) componentProxy).initializeData(dataValue, initialValue, expectedValue);
        ((ComponentData) componentProxy).addCustomData(customData);
        Utils.writeField(componentProxy, "fieldName", field.getName());
        Utils.writeField(componentProxy, "selector", by);
        return componentProxy;
    }

    @SuppressWarnings({"java:S3011", "java:S112"})
    private void initializeAsPageObject(Field field) {
        try {
            PageObject po = (PageObject) field.get(page);
            if (po == null) {
                Object value = field.getType().newInstance();
                po = (PageObject) value;
                field.set(page, value);
                Utils.writeField(po, "dataProvided", false);
            } else {
                Utils.writeField(po, "dataProvided", true);
            }

            if (!field.isAnnotationPresent(InitPage.class)) {
                Annotations annotations = new Annotations(field);
                Utils.writeField(po, "locator", annotations.buildBy());
            }

            PageFactory.initElements(new AppiumClientV8FieldDecorator(context, po), po);
            Utils.writeField(po, "context", page.getContext());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S2109")
    private boolean hasSupposedAnnotationPresent(Field field) {
        return field.isAnnotationPresent(FindBy.class)
                || field.isAnnotationPresent(FindAll.class)
                || field.isAnnotationPresent(FindBys.class)
                || field.isAnnotationPresent(AndroidBy.class)
                || field.isAnnotationPresent(AndroidFindAll.class)
                || field.isAnnotationPresent(AndroidFindBy.class)
                || field.isAnnotationPresent(AndroidFindByAllSet.class)
                || field.isAnnotationPresent(AndroidFindByChainSet.class)
                || field.isAnnotationPresent(AndroidFindBys.class)
                || field.isAnnotationPresent(AndroidFindBySet.class)
                || field.isAnnotationPresent(iOSBy.class)
                || field.isAnnotationPresent(iOSXCUITBy.class)
                || field.isAnnotationPresent(iOSXCUITFindAll.class)
                || field.isAnnotationPresent(iOSXCUITFindBy.class)
                || field.isAnnotationPresent(iOSXCUITFindByAllSet.class)
                || field.isAnnotationPresent(iOSXCUITFindByChainSet.class)
                || field.isAnnotationPresent(iOSXCUITFindBys.class)
                || field.isAnnotationPresent(iOSXCUITFindBySet.class)
                || field.isAnnotationPresent(WindowsBy.class)
                || field.isAnnotationPresent(WindowsFindAll.class)
                || field.isAnnotationPresent(WindowsFindBy.class)
                || field.isAnnotationPresent(WindowsFindByAllSet.class)
                || field.isAnnotationPresent(WindowsFindByChainSet.class)
                || field.isAnnotationPresent(WindowsFindBys.class)
                || field.isAnnotationPresent(WindowsFindBySet.class)
                || field.isAnnotationPresent(InitPage.class)
                ;
    }

}
