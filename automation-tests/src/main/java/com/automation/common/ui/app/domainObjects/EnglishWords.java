package com.automation.common.ui.app.domainObjects;

/**
 * Contains all words that translation was needed and can be added to the vocabulary during
 * instantiation &amp; use later as translations are needed. (This prevents issues if the words are changed.)
 */
public class EnglishWords {
    private EnglishWords() {
        // Prevent initialization of class as all public methods should be static
    }

    public static final String ERROR_CREDENTIALS_INVALID = "Email address and password combination is incorrect.";

}
