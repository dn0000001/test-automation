package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.LocatorUtils;
import com.taf.automation.ui.support.RegExUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.taf.automation.ui.support.AssertsUtil.matchesRegex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

    @Test
    public void performByChainedTest() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("xpath", "auto-11");
        substitutions.put("css", "auto-22");
        By bychained = new ByChained(By.cssSelector(".test [id='${css}']"), By.xpath("//*[@id='${xpath}']"));
        By test = LocatorUtils.processForSubstitutions(bychained, substitutions);
        assertThat("performByChainedTest - instanceof", test instanceof ByChained);
        assertThat("performByChainedTest - locator", test.toString(), equalTo("By.chained({By.cssSelector: .test [id='auto-22'],By.xpath: //*[@id='auto-11']})"));
    }

    @Test
    public void performByAllTest() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("name", "auto-33");
        substitutions.put("id", "auto-44");
        By byall = new ByAll(By.id("${id}"), By.name("${name}"));
        By test = LocatorUtils.processForSubstitutions(byall, substitutions);
        assertThat("performByAllTest - instanceof", test instanceof ByAll);
        assertThat("performByAllTest - locator", test.toString(), equalTo("By.all({By.id: auto-44,By.name: auto-33})"));
    }

    @SuppressWarnings("squid:S1192")
    @Test
    public void performByChainedRecursiveTest() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("xpath1", "auto-1");
        substitutions.put("css1", "auto-20");
        substitutions.put("xpath2", "auto-3");
        substitutions.put("css2", "auto-40");
        substitutions.put("xpath3", "auto-5");
        By bychained1 = new ByChained(By.cssSelector(".test [id='${css1}']"), By.xpath("//*[@id='${xpath1}']"));
        By bychained2 = new ByChained(By.cssSelector(".test [id='${css2}']"), By.xpath("//*[@id='${xpath2}']"));
        By bychained = new ByChained(bychained1, By.xpath("//*[@id='${xpath3}']"), bychained2);
        By test = LocatorUtils.processForSubstitutions(bychained, substitutions);
        String expected = "By.chained({"
                + "By.chained({"
                + "By.cssSelector: .test [id='auto-20']"
                + ","
                + "By.xpath: //*[@id='auto-1']"
                + "})"
                + ","
                + "By.xpath: //*[@id='auto-5']"
                + ","
                + "By.chained({"
                + "By.cssSelector: .test [id='auto-40']"
                + ","
                + "By.xpath: //*[@id='auto-3']"
                + "})"
                + "})";
        assertThat("performByChainedRecursiveTest - instanceof", test instanceof ByChained);
        assertThat("performByChainedRecursiveTest- locator", test.toString(), equalTo(expected));
    }

    @SuppressWarnings("squid:S1192")
    @Test
    public void performByAllRecursiveTest() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("xpath1", "auto-1");
        substitutions.put("css1", "auto-20");
        substitutions.put("xpath2", "auto-3");
        substitutions.put("css2", "auto-40");
        substitutions.put("xpath3", "auto-5");
        By byall1 = new ByAll(By.cssSelector(".test [id='${css1}']"), By.xpath("//*[@id='${xpath1}']"));
        By byall2 = new ByAll(By.cssSelector(".test [id='${css2}']"), By.xpath("//*[@id='${xpath2}']"));
        By byall = new ByAll(byall1, By.xpath("//*[@id='${xpath3}']"), byall2);
        By test = LocatorUtils.processForSubstitutions(byall, substitutions);
        String expected = "By.all({"
                + "By.all({"
                + "By.cssSelector: .test [id='auto-20']"
                + ","
                + "By.xpath: //*[@id='auto-1']"
                + "})"
                + ","
                + "By.xpath: //*[@id='auto-5']"
                + ","
                + "By.all({"
                + "By.cssSelector: .test [id='auto-40']"
                + ","
                + "By.xpath: //*[@id='auto-3']"
                + "})"
                + "})";
        assertThat("performByAllRecursiveTest - instanceof", test instanceof ByAll);
        assertThat("performByAllRecursiveTest- locator", test.toString(), equalTo(expected));
    }

    @Test
    public void performMixedRecursive1Test() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("xpath1", "auto-1");
        substitutions.put("css1", "auto-20");
        substitutions.put("xpath2", "auto-3");
        substitutions.put("css2", "auto-40");
        substitutions.put("xpath3", "auto-5");
        By bychained = new ByChained(By.cssSelector(".test [id='${css1}']"), By.xpath("//*[@id='${xpath1}']"));
        By byall = new ByAll(By.cssSelector(".test [id='${css2}']"), By.xpath("//*[@id='${xpath2}']"));
        By recursiveMixed = new ByChained(bychained, By.xpath("//*[@id='${xpath3}']"), byall);
        By test = LocatorUtils.processForSubstitutions(recursiveMixed, substitutions);
        String expected = "By.chained({"
                + "By.chained({"
                + "By.cssSelector: .test [id='auto-20']"
                + ","
                + "By.xpath: //*[@id='auto-1']"
                + "})"
                + ","
                + "By.xpath: //*[@id='auto-5']"
                + ","
                + "By.all({"
                + "By.cssSelector: .test [id='auto-40']"
                + ","
                + "By.xpath: //*[@id='auto-3']"
                + "})"
                + "})";
        assertThat("performMixedRecursive1Test - instanceof", test instanceof ByChained);
        assertThat("performMixedRecursive1Test- locator", test.toString(), equalTo(expected));
    }

    @Test
    public void performMixedRecursive2Test() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("xpath1", "auto-1");
        substitutions.put("css1", "auto-20");
        substitutions.put("xpath2", "auto-3");
        substitutions.put("css2", "auto-40");
        substitutions.put("xpath3", "auto-5");
        By bychained = new ByChained(By.cssSelector(".test [id='${css1}']"), By.xpath("//*[@id='${xpath1}']"));
        By byall = new ByAll(By.cssSelector(".test [id='${css2}']"), By.xpath("//*[@id='${xpath2}']"));
        By recursiveMixed = new ByAll(bychained, By.xpath("//*[@id='${xpath3}']"), byall);
        By test = LocatorUtils.processForSubstitutions(recursiveMixed, substitutions);
        String expected = "By.all({"
                + "By.chained({"
                + "By.cssSelector: .test [id='auto-20']"
                + ","
                + "By.xpath: //*[@id='auto-1']"
                + "})"
                + ","
                + "By.xpath: //*[@id='auto-5']"
                + ","
                + "By.all({"
                + "By.cssSelector: .test [id='auto-40']"
                + ","
                + "By.xpath: //*[@id='auto-3']"
                + "})"
                + "})";
        assertThat("performMixedRecursive2Test - instanceof", test instanceof ByAll);
        assertThat("performMixedRecursive2Test- locator", test.toString(), equalTo(expected));
    }

}
