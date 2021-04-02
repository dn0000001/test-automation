package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.LocatorUtils;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Check Box component that works with AJAX
 */
@SuppressWarnings("java:S3252")
public class CheckBoxAJAX extends PageComponent {
    private WebDriver driver;
    private Map<String, String> substitutions;
    private By staticLocator;

    public CheckBoxAJAX() {
        super();
    }

    public CheckBoxAJAX(WebElement element) {
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

    private WebElement getCheckBox() {
        return getDriver().findElement(getStaticLocator());
    }

    @Override
    public boolean isSelected() {
        return getCheckBox().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getCheckBox().isEnabled();
    }

    public void check() {
        if (!isSelected()) {
            assertThat("CheckBoxAJAX was disabled", isEnabled());
            Utils.clickAndWaitForStale(getCheckBox());
        }
    }

    public void uncheck() {
        if (isSelected()) {
            assertThat("CheckBoxAJAX was disabled", isEnabled());
            Utils.clickAndWaitForStale(getCheckBox());
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
        return String.valueOf(isSelected());
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        AssertJUtil.assertThat(isSelected()).isEqualTo(validationMethod.getData(this).toLowerCase().trim().equals("true"));
    }

}
