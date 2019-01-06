package com.taf.automation.ui.support;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Recordset;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Utilities for Fillo library
 */
public class FilloUtils {
    private FilloUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Get Connection to Excel file
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the Excel file to read
     * @return Connection
     */
    public static Connection getConnection(String resourceFilePath) {
        Connection connection;

        try {
            FileInputStream file = openFileInputStream(resourceFilePath);
            assertThat("Could not open stream - " + resourceFilePath, file, notNullValue());

            Workbook workbook = getWorkbook(file, resourceFilePath);
            assertThat("Unable to connect workbook - " + resourceFilePath, workbook, notNullValue());

            connection = new Connection(workbook, file, resourceFilePath, true);
        } catch (Exception ignore) {
            connection = null;
        }

        assertThat("Workbook is not found - " + resourceFilePath, connection, notNullValue());
        return connection;
    }

    /**
     * Open FileInputStream from resource file and if that fails try from the actual file system
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the excel file to read
     * @return null if cannot open else FileInputStream
     */
    private static FileInputStream openFileInputStream(String resourceFilePath) {
        FileInputStream fileInputStream = null;

        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceFilePath);
        if (url == null) {
            try {
                fileInputStream = new FileInputStream(resourceFilePath);
            } catch (Exception ex) {
                //
            }
        } else {
            try {
                File file = new File(url.toURI());
                fileInputStream = new FileInputStream(file);
            } catch (Exception ex) {
                //
            }
        }

        return fileInputStream;
    }

    private static Workbook getWorkbook(InputStream in, String filePath) {
        Workbook workbook;

        try {
            if (FilenameUtils.getExtension(filePath).equalsIgnoreCase("XLS")) {
                workbook = new HSSFWorkbook(in);
            } else if (FilenameUtils.getExtension(filePath).equalsIgnoreCase("XLSX")) {
                workbook = new XSSFWorkbook(in);
            } else if (FilenameUtils.getExtension(filePath).equalsIgnoreCase("XLSM")) {
                workbook = new XSSFWorkbook(OPCPackage.open(in));
            } else {
                workbook = null;
            }
        } catch (Exception ignore) {
            workbook = null;
        }

        return workbook;
    }

    /**
     * Gets all the data from a specific Excel Worksheet
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the Excel file to read
     * @param workSheet        - Excel Worksheet to read from
     * @return String[][]
     */
    @SuppressWarnings("squid:S2259")
    public static String[][] getAllData(String resourceFilePath, String workSheet) {
        String[][] data;
        String error = "";

        try {
            Connection connection = getConnection(resourceFilePath);
            Recordset records = connection.executeQuery("select * from \"" + workSheet + "\"");
            int size = records.getCount();
            List<String> headers = records.getFieldNames();
            data = new String[size][headers.size()];
            int i = 0;
            while (records.next()) {
                for (int j = 0; j < headers.size(); j++) {
                    data[i][j] = records.getField(headers.get(j));
                }

                i++;
            }
        } catch (Exception ex) {
            error = ex.getMessage();
            data = null;
        }

        assertThat("Could not load excel file due to error:  " + error, data, notNullValue());
        return data;
    }

    /**
     * Get Excel records from the specified resource and update the records parameter and headers parameter<BR>
     * <B>Note: </B> The CsvTestData class is being re-used to store the data
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the Excel file to read
     * @param workSheet        - Excel Worksheet to read from
     * @param records          - Updated with the Excel records
     * @param headers          - Updated with the Excel header record
     */
    @SuppressWarnings("squid:S2259")
    public static void read(
            String resourceFilePath,
            String workSheet,
            List<CSVRecord> records,
            Map<String, Integer> headers
    ) {
        try {
            Connection connection = getConnection(resourceFilePath);
            Recordset excelRecords = connection.executeQuery("select * from \"" + workSheet + "\"");

            List<String> excelHeaders = excelRecords.getFieldNames();
            for (int i = 0; i < excelHeaders.size(); i++) {
                headers.put(excelHeaders.get(i), i);
            }

            Constructor<CSVRecord> constructor = CSVRecord.class
                    .getDeclaredConstructor(String[].class, Map.class, String.class, long.class, long.class);
            constructor.setAccessible(true);
            int index = 0;
            while (excelRecords.next()) {
                String comment = "";
                long recordNumber = index;
                long characterPosition = index;
                String[] values = new String[excelHeaders.size()];
                for (int i = 0; i < excelHeaders.size(); i++) {
                    values[i] = excelRecords.getField(excelHeaders.get(i));
                }

                CSVRecord record = constructor.newInstance(values, headers, comment, recordNumber, characterPosition);
                records.add(record);
                index++;
            }
        } catch (Exception ex) {
            assertThat("Could not read records from Excel file due to error:  " + ex.getMessage(), false);
        }
    }

}
