package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.HerokuappDataTable1Page;
import com.automation.common.ui.app.pageObjects.HerokuappDataTablesPage;
import com.automation.common.ui.app.pageObjects.HerokuappRow;
import com.automation.common.ui.app.pageObjects.HerokuappRowTable1;
import com.taf.automation.api.JsonUtils;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Testing Dynamic Locators in a page that represents a row in a table
 */
public class HerokuappDataTableEqualsTest extends TestNGBase {
    @SuppressWarnings({"squid:S1068", "squid:S1206"})
    private class Table {
        private String lastName;
        private String firstName;
        private String email;
        private String dues;
        private String website;

        @Override
        public boolean equals(Object object) {
            List<String> excludeFields = new ArrayList<>();
            return Utils.equals(this, object, excludeFields);
        }

        @Override
        public String toString() {
            return JsonUtils.getGson().toJson(this);
        }

    }

    @Features("Framework")
    @Stories("Page Object with Dynamic Locators used to represent a row in a table")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("url")
    @Test
    public void verifyTable1EqualsTable2Test(@Optional("https://the-internet.herokuapp.com/tables") String url) {
        getContext().getDriver().get(url);

        List<Table> table2Rows = new ArrayList<>();
        HerokuappDataTablesPage table2 = new HerokuappDataTablesPage(getContext());
        List<HerokuappRow> all = table2.getAllRows();
        for (HerokuappRow row : all) {
            Table actual = new Table();
            actual.lastName = row.getLastName();
            actual.firstName = row.getFirstName();
            actual.email = row.getEmail();
            actual.dues = row.getDues();
            actual.website = row.getWebsite();
            table2Rows.add(actual);
        }

        List<Table> table1Rows = new ArrayList<>();
        HerokuappDataTable1Page table1 = new HerokuappDataTable1Page(getContext());
        List<HerokuappRowTable1> allTable1Rows = table1.getAllRows();
        for (HerokuappRowTable1 row : allTable1Rows) {
            Table actual = new Table();
            actual.lastName = row.getLastName();
            actual.firstName = row.getFirstName();
            actual.email = row.getEmail();
            actual.dues = row.getDues();
            actual.website = row.getWebsite();
            table1Rows.add(actual);
        }

        assertThat("Table 1 == Table 2", table2Rows, equalTo(table1Rows));
    }

}
