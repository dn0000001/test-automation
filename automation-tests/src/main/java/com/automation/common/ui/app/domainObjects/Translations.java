package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.Vocabulary;
import com.taf.automation.ui.support.Word;

import java.util.Locale;

/**
 * This class is responsible for initializing all the translations that can be used.
 */
public class Translations {
    /**
     * Stores the translations for all the languages/locales
     */
    public static Vocabulary voc = initialize();

    /**
     * Initializes translations<BR>
     * <B>Notes:</B><BR>
     * 1) This method will throw a runtime exception, if there are any duplicate words/phases (based on Keyword)<BR>
     *
     * @return Vocabulary
     */
    private static Vocabulary initialize() {
        voc = new Vocabulary();
        Word word;

        //
        // Add all words/phases that need translation here
        //

        word = new Word(Keywords.ERROR_CREDENTIALS_INVALID);
        word.addTranslation(Locale.CANADA, EnglishWords.ERROR_CREDENTIALS_INVALID);
        word.addTranslation(Locale.CANADA_FRENCH, FrenchWords.ERROR_CREDENTIALS_INVALID);
        voc.addWord(word);

        // *****

        return voc;
    }

    private Translations() {
        // Prevent initialization of class as all public methods should be static
    }

}
