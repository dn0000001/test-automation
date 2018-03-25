package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Number of Elements matching class
 */
public class NumberOfElementsMatch implements Match {
    private WebDriver driver;
    private Criteria criteria;
    private ResultInfo resultInfo;
    private NumberOptions numberOptions;

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

        if (criteria.getCriteriaType() != CriteriaType.ELEMENTS_EQUAL &&
                criteria.getCriteriaType() != CriteriaType.ELEMENTS_RANGE &&
                criteria.getCriteriaType() != CriteriaType.ELEMENTS_LESS_THAN &&
                criteria.getCriteriaType() != CriteriaType.ELEMENTS_MORE_THAN) {
            return false;
        }

        try {
            boolean found = false;
            By locator = getOptions().getLocator();
            List<WebElement> elements = driver.findElements(locator);

            if (criteria.getCriteriaType() == CriteriaType.ELEMENTS_EQUAL) {
                found = elements.size() == getOptions().getMin();
            }

            if (criteria.getCriteriaType() == CriteriaType.ELEMENTS_RANGE) {
                found = getOptions().getMin() >= elements.size() && elements.size() <= getOptions().getMax();
            }

            if (criteria.getCriteriaType() == CriteriaType.ELEMENTS_LESS_THAN) {
                found = elements.size() < getOptions().getMin();
            }

            if (criteria.getCriteriaType() == CriteriaType.ELEMENTS_MORE_THAN) {
                found = elements.size() > getOptions().getMin();
            }

            if (found) {
                resultInfo = new ResultInfo();
                resultInfo.setMatch(true);
                resultInfo.setCriteriaType(criteria.getCriteriaType());
                resultInfo.getAdditionalInfo().put(ResultType.LOCATOR, locator);
                resultInfo.getAdditionalInfo().put(ResultType.VALUE, String.valueOf(elements.size()));
                resultInfo.getAdditionalInfo().put(ResultType.ELEMENTS, elements);
                return true;
            }
        } catch (Exception ignore) {
            // Ignore exception
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
     * @return NumberOptions
     */
    private NumberOptions getOptions() {
        if (numberOptions == null) {
            numberOptions = (NumberOptions) criteria.getOptions();
        }

        return numberOptions;
    }

}
