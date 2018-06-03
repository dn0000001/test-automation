package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.automation.common.ui.app.pageObjects.TNHC_LandingPage;
import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.AssertsUtil;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.mutable.MutableInt;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Unit testing AssertsUtil class
 */
public class AssertsTest extends TestNGBase {
    private static final boolean run = false;

    @Features("AssertsUtil")
    @Stories("WebElement is displayed assert with null element")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = run)
    public void assertIsDisplayedWithElementDoesNotExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        WebElement actual = null;
        assertThat(actual, AssertsUtil.isElementDisplayed());
    }

    @Features("AssertsUtil")
    @Stories("WebElement is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = true)
    public void assertIsDisplayedWithElementExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        WebElement actual = driver.findElement(By.id("player"));
        assertThat(actual, AssertsUtil.isElementDisplayed());
    }

    @Features("AssertsUtil")
    @Stories("By is displayed assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = true)
    public void assertIsDisplayedWithByExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        assertThat(By.id("player"), AssertsUtil.isElementDisplayed(driver));
    }

    @Features("AssertsUtil")
    @Stories("WebElement is disabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = run)
    public void assertIsDisabledWithElementExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        WebElement actual = driver.findElement(By.id("player"));
        assertThat(actual, AssertsUtil.isElementDisabled());
    }

    @Features("AssertsUtil")
    @Stories("WebElement is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = true)
    public void assertIsEnabledWithElementExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        WebElement actual = driver.findElement(By.id("player"));
        assertThat(actual, AssertsUtil.isElementEnabled());
    }

    @Features("AssertsUtil")
    @Stories("By is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = true)
    public void assertIsEnabledWithByExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        assertThat(By.id("player"), AssertsUtil.isElementEnabled(driver));
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is enabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = true)
    public void assertIsEnabledWithComponentExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        TNHC_LandingPage landing = new TNHC_LandingPage(getContext());
        assertThat(landing.getPlayer(), AssertsUtil.isComponentEnabled());
    }

    @Features("AssertsUtil")
    @Stories("WebComponent is disabled assert with element that exists")
    @Severity(SeverityLevel.CRITICAL)
    @Test(enabled = run)
    public void assertIsDisabledWithComponentExistTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        TNHC_LandingPage landing = new TNHC_LandingPage(getContext());
        assertThat(landing.getPlayer(), AssertsUtil.isComponentDisabled());
    }

    @Features("Framework")
    @Stories("General Framework unit testing")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void generalUnitTest(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        TNHC_DO hnhc = new TNHC_DO(getContext()).fromResource(dataSet);
        getContext().getDriver().get(TestProperties.getInstance().getURL());

        TNHC_LandingPage landing = hnhc.getLanding();
        landing.setPlayerJS();
        landing.setTeam();
    }

    @Test
    public void testBigDecimalAssertions() {
        assertThat("All Infinite", null, AssertsUtil.range(null, null));
        assertThat("Infinite Less than or equal to infinite", null, AssertsUtil.range(new BigDecimal("-1"), null));
        assertThat("Any number less than infinite", new BigDecimal("5"), AssertsUtil.range(new BigDecimal("0"), null));
        assertThat("Lower bound equal test", new BigDecimal("5"), AssertsUtil.range(new BigDecimal("5"), null));
        assertThat("Infinite Less than or equal to infinite #2", null, AssertsUtil.range(new BigDecimal("0"), null));
        assertThat("Lower bound equal test #2", new BigDecimal("20"), AssertsUtil.range(new BigDecimal("0"), null));
    }

    @Test
    public void testBigDecimalAssertionFailures_InfiniteGreaterThanAnyNumber() {
        try {
            assertThat("InfiniteGreaterThanAnyNumber", null, AssertsUtil.range(null, new BigDecimal("90")));
            throw new RuntimeException("Assertion did not fail:  InfiniteGreaterThanAnyNumber");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteGreaterThanAnyNumber)", true);
        }
    }

    @Test
    public void testBigDecimalAssertionFailures_AnyNumberNotInRangeOfInfinity() {
        try {
            assertThat("AnyNumberNotInRangeOfInfinity", new BigDecimal("0"), AssertsUtil.range(null, null));
            throw new RuntimeException("Assertion did not fail:  AnyNumberNotInRangeOfInfinity");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (AnyNumberNotInRangeOfInfinity)", true);
        }
    }

    @Test
    public void testBigDecimalAssertionFailures_InfiniteNotInNumberRange() {
        try {
            assertThat("InfiniteNotInNumberRange", null, AssertsUtil.range(new BigDecimal("0"), new BigDecimal("10")));
            throw new RuntimeException("Assertion did not fail:  InfiniteNotInNumberRange");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteNotInNumberRange)", true);
        }
    }

    @Test
    public void testBigDecimalAssertionFailures_InfiniteAsLowerBoundAndNumberUpperBound() {
        try {
            assertThat("InfiniteAsLowerBoundAndNumberUpperBound", new BigDecimal("4"), AssertsUtil.range(null, new BigDecimal("333")));
            throw new RuntimeException("Assertion did not fail:  InfiniteAsLowerBoundAndNumberUpperBound");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteAsLowerBoundAndNumberUpperBound)", true);
        }
    }

    @Test
    public void testBigDecimalAssertionFailures_InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite() {
        try {
            assertThat("InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite", null, AssertsUtil.range(null, new BigDecimal("333")));
            throw new RuntimeException("Assertion did not fail:  InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite");
        } catch (AssertionError ae) {
            Helper.log("Assertion failed as expected (InfiniteAsLowerBoundAndNumberUpperBoundWithInfinite)", true);
        }
    }

    @Test
    public void testAggregator_AllSuccess() {
        AssertAggregator aa = new AssertAggregator();
        aa.setConsole(true);
        aa.assertThat("Some test #1", true);
        aa.assertThat("Some test #2", 10, greaterThan(5));
        aa.assertThat(10, greaterThan(5));
        Helper.assertThat(aa);
    }

    @Test
    public void testAggregator_AllFailures() {
        AssertAggregator aa = new AssertAggregator();
        aa.setConsole(true);
        aa.assertThat("Some test #1", false);
        aa.assertThat("Some test #2", 5, greaterThan(10));
        aa.assertThat(100, greaterThan(500));
        Helper.assertThat(aa);
    }

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
        Helper.assertThat(aa);
    }

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

}
