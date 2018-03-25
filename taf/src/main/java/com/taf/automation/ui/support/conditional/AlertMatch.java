package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

/**
 * Alert matching class
 */
public class AlertMatch implements Match {
    private WebDriver driver;
    private Criteria criteria;
    private ResultInfo resultInfo;
    private AlertOptions alertOptions;

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
        if (criteria == null || criteria.getCriteriaType() != CriteriaType.ALERT) {
            return false;
        }

        try {
            // Give focus to the alert
            // Note: If no alert then exception will occur
            Alert alert = driver.switchTo().alert();

            // Get the alert text
            String sMessage = alert.getText();

            if (getOptions().isAccept()) {
                // Accept the alert
                alert.accept();
            } else {
                // Dismiss the alert
                alert.dismiss();
            }

            resultInfo = new ResultInfo();
            resultInfo.setMatch(true);
            resultInfo.setCriteriaType(CriteriaType.ALERT);
            resultInfo.getAdditionalInfo().put(ResultType.ACCEPTED_ALERT, getOptions().isAccept());
            resultInfo.getAdditionalInfo().put(ResultType.VALUE, sMessage);
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
     * @return AlertOptions
     */
    private AlertOptions getOptions() {
        if (alertOptions == null) {
            alertOptions = (AlertOptions) criteria.getOptions();
        }

        return alertOptions;
    }

}
