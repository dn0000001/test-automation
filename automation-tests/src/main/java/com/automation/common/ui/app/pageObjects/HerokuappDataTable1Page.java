package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.domainObjects.HerokuappColumnMapping;
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
import java.util.Map;

import static com.taf.automation.ui.support.AssertsUtil.range;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Herokuapp Data Tables Page<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 */
public class HerokuappDataTable1Page extends PageObjectV2 {
    private static final By TABLE1_ROWS = By.cssSelector("#table1 tbody tr");
    private List<HerokuappRowTable1> cached;

    public HerokuappDataTable1Page() {
        super();
    }

    public HerokuappDataTable1Page(TestContext context) {
        super(context);
    }

    public HerokuappRowTable1 getRow(int index) {
        List<HerokuappRowTable1> all = getAllRows();
        assertThat("Invalid Index", index, range(0, all.size() - 1));
        return all.get(index);
    }

    public List<HerokuappRowTable1> getAllRows() {
        if (cached != null) {
            return cached;
        }

        List<HerokuappRowTable1> all = new ArrayList<>();

        HerokuappColumnPositionsExtractor extractor = new HerokuappColumnPositionsExtractor();
        Map<String, String> columnPositions = extractor.getMap();
        Map<String, String> substitutions = extractor.getSubstitutions(HerokuappColumnMapping.LAST_NAME, columnPositions);

        String randomBaseValue = Rand.letters(10);
        List<WebElement> rows = Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE1_ROWS, 0));
        for (int i = 0; i < rows.size(); i++) {
            String randomIdValue = randomBaseValue + i;
            JsUtils.addAttributeId(rows.get(i), randomIdValue);

            HerokuappRowTable1 row = new HerokuappRowTable1();
            row.updateRowIdKey(randomIdValue);
            row.updateSubstitutions(substitutions);
            row.initPage(getContext());
            all.add(row);
        }

        return all;
    }

    public HerokuappDataTable1Page reset() {
        cached = null;
        return this;
    }

}
