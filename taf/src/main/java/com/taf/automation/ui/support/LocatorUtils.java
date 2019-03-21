package com.taf.automation.ui.support;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class provides utility methods to work with locators
 */
public class LocatorUtils {
    private LocatorUtils() {
        // Prevent initialization of class as all public methods should be static
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

}
