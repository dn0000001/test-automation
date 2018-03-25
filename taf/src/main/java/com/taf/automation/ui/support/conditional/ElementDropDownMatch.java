package com.taf.automation.ui.support.conditional;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Element Drop Down (that only allows a single selection) matching class
 */
public class ElementDropDownMatch implements Match {
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

        if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_EQUALS
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_REGEX
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_CONTAINS
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_INDEX
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_EQUALS
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_REGEX
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_CONTAINS) {
            try {
                String valueVisibleText = null;
                String valueHTML = null;
                String valueIndex = null;

                By locator = getOptions().getLocator();
                WebElement element = driver.findElement(locator);
                Select dropdown = new Select(element);
                List<WebElement> options = dropdown.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    // Find the 1st selected drop down option
                    if (options.get(i).isSelected()) {
                        valueVisibleText = options.get(i).getText();
                        valueHTML = options.get(i).getAttribute("value");
                        valueIndex = String.valueOf(i);
                        break;
                    }
                }

                String actual;
                if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_INDEX) {
                    actual = valueIndex;
                } else if (criteria.getCriteriaType().toString().startsWith("DROPDOWN_HTML_")) {
                    actual = valueHTML;
                } else {
                    actual = valueVisibleText;
                }

                String expected = getOptions().getPattern().trim();
                boolean match = false;
                if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_INDEX
                        || criteria.getCriteriaType() == CriteriaType.DROPDOWN_EQUALS
                        || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_EQUALS) {
                    // Equals option
                    match = actual.equals(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_EQUALS_IGNORE_CASE
                        || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_EQUALS_IGNORE_CASE) {
                    // Equals Ignore Case option
                    match = actual.equalsIgnoreCase(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_REGEX
                        || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_REGEX) {
                    // Regular Expression option
                    match = actual.matches(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_NOT_EQUAL
                        || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_NOT_EQUAL) {
                    // Not Equal option
                    match = !actual.equals(expected);
                } else if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_DOES_NOT_CONTAIN
                        || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_DOES_NOT_CONTAIN) {
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
