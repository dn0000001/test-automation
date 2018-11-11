package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.csv.ColumnMapper;

public enum CsvColumnMapping implements ColumnMapper {
    USER("User"),
    PASS("Pass"),
    PLAYER("player"),
    TEAM("team");

    private String columnName;

    CsvColumnMapping(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String getColumnName() {
        return columnName.toUpperCase();
    }

    @Override
    public ColumnMapper[] getValues() {
        return values();
    }

}
