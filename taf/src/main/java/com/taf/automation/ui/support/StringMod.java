package com.taf.automation.ui.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to do multiple actions on a string which can be strung together that is more convenient/easier to
 * read/maintain (using Fluent Pattern.)
 */
public class StringMod {
    /**
     * The current/working value
     */
    private String working;

    /**
     * Constructor that initializes the working string to be the empty string
     */
    public StringMod() {
        this("");
    }

    /**
     * Constructor that initializes the working string to the the specified value<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) A null value is converted to the empty string<BR>
     *
     * @param value - Working string to start with
     */
    public StringMod(String value) {
        working = StringUtils.defaultString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        return StringUtils.equals(working, obj.toString());
    }

    /**
     * Compares this StringMod to another StringMod, ignoring case considerations. Two StringMod are
     * considered equal ignoring case if they are of the same length and corresponding characters in the two
     * stored strings are equal ignoring case.<BR>
     *
     * @param obj - the reference object with which to compare
     * @return true if the argument is not null and it represents an equivalent object ignoring case; false
     * otherwise
     */
    public boolean equalsIgnoreCase(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        return StringUtils.equalsIgnoreCase(working, obj.toString());
    }

    @Override
    public int hashCode() {
        List<String> excludeFields = new ArrayList<>();
        return HashCodeBuilder.reflectionHashCode(this, excludeFields);
    }

    @Override
    public String toString() {
        return working;
    }

    /**
     * Get the current working string value (same as toString method)
     *
     * @return String
     */
    public String get() {
        return toString();
    }

    /**
     * Appends the specified value to the working string<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) A null value is converted to the empty string<BR>
     *
     * @param value - Value to be appended to the working string
     * @return StringMod
     */
    public StringMod append(String value) {
        working += StringUtils.defaultString(value);
        return this;
    }

    /**
     * Prepends the specified value to the working string<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) A null value is converted to the empty string<BR>
     *
     * @param value - Value to be prepended to the working string
     * @return StringMod
     */
    public StringMod prepend(String value) {
        working = StringUtils.defaultString(value) + working;
        return this;
    }

    /**
     * Remove all characters matching the regular expression
     *
     * @param regex - the regular expression to which this string is to be matched
     * @return StringMod
     */
    public StringMod removeAll(String regex) {
        return replaceAll(regex, "");
    }

    /**
     * Replace all characters matching the regular expression<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If regular expression is invalid, then nothing will be removed<BR>
     *
     * @param regex       - the regular expression to which this string is to be matched
     * @param replacement - the string to be substituted for each match
     * @return StringMod
     */
    public StringMod replaceAll(String regex, String replacement) {
        try {
            working = working.replaceAll(regex, replacement);
        } catch (Exception ex) {
            //
        }

        return this;
    }

    /**
     * Remove non-digits
     *
     * @return StringMod
     */
    public StringMod removeNonDigits() {
        return removeAll("\\D");
    }

    /**
     * Remove non-letters (ASCII alphabet)
     *
     * @return StringMod
     */
    public StringMod removeNonLetters() {
        return removeAll("[^A-Za-z]");
    }

    /**
     * Remove non-digits and non-letters (ASCII alphabet)
     *
     * @return StringMod
     */
    public StringMod removeNonAlphanumeric() {
        return removeAll("[^A-Za-z0-9]");
    }

    /**
     * Insert the value at the specified offset<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If offset is invalid, then no string is inserted<BR>
     * 2) Offset needs to be greater than or equal to 0 and less than or equal to the length of the currently
     * stored string<BR>
     *
     * @param offset - Offset to insert string at
     * @param value  - Value to be inserted
     * @return StringMod
     */
    public StringMod insert(int offset, String value) {
        StringBuilder sb = new StringBuilder(working);

        try {
            sb.insert(offset, value);
        } catch (Exception ex) {
            //
        }

        working = sb.toString();
        return this;
    }

