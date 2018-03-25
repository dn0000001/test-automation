package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Element Disabled matching class
 */
public class ElementDisabledMatch implements Match {
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
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.DISABLED) {
            return false;
        }

        try {
            By locator = getOptions().getLocator();
            WebElement element = driver.findElement(locator);
            if (!element.isEnabled()) {
                resultInfo = new ResultInfo();
                resultInfo.setMatch(true);
                resultInfo.setCriteriaType(CriteriaType.DISABLED);
                resultInfo.getAdditionalInfo().put(ResultType.LOCATOR, locator);
                return true;
            }
        } catch (Exception ignore) {
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
