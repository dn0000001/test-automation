package com.taf.automation.ui.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This class provides utility methods to work with locators
 */
public class LocatorUtils {
    private static final String ENDS_WITH = "substring(${text}, string-length(${text}) - string-length(${suffix}) + 1) = ${suffix}";

    private LocatorUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * The method extracts the locator string from the By variable. Then it looks for instances of ${key} and
     * if there is a key in the substitutions map that is equal to 'key', the substring ${key} is replaced by
     * the value mapped to 'key'.  Finally, the processed locator string is used to create the same By locator type.
     *
     * @param locator       - By locator
     * @param substitutions - Substitutions map
     * @return By
     */
    public static By processForSubstitutions(final By locator, final Map<String, String> substitutions) {
        if (substitutions == null || substitutions.isEmpty()) {
            return locator;
        }

        String extractedLocator = locator.toString().replaceFirst(RegExUtils.BY_PREFIX, "");
        String processedLocator = processForSubstitutions(extractedLocator, substitutions);
        if (locator instanceof By.ById) {
            return By.id(processedLocator);
        } else if (locator instanceof By.ByCssSelector) {
            return By.cssSelector(processedLocator);
        } else if (locator instanceof By.ByXPath) {
            return By.xpath(processedLocator);
        } else if (locator instanceof By.ByName) {
            return By.name(processedLocator);
        } else if (locator instanceof By.ByLinkText) {
            return By.linkText(processedLocator);
        } else if (locator instanceof By.ByPartialLinkText) {
            return By.partialLinkText(processedLocator);
        } else if (locator instanceof By.ByTagName) {
            return By.tagName(processedLocator);
        } else if (locator instanceof By.ByClassName) {
            return By.className(processedLocator);
        } else {
            assertThat("Unsupported By locator:  " + locator.toString(), false);
            return By.id(processedLocator);
        }
    }

    /**
     * The method looks for instances of ${key} and if there is a key in the substitutions map that is equal to 'key',
     * the substring ${key} is replaced by the value mapped to 'key'
     *
     * @param locator       - Locator
     * @param substitutions - Substitutions map
     * @return String
     */
    public static String processForSubstitutions(final String locator, final Map<String, String> substitutions) {
        String[] split = StringUtils.substringsBetween(locator, "${", "}");
        List<String> subs = new ArrayList<>();
        if (split != null) {
            subs = Arrays.asList(split);
        }

        String processed = locator;
        for (String sub : subs) {
            //If there is no matching key, the substring "${...}" is treated as a literal
            if (substitutions.get(sub) != null) {
                processed = StringUtils.replace(processed, "${" + sub + "}", substitutions.get(sub));
            }
        }

        return processed;
    }

    /**
     * Set Locator for the component
     *
     * @param component - Component to set locator
     * @param locator   - Locator to be set using reflection
     */
    public static void setLocator(PageComponent component, By locator) {
        try {
            FieldUtils.writeField(component, "selector", locator, true);
        } catch (Exception ex) {
            assertThat("Could not set 'selector' for component due to error:  " + ex.getMessage(), false);
        }
    }

    /**
     * Returns an xpath 1.0 compatible statement for ends with check<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>
     * In xpath 2.0, there exists a function to check this.  However, most browsers do not support this version yet.
     * </LI>
     * <LI>
     * The surround text/suffix should be a quote (or double quote) if no evaluation is required for the value.
     * In this case, the value is treated as a string.
     * </LI>
     * <LI>
     * The surround text/suffix should be empty (or null) if further evaluation is required for the value.
     * For example, if it is an attribute (@data-bind) which needs to be evaluated.
     * </LI>
     * </OL>
     * <B>Example:</B><BR>
     * You want to find all elements that have the attribute data-bind and ends with the string LastName.  The xpath 1.0
     * for this in the console would be the following line:
     * $x("//*[substring(@data-bind, string-length(@data-bind) - string-length('LastName') + 1) = 'LastName']");
     * <BR>
     * This method can be used to generate the xpath 1.0 line with the following code:
     * <BR>
     * String endsWith = "$x(\"//*[" + LocatorUtils.endsWith("@data-bind", "", "LastName", "'") + "]\");";
     * <BR>
     *
     * @param text           - Text string to check if it ends with the specified suffix
     * @param surroundText   - Surround the text with this value to escape the value
     * @param suffix         - The suffix that the string should end with
     * @param surroundSuffix - Surround the suffix with this value to escape the value
     * @return an xpath 1.0 compatible statement to be used
     */
    public static String endsWith(String text, String surroundText, String suffix, String surroundSuffix) {
        String escapedTextWithValue = StringUtils.defaultString(surroundText);
        String escapedText = escapedTextWithValue + text + escapedTextWithValue;

        String escapedSuffixWithValue = StringUtils.defaultString(surroundSuffix);
        String escapedSuffix = escapedSuffixWithValue + suffix + escapedSuffixWithValue;

        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("text", escapedText);
        substitutions.put("suffix", escapedSuffix);

        return processForSubstitutions(ENDS_WITH, substitutions);
    }

}
