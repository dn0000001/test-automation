package com.automation.common.ui.app.components;

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

import static com.taf.automation.ui.support.util.AssertsUtil.matchesRegex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;

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

    private T getInstanceOfT(WebElement option) {
        T component = null;
        String error = "Failed to invoke constructor using reflection due to error:  ";

        try {
            component = ConstructorUtils.invokeConstructor(clazz, option);
        } catch (Exception ex) {
            error += ex.getMessage();
        }

        assertThat(error, component, notNullValue());
        return component;
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

    private List<WebElement> getOptions() {
        return Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(getStaticLocator(), 0));
    }

    private boolean isMatchedOption(String displayedText, String matchToData) {
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
                component.setValue();
                return;
            }
        }
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

        assertThat("No Radio Button Group option was selected", false);
        return null;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        String actual = getValue();
        String expected = validationMethod.getData(this);

        String error = "Radio Button Group Validation";
        if (selection == Selection.CONTAINS) {
            assertThat(error, actual, containsString(expected));
        } else if (selection == Selection.REGEX) {
            assertThat(error, actual, matchesRegex(expected));
        } else if (selection == Selection.EQUALS_IGNORING_CASE) {
            assertThat(error, actual, equalToIgnoringCase(expected));
        } else {
            assertThat(error, actual, equalTo(expected));
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
