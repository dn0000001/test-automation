package com.taf.automation.ui.support.generators;

import com.taf.automation.ui.support.util.Utils;
import datainstiller.generators.GeneratorInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomDateGenerator implements GeneratorInterface {
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

    @Override
    public String generate(String pattern, String value) {
        String usePattern = (StringUtils.defaultString(pattern).trim().equals("")) ? "yyyy-MM-dd" : pattern;
        String[] pieces = Utils.splitData(StringUtils.defaultString(value), "|", 2);

        String start = StringUtils.defaultString(pieces[0]);
        start = (start.equals("")) ? "0" : start;

        String end = StringUtils.defaultString(pieces[1]);
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

        SimpleDateFormat sdf = new SimpleDateFormat(usePattern);
        Date today = new Date();
        return sdf.format(DateUtils.addDays(today, range));
    }

}
