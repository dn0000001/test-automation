package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Options for matching a relative element in general
 */
public class ElementRelativeOptions {
    private WebElement anchor;
    private By relative;
    private String attribute;
    private String pattern;

    /**
     * @return the anchor element
     */
    public WebElement getAnchor() {
        return anchor;
    }

    /**
     * @param anchor - the anchor element used to find match relative to
     */
    public void setAnchor(WebElement anchor) {
        this.anchor = anchor;
    }

    /**
     * @return the relative locator
     */
    public By getRelative() {
        return relative;
    }

    /**
     * @param relative - the relative locator used to find match from the anchor element
     */
    public void setRelative(By relative) {
        this.relative = relative;
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
