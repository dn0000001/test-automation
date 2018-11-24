package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.csv.CSVFormat;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Example test that writes a csv file
 */
public class CsvWriteTest extends TestNGBase {
    private static final String[] HEADER_ROW = {"ROW", "First", "Last", "Phone"};
    private List<CsvOutputRecord> outputRecords;

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
        outputRecords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OutputInfo item = new OutputInfo();
            item.setRow("" + (i + 1));
            item.setFirst(Rand.letters(10));
            item.setLast(Rand.letters(20));
            item.setPhone(Rand.numbers(3) + "-" + Rand.numbers(3) + "-" + Rand.numbers(4));
            outputRecords.add(item);
        }
    }

    private void writeToCSV() {
        CSVFormat format = CSVFormat.EXCEL.withHeader(HEADER_ROW);
        SimpleDateFormat formatter = new SimpleDateFormat("hh_mm_ss");
        String strDateStamp = formatter.format(new Date());
        String filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "test-csv-file-" + strDateStamp + ".csv";
        CsvUtils.writeToCSV(filename, format, outputRecords);
    }

    @Test
    public void performCsvWriteTest() {
        createTestData();
        writeToCSV();
    }

}
