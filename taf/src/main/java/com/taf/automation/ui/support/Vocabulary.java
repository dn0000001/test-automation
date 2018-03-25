package com.taf.automation.ui.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to hold the vocabulary of translated words/phases.
 */
public class Vocabulary {
    private static final Logger LOG = LoggerFactory.getLogger(Vocabulary.class);
    private Map<String, Word> vocabulary;

    /**
     * Constructor
     */
    public Vocabulary() {
        vocabulary = new HashMap<>();
    }

    /**
     * Adds a new word to the vocabulary.
     *
     * @param word - Word to add to the vocabulary
     */
    public void addWord(Word word) {
        if (vocabulary.containsKey(word.getTranslation(Locale.ROOT))) {
            String error = "The word (" + word.getTranslation(Locale.ROOT) + ") already exists!";
            LOG.info(error);
            throw new RuntimeException(error);
        }

        vocabulary.put(word.getTranslation(Locale.ROOT), word);
    }

    /**
     * Gets the keyword (to work with)
     *
     * @param keyword - Get word associated to the keyword
     * @return Word that corresponds to the specified keyword
     */
    public Word getWord(String keyword) {
        if (keyword == null) {
            throw new RuntimeException("Search using a null string is not supported");
        }

        if (!vocabulary.containsKey(keyword)) {
            throw new RuntimeException("Specified keyword was not in vocabulary:  " + keyword);
        }

        return vocabulary.get(keyword);
    }

    /**
     * Get entrySet() for the vocabulary variable<BR>
     * <BR>
     * <B>Notes: </B><BR>
     * 1) Method used to be able to iterate over all translations<BR>
     * 2) Method should only be used for unit testing Translation classes<BR>
     *
     * @return Set&lt;Entry&lt;String, Word&gt;&gt;
     */
    public Set<Map.Entry<String, Word>> entrySet() {
        return vocabulary.entrySet();
    }

}
