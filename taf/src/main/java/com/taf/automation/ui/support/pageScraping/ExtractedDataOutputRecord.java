package com.taf.automation.ui.support.pageScraping;

import com.taf.automation.ui.support.csv.CsvOutputRecord;

import java.util.ArrayList;
import java.util.List;

public class ExtractedDataOutputRecord implements CsvOutputRecord {
    private String row;
    private String pageName;
    private String fieldType;
    private String fieldName;
    private String expectedValue;
    private String actualValue;
    private String testStatus;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    @Override
    public List<String> asList() {
        List<String> columns = new ArrayList<>();
        columns.add(getRow());
        columns.add(getPageName());
        columns.add(getFieldType());
        columns.add(getFieldName());
        columns.add(getExpectedValue());
        columns.add(getActualValue());
        columns.add(getTestStatus());
        return columns;
    }

}
