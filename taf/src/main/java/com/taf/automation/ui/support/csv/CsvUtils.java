package com.taf.automation.ui.support.csv;

import com.taf.automation.api.ApiDomainObject;
import com.taf.automation.ui.support.util.DataInstillerUtils;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.util.FilloUtils;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.RegExUtils;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.Attachment;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.testng.TestNGBaseWithoutListeners;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataAliases;
import datainstiller.data.DataPersistence;
import datainstiller.generators.GeneratorInterface;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.testng.ITestContext;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

/**
 * CSV utilities
 */
public class CsvUtils {
    private static final String ALIAS_PREFIX = "alias-";
    private static final String LIST_SUFFIX = "-list-";

    private CsvUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Open InputStream from resource file and if that fails try from the actual file system
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the CSV file to read
     * @return null if cannot open else InputStream
     */
    private static InputStream openInputStream(String resourceFilePath) {
        InputStream in = null;

        try {
            in = new FileInputStream(Helper.getFile(resourceFilePath));
        } catch (Exception ignore) {
            //
        }

        return in;
    }

    /**
     * Get CSV records from the specified resource and update the records parameter and headers parameter
     *
     * @param resourceFilePath - Resource File Path (or actual file system path) to the CSV file to read
     * @param records          - Updated with the CSV records
     * @param headers          - Updated with the CSV header record
     */
    public static void read(String resourceFilePath, List<CSVRecord> records, Map<String, Integer> headers) {
        try {
            InputStream in = openInputStream(resourceFilePath);
            InputStreamReader reader = new InputStreamReader(in);
            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader();
            CSVParser csvFileParser = new CSVParser(reader, csvFileFormat);
            headers.putAll(csvFileParser.getHeaderMap());
            records.addAll(csvFileParser.getRecords());
        } catch (Exception ex) {
            assertThat("Could not read records from CSV file due to error:  " + ex.getMessage(), false);
        }
    }

    /**
     * Get all the aliases from the CSV header row
     *
     * @param headers - CSV Headers to check for aliases
     * @return Map of aliases
     */
    public static Map<String, Integer> getAliasHeaders(Map<String, Integer> headers) {
        Map<String, Integer> aliasMap = new HashMap<>();

        for (Map.Entry<String, Integer> item : headers.entrySet()) {
            if (StringUtils.startsWithIgnoreCase(item.getKey(), ALIAS_PREFIX)) {
                aliasMap.put(item.getKey(), item.getValue());
            }
        }

        return aliasMap;
    }

    /**
     * Update aliases<BR>
     * <B>Note: </B> Domain Object can be set to null to skip updating it
     *
     * @param domainObject - Domain Object to update aliases just for reporting
     * @param csvTestData  - CSV test data
     */
    public static void updateAliases(DomainObject domainObject, CsvTestData csvTestData) {
        if (domainObject != null && domainObject.getDataAliases() == null) {
            try {
                FieldUtils.writeField(domainObject, "aliases", new DataAliases(), true);
            } catch (Exception ex) {
                assertThat("Unable to initialize the variable aliases", false);
            }
        }

        for (Map.Entry<String, Integer> item : csvTestData.getAliases().entrySet()) {
            String aliasKey = item.getKey();
            String aliasValue = replaceGenerator(csvTestData.getRecord().get(aliasKey));
            PageComponentContext.getGlobalAliases().put(aliasKey, aliasValue);

            // Add to domain object such that data is in the report
            if (domainObject != null) {
                domainObject.getDataAliases().put(aliasKey, aliasValue);
            }
        }
    }

    /**
     * Update aliases<BR>
     * <B>Note: </B> Domain Object can be set to null to skip updating it
     *
     * @param apiDomainObject - Domain Object to update aliases just for reporting
     * @param csvTestData     - CSV test data
     */
    public static void updateAliases(ApiDomainObject apiDomainObject, CsvTestData csvTestData) {
        if (apiDomainObject != null && apiDomainObject.getDataAliases() == null) {
            try {
                FieldUtils.writeField(apiDomainObject, "aliases", new DataAliases(), true);
            } catch (Exception ex) {
                assertThat("Unable to initialize the variable aliases", false);
            }
        }

        for (Map.Entry<String, Integer> item : csvTestData.getAliases().entrySet()) {
            String aliasKey = item.getKey();
            String aliasValue = replaceGenerator(csvTestData.getRecord().get(aliasKey));
            PageComponentContext.getGlobalAliases().put(aliasKey, aliasValue);

            // Add to domain object such that data is in the report
            if (apiDomainObject != null) {
                apiDomainObject.getDataAliases().put(aliasKey, aliasValue);
            }
        }
    }

