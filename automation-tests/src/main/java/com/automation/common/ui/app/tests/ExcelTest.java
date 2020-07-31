package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.ExcelUtils;
import com.taf.automation.ui.support.util.FilloUtils;
import com.taf.automation.ui.support.Helper;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Testing the Excel Utils class
 */
public class ExcelTest {
    private static final String EXCEL_FILE = "data/ui/testingExcel.xls";
    private static final String EXCEL_2013_FILE = "data/ui/testingExcel2013.xlsx";
    private static final String SHEET_1 = "Sheet1";
    private static final String SHEET_2 = "Another Sheet";
    private static final String[] ROWS = new String[]{"A", "B", "C", "D", "E"};
    private static final String[] COLUMNS = new String[]{"1", "2", "3", "4", "5"};
    private static final String SHEET_2_PREFIX = "2-";

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

    @Parameters({"data-set", "worksheet"})
    @Test
    public void printDataSheet1(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] data = ExcelUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void printDataSheet2(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        String[][] data = ExcelUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void printDataSheet3(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] data = FilloUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void printDataSheet4(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        String[][] data = FilloUtils.getAllData(dataSet, worksheet);
        logData(data);
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void validateMethodsGetSameData(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] resourceData = ExcelUtils.getAllData(dataSet, worksheet);
        URL url = Thread.currentThread().getContextClassLoader().getResource(dataSet);
        String[][] fileData = ExcelUtils.getAllData(url.getFile(), worksheet);
        assertThat("Methods did not load data the same", resourceData, equalTo(fileData));
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void validateCorrectDataForSheet1(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] actualData = ExcelUtils.getAllData(dataSet, worksheet);
        String[][] expectedData = getExpectedSheet1Data();
        assertThat("Incorrect Data", actualData, equalTo(expectedData));
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void validateCorrectDataForSheet2(@Optional(EXCEL_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        String[][] actualData = ExcelUtils.getAllData(dataSet, worksheet);
        String[][] expectedData = getExpectedSheet2Data();
        assertThat("Incorrect Data", actualData, equalTo(expectedData));
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void validateCorrectDataForSheet3(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_1) String worksheet) {
        String[][] actualData = FilloUtils.getAllData(dataSet, worksheet);
        String[][] expectedData = getExpectedSheet1Data();
        assertThat("Incorrect Data", actualData, equalTo(expectedData));
    }

    @Parameters({"data-set", "worksheet"})
    @Test
    public void validateCorrectDataForSheet4(@Optional(EXCEL_2013_FILE) String dataSet, @Optional(SHEET_2) String worksheet) {
        String[][] actualData = FilloUtils.getAllData(dataSet, worksheet);
        String[][] expectedData = getExpectedSheet2Data();
        assertThat("Incorrect Data", actualData, equalTo(expectedData));
    }

}
