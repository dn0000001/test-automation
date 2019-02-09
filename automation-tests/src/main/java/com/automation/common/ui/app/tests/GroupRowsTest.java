package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.FilloUtils;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.csv.GroupRow;
import com.taf.automation.ui.support.pageScraping.ExtractedDataOutputRecord;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class GroupRowsTest {
    private static final String PASS = "PASS";
    private static final String FAIL = "FAIL";
    private static final String PAGE1 = "p1";
    private static final String PAGE2 = "p2";
    private static final String PAGE3 = "p3";
    private static final String NOT_SUMMARY = "Not Summary";
    private static final String[] HEADERS = {"ROW", "PAGE NAME", "FIELD TYPE", "FIELD NAME", "EXPECTED", "ACTUAL", "STATUS"};
    private static final String FILENAME = "temp-group-rows.xlsx";
    private static final String WORKSHEET = "Sheet 1";

    private void runAndValidate(List<CsvOutputRecord> records, List<GroupRow> groupRows, String... status) {
        int recordsCount = records.size();
        FilloUtils.addPageLevelSummaries(records, groupRows);
        assertThat("Records", records.size(), equalTo(recordsCount + status.length));
        assertThat("Group Rows", groupRows.size(), equalTo(status.length));
        for (int i = 0; i < groupRows.size(); i++) {
            int index = groupRows.get(i).getFromRow();
            ExtractedDataOutputRecord summary = (ExtractedDataOutputRecord) records.get(index);
            assertThat("Summary Status[" + i + "]", summary.getTestStatus(), equalTo(status[i]));
            // Field Name is not set on the summary record
            assertThat("Summary Field Name[" + i + "]", summary.getFieldName(), nullValue());
        }
    }

    private void createExcel(List<CsvOutputRecord> records, List<GroupRow> groupRows) {
        ExtractedDataOutputRecord result1 = new ExtractedDataOutputRecord();
        result1.setPageName(PAGE1);
        result1.setTestStatus(PASS);
        result1.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result2 = new ExtractedDataOutputRecord();
        result2.setPageName(PAGE1);
        result2.setTestStatus(PASS);
        result2.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result3 = new ExtractedDataOutputRecord();
        result3.setPageName(PAGE2);
        result3.setTestStatus(PASS);
        result3.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result4 = new ExtractedDataOutputRecord();
        result4.setPageName(PAGE2);
        result4.setTestStatus(PASS);
        result4.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result5 = new ExtractedDataOutputRecord();
        result5.setPageName(PAGE2);
        result5.setTestStatus(FAIL);
        result5.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result6 = new ExtractedDataOutputRecord();
        result6.setPageName(PAGE2);
        result6.setTestStatus(PASS);
        result6.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result7 = new ExtractedDataOutputRecord();
        result7.setPageName(PAGE3);
        result7.setTestStatus(PASS);
        result7.setFieldName(NOT_SUMMARY);

        ExtractedDataOutputRecord result8 = new ExtractedDataOutputRecord();
        result8.setPageName(PAGE3);
        result8.setTestStatus(PASS);
        result8.setFieldName(NOT_SUMMARY);

        records.add(result1);
        records.add(result2);
        records.add(result3);
        records.add(result4);
        records.add(result5);
        records.add(result6);
        records.add(result7);
        records.add(result8);

        runAndValidate(records, groupRows, PASS, FAIL, PASS);
        FilloUtils.writeToExcel(FILENAME, WORKSHEET, false, HEADERS, records);
    }

    @Test
    public void performGroupRowsTest() {
        List<CsvOutputRecord> records = new ArrayList<>();
        List<GroupRow> groupRows = new ArrayList<>();
        createExcel(records, groupRows);
        FilloUtils.groupRows(FILENAME, WORKSHEET, groupRows, true, true, false);
        for (GroupRow item : groupRows) {
            int fromRow = item.getFromRow() + 2;
            int toRow = item.getToRow() + 2;
            Helper.log("[" + fromRow + ", " + toRow + "]", true);
        }
    }

}
