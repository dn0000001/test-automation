package com.taf.automation.ui.app.components;

import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.Utils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.taf.automation.ui.support.AssertsUtil.matchesRegex;
import static com.taf.automation.ui.support.AssertsUtil.range;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Enhanced version of SelectComponent for more flexibility with drop downs<BR>
 * <B>Data Formats:</B><BR>
 * <OL>
 * <LI>For non-random options:  {Selection} >>> {Value}</LI>
 * <LI>For random option starting from min value:  RANDOM_INDEX >>> {Minimum Index}</LI>
 * <LI>For random option from specified list:  RANDOM_INDEX_VALUES >>> {Index 1},{Index 2}, ..., {Index N}</LI>
 * <LI>For random option in range:  RANDOM_INDEX_RANGE >>> {Minimum Index Inclusive}:{Maximum Index Exclusive}</LI>
 * </OL>
 * <B>Examples:</B><BR>
 * <OL>
 * <LI>Select drop down option using visible text of "option 1":  <B>option 1</B></LI>
 * <LI>Select drop down option using visible text of "option 2":  <B>VISIBLE_TEXT >>> option 2</B></LI>
 * <LI>Select drop down option using the HTML attribute value of "option 3":  <B>VALUE_HTML >>> option 3</B></LI>
 * <LI>Select drop down option at index 4 (provided there are at least 5 drop down options):  <B>INDEX >>> 4</B></LI>
 * <LI>Select drop down option using regular expression to match visible text of "option 5":   <B>VISIBLE_TEXT_REGEX >>> .*5.*</B></LI>
 * <LI>Select drop down option using regular expression to match the HTML attribute value of "option 6":  <B>VALUE_HTML_REGEX >>> .*6.*</B></LI>
 * <LI>Select a random option:  <B>RANDOM_INDEX >>> 0</B></LI>
 * <LI>Select a random option from index 1:  <B>RANDOM_INDEX >>> 1</B></LI>
 * <LI>Select a random option from the list of indexes {1, 3, 5}:  <B>RANDOM_INDEX_VALUES >>> 1,3,5</B></LI>
 * <LI>Select a random option from the list of consecutive indexes {1,2,3}:  <B>RANDOM_INDEX_RANGE >>> 1:4</B></LI>
 * </OL>
 */
public class SelectEnhanced extends PageComponent {
    private static final String SPLIT_ON = ">>>";
    private static final String SPLIT_ON_RANGE = ":";
    private static final String SPLIT_ON_LIST = ",";

    /**
     * Drop Down selection options
     */
    private enum Selection {
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
        private static Selection to(String value, Selection defaultOption) {
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
    private Select select; // Drop down element

    @XStreamOmitField
    private Selection selection; // How to select drop down value

    @XStreamOmitField
    private String rawSelectionData; // If applicable, the value to selected as a String

    @XStreamOmitField
    private int minRandomIndex; // If applicable, minimum random index to use

    @XStreamOmitField
    private int maxRandomIndex; // If applicable, maximum random index to use

    @XStreamOmitField
    private List<Integer> randomIndexValues; // If applicable, list of random index values to use

    @XStreamOmitField
    private int selectedIndex;  // If applicable, the index of the selected drop down option

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
    private Selection getSelection() {
        if (selection == null) {
            parseDataAndInitializeFields();
        }

        return selection;
    }

    private void parseDataAndInitializeFields() {
        String[] pieces = StringUtils.split(getData(), SPLIT_ON, 2);
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
        //
    }

    public SelectEnhanced(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        select = new Select(coreElement);
    }

    @Override
    public void setValue() {
        Failsafe.with(Utils.getPollingRetryPolicy()).run(this::setValueAttempt);
    }

    private void setValueAttempt() {
        if (getSelection() == Selection.VISIBLE_TEXT) {
            select.selectByVisibleText(rawSelectionData);
        }

        if (getSelection() == Selection.VALUE_HTML) {
            select.selectByValue(rawSelectionData);
        }

        if (getSelection() == Selection.INDEX) {
            selectedIndex = Integer.parseInt(rawSelectionData);
            select.selectByIndex(selectedIndex);
        }

        if (getSelection() == Selection.VISIBLE_TEXT_REGEX) {
            int index = -1;
            List<WebElement> all = select.getOptions();
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getText().matches(rawSelectionData)) {
                    index = i;
                    break;
                }
            }

            assertThat("Could not find match using regex on visible text:  " + rawSelectionData, index, greaterThanOrEqualTo(0));
            selectedIndex = index;
            select.selectByIndex(index);
        }

        if (getSelection() == Selection.VALUE_HTML_REGEX) {
            int index = -1;
            List<WebElement> all = select.getOptions();
            for (int i = 0; i < all.size(); i++) {
                if (StringUtils.defaultString(all.get(i).getAttribute("value")).matches(rawSelectionData)) {
                    index = i;
                    break;
                }
            }

            assertThat("Could not find match using regex on html value:  " + rawSelectionData, index, greaterThanOrEqualTo(0));
            selectedIndex = index;
            select.selectByIndex(index);
        }

        if (getSelection() == Selection.RANDOM_INDEX || getSelection() == Selection.RANDOM_INDEX_RANGE) {
            List<WebElement> all = select.getOptions();
            int max = (maxRandomIndex > 0) ? maxRandomIndex : all.size();
            assertThat("Invalid Index", all.size(), range(minRandomIndex, max - 1));
            selectedIndex = Rand.randomRange(minRandomIndex, max - 1);
            select.selectByIndex(selectedIndex);
        }

        if (getSelection() == Selection.RANDOM_INDEX_VALUES) {
            assertThat("No Random Index Values Specified", randomIndexValues.size(), greaterThan(0));
            Collections.shuffle(randomIndexValues);
            selectedIndex = randomIndexValues.get(0);

            List<WebElement> all = select.getOptions();
            assertThat("Invalid Index", selectedIndex, range(0, all.size() - 1));
            select.selectByIndex(selectedIndex);
        }
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
                actualHtmlValue = all.get(i).getAttribute("value");
                actualIndex = i;
                break;
            }
        }

        assertThat("Could not find index of a selected drop down option", actualIndex, greaterThanOrEqualTo(0));
        assertThat("Could not find visible text of a selected drop down option", actualVisibleText, notNullValue());
        assertThat("Could not find html value of a selected drop down option", actualHtmlValue, notNullValue());

        //
        // Verify the actual selected drop down option matches the one previously selected
        //
        if (getSelection() == Selection.VISIBLE_TEXT) {
            assertThat("Visible Text of currently selected drop down option", actualVisibleText, equalTo(rawSelectionData));
        } else if (getSelection() == Selection.VALUE_HTML) {
            assertThat("HTML Value of currently selected drop down option", actualHtmlValue, equalTo(rawSelectionData));
        } else if (getSelection() == Selection.INDEX
                || getSelection() == Selection.RANDOM_INDEX
                || getSelection() == Selection.RANDOM_INDEX_RANGE
                || getSelection() == Selection.RANDOM_INDEX_VALUES) {
            assertThat("Index of currently selected drop down option", actualIndex, equalTo(selectedIndex));
        } else if (getSelection() == Selection.VISIBLE_TEXT_REGEX) {
            assertThat("Visible Text of currently selected drop down option", actualVisibleText, matchesRegex(rawSelectionData));
        } else if (getSelection() == Selection.VALUE_HTML_REGEX) {
            assertThat("HTML Value of currently selected drop down option", actualHtmlValue, matchesRegex(rawSelectionData));
        } else {
            assertThat("Unsupported Selection:  " + getSelection(), false);
        }
    }

}
