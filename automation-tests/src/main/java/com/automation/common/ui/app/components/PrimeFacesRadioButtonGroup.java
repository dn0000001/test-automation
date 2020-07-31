package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.LocatorUtils;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Component to work with a PrimeFaces Radio Button Group
 */
public class PrimeFacesRadioButtonGroup extends PageComponent {
    private Map<String, String> substitutions;
    private RadioButtonGroup<RadioOption> component;
    private String componentData;
    private String componentInitialData;
    private String componentExpectedData;

    public PrimeFacesRadioButtonGroup() {
        super();
    }

    public PrimeFacesRadioButtonGroup(WebElement element) {
        super(element);
    }

    private Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    public void setSubstitutions(Map<String, String> substitutions) {
        this.substitutions = substitutions;
        component.setSubstitutions(substitutions);
    }

    @Override
    protected void init() {
        component = new RadioButtonGroup<>(getCoreElement());
        component.setClazz(RadioOption.class);
        component.setSelection(RadioButtonGroup.Selection.REGEX);
        component.setSubstitutions(getSubstitutions());
        initComponentDataVariables();
        component.initializeData(componentData, componentInitialData, componentExpectedData);
        LocatorUtils.setLocator(component, getLocator());
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

    @Override
    public void setValue() {
        component.setValue();
    }

    @Override
    public String getValue() {
        return component.getValue();
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        component.validateData(validationMethod);
    }

}
