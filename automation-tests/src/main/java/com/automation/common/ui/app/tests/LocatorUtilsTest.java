package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.LocatorUtils;
import com.taf.automation.ui.support.RegExUtils;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.taf.automation.ui.support.AssertsUtil.matchesRegex;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocatorUtilsTest {
    private static final String KEY = "row";
    private static final String ROW_SUBSTITUTION = "${" + KEY + "}";

    @Test
    public void performNullTest() {
        Map<String, String> substitutions = null;
        String value = "nullTest";
        By test = LocatorUtils.processForSubstitutions(By.id(value), substitutions);
        assertThat("performNullTest - instanceof", test instanceof By.ById);
        assertThat("performNullTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performEmptyTest() {
        Map<String, String> substitutions = new HashMap<>();
        String value = "EmptyTest";
        By test = LocatorUtils.processForSubstitutions(By.id(value), substitutions);
        assertThat("performEmptyTest - instanceof", test instanceof By.ById);
        assertThat("performEmptyTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performIdTest() {
        String value = "myID12345";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.id(ROW_SUBSTITUTION), substitutions);
        assertThat("performIdTest - instanceof", test instanceof By.ById);
        assertThat("performIdTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performCssTest() {
        String value = "999";
        String locator = "[id='" + ROW_SUBSTITUTION + "']";
        String expected = RegExUtils.BY_PREFIX + "\\[id='" + value + "'\\]";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.cssSelector(locator), substitutions);
        assertThat("performCssTest - instanceof", test instanceof By.ByCssSelector);
        assertThat("performCssTest - locator", test.toString(), matchesRegex(expected));
    }

    @Test
    public void performXpathTest() {
        String value = "333";
        String locator = "//*[@id='" + ROW_SUBSTITUTION + "']";
        String expected = RegExUtils.BY_PREFIX + "\\/\\/\\*\\[@id='" + value + "'\\]";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.xpath(locator), substitutions);
        assertThat("performXpathTest - instanceof", test instanceof By.ByXPath);
        assertThat("performXpathTest - locator", test.toString(), matchesRegex(expected));
    }

    @Test
    public void performNameTest() {
        String value = "abc";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.name(ROW_SUBSTITUTION), substitutions);
        assertThat("performNameTest - instanceof", test instanceof By.ByName);
        assertThat("performNameTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performLinkTextTest() {
        String value = "complete";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.linkText(ROW_SUBSTITUTION), substitutions);
        assertThat("performLinkTextTest - instanceof", test instanceof By.ByLinkText);
        assertThat("performLinkTextTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performPartialLinkTextTest() {
        String value = "partial";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.partialLinkText(ROW_SUBSTITUTION), substitutions);
        assertThat("performPartialLinkTextTest - instanceof", test instanceof By.ByPartialLinkText);
        assertThat("performPartialLinkTextTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performTagNameTest() {
        String value = "tag";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.tagName(ROW_SUBSTITUTION), substitutions);
        assertThat("performTagNameTest - instanceof", test instanceof By.ByTagName);
        assertThat("performTagNameTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Test
    public void performClassNameTest() {
        String value = "class";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.className(ROW_SUBSTITUTION), substitutions);
        assertThat("performClassNameTest - instanceof", test instanceof By.ByClassName);
        assertThat("performClassNameTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

}
