package com.taf.automation.api.rest;

import org.apache.commons.lang3.StringUtils;

/**
 * Json Error
 */
public class JsonError implements JsonBaseError {
    private String error;

    public JsonError() {
        this("");
    }

    public JsonError(String error) {
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
