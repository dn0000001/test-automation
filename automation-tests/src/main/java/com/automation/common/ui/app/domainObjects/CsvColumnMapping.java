package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.csv.ColumnMapper;

public enum CsvColumnMapping implements ColumnMapper {
    USER("USER"),
    PASS("PASS"),
    PLAYER("player"),
    TEAM("Team");

    private String columnName;

    CsvColumnMapping(String columnName) {
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
