package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.csv.GroupRow;
import com.taf.automation.ui.support.pageScraping.ExtractedDataOutputRecord;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.FilloUtils;
import com.taf.automation.ui.support.util.Helper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@Listeners(AllureTestNGListener.class)
public class GroupRowsTest {
    private static final String HOME = System.getProperty("user.home");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String PASS = "PASS";
    private static final String FAIL = "FAIL";
    private static final String PAGE1 = "p1";
    private static final String PAGE2 = "p2";
    private static final String PAGE3 = "p3";
    private static final String NOT_SUMMARY = "Not Summary";
    private static final String[] HEADERS = {"ROW", "PAGE NAME", "FIELD TYPE", "FIELD NAME", "EXPECTED", "ACTUAL", "STATUS"};
    private static final String EXPANDED = "temp-group-rows-expanded.xlsx";
    private static final String COLLAPSED = "temp-group-rows-collasped.xlsx";
    private static final String MIXED = "temp-group-rows-mixed.xlsx";
    private static final String WORKSHEET = "Sheet 1";

    private void runAndValidate(List<CsvOutputRecord> records, List<GroupRow> groupRows, String... status) {
        int recordsCount = records.size();
        FilloUtils.addPageLevelSummaries(records, groupRows);
        assertThat("Records", records.size(), equalTo(recordsCount + status.length));
        assertThat("Group Rows", groupRows.size(), equalTo(status.length));
        for (int i = 0; i < groupRows.size(); i++) {
            int index = groupRows.get(i).getSummaryRow();
            ExtractedDataOutputRecord summary = (ExtractedDataOutputRecord) records.get(index);
            assertThat("Summary Status[" + i + "]", summary.getTestStatus(), equalTo(status[i]));
            // Field Name is not set on the summary record
            assertThat("Summary Field Name[" + i + "]", summary.getFieldName(), nullValue());
        }
    }

    @Step("Create Excel:  {0}")
    private void createExcel(String filename, String worksheet, List<CsvOutputRecord> records, List<GroupRow> groupRows) {
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

        records.add(result1);
        records.add(result2);
        records.add(result3);
        records.add(result4);
        records.add(result5);
        records.add(result6);
        records.add(result7);

        runAndValidate(records, groupRows, PASS, FAIL, PASS);
        FilloUtils.writeToExcel(filename, worksheet, false, HEADERS, records);
    }

    private void logGroupRowInfo(String test, List<GroupRow> groupRows) {
        StringBuilder sb = new StringBuilder();

        for (GroupRow item : groupRows) {
            int summaryRow = item.getSummaryRow() + 2;
            int fromRow = item.getFromRow() + 2;
            int toRow = item.getToRow() + 2;
            sb.append("[");
            sb.append(summaryRow);
            sb.append(", ");
            sb.append(fromRow);
            sb.append("-");
            sb.append(toRow);
            sb.append(", ");
            sb.append(item.isCollapsed());
            sb.append("], ");
        }

        Helper.log(test + ":  " + StringUtils.removeEnd(sb.toString(), ", "), true);
    }

    @Features("FilloUtils")
    @Stories("Group Rows Expanded")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void performGroupRowsExpandedTest() {
        List<CsvOutputRecord> records = new ArrayList<>();
        List<GroupRow> groupRows = new ArrayList<>();
        createExcel(HOME + SEPARATOR + EXPANDED, WORKSHEET, records, groupRows);
        FilloUtils.groupRows(HOME + SEPARATOR + EXPANDED, WORKSHEET, groupRows, true);
        logGroupRowInfo("Expanded Test", groupRows);
    }

    @Features("FilloUtils")
    @Stories("Group Rows Collapsed")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void performGroupRowsCollapsedTest() {
        List<CsvOutputRecord> records = new ArrayList<>();
        List<GroupRow> groupRows = new ArrayList<>();
        createExcel(HOME + SEPARATOR + COLLAPSED, WORKSHEET, records, groupRows);

        // Set all the rows to be collapsed
        for (GroupRow item : groupRows) {
            item.withCollapsed(true);
        }

        FilloUtils.groupRows(HOME + SEPARATOR + COLLAPSED, WORKSHEET, groupRows, true);
        logGroupRowInfo("Collapsed Test", groupRows);
    }

    @Features("FilloUtils")
    @Stories("Group Rows Mixed")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void performGroupRowsMixedTest() {
        List<CsvOutputRecord> records = new ArrayList<>();
        List<GroupRow> groupRows = new ArrayList<>();
        createExcel(HOME + SEPARATOR + MIXED, WORKSHEET, records, groupRows);

        // Set a random row to be collapsed
        Collections.shuffle(groupRows);
        groupRows.get(0).withCollapsed(true);

        FilloUtils.groupRows(HOME + SEPARATOR + MIXED, WORKSHEET, groupRows, true);
        logGroupRowInfo("Mixed Test", groupRows);
    }

    @AfterClass(alwaysRun = true)
    public void deleteFileTest() {
        File fileEXPANDED = new File(HOME + SEPARATOR + EXPANDED);
        FileUtils.deleteQuietly(fileEXPANDED);

        File fileCOLLAPSED = new File(HOME + SEPARATOR + COLLAPSED);
        FileUtils.deleteQuietly(fileCOLLAPSED);

        File fileMIXED = new File(HOME + SEPARATOR + MIXED);
        FileUtils.deleteQuietly(fileMIXED);
    }

}
