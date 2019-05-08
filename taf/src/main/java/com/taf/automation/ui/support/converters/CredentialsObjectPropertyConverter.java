package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;

/**
 * Converter for a single Credentials object (not an array)
 */
public class CredentialsObjectPropertyConverter implements Converter {
    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        return new Credentials((String) obj);
    }

}
