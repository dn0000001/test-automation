package com.taf.automation.ui.support;

/**
 * Replacement for SystemClock class as this is deprecated in Selenium 3.14<BR>
 * <B>Note: </B> This is the same code from the SystemClock class.<BR>
 */
public class BasicClock {
    /**
     * Computes a point of time in the future.
     *
     * @param durationInMillis The point in the future, in milliseconds relative to the {@link #now()
     *                         current time}.
     * @return A timestamp representing a point in the future.
     */
    public long laterBy(long durationInMillis) {
        return System.currentTimeMillis() + durationInMillis;
    }

    /**
     * Tests if a point in time occurs before the {@link #now() current time}.
     *
     * @param endInMillis The timestamp to check.
     * @return Whether the given timestamp represents a point in time before the current time.
     */
    public boolean isNowBefore(long endInMillis) {
        return System.currentTimeMillis() < endInMillis;
    }

    /**
     * @return The current time in milliseconds since epoch time.
     * @see System#currentTimeMillis()
     */
    public long now() {
        return System.currentTimeMillis();
    }

}
