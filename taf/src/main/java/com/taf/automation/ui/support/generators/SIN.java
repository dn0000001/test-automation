package com.taf.automation.ui.support.generators;

import com.taf.automation.ui.support.Rand;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Class used to generate random valid SIN
 */
public class SIN {
    private SIN() {
        //
    }

    /**
     * Get Instance for use with JEXL Expressions
     *
     * @return Rand
     */
    public static SIN getInstance() {
        return new SIN();
    }

    /**
     * Calculate the check digit from a string of given digits <BR>
     * <B>Note:</B> digits needs to be exactly 8 characters or the calculation may not be correct<BR>
     *
     * @param digits - Digits to calculate the check digit from
     * @return the check digit
     */
    private static int calculateCheckDigit(String digits) {
        // If not correct number of characters, then just return false
        if (digits == null || digits.length() != 8) {
            return -2;
        }

        /*
         * Step 1 and 2: double the even-positioned digits, take the digits of each result, and sum them with
         * the odd-positioned digits.
         */
        int digitSum = 0;
        int currentDigit = 0;
        for (int i = 0; i < digits.length(); i++) {
            currentDigit = Integer.parseInt(digits.substring(i, i + 1));
            if (i % 2 == 0) {
                // Odd-positioned digit, add directly
                digitSum += currentDigit;
            } else {
                // Even-positioned digit, double first
                currentDigit *= 2;
                if (currentDigit < 10) {
                    // If the doubling results in a number < 10, simply add that number
                    digitSum += currentDigit;
                } else {
                    /*
                     * If the doubling results in a number >= 10, it will still be < 20, so the two digits
                     * will be 1, and the result modulo 10
                     */
                    digitSum += (1 + currentDigit % 10);
                }
            }
        }

        // Step 3 and 4: multiply by 9, take the last digit, and return the result
        return (digitSum * 9) % 10;
    }

    /**
     * Verify the check digit from a string of given digits<BR>
     * <B>Note:</B>
     * <OL>
     * <LI>digits needs to be exactly 9 characters or the calculation may not be correct as such returns false
     * immediately if this is not the case</LI>
     * </OL>
     *
     * @param digits - Digits to validate the check digit
     * @return true if check digit is correct else false
     */
    public static boolean verifyCheckDigit(String digits) {
        // If not correct number of characters, then just return false
        if (digits == null || digits.length() != 9) {
            return false;
        }

        // Calculate the check digit using the 1st 8 characters
        String first8 = digits.substring(0, digits.length() - 1);
        int expectedCheckDigit = calculateCheckDigit(first8);

        // Get the Actual Check Digit
        int actualCheckDigit = NumberUtils.toInt(digits.substring(digits.length() - 1), -1);

        // If calculated check digit matches actual check digit, then valid
        return expectedCheckDigit == actualCheckDigit;
    }

    /**
     * Generate a random valid SIN<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>Random SIN will not start with 0, 3 or 8 as these seem to be considered invalid regardless of the check digit.
     * Use one of the overloaded methods for more control</LI>
     * </OL>
     *
     * @return valid random SIN
     */
    public static String generate() {
        int[] validStartDigits = {1, 2, 4, 5, 6, 7, 9};
        int randomStartDigit = Rand.randomRange(0, validStartDigits.length - 1);
        return generate(validStartDigits[randomStartDigit]);
    }

    /**
     * Generate a random valid SIN that starts with the specified digit<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>It seems that certain numbers are not allowed to begin the SIN even though the check digit is valid.</LI>
     * <LI>Invalid starting digits appear to be 0, 3 & 8. If the randomly generated SIN is considered invalid,
     * then this is probably the reason</LI>
     * </OL>
     *
     * @param startWith - Random valid SIN will start with this digit
     * @return valid random SIN
     */
    public static String generate(int startWith) {
        // Generate 7 more random digits
        StringBuilder next7 = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            next7.append(Rand.randomRange(0, 9));
        }

        // Now calculate the check digit
        int checkDigit = calculateCheckDigit(startWith + next7.toString());

        // Return the valid random SIN
        return startWith + next7.toString() + checkDigit;
    }

    /**
     * Generate a random valid SIN that starts with one of the specified valid start digits<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>It seems that certain numbers are not allowed to begin the SIN even though the check digit is valid.</LI>
     * <LI>Invalid starting digits appear to be 0, 3 & 8. If the randomly generated SIN is considered invalid,
     * then this is probably the reason</LI>
     * </OL>
     *
     * @param validStartDigits - array of integers that the randomly generated SIN can start with
     * @return valid random SIN
     */
    public static String generate(int[] validStartDigits) {
        int randomStartDigit = Rand.randomRange(0, validStartDigits.length - 1);
        return generate(validStartDigits[randomStartDigit]);
    }

}
