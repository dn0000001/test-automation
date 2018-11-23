package com.taf.automation.ui.support.csv;

import com.taf.automation.ui.support.DataInstillerUtils;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.testng.TestNGBaseWithoutListeners;
import datainstiller.data.DataAliases;
import datainstiller.data.DataPersistence;
import datainstiller.generators.GeneratorInterface;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.testng.ITestContext;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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

    private CsvUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Get CSV records from the specified resource and update the records parameter and headers parameter
     *
     * @param resourceFilePath - Resource File Path to the CSV file to read
     * @param records          - Updated with the CSV records
     * @param headers          - Updated with the CSV header record
     */
    public static void read(String resourceFilePath, List<CSVRecord> records, Map<String, Integer> headers) {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceFilePath);
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

            GeneratorInterface genType = DataInstillerUtils.getGenerator().getGenerator(matcher.group(1).trim());
            String init = matcher.group(2);
            String val = matcher.group(3);
            return genType.generate(init, val);
        }

        return value;
    }

    /**
     * Attach Data Set to the allure report
     *
     * @param data - Data to be attached to the report
     */
    public static void attachDataSet(DataPersistence data) {
        byte[] attachment = data.toXML().getBytes();
        MakeAttachmentEvent ev = new MakeAttachmentEvent(attachment, "Test Data", "text/xml");
        Allure.LIFECYCLE.fire(ev);
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
        if (csv.isMapped(column.getColumnName())) {
            String initial = component.getData(DataTypes.Initial, false);
            String expected = component.getData(DataTypes.Expected, false);
            component.initializeData(csv.get(column.getColumnName()), initial, expected);
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
        List<Object[]> tests = new ArrayList<>();

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        CsvUtils.read(csvDataSet, records, headers);
        Map<String, Integer> aliases = CsvUtils.getAliasHeaders(headers);

        for (CSVRecord record : records) {
            tests.add(new Object[]{new CsvTestData(record, aliases)});
        }

        return tests;
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
        domainObject.setData(csvTestData);
        updateAliases(domainObject, csvTestData);
        attachDataSet(domainObject);
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
     * Write records to a new CSV file
     *
     * @param filename      - Location & File to write to
     * @param format        - Format of the CSV file
     * @param outputRecords - Records to be output to the file
     */
    public static void writeToCSV(String filename, CSVFormat format, List<CsvOutputRecord> outputRecords) {
        writeToCSV(filename, false, format, outputRecords);
    }

    /**
     * Write records to CSV file
     *
     * @param filename      - Location & File to write to
     * @param append        - if true, then data will be written to the end of the file rather than the beginning.
     * @param format        - Format of the CSV file
     * @param outputRecords - Records to be output to the file
     */
    public static void writeToCSV(String filename, boolean append, CSVFormat format, List<CsvOutputRecord> outputRecords) {
        try (
                FileWriter writer = new FileWriter(filename, append);
                CSVPrinter printer = new CSVPrinter(writer, format)
        ) {
            for (CsvOutputRecord item : outputRecords) {
                printer.printRecord(item.asList());
            }

            printer.flush();
        } catch (Exception ex) {
            assertThat("Failed to write CSV file due to exception:  " + ex.getMessage(), false);
        }
    }

}
