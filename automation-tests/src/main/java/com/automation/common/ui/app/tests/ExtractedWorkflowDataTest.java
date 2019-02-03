package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.pageScraping.ExtractedDataOutputRecord;
import com.taf.automation.ui.support.pageScraping.ExtractedPageData;
import com.taf.automation.ui.support.pageScraping.ExtractedRowData;
import com.taf.automation.ui.support.pageScraping.ExtractedTableData;
import com.taf.automation.ui.support.pageScraping.ExtractedWorkflowData;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExtractedWorkflowDataTest {
    private static final String OUTPUT_FILE = "test.xml";
    private static final String EMPTY_WORKFLOW = "data/ui/scraping/empty-workflow.xml";
    private static final String EMPTY_PAGE = "data/ui/scraping/single-empty-page.xml";
    private static final String EMPTY_TABLE = "data/ui/scraping/single-empty-table.xml";
    private static final String EMPTY_ROW = "data/ui/scraping/single-empty-row.xml";
    private static final String COMPLEX_SINGLE_PAGE_WORKFLOW = "data/ui/scraping/complex-single-page-workflow.xml";
    private static final String COMPLEX_MULTI_PAGE_WORKFLOW = "data/ui/scraping/complex-multi-page-workflow.xml";

    public enum WorkflowKeys implements ColumnMapper {
        P1("p1"),
        P1_F1("p1-f1"),
        P1_F2("p1-f2"),

        P2("p2"),
        P2_T1("p2-t1"),
        P2_T2("p2-t2"),
        P2_T2_R1_C1("p2-t2-r1-c1"),
        P2_T3("p2-t3"),
        P2_T3_R1_C1("p2-t3-r1-c1"),
        P2_T3_R2_C1("p2-t3-r2-c1"),

        P3("p3"),
        P3_F1("p3-f1"),
        P3_T1("p3-t1"),
        P3_T1_R1_C1("p3-t1-r1-c1"),
        P3_T1_R1_C2("p3-t1-r1-c2"),

        P4("p4"),
        P4_F1("p4-f1"),

        P5("p5"),
        P5_T1("p5-t1"),

        P6("p6"),
        P6_T1("p6-t1"),
        P6_T1_R1_C1("p6-t1-r1-c1"),
        P6_T1_R2_C1("p6-t1-r2-c1"),
        P6_T1_R3_C1("p6-t1-r3-c1"),

        P7("p7"),
        P7_T1("p7-t1"),
        P7_T1_R1_C1("p7-t1-r1-c1"),
        P7_T1_R1_C2("p7-t1-r1-c2"),
        P7_T1_R2_C1("p7-t1-r2-c1"),
        P7_T1_R2_C2("p7-t1-r2-c2"),

        P7_T2("p7-t2"),
        P7_T2_R1_C1("p7-t2-r1-c1"),
        P7_T2_R1_C2("p7-t2-r1-c2"),
        P7_T2_R2_C1("p7-t2-r2-c1"),
        P7_T2_R2_C2("p7-t2-r2-c2"),
        P7_T2_R3_C1("p7-t2-r3-c1"),
        P7_T2_R3_C2("p7-t2-r3-c2"),
        P7_T3("p7-t3"),
        P7_T3_R1_C1("p7-t3-r1-c1"),
        P7_T3_R1_C2("p7-t3-r1-c2"),

        P8("p8"),
        P8_T1("p8-t1"),
        P8_T1_R1_C1("p8-t1-r1-c1"),
        P8_T1_R1_C2("p8-t1-r1-c2"),
        P8_T1_R1_C3("p8-t1-r1-c3"),

        P9("p9"),
        P9_F1("p9-f1"),
        P9_F2("p9-f2"),
        P9_T1("p9-t1"),
        P9_T1_R1_C1("p9-t1-r1-c1"),
        P9_T1_R1_C2("p9-t1-r1-c2"),
        P9_T2("p9-t2"),
        P9_T2_R1_C1("p9-t2-r1-c1"),
        P9_T2_R1_C2("p9-t2-r1-c2"),

        P10("p10"),
        P10_F1("p10-f1"),
        P10_F2("p10-f2"),
        P10_F3("p10-f3"),
        P10_T1("p10-t1"),
        P10_T1_R1_C1("p10-t1-r1-c1"),
        P10_T1_R1_C2("p10-t1-r1-c2"),
        P10_T1_R1_C3("p10-t1-r1-c3"),
        P10_T1_R2_C1("p10-t1-r2-c1"),
        P10_T1_R2_C2("p10-t1-r2-c2"),
        P10_T1_R2_C3("p10-t1-r2-c3"),
        P10_T1_R3_C1("p10-t1-r3-c1"),
        P10_T1_R3_C2("p10-t1-r3-c2"),
        P10_T1_R3_C3("p10-t1-r3-c3"),

        P10_T2("p10-t2"),
        P10_T2_R1_C1("p10-t2-r1-c1"),
        P10_T2_R1_C2("p10-t2-r1-c2"),
        P10_T2_R2_C1("p10-t2-r2-c1"),
        P10_T2_R2_C2("p10-t2-r2-c2"),
        P10_T2_R3_C1("p10-t2-r3-c1"),
        P10_T2_R3_C2("p10-t2-r3-c2"),
        P10_T2_R4_C1("p10-t2-r4-c1"),
        P10_T2_R4_C2("p10-t2-r4-c2");

        private String columnName;

        WorkflowKeys(String columnName) {
            this.columnName = columnName;
        }

        @Override
        public String getColumnName() {
            return columnName;
        }

        @Override
        public ColumnMapper[] getValues() {
            return values();
        }

    }

    private ExtractedWorkflowData getWorkflowData() {
        ExtractedRowData t1r1 = new ExtractedRowData();
        t1r1.addField("cell1", "a1");
        t1r1.addField("cell2", "b1");

        ExtractedRowData t1r2 = new ExtractedRowData();
        t1r2.addField("cell1", "a2");
        t1r2.addField("cell2", "b2");

        ExtractedRowData t2r1 = new ExtractedRowData();
        t2r1.addField("cell1", "a1");
        t2r1.addField("cell2", "b1");

        ExtractedRowData t2r2 = new ExtractedRowData();
        t2r2.addField("cell1", "a2");
        t2r2.addField("cell2", "b2");

        ExtractedTableData tableData1 = new ExtractedTableData();
        tableData1.setTableNameKey("table1");
        tableData1.addRow(t1r1);
        tableData1.addRow(t1r2);

        ExtractedTableData tableData2 = new ExtractedTableData();
        tableData2.setTableNameKey("table2");
        tableData2.addRow(t2r1);
        tableData2.addRow(t2r2);

        ExtractedPageData pageData1 = new ExtractedPageData();
        pageData1.setPageNameKey("page 1");
        pageData1.addField("field1", "value1");
        pageData1.addTable(tableData1);
        pageData1.addTable(tableData2);

        ExtractedPageData pageData2 = new ExtractedPageData();
        pageData2.setPageNameKey("page 2");
        pageData2.addField("field1", "value1");

        ExtractedWorkflowData workflowData = new ExtractedWorkflowData();
        workflowData.setFlowName("My Flow #1");
        workflowData.addPage(pageData1);
        workflowData.addPage(pageData2);

        return workflowData;
    }

    private void validateOutputRecords(AssertAggregator aggregator, List<CsvOutputRecord> outputRecords, int fieldCount) {
        aggregator.assertThat("Output Records", outputRecords.size(), equalTo(fieldCount));
        for (int i = 0; i < outputRecords.size(); i++) {
            CsvOutputRecord item = outputRecords.get(i);
            if (item instanceof ExtractedDataOutputRecord) {
                ExtractedDataOutputRecord record = (ExtractedDataOutputRecord) item;
                aggregator.assertThat("Test Status [" + i + "]", record.getTestStatus(), equalTo("PASS"));
                aggregator.assertThat("Value [" + i + "]", record.getActualValue(), equalTo(record.getExpectedValue()));
            } else {
                String error = "Output Record[" + i + "] was not instance of ExtractedDataOutputRecord";
                aggregator.assertThat(error, false);
            }
        }
    }

    @Test
    public void writeToFileTest() {
        ExtractedWorkflowData workflowData = getWorkflowData();

        String xml = workflowData.toXML();
        Helper.log(xml, true);

        workflowData.writeToFile(OUTPUT_FILE);
    }

    @Test(dependsOnMethods = "writeToFileTest")
    public void readFromFileTest() {
        ExtractedWorkflowData expected = getWorkflowData();
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(OUTPUT_FILE);
        assertThat("Data from read XML did not match expected data", actual, equalTo(expected));
    }

    @Test
    public void emptyTest() {
        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(EMPTY_WORKFLOW);

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);

        assertThat("Output Records", outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Test
    public void singleEmptyPageTest() {
        ExtractedPageData pageData = new ExtractedPageData();

        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        expected.setFlowName("Single Empty Page Test");
        expected.addPage(pageData);

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(EMPTY_PAGE);

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);

        assertThat("Output Records", outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Test
    public void singleEmptyTableTest() {
        ExtractedTableData tableData = new ExtractedTableData();

        ExtractedPageData pageData = new ExtractedPageData();
        pageData.addTable(tableData);

        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        expected.setFlowName("Single Empty Table Test");
        expected.addPage(pageData);

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(EMPTY_TABLE);

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);

        assertThat("Output Records", outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Test
    public void singleEmptyRowTest() {
        ExtractedRowData rowData = new ExtractedRowData();

        ExtractedTableData tableData = new ExtractedTableData();
        tableData.addRow(rowData);

        ExtractedPageData pageData = new ExtractedPageData();
        pageData.addTable(tableData);

        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        expected.setFlowName("Single Empty Row Test");
        expected.addPage(pageData);

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(EMPTY_ROW);

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);

        assertThat("Output Records", outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Test
    public void complexSinglePageTest() {
        int fieldCount = 0;
        ExtractedRowData p10t1r1 = new ExtractedRowData();
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C1, "bb");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C2, "cc");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C3, "dd");
        fieldCount += 3;

        ExtractedRowData p10t1r2 = new ExtractedRowData();
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C1, "ee");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C2, "ff");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C3, "gg");
        fieldCount += 3;

        ExtractedRowData p10t1r3 = new ExtractedRowData();
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C1, "hh");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C2, "ii");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C3, "jj");
        fieldCount += 3;

        ExtractedTableData p10t1 = new ExtractedTableData();
        p10t1.setTableNameKey(WorkflowKeys.P10_T1);
        p10t1.addRow(p10t1r1);
        p10t1.addRow(p10t1r2);
        p10t1.addRow(p10t1r3);

        ExtractedRowData p10t2r1 = new ExtractedRowData();
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C1, "ll");
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C2, "mm");
        fieldCount += 2;

        ExtractedRowData p10t2r2 = new ExtractedRowData();
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C1, "nn");
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C2, "oo");
        fieldCount += 2;

        ExtractedRowData p10t2r3 = new ExtractedRowData();
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C1, "pp");
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C2, "qq");
        fieldCount += 2;

        ExtractedRowData p10t2r4 = new ExtractedRowData();
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C1, "rr");
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C2, "ss");
        fieldCount += 2;

        ExtractedTableData p10t2 = new ExtractedTableData();
        p10t2.setTableNameKey(WorkflowKeys.P10_T2);
        p10t2.addRow(p10t2r1);
        p10t2.addRow(p10t2r2);
        p10t2.addRow(p10t2r3);
        p10t2.addRow(p10t2r4);

        ExtractedPageData p10 = new ExtractedPageData();
        p10.setPageNameKey(WorkflowKeys.P10);
        p10.addField(WorkflowKeys.P10_F1, "aa");
        p10.addTable(p10t1);
        p10.addField(WorkflowKeys.P10_F2, "kk");
        p10.addTable(p10t2);
        fieldCount += 2;

        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        expected.setFlowName("Complex Single Page WorkFlow");
        expected.addPage(p10);

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(COMPLEX_SINGLE_PAGE_WORKFLOW);
        assertThat("Data from read XML did not match expected data", actual, equalTo(expected));

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);
        validateOutputRecords(aggregator, outputRecords, fieldCount);

        Helper.assertThat(aggregator);
    }

    @Test
    public void complexMultiPageTest() {
        int fieldCount = 0;
        ExtractedPageData p1 = new ExtractedPageData();
        p1.setPageNameKey(WorkflowKeys.P1);
        p1.addField(WorkflowKeys.P1_F1, "aa");
        p1.addField(WorkflowKeys.P1_F2, "bb");
        fieldCount += 2;

        ExtractedTableData p2t1 = new ExtractedTableData();
        p2t1.setTableNameKey(WorkflowKeys.P2_T1);

        ExtractedRowData p2t2r1 = new ExtractedRowData();
        p2t2r1.addField(WorkflowKeys.P2_T2_R1_C1, "cc");
        fieldCount++;

        ExtractedTableData p2t2 = new ExtractedTableData();
        p2t2.setTableNameKey(WorkflowKeys.P2_T2);
        p2t2.addRow(p2t2r1);

        ExtractedRowData p2t3r1 = new ExtractedRowData();
        p2t3r1.addField(WorkflowKeys.P2_T3_R1_C1, "dd");
        fieldCount++;

        ExtractedRowData p2t3r2 = new ExtractedRowData();
        p2t3r2.addField(WorkflowKeys.P2_T3_R2_C1, "ee");
        fieldCount++;

        ExtractedTableData p2t3 = new ExtractedTableData();
        p2t3.setTableNameKey(WorkflowKeys.P2_T3);
        p2t3.addRow(p2t3r1);
        p2t3.addRow(p2t3r2);

        ExtractedPageData p2 = new ExtractedPageData();
        p2.setPageNameKey(WorkflowKeys.P2);
        p2.addTable(p2t1);
        p2.addTable(p2t2);
        p2.addTable(p2t3);

        ExtractedRowData p3t1r1 = new ExtractedRowData();
        p3t1r1.addField(WorkflowKeys.P3_T1_R1_C1, "gg");
        p3t1r1.addField(WorkflowKeys.P3_T1_R1_C2, "hh");
        fieldCount += 2;

        ExtractedTableData p3t1 = new ExtractedTableData();
        p3t1.setTableNameKey(WorkflowKeys.P3_T1);
        p3t1.addRow(p3t1r1);

        ExtractedPageData p3 = new ExtractedPageData();
        p3.setPageNameKey(WorkflowKeys.P3);
        p3.addField(WorkflowKeys.P3_F1, "ff");
        p3.addTable(p3t1);
        fieldCount++;

        ExtractedPageData p4 = new ExtractedPageData();
        p4.setPageNameKey(WorkflowKeys.P4);
        p4.addField(WorkflowKeys.P4_F1, "ii");
        fieldCount++;

        ExtractedTableData p5t1 = new ExtractedTableData();
        p5t1.setTableNameKey(WorkflowKeys.P5_T1);

        ExtractedPageData p5 = new ExtractedPageData();
        p5.setPageNameKey(WorkflowKeys.P5);
        p5.addTable(p5t1);

        ExtractedRowData p6t1r1 = new ExtractedRowData();
        p6t1r1.addField(WorkflowKeys.P6_T1_R1_C1, "jj");
        fieldCount++;

        ExtractedRowData p6t1r2 = new ExtractedRowData();
        p6t1r2.addField(WorkflowKeys.P6_T1_R2_C1, "kk");
        fieldCount++;

        ExtractedRowData p6t1r3 = new ExtractedRowData();
        p6t1r3.addField(WorkflowKeys.P6_T1_R3_C1, "ll");
        fieldCount++;

        ExtractedTableData p6t1 = new ExtractedTableData();
        p6t1.setTableNameKey(WorkflowKeys.P6_T1);
        p6t1.addRow(p6t1r1);
        p6t1.addRow(p6t1r2);
        p6t1.addRow(p6t1r3);

        ExtractedPageData p6 = new ExtractedPageData();
        p6.setPageNameKey(WorkflowKeys.P6);
        p6.addTable(p6t1);

        ExtractedRowData p7t1r1 = new ExtractedRowData();
        p7t1r1.addField(WorkflowKeys.P7_T1_R1_C1, "mm");
        p7t1r1.addField(WorkflowKeys.P7_T1_R1_C2, "nn");
        fieldCount += 2;

        ExtractedRowData p7t1r2 = new ExtractedRowData();
        p7t1r2.addField(WorkflowKeys.P7_T1_R2_C1, "oo");
        p7t1r2.addField(WorkflowKeys.P7_T1_R2_C2, "pp");
        fieldCount += 2;

        ExtractedRowData p7t2r1 = new ExtractedRowData();
        p7t2r1.addField(WorkflowKeys.P7_T2_R1_C1, "qq");
        p7t2r1.addField(WorkflowKeys.P7_T2_R1_C2, "rr");
        fieldCount += 2;

        ExtractedRowData p7t2r2 = new ExtractedRowData();
        p7t2r2.addField(WorkflowKeys.P7_T2_R2_C1, "ss");
        p7t2r2.addField(WorkflowKeys.P7_T2_R2_C2, "tt");
        fieldCount += 2;

        ExtractedRowData p7t2r3 = new ExtractedRowData();
        p7t2r3.addField(WorkflowKeys.P7_T2_R3_C1, "uu");
        p7t2r3.addField(WorkflowKeys.P7_T2_R3_C2, "ww");
        fieldCount += 2;

        ExtractedRowData p7t3r1 = new ExtractedRowData();
        p7t3r1.addField(WorkflowKeys.P6_T1_R3_C1, "xx");
        fieldCount++;

        ExtractedTableData p7t1 = new ExtractedTableData();
        p7t1.setTableNameKey(WorkflowKeys.P7_T1);
        p7t1.addRow(p7t1r1);
        p7t1.addRow(p7t1r2);

        ExtractedTableData p7t2 = new ExtractedTableData();
        p7t2.setTableNameKey(WorkflowKeys.P7_T2);
        p7t2.addRow(p7t2r1);
        p7t2.addRow(p7t2r2);
        p7t2.addRow(p7t2r3);

        ExtractedTableData p7t3 = new ExtractedTableData();
        p7t3.setTableNameKey(WorkflowKeys.P7_T3);
        p7t3.addRow(p7t3r1);

        ExtractedPageData p7 = new ExtractedPageData();
        p7.setPageNameKey(WorkflowKeys.P7);
        p7.addTable(p7t1);
        p7.addTable(p7t2);
        p7.addTable(p7t3);

        ExtractedRowData p8t1r1 = new ExtractedRowData();
        p8t1r1.addField(WorkflowKeys.P8_T1_R1_C1, "yy");
        p8t1r1.addField(WorkflowKeys.P8_T1_R1_C2, "zz");
        p8t1r1.addField(WorkflowKeys.P8_T1_R1_C3, "aaa");
        fieldCount += 3;

        ExtractedTableData p8t1 = new ExtractedTableData();
        p8t1.setTableNameKey(WorkflowKeys.P8_T1);
        p8t1.addRow(p8t1r1);

        ExtractedPageData p8 = new ExtractedPageData();
        p8.setPageNameKey(WorkflowKeys.P8);
        p8.addTable(p8t1);

        ExtractedRowData p9t1r1 = new ExtractedRowData();
        p9t1r1.addField(WorkflowKeys.P9_T1_R1_C1, "ccc");
        p9t1r1.addField(WorkflowKeys.P9_T1_R1_C2, "ddd");
        fieldCount += 2;

        ExtractedTableData p9t1 = new ExtractedTableData();
        p9t1.setTableNameKey(WorkflowKeys.P9_T1);
        p9t1.addRow(p9t1r1);

        ExtractedRowData p9t2r1 = new ExtractedRowData();
        p9t2r1.addField(WorkflowKeys.P9_T2_R1_C1, "fff");
        p9t2r1.addField(WorkflowKeys.P9_T2_R1_C2, "ggg");
        fieldCount += 2;

        ExtractedTableData p9t2 = new ExtractedTableData();
        p9t2.setTableNameKey(WorkflowKeys.P9_T2);
        p9t2.addRow(p9t2r1);

        ExtractedPageData p9 = new ExtractedPageData();
        p9.setPageNameKey(WorkflowKeys.P9);
        p9.addField(WorkflowKeys.P9_F1, "bbb");
        p9.addTable(p9t1);
        p9.addField(WorkflowKeys.P9_F2, "eee");
        p9.addTable(p9t2);
        fieldCount += 2;

        ExtractedRowData p10t1r1 = new ExtractedRowData();
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C1, "iii");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C2, "jjj");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C3, "aaaa");
        fieldCount += 3;

        ExtractedRowData p10t1r2 = new ExtractedRowData();
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C1, "bbbb");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C2, "cccc");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C3, "dddd");
        fieldCount += 3;

        ExtractedRowData p10t1r3 = new ExtractedRowData();
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C1, "eeee");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C2, "ffff");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C3, "gggg");
        fieldCount += 3;

        ExtractedTableData p10t1 = new ExtractedTableData();
        p10t1.setTableNameKey(WorkflowKeys.P10_T1);
        p10t1.addRow(p10t1r1);
        p10t1.addRow(p10t1r2);
        p10t1.addRow(p10t1r3);

        ExtractedRowData p10t2r1 = new ExtractedRowData();
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C1, "hhhh");
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C2, "iiii");
        fieldCount += 2;

        ExtractedRowData p10t2r2 = new ExtractedRowData();
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C1, "jjjj");
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C2, "kkkk");
        fieldCount += 2;

        ExtractedRowData p10t2r3 = new ExtractedRowData();
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C1, "llll");
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C2, "mmmm");
        fieldCount += 2;

        ExtractedRowData p10t2r4 = new ExtractedRowData();
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C1, "nnnn");
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C2, "oooo");
        fieldCount += 2;

        ExtractedTableData p10t2 = new ExtractedTableData();
        p10t2.setTableNameKey(WorkflowKeys.P10_T2);
        p10t2.addRow(p10t2r1);
        p10t2.addRow(p10t2r2);
        p10t2.addRow(p10t2r3);
        p10t2.addRow(p10t2r4);

        ExtractedPageData p10 = new ExtractedPageData();
        p10.setPageNameKey(WorkflowKeys.P10);
        p10.addField(WorkflowKeys.P10_F1, "hhh");
        p10.addTable(p10t1);
        p10.addField(WorkflowKeys.P10_F2, "pppp");
        p10.addTable(p10t2);
        fieldCount += 2;

        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        expected.setFlowName("Complex Multi-Page WorkFlow");
        expected.addPage(p1);
        expected.addPage(p2);
        expected.addPage(p3);
        expected.addPage(p4);
        expected.addPage(p5);
        expected.addPage(p6);
        expected.addPage(p7);
        expected.addPage(p8);
        expected.addPage(p9);
        expected.addPage(p10);

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(COMPLEX_MULTI_PAGE_WORKFLOW);
        assertThat("Data from read XML did not match expected data", actual, equalTo(expected));

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);
        validateOutputRecords(aggregator, outputRecords, fieldCount);

        Helper.assertThat(aggregator);
    }

}
