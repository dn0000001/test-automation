package com.taf.automation.ui.support;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Represent a generic row in a table
 */
public class GenericRow extends PageObjectV2 {
    @XStreamOmitField
    private Map<String, String> substitutions;

    public GenericRow() {
        super();
    }

    public GenericRow(TestContext context) {
        super(context);
    }

    /**
     * Get the row key to be used.<BR>
     * <B>Note:  </B> Override this method if you have the need to change the row key
     *
     * @return String
     */
    @SuppressWarnings("squid:S3400")
    public String getRowKey() {
        return "row";
    }

    public void updateRowKey(String value) {
        getSubstitutions().put(getRowKey(), value);
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

    public void initPage(TestContext context) {
        initPage(context, getSubstitutions());
    }

    /**
     * Get a value from the row that uniquely identifies it
     *
     * @return identifier
     */
    public String getRowIdentifier() {
        assertThat("This row implementation cannot be uniquely identified using a single column", false);
        return null;
    }

}
