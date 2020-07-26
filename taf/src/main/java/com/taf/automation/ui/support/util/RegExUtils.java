package com.taf.automation.ui.support.util;

/**
 * Regular Expression Utilities
 */
public class RegExUtils {
    public static final String NOT_ALPHANUMERIC = "\\W";
    public static final String NOT_DIGITS = "\\D";
    public static final String HTTP = "http.*";
    public static final String BY_PREFIX = "^By.*: ";

    /**
     * Matches anything including newline.  Use of this should be limited.<BR>
     * <B>Explanation:</B>
     * <OL>
     * <LI>\s matches any whitespace character (equal to [\r\n\t\f\v ])</LI>
     * <LI>\S matches any non-whitespace character (equal to [^\r\n\t\f\v ])</LI>
     * <LI>* matches zero and unlimited</LI>
     * </OL>
     */
    public static final String ANYTHING = "[\\s\\S]*";

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
