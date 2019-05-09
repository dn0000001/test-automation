package com.taf.automation.ui.support.converters;

public class CreditCard {
    private String number;
    private String month;
    private String year;
    private String code;
    private String cardHolder;

    public CreditCard(String creditInfo) {
        String[] cred = creditInfo.split(":");
        number = cred[0].trim();
        month = cred[1].trim();
        year = cred[2].trim();
        code = cred[3].trim();
        cardHolder = cred[4].trim();
    }

    public String getNumber() {
        return number;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getCode() {
        return code;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    @Override
    public String toString() {
        return number + ":" + month + ":" + year + ":" + code + ":" + cardHolder;
    }

}
