package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.csv.ColumnMapper;

public enum HerokuappColumnMapping implements ColumnMapper {
    LAST_NAME("Last Name"),
    FIRST_NAME("First Name"),
    EMAIL("Email"),
    DUE("Due"),
    WEB_SITE("Web Site"),
    ACTION("Action");

    private String columnName;

    HerokuappColumnMapping(String columnName) {
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
