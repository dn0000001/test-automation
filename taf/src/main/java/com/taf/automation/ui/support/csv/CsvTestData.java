package com.taf.automation.ui.support.csv;

import org.apache.commons.csv.CSVRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is specific for data providers for GUI that use a CSV file to hold all the tests
 */
public class CsvTestData {
    private CSVRecord record;
    private Map<String, Integer> aliases;

    public CsvTestData() {
        //
    }

    public CsvTestData(CSVRecord record, Map<String, Integer> aliases) {
        setRecord(record);
        getAliases().putAll(aliases);
    }

    public CSVRecord getRecord() {
        return record;
    }

    public void setRecord(CSVRecord record) {
        this.record = record;
    }

    public Map<String, Integer> getAliases() {
        if (aliases == null) {
            aliases = new HashMap<>();
        }

        return aliases;
    }

    @Override
    public String toString() {
        String recordNumber = (getRecord() == null) ? "null" : String.valueOf(getRecord().getRecordNumber());
        return "Record Number=" + recordNumber;
    }

}
