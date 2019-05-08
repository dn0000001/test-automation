package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;

public class CredentialsPropertyConverter implements Converter {
    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        String[] creds = ((String) obj).split(",");
        Credentials[] credentials = new Credentials[creds.length];
        for (int i = 0; i < creds.length; i++) {
            credentials[i] = new Credentials(creds[i]);
        }

        return credentials;
    }

}
