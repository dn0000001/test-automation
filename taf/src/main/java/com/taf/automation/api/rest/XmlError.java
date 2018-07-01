package com.taf.automation.api.rest;

import org.apache.commons.lang3.StringUtils;

/**
 * XML Error
 */
public class XmlError implements XmlBaseError {
    private String error;

    public XmlError() {
        this("");
    }

    public XmlError(String error) {
        setError(error);
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public void setError(String error) {
        this.error = StringUtils.defaultString(error);
    }

}
