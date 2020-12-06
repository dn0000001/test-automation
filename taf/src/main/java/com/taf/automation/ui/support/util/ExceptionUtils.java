package com.taf.automation.ui.support.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.openqa.selenium.WebDriverException;

/**
 * This class holds methods to work with exceptions
 */
public class ExceptionUtils {
    private static final String BUILD_INFO = "Build info: version:" + RegExUtils.ANYTHING;
    private static final String UUID = "\\(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}\\)";

    private ExceptionUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Clean the exception message of the dynamic information which prevents allure from grouping properly
     *
     * @param message - message to clean
     * @return message minus the problematic dynamic information
     */
    public static String clean(String message) {
        return StringUtils.defaultString(message).replaceAll(BUILD_INFO, "").replaceAll(UUID, "");
    }

    /**
     * Modify any WebDriverException to remove the dynamic information which prevents allure from grouping properly
     *
     * @param exception - Exception to clean/modify
     * @return Throwable that is cleaned/modified if a type of WebDriverException
     * otherwise the passed throwable exception is returned without modification
     */
    public static Throwable clean(Throwable exception) {
        Class<? extends Throwable> clazz = exception.getClass();
        if (!WebDriverException.class.isAssignableFrom(clazz)) {
            return exception;
        }

        String cleanedMessage = clean(exception.getMessage());
        Throwable modifiedException;

        try {
            // Create the same exception such that it is clear in the report the cause of the failure
            modifiedException = ConstructorUtils.invokeConstructor(clazz, cleanedMessage, exception);
        } catch (Exception ex) {
            // As Fallback use the generic WebDriverException
            modifiedException = new WebDriverException(cleanedMessage, exception);
        }

        // We don't want the current stacktrace which is just wrapping as such set to the original stacktrace
        modifiedException.setStackTrace(exception.getStackTrace());

        return modifiedException;
    }

}
