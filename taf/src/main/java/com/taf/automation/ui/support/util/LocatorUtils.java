package com.taf.automation.ui.support.util;

import com.taf.automation.api.ApiUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import ui.auto.core.pagecomponent.PageComponent;

import java.lang.reflect.Field;
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
    private static final String DOUBLE_QUOTE = "\"";
    private static final String SINGLE_QUOTE = "'";
    private static final String SEPARATOR = ",";
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
        } else if (locator instanceof ByAll || locator instanceof ByChained) {
            Class<?> cls = (locator instanceof ByAll) ? ByAll.class : ByChained.class;
            Field field = FieldUtils.getDeclaredField(cls, "bys", true);
            By[] bys = (By[]) ApiUtils.readField(field, locator);
            return processForSubstitutions(locator, bys, substitutions);
        } else {
            assertThat("Unsupported By locator:  " + locator.toString(), false);
            return By.id(processedLocator);
        }
    }

    /**
     * The method processes the array of locators and uses the locator type parameter to create the desired locator
     *
     * @param locatorType   - Locator Type to be created
     * @param locators      - The locators from the locator type to be processed
     * @param substitutions - Substitutions map
     * @return By
     */
    private static By processForSubstitutions(By locatorType, By[] locators, Map<String, String> substitutions) {
        if (!(locatorType instanceof ByAll) && !(locatorType instanceof ByChained)) {
            assertThat("Unsupported locator type:  " + locatorType.toString(), false);
        }

        List<By> processed = new ArrayList<>();
        for (By locator : locators) {
            By processedItem = processForSubstitutions(locator, substitutions);
            processed.add(processedItem);
        }

        if (locatorType instanceof ByAll) {
            return new ByAll(processed.toArray(new By[0]));
        } else {
            return new ByChained(processed.toArray(new By[0]));
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

    /**
     * Construct an xpath safe value<BR>
     * <B>Note: </B> Insert this value without escaping into the xpath<BR>
     *
     * @param unsafeValue - Value to ensure is safe
     * @return the equivalent unsafe value constructed using the xpath function concat
     */
    @SuppressWarnings("squid:S1643")
    public static String constructXpathSafeValue(String unsafeValue) {
        if (StringUtils.defaultString(unsafeValue).equals("")) {
            return "concat('', '')";
        }

        // If no problem quotes, then it is simple to make it safe with concat
        if (!unsafeValue.contains(SINGLE_QUOTE) && !unsafeValue.contains(DOUBLE_QUOTE)) {
            return "concat('" + unsafeValue + "', '')";
        }

        // Store the safe string as it is constructed
        String safe = "concat(";

        String afterPart = unsafeValue;
        int splitPos = -1;
        do {
            int indexOfSingleQuote = StringUtils.indexOf(afterPart, SINGLE_QUOTE);
            int indexOfDoubleQuote = StringUtils.indexOf(afterPart, DOUBLE_QUOTE);

            final String outerQuoteToUse;
            final String problemQuote = findFirstProblemQuote(indexOfSingleQuote, indexOfDoubleQuote);
            if (StringUtils.defaultString(problemQuote).equals(SINGLE_QUOTE)) {
                // First found problem quote
                splitPos = indexOfSingleQuote;

                // The character to surround the problem quote with
                outerQuoteToUse = DOUBLE_QUOTE;
            } else if (StringUtils.defaultString(problemQuote).equals(DOUBLE_QUOTE)) {
                // First found problem quote
                splitPos = indexOfDoubleQuote;

                // The character to surround the problem quote with
                outerQuoteToUse = SINGLE_QUOTE;
            } else {
                // No problem quote found
                splitPos = -1;

                // Still need to add the part to the safe string
                outerQuoteToUse = SINGLE_QUOTE;
            }

            if (splitPos >= 0) {
                // The part which does not have a single quote or double quote
                String beforePart = StringUtils.substring(afterPart, 0, splitPos);

                // Update the safe string with the before part and the problem quote
                safe += outerQuoteToUse + beforePart + outerQuoteToUse + SEPARATOR
                        + outerQuoteToUse + problemQuote + outerQuoteToUse + SEPARATOR;

                // Update the after part which still needs to be processed
                afterPart = StringUtils.substring(afterPart, splitPos + 1);
            } else {
                // Add the last part to the safe string
                safe += outerQuoteToUse + afterPart + outerQuoteToUse + SEPARATOR;
            }
        } while (splitPos >= 0);

        safe = StringUtils.removeEnd(safe, ",") + ")";
        return safe;
    }

    /**
     * Find 1st problem quote (if any)
     *
     * @param indexOfSingleQuote - Index of (1st) Single Quote (negative indicates no single quote)
     * @param indexOfDoubleQuote - Index of (1st) Double Quote (negative indicates no double quote)
     * @return null if no problem quote else the 1st problem quote (SINGLE_QUOTE or DOUBLE_QUOTE)
     */
    private static String findFirstProblemQuote(int indexOfSingleQuote, int indexOfDoubleQuote) {
        if (indexOfSingleQuote >= 0 && indexOfDoubleQuote >= 0) {
            if (indexOfSingleQuote < indexOfDoubleQuote) {
                return SINGLE_QUOTE;
            } else {
                return DOUBLE_QUOTE;
            }
        }

        if (indexOfSingleQuote >= 0) {
            return SINGLE_QUOTE;
        }

        if (indexOfDoubleQuote >= 0) {
            return DOUBLE_QUOTE;
        }

        return null;
    }

}
