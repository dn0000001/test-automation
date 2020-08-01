package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.GenericTable;
import com.taf.automation.ui.support.util.JsUtils;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Herokuapp Data Tables Page<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class HerokuappDataTablesPage extends GenericTable<HerokuappRow> {
    private static final By TABLE2_ROWS = By.cssSelector("#table2 tbody tr");

    public HerokuappDataTablesPage() {
        super();
    }

    public HerokuappDataTablesPage(TestContext context) {
        super(context);
    }

    @Override
    protected By getAllRowsLocator() {
        return TABLE2_ROWS;
    }

    @Override
    protected By getNoRowsLocator() {
        return null;
    }

    @Override
    protected HerokuappRow getNewRowInstance() {
        return new HerokuappRow();
    }

    public HerokuappDataTablesPage resetRows() {
        resetTableRows();
        return this;
    }

    public List<HerokuappRow> getAllRows() {
        return getTableRows();
    }

    @Override
    protected String getAttributeToExtractRowKey() {
        // This is not necessary but it makes it clear the attribute we are adding to the element
        return JsUtils.ID;
    }

    @Override
    protected void addAttributeToRow(WebElement row, String attribute, String uniqueValue) {
        // We know these rows do not have IDs as such it is safe to add an ID
        JsUtils.addAttribute(row, attribute, uniqueValue);
    }

    /**
     * This method is just for testing purposes<BR>
     * <B>Note:  </B> Any time this method is called the rows must be reset as it changes the element ids
     *
     * @param index - Index of row to get
     * @return HerokuappRow
     */
    public HerokuappRow getRow(int index) {
        //
        // This is just for testing purposes.  Normally, you would just use getTableRows().get(index) as we already
        // have all the rows if this type of method was really necessary.
        //
        List<WebElement> rows = Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE2_ROWS, index));
        String randomIdValue = Rand.letters(10) + index;
        JsUtils.addAttributeId(rows.get(index), randomIdValue);

        HerokuappRow row = new HerokuappRow();
        row.updateRowKey(randomIdValue);
        row.initPage(getContext());
        return row;
    }

    @Override
    protected boolean isMatch(HerokuappRow actualRow, HerokuappRow rowToMatch) {
        return actualRow.isMatch(rowToMatch);
    }

}
