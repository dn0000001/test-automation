package com.taf.automation.ui.support.util;

import com.taf.automation.ui.support.Lookup;
import com.taf.automation.ui.support.TestProperties;
import ui.auto.core.support.EnvironmentsSetup;

/**
 * Miscellaneous methods for use with JEXL
 */
public class JexlUtils {
    private JexlUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * @return get instance of this class for use with JEXL expressions
     */
    public static JexlUtils getInstance() {
        return new JexlUtils();
    }

    public static String getFirstExists(String... roles) {
        for (String role : roles) {
            if (getUserAndStore(role) != null) {
                return role;
            }
        }

        return null;
    }

    public static EnvironmentsSetup.User getUserAndStore(String role) {
        try {
            EnvironmentsSetup.User user = TestProperties.getInstance().getTestEnvironment().getUser(role);
            Lookup.getInstance().put(role, user);
            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCustom(String role, String property) {
        try {
            EnvironmentsSetup.User user = (EnvironmentsSetup.User) Lookup.getInstance().get(role);
            return user.getCustom(property);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getCustom(String key, String defaultValue, boolean decode) {
        return TestProperties.getInstance().getCustom(key, defaultValue, decode);
    }

}
