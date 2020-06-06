package com.taf.automation.ui.support;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an abstract page object to be used for component page objects
 */
public abstract class ComponentPO extends PageObjectV2 {
    @XStreamOmitField
    private Map<String, String> substitutions;

    public ComponentPO() {
        super();
    }

    public ComponentPO(TestContext context) {
        // Note: When using this constructor there will be no substitutions (the variable is null at this point)
        // as such it will always be a normal page object.  So, no special logic is necessary.
        super(context);
    }

    public void updateSubstitutions(Map<String, String> additions) {
        if (additions == null || additions.isEmpty()) {
            return;
        }

        getSubstitutions().putAll(additions);
    }

    protected Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    /**
     * Initialize the page<BR>
     * <B>Note: </B> If no substitutions, then it is initialized as a normal page object
     * else it is initialized as a dynamic page object
     *
     * @param context - Context to initialize the page with
     */
    public void initPage(TestContext context) {
        if (getSubstitutions().isEmpty()) {
            super.initPage(context);
        } else {
            initPage(context, getSubstitutions());
        }
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
