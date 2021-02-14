package com.automation.common.ui.app.components;

import com.automation.common.ui.app.components.validator.BasicEqualsValidator;
import org.openqa.selenium.WebElement;

public class TextBoxBackspace extends TextBox {
    public TextBoxBackspace() {
        super();
        clearUsingBackspace = true;
        validator = new BasicEqualsValidator().withFailureMessage("TextBoxBackspace Value");
    }

    public TextBoxBackspace(WebElement element) {
        super(element);
        clearUsingBackspace = true;
        validator = new BasicEqualsValidator().withFailureMessage("TextBoxBackspace Value");
    }

}
