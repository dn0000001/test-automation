package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.taf.automation.ui.support.util.AssertsUtil.matchesRegex;
import static com.taf.automation.ui.support.util.AssertsUtil.range;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Enhanced version of SelectComponent for more flexibility with drop downs<BR>
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
public class SelectEnhanced extends PageComponent {
    private static final String SPLIT_ON = ">>>";
    private static final String SPLIT_ON_RANGE = ":";
    private static final String SPLIT_ON_LIST = ",";
    private static final String VALUE = "value";

    /**
     * Drop Down selection options
     */
    protected enum Selection {
        VISIBLE_TEXT,
        VALUE_HTML,
        INDEX,
        VISIBLE_TEXT_REGEX,
        VALUE_HTML_REGEX,
        RANDOM_INDEX,
        RANDOM_INDEX_VALUES,
        RANDOM_INDEX_RANGE;

        /**
         * Convert string to Selection enum
         *
         * @param value         - Value to convert
         * @param defaultOption - Default Option if no match
         * @return Selection
         */
        public static Selection to(String value, Selection defaultOption) {
            if (value == null || value.equals("")) {
                return defaultOption;
            }

            for (Selection option : Selection.values()) {
                if (value.equalsIgnoreCase(option.toString())) {
                    return option;
                }
            }

            return defaultOption;
        }

    }

    @XStreamOmitField
    protected Select select; // Drop down element

    @XStreamOmitField
    private Selection selection; // How to select drop down value

    @XStreamOmitField
    protected String rawSelectionData; // If applicable, the value to selected as a String

    @XStreamOmitField
    private int minRandomIndex; // If applicable, minimum random index to use

    @XStreamOmitField
    private int maxRandomIndex; // If applicable, maximum random index to use

    @XStreamOmitField
    private List<Integer> randomIndexValues; // If applicable, list of random index values to use

    @XStreamOmitField
    private int selectedIndex;  // If applicable, the index of the selected drop down option

    @XStreamOmitField
    private RetryPolicy<Object> retryPolicy;

    /**
     * Get how to select the drop down option.  Also, initializes the fields that will be used to select the drop down
     * option.<BR>
     * <B>Notes:</B> <BR>
     * 1)  Use this method to always get the selection variable as it ensures that it is initialized.<BR>
     * 2) The reason for initializing the fields used to select the drop down here is because the initialization of the
     * selection variable requires parsing of the data as it is more efficient to parse out all the fields at once.<BR>
     *
     * @return Selection
     */
    protected Selection getSelection() {
        if (selection == null) {
            parseDataAndInitializeFields();
        }

        return selection;
    }

    @SuppressWarnings({"squid:S3776", "squid:S1871"})
    private void parseDataAndInitializeFields() {
        String[] pieces = StringUtils.splitByWholeSeparator(getData(), SPLIT_ON, 2);
        if (pieces == null) {
            selection = Selection.VISIBLE_TEXT;
            rawSelectionData = "";
        } else if (pieces.length == 1 && !StringUtils.contains(getData(), SPLIT_ON)) {
            // Assume that if no separator that user just specified drop down option to be selected by visible text
            selection = Selection.VISIBLE_TEXT;
            rawSelectionData = pieces[0].trim();
        } else if (pieces.length == 1 && StringUtils.startsWith(getData().trim(), SPLIT_ON)) {
            // The data looks like ">>> option"
            selection = Selection.VISIBLE_TEXT;
            rawSelectionData = pieces[0].trim();
        } else if (pieces.length == 1 && StringUtils.endsWith(getData().trim(), SPLIT_ON)) {
            // The data looks like "option >>>"
            selection = Selection.to(pieces[0].trim(), Selection.VISIBLE_TEXT);
            rawSelectionData = "";
        } else {
            selection = Selection.to(pieces[0].trim(), Selection.VISIBLE_TEXT);
            rawSelectionData = pieces[1].trim();
        }

        minRandomIndex = 0;
        maxRandomIndex = -1;
        randomIndexValues = new ArrayList<>();
        selectedIndex = -1;

        if (selection == Selection.RANDOM_INDEX) {
            minRandomIndex = NumberUtils.toInt(rawSelectionData, 0);
        }

        if (selection == Selection.RANDOM_INDEX_VALUES) {
            String[] all = StringUtils.split(rawSelectionData, SPLIT_ON_LIST);
            for (String randomIndexValue : all) {
                randomIndexValues.add(Integer.parseInt(randomIndexValue.trim()));
            }
        }

        if (selection == Selection.RANDOM_INDEX_RANGE) {
            String[] all = StringUtils.split(rawSelectionData, SPLIT_ON_RANGE, 2);
            if (all.length == 1 && StringUtils.startsWith(rawSelectionData, SPLIT_ON_RANGE)) {
                maxRandomIndex = Integer.parseInt(all[0].trim());
            } else if (all.length == 1 && StringUtils.endsWith(rawSelectionData, SPLIT_ON_RANGE)) {
                minRandomIndex = Integer.parseInt(all[0].trim());
            } else if (all.length == 2) {
                minRandomIndex = Integer.parseInt(all[0].trim());
                maxRandomIndex = Integer.parseInt(all[1].trim());
            }
        }
    }

