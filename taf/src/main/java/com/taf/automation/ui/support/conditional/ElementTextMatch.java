package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Element Text matching class
 */
public class ElementTextMatch implements Match {
    private WebDriver driver;
    private Criteria criteria;
    private ResultInfo resultInfo;
    private ElementOptions elementOptions;

    @Override
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean isMatch() {
        if (criteria == null) {
            return false;
        }

        if (criteria.getCriteriaType() == CriteriaType.TEXT_EQUALS
                || criteria.getCriteriaType() == CriteriaType.TEXT_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.TEXT_REGEX
                || criteria.getCriteriaType() == CriteriaType.TEXT_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.TEXT_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.TEXT_CONTAINS) {
            try {
                By locator = getOptions().getLocator();
                WebElement element = driver.findElement(locator);
                String actual = element.getText().trim();
                String expected = getOptions().getPattern().trim();

                boolean match = false;
                if (criteria.getCriteriaType() == CriteriaType.TEXT_EQUALS) {
                    // Equals option
                    match = actual.equals(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.TEXT_EQUALS_IGNORE_CASE) {
                    // Equals Ignore Case option
                    match = actual.equalsIgnoreCase(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.TEXT_REGEX) {
                    // Regular Expression option
                    match = actual.matches(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.TEXT_NOT_EQUAL) {
                    // Not Equal option
                    match = !actual.equals(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.TEXT_DOES_NOT_CONTAIN) {
                    // Does Not Contain option
                    match = !actual.contains(expected);
                } else {
                    // Default option is contains the text
                    match = actual.contains(expected);
                }

                // Was there a match?
                if (match) {
                    resultInfo = new ResultInfo();
                    resultInfo.setMatch(true);
                    resultInfo.setCriteriaType(criteria.getCriteriaType());
                    resultInfo.getAdditionalInfo().put(ResultType.LOCATOR, locator);
                    resultInfo.getAdditionalInfo().put(ResultType.VALUE, actual);
                    return true;
                }
            } catch (Exception ignore) {
            }
        }

        return false;
    }

    @Override
    public ResultInfo getResultInfo() {
        if (resultInfo == null) {
            resultInfo = new ResultInfo();
            resultInfo.setMatch(false);
        }

        return resultInfo;
    }

    /**
     * Get Options
     *
     * @return ElementOptions
     */
    private ElementOptions getOptions() {
        if (elementOptions == null) {
            elementOptions = (ElementOptions) criteria.getOptions();
        }

        return elementOptions;
    }

}
