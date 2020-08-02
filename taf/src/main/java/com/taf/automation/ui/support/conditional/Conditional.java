package com.taf.automation.ui.support.conditional;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to find the first condition that matches a list of possible conditions. An example of
 * this would be if 2 possible outcomes of an action are a button becomes ready or a button is removed. You
 * want to handle each condition differently as such you need to detect which condition occurred. This class
 * will allow you to determine this.
 */
public class Conditional {
    private WebDriver driver;
    private ResultInfo resultInfo;

    /**
     * Stored timeout (in seconds)
     */
    private int timeout;

    /**
     * Stored poll interval (in milliseconds)
     */
    private int poll;

    /**
     * Default Constructor - Poll Interval set to 0.5 second. <B>WebDriver still needs to be set.</B>
     */
    public Conditional() {
        this(null, TestProperties.getInstance().getElementTimeout(), 500);
    }

    /**
     * Constructor - Timeout set from TestProperties. Poll Interval set to 0.5 second
     *
     * @param driver - WebDriver to be used
     */
    public Conditional(WebDriver driver) {
        this(driver, TestProperties.getInstance().getElementTimeout(), 500);
    }

    /**
     * Constructor that specifies timeout. Poll Interval set to 0.5 second
     *
     * @param driver  - WebDriver to be used
     * @param timeout - timeout (in seconds)
     */
    public Conditional(WebDriver driver, int timeout) {
        this(driver, timeout, 500);
    }

    /**
     * Constructor
     *
     * @param driver  - WebDriver to be used
     * @param timeout - timeout (in seconds)
     * @param poll    - Poll interval (in milliseconds)
     */
    public Conditional(WebDriver driver, int timeout, int poll) {
        setDriver(driver);
        setTimeout(timeout);
        setPollInterval(poll);
    }

    /**
     * Set the WebDriver to be used
     *
     * @param driver - WebDriver
     */
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Set the timeout
     *
     * @param timeout - timeout (in seconds)
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set the poll interval
     *
     * @param poll - poll interval (in milliseconds)
     */
    public void setPollInterval(int poll) {
        this.poll = poll;
    }

    /**
     * Get result information
     *
     * @return the result and additional information specific to the criteria type
     */
    public ResultInfo getResultInfo() {
        if (resultInfo == null) {
            resultInfo = new ResultInfo();
            resultInfo.setMatch(false);
        }

        return resultInfo;
    }

    /**
     * Waits for the 1st criteria to match the current condition
     *
     * @param criteria - List of criteria to check against current condition
     * @return <ul>
     * <li>-2 if criteria is null</li>
     * <li>-1 if none of the criteria match the current condition</li>
     * <li>Index of the 1st criteria that matches the current condition (&gt;=0)</li>
     * </ul>
     * @throws RuntimeException if none of the criteria match the current condition before timeout occurs
     */
    public int waitForMatch(List<Criteria> criteria) {
        return waitForMatch(criteria, true);
    }

    /**
     * Waits for the 1st criteria to match the current condition
     *
     * @param criteria   - List of criteria to check against current condition
     * @param throwError - true to throw exception if timeout occurs
     * @return <ul>
     * <li>-2 if criteria is null</li>
     * <li>-1 if none of the criteria match the current condition</li>
     * <li>Index of the 1st criteria that matches the current condition (&gt;=0)</li>
     * </ul>
     * @throws RuntimeException if none of the criteria match the current condition before timeout occurs and
     *                          throw error flag set
     */
    public int waitForMatch(List<Criteria> criteria, boolean throwError) {
        long maxtime = System.currentTimeMillis() + timeout * 1000;
        do {
            int index = match(criteria);

            // If no criteria, then no need to wait until timeout
            if (index == -2) {
                break;
            }

            if (index >= 0) {
                return index;
            } else {
                Utils.sleep(poll);
            }
        }
        while (System.currentTimeMillis() < maxtime);

        if (throwError) {
            StringBuilder sb = new StringBuilder();
            sb.append("None of the criteria matched the current condition before timeout occurred.  Criteria:  ");
            sb.append(Arrays.toString(criteria.toArray()));
            throw new RuntimeException(sb.toString());
        }

        return -1;
    }

    /**
     * Waits for <B>all</B> of the criteria to match the current condition
     *
     * @param criteria - List of criteria to match against current condition
     * @throws RuntimeException if all the criteria does not match before timeout occurs
     */
    public void waitForAllMatches(List<Criteria> criteria) {
        waitForAllMatches(criteria, true);
    }

    /**
     * Waits for <B>all</B> of the criteria to match the current condition
     *
     * @param criteria   - List of criteria to match against current condition
     * @param throwError - true to throw exception if timeout occurs
     * @return true if all criteria was matched before timeout occurred else false
     * @throws RuntimeException if all the criteria does not match before timeout occurs and throw error flag
     *                          set
     */
    public boolean waitForAllMatches(List<Criteria> criteria, boolean throwError) {
        long maxtime = System.currentTimeMillis() + timeout * 1000;
        do {
            if (isAllMatched(criteria)) {
                return true;
            } else {
                Utils.sleep(poll);
            }
        }
        while (System.currentTimeMillis() < maxtime);

        if (throwError) {
            StringBuilder sb = new StringBuilder();
            sb.append("All of the criteria was not matched before timeout occurred.  Criteria:  ");
            sb.append(Arrays.toString(criteria.toArray()));
            throw new RuntimeException(sb.toString());
        }

        return false;
    }

    /**
     * Checks if any of the criteria match the current condition
     *
     * @param criteria - List of criteria to check against
     * @return <ul>
     * <li>-2 if criteria is null</li>
     * <li>-1 if none of the criteria match the current condition</li>
     * <li>Index of the 1st criteria that matches the current condition (&gt;=0)</li>
     * </ul>
     */
    public int match(List<Criteria> criteria) {
        if (criteria == null) {
            return -2;
        }

        for (int i = 0; i < criteria.size(); i++) {
            boolean result = isMatch(criteria.get(i));
            if (result) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Checks if <B>all</B> of the criteria match the current condition
     *
     * @param criteria - List of criteria to check
     * @return false if any criteria is not matched, true if all criteria are matched
     */
    public boolean isAllMatched(List<Criteria> criteria) {
        if (criteria == null) {
            return false;
        }

        for (int i = 0; i < criteria.size(); i++) {
            boolean result = isMatch(criteria.get(i));
            if (!result) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the criteria matches the current condition
     *
     * @param criteria - criteria to check against
     * @return true if criteria matches the current condition else false
     */
    public boolean isMatch(Criteria criteria) {
        MatchFactory matchFactory = new MatchFactory();
        Match matcher = matchFactory.getMatch(criteria);
        matcher.setDriver(driver);
        matcher.setCriteria(criteria);
        if (matcher.isMatch()) {
            resultInfo = matcher.getResultInfo();
            return true;
        }

        return false;
    }

}
