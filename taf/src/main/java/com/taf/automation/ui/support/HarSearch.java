package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Utils;
import net.jodah.failsafe.Failsafe;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

/**
 * This class is to simplify searching the Har logs
 */
public class HarSearch {
    private int startIndex;
    private int stopIndex;

    public HarSearch() {
        startIndex = 0;
        stopIndex = -1;
    }

    /**
     * Set the starting search index which is inclusive to be the current size of entries
     */
    public void setStartingSearchIndex() {
        Har har = TestProperties.getInstance().getHarForThread();
        assertThat("Har Object", har, notNullValue());

        HarLog log = har.getLog();
        assertThat("Har Log", log, notNullValue());

        setStartingSearchIndex(log.getEntries().size());
    }

    /**
     * Set the start search index which is inclusive
     *
     * @param index - Index
     */
    public void setStartingSearchIndex(int index) {
        startIndex = index;
    }

    /**
     * Set the stop search index which is exclusive
     *
     * @param index - Index (use less than 1 to search all entries)
     */
    public void setStopSearchIndex(int index) {
        stopIndex = index;
    }

    /**
     * Find 1st Entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param containsURL - Entry's Request URL must contain this URL
     * @return less than 0 if no match else index of matching entry
     */
    public int findEntry(String containsURL) {
        return findEntry(containsURL, null);
    }

    /**
     * Find 1st Entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return less than 0 if no match else index of matching entry
     */
    public int findEntry(String containsURL, String equalsMethod) {
        return findEntry(containsURL, null, equalsMethod);
    }

    /**
     * Find 1st Entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param regex - Entry's Request URL must match this regular expression
     * @return less than 0 if no match else index of matching entry
     */
    public int findEntryByRegEx(String regex) {
        return findEntryByRegEx(regex, null);
    }

    /**
     * Find 1st Entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return less than 0 if no match else index of matching entry
     */
    public int findEntryByRegEx(String regex, String equalsMethod) {
        return findEntry(null, regex, equalsMethod);
    }

