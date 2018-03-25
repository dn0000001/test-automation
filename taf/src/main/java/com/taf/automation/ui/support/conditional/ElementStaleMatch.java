package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

/**
 * Element Stale matching class
 */
public class ElementStaleMatch implements Match {
    private Criteria criteria;
    private ResultInfo resultInfo;
    private ElementRelativeOptions elementRelativeOptions;

    @Override
    public void setDriver(WebDriver driver) {
        // Not used for this matcher
    }

    @Override
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean isMatch() {
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.STALE) {
            return false;
        }

        try {
            getOptions().getAnchor().getTagName();
            // No exception occurred if you get to here which means the element is not stale
        } catch (StaleElementReferenceException ex) {
            resultInfo = new ResultInfo();
            resultInfo.setMatch(true);
            resultInfo.setCriteriaType(CriteriaType.STALE);
            return true;
        } catch (Exception ignore) {
            // Not the exception we are expecting
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
