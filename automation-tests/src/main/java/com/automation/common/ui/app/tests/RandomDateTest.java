package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.RandomDateUtil;
import com.taf.automation.ui.support.DateActions;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.annotations.Test;
import ui.auto.core.context.PageComponentContext;

import java.util.Date;

import static com.taf.automation.ui.support.util.AssertsUtil.dateEqualTo;
import static com.taf.automation.ui.support.util.AssertsUtil.dateGreaterThanOrEqualTo;
import static com.taf.automation.ui.support.util.AssertsUtil.dateLessThan;
import static com.taf.automation.ui.support.util.AssertsUtil.dateLessThanOrEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Random Date tests.<BR>
 * <B>Note: </B> Only 1 test can be run at a time as random date is only generated once per thread<BR>
 */
public class RandomDateTest {
    private static final String RANDOM_DATE_RANGE_MIN = "random_date_range_min";
    private static final String RANDOM_DATE_RANGE_MAX = "random_date_range_max";
    private static final String RANDOM_DATE_PATTERN = "random_date_pattern";
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    @Test
    public void testRandomDates() {
        //
        // Note: Only 1 test can be run at a time as random date is only generated once per thread
        //
        String actual = RandomDateUtil.getInstance().random();
        String expected = DateActions.format(new Date(), DEFAULT_PATTERN);
        assertThat("No Aliases - Expect today as result with default pattern", actual, dateEqualTo(expected, DEFAULT_PATTERN));
    }

    @Test
    public void testRandomDatesMinOnly() {
        //
        // Note: Only 1 test can be run at a time as random date is only generated once per thread
        //
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_RANGE_MIN, "10");
        String actual = RandomDateUtil.getInstance().random();
        Date todayDate = new Date();
        String today = DateActions.format(todayDate, DEFAULT_PATTERN);
        String plus10 = DateActions.format(DateUtils.addDays(todayDate, 10), DEFAULT_PATTERN);
        assertThat("Generated >= Today", actual, dateGreaterThanOrEqualTo(today, DEFAULT_PATTERN));
        assertThat("Generated < (Today + 10)", actual, dateLessThan(plus10, DEFAULT_PATTERN));
    }

    @Test
    public void testRandomDatesMaxOnly() {
        //
        // Note: Only 1 test can be run at a time as random date is only generated once per thread
        //
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_RANGE_MAX, "-10");
        String actual = RandomDateUtil.getInstance().random();
        Date todayDate = new Date();
        String today = DateActions.format(todayDate, DEFAULT_PATTERN);
        String minus10 = DateActions.format(DateUtils.addDays(todayDate, -10), DEFAULT_PATTERN);
        assertThat("Generated <= Today", actual, dateLessThanOrEqualTo(today, DEFAULT_PATTERN));
        assertThat("Generated >= (Today - 10)", actual, dateGreaterThanOrEqualTo(minus10, DEFAULT_PATTERN));
    }

    @Test
    public void testRandomDatesPatternOnly() {
        //
        // Note: Only 1 test can be run at a time as random date is only generated once per thread
        //
        String testPattern = "MMMM dd, yyyy";
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_PATTERN, testPattern);
        String actual = RandomDateUtil.getInstance().random();
        String expected = DateActions.format(new Date(), testPattern);
        assertThat("Expect today as result with test pattern", actual, dateEqualTo(expected, testPattern));
    }

    @Test
    public void testRandomDatesMinMax() {
        //
        // Note: Only 1 test can be run at a time as random date is only generated once per thread
        //
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_RANGE_MIN, "-30");
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_RANGE_MAX, "30"); // Excludes
        String actual = RandomDateUtil.getInstance().random();
        Date todayDate = new Date();
        String plus30 = DateActions.format(DateUtils.addDays(todayDate, 30), DEFAULT_PATTERN);
        String minus30 = DateActions.format(DateUtils.addDays(todayDate, -30), DEFAULT_PATTERN);
        assertThat("Generated <= (Today + 30)", actual, dateLessThan(plus30, DEFAULT_PATTERN));
        assertThat("Generated >= (Today - 30)", actual, dateGreaterThanOrEqualTo(minus30, DEFAULT_PATTERN));
    }

    @Test
    public void testRandomDatesAll() {
        //
        // Note: Only 1 test can be run at a time as random date is only generated once per thread
        //
        String testPattern = "MMMM dd, yyyy";
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_RANGE_MIN, "-90");
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_RANGE_MAX, "90"); // Excludes
        PageComponentContext.getGlobalAliases().put(RANDOM_DATE_PATTERN, testPattern);
        String actual = RandomDateUtil.getInstance().random();
        Date todayDate = new Date();
        String plus90 = DateActions.format(DateUtils.addDays(todayDate, 90), testPattern);
        String minus90 = DateActions.format(DateUtils.addDays(todayDate, -90), testPattern);
        assertThat("Generated <= (Today + 90)", actual, dateLessThan(plus90, testPattern));
        assertThat("Generated >= (Today - 90)", actual, dateGreaterThanOrEqualTo(minus90, testPattern));
    }

    @Test
    public void testRandomRangeMaxDateInPast() {
        String testPattern = "dd/MM/yyyy";
        Date todayDate = new Date();
        Date pastDate = DateUtils.addDays(todayDate, -120);
        String past = DateActions.format(pastDate, testPattern);

        String minus90 = DateActions.format(DateUtils.addDays(todayDate, -90), testPattern);
        String plus90 = DateActions.format(DateUtils.addDays(todayDate, 90), testPattern);

        String value = minus90 + "|" + plus90 + "|" + testPattern;
        String actual = RandomDateUtil.getInstance().range(testPattern, value, pastDate);

        assertThat("Generated == Past Date", actual, dateEqualTo(past, testPattern));
    }

    @Test
    public void testRandomRangeMaxDateInFuture() {
        String testPattern = "MM/dd/yyyy";
        Date todayDate = new Date();
        Date futureDate = DateUtils.addDays(todayDate, 120);

        String minus90 = DateActions.format(DateUtils.addDays(todayDate, -90), testPattern);
        String plus90 = DateActions.format(DateUtils.addDays(todayDate, 90), testPattern);

        String value = minus90 + "|" + plus90 + "|" + testPattern;
        String actual = RandomDateUtil.getInstance().range(testPattern, value, futureDate);

        assertThat("Generated <= (Today + 90)", actual, dateLessThanOrEqualTo(plus90, testPattern));
        assertThat("Generated >= (Today - 90)", actual, dateGreaterThanOrEqualTo(minus90, testPattern));
    }

    @Test
    public void testRandomRangeMaxDateIsToday() {
        String testPattern = "MM/dd/yyyy";
        Date todayDate = new Date();

        String minus89 = DateActions.format(DateUtils.addDays(todayDate, -89), testPattern);
        String today = DateActions.format(todayDate, testPattern);
        String plus89 = DateActions.format(DateUtils.addDays(todayDate, 89), testPattern);

        String value = minus89 + "|" + plus89 + "|" + testPattern;
        String actual = RandomDateUtil.getInstance().range(testPattern, value, todayDate);

        assertThat("Generated <= Today", actual, dateLessThanOrEqualTo(today, testPattern));
        assertThat("Generated >= (Today - 89)", actual, dateGreaterThanOrEqualTo(minus89, testPattern));
    }

}
