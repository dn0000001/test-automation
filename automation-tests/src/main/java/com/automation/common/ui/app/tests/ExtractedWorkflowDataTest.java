package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.pageScraping.ExtractedDataOutputRecord;
import com.taf.automation.ui.support.pageScraping.ExtractedPageData;
import com.taf.automation.ui.support.pageScraping.ExtractedRowData;
import com.taf.automation.ui.support.pageScraping.ExtractedTableData;
import com.taf.automation.ui.support.pageScraping.ExtractedWorkflowData;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.Helper;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Listeners(AllureTestNGListener.class)
public class ExtractedWorkflowDataTest {
    private static final String HOME = System.getProperty("user.home");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String OUTPUT_FILE = "test.xml";
    private static final String EMPTY_WORKFLOW = "data/ui/scraping/empty-workflow.xml";
    private static final String EMPTY_PAGE = "data/ui/scraping/single-empty-page.xml";
    private static final String EMPTY_TABLE = "data/ui/scraping/single-empty-table.xml";
    private static final String EMPTY_ROW = "data/ui/scraping/single-empty-row.xml";
    private static final String COMPLEX_SINGLE_PAGE_WORKFLOW = "data/ui/scraping/complex-single-page-workflow.xml";
    private static final String COMPLEX_MULTI_PAGE_WORKFLOW = "data/ui/scraping/complex-multi-page-workflow.xml";
    private static final String COMPLEX_SINGLE_PAGE_WORKFLOW_2 = "data/ui/scraping/complex-single-page-workflow-2.xml";
    private static final String CELL1 = "cell1";
    private static final String CELL2 = "cell2";
    private static final String EXPECTED = "Expected";
    private static final String ACTUAL = "Actual";
    private static final String OUTPUT_RECORDS = "Output Records";
    private static final String MISMATCH = "Data from read XML did not match expected data";

    private enum WorkflowKeys implements ColumnMapper {
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
        P10_T2_R4_C2("p10-t2-r4-c2"),

        LEVEL1("level1"),
        LEVEL1_PAGE1("level1-page1"),
        LEVEL1_TABLE1("level1-table1"),
        LEVEL1_FIELD1("level1-field1"),

        LEVEL2("level2"),
        LEVEL2_PAGE1("level2-page1"),
        LEVEL2_TABLE1("level2-table1"),
        LEVEL2_FIELD1("level2-field1"),

        LEVEL3("level3"),
        LEVEL3_PAGE1("level3-page1"),
        LEVEL3_TABLE1("level3-table1"),
        LEVEL3_FIELD1("level3-field1"),

        LEVEL4("level4"),
        LEVEL4_PAGE1("level4-page1"),
        LEVEL4_TABLE1("level4-table1"),
        LEVEL4_FIELD1("level4-field1"),

        NOT_SEARCHED_ROW1("not-searched-row1"),
        NOT_SEARCHED_FIELD1("not-searched-field1"),
        NOT_SEARCHED_FIELD2("not-searched-field2"),
        NOT_SEARCHED_FIELD3("not-searched-field3"),
        NOT_SEARCHED_PAGE1("not-searched-page1"),
        ;

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
        t1r1.addField(CELL1, "a1");
        t1r1.addField(CELL2, "b1");

        ExtractedRowData t1r2 = new ExtractedRowData();
        t1r2.addField(CELL1, "a2");
        t1r2.addField(CELL2, "b2");

        ExtractedRowData t2r1 = new ExtractedRowData();
        t2r1.addField(CELL1, "a1");
        t2r1.addField(CELL2, "b1");

        ExtractedRowData t2r2 = new ExtractedRowData();
        t2r2.addField(CELL1, "a2");
        t2r2.addField(CELL2, "b2");

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
        aggregator.assertThat(OUTPUT_RECORDS, outputRecords.size(), equalTo(fieldCount));
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

    private String makeKey(CsvOutputRecord record) {
        ExtractedDataOutputRecord item = (ExtractedDataOutputRecord) record;
        return item.getPageName() + item.getFieldName() + item.getFieldType();
    }

    private void validateOutputRecords(AssertAggregator aggregator, List<CsvOutputRecord> outputRecords1, List<CsvOutputRecord> outputRecords2) {
        aggregator.assertThat("Records Size", outputRecords1.size(), equalTo(outputRecords2.size()));
        aggregator.assertThat("Number of records", outputRecords1.size(), greaterThan(0));

        Map<String, CsvOutputRecord> cache1 = outputRecords1
                .stream()
                .collect(Collectors.toMap(this::makeKey, item -> item, (lhs, rhs) -> rhs));
        for (CsvOutputRecord item : outputRecords2) {
            ExtractedDataOutputRecord record2 = (ExtractedDataOutputRecord) item;
            String key = makeKey(record2);
            CsvOutputRecord cachedRecord = cache1.get(key);
            if (cachedRecord == null) {
                aggregator.assertThat("Could not find key in cache:  " + key, false);
            } else {
                ExtractedDataOutputRecord record1 = (ExtractedDataOutputRecord) cachedRecord;
                aggregator.assertThat(key + "- Test Status", record1.getTestStatus(), equalTo(record2.getTestStatus()));
                aggregator.assertThat(key + "- Test Status Value", record1.getTestStatus(), equalTo("FAIL"));
                aggregator.assertThat(key + "- Value", record1.getExpectedValue(), equalTo(record2.getActualValue()));
            }
        }
    }

