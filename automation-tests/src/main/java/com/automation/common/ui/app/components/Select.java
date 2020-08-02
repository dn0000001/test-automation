package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.WebElement;
import ui.auto.core.components.SelectComponent;

public class Select extends SelectComponent {
    public Select() {
        super();
    }

    public Select(WebElement element) {
        super(element);
    }

    @Override
    public String getValue() {
        return super.getValue().trim(); // There are trailing spaces in text of select component when using Chrome browser
    }

    @Override
    public void setValue() {
        // In some cases content of the select element is lazily populated, which triggers an error
        // as required option is not loaded before selection occurs
        // to avoid errors we will wait for the specific option to appear
        Utils.waitForSelectOption(this);
        super.setValue();
    }

}
