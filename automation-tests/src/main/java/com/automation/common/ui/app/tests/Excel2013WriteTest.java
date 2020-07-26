package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.FilloUtils;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Example test using a data provider that reads an excel (2013) file
 */
public class Excel2013WriteTest extends TestNGBase {
    private static final String WORKSHEET = "Sheet 1";
    private static final String[] HEADER_ROW = {"ROW", "First", "Last", "Phone"};
    private List<CsvOutputRecord> outputRecords;
    private Mutable<String> existingFile;

    private class OutputInfo implements CsvOutputRecord {
        private String row;
        private String first;
        private String last;
        private String phone;

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public List<String> asList() {
            List<String> columns = new ArrayList<>();
            columns.add(getRow());
            columns.add(getFirst());
            columns.add(getLast());
            columns.add(getPhone());
            return columns;
        }

    }

    private void createTestData() {
        createTestData(0, 5);
    }

    private void createTestData(int start, int stop) {
        outputRecords = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            OutputInfo item = new OutputInfo();
            item.setRow("" + (i + 1));
            item.setFirst(Rand.letters(10));
            item.setLast(Rand.letters(20));
            item.setPhone(Rand.numbers(3) + "-" + Rand.numbers(3) + "-" + Rand.numbers(4));
            outputRecords.add(item);
        }
    }

    private String writeToCSV() {
        CSVFormat format = CSVFormat.EXCEL.withHeader(HEADER_ROW);
        SimpleDateFormat formatter = new SimpleDateFormat("hh_mm_ss");
        String strDateStamp = formatter.format(new Date());
        String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "test-csv-file-" + strDateStamp + ".csv";
        CsvUtils.writeToCSV(filename, format, outputRecords);
        return filename;
    }

    private String writeToExcel() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh_mm_ss");
        String strDateStamp = formatter.format(new Date());
        String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "test-excel-file-" + strDateStamp + ".xlsx";
        FilloUtils.writeToExcel(filename, WORKSHEET, false, HEADER_ROW, outputRecords);
        return filename;
    }

    private void setExistingFile(String excelFile) {
        if (existingFile == null) {
            existingFile = new MutableObject<>();
        }

        existingFile.setValue(excelFile);
    }

    @Test
    public void performExcelWriteTest() {
        createTestData();
        String csvFile = writeToCSV();
        String excelFile = writeToExcel();
        setExistingFile(excelFile);

        List<CSVRecord> csvRecords = new ArrayList<>();
        Map<String, Integer> csvHeaderMap = new HashMap<>();
        CsvUtils.read(csvFile, csvRecords, csvHeaderMap);

        List<CSVRecord> excelRecords = new ArrayList<>();
        Map<String, Integer> excelHeaderMap = new HashMap<>();
        FilloUtils.read(excelFile, "Sheet 1", excelRecords, excelHeaderMap);

        assertThat("Headers", csvHeaderMap, equalTo(excelHeaderMap));
        assertThat("Size", csvRecords.size(), equalTo(excelRecords.size()));
        for (int i = 0; i < csvRecords.size(); i++) {
            CSVRecord expected = csvRecords.get(i);
            CSVRecord actual = excelRecords.get(i);
            assertThat("Row " + (i + 1), actual.toMap(), equalTo(expected.toMap()));
        }
    }

    @Test(dependsOnMethods = "performExcelWriteTest")
    public void performExcelUpdateTest() {
        createTestData(5, 10);
        FilloUtils.writeToExcel(existingFile.getValue(), WORKSHEET, true, HEADER_ROW, outputRecords);

        createTestData(10, 15);
        FilloUtils.writeToExcel(existingFile.getValue(), WORKSHEET, true, HEADER_ROW, outputRecords);

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        FilloUtils.read(existingFile.getValue(), WORKSHEET, records, headers);
        assertThat("Total Rows (minus header row)", records.size(), equalTo(15));
        assertThat("Headers", headers.size(), equalTo(HEADER_ROW.length));
        for (String header : HEADER_ROW) {
            assertThat(header, headers.containsKey(header));
        }
    }

}
