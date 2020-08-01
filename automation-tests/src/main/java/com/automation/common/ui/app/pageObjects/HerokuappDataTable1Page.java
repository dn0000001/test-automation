package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.domainObjects.HerokuappColumnMapping;
import com.taf.automation.ui.support.GenericTable;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.By;

import java.util.List;
import java.util.Map;

import static com.taf.automation.ui.support.util.AssertsUtil.range;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Herokuapp Data Tables Page<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class HerokuappDataTable1Page extends GenericTable<HerokuappRowTable1> {
    private static final By TABLE1_ROWS = By.cssSelector("#table1 tbody tr");

    public HerokuappDataTable1Page() {
        super();
    }

    public HerokuappDataTable1Page(TestContext context) {
        super(context);
    }

    @Override
    protected By getAllRowsLocator() {
        return TABLE1_ROWS;
    }

    @Override
    protected By getNoRowsLocator() {
        return null;
    }

    @Override
    protected HerokuappRowTable1 getNewRowInstance() {
        return new HerokuappRowTable1();
    }

    @Override
    protected Map<String, String> getSubstitutions() {
        HerokuappColumnPositionsExtractor extractor = new HerokuappColumnPositionsExtractor();
        Map<String, String> columnPositions = extractor.getMap();
        return extractor.getSubstitutions(HerokuappColumnMapping.LAST_NAME, columnPositions);
    }

    @Override
    protected String getAttributeToExtractRowKey() {
        return "auto";
    }

    public HerokuappRowTable1 getRow(int index) {
        List<HerokuappRowTable1> all = getAllRows();
        assertThat("Invalid Index", index, range(0, all.size() - 1));
        return all.get(index);
    }

    public List<HerokuappRowTable1> getAllRows() {
        return getTableRows();
    }

    public HerokuappDataTable1Page reset() {
        resetTableRows();
        return this;
    }

}
