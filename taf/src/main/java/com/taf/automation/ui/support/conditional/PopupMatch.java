package com.taf.automation.ui.support.conditional;

import java.util.List;

import org.openqa.selenium.WebDriver;

/**
 * Popup (window) matching class
 */
public class PopupMatch implements Match {
    private WebDriver driver;
    private Criteria criteria;
    private ResultInfo resultInfo;
    private PopupOptions popupOptions;

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
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.POPUP) {
            return false;
        }

        try {
            List<String> excluding = getOptions().getExcluding();
            String[] openWindows = driver.getWindowHandles().toArray(new String[0]);
            if (openWindows.length > 1) {
                // Find the 1st window that is not the exclusion list
                for (int i = 0; i < openWindows.length; i++) {
                    if (!openWindows[i].equals("") && !excluding.contains(openWindows[i])) {
                        resultInfo = new ResultInfo();
                        resultInfo.setMatch(true);
                        resultInfo.setCriteriaType(CriteriaType.POPUP);
                        resultInfo.getAdditionalInfo().put(ResultType.VALUE, openWindows[i]);
                        return true;
                    }
                }
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
     * @return PopupOptions
     */
    private PopupOptions getOptions() {
        if (popupOptions == null) {
            popupOptions = (PopupOptions) criteria.getOptions();
        }

        return popupOptions;
    }

}
