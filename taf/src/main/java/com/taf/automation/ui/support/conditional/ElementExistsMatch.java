package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Element Exists matching class
 */
public class ElementExistsMatch implements Match {
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
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.EXISTS) {
            return false;
        }

        try {
            By locator = getOptions().getLocator();
            driver.findElement(locator);

            // No exception occurred if you get to here which means the element exists
            resultInfo = new ResultInfo();
            resultInfo.setMatch(true);
            resultInfo.setCriteriaType(CriteriaType.EXISTS);
            resultInfo.getAdditionalInfo().put(ResultType.LOCATOR, locator);
            return true;
        } catch (Exception ignore) {
            return false;
        }
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
