package com.taf.automation.ui.support;

import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import ui.auto.core.data.ComponentData;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;
import ui.auto.core.pagecomponent.PageObject;

import java.lang.reflect.Field;

/**
 * This is an enhanced version of the ComponentFieldDecorator that handles the JavascriptException thrown by geckodriver
 * sometimes instead of the StaleElementReferenceException.
 */
public class ComponentFieldDecoratorV2 extends DefaultFieldDecorator {
    private PageObject page;

    public ComponentFieldDecoratorV2(ElementLocatorFactory factory, PageObject page) {
        super(factory);
        this.page = page;
    }

    @Override
    public Object decorate(ClassLoader loader, final Field field) {
        if (PageComponent.class.isAssignableFrom(field.getType())) {
            return decoratePageComponent(field);
        }

        if (PageObject.class.isAssignableFrom(field.getType())) {
            return decoratePageObject(field);
        }

        return super.decorate(loader, field);
    }

    @SuppressWarnings("squid:S00112")
    private Object decoratePageComponent(final Field field) {
        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }

        ComponentData componentData;
        try {
            field.setAccessible(true);
            componentData = (ComponentData) field.get(page);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String dataValue = null;
        String initialValue = null;
        String expectedValue = null;
        if (componentData != null) {
            dataValue = componentData.getData(DataTypes.Data, false);
            initialValue = componentData.getData(DataTypes.Initial, false);
            expectedValue = componentData.getData(DataTypes.Expected, false);
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(field.getType());
        enhancer.setCallback(new ComponentMethodInterceptorV2(locator));
        Object componentProxy = enhancer.create();
        ((ComponentData) componentProxy).initializeData(dataValue, initialValue, expectedValue);
        try {
            FieldUtils.writeField(componentProxy, "selector", modifyLocator(locator, page.getLocator()), true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return componentProxy;
    }

    private By modifyLocator(ElementLocator locator, By parentLocator) throws IllegalAccessException, NoSuchFieldException {
        By bys = null;

        if (DefaultElementLocator.class.isAssignableFrom(locator.getClass())) {
            bys = getConstructedLocator(DefaultElementLocator.class.getDeclaredField("by"), locator, parentLocator);
        } else if (DynamicElementLocator.class.isAssignableFrom(locator.getClass())) {
            bys = getConstructedLocator(DynamicElementLocator.class.getDeclaredField("by"), locator, parentLocator);
        }

        return bys;
    }

    private By getConstructedLocator(Field by, ElementLocator locator, By parentLocator) throws IllegalAccessException {
        By bys;

        by.setAccessible(true);
        By childLocator = (By) by.get(locator);
        if (parentLocator != null) {
            bys = new ByChained(parentLocator, childLocator);
            by.set(locator, bys);
        } else {
            bys = childLocator;
        }

        return bys;
    }

    @SuppressWarnings("squid:S00112")
    private Object decoratePageObject(final Field field) {
        field.setAccessible(true);
        if (field.getAnnotation(FindBy.class) != null) {
            PageObject po;
            try {
                field.setAccessible(true);
                po = (PageObject) field.get(page);

                // Handle case in which page object has annotation for locator but does not have any data specified
                if (po == null) {
                    Enhancer enhancer = new Enhancer();
                    enhancer.setSuperclass(field.getType());
                    enhancer.setCallback(new ComponentMethodInterceptorV2(factory.createLocator(field)));
                    po = (PageObject) enhancer.create();
                }

                if (po != null) {
                    Annotations annotations = new Annotations(field);
                    FieldUtils.writeField(po, "locator", annotations.buildBy(), true);
                    return po;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

}
