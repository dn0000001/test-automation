package com.taf.automation.ui.support;

/**
 * Regular Expression Utilities
 */
public class RegExUtils {
    public static final String NOT_ALPHANUMERIC = "\\W";
    public static final String NOT_DIGITS = "\\D";

    private RegExUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Treat the value as literals<BR>
     * <B>Note: </B> This is useful if the value comes from a user
     *
     * @param value - Value to treat as literals
     * @return regular expression that treat the value as literals
     */
    public static String treatAsLiterals(String value) {
        return "\\Q" + value + "\\E";
    }

    /**
     * Return the regular expression that ignores case
     *
     * @param regex - regular expression to ignore case
     * @return regular expression that ignores case
     */
    public static String ignoreCase(String regex) {
        return "(?i)" + regex;
    }

}