    /**
     * Replace generator in the specified value
     *
     * @param value - Value to check for generator to be replaced
     * @return if value contains a generator, then it is replaced with the generated value else the value
     */
    private static String replaceGenerator(String value) {
        // This is similar code as in DataAliasesConverterV2 but it does not support Jexl
        if (value.matches("\\$\\[.+]")) {
            Pattern pattern = Pattern.compile("\\$\\[(.+)\\(\\s*'\\s*(.*)\\s*'\\s*,\\s*'\\s*(.*)\\s*'\\s*\\)");
            Matcher matcher = pattern.matcher(value);
            assertThat(value + " - invalid data generation expression!", matcher.find());

            GeneratorInterface genType = DataInstillerUtils.getGenerator(null).getGenerator(matcher.group(1).trim());
            String init = matcher.group(2);
            String val = matcher.group(3);
            return genType.generate(init, val);
        }

        return value;
    }

    /**
     * Attach Data Set to the allure report
     *
     * @param data  - Data to be attached to the report
     * @param title - Title of attachment. Shown at report as name of attachment
     */
    public static void attachDataSet(DataPersistence data, String title) {
        byte[] attachment = data.toXML().getBytes();
        new Attachment().withTitle(title).withType("text/xml").withFile(attachment).build();
    }

    /**
     * Use the CSV data to set a <B>String</B> variable in an object provided it is not blank
     *
     * @param target    - Target object that contains the field to be set
     * @param fieldname - Field Name to be set
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to field name
     */
    public static void setStringData(Object target, String fieldname, CSVRecord csv, ColumnMapper column) {
        if (isNotBlank(csv, column)) {
            Utils.writeField(target, fieldname, csv.get(column.getColumnName()));
        }
    }

    /**
     * Use the CSV data to set a <B>Boolean</B> variable in an object provided it is not blank
     *
     * @param target    - Target object that contains the field to be set
     * @param fieldname - Field Name to be set
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to field name
     */
    public static void setBooleanData(Object target, String fieldname, CSVRecord csv, ColumnMapper column) {
        if (isNotBlank(csv, column)) {
            Utils.writeField(target, fieldname, BooleanUtils.toBoolean(csv.get(column.getColumnName())));
        }
    }

    /**
     * Use the CSV data to set a <B>Integer</B> variable in an object provided it is not blank
     *
     * @param target       - Target object that contains the field to be set
     * @param fieldname    - Field Name to be set
     * @param csv          - CSV record data
     * @param column       - Column Enumeration that maps to field name
     * @param defaultValue - The default value if conversion to Integer fails
     */
    public static void setIntegerData(Object target, String fieldname, CSVRecord csv, ColumnMapper column, int defaultValue) {
        if (isNotBlank(csv, column)) {
            Utils.writeField(target, fieldname, NumberUtils.toInt(csv.get(column.getColumnName()), defaultValue));
        }
    }

    /**
     * Use the CSV data to set a <B>String</B> variable in an list item object provided it is not blank
     *
     * @param target    - Target object that contains the field to be set
     * @param fieldname - Field Name to be set
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to field name
     * @param index     - Index of list item
     */
    public static void setStringData(Object target, String fieldname, CSVRecord csv, ColumnMapper column, int index) {
        String listColumnName = getListColumnName(column, index);
        if (csv.isMapped(listColumnName) && StringUtils.isNotBlank(csv.get(listColumnName))) {
            Utils.writeField(target, fieldname, csv.get(listColumnName));
        }
    }

