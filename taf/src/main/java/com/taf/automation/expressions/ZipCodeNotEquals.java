package com.taf.automation.expressions;

import org.apache.commons.lang3.StringUtils;

/**
 * Expression that does not match a zip code against an address
 */
public class ZipCodeNotEquals implements Expression {
    private static final String STARTS_WITH = "ZIP!=";
    private String zipCode;

    @Override
    public boolean parse(String expression) {
        if (StringUtils.startsWith(expression, STARTS_WITH)) {
            // ex. "ZIP!=90210" => zipCode = "90210"
            zipCode = StringUtils.removeStart(expression, STARTS_WITH);
            return true;
        }

        return false;
    }

    @Override
    public <T> boolean eval(T value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return !StringUtils.equals(zipCode, (String) value);
        }

        if (value instanceof USAddress) {
            USAddress address = (USAddress) value;
            return !StringUtils.equals(zipCode, address.getZipCode());
        }

        return false;
    }

    @Override
    public Expression deepCopy() {
        ZipCodeNotEquals copy = new ZipCodeNotEquals();
        copy.zipCode = zipCode;
        return copy;
    }

}