    public SelectEnhanced() {
        super();
    }

    public SelectEnhanced(WebElement element) {
        super(element);
    }

    protected RetryPolicy<Object> getRetryPolicy() {
        if (retryPolicy == null) {
            return Utils.getPollingRetryPolicy();
        }

        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy<Object> retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public void useNegativeRetryPolicy() {
        setRetryPolicy(Utils.getPollingRetryPolicy(TestProperties.getInstance().getNegativeTimeout()));
    }

    @Override
    protected void init() {
        select = new Select(coreElement);
    }

    @Override
    public void initializeData(String data, String initialData, String expectedData) {
        selection = null;  // Trigger parsing and initialization
        super.initializeData(data, initialData, expectedData);
    }

    @Override
    public void setValue() {
        Failsafe.with(getRetryPolicy()).run(this::setValueAttempt);
    }

    private void setValueAttempt() {
        // Selenium allows disabled drop downs to still be set.
        // So, to prevent this we will assert that it is enabled before setting the value
        assertThat("Select was disabled", isEnabled());

        if (getSelection() == Selection.VISIBLE_TEXT) {
            setValueUsingVisibleText();
            return;
        }

        if (getSelection() == Selection.VALUE_HTML) {
            setValueUsingValue();
            return;
        }

        if (getSelection() == Selection.INDEX
                || getSelection() == Selection.RANDOM_INDEX
                || getSelection() == Selection.RANDOM_INDEX_RANGE
                || getSelection() == Selection.RANDOM_INDEX_VALUES
        ) {
            setValueUsingIndex();
            return;
        }

        if (getSelection() == Selection.VISIBLE_TEXT_REGEX) {
            setValueUsingTextRegEx();
            return;
        }

        if (getSelection() == Selection.VALUE_HTML_REGEX) {
            setValueUsingHtmlRegEx();
            return;
        }

        assertThat("Unsupported Selection:  " + getSelection(), false);
    }

    private boolean isMatch(WebElement option, String value) {
        if (getSelection() == Selection.VISIBLE_TEXT) {
            String optionText = option.getText();
            return StringUtils.equals(optionText, value)
                    || StringUtils.equals(StringUtils.normalizeSpace(optionText), value);
        }

        if (getSelection() == Selection.INDEX
                || getSelection() == Selection.RANDOM_INDEX
                || getSelection() == Selection.RANDOM_INDEX_RANGE
                || getSelection() == Selection.RANDOM_INDEX_VALUES
        ) {
            return value.equals(option.getAttribute("index"));
        }

        if (getSelection() == Selection.VALUE_HTML) {
            return StringUtils.equals(option.getAttribute(VALUE), value);
        }

        if (getSelection() == Selection.VISIBLE_TEXT_REGEX) {
            return option.getText().matches(value);
        }

        if (getSelection() == Selection.VALUE_HTML_REGEX) {
            return StringUtils.defaultString(option.getAttribute(VALUE)).matches(value);
        }

        return false;
    }

    private void makeOptionSelected(WebElement option, String reason) {
        if (!option.isSelected()) {
            assertThat(reason, option.isEnabled());
            option.click();
        }
    }

    private void genericSetValueUsing(String disabledReason, String notFoundReason, String compareAgainst) {
        int index = -1;
        List<WebElement> all = select.getOptions();
        for (int i = 0; i < all.size(); i++) {
            WebElement option = all.get(i);
            if (isMatch(option, compareAgainst)) {
                makeOptionSelected(option, disabledReason);
                index = i;
                break;
            }
        }

        assertThat(notFoundReason, index, greaterThanOrEqualTo(0));
        selectedIndex = index;
    }

    private void setValueUsingVisibleText() {
        String disabledReason = "Disabled Option found using visible text:  " + rawSelectionData;
        String notFoundReason = "Cannot locate element with text:  " + rawSelectionData;
        genericSetValueUsing(disabledReason, notFoundReason, rawSelectionData);
    }

    private void setValueUsingValue() {
        String disabledReason = "Disabled Option found using value:  " + rawSelectionData;
        String notFoundReason = "Cannot locate element with value:  " + rawSelectionData;
        genericSetValueUsing(disabledReason, notFoundReason, rawSelectionData);
    }

    private void setValueUsingIndex() {
        int index = -1;
        List<WebElement> all = select.getOptions();

        if (getSelection() == Selection.INDEX) {
            index = NumberUtils.toInt(rawSelectionData, -1);
            assertThat("Invalid Index:  " + rawSelectionData, index, greaterThanOrEqualTo(0));
        } else if (getSelection() == Selection.RANDOM_INDEX || getSelection() == Selection.RANDOM_INDEX_RANGE) {
            int max = (maxRandomIndex > 0) ? maxRandomIndex : all.size();
            index = Rand.randomRange(minRandomIndex, max - 1);
        } else if (getSelection() == Selection.RANDOM_INDEX_VALUES) {
            assertThat("No Random Index Values Specified", randomIndexValues.size(), greaterThan(0));
            Collections.shuffle(randomIndexValues);
            index = randomIndexValues.get(0);
        } else {
            assertThat("Unsupported Index Selection Option:  " + getSelection(), false);
        }

        assertThat("Invalid Index", index, range(0, all.size() - 1));
        String disabledReason = "Disabled Option found index:  " + index;
        String notFoundReason = "Cannot locate option with index:  " + index;
        genericSetValueUsing(disabledReason, notFoundReason, "" + index);
    }

    private void setValueUsingTextRegEx() {
        String disabledReason = "Disabled Option found using regex on visible text:  " + rawSelectionData;
        String notFoundReason = "Could not find match using regex on visible text:  " + rawSelectionData;
        genericSetValueUsing(disabledReason, notFoundReason, rawSelectionData);
    }

    private void setValueUsingHtmlRegEx() {
        String disabledReason = "Disabled Option found using regex on html value:  " + rawSelectionData;
        String notFoundReason = "Could not find match using regex on html value:  " + rawSelectionData;
        genericSetValueUsing(disabledReason, notFoundReason, rawSelectionData);
    }

    @Override
    public String getValue() {
        return StringUtils.defaultString(select.getFirstSelectedOption().getText()).trim();
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        //
        // Find the 1st selected drop down option and store the visible text, html value and index of the option
        // for verification later.
        //
        String actualVisibleText = null;
        String actualHtmlValue = null;
        int actualIndex = -1;

        List<WebElement> all = select.getOptions();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).isSelected()) {
                actualVisibleText = all.get(i).getText();
                actualHtmlValue = all.get(i).getAttribute(VALUE);
                actualIndex = i;
                break;
            }
        }

        assertThat("Could not find index of a selected drop down option", actualIndex, greaterThanOrEqualTo(0));
        assertThat("Could not find visible text of a selected drop down option", actualVisibleText, notNullValue());
        assertThat("Could not find html value of a selected drop down option", actualHtmlValue, notNullValue());

        // Assume that expected information is from DataTypes.Data
        String expectedData = rawSelectionData;
        int expectedIndex = selectedIndex;

        if (validationMethod == DataTypes.Expected) {
            expectedData = StringUtils.defaultString(validationMethod.getData(this), expectedData);
            if (getSelection() == Selection.INDEX
                    || getSelection() == Selection.RANDOM_INDEX
                    || getSelection() == Selection.RANDOM_INDEX_RANGE
                    || getSelection() == Selection.RANDOM_INDEX_VALUES) {
                expectedIndex = NumberUtils.toInt(expectedData, expectedIndex);
            }
        }

        //
        // Verify the actual selected drop down option matches the one previously selected
        //
        if (getSelection() == Selection.VISIBLE_TEXT) {
            assertThat("Visible Text of currently selected drop down option", actualVisibleText, equalTo(expectedData));
        } else if (getSelection() == Selection.VALUE_HTML) {
            assertThat("HTML Value of currently selected drop down option", actualHtmlValue, equalTo(expectedData));
        } else if (getSelection() == Selection.INDEX
                || getSelection() == Selection.RANDOM_INDEX
                || getSelection() == Selection.RANDOM_INDEX_RANGE
                || getSelection() == Selection.RANDOM_INDEX_VALUES) {
            assertThat("Index of currently selected drop down option", actualIndex, equalTo(expectedIndex));
        } else if (getSelection() == Selection.VISIBLE_TEXT_REGEX) {
            assertThat("Visible Text of currently selected drop down option", actualVisibleText, matchesRegex(expectedData));
        } else if (getSelection() == Selection.VALUE_HTML_REGEX) {
            assertThat("HTML Value of currently selected drop down option", actualHtmlValue, matchesRegex(expectedData));
        } else {
            assertThat("Unsupported Selection:  " + getSelection(), false);
        }
    }

    /**
     * @return Get all currently available options that are enabled
     */
    public List<String> getAllOptions() {
        List<String> all = new ArrayList<>();

        for (WebElement option : select.getOptions()) {
            if (option.isEnabled()) {
                all.add(StringUtils.trimToEmpty(option.getText()));
            }
        }

        return all;
    }

}