    /**
     * Use the CSV data to set a <B>Boolean</B> variable in an list item object provided it is not blank
     *
     * @param target    - Target object that contains the field to be set
     * @param fieldname - Field Name to be set
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to field name
     * @param index     - Index of list item
     */
    public static void setBooleanData(Object target, String fieldname, CSVRecord csv, ColumnMapper column, int index) {
        String listColumnName = getListColumnName(column, index);
        if (csv.isMapped(listColumnName) && StringUtils.isNotBlank(csv.get(listColumnName))) {
            Utils.writeField(target, fieldname, BooleanUtils.toBoolean(csv.get(listColumnName)));
        }
    }

    /**
     * Use the CSV data to set a <B>Integer</B> variable in an object provided it is not blank
     *
     * @param target       - Target object that contains the field to be set
     * @param fieldname    - Field Name to be set
     * @param csv          - CSV record data
     * @param column       - Column Enumeration that maps to field name
     * @param index        - Index of list item
     * @param defaultValue - The default value if conversion to Integer fails
     */
    public static void setIntegerData(Object target, String fieldname, CSVRecord csv, ColumnMapper column, int index, int defaultValue) {
        String listColumnName = getListColumnName(column, index);
        if (csv.isMapped(listColumnName) && StringUtils.isNotBlank(csv.get(listColumnName))) {
            Utils.writeField(target, fieldname, NumberUtils.toInt(csv.get(listColumnName), defaultValue));
        }
    }

    /**
     * Get List Column Name
     *
     * @param column - Column Enumeration to create key to retrieve data
     * @param index  - Index of the item to retrieve data from
     * @return The key to retrieve data from a CSVRecord object
     */
    public static String getListColumnName(ColumnMapper column, int index) {
        return column.getColumnName() + LIST_SUFFIX + index;
    }

    /**
     * Use the CSV data to set the variables in the domain object<BR>
     * <B>Note: </B> Initial &amp; Expected data is not changed<BR>
     *
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to component
     * @param component - Component to initialize data
     */
    public static void setData(CSVRecord csv, ColumnMapper column, PageComponent component) {
        if (isNotBlank(csv, column)) {
            String initial = component.getData(DataTypes.Initial, false);
            String expected = component.getData(DataTypes.Expected, false);
            component.initializeData(csv.get(column.getColumnName()), initial, expected);
        }
    }

    /**
     * Use the CSV data to set the list data for a component<BR>
     * <B>Note: </B> Initial &amp; Expected data is not changed<BR>
     *
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to component
     * @param index     - Index used to construct the list column name to get the data
     * @param component - Component to initialize data
     */
    public static void setData(CSVRecord csv, ColumnMapper column, int index, PageComponent component) {
        String listColumnName = getListColumnName(column, index);
        if (csv.isMapped(listColumnName) && StringUtils.isNotBlank(csv.get(listColumnName))) {
            String initial = component.getData(DataTypes.Initial, false);
            String expected = component.getData(DataTypes.Expected, false);
            component.initializeData(csv.get(listColumnName), initial, expected);
        }
    }

    /**
     * Use the CSV data to set the variables in the domain object with left padding if necessary on non-empty value<BR>
     * <B>Note: </B> Initial &amp; Expected data is not changed<BR>
     *
     * @param csv       - CSV record data
     * @param column    - Column Enumeration that maps to component
     * @param component - Component to initialize data
     * @param size      - The size to pad to
     * @param padStr    - The String to pad with, null or empty treated as single space
     */
    public static void setData(CSVRecord csv, ColumnMapper column, PageComponent component, int size, String padStr) {
        if (csv.isMapped(column.getColumnName())) {
            String data = StringUtils.defaultString(csv.get(column.getColumnName()));
            if (!data.isEmpty()) {
                data = leftPad(data, size, padStr);
            }

            String initial = component.getData(DataTypes.Initial, false);
            String expected = component.getData(DataTypes.Expected, false);
            component.initializeData(data, initial, expected);
        }
    }

