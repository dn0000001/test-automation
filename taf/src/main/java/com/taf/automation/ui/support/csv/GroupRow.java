package com.taf.automation.ui.support.csv;

/**
 * This class holds information to group rows in Excel
 */
public class GroupRow {
    private int fromRow;
    private int toRow;

    public int getFromRow() {
        return fromRow;
    }

    public GroupRow withFromRow(int fromRow) {
        this.fromRow = fromRow;
        return this;
    }

    public int getToRow() {
        return toRow;
    }

    public GroupRow withToRow(int toRow) {
        this.toRow = toRow;
        return this;
    }

}
