package com.automation.unit.tests;

import com.automation.common.ui.app.domainObjects.Keywords;
import com.automation.common.ui.app.domainObjects.Translations;
import com.taf.automation.ui.support.Word;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * This class is for unit testing the Translations class (to ensure no duplicate words)
 */
public class TranslationsTest {
    private static final Logger LOG = LoggerFactory.getLogger(TranslationsTest.class);

    @Test
    public void verifyVocabulary() {
        LOG.info("Entire Vocabulary:");
        LOG.info("");

        for (Map.Entry<String, Word> e : Translations.voc.entrySet()) {
            LOG.info("KEY:  " + e.getKey());
            LOG.info("EN:   " + Translations.voc.getWord(e.getKey()).getTranslation(Locale.CANADA));
            LOG.info("FR:   " + Translations.voc.getWord(e.getKey()).getTranslation(Locale.CANADA_FRENCH));
            LOG.info("");
        }
    }

    @Test(dependsOnMethods = "verifyVocabulary")
    public void verifyAllKeywordsInTranslations() throws Exception {
        LOG.info("Verifying All Keywords in Translations:");
        LOG.info("");
        Field[] keys = FieldUtils.getAllFields(Keywords.class);
        for (Field key : keys) {
            String keyword = (String) FieldUtils.readStaticField(key);
            LOG.info("KEY:  " + keyword);
            LOG.info("EN:   " + Translations.voc.getWord(keyword).getTranslation(Locale.CANADA));
            LOG.info("FR:   " + Translations.voc.getWord(keyword).getTranslation(Locale.CANADA_FRENCH));
            LOG.info("");
        }
    }

}
