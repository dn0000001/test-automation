package com.taf.automation.ui.support;

import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Generic class for representing a page containing a table
 */
public abstract class GenericTable<T extends GenericRow> extends PageObjectV2 {
    @XStreamOmitField
    private List<T> tableRows;

    public GenericTable() {
        super();
    }

    public GenericTable(TestContext context) {
        super(context);
    }

    protected GenericTable resetTableRows() {
        tableRows = null;
        return this;
    }

    protected List<T> getTableRows() {
        if (tableRows != null) {
            return tableRows;
        }

        tableRows = new ArrayList<>();
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forExpectedConditions(ExpectedConditions.numberOfElementsToBeMoreThan(getNoRowsLocator(), 0)));
        criteria.add(CriteriaMaker.forExpectedConditions(ExpectedConditions.numberOfElementsToBeMoreThan(getAllRowsLocator(), 0)));

        int result = Helper.assertThat(getDriver(), criteria);
        if (result == 0) {
            // There are no rows in the table
            return tableRows;
        }

        String randomBaseValue = getRandomBaseValue();
        Map<String, String> substitutions = getSubstitutions();
        List<WebElement> all = Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(getAllRowsLocator(), 0));
        for (int i = 0; i < all.size(); i++) {
            // If necessary, we will make the row unique
            String randomIdValue = randomBaseValue + i;
            addAttributeToRow(all.get(i), getAttributeToExtractRowKey(), randomIdValue);

            // Validate that the row is unique
            String rowID = getRowKey(all.get(i), getAttributeToExtractRowKey());
            assertThat("Row Index (" + i + ") missing " + getAttributeToExtractRowKey() + " attribute", rowID, not(isEmptyOrNullString()));

            T row = getNewRowInstance();
            row.updateRowKey(rowID);
            row.updateSubstitutions(substitutions);
            row.initPage(getContext());
            tableRows.add(row);
        }

        return tableRows;
    }

    /**
     * Find a row from the table using the given unique identifier. It is compared with the result of getRowIdentifier()
     * for each row to find a regular expression match.
     *
     * @param identifier - String that uniquely identifies the row we are looking for as regular expression
     * @return matching row (or assertion failure if a matching row cannot be found)
     */
    protected T findTableRow(String identifier) {
        return findTableRow(identifier, "");
    }

    /**
     * Find a row from the table using the given unique identifier. It is compared with the result of getRowIdentifier()
     * for each row to find a regular expression match.
     *
     * @param identifier - String that uniquely identifies the row we are looking for as regular expression
     * @param columnName - For logging purposes only
     * @return matching row (or assertion failure if a matching row cannot be found)
     */
    protected T findTableRow(String identifier, String columnName) {
        T match = null;
        for (T row : getTableRows()) {
            if (StringUtils.defaultString(row.getRowIdentifier()).matches(identifier)) {
                match = row;
                break;
            }
        }

        assertThat("Could not find Row with " + columnName + " matching regular expression:  " + identifier, match, notNullValue());
        return match;
    }

    /**
     * @return the attribute that will be used to extract the row key
     */
    protected String getAttributeToExtractRowKey() {
        // Normally, the id attribute will be unique if it exists
        return JsUtils.ID;
    }

    /**
     * @return Get a random base value
     */
    protected String getRandomBaseValue() {
        return Rand.letters(10);
    }

    /**
     * Add Attribute To Row
     *
     * @param row         - Element to add the attribute
     * @param attribute   - Attribute to be added to the element
     * @param uniqueValue - Unique value for the row such that it can be found later
     */
    protected void addAttributeToRow(WebElement row, String attribute, String uniqueValue) {
        if (StringUtils.equals(JsUtils.ID, attribute)) {
            // Do not add id attribute because this could break the page.
            return;
        }

        // Assuming it is safe to add the specified attribute
        JsUtils.addAttribute(row, attribute, uniqueValue);
    }

    /**
     * @param row       - Element to get key from
     * @param attribute - Attribute that contains the key
     * @return the row key from the element
     */
    protected String getRowKey(WebElement row, String attribute) {
        return row.getAttribute(attribute);
    }

    /**
     * Get additional substitutions.  This may be necessary to find the individual columns when the unique row
     * identifier is not enough.
     *
     * @return Map&lt;String, String&gt;
     */
    protected Map<String, String> getSubstitutions() {
        return new HashMap<>();
    }

    /**
     * Get a locator that should find all rows in the table <BR>
     * If NULL is returned, criteria looking for this will skip it without waiting for timeout
     *
     * @return By
     */
    protected abstract By getAllRowsLocator();

    /**
     * Get a locator that should find an element indicating there are no rows in the table <BR>
     * If NULL is returned, criteria looking for this will skip it without waiting for timeout
     *
     * @return By
     */
    protected abstract By getNoRowsLocator();

    /**
     * Create and return a new instance of a row to use with this table
     *
     * @return T
     */
    protected abstract T getNewRowInstance();
}
