package com.automation.common.ui.app.components;

import org.apache.commons.lang3.reflect.FieldUtils;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Component to work with a PrimeFaces Radio Button Group
 */
public class PrimeFacesRadioButtonGroup extends PageComponent {
    private RadioButtonGroup<RadioOption> component;
    private String componentData;
    private String componentInitialData;
    private String componentExpectedData;

    @Override
    protected void init() {
        component = new RadioButtonGroup<>(getCoreElement());
        component.setClazz(RadioOption.class);
        component.setSelection(RadioButtonGroup.Selection.REGEX);
        initComponentDataVariables();
        component.initializeData(componentData, componentInitialData, componentExpectedData);
        try {
            FieldUtils.writeField(component, "selector", getLocator(), true);
        } catch (Exception ex) {
            assertThat("Could not set 'selector' for component due to error:  " + ex.getMessage(), false);
        }
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
