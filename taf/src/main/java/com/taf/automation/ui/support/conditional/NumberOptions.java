package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;

/**
 * Options for matching counts in general
 */
public class NumberOptions {
    private By locator;
    private Integer min;
    private Integer max;

    /**
     * @return the locator
     */
    public By getLocator() {
        return locator;
    }

    /**
     * @param locator - the locator used to find match
     */
    public void setLocator(By locator) {
        this.locator = locator;
    }

    /**
     * @return minimum value
     */
    public Integer getMin() {
        return min;
    }

    /**
     * @param min - Set the minimum value
     */
    public void setMin(Integer min) {
        this.min = min;
    }

    /**
     * @return maximum value
     */
    public Integer getMax() {
        return max;
    }

    /**
     * @param max - Set the maximum value
     */
    public void setMax(Integer max) {
        this.max = max;
    }

}

