package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.HerokuappDataTable1Page;
import com.automation.common.ui.app.pageObjects.HerokuappDataTablesPage;
import com.automation.common.ui.app.pageObjects.HerokuappRow;
import com.automation.common.ui.app.pageObjects.HerokuappRowTable1;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.api.JsonUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing Dynamic Locators in a page that represents a row in a table
 */
@SuppressWarnings("java:S3252")
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
    @Test
    public void verifyTable1EqualsTable2Test() {
        new Navigation(getContext()).toHerokuappTables(Utils.isCleanCookiesSupported());

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

        AssertJUtil.assertThat(table2Rows).as("Table 1 == Table 2").isEqualTo(table1Rows);
    }

}
