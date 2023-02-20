package com.taf.automation.ui.support.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * BigDecimal Utils for use with JEXL Expressions in test data
 */
@SuppressWarnings("squid:S3252")
public class BigDecimalUtils {
    private BigDecimalUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Get Instance for use with JEXL Expressions
     *
     * @return BigDecimalUtils
     */
    public static BigDecimalUtils getInstance() {
        return new BigDecimalUtils();
    }

    public static BigDecimal create(String value) {
        return new BigDecimal(value);
    }

    public static BigDecimal create(String value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal create(String value, int scale, String roundingMode) {
        return new BigDecimal(value).setScale(scale, rounding(roundingMode));
    }

    public static RoundingMode rounding(String name) {
        try {
            return RoundingMode.valueOf(name.toUpperCase());
        } catch (Exception ex) {
            return RoundingMode.HALF_UP;
        }
    }

    public static BigDecimal add(String lhs, String rhs) {
        return create(lhs).add(create(rhs));
    }

    public static BigDecimal subtract(String lhs, String rhs) {
        return create(lhs).subtract(create(rhs));
    }

    public static BigDecimal multiply(String lhs, String rhs) {
        return create(lhs).multiply(create(rhs));
    }

    /**
     * Perform division<BR>
     * <B>Examples:</B>
     * <OL>
     * <LI>
     * <DIV>5/2 = 2.5;</DIV>
     * <DIV>divide("5","2","","") == 2.50</DIV>
     * <DIV>divide("5","2","0","") == 3</DIV>
     * <DIV>divide("5","2","0","FLOOR") == 2</DIV>
     * </LI>
     * <LI>
     * <DIV>11/3 ~= 3.666666 (quotient does not have a terminating decimal expansion)</DIV>
     * <DIV>divide("11","3","","") == 3.67</DIV>
     * <DIV>divide("11","3","5","") == 3.66667</DIV>
     * <DIV>divide("11","3","5","FLOOR") == 3.66666</DIV>
     * </LI>
     * </OL>
     * <B>Notes: </B>
     * <OL>
     * <LI>If Scale is invalid (such as null, empty or letters), then max scale of dividend, divisor or 2 is used.
     * The value of 2 was chosen as one of the defaults because if dividing integers, the quotient will be closer to
     * being correct and 2 decimals places if fairly commonly assumed.
     * </LI>
     * <LI>If Rounding Mode is invalid (such as null or empty), then RoundingMode.HALF_UP is used</LI>
     * </OL>
     *
     * @param lhs          - Left hand side (dividend)
     * @param rhs          - Right hand side (divisor)
     * @param scale        - Scale (if invalid, then max scale of dividend, divisor or 2 is used)
     * @param roundingMode - Rounding Mode (if invalid, then RoundingMode.HALF_UP is used)
     * @return BigDecimal (quotient)
     */
    public static BigDecimal divide(String lhs, String rhs, String scale, String roundingMode) {
        BigDecimal dividend = create(lhs);
        BigDecimal divisor = create(rhs);
        int defaultScale = Math.max(dividend.scale(), divisor.scale());
        defaultScale = Math.max(defaultScale, 2);
        return dividend.divide(divisor, NumberUtils.toInt(scale, defaultScale), rounding(roundingMode));
    }

    /**
     * Convert currency amount from a String to BigDecimal<BR>
     * <B>Notes:</B><BR>
     * 1) The locale determines what is the decimal separator and the group separator<BR>
     * 2) Only successfully parsed amounts greater than or equal to 0 will be returned<BR>
     *
     * @param amount - Currency amount as a String
     * @param locale - Locale.CANADA for English or Locale.CANADA_FRENCH for French currency format
     * @return BigDecimal version of the currency amount
     */
    public static BigDecimal parse(final String amount, final Locale locale) {
        return parse(amount, locale, null);
    }

    /**
     * Convert currency amount from a String to BigDecimal<BR>
     * <B>Notes:</B><BR>
     * 1) The locale determines what is the decimal separator and the group separator<BR>
     * 2) Only successfully parsed amounts greater than or equal to 0 will be returned<BR>
     *
     * @param amount       - Currency amount as a String
     * @param locale       - Locale.CANADA for English or Locale.CANADA_FRENCH for French currency format
     * @param defaultValue - If unable to parse the amount, then return this value unless it is <B>null</B>
     * @return BigDecimal version of the currency amount
     */
    public static BigDecimal parse(final String amount, final Locale locale, final BigDecimal defaultValue) {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        // Clean the amount of all non-digits/periods/commas
        String cleanedAmount = amount.replaceAll("[^\\d.,]", "");
        try {
            return (BigDecimal) format.parse(cleanedAmount);
        } catch (ParseException exception) {
            AssertJUtil.assertThat(defaultValue).as("Unable to parse value to BigDecimal:  " + cleanedAmount).isNotNull();
            return defaultValue;
        }
    }

    /**
     * Convert currency amount from a String to BigDecimal<BR>
     * <B>Notes:</B>
     * 1) The locale determines what is the decimal separator and the group separator<BR>
     * 2) Only successfully parsed amounts greater than or equal to 0 will be returned<BR>
     *
     * @param amount         - Currency amount as a String
     * @param locale         - Locale.CANADA for English or Locale.CANADA_FRENCH for French currency format
     * @param defineInfinite - true to define infinite as null
     * @return null if defineInfinite is true and amount is null or empty string, else BigDecimal version of the currency amount
     */
    public static BigDecimal parse(final String amount, final Locale locale, final boolean defineInfinite) {
        if (defineInfinite && StringUtils.defaultString(amount).equals("")) {
            return null;
        }

        return parse(amount, locale);
    }

    /**
     * Compare values handling null values<BR>
     * <B>Notes:</B><BR>
     * 1) null is considered to be infinite<BR>
     *
     * @param lhs - Left Hand Side value
     * @param rhs - Right Hand Side value
     * @return 0 if lhs equals rhs, 1 if lhs greater than rhs, -1 if lhs less than rhs
     */
    public static int compareTo(BigDecimal lhs, BigDecimal rhs) {
        // If both values infinite, then we consider them equal
        if (lhs == null && rhs == null) {
            return 0;
        }

        // If only the lhs is infinite, then it is greater than the rhs
        if (lhs == null) {
            return 1;
        }

        // If only the rhs is infinite, then lhs is less than the rhs
        if (rhs == null) {
            return -1;
        }

        return lhs.compareTo(rhs);
    }

    public static boolean equals(BigDecimal lhs, BigDecimal rhs) {
        return compareTo(lhs, rhs) == 0;
    }

    public static BigDecimal min(BigDecimal first, BigDecimal... others) {
        List<BigDecimal> all = new ArrayList<>();

        all.add(first);
        if (others != null) {
            all.addAll(Arrays.asList(others));
        }

        all.sort(BigDecimalUtils::compareTo);
        return all.get(0);
    }

    public static BigDecimal max(BigDecimal first, BigDecimal... others) {
        List<BigDecimal> all = new ArrayList<>();

        all.add(first);
        if (others != null) {
            all.addAll(Arrays.asList(others));
        }

        all.sort(BigDecimalUtils::compareTo);
        return all.get(all.size() - 1);
    }

}