    private void validateWorkflow(ExtractedWorkflowData expected, ExtractedWorkflowData actual) {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords1 = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords1);

        List<CsvOutputRecord> outputRecords2 = new ArrayList<>();
        actual.compare(expected, aggregator, outputRecords2);

        aggregator.reset();
        validateOutputRecords(aggregator, outputRecords1, outputRecords2);
        Helper.assertThat(aggregator);
    }

    private ExtractedWorkflowData getWorkflow(String flowname, String f1Value) {
        ExtractedPageData p1 = new ExtractedPageData();
        p1.setPageNameKey(WorkflowKeys.P1);
        p1.addField(WorkflowKeys.P1_F1, f1Value);

        ExtractedWorkflowData w1 = new ExtractedWorkflowData();
        w1.setFlowName(flowname);
        w1.addPage(p1);

        return w1;
    }

    @SuppressWarnings("java:S107")
    private ExtractedWorkflowData getWorkflow(
            String flowname,
            ColumnMapper t1Key,
            ColumnMapper r1f1Key,
            String r1f1Value,
            ColumnMapper r1f2Key,
            String r1f2Value,
            ColumnMapper r2f1Key,
            String r2f1Value,
            ColumnMapper r2f2Key,
            String r2f2Value
    ) {
        ExtractedRowData r1 = new ExtractedRowData();
        r1.addField(r1f1Key, r1f1Value);
        r1.addField(r1f2Key, r1f2Value);

        ExtractedRowData r2 = new ExtractedRowData();
        r2.addField(r2f1Key, r2f1Value);
        r2.addField(r2f2Key, r2f2Value);

        ExtractedTableData t1 = new ExtractedTableData();
        t1.setTableNameKey(t1Key);
        t1.addRow(r1);
        t1.addRow(r2);

        ExtractedPageData p1 = new ExtractedPageData();
        p1.setPageNameKey(WorkflowKeys.P1);
        p1.addTable(t1);

        ExtractedWorkflowData w1 = new ExtractedWorkflowData();
        w1.setFlowName(flowname);
        w1.addPage(p1);

        return w1;
    }

    @SuppressWarnings("java:S107")
    private ExtractedWorkflowData getWorkflow(
            String flowname,
            ColumnMapper t1Key,
            ColumnMapper r1f1Key,
            String r1f1Value,
            ColumnMapper r2f1Key,
            String r2f1Value,
            boolean extraR1,
            ColumnMapper extraRowKey,
            String extraRow
    ) {
        ExtractedRowData r1 = new ExtractedRowData();
        r1.addField(r1f1Key, r1f1Value);

        ExtractedRowData r2 = new ExtractedRowData();
        r2.addField(r2f1Key, r2f1Value);

        if (extraR1) {
            r1.addField(extraRowKey, extraRow);
        } else {
            r2.addField(extraRowKey, extraRow);
        }

        ExtractedTableData t1 = new ExtractedTableData();
        t1.setTableNameKey(t1Key);
        t1.addRow(r1);
        t1.addRow(r2);

        ExtractedPageData p1 = new ExtractedPageData();
        p1.setPageNameKey(WorkflowKeys.P1);
        p1.addTable(t1);

        ExtractedWorkflowData w1 = new ExtractedWorkflowData();
        w1.setFlowName(flowname);
        w1.addPage(p1);

        return w1;
    }

