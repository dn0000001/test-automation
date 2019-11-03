package com.automation.common.ui.app.components;

import org.openqa.selenium.WebElement;

public class TextBoxBackspace extends TextBox {
    public TextBoxBackspace() {
        super();
        clearUsingBackspace = true;
    }

    public TextBoxBackspace(WebElement element) {
        super(element);
        clearUsingBackspace = true;
    }

}
