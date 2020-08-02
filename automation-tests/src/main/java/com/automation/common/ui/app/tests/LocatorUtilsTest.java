package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.LocatorUtils;
import com.taf.automation.ui.support.util.RegExUtils;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.HashMap;
import java.util.Map;

import static com.taf.automation.ui.support.util.AssertsUtil.matchesRegex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Listeners(AllureTestNGListener.class)
public class LocatorUtilsTest {
    private static final String DOUBLE_QUOTE = "\"";
    private static final String ESCAPE_DOUBLE_QUOTE = "\\" + DOUBLE_QUOTE;
    private static final String SINGLE_QUOTE = "'";
    private static final String SEPARATOR = ",";
    private static final String VALUE = "value";
    private static final String PART = "Part";
    private static final String ANOTHER = "another";
    private static final String SOMETHING = "Something";
    private static final String CAPS = "UPPER";
    private static final String SAFE_START = "concat(";
    private static final String SAFE_END = ")";
    private static final String KEY = "row";
    private static final String ROW_SUBSTITUTION = "${" + KEY + "}";

    @Features("LocatorUtils")
    @Stories("Null")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNullTest() {
        Map<String, String> substitutions = null;
        String value = "nullTest";
        By test = LocatorUtils.processForSubstitutions(By.id(value), substitutions);
        assertThat("performNullTest - instanceof", test instanceof By.ById);
        assertThat("performNullTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("Empty")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performEmptyTest() {
        Map<String, String> substitutions = new HashMap<>();
        String value = "EmptyTest";
        By test = LocatorUtils.processForSubstitutions(By.id(value), substitutions);
        assertThat("performEmptyTest - instanceof", test instanceof By.ById);
        assertThat("performEmptyTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("By.id")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performIdTest() {
        String value = "myID12345";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.id(ROW_SUBSTITUTION), substitutions);
        assertThat("performIdTest - instanceof", test instanceof By.ById);
        assertThat("performIdTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("By.cssSelector")
    @Severity(SeverityLevel.NORMAL)
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

    @Features("LocatorUtils")
    @Stories("By.xpath")
    @Severity(SeverityLevel.NORMAL)
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

    @Features("LocatorUtils")
    @Stories("By.name")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNameTest() {
        String value = "abc";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.name(ROW_SUBSTITUTION), substitutions);
        assertThat("performNameTest - instanceof", test instanceof By.ByName);
        assertThat("performNameTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("By.linkText")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performLinkTextTest() {
        String value = "complete";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.linkText(ROW_SUBSTITUTION), substitutions);
        assertThat("performLinkTextTest - instanceof", test instanceof By.ByLinkText);
        assertThat("performLinkTextTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("By.partialLinkText")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performPartialLinkTextTest() {
        String value = "partial";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.partialLinkText(ROW_SUBSTITUTION), substitutions);
        assertThat("performPartialLinkTextTest - instanceof", test instanceof By.ByPartialLinkText);
        assertThat("performPartialLinkTextTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("By.tagName")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performTagNameTest() {
        String value = "tag";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.tagName(ROW_SUBSTITUTION), substitutions);
        assertThat("performTagNameTest - instanceof", test instanceof By.ByTagName);
        assertThat("performTagNameTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("By.className")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performClassNameTest() {
        String value = "class";
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("row", value);
        By test = LocatorUtils.processForSubstitutions(By.className(ROW_SUBSTITUTION), substitutions);
        assertThat("performClassNameTest - instanceof", test instanceof By.ByClassName);
        assertThat("performClassNameTest - locator", test.toString(), matchesRegex(RegExUtils.BY_PREFIX + value));
    }

    @Features("LocatorUtils")
    @Stories("ByChained")
    @Severity(SeverityLevel.NORMAL)
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

    @Features("LocatorUtils")
    @Stories("ByAll")
    @Severity(SeverityLevel.NORMAL)
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

    @Features("LocatorUtils")
    @Stories("ByChained Recursive")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @SuppressWarnings("squid:S1192")
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

    @Features("LocatorUtils")
    @Stories("ByAll Recursive")
    @Severity(SeverityLevel.NORMAL)
    @Test
    @SuppressWarnings("squid:S1192")
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

    @Features("LocatorUtils")
    @Stories("Mixed Recursive")
    @Severity(SeverityLevel.NORMAL)
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

    @Features("LocatorUtils")
    @Stories("Mixed Recursive")
    @Severity(SeverityLevel.NORMAL)
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

    /**
     * Output a console command that can be run to verify the unsafeValue &amp; safeValue are the same
     *
     * @param unsafeValue - Unsafe Value
     * @param safeValue   - Safe Value
     */
    private void outputConsoleComand(String unsafeValue, String safeValue) {
        String console = "$x("
                + DOUBLE_QUOTE + safeValue.replace(DOUBLE_QUOTE, ESCAPE_DOUBLE_QUOTE) + DOUBLE_QUOTE
                + ") === "
                + DOUBLE_QUOTE + unsafeValue.replace(DOUBLE_QUOTE, ESCAPE_DOUBLE_QUOTE) + DOUBLE_QUOTE;
        Helper.log(console, true);
    }

    @Features("LocatorUtils")
    @Stories("No Quotes")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNoQuotesTest() {
        String unsafeValue = VALUE;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SEPARATOR
                + " " + SINGLE_QUOTE + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performNoQuotesTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Matching Single Quotes")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMatchingSingleQuotesTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE + PART + SINGLE_QUOTE + ANOTHER;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + PART + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + ANOTHER + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMatchingSingleQuotesTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Matching Double Quotes")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMatchingDoubleQuotesTest() {
        String unsafeValue = VALUE + DOUBLE_QUOTE + PART + DOUBLE_QUOTE + ANOTHER;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + PART + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + ANOTHER + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMatchingDoubleQuotesTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Mix Quotes Sequential")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMatchingMixQuotesSequentialTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE + PART + SINGLE_QUOTE + ANOTHER + DOUBLE_QUOTE + SOMETHING + DOUBLE_QUOTE + CAPS;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + PART + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + ANOTHER + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + SOMETHING + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + CAPS + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMatchingMixQuotesSequentialTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Matching Mix Quotes Alternating")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMatchingMixQuotesAlternatingTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE + PART + DOUBLE_QUOTE + ANOTHER + SINGLE_QUOTE + SOMETHING + DOUBLE_QUOTE + CAPS;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + PART + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + ANOTHER + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + SOMETHING + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + CAPS + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMatchingMixQuotesAlternatingTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Single Quote")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performSingleQuoteTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE + PART;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + PART + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performSingleQuoteTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Double Quote")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performDoubleQuoteTest() {
        String unsafeValue = VALUE + DOUBLE_QUOTE + PART;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + PART + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performDoubleQuoteTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Single Quote Start")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performSingleQuoteStartTest() {
        String unsafeValue = SINGLE_QUOTE + VALUE;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performSingleQuoteStartTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Double Quote Start")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performDoubleQuoteStartTest() {
        String unsafeValue = DOUBLE_QUOTE + VALUE;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + SINGLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performDoubleQuoteStartTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Single Quote End")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performSingleQuoteEndTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performSingleQuoteEndTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Double Quote End")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performDoubleQuoteEndTest() {
        String unsafeValue = VALUE + DOUBLE_QUOTE;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performDoubleQuoteEndTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Mix Quote")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMixQuoteTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE + PART + DOUBLE_QUOTE + ANOTHER;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + PART + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + ANOTHER + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMixQuoteTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Mix Quote Sequential")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMixQuoteSequentialTest() {
        String unsafeValue = VALUE + SINGLE_QUOTE + DOUBLE_QUOTE + PART;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + VALUE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + PART + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMixQuoteSequentialTest", actualSafeValue, equalTo(expectedSafeValue));
    }

    @Features("LocatorUtils")
    @Stories("Mix Quote End")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMixQuoteEndTest() {
        String unsafeValue = SINGLE_QUOTE + VALUE + DOUBLE_QUOTE;
        String actualSafeValue = LocatorUtils.constructXpathSafeValue(unsafeValue);
        String expectedSafeValue = SAFE_START
                + DOUBLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + DOUBLE_QUOTE + SINGLE_QUOTE + DOUBLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + VALUE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + DOUBLE_QUOTE + SINGLE_QUOTE
                + SEPARATOR
                + SINGLE_QUOTE + SINGLE_QUOTE
                + SAFE_END;

        Helper.log(unsafeValue, true);
        outputConsoleComand(unsafeValue, actualSafeValue);

        assertThat("performMixQuoteEndTest", actualSafeValue, equalTo(expectedSafeValue));
    }

}
