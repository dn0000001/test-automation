package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.LocatorUtils;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic Component to work with radio button group options<BR>
 * <B>Notes:</B>
 * <OL>
 * <LI>
 * It is necessary to use method <B>setClazz</B> to set the class because this cannot be determined using reflection.
 * </LI>
 * <LI>
 * The selection/validation method can be changed using the <B>setSelection</B> method
 * </LI>
 * </OL>
 *
 * @param <T> PageComponent
 */
@SuppressWarnings("java:S3252")
public class RadioButtonGroup<T extends PageComponent> extends PageComponent {
    private Map<String, String> substitutions;
    private By staticLocator;
    private Class<T> clazz;
    private Selection selection;

    public enum Selection {
        EQUALS,
        CONTAINS,
        REGEX,
        EQUALS_IGNORING_CASE
    }

    public RadioButtonGroup() {
        super();
        selection = Selection.EQUALS;
    }

    public RadioButtonGroup(WebElement element) {
        super(element);
        selection = Selection.EQUALS;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    protected T getInstanceOfT(WebElement option) {
        T component = null;
        String error = "Failed to invoke constructor using reflection due to error:  ";

        try {
            component = ConstructorUtils.invokeConstructor(clazz, option);
        } catch (Exception ex) {
            error += ex.getMessage();
        }

        AssertJUtil.assertThat(component).as(error).isNotNull();
        return component;
    }

    private By getStaticLocator() {
        if (staticLocator == null) {
            staticLocator = LocatorUtils.processForSubstitutions(getLocator(), getSubstitutions());
        }

        return staticLocator;
    }

    protected Map<String, String> getSubstitutions() {
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

    protected List<WebElement> getOptions() {
        return Utils.until(ExpectedConditions.numberOfElementsToBeMoreThan(getStaticLocator(), 0));
    }

    protected boolean isMatchedOption(String displayedText, String matchToData) {
        if (selection == Selection.CONTAINS) {
            return StringUtils.contains(displayedText, matchToData);
        } else if (selection == Selection.REGEX) {
            return StringUtils.defaultString(displayedText).matches(matchToData);
        } else if (selection == Selection.EQUALS_IGNORING_CASE) {
            return StringUtils.equalsIgnoreCase(displayedText, matchToData);
        } else {
            return StringUtils.equals(displayedText, matchToData);
        }
    }

    @Override
    protected void init() {
        //
    }

    @SuppressWarnings("squid:S2259")
    @Override
    public void setValue() {
        List<WebElement> options = getOptions();
        String theData = getData();
        for (WebElement option : options) {
            T component = getInstanceOfT(option);
            if (isMatchedOption(component.getText(), theData)) {
                if (component.isSelected()) {
                    return;
                }

                AssertJUtil.assertThat(component.isEnabled()).as("Radio Option was disabled").isTrue();
                component.setValue();
                return;
            }
        }

        AssertJUtil.fail("Could not find radio option:  " + theData);
    }

    @SuppressWarnings("squid:S2259")
    @Override
    public String getValue() {
        List<WebElement> options = getOptions();
        for (WebElement option : options) {
            T component = getInstanceOfT(option);
            if (component.isSelected()) {
                return component.getText();
            }
        }

        AssertJUtil.fail("No Radio Button Group option was selected");
        return null;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        String actual = getValue();
        String expected = validationMethod.getData(this);

        String error = "Radio Button Group Validation";
        if (selection == Selection.CONTAINS) {
            AssertJUtil.assertThat(actual).as(error).contains(expected);
        } else if (selection == Selection.REGEX) {
            AssertJUtil.assertThat(actual).as(error).matches(expected);
        } else if (selection == Selection.EQUALS_IGNORING_CASE) {
            AssertJUtil.assertThat(actual).as(error).isEqualToIgnoringCase(expected);
        } else {
            AssertJUtil.assertThat(actual).as(error).isEqualTo(expected);
        }
    }

    public List<T> getAllOptions() {
        List<T> all = new ArrayList<>();

        List<WebElement> options = getOptions();
        for (WebElement option : options) {
            T component = getInstanceOfT(option);
            all.add(component);
        }

        return all;
    }

}
