package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Utils;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class Rand {
    private static final Random r = new Random();

    /**
     * Default Special characters that cannot be changed
     */
    private static final String SPECIAL = "`~!@#$%^&*()_+-=,./;'[]<>?:{}|\"\\";

    /**
     * All valid ASCII letters that cannot be changed
     */
    private static final String ASCII_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * All valid ASCII numbers that cannot be changed
     */
    private static final String ASCII_NUMBERS = "0123456789";

    /**
     * The default extended letters that cannot be changed<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) All non-ASCII letters that are to be supported should be assigned here<BR>
     * 2) It is necessary to use unicode code points to avoid encoding issues<BR>
     */
    private static final String EXTENDED_LETTERS = Accents.getFrench();

    /**
     * The default extended numbers that cannot be changed<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) All non-ASCII numbers that are to be supported should be assigned here<BR>
     * 2) It is necessary to use unicode code points to avoid encoding issues<BR>
     */
    private static final String EXTENDED_NUMBERS = "";

    /**
     * All the extended letters that cannot be changed that are used to support additional character sets
     */
    private static final String ALL_LETTERS = ASCII_LETTERS + EXTENDED_LETTERS;

    /**
     * All the extended numbers that cannot be changed that are used to support additional character sets
     */
    private static final String ALL_NUMBERS = ASCII_NUMBERS + EXTENDED_NUMBERS;

    private Rand() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Get Instance for use with JEXL Expressions
     *
     * @return Rand
     */
    public static Rand getInstance() {
        return new Rand();
    }

    /**
     * Gets the default special characters.
     *
     * @return SPECIAL
     */
    public static String getSpecialDefaults() {
        return SPECIAL;
    }

    /**
     * Gets the default letters used in specific methods to support additional character sets
     *
     * @return ALL_LETTERS
     */
    public static String getLettersDefaults() {
        return ALL_LETTERS;
    }

    /**
     * Gets the default numbers used in specific methods to support additional character sets
     *
     * @return ALL_NUMBERS
     */
    public static String getNumbersDefaults() {
        return ALL_NUMBERS;
    }

    /**
     * Gets only the extended default letters
     *
     * @return EXTENDED_LETTERS
     */
    public static String getOnlyExtendedLetters() {
        return EXTENDED_LETTERS;
    }

    /**
     * Gets only the extended default numbers
     *
     * @return EXTENDED_NUMBERS
     */
    public static String getOnlyExtendedNumbers() {
        return EXTENDED_NUMBERS;
    }

    /**
     * Returns a number in the specified range.<BR>
     * <BR>
     * <B>Inclusive Examples:</B><BR>
     * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
     * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
     * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4, 5}<BR>
     * <BR>
     * <B>Exclusive Examples:</B><BR>
     * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
     * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
     * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4}<BR>
     *
     * @param nMin      - Minimum value
     * @param nMax      - Maximum value
     * @param inclusive - true for max value to be inclusive, false for max value to be exclusive
     * @return int
     */
    private static int randomRange(int nMin, int nMax, boolean inclusive) {
        int nOffset;
        int nUseRangeMin;
        int nUseRangeMax;
        int nRange;

        // Set the default values for the range
        nUseRangeMin = nMin;
        nUseRangeMax = nMax;

        // Get a valid range
        if (nUseRangeMin > nUseRangeMax) {
            int nTemp = nUseRangeMin;
            nUseRangeMin = nUseRangeMax;
            nUseRangeMax = nTemp;
        }

        /*
         * Need to shift the range to be 0 to Max for random number generation. The offset will be used after
         * to random number in range.
         */
        nOffset = nUseRangeMin;

        // Need to ensure range is positive
        if (nUseRangeMin < 0) {
            nUseRangeMin += -1 * nOffset;
            nUseRangeMax += -1 * nOffset;
        }

        if (inclusive)
            nRange = nOffset + r.nextInt(nUseRangeMax - nUseRangeMin + 1);
        else
            nRange = nOffset + r.nextInt(nUseRangeMax - nUseRangeMin);

        return nRange;
    }

    /**
     * Returns a number in the specified range.<BR>
     * <BR>
     * <B>Examples:</B><BR>
     * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
     * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}<BR>
     * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4, 5}<BR>
     *
     * @param nMin - Minimum value (inclusive)
     * @param nMax - Maximum value (inclusive)
     * @return int
     */
    public static int randomRange(int nMin, int nMax) {
        return randomRange(nMin, nMax, true);
    }

    /**
     * Returns a number in the specified range.<BR>
     * <BR>
     * <B>Examples:</B><BR>
     * 1) randomizeRangeToken(1,12) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
     * 2) randomizeRangeToken(12,1) can return {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}<BR>
     * 3) randomizeRangeToken(-5,5) can return {-5, -4, -3, -2, -1, 0, 2, 3, 4}<BR>
     *
     * @param nMin - Minimum value (inclusive)
     * @param nMax - Maximum value (exclusive)
     * @return int
     */
    public static int randomRangeIndex(int nMin, int nMax) {
        return randomRange(nMin, nMax, false);
    }

    /**
     * Return integer in specified range excluding specified values
     *
     * @param retries  - Number of retries to generate the random value in the range and not any of the excluded values
     * @param min      - Minimum value (inclusive)
     * @param max      - Maximum value (inclusive)
     * @param excludes - Excluded values
     * @return int
     */
    public static int randomRange(int retries, int min, int max, int... excludes) {
        try {
            return Failsafe.with(Utils.getRetryPolicy(retries)).get(() -> randomRangeChecked(min, max, excludes));
        } catch (Exception | AssertionError e) {
            String reason = "Could NOT find random value in [" + min + "," + max + "] and not in ";
            reason += Arrays.toString(excludes) + " after " + retries + " retries";
            assertThat(reason, false);
            return -1;
        }
    }

    /**
     * Return integer in specified range excluding specified values<BR>
     * <B>Note: </B> If the random number matches any of the excluded values, then assertion failure occurs
     *
     * @param min      - Minimum value (inclusive)
     * @param max      - Maximum value (inclusive)
     * @param excludes - Excluded values
     * @return int
     */
    private static int randomRangeChecked(int min, int max, int... excludes) {
        int random = randomRange(min, max);
        Arrays.stream(excludes).forEach(item -> assertThat("Excluded Value", random, not(equalTo(item))));
        return random;
    }

    /**
     * Gets all enumeration values except excluded values
     *
     * @param e       - Any Enumeration value
     * @param exclude - enumeration values to be excluded from values returned
     * @return enumeration values minus exclusions
     */
    private static Enum<?>[] getValues(Enum<?> e, Enum<?>... exclude) {
        Enum<?>[] options = e.getDeclaringClass().getEnumConstants();
        for (Enum<?> item : exclude) {
            options = ArrayUtils.removeElement(options, item);
        }

        return options;
    }

    /**
     * Returns either true or false based on a random number<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Max rand value is 10000<BR>
     *
     * @return true if (rand % 2 == 0) else false
     */
    public static boolean randomBoolean() {
        return randomBoolean(10000);
    }

    /**
     * Returns either true or false based on a random number<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) The higher the max random number, the better standard deviation of the results should be<BR>
     *
     * @param nRange - Max Random number to determine true/false
     * @return true if (rand % 2 == 0) else false
     */
    public static boolean randomBoolean(int nRange) {
        return randomRange(0, nRange) % 2 == 0;
    }

    /**
     * Gets a random enumeration option<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Range Multiplier is 10000<BR>
     *
     * @param e - Any Enumeration value
     * @return enumeration value
     */
    @SuppressWarnings("squid:S1452")
    public static Enum<?> randomEnum(Enum<?> e) {
        return randomEnum(e, 10000);
    }

    /**
     * Gets a random enumeration option<BR>
     * <BR>
     * <B>Example: </B><BR>
     * Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000);<BR>
     *
     * @param e                - Any Enumeration value
     * @param nRangeMultiplier - Increases the range of values before getting the remainder when divided by
     *                         number of enumeration values for the enumeration
     * @return enumeration value
     */
    @SuppressWarnings("squid:S1452")
    public static Enum<?> randomEnum(Enum<?> e, int nRangeMultiplier) {
        Enum<?>[] options = e.getDeclaringClass().getEnumConstants();
        int nSize = options.length;
        int nRandom = randomRange(0, nRangeMultiplier * nSize) % nSize;
        return options[nRandom];
    }

    /**
     * Gets a random enumeration option<BR>
     * <BR>
     * <B>Example: </B><BR>
     * Languages randomLang = (Languages) Rand.randomEnum(Languages.English, Languages.KEY);<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If all enumeration values are excluded, then an ArithmeticException will occur due to division by
     * zero<BR>
     * 2) Range Multiplier is 10000<BR>
     *
     * @param e       - Any Enumeration value
     * @param exclude - enumeration values to be excluded from possible selection
     * @return enumeration value
     * @throws ArithmeticException if all enumeration values are excluded
     */
    @SuppressWarnings("squid:S1452")
    public static Enum<?> randomEnum(Enum<?> e, Enum<?>... exclude) {
        return randomEnum(e, 10000, exclude);
    }

    /**
     * Gets a random enumeration option<BR>
     * <BR>
     * <B>Example: </B><BR>
     * Languages randomLang = (Languages) Rand.randomEnum(Languages.English, 10000, Languages.KEY);<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If all enumeration values are excluded, then an ArithmeticException will occur due to division by
     * zero<BR>
     *
     * @param e                - Any Enumeration value
     * @param nRangeMultiplier - Increases the range of values before getting the remainder when divided by
     *                         number of enumeration values for the enumeration
     * @param exclude          - enumeration values to be excluded from possible selection
     * @return enumeration value
     * @throws ArithmeticException if all enumeration values are excluded
     */
    @SuppressWarnings("squid:S1452")
    public static Enum<?> randomEnum(Enum<?> e, int nRangeMultiplier, Enum<?>... exclude) {
        Enum<?>[] options = getValues(e, exclude);
        int nSize = options.length;
        int nRandom = randomRange(0, nRangeMultiplier * nSize) % nSize;
        return options[nRandom];
    }

    /**
     * Returns a random string of the specified size with only alphabetic characters<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Wraps the RandomStringUtils.randomAlphabetic<BR>
     *
     * @param nLength - Size of String to return
     * @return Random String of only Alphabetic characters that is of specified length
     */
    public static String letters(int nLength) {
        return RandomStringUtils.randomAlphabetic(nLength);
    }

    /**
     * Returns a random string that only consists of numbers and starts with a non-zero number
     *
     * @param nLength - Size of String to return
     * @return String that begins with non-zero number and is only numbers of specified size
     */
    public static String numbers(int nLength) {
        String nonZero = String.valueOf(randomRange(1, 9));
        String rest = "";
        if (nLength - 1 > 0)
            rest = RandomStringUtils.randomNumeric(nLength - 1);

        return nonZero + rest;
    }

    /**
     * Returns an alphanumeric string that starts with a letter of the specified size
     *
     * @param nLength - Size of String to return
     * @return String that begins with a letter and only consists of alphanumeric characters of specified
     * length
     */
    public static String alphanumeric(int nLength) {
        String nonNumber = RandomStringUtils.randomAlphabetic(1);
        String rest = "";
        if (nLength - 1 > 0)
            rest = RandomStringUtils.randomAlphanumeric(nLength - 1);

        return nonNumber + rest;
    }

    /**
     * Returns a random string in the specified range size with only alphabetic characters<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Minimum &amp; Maximum values need to be greater than zero<BR>
     * 2) The range is not affected if Minimum is greater than Maximum<BR>
     * <BR>
     * <B>Examples:</B><BR>
     * 1) letters(3, 5) could return an alphabetic string of size 3, 4 or 5 such as "abCd"<BR>
     * 2) letters(5, 3) could return an alphabetic string of size 3, 4 or 5 such as "Zap"<BR>
     *
     * @param nMin - Minimum value (inclusive)
     * @param nMax - Maximum value (inclusive)
     * @return Random String of only Alphabetic characters that is in the specified range size
     */
    public static String letters(int nMin, int nMax) {
        int nLength = randomRange(nMin, nMax);
        return letters(nLength);
    }

    /**
     * Returns a random string that only consists of numbers and starts with a non-zero number<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Minimum &amp; Maximum values need to be greater than zero<BR>
     * 2) The range is not affected if Minimum is greater than Maximum<BR>
     * <BR>
     * <B>Examples:</B><BR>
     * 1) numbers(3, 5) could return a numbers string starting with non-zero number of size 3, 4 or 5 such as
     * "159"<BR>
     * 2) numbers(5, 3) could return a numbers string starting with non-zero number of size 3, 4 or 5 such as
     * "9510"<BR>
     *
     * @param nMin - Minimum value (inclusive)
     * @param nMax - Maximum value (inclusive)
     * @return String that begins with non-zero number and is only numbers in the specified range size
     */
    public static String numbers(int nMin, int nMax) {
        int nLength = randomRange(nMin, nMax);
        return numbers(nLength);
    }

    /**
     * Returns an alphanumeric string that starts with a letter in the specified range size<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Minimum &amp; Maximum values need to be greater than zero<BR>
     * 2) The range is not affected if Minimum is greater than Maximum<BR>
     * <BR>
     * <B>Examples:</B><BR>
     * 1) alphanumeric(3, 5) could return an alphanumeric string starting with a letter of size 3, 4 or 5 such
     * as "A159"<BR>
     * 2) alphanumeric(5, 3) could return an alphanumeric string starting with a letter of size 3, 4 or 5 such
     * as "Z9145"<BR>
     *
     * @param nMin - Minimum value (inclusive)
     * @param nMax - Maximum value (inclusive)
     * @return String that begins with a letter and only consists of alphanumeric characters in the specified
     * range size
     */
    public static String alphanumeric(int nMin, int nMax) {
        int nLength = randomRange(nMin, nMax);
        return alphanumeric(nLength);
    }

    /**
     * Returns a random string of the specified size with only characters using the specified characters
     *
     * @param nLength - Size of String to return
     * @param chars   - Characters that can be selected
     * @return Random String of only specified characters that is of specified length
     */
    public static String onlyChars(int nLength, String chars) {
        return RandomStringUtils.random(nLength, chars);
    }

}
