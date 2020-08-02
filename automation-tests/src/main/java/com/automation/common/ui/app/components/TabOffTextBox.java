package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.LocatorUtils;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * TextBox that enters value and tabs off to save which may make the element stale
 */
public class TabOffTextBox extends PageComponent {
    private WebDriver driver;
    private Map<String, String> substitutions;
    private By staticLocator;
    private boolean sendKeysDelay;
    private int delayInMilliseconds;

    public TabOffTextBox() {
        super();
        sendKeysDelay = false;
        delayInMilliseconds = 100;
    }

    public TabOffTextBox(WebElement element) {
        super(element);
        sendKeysDelay = false;
        delayInMilliseconds = 100;
    }

    /**
     * Enable or Disable the send keys delay
     *
     * @param sendKeyDelay - true to enable send keys delay, false to disable any delay
     */
    public void setSendKeysDelay(boolean sendKeyDelay) {
        this.sendKeysDelay = sendKeyDelay;
    }

    /**
     * Change the send keys delay (in milliseconds)<BR>
     * <B>Note: </B> It is necessary to enable send keys delay for this to have any effect.
     *
     * @param delayInMilliseconds - Delay in milliseconds between sending each key
     */
    public void setSendKeysDelay(int delayInMilliseconds) {
        this.delayInMilliseconds = delayInMilliseconds;
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

    private By getStaticLocator() {
        if (staticLocator == null) {
            staticLocator = LocatorUtils.processForSubstitutions(getLocator(), getSubstitutions());
        }

        return staticLocator;
    }

    private Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    public void setSubstitutions(Map<String, String> substitutions) {
        this.substitutions = substitutions;
    }

    public void resetStaticLocator() {
        this.staticLocator = null;
    }

    @Override
    public void setValue() {
        setValue(getData());
    }

    public void setValue(String value) {
        getCoreElement().clear();
        if (sendKeysDelay) {
            Utils.sendKeysWithDelay(getCoreElement(), delayInMilliseconds, value + Keys.TAB);
        } else {
            getCoreElement().sendKeys(value + Keys.TAB);
        }
    }

    @Override
    public String getValue() {
        String value = getDriver().findElement(getStaticLocator()).getAttribute("value");
        if (value == null) {
            value = getDriver().findElement(getStaticLocator()).getText();
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
