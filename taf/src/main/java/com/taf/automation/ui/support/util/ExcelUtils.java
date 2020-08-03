package com.taf.automation.ui.support.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Class to work with Excel files<BR>
 * <B>Notes:</B>
 * <OL>
 * <LI>Only Excel files from 97-2003 are supported. (File extension is <B>xls</B>)</LI>
 * <LI>The newer format Excel Files from 2007 are <B>NOT</B> supported.  (File extension is <B>xlsx</B>)</LI>
 * </OL>
 */
public class ExcelUtils {
    private ExcelUtils() {
        //
    }

    /**
     * Get all data from Excel file on specific WorkSheet
     *
     * @param inputWorkbook - Excel File
     * @param workSheet     - Worksheet to read from
     * @return String[][]
     */
    private static String[][] getAllData(File inputWorkbook, String workSheet) throws IOException, BiffException {
        Workbook w = Workbook.getWorkbook(inputWorkbook);
        Sheet sheet = w.getSheet(workSheet);

        int rows = sheet.getRows();
        int cols = sheet.getColumns();
        String[][] data = new String[rows][cols];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Cell cell = sheet.getCell(i, j);
                data[j][i] = cell.getContents();
            }
        }

        return data;
    }

    /**
     * Gets all the data from a specific Excel Worksheet
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the Excel file to read
     * @param workSheet        - Excel Worksheet to read from
     * @return String[][]
     */
    public static String[][] getAllData(String resourceFilePath, String workSheet) {
        String[][] data;
        String error = "";

        try {
            File inputWorkbook = Helper.getFile(resourceFilePath);
            data = getAllData(inputWorkbook, workSheet);
        } catch (Exception ex) {
            error = ex.getMessage();
            data = null;
        }

        assertThat("Could not load excel file due to error:  " + error, data, notNullValue());
        return data;
    }

}
