package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;

public class DynamicCredentialsPropertyConverter implements Converter {
    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        String[] creds = ((String) obj).split(",");
        DynamicCredentials[] credentials = new DynamicCredentials[creds.length];
        for (int i = 0; i < creds.length; i++) {
            credentials[i] = new DynamicCredentials(creds[i]);
        }

        return credentials;
    }

}