    /**
     * Replaces each substring of this string that matches the literal target sequence with the specified
     * literal replacement sequence. The replacement proceeds from the beginning of the string to the end,
     * for example, replacing "aa" with "b" in the string "aaa" will result in "ba" rather than "ab".<BR>
     *
     * @param target      - String to find
     * @param replacement - the string to be substituted for each match
     * @return StringMod
     */
    public StringMod replace(String target, String replacement) {
        working = working.replace(StringUtils.defaultString(target), StringUtils.defaultString(replacement));
        return this;
    }

    /**
     * Removes each substring of this string that matches the literal target sequence with the empty string<BR>
     *
     * @param target - String to find and replace with the empty string
     * @return StringMod
     */
    public StringMod remove(String target) {
        return replace(target, "");
    }

    /**
     * Replace 1st match using the regular expression<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If regular expression is invalid, then nothing will be replaced<BR>
     *
     * @param regex       - the regular expression to which this string is to be matched
     * @param replacement - the string to be substituted
     * @return StringMod
     */
    public StringMod replaceFirstRegEx(String regex, String replacement) {
        try {
            working = working.replaceFirst(regex, replacement);
        } catch (Exception ex) {
            //
        }

        return this;
    }

    /**
     * Remove 1st match using the regular expression<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If regular expression is invalid, then nothing will be replaced<BR>
     *
     * @param regex - the regular expression to which this string is to be matched
     * @return StringMod
     */
    public StringMod removeFirstRegEx(String regex) {
        return replaceFirstRegEx(regex, "");
    }

    /**
     * Replaces the first occurrence of the specified string
     *
     * @param first       - First occurrence of string to replace
     * @param replacement - Replacement string
     * @return StringMod
     */
    public StringMod replaceFirst(String first, String replacement) {
        return replaceFirst(first, replacement, false);
    }

    /**
     * Replaces the first occurrence of the specified string
     *
     * @param first           - First occurrence of string to replace
     * @param replacement     - Replacement string
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod replaceFirst(String first, String replacement, boolean caseInsensitive) {
        if (first == null || first.equals("")) {
            return this;
        }

        StringMod tempStored = new StringMod(working);
        if (caseInsensitive) {
            tempStored.toLowerCase();
        }

        StringMod tempFirst = new StringMod(first);
        if (caseInsensitive) {
            tempFirst.toLowerCase();
        }

        int firstIndex = tempStored.get().indexOf(tempFirst.get());
        if (firstIndex == 0) {
            if (working.length() == first.length()) {
                working = replacement;
            } else {
                working = replacement + working.substring(first.length());
            }
        } else if (firstIndex > 0) {
            // The part of the string before the first occurrence
            String part1 = working.substring(0, firstIndex);

            // Does the string end with the first occurrence?
            if (firstIndex + first.length() >= working.length()) {
                working = part1 + replacement;
            } else {
                working = part1 + replacement + working.substring(firstIndex + first.length());
            }
        }

        return this;
    }

    /**
     * Removes the first occurrence of the specified string
     *
     * @param first - First occurrence of string to remove
     * @return StringMod
     */
    public StringMod removeFirst(String first) {
        return replaceFirst(first, "");
    }

    /**
     * Removes the first occurrence of the specified string
     *
     * @param first           - First occurrence of string to remove
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod removeFirst(String first, boolean caseInsensitive) {
        return replaceFirst(first, "", caseInsensitive);
    }

    /**
     * Replaces the last occurrence of the specified string
     *
     * @param last        - Last occurrence of string to replace
     * @param replacement - Replacement string
     * @return StringMod
     */
    public StringMod replaceLast(String last, String replacement) {
        return replaceLast(last, replacement, false);
    }

