package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.WebDriver;

public interface Match {
    /**
     * Set the driver to be used to perform actions
     *
     * @param driver - WebDriver
     */
    void setDriver(WebDriver driver);

    /**
     * Set the criteria for the match
     *
     * @param criteria - Criteria to find match for
     */
    void setCriteria(Criteria criteria);

    /**
     * Determines if there is a match using the criteria<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If method returns true, then it needs to store the ResultInfo for retrieve by method getResultInfo<BR>
     * 2) This method should return false if criteria is null or for any criteria types that are not supported<BR>
     *
     * @return true if match else false
     */
    boolean isMatch();

    /**
     * Get result information. Method needs to handle if the result information was not set
     *
     * @return the result and additional information specific to the criteria type
     */
    ResultInfo getResultInfo();
}
