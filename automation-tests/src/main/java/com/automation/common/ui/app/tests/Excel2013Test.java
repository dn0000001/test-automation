package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.CsvColumnMapping;
import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
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

        getContext().getDriver().get(TestProperties.getInstance().getURL());
        tnhc.getLanding().setPlayer();
        tnhc.getLanding().setTeam();
    }

}
