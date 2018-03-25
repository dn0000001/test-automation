package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.WebDriver;

/**
 * URL matching class
 */
public class URL_Match implements Match {
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

        if (criteria.getCriteriaType() == CriteriaType.URL_EQUALS
                || criteria.getCriteriaType() == CriteriaType.URL_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.URL_REGEX
                || criteria.getCriteriaType() == CriteriaType.URL_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.URL_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.URL_CONTAINS) {
            try {
                String actual = driver.getCurrentUrl();
                String expected = getOptions().getPattern().trim();

                boolean match = false;
                if (criteria.getCriteriaType() == CriteriaType.URL_EQUALS) {
                    // Equals option
                    match = actual.equals(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.URL_EQUALS_IGNORE_CASE) {
                    // Equals Ignore Case option
                    match = actual.equalsIgnoreCase(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.URL_REGEX) {
                    // Regular Expression option
                    match = actual.matches(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.URL_NOT_EQUAL) {
                    // Not Equal option
                    match = !actual.equals(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.URL_DOES_NOT_CONTAIN) {
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
