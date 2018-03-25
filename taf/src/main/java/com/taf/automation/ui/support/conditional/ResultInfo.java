package com.taf.automation.ui.support.conditional;

import java.util.HashMap;
import java.util.Map;

/**
 * Store the result and additional information gathered from getting the result
 */
public class ResultInfo {
    private boolean match;
    private CriteriaType type;
    private Map<ResultType, Object> additionalInfo;

    /**
     * @return true if match else false
     */
    public boolean isMatch() {
        return match;
    }

    /**
     * @param match - true if match else false
     */
    public void setMatch(boolean match) {
        this.match = match;
    }

    /**
     * Get the criteria type that was matched
     *
     * @return null if no match else CriteriaType
     */
    public CriteriaType getCriteriaType() {
        return type;
    }

    /**
     * Set the criteria type that was matched
     *
     * @param type - Criteria Type that was matched
     */
    public void setCriteriaType(CriteriaType type) {
        this.type = type;
    }

    /**
     * Get the additional information
     *
     * @return Map&lt;ResultType, Object&gt;
     */
    public Map<ResultType, Object> getAdditionalInfo() {
        if (additionalInfo == null) {
            additionalInfo = new HashMap<>();
        }

        return additionalInfo;
    }

}