    /**
     * Find 1st Entry that matches criteria<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>The start and stop index are applied</LI>
     * <LI><B>regex</B> only used if value is not null & <B>containsURL</B> is null</LI>
     * </OL>
     *
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return less than 0 if no match else index of matching entry
     */
    private int findEntry(String containsURL, String regex, String equalsMethod) {
        Har har = TestProperties.getInstance().getHarForThread();
        if (har == null) {
            return -2;
        }

        HarLog log = har.getLog();
        if (log == null) {
            return -3;
        }

        int stopEntry = (stopIndex > 0) ? stopIndex : log.getEntries().size();
        for (int i = startIndex; i < stopEntry; i++) {
            HarEntry entry = log.getEntries().get(i);
            if (isMatch(entry, containsURL, regex, equalsMethod)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Check if the entry matches the criteria<BR>
     * <B>Note: </B> <B>regex</B> only used if value is not null & <B>containsURL</B> is null<BR>
     *
     * @param entry        - Entry to check
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return true if match else false
     */
    private boolean isMatch(HarEntry entry, String containsURL, String regex, String equalsMethod) {
        if (entry == null || entry.getRequest() == null) {
            return false;
        }

        boolean isURL;
        if (containsURL == null && regex != null) {
            isURL = StringUtils.defaultString(entry.getRequest().getUrl()).matches(regex);
        } else {
            isURL = StringUtils.contains(entry.getRequest().getUrl(), containsURL);
        }

        boolean isMethod;
        if (equalsMethod == null) {
            isMethod = true;
        } else {
            isMethod = StringUtils.equals(equalsMethod, entry.getRequest().getMethod());
        }

        boolean[] conditions = new boolean[]{isURL, isMethod};
        return BooleanUtils.and(conditions);
    }

    /**
     * Find All Entries that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param containsURL - Entry's Request URL must contain this URL
     * @return list of matching indexes (empty if no matches)
     */
    public List<Integer> findEntries(String containsURL) {
        return findEntries(containsURL, null);
    }

    /**
     * Find All Entries that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return list of matching indexes (empty if no matches)
     */
    public List<Integer> findEntries(String containsURL, String equalsMethod) {
        return findEntries(containsURL, null, equalsMethod);
    }

    /**
     * Find All Entries that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param regex - Entry's Request URL must match this regular expression
     * @return list of matching indexes (empty if no matches)
     */
    public List<Integer> findEntriesByRegEx(String regex) {
        return findEntriesByRegEx(regex, null);
    }

    /**
     * Find All Entries that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return list of matching indexes (empty if no matches)
     */
    public List<Integer> findEntriesByRegEx(String regex, String equalsMethod) {
        return findEntries(null, regex, equalsMethod);
    }

    /**
     * Find All Entries that matches criteria<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>The start and stop index are applied</LI>
     * <LI><B>regex</B> only used if value is not null & <B>containsURL</B> is null</LI>
     * </OL>
     *
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return list of matching indexes (empty if no matches)
     */
    private List<Integer> findEntries(String containsURL, String regex, String equalsMethod) {
        List<Integer> matches = new ArrayList<>();

        Har har = TestProperties.getInstance().getHarForThread();
        if (har == null) {
            return matches;
        }

        HarLog log = har.getLog();
        if (log == null) {
            return matches;
        }

        int stopEntry = (stopIndex > 0) ? stopIndex : log.getEntries().size();
        for (int i = startIndex; i < stopEntry; i++) {
            HarEntry entry = log.getEntries().get(i);
            if (isMatch(entry, containsURL, regex, equalsMethod)) {
                matches.add(i);
            }
        }

        return matches;
    }

    /**
     * Wait for an entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param containsURL - Entry's Request URL must contain this URL
     * @return index of matching entry
     */
    public int waitForEntry(String containsURL) {
        return waitForEntry(containsURL, null);
    }

    /**
     * Wait for an entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return index of matching entry
     */
    public int waitForEntry(String containsURL, String equalsMethod) {
        return waitForEntry(containsURL, null, equalsMethod);
    }

    /**
     * Wait for an entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param regex - Entry's Request URL must match this regular expression
     * @return index of matching entry
     */
    public int waitForEntryByRegEx(String regex) {
        return waitForEntryByRegEx(regex, null);
    }

    /**
     * Wait for an entry that matches criteria<BR>
     * <B>Notes: </B> The start and stop index are applied<BR>
     *
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return index of matching entry
     */
    public int waitForEntryByRegEx(String regex, String equalsMethod) {
        return waitForEntry(null, regex, equalsMethod);
    }

    /**
     * Wait for an entry that matches criteria<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>The start and stop index are applied</LI>
     * <LI><B>regex</B> only used if value is not null & <B>containsURL</B> is null</LI>
     * </OL>
     *
     * @param containsURL  - Entry's Request URL must contain this URL
     * @param regex        - Entry's Request URL must match this regular expression
     * @param equalsMethod - Entry's Request Method must equal this (or null if ignore method)
     * @return index of matching entry
     */
    private int waitForEntry(String containsURL, String regex, String equalsMethod) {
        return Failsafe.with(Utils.getPollingRetryPolicy()).get(() -> findEntryThatMustExist(containsURL, regex, equalsMethod));
    }

    private int findEntryThatMustExist(String containsURL, String regex, String equalsMethod) {
        int index = findEntry(containsURL, regex, equalsMethod);
        assertThat("Could not find any matching entry", index, greaterThanOrEqualTo(0));
        return index;
    }

    /**
     * Get Entry
     *
     * @param index - Index
     * @return Entry at specified index
     */
    public HarEntry getEntry(int index) {
        Har har = TestProperties.getInstance().getHarForThread();
        assertThat("Har Object", har, notNullValue());

        HarLog log = har.getLog();
        assertThat("Har Log", log, notNullValue());
        assertThat("Har Entries", log.getEntries(), notNullValue());
        assertThat("Index", index, greaterThanOrEqualTo(0));
        assertThat("Index", index, lessThan(log.getEntries().size()));

        return log.getEntries().get(index);
    }

    /**
     * Get All Entries
     *
     * @param entries - The index of all entries to get
     * @return List of entries
     */
    public List<HarEntry> getEntries(int... entries) {
        List<HarEntry> all = new ArrayList<>();

        if (entries == null || entries.length == 0) {
            return all;
        }

        int minIndex = NumberUtils.min(entries);
        int maxIndex = NumberUtils.max(entries);

        Har har = TestProperties.getInstance().getHarForThread();
        assertThat("Har Object", har, notNullValue());

        HarLog log = har.getLog();
        assertThat("Har Log", log, notNullValue());
        assertThat("Har Entries", log.getEntries(), notNullValue());
        assertThat("Min Index", minIndex, greaterThanOrEqualTo(0));
        assertThat("Max Index", maxIndex, lessThan(log.getEntries().size()));

        for (int index : entries) {
            all.add(log.getEntries().get(index));
        }

        return all;
    }

}
