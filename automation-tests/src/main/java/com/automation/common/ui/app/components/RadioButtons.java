package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.LocatorUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class from which application specific components can be created to work with radio button options.
 *
 * @param <T> Type of options
 */
public abstract class RadioButtons<T extends PageComponent> extends PageComponent {
    private Map<String, String> substitutions;
    private RadioButtonGroup<T> component;
    private By staticLocator;
    private String componentData;
    private String componentInitialData;
    private String componentExpectedData;
    protected RadioButtonGroup.Selection selection;

    protected RadioButtons() {
        super();
    }

    protected RadioButtons(WebElement element) {
        super(element);
    }

    protected By getStaticLocator() {
        if (staticLocator == null) {
            staticLocator = LocatorUtils.processForSubstitutions(getLocator(), getSubstitutions());
        }

        return staticLocator;
    }

    protected Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    public RadioButtons<T> withSubstitutions(Map<String, String> substitutions) {
        this.substitutions = substitutions;
        getComponent().setSubstitutions(substitutions);
        return this;
    }

    protected RadioButtonGroup.Selection getSelection() {
        return selection == null ? RadioButtonGroup.Selection.EQUALS : selection;
    }

    public RadioButtons<T> withSelection(RadioButtonGroup.Selection selection) {
        this.selection = selection;
        return this;
    }

    @Override
    protected void init() {
        component = new RadioButtonGroup<>(getCoreElement());
        component.setClazz(getClazz());
        component.setSelection(getSelection());
        component.setSubstitutions(getSubstitutions());
        initComponentDataVariables();
        component.initializeData(componentData, componentInitialData, componentExpectedData);
        LocatorUtils.setLocator(component, getStaticLocator());
    }

    @Override
    public void initializeData(String data, String initialData, String expectedData) {
        componentData = data;
        componentInitialData = initialData;
        componentExpectedData = expectedData;
        if (component != null) {
            component.initializeData(componentData, componentInitialData, componentExpectedData);
        }

        super.initializeData(data, initialData, expectedData);
    }

    /**
     * For performance reasons, only initialize the data once
     */
    private void initComponentDataVariables() {
        if (componentData == null) {
            componentData = getData(DataTypes.Data);
        }

        if (componentInitialData == null) {
            componentInitialData = getData(DataTypes.Initial);
        }

        if (componentExpectedData == null) {
            componentExpectedData = getData(DataTypes.Expected);
        }
    }

    /**
     * Get the component if it is necessary to implement custom logic for setting/getting the value
     *
     * @return RadioButtonGroup&lt;T&gt;
     */
    protected RadioButtonGroup<T> getComponent() {
        return component;
    }

    @Override
    public void setValue() {
        getComponent().setValue();
    }

    @Override
    public String getValue() {
        return getComponent().getValue();
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        getComponent().validateData(validationMethod);
    }

    public List<T> getAllOptions() {
        return getComponent().getAllOptions();
    }

    /**
     * Get the Radio Button Group class type which is used create the instances of the available options
     *
     * @return Class<T>
     */
    protected abstract Class<T> getClazz();
}
