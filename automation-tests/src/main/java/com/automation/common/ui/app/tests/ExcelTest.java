package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.ExcelUtils;
import com.taf.automation.ui.support.util.FilloUtils;
import com.taf.automation.ui.support.util.Helper;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Testing the Excel Utils class
 */
@Listeners(AllureTestNGListener.class)
public class ExcelTest {
    private static final String EXCEL_FILE = "data/ui/testingExcel.xls";
    private static final String EXCEL_2013_FILE = "data/ui/testingExcel2013.xlsx";
    private static final String SHEET_1 = "Sheet1";
    private static final String SHEET_2 = "Another Sheet";
    private static final String[] ROWS = new String[]{"A", "B", "C", "D", "E"};
    private static final String[] COLUMNS = new String[]{"1", "2", "3", "4", "5"};
    private static final String SHEET_2_PREFIX = "2-";
    private static final String INCORRECT_DATA = "Incorrect Data";

    private String[][] getExpectedSheet1Data() {
        String[][] data = new String[ROWS.length][COLUMNS.length];

        int col = 0;
        for (int i = 0; i < ROWS.length; i++) {
            int row = 0;

            for (int j = 0; j < COLUMNS.length; j++) {
                data[i][j] = ROWS[row++] + COLUMNS[col];
            }

            col++;
        }

        return data;
    }

    private String[][] getExpectedSheet2Data() {
        String[][] data = new String[ROWS.length][COLUMNS.length];

        int col = 0;
        for (int i = 0; i < ROWS.length; i++) {
            int row = 0;

            for (int j = 0; j < COLUMNS.length; j++) {
                data[i][j] = SHEET_2_PREFIX + ROWS[row++] + COLUMNS[col];
            }

            col++;
        }

        return data;
    }

    private void logData(String[][] data) {
        for (String[] row : data) {
            Helper.log(Arrays.toString(row), true);
        }
    }

    @Features("ExcelUtils")
    @Stories("Print Data")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xls", "worksheet-sheet1"})
    @Test
    public void printDataSheet1(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] data = ExcelUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Features("ExcelUtils")
    @Stories("Print Data")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xls", "worksheet-sheet2"})
    @Test
    public void printDataSheet2(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        String[][] data = ExcelUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Features("ExcelUtils")
    @Stories("Print Data")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xlsx", "worksheet-sheet1"})
    @Test
    public void printDataSheet3(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] data = FilloUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Features("ExcelUtils")
    @Stories("Print Data")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xlsx", "worksheet-sheet2"})
    @Test
    public void printDataSheet4(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        String[][] data = FilloUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Features("ExcelUtils")
    @Stories("Validate Methods Get Same Data")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xls", "worksheet-sheet1"})
    @Test
    public void validateMethodsGetSameData(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] resourceData = ExcelUtils.getAllData(dataSet, worksheet);
        URL url = Thread.currentThread().getContextClassLoader().getResource(dataSet);
        String[][] fileData = ExcelUtils.getAllData(url.getFile(), worksheet);
        assertThat("Methods did not load data the same", resourceData, equalTo(fileData));
    }

    private void validateUsingExcelUtils(String dataSet, String worksheet, boolean sheet2) {
        String[][] actualData = ExcelUtils.getAllData(dataSet, worksheet);
        String[][] expectedData = sheet2 ? getExpectedSheet2Data() : getExpectedSheet1Data();
        assertThat(INCORRECT_DATA, actualData, equalTo(expectedData));
    }

    @Features("ExcelUtils")
    @Stories("Validate Correct Data For Sheet")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xls", "worksheet-sheet1"})
    @Test
    public void validateCorrectDataForSheet1(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        validateUsingExcelUtils(dataSet, worksheet, false);
    }

    @Features("ExcelUtils")
    @Stories("Validate Correct Data For Sheet")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xls", "worksheet-sheet2"})
    @Test
    public void validateCorrectDataForSheet2(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        validateUsingExcelUtils(dataSet, worksheet, true);
    }

    private void validateUsingFilloUtils(String dataSet, String worksheet, boolean sheet2) {
        String[][] actualData = FilloUtils.getAllData(dataSet, worksheet);
        String[][] expectedData = sheet2 ? getExpectedSheet2Data() : getExpectedSheet1Data();
        assertThat(INCORRECT_DATA, actualData, equalTo(expectedData));
    }

    @Features("ExcelUtils")
    @Stories("Validate Correct Data For Sheet")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xlsx", "worksheet-sheet1"})
    @Test
    public void validateCorrectDataForSheet3(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        validateUsingFilloUtils(dataSet, worksheet, false);
    }

    @Features("ExcelUtils")
    @Stories("Validate Correct Data For Sheet")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"data-set-xlsx", "worksheet-sheet2"})
    @Test
    public void validateCorrectDataForSheet4(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        validateUsingFilloUtils(dataSet, worksheet, true);
    }

}
