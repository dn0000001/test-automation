package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Component purely for unit testing purposes
 */
public class UnitTestComponent extends PageComponent {
    private final Map<String, String> attributes;
    private boolean selected;
    private boolean displayed;
    private boolean enabled;
    private String text;
    private String value;

    public UnitTestComponent() {
        attributes = new HashMap<>();
        selected = false;
        displayed = true;
        enabled = true;
        text = "";
        value = "";
    }

    public UnitTestComponent(WebElement element) {
        super(element);
        attributes = new HashMap<>();
        selected = false;
        displayed = true;
        enabled = true;
        text = "";
        value = "";
    }

    public UnitTestComponent withAttribute(String attribute, String value) {
        attributes.put(attribute, value);
        return this;
    }

    public UnitTestComponent withSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public UnitTestComponent withDisplayed(boolean displayed) {
        this.displayed = displayed;
        return this;
    }

    public UnitTestComponent withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UnitTestComponent withText(String text) {
        this.text = text;
        return this;
    }

    public UnitTestComponent withValue(String value) {
        this.value = value;
        return this;
    }

    public UnitTestComponent withLocator(By locator) {
        try {
            FieldUtils.writeField(this, "selector", locator, true);
        } catch (Exception ex) {
            Helper.log("Could not set locator in UnitTestComponent to:  " + locator, true);
        }

        return this;
    }

    @Override
    protected void init() {
        //
    }

    @Override
    public void setValue() {
        assertThat("UnitTestComponent value could not be set as it is not displayed", displayed);
        assertThat("UnitTestComponent value could not be set as it is not enabled", enabled);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        //
    }

    @Override
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isDisplayed() {
        return displayed;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        ToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
        return ReflectionToStringBuilder.toStringExclude(this);
    }

}
