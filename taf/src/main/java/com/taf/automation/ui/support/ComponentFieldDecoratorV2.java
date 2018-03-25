package com.taf.automation.ui.support;

import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.support.pagefactory.Annotations;
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
        String dataValue = null;
        String initialValue = null;
        String expectedValue = null;

        if (PageComponent.class.isAssignableFrom(field.getType())) {
            ElementLocator locator = factory.createLocator(field);
            if (locator == null) {
                return null;
            }

            ComponentData componentData = null;
            try {
                field.setAccessible(true);
                componentData = (ComponentData) field.get(page);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
                Annotations annotations = new Annotations(field);
                FieldUtils.writeField(componentProxy, "selector", annotations.buildBy(), true);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            return componentProxy;
        }

        return super.decorate(loader, field);
    }

}
