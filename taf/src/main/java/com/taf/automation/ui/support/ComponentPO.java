package com.taf.automation.ui.support;

/**
 * This is an abstract page object to be used for component page objects
 */
public abstract class ComponentPO extends PageObjectV2 {
    public ComponentPO() {
        super();
    }

    public ComponentPO(TestContext context) {
        super(context);
    }

    /**
     * @return true if there is data to be entered else false
     */
    public abstract boolean hasData();

    /**
     * Fill the page object
     */
    public abstract void fill();

    /**
     * Validate the page object was filled properly
     */
    public abstract void validate();
}
