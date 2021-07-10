package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.CsvColumnMapping;
import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.apache.commons.csv.CSVRecord;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Iterator;

/**
 * Example test using a data provider that reads an excel (2013) file
 */
public class Excel2013Test extends TestNGBase {
    private Iterator<Object[]> records;

    @Parameters({"excel", "worksheet"})
    @BeforeTest
    public void setup(String excelDataSet, String worksheet) {
        records = CsvUtils.dataProvider(excelDataSet, worksheet, CsvColumnMapping.RUN.getColumnName(), false).iterator();
    }

    @DataProvider(name = "ExcelData", parallel = true)
    public Iterator<Object[]> dataProvider() {
        return records;
    }

    @Features("TestNG")
    @Stories("Excel Data Provider")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "ExcelData")
    public void runTestPermutation(CsvTestData csvTestData, ITestContext injectedContext) {
        CsvUtils.testRunner(this, injectedContext, () -> runWrapper(csvTestData));
    }

    private void runWrapper(CsvTestData csvTestData) {
        TNHC_DO tnhc = new TNHC_DO(getContext());
        CsvUtils.initializeDataAndAttach(tnhc, csvTestData);
        validateRecord("Player", csvTestData.getRecord(), CsvColumnMapping.PLAYER, tnhc.getLanding().getDataPlayerWithAliases());
        validateRecord("Team", csvTestData.getRecord(), CsvColumnMapping.TEAM, tnhc.getLanding().getDataTeamWithAliases());
    }

    @SuppressWarnings("java:S3252")
    @Step("Validate Excel data for {0} loaded properly into Page Object")
    private void validateRecord(String description, CSVRecord csv, ColumnMapper column, String pageObjectDataWithAliases) {
        String expectedWithAliases = CsvUtils.isNotBlank(csv, column) ? csv.get(column.getColumnName()) : null;
        AssertJUtil.assertThat(pageObjectDataWithAliases).as(description).isEqualTo(expectedWithAliases);
    }

}
