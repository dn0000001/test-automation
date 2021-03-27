package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.components.CreditCardFields;
import com.automation.common.ui.app.components.UnitTestComponent;
import com.automation.common.ui.app.components.UnitTestWebElement;
import com.automation.common.ui.app.pageObjects.FakeComponentsPage;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.automation.common.ui.app.pageObjects.RoboFormLoginPage;
import com.taf.automation.asserts.AssertJCondition;
import com.taf.automation.asserts.CustomSoftAssertions;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.ExpectedConditionsUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ui.auto.core.components.WebComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssertJTest extends TestNGBase {
    private static final String DEC_12_31_2018 = "12/31/2018";
    private static final String DATE_PATTERN = "MM/dd/yyyy";
    private static final String FAILURE_COUNT = "Failure Count";

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

    @SuppressWarnings("squid:S1068")
    private static class ObjA {
        private String field1A;
        private Boolean field2A;
        private Integer field3A;
        private ObjB nestedBinA;
        private ObjC nestedCinA;
        private ObjD nestedDinA;
    }

    @SuppressWarnings("squid:S1068")
    private static class ObjB {
        private String field1B;
        private Boolean field2B;
        private Integer field3B;
        private ObjC nestedCinB;
        private ObjD nestedDinB;
    }

    @SuppressWarnings("squid:S1068")
    private static class ObjC {
        private String field1C;
        private Boolean field2C;
        private Integer field3C;
        private ObjD nestedDinC;
    }

    @SuppressWarnings("squid:S1068")
    private static class ObjD {
        private String field1D;
        private Boolean field2D;
        private Integer field3D;
    }

    @SuppressWarnings("java:S112")
    @Features("AssertJUtil")
    @Stories("WebElement is displayed assert with null element")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithElementDoesNotExistTest() {
        try {
            WebElement actual = null;
            AssertJUtil.assertThat(actual).isDisplayed();
            throw new RuntimeException("Assertion did not fail:  assertIsDisplayedWithElementDoesNotExistTest");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (assertIsDisplayedWithElementDoesNotExistTest)", true);
        }
    }

    @Features("AssertJUtil")
    @Stories("WebElement is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithElementExistTest() {
        UnitTestWebElement element = new UnitTestWebElement().withDisplayed(true);
        AssertJUtil.assertThat(element).isDisplayed();
    }

    @Features("AssertJUtil")
    @Stories("By is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithByExistTest() {
        new Navigation(getContext()).toTrueNorthHockey(Utils.isCleanCookiesSupported());
        AssertJUtil.assertThat(getContext().getDriver()).isDisplayed(By.id("headersearch"));
    }

    @Features("AssertJUtil")
    @Stories("WebElement is disabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisabledWithElementExistTest() {
        UnitTestWebElement element = new UnitTestWebElement().withEnabled(false);
        AssertJUtil.assertThat(element).isDisabled();
    }

    @Features("AssertJUtil")
    @Stories("WebElement is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsEnabledWithElementExistTest() {
        UnitTestWebElement element = new UnitTestWebElement().withEnabled(true);
        AssertJUtil.assertThat(element).isEnabled();
    }

    @Features("AssertJUtil")
    @Stories("By is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsEnabledWithByExistTest() {
        new Navigation(getContext()).toTrueNorthHockey(Utils.isCleanCookiesSupported());
        AssertJUtil.assertThat(getContext().getDriver()).isEnabled(By.id("headersearch"));
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsEnabledWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withEnabled(true);
        AssertJUtil.assertThat(component).isEnabled();
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is disabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisabledWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withEnabled(false);
        AssertJUtil.assertThat(component).isDisabled();
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsDisplayedWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withDisplayed(true);
        AssertJUtil.assertThat(component).isDisplayed();
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is removed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsRemovedWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withDisplayed(false);
        AssertJUtil.assertThat(component).isInvisible();
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is ready assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsReadyWithComponentExistTest() {
        UnitTestComponent component = new UnitTestComponent().withEnabled(true).withDisplayed(true);
        AssertJUtil.assertThat(component).isClickable();
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is not ready assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsNotReadyWithComponentExistTest() {
        String reason = "Enabled (%s), Displayed (%s)";
        UnitTestComponent component = new UnitTestComponent().withEnabled(false).withDisplayed(false);
        AssertJUtil.assertThat(component).as(reason, false, false).isNotClickable();

        component = new UnitTestComponent().withEnabled(true).withDisplayed(false);
        AssertJUtil.assertThat(component).as(reason, true, false).isNotClickable();

        component = new UnitTestComponent().withEnabled(false).withDisplayed(true);
        AssertJUtil.assertThat(component).as(reason, false, true).isNotClickable();
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Validate Component Cannot Be Set Assertion")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertComponentCannotBeSetTest() {
        new Navigation(getContext()).toDuckDuckGo(Utils.isCleanCookiesSupported());
        String valueToUse = "Does not matter";
        WebElement element = Utils.until(ExpectedConditionsUtil.ready(By.name("q")));
        WebComponent realComponent = new WebComponent(element);
        Helper.log("UnitTestComponent (ready) - start:  " + new Date(), true);
        CustomSoftAssertions softly = new CustomSoftAssertions();
        softly.assertThat(realComponent).as("UnitTestComponent (ready)").cannotBeSet(valueToUse);
        AssertJUtil.assertThat(softly.allSuccessful())
                .as("Expected to be able to set the UnitTestComponent when ready")
                .isFalse();
        Helper.log("UnitTestComponent (ready) - complete:  " + new Date(), true);

        // This will always take timeout as it constantly tries to set the value until timeout occurs
        UnitTestComponent component = new UnitTestComponent(element).withEnabled(false).withDisplayed(false);
        AssertJUtil.assertThat(component).as("UnitTestComponent (not ready)").cannotBeSet(valueToUse);
        Helper.log("UnitTestComponent (not ready) - complete:  " + new Date(), true);
    }

    @Features("AssertJUtil")
    @Stories("Validate Component Cannot Be Set Assertion")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertComponentCannotBeSetFastTest() {
        new Navigation(getContext()).toRoboFormFill(Utils.isCleanCookiesSupported());
        CreditCardFields creditCardFields = new CreditCardFields(getContext());
        creditCardFields.disableFieldsAndValidateCannotSetAssertJ();

        new Navigation(getContext()).toRoboFormLogin(Utils.isCleanCookiesSupported());
        RoboFormLoginPage roboFormLoginPage = new RoboFormLoginPage(getContext());
        roboFormLoginPage.disableFieldsAndValidateCannotSetAssertJ();
    }

    @Features("AssertJUtil")
    @Stories("WebComponent is not displayed assert using WebDriverWait")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void assertIsNotDisplayedUsingWebDriverWaitTest() {
        FakeComponentsPage fakeComponentsPage = new FakeComponentsPage(getContext());
        AssertJUtil.assertThat(fakeComponentsPage.getTextBoxComponent()).isInvisibleFast(getContext().getDriver());
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("BigDecimal Assertions")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertions() {
        BigDecimal infinite = null;
        AssertJUtil.assertThat(infinite)
                .as("All Infinite")
                .is(AssertJCondition.range(null, null));
        AssertJUtil.assertThat(infinite)
                .as("Infinite Less than or equal to infinite")
                .is(AssertJCondition.range(new BigDecimal("-1"), null));
        AssertJUtil.assertThat(new BigDecimal("5"))
                .as("Any number less than infinite")
                .is(AssertJCondition.range(BigDecimal.ZERO, null));
        AssertJUtil.assertThat(new BigDecimal("5"))
                .as("Lower bound equal test")
                .is(AssertJCondition.range(new BigDecimal("5"), null));
        AssertJUtil.assertThat(infinite)
                .as("Infinite Less than or equal to infinite #2")
                .is(AssertJCondition.range(BigDecimal.ZERO, null));
        AssertJUtil.assertThat(new BigDecimal("20"))
                .as("Lower bound equal test #2")
                .is(AssertJCondition.range(BigDecimal.ZERO, null));
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailuresInfiniteGreaterThanAnyNumber() {
        try {
            BigDecimal infinite = null;
            AssertJUtil.assertThat(infinite)
                    .as("InfiniteGreaterThanAnyNumber")
                    .is(AssertJCondition.range(null, new BigDecimal("90")));
            throw new RuntimeException("Assertion did not fail:  InfiniteGreaterThanAnyNumber");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteGreaterThanAnyNumber)", true);
        }
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailuresAnyNumberNotInRangeOfInfinity() {
        try {
            AssertJUtil.assertThat(BigDecimal.ZERO)
                    .as("AnyNumberNotInRangeOfInfinity")
                    .is(AssertJCondition.range(null, null));
            throw new RuntimeException("Assertion did not fail:  AnyNumberNotInRangeOfInfinity");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (AnyNumberNotInRangeOfInfinity)", true);
        }
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailuresInfiniteNotInNumberRange() {
        try {
            BigDecimal infinite = null;
            AssertJUtil.assertThat(infinite)
                    .as("InfiniteNotInNumberRange")
                    .is(AssertJCondition.range(new BigDecimal("0"), new BigDecimal("10")));
            throw new RuntimeException("Assertion did not fail:  InfiniteNotInNumberRange");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteNotInNumberRange)", true);
        }
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailuresInfiniteAsLowerBoundAndNumberUpperBound() {
        try {
            AssertJUtil.assertThat(new BigDecimal("4"))
                    .as("InfiniteAsLowerBoundAndNumberUpperBound")
                    .is(AssertJCondition.range(null, new BigDecimal("333")));
            throw new RuntimeException("Assertion did not fail:  InfiniteAsLowerBoundAndNumberUpperBound");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteAsLowerBoundAndNumberUpperBound)", true);
        }
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("BigDecimal Assertion Failures")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalAssertionFailuresInfiniteAsLowerBoundAndNumberUpperBoundWithInfinite() {
        try {
            BigDecimal infinite = null;
            AssertJUtil.assertThat(infinite).is(AssertJCondition.range(null, new BigDecimal("333")));
            throw new RuntimeException("Assertion did not fail:  InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite)", true);
        }
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Range Primitive")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testRangePrimitive() {
        AssertJUtil.assertThat(0).as("Start").isBetween(0, 2);
        AssertJUtil.assertThat(1).as("Middle").isBetween(0, 2);
        AssertJUtil.assertThat(2).as("End").isBetween(0, 2);

        int failures = 0;
        CustomSoftAssertions softly = new CustomSoftAssertions();
        softly.assertThat(3).as("Greater Than Range").isBetween(0, 2);
        failures++;
        AssertJUtil.assertThat(softly.getFailureCount()).as("Greater Than Range did not fail").isEqualTo(failures);

        softly.assertThat(0).as("Less Than Range").isBetween(1, 3);
        failures++;
        AssertJUtil.assertThat(softly.getFailureCount()).as("Less Than Range did not fail").isEqualTo(failures);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Range BigDecimal")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testRangeBigDecimal() {
        final BigDecimal TWO = Utils.parse("2", Locale.CANADA);
        final BigDecimal THREE = Utils.parse("3", Locale.CANADA);
        final BigDecimal FIVE = Utils.parse("5", Locale.CANADA);
        final BigDecimal TEN_THOUSAND = Utils.parse("10000", Locale.CANADA);
        final BigDecimal INFINITE = null;

        AssertJUtil.assertThat(BigDecimal.ZERO).as("Start").is(AssertJCondition.range(BigDecimal.ZERO, BigDecimal.TEN));
        AssertJUtil.assertThat(FIVE).as("Middle").is(AssertJCondition.range(BigDecimal.ZERO, BigDecimal.TEN));
        AssertJUtil.assertThat(BigDecimal.TEN).as("End").is(AssertJCondition.range(BigDecimal.ZERO, BigDecimal.TEN));
        AssertJUtil.assertThat(TEN_THOUSAND).as("0 to Infinite").is(AssertJCondition.range(BigDecimal.ZERO, INFINITE));
        AssertJUtil.assertThat(INFINITE).as("Infinite range").is(AssertJCondition.range(INFINITE, INFINITE));

        int failures = 0;
        CustomSoftAssertions softly = new CustomSoftAssertions();

        softly.assertThat(THREE).as("Greater Than Range").is(AssertJCondition.range(BigDecimal.ZERO, TWO));
        failures++;
        AssertJUtil.assertThat(softly.getFailureCount()).as("Greater Than Range did not fail").isEqualTo(failures);

        softly.assertThat(BigDecimal.ZERO).as("Less Than Range").is(AssertJCondition.range(BigDecimal.ONE, THREE));
        failures++;
        AssertJUtil.assertThat(softly.getFailureCount()).as("Less Than Range did not fail").isEqualTo(failures);

        softly.assertThat(TEN_THOUSAND).as("Invalid Infinite range").is(AssertJCondition.range(INFINITE, BigDecimal.ZERO));
        failures++;
        AssertJUtil.assertThat(softly.getFailureCount()).as("Invalid Infinite range did not fail").isEqualTo(failures);
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("Range Primitives")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyPrimitivesTest() {
        final boolean actualBool = true;
        final int actualInt = 10;
        final Boolean actualBoolean = true;
        final Integer actualInteger = 10;
        final String actualString = "abc";

        int failures = 0;
        CustomSoftAssertions softly = new CustomSoftAssertions();

        boolean expectedBool = true;
        AssertJUtil.assertThat(actualBool).as("boolean match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBool));
        softly.assertThat(actualBool).as("boolean match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBool));
        try {
            expectedBool = false;
            softly.assertThat(actualBool).as("boolean mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBool));
            failures++;
            AssertJUtil.assertThat(actualBool).as("boolean mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBool));
            throw new RuntimeException("Assertion did not fail:  boolean mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (boolean mismatch)", true);
        }

        int expectedInt = 10;
        AssertJUtil.assertThat(actualInt).as("int match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInt));
        softly.assertThat(actualInt).as("int match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInt));
        try {
            expectedInt = 20;
            softly.assertThat(actualInt).as("int mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInt));
            failures++;
            AssertJUtil.assertThat(actualInt).as("int mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInt));
            throw new RuntimeException("Assertion did not fail:  int mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (int mismatch)", true);
        }

        Boolean expectedBoolean = true;
        AssertJUtil.assertThat(actualBoolean).as("Boolean match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBoolean));
        softly.assertThat(actualBoolean).as("Boolean match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBoolean));

        expectedBoolean = null;
        AssertJUtil.assertThat(actualBoolean).as("Boolean skip").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBoolean));
        softly.assertThat(actualBoolean).as("Boolean skip").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBoolean));
        try {
            expectedBoolean = false;
            softly.assertThat(actualBoolean).as("Boolean mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBoolean));
            failures++;
            AssertJUtil.assertThat(actualBoolean).as("Boolean mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedBoolean));
            throw new RuntimeException("Assertion did not fail:  Boolean mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Boolean mismatch)", true);
        }

        try {
            expectedBoolean = null;  // Re-using and passing as the actual parameter
            softly.assertThat(expectedBoolean).as("Boolean mismatch actual null").is(AssertJCondition.isEqualToIgnoringNullFields(actualBoolean));
            failures++;
            AssertJUtil.assertThat(expectedBoolean).as("Boolean mismatch actual null").is(AssertJCondition.isEqualToIgnoringNullFields(actualBoolean));
            throw new RuntimeException("Assertion did not fail:  Boolean mismatch actual null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Boolean mismatch actual null)", true);
        }

        Integer expectedInteger = 10;
        AssertJUtil.assertThat(actualInteger).as("Integer match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInteger));
        softly.assertThat(actualInteger).as("Integer match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInteger));

        expectedInteger = null;
        AssertJUtil.assertThat(actualInteger).as("Integer skip").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInteger));
        softly.assertThat(actualInteger).as("Integer skip").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInteger));
        try {
            expectedInteger = 20;
            softly.assertThat(actualInteger).as("Integer mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInteger));
            failures++;
            AssertJUtil.assertThat(actualInteger).as("Integer mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedInteger));
            throw new RuntimeException("Assertion did not fail:  Integer mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Integer mismatch)", true);
        }

        try {
            expectedInteger = null;  // Re-using and passing as the actual parameter
            softly.assertThat(expectedInteger).as("Integer mismatch actual null").is(AssertJCondition.isEqualToIgnoringNullFields(actualInteger));
            failures++;
            AssertJUtil.assertThat(expectedInteger).as("Integer mismatch actual null").is(AssertJCondition.isEqualToIgnoringNullFields(actualInteger));
            throw new RuntimeException("Assertion did not fail:  Integer mismatch actual null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (Integer mismatch actual null)", true);
        }

        String expectedString = "abc";
        AssertJUtil.assertThat(actualString).as("String match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedString));
        softly.assertThat(actualString).as("String match").is(AssertJCondition.isEqualToIgnoringNullFields(expectedString));

        expectedString = null;
        AssertJUtil.assertThat(actualString).as("String skip").is(AssertJCondition.isEqualToIgnoringNullFields(expectedString));
        softly.assertThat(actualString).as("String skip").is(AssertJCondition.isEqualToIgnoringNullFields(expectedString));
        try {
            expectedString = "bcd";
            softly.assertThat(actualString).as("String mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedString));
            failures++;
            AssertJUtil.assertThat(actualString).as("String mismatch").is(AssertJCondition.isEqualToIgnoringNullFields(expectedString));
            throw new RuntimeException("Assertion did not fail:  String mismatch");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (String mismatch)", true);
        }

        try {
            expectedString = null;  // Re-using and passing as the actual parameter
            softly.assertThat(expectedString).as("String mismatch actual null").is(AssertJCondition.isEqualToIgnoringNullFields(actualString));
            failures++;
            AssertJUtil.assertThat(expectedString).as("String mismatch actual null").is(AssertJCondition.isEqualToIgnoringNullFields(actualString));
            throw new RuntimeException("Assertion did not fail:  String mismatch actual null");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (String mismatch actual null)", true);
        }

        AssertJUtil.assertThat(softly.getFailureCount()).as("Aggregator Failure Count").isEqualTo(failures);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Date Greater Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateGreaterThan() {
        String expected = "12/29/2018";
        AssertJUtil.assertThat(DEC_12_31_2018)
                .as("testDateGreaterThan")
                .is(AssertJCondition.dateGreaterThan(expected, DATE_PATTERN));
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("BigDecimal Greater Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalGreaterThan() {
        BigDecimal actual = new BigDecimal("5.01");
        BigDecimal expected = new BigDecimal("5");
        AssertJUtil.assertThat(actual).as("testBigDecimalGreaterThan").isGreaterThan(expected);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Date Greater Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateGreaterThanOrEqualTo() {
        String expected = "12/29/2018";
        AssertJUtil.assertThat(DEC_12_31_2018)
                .as("testDateGreaterThanOrEqualTo")
                .is(AssertJCondition.dateGreaterThanOrEqualTo(expected, DATE_PATTERN));
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("BigDecimal Greater Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalGreaterThanOrEqualTo() {
        BigDecimal actual = new BigDecimal("5.01");
        BigDecimal expected = new BigDecimal("5");
        AssertJUtil.assertThat(actual).as("testBigDecimalGreaterThanOrEqualTo").isGreaterThanOrEqualTo(expected);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Date Less Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateLessThan() {
        String actual = "12/30/2018";
        AssertJUtil.assertThat(actual).as("testDateLessThan").is(AssertJCondition.dateLessThan(DEC_12_31_2018, DATE_PATTERN));
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("BigDecimal Less Than")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalLessThan() {
        BigDecimal actual = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5.01");
        AssertJUtil.assertThat(actual).as("testBigDecimalLessThan").isLessThan(expected);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Date Less Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateLessThanOrEqualTo() {
        String actual = "12/30/2018";
        AssertJUtil.assertThat(actual)
                .as("testDateLessThanOrEqualTo")
                .is(AssertJCondition.dateLessThanOrEqualTo(DEC_12_31_2018, DATE_PATTERN));
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("BigDecimal Less Than Or Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalLessThanOrEqualTo() {
        BigDecimal actual = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5.01");
        AssertJUtil.assertThat(actual).as("testBigDecimalLessThanOrEqualTo").isLessThanOrEqualTo(expected);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Date Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testDateEqualTo() {
        AssertJUtil.assertThat(DEC_12_31_2018)
                .as("testDateEqualTo")
                .is(AssertJCondition.dateEqualTo(DEC_12_31_2018, DATE_PATTERN));
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("BigDecimal Equal To")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testBigDecimalEqualTo() {
        BigDecimal actual = new BigDecimal("5");
        BigDecimal expected = new BigDecimal("5.00");
        AssertJUtil.assertThat(actual).as("testBigDecimalEqualTo").isEqualByComparingTo(expected);
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("Objects")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void verifyObjectsTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
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

        AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
        softly.assertThat(actual).isEqualToIgnoringNullFields(expected);

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

        AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
        softly.assertThat(actual).isEqualToIgnoringNullFields(expected);

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
            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
            throw new RuntimeException("Assertion did not fail:  fieldString2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldString2)", true);
        }

        try {
            expected.fieldString2 = "xyz";
            expected.fieldInteger1 = 10;

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
            throw new RuntimeException("Assertion did not fail:  fieldInteger1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldInteger1)", true);
        }

        try {
            expected.fieldInteger1 = 1;
            expected.fieldInt2 = 40;

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
            throw new RuntimeException("Assertion did not fail:  fieldInt2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldInt2)", true);
        }

        try {
            expected.fieldInt2 = 4;
            expected.fieldBoolean1 = false;

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
            throw new RuntimeException("Assertion did not fail:  fieldBoolean1");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldBoolean1)", true);
        }

        try {
            expected.fieldBoolean1 = true;
            expected.fieldBool2 = true;

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
            throw new RuntimeException("Assertion did not fail:  fieldBool2");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (fieldBool2)", true);
        }

        expected.fieldBool2 = false;
        AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);

        // All fields verified, multiple failures
        try {
            expected.fieldString2 = "def";
            expected.fieldInteger2 = 20;
            expected.fieldInt2 = 40;
            expected.fieldBoolean2 = true;
            expected.fieldBool1 = false;

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
            throw new RuntimeException("Assertion did not fail:  multiple failures");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (multiple failures)", true);
        }

        expected.fieldString2 = "xyz";
        expected.fieldInteger2 = 2;
        expected.fieldInt2 = 4;
        expected.fieldBoolean2 = false;
        expected.fieldBool1 = true;
        AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);

        // Some fields skipped, 1 failure
        try {
            expected.fieldString1 = null;
            expected.fieldString2 = "def";
            expected.fieldInteger1 = null;
            expected.fieldBoolean1 = null;

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
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
        AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);

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

            softly.assertThat(actual).isEqualToIgnoringNullFields(expected);
            failures++;
            AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);
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
        AssertJUtil.assertThat(actual).isEqualToIgnoringNullFields(expected);

        AssertJUtil.assertThat(softly.getFailureCount()).as("Aggregator Failure Count").isEqualTo(failures);
    }

    private void genericValidateAction(CustomSoftAssertions softly, ObjA objA, final Runnable runIfNonNullChecksPass) {
        boolean allNonNull = validateNotNullNested(softly, objA);
        if (allNonNull) {
            runIfNonNullChecksPass.run();
        }
    }

    private boolean validateNotNullNested(CustomSoftAssertions softly, ObjA objA) {
        try {
            validateNotNullNestedUnchecked(softly, objA);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void validateNotNullNestedUnchecked(CustomSoftAssertions softly, ObjA objA) {
        softly.assertThat(objA).as("objA").isNotNull();
        softly.assertThat(objA.nestedBinA).as("nestedBinA").isNotNull();
        softly.assertThat(objA.nestedBinA.nestedCinB).as("nestedCinB").isNotNull();
        softly.assertThat(objA.nestedBinA.nestedCinB.nestedDinC).as("nestedDinC").isNotNull();

        softly.assertThat(objA.nestedCinA).as("nestedCinA").isNotNull();
        softly.assertThat(objA.nestedCinA.nestedDinC).as("nestedDinC").isNotNull();
        softly.assertThat(objA.nestedCinA.nestedDinC.field2D).as("field2D").isNotNull();
        softly.assertThat(objA.nestedCinA.nestedDinC.field2D.toString()).as("use toString on  field2D").isNotNull();

        softly.assertThat(objA.nestedDinA).as("nestedDinA").isNotNull();
        softly.assertThat(objA.nestedDinA.field3D).as("field3D").isNotNull();
        softly.assertThat(objA.nestedDinA.field3D.toString()).as("use toString on field3D").isNotNull();
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Perform Root Object Is Null Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performRootObjectIsNullTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        genericValidateAction(softly, null, () -> AssertJUtil.fail("Root object was null"));
        AssertJUtil.assertThat(softly.getFailureCount()).as(FAILURE_COUNT).isEqualTo(1);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Perform Nested Child Object Is Null Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performNestedChildObjectIsNullTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        ObjA objA = new ObjA();
        genericValidateAction(softly, objA, () -> AssertJUtil.fail("nestedBinA was null"));
        AssertJUtil.assertThat(softly.getFailureCount()).as(FAILURE_COUNT).isEqualTo(1);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Perform Nested Child Object (level 2) Is Null Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performNestedChildObjectLevel2IsNullTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        ObjA objA = new ObjA();
        objA.nestedBinA = new ObjB();
        genericValidateAction(softly, objA, () -> AssertJUtil.fail("nestedCinB was null"));
        AssertJUtil.assertThat(softly.getFailureCount()).as(FAILURE_COUNT).isEqualTo(1);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Perform Nested Child Object (level 3) Is Null Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performNestedChildObjectLevel3IsNullTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        ObjA objA = new ObjA();
        objA.nestedBinA = new ObjB();
        objA.nestedBinA.nestedCinB = new ObjC();
        genericValidateAction(softly, objA, () -> AssertJUtil.fail("nestedDinC was null"));

        // Note: The extra failure is due objA.nestedCinA being null (this causes the exception in the unchecked method)
        AssertJUtil.assertThat(softly.getFailureCount()).as(FAILURE_COUNT).isEqualTo(2);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Perform Nested Field Is Null Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performNestedFieldIsNullTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        ObjA objA = new ObjA();
        objA.nestedBinA = new ObjB();
        objA.nestedBinA.nestedCinB = new ObjC();
        objA.nestedBinA.nestedCinB.nestedDinC = new ObjD();
        objA.nestedCinA = new ObjC();
        objA.nestedCinA.nestedDinC = new ObjD();

        genericValidateAction(softly, objA, () -> AssertJUtil.fail("field2D was null"));
        AssertJUtil.assertThat(softly.getFailureCount()).as(FAILURE_COUNT).isEqualTo(1);
    }

    @SuppressWarnings("java:S3252")
    @Features("AssertJUtil")
    @Stories("Perform Nested Pass Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performNestedPassTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        boolean field2D = Rand.randomBoolean();
        int field3D = Rand.randomRange(0, 1000);
        ObjA objA = new ObjA();
        objA.nestedBinA = new ObjB();
        objA.nestedBinA.nestedCinB = new ObjC();
        objA.nestedBinA.nestedCinB.nestedDinC = new ObjD();
        objA.nestedCinA = new ObjC();
        objA.nestedCinA.nestedDinC = new ObjD();
        objA.nestedCinA.nestedDinC.field2D = field2D;
        objA.nestedDinA = new ObjD();
        objA.nestedDinA.field3D = field3D;

        genericValidateAction(softly, objA, () -> {
            AssertJUtil.assertThat(objA.nestedCinA.nestedDinC.field2D).as("field2D").isEqualTo(field2D);
            AssertJUtil.assertThat(objA.nestedDinA.field3D).as("field3D").isEqualTo(field3D);
        });
        softly.assertAll();
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("Perform Expected Failure Test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performExpectedFailureTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        boolean result;
        String log;

        log = "Assertion is supposed to fail";
        result = softly.assertExpectedFailure(log, () -> AssertJUtil.assertThat(5000).isEqualTo(400));
        AssertJUtil.assertThat(result).as(log).isTrue();

        log = "Exception is thrown";
        result = softly.assertExpectedFailure(log, () -> { throw new RuntimeException("Testing"); });
        AssertJUtil.assertThat(result).as(log).isTrue();

        // No errors at this point
        softly.assertAll();

        log = "Assertion was successful";
        result = softly.assertExpectedFailure(log, () -> AssertJUtil.assertThat("").isEmpty());
        AssertJUtil.assertThat(result).as(log).isFalse();

        // There is a failure now
        AssertJUtil.assertThat(softly.getFailureCount()).as(FAILURE_COUNT).isEqualTo(1);

        AssertJUtil.assertExpectedFailure("Should not stop execution", () -> AssertJUtil.assertThat(25).isEqualTo(24));
    }

    @SuppressWarnings({"java:S3252", "java:S112"})
    @Features("AssertJUtil")
    @Stories("Perform Expected Failure that stops execution")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performExpectedFailureStopsExecutionTest() {
        try {
            AssertJUtil.assertExpectedFailure("Assertion is supposed to fail", () -> AssertJUtil.assertThat(25).isEqualTo(25));
            throw new RuntimeException("assertExpectedFailure did not stop execution");
        } catch (AssertionError ae) {
            Helper.log("assertExpectedFailure stopped execution as expected", true);
        }
    }

    @SuppressWarnings("java:S3252")
    @Features("Helper")
    @Stories("Perform Helper.isEqualSize tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performIsEqualSizeTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        List<String> actual;
        List<String> expected;
        boolean result;

        actual = null;
        expected = null;
        result = Helper.isEqualSize("Both null", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Both null").isFalse();

        expected = new ArrayList<>();
        result = Helper.isEqualSize("Only Actual null", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Only Actual null").isFalse();

        actual = new ArrayList<>();
        expected = null;
        result = Helper.isEqualSize("Only Expected null", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Only Expected null").isFalse();

        actual = new ArrayList<>();
        expected = new ArrayList<>();
        result = Helper.isEqualSize("Both Empty", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Both Empty").isTrue();

        actual = new ArrayList<>();
        actual.add("aaa");
        expected = new ArrayList<>();
        expected.add("bbb");
        result = Helper.isEqualSize("Non-Empty Same Size", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Non-Empty Same Size").isTrue();

        actual.add("ccc");
        result = Helper.isEqualSize("Actual Larger", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Actual Larger").isFalse();

        expected.add("ddd");
        expected.add("eee");
        result = Helper.isEqualSize("Expected Larger", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Expected Larger").isFalse();

        actual.add("fff");
        result = Helper.isEqualSize("Equal Again", softly, actual, expected);
        AssertJUtil.assertThat(result).as("Equal Again").isTrue();
    }

    @SuppressWarnings("java:S3252")
    @Features("Helper")
    @Stories("Perform Helper.isAtLeast tests")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performIsAtLeastTest() {
        CustomSoftAssertions softly = new CustomSoftAssertions();
        List<String> actual;
        boolean result;

        actual = null;
        result = Helper.isAtLeast("null - size 0", softly, actual, -1);
        AssertJUtil.assertThat(result).as("null - size 0").isFalse();

        result = Helper.isAtLeast("null - size -1", softly, actual, 0);
        AssertJUtil.assertThat(result).as("null - size -1").isFalse();

        result = Helper.isAtLeast("null - size 1", softly, actual, 1);
        AssertJUtil.assertThat(result).as("null - size 1").isFalse();

        actual = new ArrayList<>();
        result = Helper.isAtLeast("empty -1", softly, actual, -1);
        AssertJUtil.assertThat(result).as("empty -1").isTrue();

        result = Helper.isAtLeast("empty zero", softly, actual, 0);
        AssertJUtil.assertThat(result).as("empty zero").isTrue();

        result = Helper.isAtLeast("empty +1", softly, actual, 1);
        AssertJUtil.assertThat(result).as("empty +1").isFalse();

        actual.add("aaa");
        result = Helper.isAtLeast("list size=1, expect=-1", softly, actual, -1);
        AssertJUtil.assertThat(result).as("list size=1, expect=-1").isTrue();

        result = Helper.isAtLeast("list size=1, expect=0", softly, actual, 0);
        AssertJUtil.assertThat(result).as("list size=1, expect=0").isTrue();

        result = Helper.isAtLeast("list size=1, expect=+1", softly, actual, 1);
        AssertJUtil.assertThat(result).as("list size=1, expect=+1").isTrue();

        actual.add("bbb");
        result = Helper.isAtLeast("list size=2, expect=-1", softly, actual, -1);
        AssertJUtil.assertThat(result).as("list size=2, expect=-1").isTrue();

        result = Helper.isAtLeast("list size=2, expect=0", softly, actual, 0);
        AssertJUtil.assertThat(result).as("list size=2, expect=0").isTrue();

        result = Helper.isAtLeast("list size=2, expect=+1", softly, actual, 1);
        AssertJUtil.assertThat(result).as("list size=2, expect=+1").isTrue();

        result = Helper.isAtLeast("list size=2, expect=+2", softly, actual, 2);
        AssertJUtil.assertThat(result).as("list size=2, expect=+2").isTrue();

        result = Helper.isAtLeast("list size=2, expect=+3", softly, actual, 3);
        AssertJUtil.assertThat(result).as("list size=2, expect=+3").isFalse();
    }

}
