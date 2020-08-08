package com.taf.automation.ui.support.converters;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import ui.auto.core.support.EnvironmentsSetup;

public class EnvironmentPropertyConverter implements Converter {
    @SuppressWarnings("java:S112")
    @Override
    public Object convert(Class aClass, Object obj) {
        if (!(obj instanceof String)) {
            return null;
        }

        String from = (String) obj;
        if (from.trim().isEmpty()) return null;
        String[] envConfig = from.trim().split(":");
        if (envConfig.length < 2) {
            throw new RuntimeException("Please provide test.env property in the following format <config file path>:<environment name>");
        }

        String config = envConfig[0].trim();
        String env = StringUtils.lowerCase(envConfig[1].trim());
        EnvironmentsSetup envSetup = new EnvironmentsSetup().fromResource(config);
        return envSetup.getEnvironment(env);
    }

}
