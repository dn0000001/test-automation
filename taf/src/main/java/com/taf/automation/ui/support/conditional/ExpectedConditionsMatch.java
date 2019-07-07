package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Matching class that uses ExpectedConditions
 */
public class ExpectedConditionsMatch implements Match {
    private WebDriver driver;
    private Criteria criteria;
    private ResultInfo resultInfo;
    private ExpectedCondition expectedCondition;

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

        if (criteria.getCriteriaType() != CriteriaType.EXPECTED_CONDITIONS) {
            return false;
        }

        try {
            Object value = getOptions().apply(driver);
            if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                resultInfo = new ResultInfo();
                resultInfo.setMatch(true);
                resultInfo.setCriteriaType(criteria.getCriteriaType());
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
     * @return ExpectedCondition
     */
    private ExpectedCondition getOptions() {
        if (expectedCondition == null) {
            expectedCondition = (ExpectedCondition) criteria.getOptions();
        }

        return expectedCondition;
    }

}
