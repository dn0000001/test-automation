package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.LocatorUtils;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ui.auto.core.data.DataTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Version of SelectEnhanced that handles AJAX<BR>
 * <B>Data Formats:</B><BR>
 * <OL>
 * <LI>For non-random options:  {Selection} &gt;&gt;&gt; {Value}</LI>
 * <LI>For random option starting from min value:  RANDOM_INDEX &gt;&gt;&gt; {Minimum Index}</LI>
 * <LI>For random option from specified list:  RANDOM_INDEX_VALUES &gt;&gt;&gt; {Index 1},{Index 2}, ..., {Index N}</LI>
 * <LI>For random option in range:  RANDOM_INDEX_RANGE &gt;&gt;&gt; {Minimum Index Inclusive}:{Maximum Index Exclusive}</LI>
 * </OL>
 * <B>Examples:</B><BR>
 * <OL>
 * <LI>Select drop down option using visible text of "option 1":  <B>option 1</B></LI>
 * <LI>Select drop down option using visible text of "option 2":  <B>VISIBLE_TEXT &gt;&gt;&gt; option 2</B></LI>
 * <LI>Select drop down option using the HTML attribute value of "option 3":  <B>VALUE_HTML &gt;&gt;&gt; option 3</B></LI>
 * <LI>Select drop down option at index 4 (provided there are at least 5 drop down options):  <B>INDEX &gt;&gt;&gt; 4</B></LI>
 * <LI>Select drop down option using regular expression to match visible text of "option 5":   <B>VISIBLE_TEXT_REGEX &gt;&gt;&gt; .*5.*</B></LI>
 * <LI>Select drop down option using regular expression to match the HTML attribute value of "option 6":  <B>VALUE_HTML_REGEX &gt;&gt;&gt; .*6.*</B></LI>
 * <LI>Select a random option:  <B>RANDOM_INDEX &gt;&gt;&gt; 0</B></LI>
 * <LI>Select a random option from index 1:  <B>RANDOM_INDEX &gt;&gt;&gt; 1</B></LI>
 * <LI>Select a random option from the list of indexes {1, 3, 5}:  <B>RANDOM_INDEX_VALUES &gt;&gt;&gt; 1,3,5</B></LI>
 * <LI>Select a random option from the list of consecutive indexes {1,2,3}:  <B>RANDOM_INDEX_RANGE &gt;&gt;&gt; 1:4</B></LI>
 * </OL>
 */
public class SelectEnhancedAJAX extends SelectEnhanced {
    private WebDriver driver;
    private Map<String, String> substitutions;
    private By staticLocator;

    public SelectEnhancedAJAX() {
        super();
    }

    public SelectEnhancedAJAX(WebElement element) {
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

    private void initSelect() {
        select = new Select(getDriver().findElement(getStaticLocator()));
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
    public String getValue() {
        initSelect();
        return super.getValue();
    }

    @Override
    public void setValue() {
        initSelect();
        boolean ajax = triggersAJAX();
        WebElement element = (ajax) ? getDriver().findElement(getStaticLocator()) : null;
        super.setValue();
        if (ajax) {
            Utils.getWebDriverWait().until(ExpectedConditions.stalenessOf(element));
        }
    }

    private boolean triggersAJAX() {
        String currentVisibleText = null;
        String currentHtmlValue = null;
        int currentIndex = -1;

        List<WebElement> all = select.getOptions();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).isSelected()) {
                currentVisibleText = all.get(i).getText();
                currentHtmlValue = all.get(i).getAttribute("value");
                currentIndex = i;
                break;
            }
        }

        assertThat("Could not find index of a selected drop down option", currentIndex, greaterThanOrEqualTo(0));
        assertThat("Could not find visible text of a selected drop down option", currentVisibleText, notNullValue());
        assertThat("Could not find html value of a selected drop down option", currentHtmlValue, notNullValue());

        boolean noChange;
        if (getSelection() == Selection.VISIBLE_TEXT) {
            noChange = StringUtils.equals(currentVisibleText, rawSelectionData);
        } else if (getSelection() == Selection.VALUE_HTML) {
            noChange = StringUtils.equals(currentHtmlValue, rawSelectionData);
        } else if (getSelection() == Selection.INDEX
                || getSelection() == Selection.RANDOM_INDEX
                || getSelection() == Selection.RANDOM_INDEX_RANGE
                || getSelection() == Selection.RANDOM_INDEX_VALUES) {
            noChange = currentIndex == NumberUtils.toInt(rawSelectionData, -1);
        } else if (getSelection() == Selection.VISIBLE_TEXT_REGEX) {
            noChange = StringUtils.defaultString(currentVisibleText).matches(rawSelectionData);
        } else if (getSelection() == Selection.VALUE_HTML_REGEX) {
            noChange = StringUtils.defaultString(currentHtmlValue).matches(rawSelectionData);
        } else {
            noChange = false;
        }

        return !noChange;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        initSelect();
        super.validateData(validationMethod);
    }

}
