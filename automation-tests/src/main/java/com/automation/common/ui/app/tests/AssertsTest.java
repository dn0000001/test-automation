package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.components.CardExpirationDateFields;
import com.automation.common.ui.app.components.TextBox;
import com.automation.common.ui.app.components.UnitTestComponent;
import com.automation.common.ui.app.components.UnitTestWebElement;
import com.automation.common.ui.app.pageObjects.FakeComponentsPage;
import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.util.AssertsUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.ExpectedConditionsUtil;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ui.auto.core.pagecomponent.PageComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;

/**
 * Unit testing AssertsUtil class
 */
@SuppressWarnings({"squid:S1192", "squid:S00112", "squid:S00100", "squid:S1481", "squid:S00117"})
public class AssertsTest extends TestNGBase {
    private static final boolean RUN = true;

    @SuppressWarnings("squid:S1068")
    private static class TestObj {
        private String fieldString1;
        private String fieldString2;
        private Integer fieldInteger1;
        private Integer fieldInteger2;
        private int fieldInt1;
        private int fieldInt2;
        private Boolean fieldBoolean1;
        private Boolean fieldBoolean2;
        private boolean fieldBool1;
        private boolean fieldBool2;

        public String getFieldString1() {
            return fieldString1;
        }

        public String getFieldString2() {
            return fieldString2;
        }

    }

    private List<TestObj> getActualForSubsetTests() {
        TestObj a1 = new TestObj();
        a1.fieldString1 = "z";
        a1.fieldString2 = "zzz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "a";
        a2.fieldString2 = "aaa";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj a3 = new TestObj();
        a3.fieldString1 = "b";
        a3.fieldString2 = "bbb";
        a3.fieldInteger1 = 9;
        a3.fieldInteger2 = 10;
        a3.fieldInt1 = 11;
        a3.fieldInt2 = 12;
        a3.fieldBoolean1 = true;
        a3.fieldBoolean2 = true;
        a3.fieldBool1 = false;
        a3.fieldBool2 = false;

        TestObj a4 = new TestObj();
        a4.fieldString1 = "y";
        a4.fieldString2 = "yyy";
        a4.fieldInteger1 = 13;
        a4.fieldInteger2 = 14;
        a4.fieldInt1 = 15;
        a4.fieldInt2 = 16;
        a4.fieldBoolean1 = false;
        a4.fieldBoolean2 = false;
        a4.fieldBool1 = true;
        a4.fieldBool2 = true;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);
        actual.add(a3);
        actual.add(a4);

