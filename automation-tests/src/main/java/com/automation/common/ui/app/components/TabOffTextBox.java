package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.Utils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * TextBox that enters value and tabs off to save which may make the element stale
 */
public class TabOffTextBox extends PageComponent {
    private WebDriver driver;

    public TabOffTextBox() {
        super();
    }

    public TabOffTextBox(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        getDriver();
    }

    private WebDriver getDriver() {
        if (driver == null) {
            driver = Utils.getWebDriver(getCoreElement());
        }

        return driver;
    }

    @Override
    public void setValue() {
        setValue(getData());
    }

    public void setValue(String value) {
        getCoreElement().clear();
        getCoreElement().sendKeys(value + Keys.TAB);
    }

    @Override
    public String getValue() {
        String value = getDriver().findElement(getLocator()).getAttribute("value");
        if (value == null) {
            value = getDriver().findElement(getLocator()).getText();
        }

        return value;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        String actual = getValue();
        String expected = validationMethod.getData(this);
        assertThat("TabOffTextBox Value", actual, equalTo(expected));
    }

}
