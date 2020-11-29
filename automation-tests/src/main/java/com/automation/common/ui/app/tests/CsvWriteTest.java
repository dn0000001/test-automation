package com.automation.common.ui.app.tests;

import com.taf.automation.locking.InterProcessBasicMutex;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Example test that writes a csv file
 */
public class CsvWriteTest extends TestNGBase {
    private static final String[] HEADER_ROW = {"ROW", "First", "Last", "Phone"};
    private List<CsvOutputRecord> outputRecords;

    private static class OutputInfo implements CsvOutputRecord {
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

    private void createTestData(String rowPrefix, int records, int delay) {
        outputRecords = new ArrayList<>();
        for (int i = 0; i < records; i++) {
            OutputInfo item = new OutputInfo();
            item.setRow(rowPrefix + (i + 1));
            item.setFirst(Rand.letters(10));
            item.setLast(Rand.letters(20));
            item.setPhone(Rand.numbers(3) + "-" + Rand.numbers(3) + "-" + Rand.numbers(4));
            outputRecords.add(item);
            Utils.sleep(delay);
        }
    }

    private void writeToCSV() {
        CSVFormat format;
        boolean append;
        InterProcessBasicMutex mutex = new InterProcessBasicMutex();
        mutex.lock();
        try {
            String filename = FilenameUtils.concat(System.getProperty("user.home"), "test-csv-file.csv");
            File file = new File(filename);
            if (file.exists()) {
                format = CSVFormat.EXCEL.withSkipHeaderRecord();
                append = true;
            } else {
                format = CSVFormat.EXCEL.withHeader(HEADER_ROW);
                append = false;
            }

            CsvUtils.writeToCSV(filename, append, format, outputRecords);
        } finally {
            mutex.release();
        }
    }

    @Parameters({"row-prefix", "create-records", "delay"})
    @Test
    public void performCsvWriteTest(
            @Optional("") String rowPrefix,
            @Optional("5") String createRecords,
            @Optional("0") String delay
    ) {
        createTestData(rowPrefix, NumberUtils.toInt(createRecords), NumberUtils.toInt(delay));
        writeToCSV();
    }

}
