package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.CSV_DO;
import com.automation.common.ui.app.domainObjects.CsvColumnMapping;
import com.automation.common.ui.app.pageObjects.FakeLoginPage;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Iterator;
import java.util.List;

/**
 * Example test using a data provider that reads a csv file with lists
 */
public class CsvListTest extends TestNGBase {
    private Iterator<Object[]> records;

    @Parameters("csv")
    @BeforeTest
    public void setup(@Optional("data/ui/list-testing.csv") String csvDataSet) {
        records = CsvUtils.dataProvider(csvDataSet, null, CsvColumnMapping.RUN.getColumnName(), false).iterator();
    }

    @DataProvider(name = "CSV_Data", parallel = true)
    public Iterator<Object[]> dataProvider() {
        return records;
    }

    @Features("TestNG")
    @Stories("CSV Data Provider")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "CSV_Data")
    public void runTestPermutation(CsvTestData csvTestData, ITestContext injectedContext) {
        CsvUtils.testRunner(this, injectedContext, () -> runWrapper(csvTestData));
    }

    private void runWrapper(CsvTestData csvTestData) {
        CSV_DO csvDO = new CSV_DO(getContext());
        CsvUtils.initializeDataAndAttach(csvDO, csvTestData);
        String description = csvTestData.getRecord().get(CsvColumnMapping.DESCRIPTION.getColumnName());
        output(description, csvDO);
    }

    @Step("Test Description:  {0}")
    private void output(String description, CSV_DO csvDO) {
        List<FakeLoginPage> users = csvDO.getUserLogins();
        List<FakeLoginPage> admins = csvDO.getAdminLogins();

        output("Users List", users.size());
        for (FakeLoginPage item : users) {
            output(item.getEmailData(), item.getPasswordData());
        }

        output("Admins List", admins.size());
        for (FakeLoginPage item : admins) {
            output(item.getEmailData(), item.getPasswordData());
        }
    }

    @Step("{0} had size of {1}")
    private void output(String listName, int size) {
        //
    }

    @Step("Email={0}; Password={1}")
    private void output(String email, String password) {
        //
    }

}
