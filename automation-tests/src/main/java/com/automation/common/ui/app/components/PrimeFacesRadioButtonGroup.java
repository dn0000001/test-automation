package com.automation.common.ui.app.components;

import org.openqa.selenium.WebElement;

/**
 * Component to work with a PrimeFaces Radio Button Group
 */
public class PrimeFacesRadioButtonGroup extends RadioButtons<RadioOption> {
    public PrimeFacesRadioButtonGroup() {
        super();

        // Note:  Any code in the constructor must not cause the element to be bound
        selection = RadioButtonGroup.Selection.REGEX; // Change the default selection method
    }

    public PrimeFacesRadioButtonGroup(WebElement element) {
        super(element);

        // Note:  Any code in the constructor must not cause the element to be bound
        selection = RadioButtonGroup.Selection.REGEX; // Change the default selection method
    }

    @Override
    protected Class<RadioOption> getClazz() {
        return RadioOption.class;
    }

}
