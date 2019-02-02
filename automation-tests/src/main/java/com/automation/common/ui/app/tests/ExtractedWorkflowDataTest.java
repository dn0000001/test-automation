package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
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
        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource("data/ui/scraping/empty-workflow.xml");

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

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource("data/ui/scraping/single-empty-page.xml");

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

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource("data/ui/scraping/single-empty-table.xml");

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

        ExtractedWorkflowData actual = new ExtractedWorkflowData().fromResource("data/ui/scraping/single-empty-row.xml");

        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true);

        List<CsvOutputRecord> outputRecords = new ArrayList<>();
        expected.compare(actual, aggregator, outputRecords);

        assertThat("Output Records", outputRecords.isEmpty());
        Helper.assertThat(aggregator);
    }

}
