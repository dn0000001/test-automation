package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.DateActions;
import datainstiller.generators.DateGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import ui.auto.core.context.PageComponentContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Utility to generate a single random date which does not change during execution using aliases
 */
public class RandomDateUtil {
    private static final RandomDateUtil instance = new RandomDateUtil();
    private final Random random = new Random();

    /**
     * Alias used to get the pattern
     */
    private static final String RANDOM_DATE_PATTERN = "random_date_pattern";

    /**
     * Alias used to get the minimum days for the random range (can be negative for past dates)
     */
    private static final String RANDOM_DATE_RANGE_MIN = "random_date_range_min";

    /**
     * Alias used to get the maximum days for the random range (can be negative for past dates)
     */
    private static final String RANDOM_DATE_RANGE_MAX = "random_date_range_max";

    /**
     * Generated random date from current date
     */
    private String randomDate;

    private RandomDateUtil() {
        randomDate = null;
    }

    /**
     * Get Instance
     *
     * @return RandomDateUtil
     */
    public static RandomDateUtil getInstance() {
        return instance;
    }

    /**
     * Get random date (from today) using aliases<BR>
     * <BR>
     * <B>Notes: </B><BR>
     * 1) Random date is only generate once<BR>
     * <BR>
     * <B>Default Alias Values:</B><BR>
     * 1) ${random_date_range_min} - Minimum days for the random range (can be negative for past dates)<BR>
     * 2) ${random_date_range_max} - Maximum days for the random range (can be negative for past dates)<BR>
     * 3) ${random_date_pattern} - Pattern for returned date string defaults to "yyyy-MM-dd"<BR>
     *
     * @return String in pattern format
     */
    public String random() {
        if (randomDate != null) {
            return randomDate;
        }

        String pattern = StringUtils.defaultString(PageComponentContext.getGlobalAliases().get(RANDOM_DATE_PATTERN));
        pattern = (pattern.trim().equals("")) ? "yyyy-MM-dd" : pattern;

        String start = StringUtils.defaultString(PageComponentContext.getGlobalAliases().get(RANDOM_DATE_RANGE_MIN));
        start = (start.equals("")) ? "0" : start;

        String end = StringUtils.defaultString(PageComponentContext.getGlobalAliases().get(RANDOM_DATE_RANGE_MAX));
        end = (end.equals("")) ? "0" : end;

        int startInclusive = NumberUtils.toInt(start);
        int endExclusive = NumberUtils.toInt(end);
        int range;
        if (startInclusive == endExclusive) {
            // Add 1 to prevent error java.lang.IllegalArgumentException: bound must be positive
            range = randomRange(startInclusive, endExclusive + 1);
        } else {
            range = randomRange(startInclusive, endExclusive);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date today = new Date();
        randomDate = sdf.format(DateUtils.addDays(today, range));

        return randomDate;
    }

    /**
     * Returns a number in the specified range
     *
     * @param startInclusive - the smallest value that can be returned
     * @param endExclusive   - the upper bound (not included)
     * @return int
     */
    private int randomRange(int startInclusive, int endExclusive) {
        // Set the default values for the range
        int min = startInclusive;
        int max = endExclusive;

        // Get a valid range
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        // Need to shift the range to be 0 to Max for random number generation. The offset will be used after to put random number in range.
        int offset = min;

        // Need to ensure range is positive
        if (min < 0) {
            min += -1 * offset;
            max += -1 * offset;
        }

        return offset + random.nextInt(max - min);
    }

    /**
     * This is wrapper around DateGenerator that takes into account a max date
     *
     * @param pattern - The pattern of the date string to be returned
     * @param value   - The normal value passed to the DateGenerator (ex. "01/01/2020|01/01/2021|MM/dd/yyyy")
     * @param maxDate - The max date to test against
     * @return random date string in the pattern
     */
    public String range(String pattern, String value, Date maxDate) {
        String[] pieces = StringUtils.defaultString(value).split("\\|");
        assertThat("Random Date value invalid format", pieces.length, equalTo(3));
        assertThat("Max Date", maxDate, notNullValue());

        String start = pieces[0];
        String end = pieces[1];
        String valueDatePattern = pieces[2];

        // If max date is before the start date, then use the max date
        Date startDate = DateActions.parseDateStrictly(start, valueDatePattern);
        assertThat("Start Date", startDate, notNullValue());
        if (maxDate.compareTo(startDate) < 0) {
            return DateActions.format(maxDate, pattern);
        }

        // If max date is after the end date, then use the end date else use the max date
        Date endDate = DateActions.parseDateStrictly(end, valueDatePattern);
        assertThat("End Date", endDate, notNullValue());
        if (maxDate.compareTo(endDate) > 0) {
            return new DateGenerator().generate(pattern, value);
        } else {
            String useValue = start + "|" + DateActions.format(maxDate, valueDatePattern) + "|" + valueDatePattern;
            return new DateGenerator().generate(pattern, useValue);
        }
    }

}
