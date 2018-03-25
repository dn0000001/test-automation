package com.taf.automation.api.rest;

import org.apache.commons.lang3.StringUtils;

/**
 * Text Error
 */
public class TextError implements GenericBaseError {
    private String error;

    public TextError() {
        this("");
    }

    public TextError(String error) {
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
