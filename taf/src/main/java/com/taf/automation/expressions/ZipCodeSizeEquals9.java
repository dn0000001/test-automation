package com.taf.automation.expressions;

import org.apache.commons.lang3.StringUtils;

/**
 * Expression that matches a zip code size of 9 against an address
 */
public class ZipCodeSizeEquals9 implements Expression {
    private static final String MATCHES = "ZIP9";
    private static final int ZIP_CODE_SIZE = 9;

    @Override
    public boolean parse(String expression) {
        return StringUtils.equals(expression, MATCHES);
    }

    private boolean zipCodeCorrectSize(String zipCode) {
        if (zipCode == null) {
            return false;
        }

        String clean = zipCode.replaceAll("\\D", "");
        return clean.length() == ZIP_CODE_SIZE;
    }

    @Override
    public <T> boolean eval(T value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return zipCodeCorrectSize((String) value);
        }

        if (value instanceof USAddress) {
            USAddress address = (USAddress) value;
            return zipCodeCorrectSize(address.getZipCode());
        }

        return false;
    }

    @Override
    public Expression deepCopy() {
        return new ZipCodeSizeEquals9();
    }

}