    /**
     * Get CSV Data Set (resource) location from parameter (in the suite file)
     *
     * @param testNGContext - TestNG Context
     * @param parameter     - Parameter that contains the CSV Data Set (resource) location
     * @return CSV Data Set (resource) location
     */
    public static String getCsvDataSetFromParameter(ITestContext testNGContext, String parameter) {
        String csvDataSet = testNGContext.getCurrentXmlTest().getParameter(parameter);
        String reason = "Parameter '" + parameter + "' missing for test:  " + testNGContext.getName();
        assertThat(reason, csvDataSet, not(isEmptyOrNullString()));
        return csvDataSet;
    }

    /**
     * Generic Data Provider for CSV files<BR>
     * <B>Note: </B> It is recommended to call this method in the BeforeTest annotation such that any error with fail
     * the test.  Any failure in the DataProvider annotation will just mark the test as skipped and you could have
     * a successful test run.
     *
     * @param csvDataSet - CSV Data Set (resource) location
     * @return List&lt;Object[]&gt;
     */
    public static List<Object[]> dataProvider(String csvDataSet) {
        return dataProvider(csvDataSet, null);
    }

    /**
     * Generic Data Provider for CSV files<BR>
     * <B>Note: </B> It is recommended to call this method in the BeforeTest annotation such that any error with fail
     * the test.  Any failure in the DataProvider annotation will just mark the test as skipped and you could have
     * a successful test run.
     *
     * @param csvDataSet    - CSV Data Set location
     * @param runColumnName - If value is not blank,
     *                      then removes all the records that are not set to run using the specific column name
     *                      else no records are removed
     * @return List&lt;Object[]&gt;
     */
    public static List<Object[]> dataProvider(String csvDataSet, String runColumnName) {
        List<Object[]> tests = new ArrayList<>();

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        read(csvDataSet, records, headers);
        Map<String, Integer> aliases = getAliasHeaders(headers);

        for (CSVRecord record : records) {
            tests.add(new Object[]{new CsvTestData(record, aliases)});
        }

        if (StringUtils.isNotBlank(runColumnName)) {
            // Remove any record that is not set to run
            tests.removeIf(data -> !BooleanUtils.toBoolean(((CsvTestData) data[0]).getRecord().get(runColumnName)));
        }

        return tests;
    }

    /**
     * Generic Data Provider for Excel files<BR>
     * <B>Note: </B> It is recommended to call this method in the BeforeTest annotation such that any error with fail
     * the test.  Any failure in the DataProvider annotation will just mark the test as skipped and you could have
     * a successful test run.
     *
     * @param excelDataSet  - Excel Data Set location
     * @param workSheet     - Excel Worksheet to read from
     * @param runColumnName - If value is not blank,
     *                      then removes all the records that are not set to run using the specific column name
     *                      else no records are removed
     * @return List&lt;Object[]&gt;
     */
    public static List<Object[]> dataProvider(String excelDataSet, String workSheet, String runColumnName) {
        List<Object[]> tests = new ArrayList<>();

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        FilloUtils.read(excelDataSet, workSheet, records, headers);
        Map<String, Integer> aliases = getAliasHeaders(headers);

        for (CSVRecord record : records) {
            tests.add(new Object[]{new CsvTestData(record, aliases)});
        }

        if (StringUtils.isNotBlank(runColumnName)) {
            // Remove any record that is not set to run
            tests.removeIf(data -> !BooleanUtils.toBoolean(((CsvTestData) data[0]).getRecord().get(runColumnName)));
        }

        return tests;
    }


    /**
     * Generic Data Provider for all (supported) file types<BR>
     * <B>Note: </B> It is recommended to call this method in the BeforeTest annotation such that any error with fail
     * the test.  Any failure in the DataProvider annotation will just mark the test as skipped and you could have
     * a successful test run.
     *
     * @param dataSet               - Data Set location
     * @param workSheet             - Excel Worksheet to read from if file type is Excel
     * @param runColumnName         - If value is not blank and flat file type (like Excel/CSV),
     *                              then removes all the records that are not set to run using the specific column name
     *                              else no records are removed
     * @param readStructuredFormats - true to read structured formats (like XML),
     *                              false to return empty records for structured formats
     * @return List&lt;Object[]&gt;
     */
    public static List<Object[]> dataProvider(
            String dataSet,
            String workSheet,
            String runColumnName,
            boolean readStructuredFormats
    ) {
        String extension = FilenameUtils.getExtension(dataSet);
        if (StringUtils.startsWithIgnoreCase(extension, "XLS")) {
            return dataProvider(dataSet, workSheet, runColumnName);
        } else if (StringUtils.equalsIgnoreCase(extension, "CSV")) {
            return dataProvider(dataSet, runColumnName);
        } else {
            // Assumed to be XML
            List<Object[]> tests = new ArrayList<>();

            if (readStructuredFormats) {
                tests.add(new String[]{dataSet});
            }

            return tests;
        }
    }


