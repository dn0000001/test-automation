package com.taf.automation.ui.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

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

}
