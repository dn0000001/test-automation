package com.taf.automation.ui.app.domainObjects;

import com.taf.automation.ui.support.TestContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Utility to generate a single random date which does not change during execution using aliases
 */
public class RandomDateUtil {
    private static final RandomDateUtil instance = new RandomDateUtil();

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
        if (randomDate == null) {
            String pattern = StringUtils.defaultString(TestContext.getGlobalAliases().get(RANDOM_DATE_PATTERN));
            pattern = (pattern.trim().equals("")) ? "yyyy-MM-dd" : pattern;

            String start = StringUtils.defaultString(TestContext.getGlobalAliases().get(RANDOM_DATE_RANGE_MIN));
            start = (start.equals("")) ? "0" : start;

            String end = StringUtils.defaultString(TestContext.getGlobalAliases().get(RANDOM_DATE_RANGE_MAX));
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
        }

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

        Random r = new Random();
        return offset + r.nextInt(max - min);
    }

}
