package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.JsUtils;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Herokuapp Data Tables Page<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 */
public class HerokuappDataTablesPage extends PageObjectV2 {
    private static final By TABLE2_ROWS = By.cssSelector("#table2 tbody tr");

    public HerokuappDataTablesPage() {
        super();
    }

    public HerokuappDataTablesPage(TestContext context) {
        super(context);
    }

    public HerokuappRow getRow(int index) {
        List<WebElement> rows = Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE2_ROWS, index));
        String randomIdValue = Rand.letters(10) + index;
        JsUtils.addAttributeId(rows.get(index), randomIdValue);

        HerokuappRow row = new HerokuappRow();
        row.updateRowIdKey(randomIdValue);
        row.initPage(getContext());
        return row;
    }

    public List<HerokuappRow> getAllRows() {
        List<HerokuappRow> all = new ArrayList<>();

        String randomBaseValue = Rand.letters(10);
        List<WebElement> rows = Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE2_ROWS, 0));
        for (int i = 0; i < rows.size(); i++) {
            String randomIdValue = randomBaseValue + i;
            JsUtils.addAttributeId(rows.get(i), randomIdValue);

            HerokuappRow row = new HerokuappRow();
            row.updateRowIdKey(randomIdValue);
            row.initPage(getContext());
            all.add(row);
        }

        return all;
    }

}