    /**
     * Generic Data Provider for CSV files<BR>
     * <B>Note: </B> It is recommended to call this method in the BeforeTest annotation such that any error with fail
     * the test.  Any failure in the DataProvider annotation will just mark the test as skipped and you could have
     * a successful test run.
     *
     * @param injectedContext  - Injected TestNG Content
     * @param useCsvParameter  - If <B>null</B> skips reading parameter and CSV file
     *                         else reads the parameter which must exist to determine if you should read the CSV file.
     * @param dataSetParameter - CSV data set parameter (based on useCsvParameter this may not be used.)
     * @param runColumnName    - If value is not blank,
     *                         then removes all the records that are not set to run using the specific column name
     *                         else no records are removed
     * @return Iterator&lt;Object[]&gt;
     */
    public static Iterator<Object[]> dataProvider(
            ITestContext injectedContext,
            String useCsvParameter,
            String dataSetParameter,
            String runColumnName
    ) {
        List<Object[]> all = new ArrayList<>();

        // Note: This method is run before all tests in this class as such only read csv if specified
        // which should only be with data provider
        if (useCsvParameter != null && BooleanUtils.toBoolean(getCsvDataSetFromParameter(injectedContext, useCsvParameter))) {
            String csvDataSet = getCsvDataSetFromParameter(injectedContext, dataSetParameter);
            all = dataProvider(csvDataSet, runColumnName);
        }

        return all.iterator();
    }

    /**
     * Generic Test Runner
     *
     * @param testNGContext - TestNG Context
     * @param testClass     - Test Class to be run
     * @param testActions   - Lambda expression to run all test steps/actions
     */
    public static void testRunner(
            TestNGBaseWithoutListeners testClass,
            ITestContext testNGContext,
            final Runnable testActions
    ) {
        boolean failure = true;
        try {
            // When using the data provider functionality, this method needs to be called in this new thread
            testClass.initTest(testNGContext);

            // Put all normal test actions in another method such that Sonar Cognitive Complexity violation
            // does not occur.
            testActions.run();
            failure = false;
        } finally {
            //
            // When using the data provider functionality, all the after test methods need to be called here
            // in this new thread as TestNG does not call any of the after test methods
            //
            if (failure) {
                TestNGBase.takeScreenshot("Failed Test Screenshot");
                TestNGBase.takeHTML("Failed Test HTML Source");
            }

            testClass.closeDriver();
        }
    }

    /**
     * Generic method to initialize the data (including aliases) and attach the data to the report
     *
     * @param domainObject - Domain Object to initialize with specified data
     * @param csvTestData  - CSV Test Data to be used
     */
    public static void initializeDataAndAttach(DomainObject domainObject, CsvTestData csvTestData) {
        initializeDataAndAttach(domainObject, csvTestData, "Test Data");
    }

    /**
     * Generic method to initialize the data (including aliases) and attach the data to the report
     *
     * @param domainObject - Domain Object to initialize with specified data
     * @param csvTestData  - CSV Test Data to be used
     * @param title        - Title of attachment. Shown at report as name of attachment
     */
    public static void initializeDataAndAttach(DomainObject domainObject, CsvTestData csvTestData, String title) {
        domainObject.setData(csvTestData);
        updateAliases(domainObject, csvTestData);
        attachDataSet(domainObject, title);
    }

    /**
     * Generic method to initialize the data (including aliases) and attach the data to the report
     *
     * @param apiDomainObject - Domain Object to initialize with specified data
     * @param csvTestData     - CSV Test Data to be used
     */
    public static void initializeDataAndAttach(ApiDomainObject apiDomainObject, CsvTestData csvTestData) {
        initializeDataAndAttach(apiDomainObject, csvTestData, "Test Data");
    }

