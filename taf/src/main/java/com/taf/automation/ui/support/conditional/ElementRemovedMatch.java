package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Element Removed matching class
 */
public class ElementRemovedMatch implements Match {
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
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.REMOVED) {
            return false;
        }

        try {
            By locator = getOptions().getLocator();
            WebElement element;
            try {
                element = driver.findElement(locator);
            } catch (Exception ignore) {
                // Element no longer in DOM (assumes that driver is not null
                element = null;
            }

            if (element == null || !element.isDisplayed()) {
                resultInfo = new ResultInfo();
                resultInfo.setMatch(true);
                resultInfo.setCriteriaType(CriteriaType.REMOVED);
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
