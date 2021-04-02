package com.taf.automation.ui.support.pageScraping;

import com.taf.automation.asserts.CustomSoftAssertions;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S3252")
@XStreamAlias("row")
public class ExtractedRowData {
    private static final String DEFAULT_PADDING = "null";
    private Map<String, Object> cells;
    private String padding;

    public ExtractedRowData() {
        cells = new LinkedHashMap<>();
        setPadding(DEFAULT_PADDING);
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getPadding() {
        return padding;
    }

    /**
     * Add a new field + value to a row
     *
     * @param key   - using the ColumnMapper Class to set the fieldNameKey with the columnName
     * @param value - value of the field linked to this page (eg MyName)
     */
    public void addField(ColumnMapper key, Object value) {
        addField(key.getColumnName(), value);
    }

    /**
     * Add a new field + value to a row
     *
     * @param fieldNameKey - unique keyName of a field to link to this page (eg NameTextBox)
     * @param value        - value of the field linked to this page (eg MyName)
     */
    public void addField(String fieldNameKey, Object value) {
        AssertJUtil.assertThat(cells).as("Duplicate Key:  " + fieldNameKey).doesNotContainKey(fieldNameKey);
        cells.put(fieldNameKey, value);
    }

    /**
     * Method used to compare this ExtractedRow objects with another ExtractedRow Object
     *
     * @param actualRow     - row to compare against this row
     * @param softly        - object used to log errors in the report
     * @param pageName      - application page name where this test is taking place. Used in output report
     * @param fieldType     - type of field where testing is taking place. Used in output report
     * @param outputRecords - current list of results to be displayed at the end of the test
     */
    public void compare(ExtractedRowData actualRow, CustomSoftAssertions softly, String pageName, String fieldType, List<CsvOutputRecord> outputRecords) {
        if (actualRow == null) {
            softly.assertThat(actualRow).as("Actual Row is Null").isNotNull();
            return;
        }

        addPadding(cells, actualRow.cells, softly);
        for (Map.Entry<String, Object> expectedField : cells.entrySet()) {
            Object actualValue = actualRow.cells.get(expectedField.getKey());
            compareField(expectedField.getKey(), expectedField.getValue(), actualValue, softly, pageName, fieldType, outputRecords);
        }
    }

    /**
     * Method to compare 2 fields in a row and add their results to an outputRecord List
     *
     * @param expectedFieldKey   - expected field key/label/name as it should be displayed in the output report
     * @param expectedFieldValue - expected field value which should be compared against the actual
     * @param actualValue        - value which to compare vs the expectedFieldValue
     * @param softly             - aggregator object used for comparison
     * @param pageName           - name of the page
     * @param fieldType          - type of field as it should be displayed in outputRecord
     * @param outputRecords      - current list of output Records for the current test
     */
    public static void compareField(String expectedFieldKey, Object expectedFieldValue, Object actualValue, CustomSoftAssertions softly, String pageName, String fieldType, List<CsvOutputRecord> outputRecords) {
        int preCompareFailureCount = softly.getFailureCount();
        softly.assertThat(actualValue).as("comparing field:" + expectedFieldKey).isEqualTo(expectedFieldValue);
        int postCompareFailureCount = softly.getFailureCount();

        // Create result record for this test
        ExtractedDataOutputRecord record = new ExtractedDataOutputRecord();
        record.setRow("" + (outputRecords.size() + 1));
        record.setPageName(pageName);
        record.setFieldType(fieldType);
        record.setFieldName(expectedFieldKey);
        record.setExpectedValue((expectedFieldValue == null) ? "null" : expectedFieldValue.toString());
        record.setActualValue((actualValue == null) ? "null" : actualValue.toString());
        record.setTestStatus((preCompareFailureCount == postCompareFailureCount) ? "PASS" : "FAIL");

        // Add the output record to the list
        outputRecords.add(record);
    }

    /**
     * Take 2 rows and add padding if one is smaller
     *
     * @param row1   - first row to compare
     * @param row2   - second row to compare
     * @param softly - aggregator object used for comparison
     */
    private void addPadding(Map<String, Object> row1, Map<String, Object> row2, CustomSoftAssertions softly) {
        if (row1.size() == row2.size()) {
            // Do nothing both maps are same sizes exit
            return;
        }

        boolean map1Bigger = row1.size() > row2.size();
        Map<String, Object> biggerMap = (map1Bigger) ? row1 : row2;
        Map<String, Object> smallerMap = (map1Bigger) ? row2 : row1;

        String errReason = "Difference in Field Count between rows[Row1=" + row1.toString() + ", Row2=" + row2.toString() + "]";
        softly.assertThat(row1.size()).as(errReason).isEqualTo(row2.size());

        for (Map.Entry<String, Object> aField : biggerMap.entrySet()) {
            if (!smallerMap.containsKey(aField.getKey())) {
                smallerMap.put(aField.getKey(), getPadding());
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        List<String> excludeFields = new ArrayList<>();
        return Utils.equals(this, object, excludeFields);
    }

    @Override
    public int hashCode() {
        List<String> excludeFields = new ArrayList<>();
        return HashCodeBuilder.reflectionHashCode(this, excludeFields);
    }

    @Override
    public String toString() {
        ToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
        return ReflectionToStringBuilder.toStringExclude(this);
    }

}
