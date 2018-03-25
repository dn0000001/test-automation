package com.taf.automation.ui.support;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class is used to hold all words/phases.
 */
public class Word {
    private Map<Locale, String> word;

    /**
     * Constructor
     *
     * @param keyword - key to be added
     */
    public Word(String keyword) {
        word = new HashMap<>();
        word.put(Locale.ROOT, keyword);
    }

    /**
     * Adds a translation of the word in a specific language/locale
     *
     * @param lang - Language/Locale of translation
     * @param word - Word in that language/locale
     */
    public void addTranslation(Locale lang, String word) {
        this.word.put(lang, word);
    }

    /**
     * Gets a translation of the word in a specific language/locale
     *
     * @param lang - Language/Locale for translation
     * @return word in specific language/locale
     */
    public String getTranslation(Locale lang) {
        return word.get(lang);
    }

}