    /**
     * Replaces the last occurrence of the specified string
     *
     * @param last            - Last occurrence of string to replace
     * @param replacement     - Replacement string
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod replaceLast(String last, String replacement, boolean caseInsensitive) {
        if (last == null || last.equals("")) {
            return this;
        }

        StringMod tempStored = new StringMod(working);
        if (caseInsensitive) {
            tempStored.toLowerCase();
        }

        StringMod tempLast = new StringMod(last);
        if (caseInsensitive) {
            tempLast.toLowerCase();
        }

        int lastIndex = tempStored.get().lastIndexOf(tempLast.get());
        if (lastIndex == 0) {
            if (working.length() == last.length()) {
                working = StringUtils.defaultString(replacement);
            } else {
                working = StringUtils.defaultString(replacement) + working.substring(last.length());
            }
        } else if (lastIndex > 0) {
            // The part of the string before the last occurrence
            String part1 = working.substring(0, lastIndex);

            // Does the string end with the last occurrence?
            if (lastIndex + last.length() >= working.length()) {
                working = part1 + StringUtils.defaultString(replacement);
            } else {
                working = part1 + StringUtils.defaultString(replacement) + working.substring(lastIndex + last.length());
            }
        }

        return this;
    }

    /**
     * Removes the last occurrence of the specified string
     *
     * @param last - Last occurrence of string to remove
     * @return StringMod
     */
    public StringMod removeLast(String last) {
        return removeLast(last, false);
    }

    /**
     * Removes the last occurrence of the specified string
     *
     * @param last            - Last occurrence of string to remove
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod removeLast(String last, boolean caseInsensitive) {
        return replaceLast(last, "", caseInsensitive);
    }

    /**
     * Converts all of the characters in this String to lower case using the rules of the default locale
     *
     * @return StringMod
     */
    public StringMod toLowerCase() {
        working = working.toLowerCase();
        return this;
    }

    /**
     * Converts all of the characters in this String to upper case using the rules of the default locale
     *
     * @return StringMod
     */
    public StringMod toUpperCase() {
        working = working.toUpperCase();
        return this;
    }

    /**
     * Removes specified string from the end of the stored string and replaced with specified string
     *
     * @param ending      - String to be replaced from end of the stored string
     * @param replacement - the replacement string to be used
     * @return StringMod
     */
    public StringMod replaceEndsWith(String ending, String replacement) {
        return replaceEndsWith(ending, replacement, false);
    }

    /**
     * Removes specified string from the end of the stored string
     *
     * @param ending - String to be removed from end of the stored string
     * @return StringMod
     */
    public StringMod removeEndsWith(String ending) {
        return removeEndsWith(ending, false);
    }

    /**
     * Removes specified string from the end of the stored string and replaced with specified string
     *
     * @param ending          - String to be replaced from end of the stored string
     * @param replacement     - the replacement string to be used
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod replaceEndsWith(String ending, String replacement, boolean caseInsensitive) {
        StringMod tempStored = new StringMod(working);
        if (caseInsensitive) {
            tempStored.toLowerCase();
        }

        StringMod tempEnding = new StringMod(ending);
        if (caseInsensitive) {
            tempEnding.toLowerCase();
        }

        if (ending != null && tempStored.get().endsWith(tempEnding.get())) {
            working = working.substring(0, working.length() - ending.length()) + StringUtils.defaultString(replacement);
        }

        return this;
    }

    /**
     * Removes specified string from the end of the stored string
     *
     * @param ending          - String to be removed from end of the stored string
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod removeEndsWith(String ending, boolean caseInsensitive) {
        return replaceEndsWith(ending, "", caseInsensitive);
    }

    /**
     * Removes specified string from the start of the stored string and replaced with specified string<BR>
     *
     * @param starting    - String to be replaced from start of the stored string
     * @param replacement - the replacement string to be used
     * @return StringMod
     */
    public StringMod replaceStartsWith(String starting, String replacement) {
        return replaceStartsWith(starting, replacement, false);
    }

