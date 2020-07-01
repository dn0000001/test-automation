package com.taf.automation.ui.support.providers;

import com.taf.automation.ui.support.csv.ColumnMapper;

public enum State implements ColumnMapper {
    // United States
    ALABAMA("AL"),
    ALASKA("AK"),
    ARIZONA("AZ"),
    ARKANSAS("AR"),
    CALIFORNIA("CA"),
    COLORADO("CO"),
    CONNECTICUT("CT"),
    DISTRICT_OF_COLUMBIA("DC"),
    DELAWARE("DE"),
    FLORIDA("FL"),
    GEORGIA("GA"),
    HAWAII("HI"),
    IDAHO("ID"),
    ILLINOIS("IL"),
    INDIANA("IN"),
    IOWA("IA"),
    KANSAS("KS"),
    KENTUCKY("KY"),
    LOUISIANA("LA"),
    MAINE("ME"),
    MARYLAND("MD"),
    MASSACHUSETTS("MA"),
    MICHIGAN("MI"),
    MINNESOTA("MN"),
    MISSISSIPPI("MS"),
    MISSOURI("MO"),
    MONTANA("MT"),
    NEBRASKA("NE"),
    NEVADA("NV"),
    NEW_HAMPSHIRE("NH"),
    NEW_JERSEY("NJ"),
    NEW_MEXICO("NM"),
    NEW_YORK("NY"),
    NORTH_CAROLINA("NC"),
    NORTH_DAKOTA("ND"),
    OHIO("OH"),
    OKLAHOMA("OK"),
    OREGON("OR"),
    PENNSYLVANIA("PA"),
    RHODE_ISLAND("RI"),
    SOUTH_CAROLINA("SC"),
    SOUTH_DAKOTA("SD"),
    TENNESSEE("TN"),
    TEXAS("TX"),
    UTAH("UT"),
    VERMONT("VT"),
    VIRGINIA("VA"),
    WASHINGTON("WA"),
    WEST_VIRGINIA("WV"),
    WISCONSIN("WI"),
    WYOMING("WY"),

    // Canadian Provinces & Territories
    ALBERTA("AB"),
    BRITISH_COLUMBIA("BC"),
    MANITOBA("MB"),
    NEW_BRUNSWICK("NB"),
    NEWFOUNDLAND_AND_LABRADOR("NL"),
    NOVA_SCOTIA("NS"),
    ONTARIO("ON"),
    PRINCE_EDWARD_ISLAND("PE"),
    QUEBEC("QC"),
    SASKATCHEWAN("SK"),
    NORTHWEST_TERRITORIES("NT"),
    NUNAVUT("NU"),
    YUKON("YT"),
    ;
    private final String label;

    State(String label) {
        this.label = label;
    }

    @Override
    public String getColumnName() {
        return label;
    }

    @Override
    public ColumnMapper[] getValues() {
        return values();
    }

    @Override
    public String toString() {
        return getColumnName();
    }

}
