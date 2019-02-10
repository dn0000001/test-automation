package com.taf.automation.ui.support.csv;

/**
 * This class holds information to group rows in Excel
 */
public class GroupRow {
    private int summaryRow;
    private int fromRow;
    private int toRow;
    private boolean collapsed;

    public int getSummaryRow() {
        return summaryRow;
    }

    public int getFromRow() {
        return fromRow;
    }

    /**
     * Sets the Summary Row and the From Row<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>The Summary Row is used to calculate the From Row</LI>
     * <LI>From Row = Summary Row + 1</LI>
     * <LI>The From Row cannot be set alone.  This was done by design.</LI>
     * </OL>
     *
     * @param summaryRow - Summary Row
     * @return GroupRow
     */
    public GroupRow withSummaryRow(int summaryRow) {
        this.summaryRow = summaryRow;
        this.fromRow = summaryRow + 1;
        return this;
    }

    public int getToRow() {
        return toRow;
    }

    public GroupRow withToRow(int toRow) {
        this.toRow = toRow;
        return this;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public GroupRow withCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
        return this;
    }

}
