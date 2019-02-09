package com.taf.automation.ui.support;

import org.apache.commons.lang3.BooleanUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Class to accumulate assert failures to throw a single assertion later.<BR>
 * <BR>
 * The main use case is performing multiple assertions on an object in which you want all assertions executed always.
 */
public class AssertAggregator {
    /**
     * Flag to indicate if to write failures to console as well
     */
    private boolean console;

    /**
     * Store the assertion failures
     */
    private List<AssertionError> assertionFailures;

    /**
     * Store the count of asserts that were successful
     */
    private int assertionSuccesses;

    public AssertAggregator() {
        assertionFailures = new ArrayList<>();
        assertionSuccesses = 0;
        console = false;
    }

    /**
     * Reset the assertion failures and successes
     */
    public void reset() {
        assertionFailures = new ArrayList<>();
        assertionSuccesses = 0;
    }

    /**
     * Set console flag
     *
     * @param console - true to write failures to the console, false to only write to report
     */
    public void setConsole(boolean console) {
        this.console = console;
    }

    /**
     * Get the console flag
     *
     * @return boolean
     */
    public boolean getConsole() {
        return console;
    }

    /**
     * Get the count of successful assertions
     *
     * @return int
     */
    public int getSuccessCount() {
        return assertionSuccesses;
    }

    /**
     * Get the count of assertions that failed
     *
     * @return int
     */
    public int getFailureCount() {
        return assertionFailures.size();
    }

    /**
     * Get assertion failures
     *
     * @return List&lt;AssertionError&gt;
     */
    public List<AssertionError> getAssertionFailures() {
        return assertionFailures;
    }

    /**
     * Verify that there were no assertion failures
     */
    public void verify() {
        String msg = "There were assertion failures.  See Allure report for specific failures.";
        MatcherAssert.assertThat(msg, getFailureCount() == 0);
    }

    /**
     * Asserts that assertion must be true
     *
     * @param reason    - Reason
     * @param assertion - Assertion
     * @return true if assertion was successful else false
     */
    public boolean assertThat(String reason, boolean assertion) {
        try {
            MatcherAssert.assertThat(reason, assertion);
            assertionSuccesses++;
            return true;
        } catch (AssertionError ae) {
            assertionFailures.add(ae);
            return false;
        }
    }

    /**
     * Wrapper for MatcherAssert.assertThat
     *
     * @param reason  - Reason
     * @param actual  - Actual Object
     * @param matcher - Matcher for assertion
     * @param <T>     - Object
     * @return true if assertion was successful else false
     */
    public <T> boolean assertThat(String reason, T actual, Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(reason, actual, matcher);
            assertionSuccesses++;
            return true;
        } catch (AssertionError ae) {
            assertionFailures.add(ae);
            return false;
        }
    }

    /**
     * Wrapper for MatcherAssert.assertThat
     *
     * @param actual  - Actual Object
     * @param matcher - Matcher for assertion
     * @param <T>     - Object
     * @return true if assertion was successful else false
     */
    public <T> boolean assertThat(T actual, Matcher<? super T> matcher) {
        try {
            MatcherAssert.assertThat(actual, matcher);
            assertionSuccesses++;
            return true;
        } catch (AssertionError ae) {
            assertionFailures.add(ae);
            return false;
        }
    }

    /**
     * Asserts that actual &amp; expected objects are equal (using xstream to create the strings for comparison)<BR>
     * <B>Notes:</B><BR>
     * 1) If both objects are null, then they are considered equal<BR>
     *
     * @param reason   - Reason
     * @param actual   - Actual Object
     * @param expected - Expected Line
     * @param <T>      - Object
     * @return true if all assertions were successful else false
     */
    public <T> boolean assertEqual(String reason, T actual, T expected) {
        List<Boolean> results = new ArrayList<>();

        if (actual == null || expected == null) {
            results.add(assertThat(reason, actual, nullValue()));
            results.add(assertThat(reason, expected, nullValue()));
        } else {
            String[] actualLines = new DomainObject().getXstream().toXML(actual).split("\n");
            String[] expectedLines = new DomainObject().getXstream().toXML(expected).split("\n");
            results.add(assertThat(reason + " - Number Of Lines", actualLines.length, equalTo(expectedLines.length)));

            int totalLines = Math.min(actualLines.length, expectedLines.length);
            for (int i = 0; i < totalLines; i++) {
                results.add(assertThat(reason + " - Line[" + (i + 1) + "]", actualLines[i], equalTo(expectedLines[i])));
            }
        }

        return BooleanUtils.and(results.toArray(new Boolean[0]));
    }

    /**
     * Appends the assertion failures and assertion success count in the specified aggregator
     *
     * @param assertAggregator - The aggregator containing the assertion information to be added
     */
    public void append(AssertAggregator assertAggregator) {
        if (assertAggregator != null) {
            assertionFailures.addAll(assertAggregator.getAssertionFailures());
            assertionSuccesses += assertAggregator.getSuccessCount();
        }
    }

}
