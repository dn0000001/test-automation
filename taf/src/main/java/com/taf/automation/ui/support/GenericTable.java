package com.taf.automation.ui.support;

import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.JsUtils;
import com.taf.automation.ui.support.util.Utils;
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
    private static final String NO_PAGINATION = "Pagination is not supported by this table";

    @XStreamOmitField
    private List<T> tableRows;

    public GenericTable() {
        super();
    }

    public GenericTable(TestContext context) {
        super(context);
    }

    protected GenericTable<T> resetTableRows() {
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
     * Find 1st row that matches the given row. Null/Empty data fields in the given row are ignored when matching.<BR>
     * <B>Notes:</B> The method isMatch needs to be implemented with the desired logic for matching rows as the
     * default implementation just causes an assertion failure.
     *
     * @param rowToMatch - Row to find in the table
     * @return a matching row
     */
    public T findTableRow(T rowToMatch) {
        return findTableRow(rowToMatch, true);
    }

    /**
     * Find 1st row that matches the given row. Null/Empty data fields in the given row are ignored when matching.<BR>
     * <B>Notes:</B> The method isMatch needs to be implemented with the desired logic for matching rows as the
     * default implementation just causes an assertion failure.
     *
     * @param rowToMatch - Row to find in the table
     * @param mustExist  - true to expect a match and fail if no match
     * @return a matching row
     */
    public T findTableRow(T rowToMatch, boolean mustExist) {
        assertThat("The row to match must be specified", rowToMatch, notNullValue());

        if (rowToMatch.getContext() == null) {
            rowToMatch.initPage(getContext());
        }

        T match = null;
        for (T row : getTableRows()) {
            if (isMatch(row, rowToMatch)) {
                match = row;
                break;
            }
        }

        if (mustExist) {
            assertThat("Could not find a matching row:  " + rowToMatch, match, notNullValue());
        }

        return match;
    }

    /**
     * Find 1st row that matches the given row. Null/Empty data fields in the given row are ignored when matching.<BR>
     * <B>Notes:</B>
     * <UL>
     * <LI>
     * The method <B>isMatch</B> needs to be implemented with the desired logic for matching rows as the
     * default implementation just causes an assertion failure.
     * </LI>
     * <LI>
     * The method <B>getMaxIterations</B> needs to be implemented to return the max iterations/pages to search
     * to prevent an infinite loop. The default implementation just causes an assertion failure.
     * </LI>
     * <LI>
     * The method <B>isNextPage</B> needs to be implemented to return whether there is a next page to search.
     * The default implementation just causes an assertion failure.
     * </LI>
     * <LI>
     * The method <B>isPreviousPage</B> needs to be implemented to return whether there is a previous page to search.
     * The default implementation just causes an assertion failure.
     * </LI>
     * <LI>
     * The method <B>clickNextPage</B>needs to be implemented with the logic to go to the next page.
     * The default implementation just causes an assertion failure.
     * </LI>
     * <LI>
     * The method <B>clickPreviousPage</B> needs to be implemented with the logic to go to the previous page.
     * The default implementation just causes an assertion failure.
     * </LI>
     * <LI>
     * If you want want to support searching a specific direction, then only the methods in that direction need to
     * be implemented.
     * </LI>
     * </UL>
     *
     * @param rowToMatch - Row to find in the table
     * @param mustExist  - true to expect a match and fail if no match
     * @param allPages   - true to search all pages from the current page
     * @param next       - true to search in the forward (next) direction, false to search in the backward (previous) direction
     * @return a matching row
     */
    protected T findTableRow(T rowToMatch, boolean mustExist, boolean allPages, boolean next) {
        T match = findTableRow(rowToMatch, false);
        if (allPages) {
            int counter = 0;
            while (match == null && isAnotherPage(next) && counter < getMaxIterations()) {
                moveToAnotherPage(next);
                resetTableRows();
                match = findTableRow(rowToMatch, false);
                counter++;
            }
        }

        if (mustExist) {
            assertThat("Could not find a matching row:  " + rowToMatch, match, notNullValue());
        }

        return match;
    }

    /**
     * @return the max iterations to prevent an infinite loop
     */
    protected int getMaxIterations() {
        assertThat(NO_PAGINATION, false);
        return 0;
    }

    /**
     * Determine if there is another page
     *
     * @param next - true to check for next page, false to check for previous page
     * @return true the is another (next or previous) page
     */
    protected boolean isAnotherPage(boolean next) {
        if (next) {
            return isNextPage();
        } else {
            return isPreviousPage();
        }
    }

    /**
     * @return true if there is a next page else false
     */
    protected boolean isNextPage() {
        assertThat(NO_PAGINATION, false);
        return false;
    }

    /**
     * @return true if there is a previous page else false
     */
    @SuppressWarnings("java:S4144")
    protected boolean isPreviousPage() {
        assertThat(NO_PAGINATION, false);
        return false;
    }

    /**
     * Move to another page in the sequence (next/previous) and wait for the page to be loaded
     *
     * @param next - true to go to the next page, false to go to the previous page
     */
    protected void moveToAnotherPage(boolean next) {
        if (next) {
            clickNextPage();
        } else {
            clickPreviousPage();
        }
    }

    /**
     * Click Next Page which needs to take the actions to move to the next page and wait for the page to be loaded
     */
    protected void clickNextPage() {
        assertThat(NO_PAGINATION, false);
    }

    /**
     * Click Previous Page which needs to take the actions to move to the next page and wait for the page to be loaded
     */
    protected void clickPreviousPage() {
        assertThat(NO_PAGINATION, false);
    }

    /**
     * Method to determine if 2 rows are considered a match<BR>
     * <B>Note: </B> This needs to be implemented in the extending class to be able to match on the entire row.
     *
     * @param actualRow  - Actual page row from the table
     * @param rowToMatch - Target row to be matched
     * @return whether they match
     */
    protected boolean isMatch(T actualRow, T rowToMatch) {
        assertThat("Implementation does not support entire row matching", false);
        return false;
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
