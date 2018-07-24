package com.automation.common.ui.app.domainObjects;

/**
 * Contains all keywords that map to translations that were needed and can be added to the vocabulary during
 * instantiation &amp; use later as translations are needed. (This prevents issues if the words are changed.) This
 * also allows for duplicate words/phases.
 */
public class Keywords {
    private Keywords() {
        // Prevent initialization of class as all public methods should be static
    }

    public static final String ERROR_CREDENTIALS_INVALID = "KEY001";

}
