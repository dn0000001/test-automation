package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.AssertJUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * Radio Option component that bypasses issues where WebDriver does not allow normal interaction with the radio button option
 * as it is disabled instead clicking the label node to make the radio button option selected<BR>
 */
@SuppressWarnings("java:S3252")
public class RadioOption extends PageComponent {
    private WebElement input;
    private WebElement label;

    public RadioOption() {
        super();
    }

    public RadioOption(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        input = getCoreElement().findElement(By.cssSelector("input[type=radio]"));
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

    @Override
    public void click() {
        setValue();
    }

    @Override
    public void setValue() {
        label.click();
    }

    @Override
    public String getValue() {
        return String.valueOf(input.isSelected());
    }

    public void validateData() {
        validateData(null);
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        AssertJUtil.assertThat(input.isSelected()).as("Radio Option not selected").isTrue();
    }

    @Override
    public String getText() {
        return label.getText();
    }

    public WebElement getInput() {
        return input;
    }

}
