package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;

/**
 * Options for matching an element in general
 */
public class ElementOptions {
    private By locator;
    private String attribute;
    private String pattern;

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
     * @return the attribute used in the comparison
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * @param attribute - the attribute used in the comparison
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the match pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern - the match pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
