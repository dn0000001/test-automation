package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Element Ready relative to another matching class
 */
public class ElementReadyRelativeToMatch implements Match {
    private Criteria criteria;
    private ResultInfo resultInfo;
    private ElementRelativeOptions elementRelativeOptions;

    @Override
    public void setDriver(WebDriver driver) {

    }

    @Override
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean isMatch() {
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.RELATIVE_READY) {
            return false;
        }

        try {
            By relative = getOptions().getRelative();
            WebElement anchor = getOptions().getAnchor();
            WebElement element = anchor.findElement(relative);
            if (element.isDisplayed() && element.isEnabled()) {
                resultInfo = new ResultInfo();
                resultInfo.setMatch(true);
                resultInfo.setCriteriaType(CriteriaType.RELATIVE_READY);
                resultInfo.getAdditionalInfo().put(ResultType.ELEMENT, element);
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
     * @return ElementRelativeOptions
     */
    private ElementRelativeOptions getOptions() {
        if (elementRelativeOptions == null) {
            elementRelativeOptions = (ElementRelativeOptions) criteria.getOptions();
        }

        return elementRelativeOptions;
    }

}
