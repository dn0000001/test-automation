package com.taf.automation.ui.app.components;

import org.openqa.selenium.WebElement;
import ui.auto.core.components.WebComponent;

public class TextBox extends WebComponent {

    public TextBox(){}

    public TextBox(WebElement element) {
        super(element);
    }

    public void setValue(String value) {
        getCoreElement().clear();
        getCoreElement().sendKeys(value);
    }
}