    /**
     * Generic method to initialize the data (including aliases) and attach the data to the report
     *
     * @param apiDomainObject - Domain Object to initialize with specified data
     * @param csvTestData     - CSV Test Data to be used
     * @param title           - Title of attachment. Shown at report as name of attachment
     */
    public static void initializeDataAndAttach(ApiDomainObject apiDomainObject, CsvTestData csvTestData, String title) {
        apiDomainObject.setData(csvTestData);
        updateAliases(apiDomainObject, csvTestData);
        attachDataSet(apiDomainObject, title);
    }

    /**
     * Check if the column is mapped and there is non-blank data
     *
     * @param csv    - CSV record data
     * @param column - Column Enumeration to check if it is mapped and there is non-blank data
     * @return true if the column is mapped and there is non-blank data
     */
    public static boolean isNotBlank(CSVRecord csv, ColumnMapper column) {
        return csv.isMapped(column.getColumnName()) && StringUtils.isNotBlank(csv.get(column.getColumnName()));
    }

    /**
     * Check if any of the specified columns are mapped and there is corresponding non-blank data
     *
     * @param csv     - CSV record data
     * @param columns - Column Enumerations to check if any are mapped and there is corresponding non-blank data
     * @return true if any column is mapped and there is corresponding non-blank data
     */
    public static boolean isAnyNotBlank(CSVRecord csv, ColumnMapper... columns) {
        if (columns == null) {
            return false;
        }

        for (ColumnMapper column : columns) {
            if (isNotBlank(csv, column)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the column of a list item is mapped and there is non-blank data<BR>
     * <B>Note: </B> The LIST_SUFFIX is appended plus the index value to the column name
     *
     * @param csv    - CSV record data
     * @param index  - Index of the item to check
     * @param column - Column Enumeration to check if it is mapped and there is non-blank data
     * @return true if the column of the specified list item is mapped and there is non-blank data
     */
    public static boolean isNotBlank(CSVRecord csv, int index, ColumnMapper column) {
        String listColumnName = column.getColumnName() + LIST_SUFFIX + index;
        return csv.isMapped(listColumnName) && StringUtils.isNotBlank(csv.get(listColumnName));
    }

    /**
     * Check if any of the columns for the list item is mapped and there is non-blank data<BR>
     * <B>Note: </B> The LIST_SUFFIX is appended plus the index value to the column name
     *
     * @param csv     - CSV record data
     * @param index   - Index of the item to check
     * @param columns - Column Enumerations to check if any are mapped and there is corresponding non-blank data
     * @return true if any column of the specified list item is mapped and there is corresponding non-blank data
     */
    public static boolean isAnyNotBlank(CSVRecord csv, int index, ColumnMapper... columns) {
        if (columns == null) {
            return false;
        }

        for (ColumnMapper column : columns) {
            if (isNotBlank(csv, index, column)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the list size
     *
     * @param csv     - CSV record data
     * @param columns - Column Enumerations of the list to check if any are mapped and there is corresponding non-blank data
     * @return the list size
     */
    public static int getListSize(CSVRecord csv, ColumnMapper... columns) {
        int size = 0;
        while (isAnyNotBlank(csv, size, columns)) {
            size++;
        }

        return size;
    }

    /**
     * Write records to a new CSV file
     *
     * @param filename      - Location & File to write to
     * @param format        - Format of the CSV file
     * @param outputRecords - Records to be output to the file
     * @param totals        - For each list in the object, the total number of items there needs to be
     */
    public static void writeToCSV(String filename, CSVFormat format, List<CsvOutputRecord> outputRecords, int... totals) {
        writeToCSV(filename, false, format, outputRecords, totals);
    }

    /**
     * Write records to CSV file
     *
     * @param filename      - Location & File to write to
     * @param append        - if true, then data will be written to the end of the file rather than the beginning.
     * @param format        - Format of the CSV file
     * @param outputRecords - Records to be output to the file
     * @param totals        - For each list in the object, the total number of items there needs to be
     */
    public static void writeToCSV(String filename, boolean append, CSVFormat format, List<CsvOutputRecord> outputRecords, int... totals) {
        try (
                FileWriter writer = new FileWriter(filename, append);
                CSVPrinter printer = new CSVPrinter(writer, format)
        ) {
            for (CsvOutputRecord item : outputRecords) {
                item.padListsWithEmptyItems(totals);
                printer.printRecord(item.asList());
            }

            printer.flush();
        } catch (Exception ex) {
            assertThat("Failed to write CSV file due to exception:  " + ex.getMessage(), false);
        }
    }

    /**
     * Left pad a String with a specified String
     *
     * @param str    - The String to pad out, null value converted to empty string and then left padded
     * @param size   - The size to pad to
     * @param padStr - The String to pad with, null or empty treated as single space
     * @return left padded String or original String if no padding is necessary
     */
    public static String leftPad(String str, int size, String padStr) {
        return StringUtils.leftPad(StringUtils.defaultString(str), size, padStr);
    }

    /**
     * Generate CSV Headers for a list
     *
     * @param genericListHeaders - The Generic List Headers for each list item
     * @param size               - Size of the list.  (This should be max size of all the lists in the test data.)
     * @param prefix             - Prefix to be used before each field.  (This should be the field name of the list.)
     * @return Header row for this specific list for use with the CSVFormat class
     */
    public static List<String> generateCsvHeadersForList(List<String> genericListHeaders, int size, String prefix) {
        List<String> headers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (String genericFieldHeader : genericListHeaders) {
                headers.add(StringUtils.removeEnd(prefix, "-") + "-" + genericFieldHeader + LIST_SUFFIX + i);
            }
        }

        return headers;
    }

    /**
     * Generate the CSV Headers row from the specified class
     *
     * @param clazz  - Class to generate the CSV Headers Row for
     * @param prefix - Prefix to be used before each field if non-blank value
     * @return CSV Headers row
     */
    public static String generateCsvHeaders(Class clazz, String prefix) {
        StringBuilder sb = new StringBuilder();

        List<String> headers = generateCsvHeadersForFurtherProcessing(clazz, prefix);
        for (String header : headers) {
            sb.append(header);
            sb.append(",");
        }

        return StringUtils.removeEnd(sb.toString(), ",");
    }

    /**
     * Generate the CSV Headers row from the specified class
     *
     * @param clazz  - Class to generate the CSV Headers Row for
     * @param prefix - Prefix to be used before each field if non-blank value
     * @return CSV Headers row as a list
     */
    public static List<String> generateCsvHeadersForFurtherProcessing(Class clazz, String prefix) {
        List<String> headers = new ArrayList<>();

        List<Field> all = FieldUtils.getAllFieldsList(clazz);
        for (Field field : all) {
            if (field.isAnnotationPresent(XStreamOmitField.class) ||
                    field.isSynthetic() ||
                    Modifier.isStatic(field.getModifiers()) ||
                    field.getType().equals(By.class) ||
                    field.getType().equals(JexlContext.class) ||
                    field.getType().equals(DataAliases.class) ||
                    StringUtils.equals(field.getName(), "xmlns") ||
                    StringUtils.equals(field.getName(), "xsi") ||
                    StringUtils.equals(field.getName(), "schemaLocation")
            ) {
                continue;
            }

            String usePrefix = (StringUtils.isNotBlank(prefix)) ? prefix : "";
            headers.add(usePrefix + field.getName());
        }

        return headers;
    }

    /**
     * Prints the generated the CSV Headers row from the specified class to the console
     *
     * @param clazz  - Class to generate the CSV Headers Row for
     * @param prefix - Prefix to be used before each field if non-blank value
     */
    public static void printGenerateCsvHeaders(Class clazz, String prefix) {
        Helper.log("\n\n" + generateCsvHeaders(clazz, prefix) + "\n\n", true);
    }

    /**
     * Generate code for enum that maps to the CSV headers
     *
     * @param csvDataSet  - CSV Data Set location
     * @param enumName    - Enumeration Name to be used, if null a placeholder is used
     * @param packageName - Package Name to be used, if null a placeholder is used
     * @return code that can be used for the enum to map to the CSV file
     */
    public static String generateCodeForEnum(String csvDataSet, String enumName, String packageName) {
        final String indent = "    ";
        StringBuilder sb = new StringBuilder();
        sb.append("package ");
        sb.append(StringUtils.defaultString(packageName, "<REPLACE_WITH_PACKAGE>"));
        sb.append(";");
        sb.append("\n\n");
        sb.append("import com.taf.automation.ui.support.csv.ColumnMapper;");
        sb.append("\n\n");
        sb.append("public enum ");
        sb.append(StringUtils.defaultString(enumName, "<REPLACE_WITH_ENUM_NAME>"));
        sb.append(" implements ColumnMapper {");
        sb.append("\n");

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        read(csvDataSet, records, headers);
        for (String key : headers.keySet()) {
            // Skip the aliases
            if (StringUtils.startsWithIgnoreCase(key, ALIAS_PREFIX)) {
                continue;
            }

            // Ensure valid Java field name
            String fieldName = key.replaceAll(RegExUtils.NOT_ALPHANUMERIC, "_");
            if (fieldName.matches("^\\d.*")) {
                fieldName = "_" + fieldName;
            }

            // Make field "fooBar" to be "FOO_BAR"
            StringBuilder rejoin = new StringBuilder();
            for (String piece : StringUtils.splitByCharacterTypeCamelCase(fieldName)) {
                rejoin.append(piece.toUpperCase());
                rejoin.append("_");
            }

            sb.append(indent);
            sb.append(StringUtils.removeEnd(rejoin.toString(), "_"));
            sb.append("(\"");
            sb.append(key);
            sb.append("\"),");
            sb.append("\n");
        }

        sb.append(indent + ";");
        sb.append("\n\n");
        sb.append(indent + "private String columnName;");
        sb.append("\n\n");
        sb.append(indent + StringUtils.defaultString(enumName, "<REPLACE_WITH_ENUM_NAME>"));
        sb.append("(String columnName) {");
        sb.append("\n");
        sb.append(indent + indent + "this.columnName = columnName;");
        sb.append("\n");
        sb.append(indent + "}");
        sb.append("\n\n");
        sb.append(indent + "@Override");
        sb.append("\n");
        sb.append(indent + "public String getColumnName() {");
        sb.append("\n");
        sb.append(indent + indent + "return columnName;");
        sb.append("\n");
        sb.append(indent + "}");
        sb.append("\n\n");
        sb.append(indent + "@Override");
        sb.append("\n");
        sb.append(indent + "public ColumnMapper[] getValues() {");
        sb.append("\n");
        sb.append(indent + indent + "return values();");
        sb.append("\n");
        sb.append(indent + "}");
        sb.append("\n\n");
        sb.append("}");
        sb.append("\n\n");

        return sb.toString();
    }

    /**
     * Prints the generated code for enum that maps to the CSV headers to the console
     *
     * @param csvDataSet  - CSV Data Set (resource) location
     * @param enumName    - Enumeration Name to be used, if null a placeholder is used
     * @param packageName - Package Name to be used, if null a placeholder is used
     */
    public static void printGeneratedCodeForEnum(String csvDataSet, String enumName, String packageName) {
        Helper.log("\n\n" + generateCodeForEnum(csvDataSet, enumName, packageName), true);
    }

    /**
     * Generate the CSV Headers row from the specified enumeration
     *
     * @param columnMapper - Enumeration to generate CSV headers row from
     * @return CSV Headers row
     */
    public static String generateCsvHeaders(ColumnMapper columnMapper) {
        StringBuilder sb = new StringBuilder();

        for (ColumnMapper header : columnMapper.getValues()) {
            sb.append(header.getColumnName());
            sb.append(",");
        }

        return StringUtils.removeEnd(sb.toString(), ",");
    }

    /**
     * Prints the generated the CSV Headers row from the specified enumeration to the console
     *
     * @param columnMapper - Enumeration to generate CSV headers row from
     */
    public static void printGenerateCsvHeaders(ColumnMapper columnMapper) {
        Helper.log("\n\n" + generateCsvHeaders(columnMapper) + "\n\n", true);
    }

}
