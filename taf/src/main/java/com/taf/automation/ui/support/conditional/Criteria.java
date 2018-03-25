package com.taf.automation.ui.support.conditional;

/**
 * This class holds criteria information to find matches in the Condition class
 */
public class Criteria {
    /**
     * The criteria type
     */
    private CriteriaType type;

    /**
     * The options to be used
     */
    private Object options;

    /**
     * Get the stored options object
     *
     * @return Options object
     */
    public Object getOptions() {
        return options;
    }

    /**
     * Set the options to be used
     *
     * @param options - Options
     */
    public void setOptions(Object options) {
        this.options = options;
    }

    /**
     * Get the criteria type represented
     *
     * @return CriteriaType
     */
    public CriteriaType getCriteriaType() {
        return type;
    }

    /**
     * Set the criteria type represented
     *
     * @param type - Criteria Type Represented
     */
    public void setCriteriaType(CriteriaType type) {
        this.type = type;
    }

}
