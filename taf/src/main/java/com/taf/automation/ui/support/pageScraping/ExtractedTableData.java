package com.taf.automation.ui.support.pageScraping;

import com.taf.automation.asserts.CustomSoftAssertions;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@XStreamAlias("table")
public class ExtractedTableData {
    private static final String DEFAULT_PADDING = "null";
    private String tableNameKey;
    private List<ExtractedRowData> rows;
    private String padding;
    private boolean sortIfErrors;

    public ExtractedTableData() {
        rows = new ArrayList<>();
        setTableNameKey("");
        setPadding(DEFAULT_PADDING);
        setSortIfErrors(true);
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getPadding() {
        return padding;
    }

    public void setSortIfErrors(boolean sortIfErrors) {
        this.sortIfErrors = sortIfErrors;
    }

    public boolean isSortIfErrors() {
        return sortIfErrors;
    }

    public String getTableNameKey() {
        return tableNameKey;
    }

    public void setTableNameKey(String tableNameKey) {
        this.tableNameKey = tableNameKey;
    }

    public void setTableNameKey(ColumnMapper enumKeyObject) {
        setTableNameKey(enumKeyObject.getColumnName());
    }

    /**
     * Add a new row to the current table
     *
     * @param row ExtractedRowData object to be added to this table
     */
    public void addRow(ExtractedRowData row) {
        rows.add(row);
    }

    /**
     * Method used to compare this ExpectedTable objects with another ExpectedTable Object
     *
     * @param actualTable   - table to compare against this table
     * @param softly        - object used to log errors in the report
     * @param pageName      - application page name where this table is located. Used in output report
     * @param outputRecords - list of result records to for the current page
     */
    public void compare(ExtractedTableData actualTable, CustomSoftAssertions softly, String pageName, List<CsvOutputRecord> outputRecords) {
        if (actualTable == null) {
            softly.assertThat(actualTable).as("Actual Table is Null").isNotNull();
            return;
        }

        addPadding(this, actualTable, softly);
        for (int i = 0; i < rows.size(); i++) {
            ExtractedRowData actualRow = actualTable.rows.get(i);
            ExtractedRowData expectedRow = rows.get(i);
            expectedRow.compare(actualRow, softly, pageName, getTableNameKey() + "-RowIndex:" + i, outputRecords);
        }
    }

    /**
     * Method to add empty row padding to a ExpectedTableData's rows
     * to ensure the rowCount to be the two tables are the same
     *
     * @param table1 - first table to compare against the second table
     * @param table2 - second table to compare against the first table
     * @param softly - object used to log errors in the report
     */
    private void addPadding(ExtractedTableData table1, ExtractedTableData table2, CustomSoftAssertions softly) {
        if (table1.rows.size() == table2.rows.size()) {
            // Do nothing both tables are same sizes exit
            return;
        }

        boolean table1Bigger = table1.rows.size() > table2.rows.size();
        ExtractedTableData biggerTable = (table1Bigger) ? table1 : table2;
        ExtractedTableData smallerTable = (table1Bigger) ? table2 : table1;

        String reason = "Difference in Row Count between tables[Table1=" + table1.getTableNameKey() + ", Table2=" + table2.getTableNameKey() + "]";
        softly.assertThat(table1.rows.size()).as(reason).isEqualTo(table2.rows.size());

        if (isSortIfErrors()) {
            biggerTable.rows.sort(Comparator.comparing(ExtractedRowData::toString));
            smallerTable.rows.sort(Comparator.comparing(ExtractedRowData::toString));
        }

        int sizeDiff = biggerTable.rows.size() - smallerTable.rows.size();
        for (int i = 0; i < sizeDiff; i++) {
            ExtractedRowData emptyRow = new ExtractedRowData();
            emptyRow.setPadding(getPadding());
            smallerTable.addRow(emptyRow);
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
