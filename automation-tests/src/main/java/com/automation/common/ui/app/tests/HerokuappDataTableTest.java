package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.HerokuappDataTablesPage;
import com.automation.common.ui.app.pageObjects.HerokuappRow;
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
public class HerokuappDataTableTest extends TestNGBase {
    @SuppressWarnings({"squid:S1068", "squid:S1206"})
    private class Table2 {
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
    public void getAllDataTest(@Optional("https://the-internet.herokuapp.com/tables") String url) {
        List<Table2> actualRows = new ArrayList<>();
        getContext().getDriver().get(url);
        HerokuappDataTablesPage tablesPage = new HerokuappDataTablesPage(getContext());
        List<HerokuappRow> all = tablesPage.getAllRows();
        for (HerokuappRow row : all) {
            row.validateLocatorsNotNull();
            Table2 actual = new Table2();
            actual.lastName = row.getLastName();
            actual.firstName = row.getFirstName();
            actual.email = row.getEmail();
            actual.dues = row.getDues();
            actual.website = row.getWebsite();
            actualRows.add(actual);
        }

        for (int i = 0; i < actualRows.size(); i++) {
            HerokuappRow row = tablesPage.getRow(i);

            Table2 expected = new Table2();
            expected.lastName = row.getLastName();
            expected.firstName = row.getFirstName();
            expected.email = row.getEmail();
            expected.dues = row.getDues();
            expected.website = row.getWebsite();

            assertThat("Row[" + i + "]", actualRows.get(i), equalTo(expected));
        }
    }

}