    private ExtractedWorkflowData getComplexSinglePageWorkFlow() {
        ExtractedRowData p10t1r1 = new ExtractedRowData();
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C1, "bb");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C2, "cc");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C3, "dd");

        ExtractedRowData p10t1r2 = new ExtractedRowData();
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C1, "ee");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C2, "ff");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C3, "gg");

        ExtractedRowData p10t1r3 = new ExtractedRowData();
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C1, "hh");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C2, "ii");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C3, "jj");

        ExtractedTableData p10t1 = new ExtractedTableData();
        p10t1.setTableNameKey(WorkflowKeys.P10_T1);
        p10t1.addRow(p10t1r1);
        p10t1.addRow(p10t1r2);
        p10t1.addRow(p10t1r3);

        ExtractedRowData p10t2r1 = new ExtractedRowData();
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C1, "ll");
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C2, "mm");

        ExtractedRowData p10t2r2 = new ExtractedRowData();
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C1, "nn");
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C2, "oo");

        ExtractedRowData p10t2r3 = new ExtractedRowData();
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C1, "pp");
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C2, "qq");

        ExtractedRowData p10t2r4 = new ExtractedRowData();
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C1, "rr");
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C2, "ss");

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

        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        expected.setFlowName("Complex Single Page WorkFlow");
        expected.addPage(p10);

        return expected;
    }

    private ExtractedWorkflowData getComplexMultiPageWorkFlow() {
        ExtractedPageData p1 = new ExtractedPageData();
        p1.setPageNameKey(WorkflowKeys.P1);
        p1.addField(WorkflowKeys.P1_F1, "aa");
        p1.addField(WorkflowKeys.P1_F2, "bb");

        ExtractedTableData p2t1 = new ExtractedTableData();
        p2t1.setTableNameKey(WorkflowKeys.P2_T1);

        ExtractedRowData p2t2r1 = new ExtractedRowData();
        p2t2r1.addField(WorkflowKeys.P2_T2_R1_C1, "cc");

        ExtractedTableData p2t2 = new ExtractedTableData();
        p2t2.setTableNameKey(WorkflowKeys.P2_T2);
        p2t2.addRow(p2t2r1);

        ExtractedRowData p2t3r1 = new ExtractedRowData();
        p2t3r1.addField(WorkflowKeys.P2_T3_R1_C1, "dd");

        ExtractedRowData p2t3r2 = new ExtractedRowData();
        p2t3r2.addField(WorkflowKeys.P2_T3_R2_C1, "ee");

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

        ExtractedTableData p3t1 = new ExtractedTableData();
        p3t1.setTableNameKey(WorkflowKeys.P3_T1);
        p3t1.addRow(p3t1r1);

        ExtractedPageData p3 = new ExtractedPageData();
        p3.setPageNameKey(WorkflowKeys.P3);
        p3.addField(WorkflowKeys.P3_F1, "ff");
        p3.addTable(p3t1);

        ExtractedPageData p4 = new ExtractedPageData();
        p4.setPageNameKey(WorkflowKeys.P4);
        p4.addField(WorkflowKeys.P4_F1, "ii");

        ExtractedTableData p5t1 = new ExtractedTableData();
        p5t1.setTableNameKey(WorkflowKeys.P5_T1);

        ExtractedPageData p5 = new ExtractedPageData();
        p5.setPageNameKey(WorkflowKeys.P5);
        p5.addTable(p5t1);

        ExtractedRowData p6t1r1 = new ExtractedRowData();
        p6t1r1.addField(WorkflowKeys.P6_T1_R1_C1, "jj");

        ExtractedRowData p6t1r2 = new ExtractedRowData();
        p6t1r2.addField(WorkflowKeys.P6_T1_R2_C1, "kk");

        ExtractedRowData p6t1r3 = new ExtractedRowData();
        p6t1r3.addField(WorkflowKeys.P6_T1_R3_C1, "ll");

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

        ExtractedRowData p7t1r2 = new ExtractedRowData();
        p7t1r2.addField(WorkflowKeys.P7_T1_R2_C1, "oo");
        p7t1r2.addField(WorkflowKeys.P7_T1_R2_C2, "pp");

        ExtractedRowData p7t2r1 = new ExtractedRowData();
        p7t2r1.addField(WorkflowKeys.P7_T2_R1_C1, "qq");
        p7t2r1.addField(WorkflowKeys.P7_T2_R1_C2, "rr");

        ExtractedRowData p7t2r2 = new ExtractedRowData();
        p7t2r2.addField(WorkflowKeys.P7_T2_R2_C1, "ss");
        p7t2r2.addField(WorkflowKeys.P7_T2_R2_C2, "tt");

        ExtractedRowData p7t2r3 = new ExtractedRowData();
        p7t2r3.addField(WorkflowKeys.P7_T2_R3_C1, "uu");
        p7t2r3.addField(WorkflowKeys.P7_T2_R3_C2, "ww");

        ExtractedRowData p7t3r1 = new ExtractedRowData();
        p7t3r1.addField(WorkflowKeys.P6_T1_R3_C1, "xx");

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

        ExtractedTableData p8t1 = new ExtractedTableData();
        p8t1.setTableNameKey(WorkflowKeys.P8_T1);
        p8t1.addRow(p8t1r1);

        ExtractedPageData p8 = new ExtractedPageData();
        p8.setPageNameKey(WorkflowKeys.P8);
        p8.addTable(p8t1);

        ExtractedRowData p9t1r1 = new ExtractedRowData();
        p9t1r1.addField(WorkflowKeys.P9_T1_R1_C1, "ccc");
        p9t1r1.addField(WorkflowKeys.P9_T1_R1_C2, "ddd");

        ExtractedTableData p9t1 = new ExtractedTableData();
        p9t1.setTableNameKey(WorkflowKeys.P9_T1);
        p9t1.addRow(p9t1r1);

        ExtractedRowData p9t2r1 = new ExtractedRowData();
        p9t2r1.addField(WorkflowKeys.P9_T2_R1_C1, "fff");
        p9t2r1.addField(WorkflowKeys.P9_T2_R1_C2, "ggg");

        ExtractedTableData p9t2 = new ExtractedTableData();
        p9t2.setTableNameKey(WorkflowKeys.P9_T2);
        p9t2.addRow(p9t2r1);

        ExtractedPageData p9 = new ExtractedPageData();
        p9.setPageNameKey(WorkflowKeys.P9);
        p9.addField(WorkflowKeys.P9_F1, "bbb");
        p9.addTable(p9t1);
        p9.addField(WorkflowKeys.P9_F2, "eee");
        p9.addTable(p9t2);

        ExtractedRowData p10t1r1 = new ExtractedRowData();
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C1, "iii");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C2, "jjj");
        p10t1r1.addField(WorkflowKeys.P10_T1_R1_C3, "aaaa");

        ExtractedRowData p10t1r2 = new ExtractedRowData();
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C1, "bbbb");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C2, "cccc");
        p10t1r2.addField(WorkflowKeys.P10_T1_R2_C3, "dddd");

        ExtractedRowData p10t1r3 = new ExtractedRowData();
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C1, "eeee");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C2, "ffff");
        p10t1r3.addField(WorkflowKeys.P10_T1_R3_C3, "gggg");

        ExtractedTableData p10t1 = new ExtractedTableData();
        p10t1.setTableNameKey(WorkflowKeys.P10_T1);
        p10t1.addRow(p10t1r1);
        p10t1.addRow(p10t1r2);
        p10t1.addRow(p10t1r3);

        ExtractedRowData p10t2r1 = new ExtractedRowData();
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C1, "hhhh");
        p10t2r1.addField(WorkflowKeys.P10_T2_R1_C2, "iiii");

        ExtractedRowData p10t2r2 = new ExtractedRowData();
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C1, "jjjj");
        p10t2r2.addField(WorkflowKeys.P10_T2_R2_C2, "kkkk");

        ExtractedRowData p10t2r3 = new ExtractedRowData();
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C1, "llll");
        p10t2r3.addField(WorkflowKeys.P10_T2_R3_C2, "mmmm");

        ExtractedRowData p10t2r4 = new ExtractedRowData();
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C1, "nnnn");
        p10t2r4.addField(WorkflowKeys.P10_T2_R4_C2, "oooo");

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

        return expected;
    }

    @Features("ExtractedWorkflowData")
    @Stories("Write To File")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void writeToFileTest() {
        ExtractedWorkflowData workflowData = getWorkflowData();

        String xml = workflowData.toXML();
        Helper.log(xml, true);

        workflowData.writeToFile(HOME + SEPARATOR + OUTPUT_FILE);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Read From File")
    @Severity(SeverityLevel.NORMAL)
    @Test(dependsOnMethods = "writeToFileTest")
    public void readFromFileTest() {
        ExtractedWorkflowData expected = getWorkflowData();
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(HOME + SEPARATOR + OUTPUT_FILE);
        assertThat(MISMATCH, actual, equalTo(expected));
    }

    @AfterClass(alwaysRun = true)
    public void deleteFileTest() {
        File file = new File(HOME + SEPARATOR + OUTPUT_FILE);
        FileUtils.deleteQuietly(file);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Empty Workflow")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void emptyTest() {
        ExtractedWorkflowData expected = new ExtractedWorkflowData();
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(EMPTY_WORKFLOW);

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);

        assertThat(OUTPUT_RECORDS, outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Single Empty Page")
    @Severity(SeverityLevel.NORMAL)
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

        assertThat(OUTPUT_RECORDS, outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Single Empty Table")
    @Severity(SeverityLevel.NORMAL)
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

        assertThat(OUTPUT_RECORDS, outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Single Empty Row")
    @Severity(SeverityLevel.NORMAL)
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

        assertThat(OUTPUT_RECORDS, outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Complex Single Page")
    @Severity(SeverityLevel.NORMAL)
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
        assertThat(MISMATCH, actual, equalTo(expected));

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);
        validateOutputRecords(aggregator, outputRecords, fieldCount);

        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Complex Multi-Page")
    @Severity(SeverityLevel.NORMAL)
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
        assertThat(MISMATCH, actual, equalTo(expected));

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);
        validateOutputRecords(aggregator, outputRecords, fieldCount);

        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Duplicate field keys")
    @Severity(SeverityLevel.NORMAL)
    @SuppressWarnings("java:S112")
    @Test
    public void duplicatesTest() {
        ExtractedPageData p1 = new ExtractedPageData();
        p1.setPageNameKey(WorkflowKeys.P1);
        p1.addField(WorkflowKeys.P1_F1, "aa");
        try {
            p1.addField(WorkflowKeys.P1_F1, "bb");
            throw new RuntimeException("Assertion did not fail for duplicate field key");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected for duplicate field key", true);
        }

        ExtractedRowData p7t1r1 = new ExtractedRowData();
        p7t1r1.addField(WorkflowKeys.P7_T1_R1_C1, "mm");
        try {
            p7t1r1.addField(WorkflowKeys.P7_T1_R1_C1, "nn");
            throw new RuntimeException("Assertion did not fail for duplicate field key on row");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected for duplicate field key on row", true);
        }

        ExtractedTableData p7t1 = new ExtractedTableData();
        p7t1.setTableNameKey(WorkflowKeys.P7_T1);
        p7t1.addRow(p7t1r1);
        p7t1.addRow(p7t1r1); // Duplicate rows are allows as stored in list
        p1.addTable(p7t1);

        ExtractedTableData p7t2 = new ExtractedTableData();
        p7t2.setTableNameKey(WorkflowKeys.P7_T1);
        p7t2.addRow(p7t1r1);
        try {
            p1.addTable(p7t2);
            throw new RuntimeException("Assertion did not fail for duplicate table key");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected for duplicate table key", true);
        }

        ExtractedPageData p2 = new ExtractedPageData();
        p2.setPageNameKey(WorkflowKeys.P1);
        p2.addField(WorkflowKeys.P1_F1, "bb");

        ExtractedWorkflowData w1 = new ExtractedWorkflowData();
        w1.setFlowName("w1");
        w1.addPage(p1);
        try {
            w1.addPage(p2);
            throw new RuntimeException("Assertion did not fail for duplicate page key");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected for duplicate page key", true);
        }
    }

    @Features("ExtractedWorkflowData")
    @Stories("Failure With Different Sizes")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void failureWithDifferentSizesTest() {
        ExtractedWorkflowData expected = new ExtractedWorkflowData().fromResource(COMPLEX_MULTI_PAGE_WORKFLOW);
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(COMPLEX_SINGLE_PAGE_WORKFLOW);
        validateWorkflow(expected, actual);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Failure With Equal Sizes")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void failureWithEqualSizesTest() {
        ExtractedWorkflowData expected = new ExtractedWorkflowData().fromResource(COMPLEX_SINGLE_PAGE_WORKFLOW);
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(COMPLEX_SINGLE_PAGE_WORKFLOW_2);
        validateWorkflow(expected, actual);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Failure With Equal Pages")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void failureWithEqualPagesTest() {
        ExtractedWorkflowData expected = getWorkflow(EXPECTED, "aa");
        ExtractedWorkflowData actual = getWorkflow(ACTUAL, "bb");
        validateWorkflow(expected, actual);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Failure With Equal Tables/Rows/Cells")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void failureWithEqualTablesRowsCellsTest() {
        ExtractedWorkflowData expected = getWorkflow(EXPECTED,
                WorkflowKeys.P10_T1,
                WorkflowKeys.P10_T1_R1_C1, "aa",
                WorkflowKeys.P10_T1_R1_C2, "bb",
                WorkflowKeys.P10_T1_R2_C1, "cc",
                WorkflowKeys.P10_T1_R2_C2, "dd");
        ExtractedWorkflowData actual = getWorkflow(ACTUAL,
                WorkflowKeys.P10_T1,
                WorkflowKeys.P10_T1_R1_C1, "dd",
                WorkflowKeys.P10_T1_R1_C2, "cc",
                WorkflowKeys.P10_T1_R2_C1, "bb",
                WorkflowKeys.P10_T1_R2_C2, "aa");
        validateWorkflow(expected, actual);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Failure With Different Size Cells")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void failureWithDifferentSizeCellsTest() {
        ExtractedWorkflowData expected = getWorkflow(EXPECTED,
                WorkflowKeys.P10_T1,
                WorkflowKeys.P10_T1_R1_C1, "aa",
                WorkflowKeys.P10_T1_R2_C2, "bb",
                true,
                WorkflowKeys.P10_T2_R1_C1, "cc"
        );
        ExtractedWorkflowData actual = getWorkflow(ACTUAL,
                WorkflowKeys.P10_T1,
                WorkflowKeys.P10_T1_R1_C1, "aa",
                WorkflowKeys.P10_T1_R2_C2, "bb",
                false,
                WorkflowKeys.P10_T2_R1_C1, "cc"
        );

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords1 = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords1);

        List<CsvOutputRecord> outputRecords2 = new ArrayList<>();
        actual.compare(expected, aggregator, outputRecords2);

        aggregator.reset();
        aggregator.assertThat("Records Size", outputRecords1.size(), equalTo(outputRecords2.size()));
        aggregator.assertThat("Number of records", outputRecords1.size(), greaterThan(0));

        Map<String, CsvOutputRecord> cache1 = outputRecords1
                .stream()
                .collect(Collectors.toMap(this::makeKey, item -> item, (lhs, rhs) -> rhs));
        for (CsvOutputRecord item : outputRecords2) {
            ExtractedDataOutputRecord record2 = (ExtractedDataOutputRecord) item;
            String key = makeKey(record2);
            CsvOutputRecord cachedRecord = cache1.get(key);
            if (cachedRecord == null) {
                aggregator.assertThat("Could not find key in cache:  " + key, false);
            } else {
                ExtractedDataOutputRecord record1 = (ExtractedDataOutputRecord) cachedRecord;
                aggregator.assertThat(key + "- Test Status", record1.getTestStatus(), equalTo(record2.getTestStatus()));
                aggregator.assertThat(key + "- Value", record1.getExpectedValue(), equalTo(record2.getActualValue()));
            }
        }

        Helper.assertThat(aggregator);
    }

    @Features("ExtractedWorkflowData")
    @Stories("Get Data")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getDataTest() {
        ExtractedTableData p2t1 = new ExtractedTableData();
        p2t1.setTableNameKey(WorkflowKeys.P2_T1);

        ExtractedRowData p2t2r1 = new ExtractedRowData();
        p2t2r1.addField(WorkflowKeys.P2_T2_R1_C1, "cc");

        ExtractedTableData p2t2 = new ExtractedTableData();
        p2t2.setTableNameKey(WorkflowKeys.P2_T2);
        p2t2.addRow(p2t2r1);

        ExtractedRowData p2t3r1 = new ExtractedRowData();
        p2t3r1.addField(WorkflowKeys.P2_T3_R1_C1, "dd");

        ExtractedRowData p2t3r2 = new ExtractedRowData();
        p2t3r2.addField(WorkflowKeys.P2_T3_R2_C1, "ee");

        ExtractedTableData p2t3 = new ExtractedTableData();
        p2t3.setTableNameKey(WorkflowKeys.P2_T3);
        p2t3.addRow(p2t3r1);
        p2t3.addRow(p2t3r2);

        ExtractedPageData p2 = new ExtractedPageData();
        p2.setPageNameKey(WorkflowKeys.P2);
        p2.addTable(p2t1);
        p2.addTable(p2t2);
        p2.addTable(p2t3);

        ExtractedPageData p3 = new ExtractedPageData();
        p3.setPageNameKey(WorkflowKeys.P3);
        p3.addField(WorkflowKeys.P3_F1, "ff");

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource(COMPLEX_MULTI_PAGE_WORKFLOW);

        ExtractedPageData actualP2 = (ExtractedPageData) actual.get(WorkflowKeys.P2);
        assertThat("p2", actualP2, equalTo(p2));

        ExtractedTableData actualP2T2 = (ExtractedTableData) actual.get(WorkflowKeys.P2_T2);
        assertThat("p2t2", actualP2T2, equalTo(p2t2));

        // Only pages are searched recursively
        String actualP2T3R1 = (String) actual.get(WorkflowKeys.P2_T3_R2_C1);
        assertThat("p2t3r2", actualP2T3R1, nullValue());

        String actualP3F1 = (String) actual.get(WorkflowKeys.P3_F1);
        assertThat("p3f1", actualP3F1, equalTo(p3.get(WorkflowKeys.P3_F1)));
    }

    @Features("ExtractedWorkflowData")
    @Stories("Remove Data")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void removeDataTest() {
        ExtractedWorkflowData data = new ExtractedWorkflowData().fromResource(COMPLEX_MULTI_PAGE_WORKFLOW);
        assertThat("Exists p1", data.get(WorkflowKeys.P1), notNullValue());
        assertThat("Exists p2", data.get(WorkflowKeys.P2), notNullValue());
        assertThat("Exists p3", data.get(WorkflowKeys.P3), notNullValue());
        assertThat("Exists p4", data.get(WorkflowKeys.P4), notNullValue());
        assertThat("Exists p5", data.get(WorkflowKeys.P5), notNullValue());
        assertThat("Exists p6", data.get(WorkflowKeys.P6), notNullValue());
        assertThat("Exists p7", data.get(WorkflowKeys.P7), notNullValue());
        assertThat("Exists p8", data.get(WorkflowKeys.P8), notNullValue());
        assertThat("Exists p9", data.get(WorkflowKeys.P9), notNullValue());
        assertThat("Exists p10", data.get(WorkflowKeys.P10), notNullValue());

        assertThat("Remove p1", data.remove(WorkflowKeys.P1), notNullValue());
        assertThat("Remove p2", data.remove(WorkflowKeys.P2), notNullValue());
        assertThat("Remove p3", data.remove(WorkflowKeys.P3), notNullValue());
        assertThat("Remove p4", data.remove(WorkflowKeys.P4), notNullValue());
        assertThat("Remove p5", data.remove(WorkflowKeys.P5), notNullValue());
        assertThat("Remove p6", data.remove(WorkflowKeys.P6), notNullValue());
        assertThat("Remove p7", data.remove(WorkflowKeys.P7), notNullValue());
        assertThat("Remove p8", data.remove(WorkflowKeys.P8), notNullValue());
        assertThat("Remove p9", data.remove(WorkflowKeys.P9), notNullValue());
        assertThat("Remove p10", data.remove(WorkflowKeys.P10), notNullValue());

        assertThat("Get p1", data.get(WorkflowKeys.P1), nullValue());
        assertThat("Get p2", data.get(WorkflowKeys.P2), nullValue());
        assertThat("Get p3", data.get(WorkflowKeys.P3), nullValue());
        assertThat("Get p4", data.get(WorkflowKeys.P4), nullValue());
        assertThat("Get p5", data.get(WorkflowKeys.P5), nullValue());
        assertThat("Get p6", data.get(WorkflowKeys.P6), nullValue());
        assertThat("Get p7", data.get(WorkflowKeys.P7), nullValue());
        assertThat("Get p8", data.get(WorkflowKeys.P8), nullValue());
        assertThat("Get p9", data.get(WorkflowKeys.P9), nullValue());
        assertThat("Get p10", data.get(WorkflowKeys.P10), nullValue());

        assertThat("Remove Again p1", data.remove(WorkflowKeys.P1), nullValue());
        assertThat("Remove Again p2", data.remove(WorkflowKeys.P2), nullValue());
        assertThat("Remove Again p3", data.remove(WorkflowKeys.P3), nullValue());
        assertThat("Remove Again p4", data.remove(WorkflowKeys.P4), nullValue());
        assertThat("Remove Again p5", data.remove(WorkflowKeys.P5), nullValue());
        assertThat("Remove Again p6", data.remove(WorkflowKeys.P6), nullValue());
        assertThat("Remove Again p7", data.remove(WorkflowKeys.P7), nullValue());
        assertThat("Remove Again p8", data.remove(WorkflowKeys.P8), nullValue());
        assertThat("Remove Again p9", data.remove(WorkflowKeys.P9), nullValue());
        assertThat("Remove Again p10", data.remove(WorkflowKeys.P10), nullValue());
    }

    @Features("ExtractedWorkflowData")
    @Stories("Get Data Recursive")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void getDataRecursiveTest() {
        final String level1field1 = "a";
        final String level2field1 = "bb";
        final String level3field1 = "ccc";
        final String level4field1 = "dddd";
        final String notSearchedField1 = "eeee";
        final String notSearchedField2 = "ffff";
        final String notSearchedField3 = "gggg";

        ExtractedPageData notSearchedPage1 = new ExtractedPageData();
        notSearchedPage1.setPageNameKey(WorkflowKeys.NOT_SEARCHED_ROW1);
        notSearchedPage1.addField(WorkflowKeys.NOT_SEARCHED_FIELD3, notSearchedField3);

        ExtractedRowData notSearchedRow1 = new ExtractedRowData();
        notSearchedRow1.addField(WorkflowKeys.NOT_SEARCHED_FIELD1, notSearchedField1);
        notSearchedRow1.addField(WorkflowKeys.NOT_SEARCHED_FIELD2, notSearchedField2);
        notSearchedRow1.addField(WorkflowKeys.NOT_SEARCHED_PAGE1, notSearchedPage1);

        ExtractedTableData level4table1 = new ExtractedTableData();
        level4table1.setTableNameKey(WorkflowKeys.LEVEL4_TABLE1);
        level4table1.addRow(notSearchedRow1);

        ExtractedPageData level4page1 = new ExtractedPageData();
        level4page1.setPageNameKey(WorkflowKeys.LEVEL4);
        level4page1.addField(WorkflowKeys.LEVEL4_FIELD1, level4field1);
        level4page1.addTable(level4table1);

        ExtractedTableData level3table1 = new ExtractedTableData();
        level3table1.setTableNameKey(WorkflowKeys.LEVEL3_TABLE1);

        ExtractedPageData level3page1 = new ExtractedPageData();
        level3page1.setPageNameKey(WorkflowKeys.LEVEL3);
        level3page1.addField(WorkflowKeys.LEVEL3_FIELD1, level3field1);
        level3page1.addTable(level3table1);
        level3page1.addField(WorkflowKeys.LEVEL3_PAGE1, level4page1);

        ExtractedTableData level2table1 = new ExtractedTableData();
        level2table1.setTableNameKey(WorkflowKeys.LEVEL2_TABLE1);

        ExtractedPageData level2page1 = new ExtractedPageData();
        level2page1.setPageNameKey(WorkflowKeys.LEVEL2);
        level2page1.addField(WorkflowKeys.LEVEL2_FIELD1, level2field1);
        level2page1.addTable(level2table1);
        level2page1.addField(WorkflowKeys.LEVEL2_PAGE1, level3page1);

        ExtractedTableData level1table1 = new ExtractedTableData();
        level1table1.setTableNameKey(WorkflowKeys.LEVEL1_TABLE1);

        ExtractedPageData level1page1 = new ExtractedPageData();
        level1page1.setPageNameKey(WorkflowKeys.LEVEL1);
        level1page1.addField(WorkflowKeys.LEVEL1_FIELD1, level1field1);
        level1page1.addTable(level1table1);
        level1page1.addField(WorkflowKeys.LEVEL1_PAGE1, level2page1);

        ExtractedWorkflowData data = new ExtractedWorkflowData();
        data.addPage(level1page1);

        assertThat(WorkflowKeys.LEVEL1.getColumnName(), data.get(WorkflowKeys.LEVEL1), equalTo(level1page1));
        assertThat(WorkflowKeys.LEVEL1_TABLE1.getColumnName(), data.get(WorkflowKeys.LEVEL1_TABLE1), equalTo(level1table1));
        assertThat(WorkflowKeys.LEVEL1_FIELD1.getColumnName(), data.get(WorkflowKeys.LEVEL1_FIELD1), equalTo(level1field1));
        assertThat(WorkflowKeys.LEVEL1_PAGE1.getColumnName(), data.get(WorkflowKeys.LEVEL1_PAGE1), equalTo(level2page1));

        assertThat(WorkflowKeys.LEVEL2.getColumnName(), data.get(WorkflowKeys.LEVEL2), equalTo(level2page1));
        assertThat(WorkflowKeys.LEVEL2_TABLE1.getColumnName(), data.get(WorkflowKeys.LEVEL2_TABLE1), equalTo(level2table1));
        assertThat(WorkflowKeys.LEVEL2_FIELD1.getColumnName(), data.get(WorkflowKeys.LEVEL2_FIELD1), equalTo(level2field1));
        assertThat(WorkflowKeys.LEVEL2_PAGE1.getColumnName(), data.get(WorkflowKeys.LEVEL2_PAGE1), equalTo(level3page1));

        assertThat(WorkflowKeys.LEVEL3.getColumnName(), data.get(WorkflowKeys.LEVEL3), equalTo(level3page1));
        assertThat(WorkflowKeys.LEVEL3_TABLE1.getColumnName(), data.get(WorkflowKeys.LEVEL3_TABLE1), equalTo(level3table1));
        assertThat(WorkflowKeys.LEVEL3_FIELD1.getColumnName(), data.get(WorkflowKeys.LEVEL3_FIELD1), equalTo(level3field1));
        assertThat(WorkflowKeys.LEVEL3_PAGE1.getColumnName(), data.get(WorkflowKeys.LEVEL3_PAGE1), equalTo(level4page1));

        assertThat(WorkflowKeys.LEVEL4.getColumnName(), data.get(WorkflowKeys.LEVEL4), equalTo(level4page1));
        assertThat(WorkflowKeys.LEVEL4_TABLE1.getColumnName(), data.get(WorkflowKeys.LEVEL4_TABLE1), equalTo(level4table1));
        assertThat(WorkflowKeys.LEVEL4_FIELD1.getColumnName(), data.get(WorkflowKeys.LEVEL4_FIELD1), equalTo(level4field1));

        assertThat(WorkflowKeys.NOT_SEARCHED_FIELD1.getColumnName(), data.get(WorkflowKeys.NOT_SEARCHED_FIELD1), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_FIELD2.getColumnName(), data.get(WorkflowKeys.NOT_SEARCHED_FIELD2), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_FIELD3.getColumnName(), data.get(WorkflowKeys.NOT_SEARCHED_FIELD3), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_PAGE1.getColumnName(), data.get(WorkflowKeys.NOT_SEARCHED_PAGE1), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_ROW1.getColumnName(), data.get(WorkflowKeys.NOT_SEARCHED_ROW1), nullValue());

        //
        // Removal Tests
        //
        assertThat(WorkflowKeys.NOT_SEARCHED_FIELD1.getColumnName(), data.remove(WorkflowKeys.NOT_SEARCHED_FIELD1), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_FIELD2.getColumnName(), data.remove(WorkflowKeys.NOT_SEARCHED_FIELD2), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_FIELD3.getColumnName(), data.remove(WorkflowKeys.NOT_SEARCHED_FIELD3), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_PAGE1.getColumnName(), data.remove(WorkflowKeys.NOT_SEARCHED_PAGE1), nullValue());
        assertThat(WorkflowKeys.NOT_SEARCHED_ROW1.getColumnName(), data.remove(WorkflowKeys.NOT_SEARCHED_ROW1), nullValue());

        assertThat(WorkflowKeys.LEVEL4_FIELD1.getColumnName(), data.remove(WorkflowKeys.LEVEL4_FIELD1), equalTo(level4field1));
        assertThat(WorkflowKeys.LEVEL4_TABLE1.getColumnName(), data.remove(WorkflowKeys.LEVEL4_TABLE1), equalTo(level4table1));

        assertThat(WorkflowKeys.LEVEL3_PAGE1.getColumnName(), data.remove(WorkflowKeys.LEVEL3_PAGE1), equalTo(level4page1));
        assertThat(WorkflowKeys.LEVEL3_FIELD1.getColumnName(), data.remove(WorkflowKeys.LEVEL3_FIELD1), equalTo(level3field1));
        assertThat(WorkflowKeys.LEVEL3_TABLE1.getColumnName(), data.remove(WorkflowKeys.LEVEL3_TABLE1), equalTo(level3table1));
        assertThat(WorkflowKeys.LEVEL3.getColumnName(), data.remove(WorkflowKeys.LEVEL3), equalTo(level3page1));

        assertThat(WorkflowKeys.LEVEL2_FIELD1.getColumnName(), data.remove(WorkflowKeys.LEVEL2_FIELD1), equalTo(level2field1));
        assertThat(WorkflowKeys.LEVEL2_TABLE1.getColumnName(), data.remove(WorkflowKeys.LEVEL2_TABLE1), equalTo(level2table1));

        assertThat(WorkflowKeys.LEVEL1_PAGE1.getColumnName(), data.remove(WorkflowKeys.LEVEL1_PAGE1), equalTo(level2page1));
        assertThat(WorkflowKeys.LEVEL1_FIELD1.getColumnName(), data.remove(WorkflowKeys.LEVEL1_FIELD1), equalTo(level1field1));
        assertThat(WorkflowKeys.LEVEL1_TABLE1.getColumnName(), data.remove(WorkflowKeys.LEVEL1_TABLE1), equalTo(level1table1));
        assertThat(WorkflowKeys.LEVEL1.getColumnName(), data.remove(WorkflowKeys.LEVEL1), equalTo(level1page1));
    }

    @Features("Helper")
    @Stories("Validate Complex Lists assertThat functions properly")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performComplexAssertThatTest() {
        ExtractedWorkflowData expectedItem1 = getComplexSinglePageWorkFlow();
        ExtractedWorkflowData expectedItem2 = getComplexMultiPageWorkFlow();
        List<ExtractedWorkflowData> expected = new ArrayList<>();
        expected.add(expectedItem1);
        expected.add(expectedItem2);

        ExtractedWorkflowData actualItem1 = new ExtractedWorkflowData().fromResource(COMPLEX_SINGLE_PAGE_WORKFLOW);
        ExtractedWorkflowData actualItem2 = new ExtractedWorkflowData().fromResource(COMPLEX_MULTI_PAGE_WORKFLOW);
        List<ExtractedWorkflowData> actual = new ArrayList<>();
        actual.add(actualItem1);
        actual.add(actualItem2);

        Helper.assertThat(actual, expected);
    }

    @Features("Helper")
    @Stories("Validate Complex Lists assertThatSubset functions properly")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performComplexAssertThatSubsetTest() {
        ExtractedWorkflowData actualItem1 = new ExtractedWorkflowData().fromResource(COMPLEX_SINGLE_PAGE_WORKFLOW);
        ExtractedWorkflowData actualItem2 = new ExtractedWorkflowData().fromResource(COMPLEX_MULTI_PAGE_WORKFLOW);
        List<ExtractedWorkflowData> actual = new ArrayList<>();
        actual.add(actualItem1);
        actual.add(actualItem2);

        ExtractedWorkflowData subsetItem1 = getComplexSinglePageWorkFlow();
        ExtractedWorkflowData subsetItem2 = getComplexMultiPageWorkFlow();
        List<ExtractedWorkflowData> subset = new ArrayList<>();
        subset.add(subsetItem1);

        Helper.assertThatSubset(actual, subset);

        subset.add(subsetItem2);
        Helper.assertThatSubset(actual, subset);
    }

}
