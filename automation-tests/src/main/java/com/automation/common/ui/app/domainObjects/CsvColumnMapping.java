package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.csv.ColumnMapper;

public enum CsvColumnMapping implements ColumnMapper {
    RUN("RUN"),
    USER("USER"),
    PASS("PASS"),
    PLAYER("player"),
    TEAM("Team"),

    DESCRIPTION("Description"),
    USER_LOGINS_EMAIL("user-logins-email"),
    USER_LOGINS_PASSWORD("user-logins-password"),
    ADMIN_LOGINS_EMAIL("admin-logins-email"),
    ADMIN_LOGINS_PASSWORD("admin-logins-password");

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
