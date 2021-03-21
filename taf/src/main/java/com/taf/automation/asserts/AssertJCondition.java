package com.taf.automation.asserts;

import com.taf.automation.ui.support.DateActions;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.assertj.core.api.Condition;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class AssertJCondition {
    private enum DateCompareType {
        EQUAL_TO,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL_TO,
        LESS_THAN,
        LESS_THAN_OR_EQUAL_TO
    }

    private AssertJCondition() {
        // Prevent initialization of class as all public methods should be static
    }

    public static <T> Condition<T> isEqualToIgnoringNullFields(T expected) {
        String logExpected = "null";
        if (expected != null) {
            ToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
            logExpected = ReflectionToStringBuilder.toString(expected);
        }

        String description = "actual (%s) to equal expected (" + logExpected + ")";
        return new Condition<T>() {
            @Override
            public boolean matches(T value) {
                if (expected == null) {
                    return true;
                }

                String logActual = "null";
                if (value != null) {
                    ToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
                    logActual = ReflectionToStringBuilder.toString(value);
                }

                as(description, logActual);
                CustomSoftAssertions softly = new CustomSoftAssertions();
                softly.assertThat(value).isEqualToIgnoringNullFields(expected);
                return softly.allSuccessful();
            }
        };
    }

    public static Condition<BigDecimal> range(final BigDecimal min, final BigDecimal max) {
        String logMin = (min == null) ? "infinite (null)" : min.toString();
        String logMax = (max == null) ? "infinite (null)" : max.toString();
        String description = "in the range [" + logMin + "," + logMax + "] but it was not";
        return new Condition<BigDecimal>(description) {
            @Override
            public boolean matches(BigDecimal value) {
                boolean lowerBound = Utils.compareTo(value, min) >= 0;
                boolean upperBound = Utils.compareTo(value, max) <= 0;
                return lowerBound && upperBound;
            }
        };
    }

    public static Condition<String> dateGreaterThan(final String expectedDate, final String... parsePatterns) {
        return dateCompare(DateCompareType.GREATER_THAN, expectedDate, parsePatterns);
    }

    public static Condition<String> dateGreaterThanOrEqualTo(final String expectedDate, final String... parsePatterns) {
        return dateCompare(DateCompareType.GREATER_THAN_OR_EQUAL_TO, expectedDate, parsePatterns);
    }

    public static Condition<String> dateLessThan(final String expectedDate, final String... parsePatterns) {
        return dateCompare(DateCompareType.LESS_THAN, expectedDate, parsePatterns);
    }

    public static Condition<String> dateLessThanOrEqualTo(final String expectedDate, final String... parsePatterns) {
        return dateCompare(DateCompareType.LESS_THAN_OR_EQUAL_TO, expectedDate, parsePatterns);
    }

    public static Condition<String> dateEqualTo(final String expectedDate, final String... parsePatterns) {
        return dateCompare(DateCompareType.EQUAL_TO, expectedDate, parsePatterns);
    }

    private static Condition<String> dateCompare(
            final DateCompareType dateCompareType,
            final String expectedDate,
            final String... parsePatterns
    ) {
        return new Condition<String>() {
            @Override
            public boolean matches(String value) {
                Date actual = DateActions.parseDateStrictly(value, parsePatterns);
                if (actual == null) {
                    as("actual date (%s) could not be parsed", value);
                    return false;
                }

                Date expected = DateActions.parseDateStrictly(expectedDate, parsePatterns);
                if (expected == null) {
                    as("expected date (%s) could not be parsed", expectedDate);
                    return false;
                }

                if (dateCompareType == DateCompareType.GREATER_THAN) {
                    as("greater than \"%s\" using parse patterns:  %s", expectedDate, Arrays.toString(parsePatterns));
                    return actual.compareTo(expected) > 0;
                } else if (dateCompareType == DateCompareType.GREATER_THAN_OR_EQUAL_TO) {
                    as("greater than or equal to \"%s\" using parse patterns:  %s", expectedDate, Arrays.toString(parsePatterns));
                    return actual.compareTo(expected) >= 0;
                } else if (dateCompareType == DateCompareType.LESS_THAN) {
                    as("less than \"%s\" using parse patterns:  %s", expectedDate, Arrays.toString(parsePatterns));
                    return actual.compareTo(expected) < 0;
                } else if (dateCompareType == DateCompareType.LESS_THAN_OR_EQUAL_TO) {
                    as("less than or equal to \"%s\" using parse patterns:  %s", expectedDate, Arrays.toString(parsePatterns));
                    return actual.compareTo(expected) <= 0;
                } else {
                    as("equal to \"%s\" using parse patterns:  %s", expectedDate, Arrays.toString(parsePatterns));
                    return actual.compareTo(expected) == 0;
                }
            }
        };
    }

}
