package com.automation.common.ui.app.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * Check Box component that bypasses issues where WebDriver does not allow normal interaction with the check box as
 * it is disabled instead clicking the label node toggles the state<BR>
 */
public class CheckBoxLabel extends PageComponent {
    private WebElement input;
    private WebElement label;

    public CheckBoxLabel() {
        super();
    }

    public CheckBoxLabel(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        input = getCoreElement().findElement(By.cssSelector("input[type=checkbox]"));
        label = getCoreElement().findElement(By.cssSelector("label"));
    }

    @Override
    public boolean isSelected() {
        return input.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return input.isEnabled();
    }

    public void check() {
        if (!isSelected()) {
            label.click();
        }
    }

    public void uncheck() {
        if (isSelected()) {
            label.click();
        }
    }

    public void check(boolean value) {
        if (value) {
            check();
        } else {
            uncheck();
        }
    }

    @Override
    public void setValue() {
        check(getData().equalsIgnoreCase("true"));
    }

    @Override
    public String getValue() {
        return String.valueOf(input.isSelected());
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        Assert.assertEquals(input.isSelected(), validationMethod.getData(this).toLowerCase().trim().equals("true"));
    }

    public WebElement getInput() {
        return input;
    }

}
