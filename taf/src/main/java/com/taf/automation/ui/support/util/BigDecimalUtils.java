package com.taf.automation.ui.support.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal Utils for use with JEXL Expressions in test data
 */
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

}
