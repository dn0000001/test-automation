package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;

public class EnumPropertyConverter implements Converter {

    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        return Enum.valueOf(aClass, (String) obj);
    }

}
