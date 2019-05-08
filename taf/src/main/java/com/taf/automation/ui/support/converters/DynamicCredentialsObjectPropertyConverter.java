package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;

/**
 * Converter for a single Dynamic Credentials object (not an array)
 */
public class DynamicCredentialsObjectPropertyConverter implements Converter {
    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        return new DynamicCredentials((String) obj);
    }

}
