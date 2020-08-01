package com.taf.automation.expressions;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Expression that matches list of zip codes against an address
 */
public class ZipCodeEqualsFromList implements Expression {
    private static final String STARTS_WITH = "ZIP*=";
    private List<String> zipCodes;

    @Override
    public boolean parse(String expression) {
        if (StringUtils.startsWith(expression, STARTS_WITH)) {
            // ex. "ZIP*=90210,90444" => states = {"90210", "90444"}
            String rawZipCodes = StringUtils.removeStart(expression, STARTS_WITH);
            zipCodes = Arrays.asList(Utils.splitData(rawZipCodes, ","));
            return true;
        }

        return false;
    }

    private boolean containsZipCode(String zipCode) {
        if (zipCodes == null) {
            return false;
        }

        for (String item : zipCodes) {
            if (StringUtils.equals(item, zipCode)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public <T> boolean eval(T value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return containsZipCode((String) value);
        }

        if (value instanceof USAddress) {
            USAddress address = (USAddress) value;
            return containsZipCode(address.getZipCode());
        }

        return false;
    }

    @Override
    public Expression deepCopy() {
        ZipCodeEqualsFromList copy = new ZipCodeEqualsFromList();
        copy.zipCodes = new ArrayList<>(zipCodes);
        return copy;
    }

}