    /**
     * Removes specified string from the start of the stored string and replaced with specified string
     *
     * @param starting        - String to be replaced from start of the stored string
     * @param replacement     - the replacement string to be used
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod replaceStartsWith(String starting, String replacement, boolean caseInsensitive) {
        StringMod tempStored = new StringMod(working);
        if (caseInsensitive) {
            tempStored.toLowerCase();
        }

        StringMod tempEnding = new StringMod(starting);
        if (caseInsensitive) {
            tempEnding.toLowerCase();
        }

        if (starting != null && tempStored.get().startsWith(tempEnding.get())) {
            try {
                working = StringUtils.defaultString(replacement) + working.substring(starting.length());
            } catch (Exception ex) {
                //
            }
        }

        return this;
    }

    /**
     * Removes specified string from the start of the stored string<BR>
     *
     * @param starting - String to be removed from start of the stored string
     * @return StringMod
     */
    public StringMod removeStartsWith(String starting) {
        return removeStartsWith(starting, false);
    }

    /**
     * Removes specified string from the start of the stored string
     *
     * @param starting        - String to be removed from start of the stored string
     * @param caseInsensitive - true to ignore case in the operation, else operation is case sensitive
     * @return StringMod
     */
    public StringMod removeStartsWith(String starting, boolean caseInsensitive) {
        return replaceStartsWith(starting, "", caseInsensitive);
    }

    /**
     * Returns a string that is a substring of this string. The substring begins with the character at the
     * specified index and extends to the end of this string.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If beginIndex is invalid, then stored string is not modified<BR>
     *
     * @param beginIndex - the beginning index, inclusive.
     * @return StringMod
     */
    public StringMod substring(int beginIndex) {
        try {
            working = working.substring(beginIndex);
        } catch (Exception ex) {
            //
        }

        return this;
    }

    /**
     * Returns a string that is a substring of this string. The substring begins at the specified beginIndex
     * and extends to the character at index endIndex - 1. Thus the length of the substring is
     * endIndex-beginIndex. <BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If beginIndex and/or endIndex is invalid, then stored string is not modified<BR>
     *
     * @param beginIndex - the beginning index, inclusive.
     * @param endIndex   - the ending index, exclusive.
     * @return StringMod
     */
    public StringMod substring(int beginIndex, int endIndex) {
        try {
            working = working.substring(beginIndex, endIndex);
        } catch (Exception ex) {
            //
        }

        return this;
    }

    /**
     * Trim leading and trailing whitespace<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This is the standard Java trim functionality<BR>
     *
     * @return StringMod
     */
    public StringMod trim() {
        working = working.trim();
        return this;
    }

    /**
     * Trims whitespace (non-visible text) from beginning and end of the string
     *
     * @return StringMod
     */
    public StringMod trimNonVisible() {
        return removeAll("^[\u0000-\u0020\u007F-\u00A0]+|[\u0000-\u0020\u007F-\u00A0]+$");
    }

    /**
     * Remove all Invisible Control characters<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Regular expression is from following page: http://www.regular-expressions.info/unicode.html<BR>
     *
     * @return StringMod
     */
    public StringMod removeInvisibleControl() {
        return replaceInvisibleControl("");
    }

    /**
     * Replace all Invisible Control characters with specified value<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Regular expression is from following page: http://www.regular-expressions.info/unicode.html<BR>
     *
     * @param replacement - the string to be substituted for each match
     * @return StringMod
     */
    public StringMod replaceInvisibleControl(String replacement) {
        return replaceAll("\\p{C}", replacement);
    }

    /**
     * Combines all methods that trim<BR>
     * <BR>
     * <B>Methods executed:</B>
     * <ol>
     * <li>trim()</li>
     * <li>trimNonInvisible()</li>
     * <li>removeInvisibleControl()</li>
     * </ol>
     *
     * @return StringMod
     */
    public StringMod trimAll() {
        trim();
        trimNonVisible();
        removeInvisibleControl();
        return this;
    }

    /**
     * Splits this string around matches of the given regular expression<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If regular expression is invalid, then the stored string will not change<BR>
     * 2) If index is invalid (based on the array created from splitting using the regular expression), then
     * the stored string will not change<BR>
     *
     * @param regex - the delimiting regular expression
     * @param index - index of array to keep after splitting using the regular expression
     * @return StringMod
     */
    public StringMod split(String regex, int index) {
        try {
            String[] pieces = working.split(regex);
            working = pieces[index];
        } catch (Exception ex) {
            //
        }

        return this;
    }

}