        return actual;
    }

    private List<TestObj> getOnePassExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "b";
        e1.fieldString2 = "bbb";
        e1.fieldInteger1 = 9;
        e1.fieldInteger2 = 10;
        e1.fieldInt1 = 11;
        e1.fieldInt2 = 12;
        e1.fieldBoolean1 = true;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);

        return expected;
    }

    private List<TestObj> getOnePassExpectedSubset2() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "y";
        e1.fieldString2 = "yyy";
        e1.fieldInteger1 = 13;
        e1.fieldInteger2 = 14;
        e1.fieldInt1 = 15;
        e1.fieldInt2 = 16;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = false;
        e1.fieldBool1 = true;
        e1.fieldBool2 = true;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);

        return expected;
    }

    private List<TestObj> getTwoPassExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);

        return expected;
    }

    private List<TestObj> getThreePassExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "z";
        e3.fieldString2 = "zzz";
        e3.fieldInteger1 = 1;
        e3.fieldInteger2 = 2;
        e3.fieldInt1 = 3;
        e3.fieldInt2 = 4;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = false;
        e3.fieldBool1 = true;
        e3.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);

        return expected;
    }

    private List<TestObj> getOneFailExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "b";
        e1.fieldString2 = "ccc";
        e1.fieldInteger1 = 9;
        e1.fieldInteger2 = 10;
        e1.fieldInt1 = 11;
        e1.fieldInt2 = 12;
        e1.fieldBoolean1 = true;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);

        return expected;
    }

    private List<TestObj> getTwoFailExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "bb";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "zz";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);

        return expected;
    }

    private List<TestObj> getThreeFailExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "bb";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "zz";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "z";
        e3.fieldString2 = "aa";
        e3.fieldInteger1 = 1;
        e3.fieldInteger2 = 2;
        e3.fieldInt1 = 3;
        e3.fieldInt2 = 4;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = false;
        e3.fieldBool1 = true;
        e3.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);

        return expected;
    }

    private List<TestObj> getOneNoMatchExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "s";
        e1.fieldString2 = "bbb";
        e1.fieldInteger1 = 9;
        e1.fieldInteger2 = 10;
        e1.fieldInt1 = 11;
        e1.fieldInt2 = 12;
        e1.fieldBoolean1 = true;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);

        return expected;
    }

    private List<TestObj> getTwoNoMatchExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "s";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "p";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);

        return expected;
    }

    private List<TestObj> getThreeNoMatchExpectedSubset() {
        TestObj e1 = new TestObj();
        e1.fieldString1 = "s";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "p";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "t";
        e3.fieldString2 = "yyy";
        e3.fieldInteger1 = 13;
        e3.fieldInteger2 = 14;
        e3.fieldInt1 = 15;
        e3.fieldInt2 = 16;
        e3.fieldBoolean1 = false;
        e3.fieldBoolean2 = false;
        e3.fieldBool1 = true;
        e3.fieldBool2 = true;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);

        return expected;
    }

    @Features("AssertsUtil")
    @Stories("WebElement is displayed assert with null element")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithElementDoesNotExistTest() {
        try {
            WebElement actual = null;
            assertThat(actual, AssertsUtil.isElementDisplayed());
            throw new RuntimeException("Assertion did not fail:  assertIsDisplayedWithElementDoesNotExistTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (assertIsDisplayedWithElementDoesNotExistTest)", true);
        }
    }

    @Features("AssertsUtil")
    @Stories("WebElement is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithElementExistTest() {
        UnitTestWebElement element = new UnitTestWebElement().withDisplayed(true);
        assertThat(element, AssertsUtil.isElementDisplayed());
    }

    @Features("AssertsUtil")
    @Stories("By is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = RUN)
    public void assertIsDisplayedWithByExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        assertThat(By.id("headersearch"), AssertsUtil.isElementDisplayed(driver));
    }

    @Features("AssertsUtil")
    @Stories("WebElement is disabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisabledWithElementExistTest() {
        UnitTestWebElement element = new UnitTestWebElement().withEnabled(false);
        assertThat(element, AssertsUtil.isElementDisabled());
    }

    @Features("AssertsUtil")
    @Stories("WebElement is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsEnabledWithElementExistTest() {
        UnitTestWebElement element = new UnitTestWebElement().withEnabled(true);
        assertThat(element, AssertsUtil.isElementEnabled());
    }

    @Features("AssertsUtil")
    @Stories("By is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = RUN)
    public void assertIsEnabledWithByExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        assertThat(By.id("headersearch"), AssertsUtil.isElementEnabled(driver));
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsEnabledWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withEnabled(true);
        assertThat(component, AssertsUtil.isComponentEnabled());
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is disabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisabledWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withEnabled(false);
        assertThat(component, AssertsUtil.isComponentDisabled());
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withDisplayed(true);
        assertThat(component, AssertsUtil.isComponentDisplayed());
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is removed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsRemovedWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withDisplayed(false);
        assertThat(component, AssertsUtil.isComponentRemoved());
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is ready assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsReadyWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withEnabled(true).withDisplayed(true);
        assertThat(component, AssertsUtil.isComponentReady());
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is not ready assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsNotReadyWithComponentExistTest() {
        String reason = "Enabled (%s), Displayed (%s)";
        UnitTestComponent component = new UnitTestComponent().withEnabled(false).withDisplayed(false);
        assertThat(String.format(reason, false, false), component, not(AssertsUtil.isComponentReady()));

        component = new UnitTestComponent().withEnabled(true).withDisplayed(false);
        assertThat(String.format(reason, true, false), component, not(AssertsUtil.isComponentReady()));

        component = new UnitTestComponent().withEnabled(false).withDisplayed(true);
        assertThat(String.format(reason, false, true), component, not(AssertsUtil.isComponentReady()));
    }

    @Features("AssertsUtil")
    @Stories("Validate Component Cannot Be Set Assertion")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertComponentCannotBeSetTest() {
        String valueToUse = "Does not matter";
        getContext().getDriver().get("https://duckduckgo.com/");
        WebElement element = Utils.getWebDriverWait().until(ExpectedConditionsUtil.ready(By.name("q")));
        UnitTestComponent component = new UnitTestComponent(element)
                .withEnabled(true)
                .withDisplayed(true);
        Helper.log("UnitTestComponent (ready) - start:  " + new Date(), true);
        assertThat("UnitTestComponent (ready)", component, not(AssertsUtil.componentCannotBeSet(valueToUse)));
        Helper.log("UnitTestComponent (ready) - complete:  " + new Date(), true);

        // This will always take timeout as it constantly tries to set the value until timeout occurs
        component.withEnabled(false).withDisplayed(false);
        assertThat("UnitTestComponent (not ready)", component, AssertsUtil.componentCannotBeSet(valueToUse));
        Helper.log("UnitTestComponent (not ready) - complete:  " + new Date(), true);
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is not displayed assert using WebDriverWait")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = RUN)
    public void assertIsNotDisplayedUsingWebDriverWaitTest() {
        WebDriverWait wait = Utils.getNegativeWebDriverWait();
        FakeComponentsPage fakeComponentsPage = new FakeComponentsPage(getContext());
        assertThat(fakeComponentsPage.getTextBoxComponent(), not(AssertsUtil.isComponentDisplayed(wait)));
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is not ready assert using WebDriverWait")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = RUN)
    public void assertIsNotReadyUsingWebDriverWaitTest() {
        WebDriverWait wait = Utils.getNegativeWebDriverWait();
        FakeComponentsPage fakeComponentsPage = new FakeComponentsPage(getContext());
        assertThat(fakeComponentsPage.getTextBoxComponent(), not(AssertsUtil.isComponentReady(wait)));
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is not enabled assert using WebDriverWait")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = RUN)
    public void assertIsNotEnabledUsingWebDriverWaitTest() {
        WebDriverWait wait = Utils.getNegativeWebDriverWait();
        FakeComponentsPage fakeComponentsPage = new FakeComponentsPage(getContext());
        assertThat(fakeComponentsPage.getTextBoxComponent(), not(AssertsUtil.isComponentEnabled(wait)));
    }

    @Features("AssertsUtil")
    @Stories("Assert that all components are enabled")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertAllEnabledTest() {
        UnitTestComponent item1 = new UnitTestComponent().withText("t1").withValue("v1").withEnabled(true);
        UnitTestComponent item2 = new UnitTestComponent().withText("t2").withValue("v2").withEnabled(true);
        UnitTestComponent item3 = new UnitTestComponent().withText("t3").withValue("v3").withEnabled(true);
        List<PageComponent> components = new ArrayList<>();
        components.add(item1);
        components.add(item2);
        components.add(item3);
        assertThat(components, AssertsUtil.isComponent(true));

        // All Disabled
        item1.withText("t4").withValue("v4").withEnabled(false);
        item2.withText("t5").withValue("v5").withEnabled(false);
        item3.withText("t6").withValue("v6").withEnabled(false);
        assertThat(components, not(AssertsUtil.isComponent(true)));

        // One Disabled
        item1.withText("t7").withValue("v7").withEnabled(true);
        item2.withText("t8").withValue("v8").withEnabled(false);
        item3.withText("t9").withValue("v9").withEnabled(true);
        assertThat(components, not(AssertsUtil.isComponent(true)));

        // Multiple Disabled
        item1.withText("t10").withValue("v10").withEnabled(false);
        item2.withText("t11").withValue("v11").withEnabled(true);
        item3.withText("t12").withValue("v12").withEnabled(false);
        assertThat(components, not(AssertsUtil.isComponent(true)));
    }

    @Features("AssertsUtil")
    @Stories("Assert that all components are disabled")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertAllDisabledTest() {
        UnitTestComponent item1 = new UnitTestComponent().withText("t1").withValue("v1").withEnabled(false);
        UnitTestComponent item2 = new UnitTestComponent().withText("t2").withValue("v2").withEnabled(false);
        UnitTestComponent item3 = new UnitTestComponent().withText("t3").withValue("v3").withEnabled(false);
        List<PageComponent> components = new ArrayList<>();
        components.add(item1);
        components.add(item2);
        components.add(item3);
        assertThat(components, AssertsUtil.isComponent(false));

        // All Enabled
        item1.withText("t4").withValue("v4").withEnabled(true);
        item2.withText("t5").withValue("v5").withEnabled(true);
        item3.withText("t6").withValue("v6").withEnabled(true);
        assertThat(components, not(AssertsUtil.isComponent(false)));

        // One Enabled
        item1.withText("t7").withValue("v7").withEnabled(true);
        item2.withText("t8").withValue("v8").withEnabled(false);
        item3.withText("t9").withValue("v9").withEnabled(true);
        assertThat(components, not(AssertsUtil.isComponent(false)));

        // Multiple Enabled
        item1.withText("t10").withValue("v10").withEnabled(false);
        item2.withText("t11").withValue("v11").withEnabled(true);
        item3.withText("t12").withValue("v12").withEnabled(false);
        assertThat(components, not(AssertsUtil.isComponent(false)));
    }

    @Features("AssertsUtil")
    @Stories("Assert that all components are enabled taking into account the exclusions using get text")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertAllEnabledWithExclusionsUsingGetTextTest() {
        UnitTestComponent item1 = new UnitTestComponent().withText("t1").withValue("v1").withEnabled(true);
        UnitTestComponent item2 = new UnitTestComponent().withText("t2").withValue("v2").withEnabled(true);
        UnitTestComponent item3 = new UnitTestComponent().withText("t3").withValue("v3").withEnabled(true);
        List<PageComponent> components = new ArrayList<>();
        components.add(item1);
        components.add(item2);
        components.add(item3);

        List<String> exclude = new ArrayList<>();
        exclude.add("no match");
        assertThat("All enabled", components, AssertsUtil.isComponent(true, exclude));

        item1.withEnabled(false);
        item2.withEnabled(false);
        item3.withEnabled(false);
        assertThat("All Disabled with no matches in excluded", components, not(AssertsUtil.isComponent(true, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("t2");
        assertThat("All enabled with one in excluded", components, AssertsUtil.isComponent(true, exclude));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("t3");
        exclude.add("t1");
        assertThat("All enabled with multiple in excluded", components, AssertsUtil.isComponent(true, exclude));

        item1.withEnabled(true);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("");
        assertThat("One disabled with no match in excluded", components, not(AssertsUtil.isComponent(true, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("");
        exclude.add("nv100");
        assertThat("Multiple disabled with no match in excluded", components, not(AssertsUtil.isComponent(true, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("t1");
        exclude.add("nv100");
        assertThat("Multiple disabled with some matches in excluded", components, not(AssertsUtil.isComponent(true, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("t3");
        exclude.add("t1");
        assertThat("Multiple disabled with all matches in excluded", components, AssertsUtil.isComponent(true, exclude));
    }

    @Features("AssertsUtil")
    @Stories("Assert that all components are enabled taking into account the exclusions using get value")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertAllEnabledWithExclusionsUsingGetValueTest() {
        UnitTestComponent item1 = new UnitTestComponent().withText("t1").withValue("v1").withEnabled(true);
        UnitTestComponent item2 = new UnitTestComponent().withText("t2").withValue("v2").withEnabled(true);
        UnitTestComponent item3 = new UnitTestComponent().withText("t3").withValue("v3").withEnabled(true);
        List<PageComponent> components = new ArrayList<>();
        components.add(item1);
        components.add(item2);
        components.add(item3);

        List<String> exclude = new ArrayList<>();
        exclude.add("no match");
        assertThat("All enabled", components, AssertsUtil.isComponent(true, true, exclude));

        item1.withEnabled(false);
        item2.withEnabled(false);
        item3.withEnabled(false);
        assertThat("All Disabled with no matches in excluded", components, not(AssertsUtil.isComponent(true, true, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("v2");
        assertThat("All enabled with one in excluded", components, AssertsUtil.isComponent(true, true, exclude));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("v3");
        exclude.add("v1");
        assertThat("All enabled with multiple in excluded", components, AssertsUtil.isComponent(true, true, exclude));

        item1.withEnabled(true);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("");
        assertThat("One disabled with no match in excluded", components, not(AssertsUtil.isComponent(true, true, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("");
        exclude.add("nv100");
        assertThat("Multiple disabled with no match in excluded", components, not(AssertsUtil.isComponent(true, true, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("v1");
        exclude.add("nv100");
        assertThat("Multiple disabled with some matches in excluded", components, not(AssertsUtil.isComponent(true, true, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("v3");
        exclude.add("v1");
        assertThat("Multiple disabled with all matches in excluded", components, AssertsUtil.isComponent(true, true, exclude));
    }

    @Features("AssertsUtil")
    @Stories("Assert that all components are disabled taking into account the exclusions using get text")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertAllDisabledWithExclusionsUsingGetTextTest() {
        UnitTestComponent item1 = new UnitTestComponent().withText("t1").withValue("v1").withEnabled(false);
        UnitTestComponent item2 = new UnitTestComponent().withText("t2").withValue("v2").withEnabled(false);
        UnitTestComponent item3 = new UnitTestComponent().withText("t3").withValue("v3").withEnabled(false);
        List<PageComponent> components = new ArrayList<>();
        components.add(item1);
        components.add(item2);
        components.add(item3);

        List<String> exclude = new ArrayList<>();
        exclude.add("no match");
        assertThat("All disabled", components, AssertsUtil.isComponent(false, exclude));

        item1.withEnabled(true);
        item2.withEnabled(true);
        item3.withEnabled(true);
        assertThat("All Enabled with no matches in excluded", components, not(AssertsUtil.isComponent(false, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("t2");
        assertThat("All disabled with one in excluded", components, AssertsUtil.isComponent(false, exclude));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("t3");
        exclude.add("t1");
        assertThat("All disabled with multiple in excluded", components, AssertsUtil.isComponent(false, exclude));

        item1.withEnabled(false);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("");
        assertThat("One enabled with no match in excluded", components, not(AssertsUtil.isComponent(false, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("");
        exclude.add("nv100");
        assertThat("Multiple enabled with no match in excluded", components, not(AssertsUtil.isComponent(false, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("t1");
        exclude.add("nv100");
        assertThat("Multiple enabled with some matches in excluded", components, not(AssertsUtil.isComponent(false, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("t3");
        exclude.add("t1");
        assertThat("Multiple enabled with all matches in excluded", components, AssertsUtil.isComponent(false, exclude));
    }

    @Features("AssertsUtil")
    @Stories("Assert that all components are disabled taking into account the exclusions using get value")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertAllDisabledWithExclusionsUsingGetValueTest() {
        UnitTestComponent item1 = new UnitTestComponent().withText("t1").withValue("v1").withEnabled(false);
        UnitTestComponent item2 = new UnitTestComponent().withText("t2").withValue("v2").withEnabled(false);
        UnitTestComponent item3 = new UnitTestComponent().withText("t3").withValue("v3").withEnabled(false);
        List<PageComponent> components = new ArrayList<>();
        components.add(item1);
        components.add(item2);
        components.add(item3);

        List<String> exclude = new ArrayList<>();
        exclude.add("no match");
        assertThat("All disabled", components, AssertsUtil.isComponent(true, false, exclude));

        item1.withEnabled(true);
        item2.withEnabled(true);
        item3.withEnabled(true);
        assertThat("All Enabled with no matches in excluded", components, not(AssertsUtil.isComponent(true, false, exclude)));

        item1.withEnabled(false);
        item2.withEnabled(true);
        item3.withEnabled(false);
        exclude = new ArrayList<>();
        exclude.add("v2");
        assertThat("All disabled with one in excluded", components, AssertsUtil.isComponent(true, false, exclude));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("v3");
        exclude.add("v1");
        assertThat("All disabled with multiple in excluded", components, AssertsUtil.isComponent(true, false, exclude));

        item1.withEnabled(false);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("");
        assertThat("One enabled with no match in excluded", components, not(AssertsUtil.isComponent(true, false, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("");
        exclude.add("nv100");
        assertThat("Multiple enabled with no match in excluded", components, not(AssertsUtil.isComponent(true, false, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("v1");
        exclude.add("nv100");
        assertThat("Multiple enabled with some matches in excluded", components, not(AssertsUtil.isComponent(true, false, exclude)));

        item1.withEnabled(true);
        item2.withEnabled(false);
        item3.withEnabled(true);
        exclude = new ArrayList<>();
        exclude.add("v3");
        exclude.add("v1");
        assertThat("Multiple enabled with all matches in excluded", components, AssertsUtil.isComponent(true, false, exclude));
    }

    @Features("AssertsUtil")
    @Stories("BigDecimal Assertions")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertions() {
        assertThat("All Infinite", null, AssertsUtil.range(null, null));
        assertThat("Infinite Less than or equal to infinite", null, AssertsUtil.range(new BigDecimal("-1"), null));
        assertThat("Any number less than infinite", new BigDecimal("5"), AssertsUtil.range(new BigDecimal("0"), null));
        assertThat("Lower bound equal test", new BigDecimal("5"), AssertsUtil.range(new BigDecimal("5"), null));
        assertThat("Infinite Less than or equal to infinite #2", null, AssertsUtil.range(new BigDecimal("0"), null));
        assertThat("Lower bound equal test #2", new BigDecimal("20"), AssertsUtil.range(new BigDecimal("0"), null));
    }

    @Features("AssertsUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailures_InfiniteGreaterThanAnyNumber() {
        try {
            assertThat("InfiniteGreaterThanAnyNumber", null, AssertsUtil.range(null, new BigDecimal("90")));
            throw new RuntimeException("Assertion did not fail:  InfiniteGreaterThanAnyNumber");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteGreaterThanAnyNumber)", true);
        }
    }

    @Features("AssertsUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailures_AnyNumberNotInRangeOfInfinity() {
        try {
            assertThat("AnyNumberNotInRangeOfInfinity", new BigDecimal("0"), AssertsUtil.range(null, null));
            throw new RuntimeException("Assertion did not fail:  AnyNumberNotInRangeOfInfinity");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (AnyNumberNotInRangeOfInfinity)", true);
        }
    }

    @Features("AssertsUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailures_InfiniteNotInNumberRange() {
        try {
            assertThat("InfiniteNotInNumberRange", null, AssertsUtil.range(new BigDecimal("0"), new BigDecimal("10")));
            throw new RuntimeException("Assertion did not fail:  InfiniteNotInNumberRange");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteNotInNumberRange)", true);
        }
    }

    @Features("AssertsUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailures_InfiniteAsLowerBoundAndNumberUpperBound() {
        try {
            assertThat("InfiniteAsLowerBoundAndNumberUpperBound", new BigDecimal("4"), AssertsUtil.range(null, new BigDecimal("333")));
            throw new RuntimeException("Assertion did not fail:  InfiniteAsLowerBoundAndNumberUpperBound");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteAsLowerBoundAndNumberUpperBound)", true);
        }
    }

    @Features("AssertsUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailures_InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite() {
        try {
            assertThat("InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite", null, AssertsUtil.range(null, new BigDecimal("333")));
            throw new RuntimeException("Assertion did not fail:  InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite)", true);
        }
    }

    @Features("AssertAggregator")
    @Stories("All Success")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testAggregator_AllSuccess() {
        AssertAggregator aa = new AssertAggregator();
        aa.setConsole(true);
        assertThat("Aggregator Return Value #1", aa.assertThat("Some test #1", true));
        assertThat("Aggregator Return Value #2", aa.assertThat("Some test #2", 10, greaterThan(5)));
        assertThat("Aggregator Return Value #3", aa.assertThat(10, greaterThan(5)));
        Helper.assertThat(aa);
    }

    @Features("AssertAggregator")
    @Stories("All Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testAggregator_AllFailures() {
        AssertAggregator aa = new AssertAggregator();
        aa.setConsole(true);
        assertThat("Aggregator Return Value #1", !aa.assertThat("Some test #1", false));
        assertThat("Aggregator Return Value #2", !aa.assertThat("Some test #2", 5, greaterThan(10)));
        assertThat("Aggregator Return Value #3", !aa.assertThat(100, greaterThan(500)));
        int failures = 3;
        try {
            Helper.assertThat(aa);
            throw new RuntimeException("Assertion did not fail");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aa.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Some Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testAggregator_SomeFailures() {
        AssertAggregator aa = new AssertAggregator();
        aa.setConsole(true);
        aa.assertThat("Some test #1", true);
        aa.assertThat("Some test #2", false);
        aa.assertThat("Some test #3", 10, greaterThan(5));
        aa.assertThat("Some test #4", 5, greaterThan(10));
        aa.assertThat(20, greaterThan(15));
        aa.assertThat(100, greaterThan(500));
        int failures = 3;
        try {
            Helper.assertThat(aa);
            throw new RuntimeException("Assertion did not fail");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aa.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Aggregator With FailSafe")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testAggregatorWithFailSafe() {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.setConsole(true); // Use if information is truncated in Allure report

        try {
            MutableInt value = new MutableInt(0);
            int stop = 3;
            Failsafe.with(Utils.getPollingRetryPolicy()).run(() -> validationThatNeedsTime(aggregator, value, stop));
        } finally {
            Helper.assertThat(aggregator);
        }
    }

    /**
     * The validation method called by Failsafe until it is successful or retry policy stops with exception
     *
     * @param aggregator - Aggregator to store the last failure
     */
    private void validationThatNeedsTime(AssertAggregator aggregator, MutableInt currentValue, int waitForGreaterThanValue) {
        // Reset the count as we only want to store the last result
        aggregator.reset();

        //
        // Perform assertion(s) for the test
        //

        // Debugging information to output to the console just for testing purposes here
        Helper.log("Is " + currentValue + " greater than " + waitForGreaterThanValue, true);

        aggregator.assertThat("Assertion that takes time to be successful", currentValue.intValue(), greaterThan(waitForGreaterThanValue));
        currentValue.increment();

        if (aggregator.getFailureCount() > 0) {
            throw new RuntimeException("There were assertion failures.  See the console log.");
        }
    }

    @Features("AssertsUtil")
    @Stories("Range Primitive")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testRangePrimitive() {
        assertThat("Start", 0, AssertsUtil.range(0, 2));
        assertThat("Middle", 1, AssertsUtil.range(0, 2));
        assertThat("End", 2, AssertsUtil.range(0, 2));

        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.assertThat("Greater Than Range", 3, AssertsUtil.range(0, 2));
        failures++;
        assertThat("Greater Than Range did not fail", failures, equalTo(aggregator.getFailureCount()));

        aggregator.assertThat("Less Than Range", 0, AssertsUtil.range(1, 3));
        failures++;
        assertThat("Less Than Range did not fail", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertsUtil")
    @Stories("Range BigDecimal")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testRangeBigDecimal() {
        final BigDecimal TWO = Utils.parse("2", Locale.CANADA);
        final BigDecimal THREE = Utils.parse("3", Locale.CANADA);
        final BigDecimal FIVE = Utils.parse("5", Locale.CANADA);
        final BigDecimal TEN_THOUSAND = Utils.parse("10000", Locale.CANADA);
        final BigDecimal INFINITE = null;

        assertThat("Start", BigDecimal.ZERO, AssertsUtil.range(BigDecimal.ZERO, BigDecimal.TEN));
        assertThat("Middle", FIVE, AssertsUtil.range(BigDecimal.ZERO, BigDecimal.TEN));
        assertThat("End", BigDecimal.TEN, AssertsUtil.range(BigDecimal.ZERO, BigDecimal.TEN));
        assertThat("0 to Infinite", TEN_THOUSAND, AssertsUtil.range(BigDecimal.ZERO, INFINITE));
        assertThat("Infinite range", INFINITE, AssertsUtil.range(INFINITE, INFINITE));

        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();

        aggregator.assertThat("Greater Than Range", THREE, AssertsUtil.range(BigDecimal.ZERO, TWO));
        failures++;
        assertThat("Greater Than Range did not fail", failures, equalTo(aggregator.getFailureCount()));

        aggregator.assertThat("Less Than Range", BigDecimal.ZERO, AssertsUtil.range(BigDecimal.ONE, THREE));
        failures++;
        assertThat("Less Than Range did not fail", failures, equalTo(aggregator.getFailureCount()));

        aggregator.assertThat("Invalid Infinite range", TEN_THOUSAND, AssertsUtil.range(INFINITE, BigDecimal.ZERO));
        failures++;
        assertThat("Invalid Infinite range did not fail", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertsUtil")
    @Stories("Range Primitives")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyPrimitivesTest() {
        final boolean actualBool = true;
        final int actualInt = 10;
        final Boolean actualBoolean = true;
        final Integer actualInteger = 10;
        final String actualString = "abc";

        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        boolean expectedBool = true;
        Helper.assertThat(null, "boolean match", actualBool, expectedBool);
        Helper.assertThat(aggregator, "boolean match", actualBool, expectedBool);
        try {
            expectedBool = false;
            Helper.assertThat(aggregator, "boolean mismatch", actualBool, expectedBool);
            failures++;
            Helper.assertThat(null, "boolean mismatch", actualBool, expectedBool);
            throw new RuntimeException("Assertion did not fail:  boolean mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (boolean mismatch)", true);
        }

        int expectedInt = 10;
        Helper.assertThat(null, "int match", actualInt, expectedInt);
        Helper.assertThat(aggregator, "int match", actualInt, expectedInt);
        try {
            expectedInt = 20;
            Helper.assertThat(aggregator, "int mismatch", actualInt, expectedInt);
            failures++;
            Helper.assertThat(null, "int mismatch", actualInt, expectedInt);
            throw new RuntimeException("Assertion did not fail:  int mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (int mismatch)", true);
        }

        Boolean expectedBoolean = true;
        Helper.assertThat(null, "Boolean match", actualBoolean, expectedBoolean);
        Helper.assertThat(aggregator, "Boolean match", actualBoolean, expectedBoolean);

        expectedBoolean = null;
        Helper.assertThat(aggregator, "Boolean skip", actualBoolean, expectedBoolean);
        Helper.assertThat(null, "Boolean skip", actualBoolean, expectedBoolean);

        try {
            expectedBoolean = false;
            Helper.assertThat(aggregator, "Boolean mismatch", actualBoolean, expectedBoolean);
            failures++;
            Helper.assertThat(null, "Boolean mismatch", actualBoolean, expectedBoolean);
            throw new RuntimeException("Assertion did not fail:  Boolean mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Boolean mismatch)", true);
        }

        try {
            expectedBoolean = null;  // Re-using and passing as the actual parameter
            Helper.assertThat(aggregator, "Boolean mismatch actual null", expectedBoolean, actualBoolean);
            failures++;
            Helper.assertThat(null, "Boolean mismatch actual null", expectedBoolean, actualBoolean);
            throw new RuntimeException("Assertion did not fail:  Boolean mismatch actual null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Boolean mismatch actual null)", true);
        }

        Integer expectedInteger = 10;
        Helper.assertThat(null, "Integer match", actualInteger, expectedInteger);
        Helper.assertThat(aggregator, "Integer match", actualInteger, expectedInteger);

        expectedInteger = null;
        Helper.assertThat(aggregator, "Integer skip", actualInteger, expectedInteger);
        Helper.assertThat(null, "Integer skip", actualInteger, expectedInteger);
        try {
            expectedInteger = 20;
            Helper.assertThat(aggregator, "Integer mismatch", actualInteger, expectedInteger);
            failures++;
            Helper.assertThat(null, "Integer mismatch", actualInteger, expectedInteger);
            throw new RuntimeException("Assertion did not fail:  Integer mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Integer mismatch)", true);
        }

        try {
            expectedInteger = null;  // Re-using and passing as the actual parameter
            Helper.assertThat(aggregator, "Integer mismatch actual null", expectedInteger, actualInteger);
            failures++;
            Helper.assertThat(null, "Integer mismatch actual null", expectedInteger, actualInteger);
            throw new RuntimeException("Assertion did not fail:  Integer mismatch actual null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Integer mismatch actual null)", true);
        }

        String expectedString = "abc";
        Helper.assertThat(null, "String match", actualString, expectedString);
        Helper.assertThat(aggregator, "String match", actualString, expectedString);

        expectedString = null;
        Helper.assertThat(aggregator, "String skip", actualString, expectedString);
        Helper.assertThat(null, "String skip", actualString, expectedString);
        try {
            expectedString = "bcd";
            Helper.assertThat(aggregator, "String mismatch", actualString, expectedString);
            failures++;
            Helper.assertThat(null, "String mismatch", actualString, expectedString);
            throw new RuntimeException("Assertion did not fail:  String mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (String mismatch)", true);
        }

        try {
            expectedString = null;  // Re-using and passing as the actual parameter
            Helper.assertThat(aggregator, "String mismatch actual null", expectedString, actualString);
            failures++;
            Helper.assertThat(null, "String mismatch actual null", expectedString, actualString);
            throw new RuntimeException("Assertion did not fail:  String mismatch actual null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (String mismatch actual null)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Objects")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyObjectsTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj actual = new TestObj();
        actual.fieldString1 = "abc";
        actual.fieldString2 = "xyz";
        actual.fieldInteger1 = 1;
        actual.fieldInteger2 = 2;
        actual.fieldInt1 = 3;
        actual.fieldInt2 = 4;
        actual.fieldBoolean1 = true;
        actual.fieldBoolean2 = false;
        actual.fieldBool1 = true;
        actual.fieldBool2 = false;

        // All fields verified
        TestObj expected = new TestObj();
        expected.fieldString1 = "abc";
        expected.fieldString2 = "xyz";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = 2;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = true;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;

        Helper.assertThat(null, actual, expected);
        Helper.assertThat(aggregator, actual, expected);

        // Some fields skipped
        expected.fieldString1 = null;
        expected.fieldString2 = "xyz";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = null;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = null;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;

        Helper.assertThat(null, actual, expected);
        Helper.assertThat(aggregator, actual, expected);

        // All fields verified, 1 failure
        expected.fieldString1 = "abc";
        expected.fieldString2 = "def";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = 2;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = true;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;

        try {
            Helper.assertThat(aggregator, actual, expected);
            failures++;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  fieldString2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldString2)", true);
        }

        try {
            expected.fieldString2 = "xyz";
            expected.fieldInteger1 = 10;

            Helper.assertThat(aggregator, actual, expected);
            failures++;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  fieldInteger1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldInteger1)", true);
        }

        try {
            expected.fieldInteger1 = 1;
            expected.fieldInt2 = 40;

            Helper.assertThat(aggregator, actual, expected);
            failures++;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  fieldInt2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldInt2)", true);
        }

        try {
            expected.fieldInt2 = 4;
            expected.fieldBoolean1 = false;

            Helper.assertThat(aggregator, actual, expected);
            failures++;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  fieldBoolean1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldBoolean1)", true);
        }

        try {
            expected.fieldBoolean1 = true;
            expected.fieldBool2 = true;

            Helper.assertThat(aggregator, actual, expected);
            failures++;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  fieldBool2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldBool2)", true);
        }

        expected.fieldBool2 = false;
        Helper.assertThat(null, actual, expected);

        // All fields verified, multiple failures
        try {
            expected.fieldString2 = "def";
            expected.fieldInteger2 = 20;
            expected.fieldInt2 = 40;
            expected.fieldBoolean2 = true;
            expected.fieldBool1 = false;

            Helper.assertThat(aggregator, actual, expected);
            failures += 5;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  multiple failures");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (multiple failures)", true);
        }

        expected.fieldString2 = "xyz";
        expected.fieldInteger2 = 2;
        expected.fieldInt2 = 4;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        Helper.assertThat(null, actual, expected);

        // Some fields skipped, 1 failure
        try {
            expected.fieldString1 = null;
            expected.fieldString2 = "def";
            expected.fieldInteger1 = null;
            expected.fieldBoolean1 = null;

            Helper.assertThat(aggregator, actual, expected);
            failures++;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  single failure with skip");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (single failure with skip)", true);
        }

        expected.fieldString1 = "abc";
        expected.fieldString2 = "xyz";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = 2;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = true;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;
        Helper.assertThat(null, actual, expected);

        // Some fields skipped, multiple failures
        try {
            expected.fieldString1 = null;
            expected.fieldString2 = "def";
            expected.fieldInteger1 = null;
            expected.fieldInteger2 = 20;
            expected.fieldInt2 = 40;
            expected.fieldBoolean1 = null;
            expected.fieldBoolean2 = true;
            expected.fieldBool1 = false;

            Helper.assertThat(aggregator, actual, expected);
            failures += 5;
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail:  multiple failures with skip");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (multiple failures with skip)", true);
        }

        expected.fieldString1 = "abc";
        expected.fieldString2 = "xyz";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = 2;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = true;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;
        Helper.assertThat(null, actual, expected);

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Objects With Excluded Fields")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyObjectsWithExcludedFieldsTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj actual = new TestObj();
        actual.fieldString1 = "abc";
        actual.fieldString2 = "xyz";
        actual.fieldInteger1 = 1;
        actual.fieldInteger2 = 2;
        actual.fieldInt1 = 3;
        actual.fieldInt2 = 4;
        actual.fieldBoolean1 = true;
        actual.fieldBoolean2 = false;
        actual.fieldBool1 = true;
        actual.fieldBool2 = false;

        // All fields verified
        TestObj expected = new TestObj();
        expected.fieldString1 = "abc";
        expected.fieldString2 = "xyz";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = 2;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = true;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;

        List<String> excludeSingleField = new ArrayList<>();
        excludeSingleField.add("fieldString1");

        List<String> excludeFields = new ArrayList<>();
        excludeFields.add("fieldString2");
        excludeFields.add("fieldBoolean2");

        // Success when there would be a failure on a single field in the excluded list
        actual.fieldString1 = "excluded to prevent failure";
        Helper.assertThat(null, actual, expected, excludeSingleField);
        Helper.assertThat(aggregator, actual, expected, excludeSingleField);
        actual.fieldString1 = "abc";

        // Success when there would be a failure on a multiple fields in the excluded list
        actual.fieldString2 = "blah";
        actual.fieldBoolean2 = true;
        Helper.assertThat(null, actual, expected, excludeFields);
        Helper.assertThat(aggregator, actual, expected, excludeFields);
        actual.fieldString2 = "xyz";
        actual.fieldBoolean2 = false;

        // Failure when excluded fields has 1 item
        try {
            actual.fieldString1 = "does not cause failure";
            actual.fieldString2 = "causes failure";

            Helper.assertThat(aggregator, actual, expected, excludeSingleField);
            failures++;
            Helper.assertThat(null, actual, expected, excludeSingleField);
            throw new RuntimeException("Assertion did not fail:  failure when excluded fields has 1 item");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (failure when excluded fields has 1 item)", true);
        }

        // Failure when excluded fields has multiple items
        try {
            actual.fieldString1 = "something";
            actual.fieldInteger2 = 20;
            actual.fieldInt2 = 40;
            actual.fieldString2 = "blah";
            actual.fieldBoolean2 = true;

            Helper.assertThat(aggregator, actual, expected, excludeFields);
            failures += 3;
            Helper.assertThat(null, actual, expected, excludeFields);
            throw new RuntimeException("Assertion did not fail:  failure when excluded fields has multiple items");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (failure when excluded fields has multiple items)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Objects With Null")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyObjectsWithNullTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj actual = null;

        TestObj expected = new TestObj();
        expected.fieldString1 = null; // Skip this field
        expected.fieldString2 = "xyz";
        expected.fieldInteger1 = 1;
        expected.fieldInteger2 = 2;
        expected.fieldInt1 = 3;
        expected.fieldInt2 = 4;
        expected.fieldBoolean1 = true;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        expected.fieldBool2 = false;

        try {
            failures += 9; // 9 expected fields are tested
            Helper.assertThat(aggregator, actual, expected);
            Helper.assertThat(null, actual, expected);
            throw new RuntimeException("Assertion did not fail with actual object is null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected with actual object is null", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Objects With Empty List")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyEmptyListTest() {
        AssertAggregator aggregator = new AssertAggregator();

        List<TestObj> actual = new ArrayList<>();
        List<TestObj> expected = new ArrayList<>();

        Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("One List Pass")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyOneListPassTest() {
        TestObj a1 = new TestObj();
        a1.fieldString1 = "abc";
        a1.fieldString2 = "xyz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj e1 = new TestObj();
        e1.fieldString1 = "abc";
        e1.fieldString2 = "xyz";
        e1.fieldInteger1 = 1;
        e1.fieldInteger2 = 2;
        e1.fieldInt1 = 3;
        e1.fieldInt2 = 4;
        e1.fieldBoolean1 = true;
        e1.fieldBoolean2 = false;
        e1.fieldBool1 = true;
        e1.fieldBool2 = false;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("One List Fail")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyOneListFailTest() {
        TestObj a1 = new TestObj();
        a1.fieldString1 = "abc";
        a1.fieldString2 = "xyz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj e1 = new TestObj();
        e1.fieldString1 = "abc";
        e1.fieldString2 = "ddd";
        e1.fieldInteger1 = 1;
        e1.fieldInteger2 = 2;
        e1.fieldInt1 = 3;
        e1.fieldInt2 = 4;
        e1.fieldBoolean1 = true;
        e1.fieldBoolean2 = false;
        e1.fieldBool1 = true;
        e1.fieldBool2 = false;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);

        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            throw new RuntimeException("Assertion did not fail with actual object is null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected with actual object is null)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi List Pass")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyMultiListPassTest() {
        TestObj a1 = new TestObj();
        a1.fieldString1 = "abc";
        a1.fieldString2 = "xyz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "def";
        a2.fieldString2 = "ghi";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj e1 = new TestObj();
        e1.fieldString1 = "abc";
        e1.fieldString2 = "xyz";
        e1.fieldInteger1 = 1;
        e1.fieldInteger2 = 2;
        e1.fieldInt1 = 3;
        e1.fieldInt2 = 4;
        e1.fieldBoolean1 = true;
        e1.fieldBoolean2 = false;
        e1.fieldBool1 = true;
        e1.fieldBool2 = false;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "def";
        e2.fieldString2 = "ghi";
        e2.fieldInteger1 = 5;
        e2.fieldInteger2 = 6;
        e2.fieldInt1 = 7;
        e2.fieldInt2 = 8;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = true;
        e2.fieldBool1 = false;
        e2.fieldBool2 = true;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Multi List Pass With Sort")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyMultiListPassWithSortTest() {
        TestObj a1 = new TestObj();
        a1.fieldString1 = "z";
        a1.fieldString2 = "zzz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "a";
        a2.fieldString2 = "aaa";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj a3 = new TestObj();
        a3.fieldString1 = "b";
        a3.fieldString2 = "bbb";
        a3.fieldInteger1 = 9;
        a3.fieldInteger2 = 10;
        a3.fieldInt1 = 11;
        a3.fieldInt2 = 12;
        a3.fieldBoolean1 = true;
        a3.fieldBoolean2 = true;
        a3.fieldBool1 = false;
        a3.fieldBool2 = false;

        TestObj a4 = new TestObj();
        a4.fieldString1 = "y";
        a4.fieldString2 = "yyy";
        a4.fieldInteger1 = 13;
        a4.fieldInteger2 = 14;
        a4.fieldInt1 = 15;
        a4.fieldInt2 = 16;
        a4.fieldBoolean1 = false;
        a4.fieldBoolean2 = false;
        a4.fieldBool1 = true;
        a4.fieldBool2 = true;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);
        actual.add(a3);
        actual.add(a4);

        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "b";
        e3.fieldString2 = "bbb";
        e3.fieldInteger1 = 9;
        e3.fieldInteger2 = 10;
        e3.fieldInt1 = 11;
        e3.fieldInt2 = 12;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = true;
        e3.fieldBool1 = false;
        e3.fieldBool2 = false;

        TestObj e4 = new TestObj();
        e4.fieldString1 = "z";
        e4.fieldString2 = "zzz";
        e4.fieldInteger1 = 1;
        e4.fieldInteger2 = 2;
        e4.fieldInt1 = 3;
        e4.fieldInt2 = 4;
        e4.fieldBoolean1 = true;
        e4.fieldBoolean2 = false;
        e4.fieldBool1 = true;
        e4.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);
        expected.add(e4);

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Multi List Fail")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyMultiListFailTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj a1 = new TestObj();
        a1.fieldString1 = "z";
        a1.fieldString2 = "sss";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "a";
        a2.fieldString2 = "aaa";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj a3 = new TestObj();
        a3.fieldString1 = "b";
        a3.fieldString2 = "bbb";
        a3.fieldInteger1 = 9;
        a3.fieldInteger2 = 10;
        a3.fieldInt1 = 11;
        a3.fieldInt2 = 12;
        a3.fieldBoolean1 = true;
        a3.fieldBoolean2 = true;
        a3.fieldBool1 = false;
        a3.fieldBool2 = false;

        TestObj a4 = new TestObj();
        a4.fieldString1 = "y";
        a4.fieldString2 = "yyy";
        a4.fieldInteger1 = 13;
        a4.fieldInteger2 = 14;
        a4.fieldInt1 = 15;
        a4.fieldInt2 = 16;
        a4.fieldBoolean1 = false;
        a4.fieldBoolean2 = false;
        a4.fieldBool1 = true;
        a4.fieldBool2 = true;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);
        actual.add(a3);
        actual.add(a4);

        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "b";
        e3.fieldString2 = "bbb";
        e3.fieldInteger1 = 9;
        e3.fieldInteger2 = 10;
        e3.fieldInt1 = 11;
        e3.fieldInt2 = 12;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = true;
        e3.fieldBool1 = false;
        e3.fieldBool2 = false;

        TestObj e4 = new TestObj();
        e4.fieldString1 = "z";
        e4.fieldString2 = "zzz";
        e4.fieldInteger1 = 1;
        e4.fieldInteger2 = 2;
        e4.fieldInt1 = 3;
        e4.fieldInt2 = 4;
        e4.fieldBoolean1 = true;
        e4.fieldBoolean2 = false;
        e4.fieldBool1 = true;
        e4.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);
        expected.add(e4);

        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1));
            throw new RuntimeException("Assertion did not fail with multiple list items");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiListFailTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi List Fail With Sort")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyMultiListFailWithSortTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj a1 = new TestObj();
        a1.fieldString1 = "x";
        a1.fieldString2 = "yyy";
        a1.fieldInteger1 = 13;
        a1.fieldInteger2 = 14;
        a1.fieldInt1 = 15;
        a1.fieldInt2 = 16;
        a1.fieldBoolean1 = false;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = true;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "a";
        a2.fieldString2 = "aaa";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj a3 = new TestObj();
        a3.fieldString1 = "b";
        a3.fieldString2 = "bbb";
        a3.fieldInteger1 = 9;
        a3.fieldInteger2 = 10;
        a3.fieldInt1 = 11;
        a3.fieldInt2 = 12;
        a3.fieldBoolean1 = true;
        a3.fieldBoolean2 = true;
        a3.fieldBool1 = false;
        a3.fieldBool2 = false;

        TestObj a4 = new TestObj();
        a4.fieldString1 = "y";
        a4.fieldString2 = "zzz";
        a4.fieldInteger1 = 1;
        a4.fieldInteger2 = 2;
        a4.fieldInt1 = 3;
        a4.fieldInt2 = 4;
        a4.fieldBoolean1 = true;
        a4.fieldBoolean2 = false;
        a4.fieldBool1 = true;
        a4.fieldBool2 = false;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);
        actual.add(a3);
        actual.add(a4);

        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "b";
        e3.fieldString2 = "bbb";
        e3.fieldInteger1 = 9;
        e3.fieldInteger2 = 10;
        e3.fieldInt1 = 11;
        e3.fieldInt2 = 12;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = true;
        e3.fieldBool1 = false;
        e3.fieldBool2 = false;

        TestObj e4 = new TestObj();
        e4.fieldString1 = "z";
        e4.fieldString2 = "zzz";
        e4.fieldInteger1 = 1;
        e4.fieldInteger2 = 2;
        e4.fieldInt1 = 3;
        e4.fieldInt2 = 4;
        e4.fieldBoolean1 = true;
        e4.fieldBoolean2 = false;
        e4.fieldBool1 = true;
        e4.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);
        expected.add(e4);

        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            failures += 2;
            Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            throw new RuntimeException("Assertion did not fail with multiple list items when sorting");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiListFailWithSortTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Different List Fail More Actual")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyDifferentListFailMoreActualTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj a1 = new TestObj();
        a1.fieldString1 = "z";
        a1.fieldString2 = "zzz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "a";
        a2.fieldString2 = "aaa";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj a3 = new TestObj();
        a3.fieldString1 = "b";
        a3.fieldString2 = "bbb";
        a3.fieldInteger1 = 9;
        a3.fieldInteger2 = 10;
        a3.fieldInt1 = 11;
        a3.fieldInt2 = 12;
        a3.fieldBoolean1 = true;
        a3.fieldBoolean2 = true;
        a3.fieldBool1 = false;
        a3.fieldBool2 = false;

        TestObj a4 = new TestObj();
        a4.fieldString1 = "y";
        a4.fieldString2 = "yyy";
        a4.fieldInteger1 = 13;
        a4.fieldInteger2 = 14;
        a4.fieldInt1 = 15;
        a4.fieldInt2 = 16;
        a4.fieldBoolean1 = false;
        a4.fieldBoolean2 = false;
        a4.fieldBool1 = true;
        a4.fieldBool2 = true;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);
        actual.add(a3);
        actual.add(a4);

        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "b";
        e3.fieldString2 = "bbb";
        e3.fieldInteger1 = 9;
        e3.fieldInteger2 = 10;
        e3.fieldInt1 = 11;
        e3.fieldInt2 = 12;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = true;
        e3.fieldBool1 = false;
        e3.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);

        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            throw new RuntimeException("Assertion did not fail when there were more actual list items");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyDifferentListFailMoreActualTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Different List Fail More Expected")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyDifferentListFailMoreExpectedTest() {
        AssertAggregator aggregator = new AssertAggregator();
        int failures = 0;

        TestObj a1 = new TestObj();
        a1.fieldString1 = "z";
        a1.fieldString2 = "zzz";
        a1.fieldInteger1 = 1;
        a1.fieldInteger2 = 2;
        a1.fieldInt1 = 3;
        a1.fieldInt2 = 4;
        a1.fieldBoolean1 = true;
        a1.fieldBoolean2 = false;
        a1.fieldBool1 = true;
        a1.fieldBool2 = false;

        TestObj a2 = new TestObj();
        a2.fieldString1 = "a";
        a2.fieldString2 = "aaa";
        a2.fieldInteger1 = 5;
        a2.fieldInteger2 = 6;
        a2.fieldInt1 = 7;
        a2.fieldInt2 = 8;
        a2.fieldBoolean1 = false;
        a2.fieldBoolean2 = true;
        a2.fieldBool1 = false;
        a2.fieldBool2 = true;

        TestObj a3 = new TestObj();
        a3.fieldString1 = "b";
        a3.fieldString2 = "bbb";
        a3.fieldInteger1 = 9;
        a3.fieldInteger2 = 10;
        a3.fieldInt1 = 11;
        a3.fieldInt2 = 12;
        a3.fieldBoolean1 = true;
        a3.fieldBoolean2 = true;
        a3.fieldBool1 = false;
        a3.fieldBool2 = false;

        List<TestObj> actual = new ArrayList<>();
        actual.add(a1);
        actual.add(a2);
        actual.add(a3);

        TestObj e1 = new TestObj();
        e1.fieldString1 = "a";
        e1.fieldString2 = "aaa";
        e1.fieldInteger1 = 5;
        e1.fieldInteger2 = 6;
        e1.fieldInt1 = 7;
        e1.fieldInt2 = 8;
        e1.fieldBoolean1 = false;
        e1.fieldBoolean2 = true;
        e1.fieldBool1 = false;
        e1.fieldBool2 = true;

        TestObj e2 = new TestObj();
        e2.fieldString1 = "y";
        e2.fieldString2 = "yyy";
        e2.fieldInteger1 = 13;
        e2.fieldInteger2 = 14;
        e2.fieldInt1 = 15;
        e2.fieldInt2 = 16;
        e2.fieldBoolean1 = false;
        e2.fieldBoolean2 = false;
        e2.fieldBool1 = true;
        e2.fieldBool2 = true;

        TestObj e3 = new TestObj();
        e3.fieldString1 = "b";
        e3.fieldString2 = "bbb";
        e3.fieldInteger1 = 9;
        e3.fieldInteger2 = 10;
        e3.fieldInt1 = 11;
        e3.fieldInt2 = 12;
        e3.fieldBoolean1 = true;
        e3.fieldBoolean2 = true;
        e3.fieldBool1 = false;
        e3.fieldBool2 = false;

        TestObj e4 = new TestObj();
        e4.fieldString1 = "z";
        e4.fieldString2 = "zzz";
        e4.fieldInteger1 = 1;
        e4.fieldInteger2 = 2;
        e4.fieldInt1 = 3;
        e4.fieldInt2 = 4;
        e4.fieldBoolean1 = true;
        e4.fieldBoolean2 = false;
        e4.fieldBool1 = true;
        e4.fieldBool2 = false;

        List<TestObj> expected = new ArrayList<>();
        expected.add(e1);
        expected.add(e2);
        expected.add(e3);
        expected.add(e4);

        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, Comparator.comparing(TestObj::getFieldString1).thenComparing(TestObj::getFieldString2));
            throw new RuntimeException("Assertion did not fail when there were more expected list items");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyDifferentListFailMoreExpectedTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Null Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyNullExpectedSubsetTest() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = null;
        List<String> excludeFields = new ArrayList<>();

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Empty Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyEmptyExpectedSubsetTest() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        List<String> excludeFields = new ArrayList<>();

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Single Pass")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifySinglePassTest() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getOnePassExpectedSubset();
        List<String> excludeFields = new ArrayList<>();

        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Single Fail")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifySingleFailTest() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getOneFailExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifySingleFailTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifySingleFailTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Single No Match")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifySingleNoMatchTest() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getOneNoMatchExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifySingleNoMatchTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifySingleNoMatchTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P1F1N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOnePassExpectedSubset2());
        expected.addAll(getOneFailExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 1;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P1F1N0");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P1F1N0)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P2F0N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getTwoPassExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P1F0N1() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOnePassExpectedSubset());
        expected.addAll(getOneNoMatchExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 1;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P1F0N1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P1F0N1)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F0N2() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getTwoNoMatchExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 2;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F0N2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F0N2)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F1N1() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOneNoMatchExpectedSubset());
        expected.addAll(getOneFailExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 2;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F1N1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F1N1)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F2N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getTwoFailExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 2;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F2N0");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F2N0)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P1F1N1() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOnePassExpectedSubset2());
        expected.addAll(getOneNoMatchExpectedSubset());
        expected.addAll(getOneFailExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 2;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P1F1N1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P1F1N1)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P2F1N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getTwoPassExpectedSubset());
        expected.addAll(getOneFailExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 1;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P2F1N0");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P2F1N0)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P1F2N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOnePassExpectedSubset2());
        expected.addAll(getTwoFailExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 2;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P1F2N0");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P1F2N0)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P2F0N1() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getTwoPassExpectedSubset());
        expected.addAll(getOneNoMatchExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 1;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P2F0N1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P2F0N1)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P1F0N2() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOnePassExpectedSubset2());
        expected.addAll(getTwoNoMatchExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 2;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P1F0N2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P1F0N2)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F2N1() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOneNoMatchExpectedSubset());
        expected.addAll(getTwoFailExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 3;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F2N1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F2N1)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F1N2() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = new ArrayList<>();
        expected.addAll(getOneFailExpectedSubset());
        expected.addAll(getTwoNoMatchExpectedSubset());

        List<String> excludeFields = new ArrayList<>();
        int failures = 3;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F1N2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F1N2)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P3F0N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getThreePassExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
        Helper.assertThat(aggregator);
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F3N0() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getThreeFailExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 3;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F3N0");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F3N0)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Multi Expected Subset")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @SuppressWarnings("squid:S00100")
    public void verifyMultiExpectedSubsetTest_P0F0N3() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getThreeNoMatchExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 3;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyMultiExpectedSubsetTest_P0F0N3");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyMultiExpectedSubsetTest_P0F0N3)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Fail When Expected Subset Greater Than Actual Items")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyFailWhenExpectedSubsetGreaterThanActualItemsTest() {
        List<TestObj> actual = getActualForSubsetTests();
        List<TestObj> expected = getActualForSubsetTests();
        expected.addAll(getOneNoMatchExpectedSubset());
        List<String> excludeFields = new ArrayList<>();
        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyFailWhenEmptyActualItemsTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyFailWhenEmptyActualItemsTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Fail When Null Actual Items")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyFailWhenNullActualItemsTest() {
        List<TestObj> actual = null;
        List<TestObj> expected = getOnePassExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            failures += 2;
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyFailWhenNullActualItemsTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyFailWhenNullActualItemsTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertAggregator")
    @Stories("Fail When Empty Actual Items")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyFailWhenEmptyActualItemsTest() {
        List<TestObj> actual = new ArrayList<>();
        List<TestObj> expected = getOnePassExpectedSubset();
        List<String> excludeFields = new ArrayList<>();
        int failures = 0;
        AssertAggregator aggregator = new AssertAggregator();
        try {
            Helper.assertThat("TestObj", aggregator, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            failures++;
            Helper.assertThat("TestObj", null, actual, expected, excludeFields, (a, e) -> StringUtils.equals(a.fieldString1, e.fieldString1));
            throw new RuntimeException("Assertion did not fail for verifyFailWhenEmptyActualItemsTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (verifyFailWhenEmptyActualItemsTest)", true);
        }

        assertThat("Aggregator Failure Count", failures, equalTo(aggregator.getFailureCount()));
    }

    @Features("AssertsUtil")
    @Stories("Date Greater Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateGreaterThan() {
        String actual = "12/31/2018";
        String expected = "12/29/2018";
        String pattern = "MM/dd/yyyy";
        assertThat("testDateGreaterThan", actual, AssertsUtil.dateGreaterThan(expected, pattern));
    }

    @Features("Matchers")
    @Stories("BigDecimal Greater Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalGreaterThan() {
        BigDecimal actual = new BigDecimal("5.01");
        BigDecimal expected = new BigDecimal("5");
        assertThat("testBigDecimalGreaterThan", actual, greaterThan(expected));
    }

    @Features("AssertsUtil")
    @Stories("Date Greater Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateGreaterThanOrEqualTo() {
        String actual = "12/31/2018";
        String expected = "12/29/2018";
        String pattern = "MM/dd/yyyy";
        assertThat("testDateGreaterThanOrEqualTo", actual, AssertsUtil.dateGreaterThanOrEqualTo(expected, pattern));
    }

    @Features("Matchers")
    @Stories("BigDecimal Greater Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalGreaterThanOrEqualTo() {
        BigDecimal actual = new BigDecimal("5.01");
        BigDecimal expected = new BigDecimal("5");
        assertThat("testBigDecimalGreaterThanOrEqualTo", actual, greaterThanOrEqualTo(expected));
    }

    @Features("AssertsUtil")
    @Stories("Date Less Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateLessThan() {
        String actual = "12/30/2018";
        String expected = "12/31/2018";
        String pattern = "MM/dd/yyyy";
        assertThat("testDateLessThan", actual, AssertsUtil.dateLessThan(expected, pattern));
    }

    @Features("Matchers")
    @Stories("BigDecimal Less Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalLessThan() {
        BigDecimal actual = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5.01");
        assertThat("testBigDecimalLessThan", actual, lessThan(expected));
    }

    @Features("AssertsUtil")
    @Stories("Date Less Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateLessThanOrEqualTo() {
        String actual = "12/30/2018";
        String expected = "12/31/2018";
        String pattern = "MM/dd/yyyy";
        assertThat("testDateLessThanOrEqualTo", actual, AssertsUtil.dateLessThanOrEqualTo(expected, pattern));
    }

    @Features("Matchers")
    @Stories("BigDecimal Less Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalLessThanOrEqualTo() {
        BigDecimal actual = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5.01");
        assertThat("testBigDecimalLessThanOrEqualTo", actual, lessThanOrEqualTo(expected));
    }

    @Features("AssertsUtil")
    @Stories("Date Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateEqualTo() {
        String actual = "12/31/2018";
        String expected = "12/31/2018";
        String pattern = "MM/dd/yyyy";
        assertThat("testDateEqualTo", actual, AssertsUtil.dateEqualTo(expected, pattern));
    }

    @Features("Matchers")
    @Stories("BigDecimal Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalEqualTo() {
        BigDecimal actual = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5.00");
        assertThat("testBigDecimalEqualTo", actual, comparesEqualTo(expected));
    }

    @Features("AssertsUtil")
    @Stories("Null/Empty Expected Contains List")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testNullEmptyExpectedContainsList() {
        List<String> actual = Arrays.asList("100", "102", "102");
        List<String> expected = null;
        assertThat("null expected", actual, AssertsUtil.contains(expected));

        expected = new ArrayList<>();
        assertThat("empty expected", actual, AssertsUtil.contains(expected));
    }

    @Features("AssertsUtil")
    @Stories("Null Empty Actual Contains List")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testNullEmptyActualContainsList() {
        List<String> actual = null;
        List<String> expected = Arrays.asList("100", "102", "102");
        assertThat("null actual", actual, not(AssertsUtil.contains(expected)));

        actual = new ArrayList<>();
        assertThat("empty actual", actual, not(AssertsUtil.contains(expected)));
    }

    @Features("AssertsUtil")
    @Stories("Contains List")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testContainsList() {
        List<String> actual = Arrays.asList("100", "102", "102");
        List<String> expected = Arrays.asList("100");
        assertThat("single expected", actual, AssertsUtil.contains(expected));
        assertThat("actual unmodified", actual.size(), equalTo(3));
        assertThat("expected unmodified", expected.size(), equalTo(1));

        expected = Arrays.asList("100", "102");
        assertThat("multiple expected", actual, AssertsUtil.contains(expected));
        assertThat("actual unmodified", actual.size(), equalTo(3));
        assertThat("expected unmodified", expected.size(), equalTo(2));

        expected = Arrays.asList("100", "100");
        assertThat("duplicate expected", actual, AssertsUtil.contains(expected));
        assertThat("actual unmodified", actual.size(), equalTo(3));
        assertThat("expected unmodified", expected.size(), equalTo(2));

        expected = Arrays.asList("99", "100");
        assertThat("missing expected", actual, not(AssertsUtil.contains(expected)));
        assertThat("actual unmodified", actual.size(), equalTo(3));
        assertThat("expected unmodified", expected.size(), equalTo(2));

        expected = Arrays.asList("99", "10");
        assertThat("missing multiple expected", actual, not(AssertsUtil.contains(expected)));
        assertThat("actual unmodified", actual.size(), equalTo(3));
        assertThat("expected unmodified", expected.size(), equalTo(2));
    }

    @Features("AssertAggregator")
    @Stories("Validation Action Run Based On Field")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = RUN)
    public void testValidationActionRunBasedOnField() {
        String skipMessage = "Validation Action was run for:  ";
        String runMessage = "Validation Action was not run for:  ";
        String blankText = "   ";
        String runText = "run";

        //
        // ComponentPO object tests
        //

        CardExpirationDateFields componentPO = null;
        Helper.assertThat(componentPO, () -> assertThat(skipMessage + "ComponentPO (null)", false));

        componentPO = new CardExpirationDateFields(getContext());
        Helper.assertThat(componentPO, () -> assertThat(skipMessage + "ComponentPO (skip)", false));

        componentPO.updateMonthTestData(blankText);
        Helper.assertThat(componentPO, () -> assertThat(skipMessage + "ComponentPO (blank)", false));

        componentPO.updateMonthTestData(runText);
        try {
            Helper.assertThat(componentPO, () -> assertThat(runMessage + "ComponentPO (run)", false));
            throw new RuntimeException(runMessage + "ComponentPO(run)");
        } catch (AssertionError ae) {
            Helper.log(skipMessage + "ComponentPO (run)", true);
        }

        //
        // PageComponent object tests
        //

        TextBox pageComponent = null;
        Helper.assertThat(pageComponent, () -> assertThat(skipMessage + "Page Component (null)", false));

        pageComponent = new TextBox();
        Helper.assertThat(pageComponent, () -> assertThat(skipMessage + "Page Component (skip)", false));

        pageComponent.initializeData(blankText, null, null);
        Helper.assertThat(pageComponent, () -> assertThat(skipMessage + "Page Component (blank)", false));

        pageComponent.initializeData(runText, null, null);
        try {
            Helper.assertThat(pageComponent, () -> assertThat(runMessage + "Page Component (run)", false));
            throw new RuntimeException(runMessage + "Page Component (run)");
        } catch (AssertionError ae) {
            Helper.log(skipMessage + "Page Component (run)", true);
        }

        //
        // String tests
        //

        String strObj = null;
        Helper.assertThat(strObj, () -> assertThat(skipMessage + "String (null)", false));

        strObj = blankText;
        Helper.assertThat(strObj, () -> assertThat(skipMessage + "String (blank)", false));

        strObj = runText;
        try {
            Helper.assertThat(strObj, () -> assertThat(runMessage + "String (run)", false));
            throw new RuntimeException(runMessage + "String (run)");
        } catch (AssertionError ae) {
            Helper.log(skipMessage + "String (run)", true);
        }
    }

}
