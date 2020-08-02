package com.taf.automation.ui.support;

import com.taf.automation.api.ApiUtils;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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

    /**
     * @return String that consists for all non-null fields that are assignable to PageComponent
     */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, Utils.getDefaultStyle());
        List<Field> fields = ApiUtils.getFieldsToValidate(this);
        for (Field field : fields) {
            if (PageComponent.class.isAssignableFrom(field.getType())) {
                builder.append(field.getName(), ((PageComponent) ApiUtils.readField(field, this)).getData(DataTypes.Data, true));
            }
        }

        return builder.toString();
    }

    /**
     * Utility method provided to simplify the process of matching fields in rows where some columns may not exist.
     * Uses the given callable to scrape data that is then checked against the given regex.  The callable will not be
     * run if the regex is null or empty, which is useful for avoiding NoSuchElementExceptions for columns that
     * we don't actually care about.
     *
     * @param actionToScrapeData - Lambda for getting the row value we want to compare
     * @param regex              - Regular Expression to compare to
     * @return whether the field counts as matching
     */
    protected static boolean isMatch(Callable<String> actionToScrapeData, String regex) {
        if (StringUtils.isEmpty(regex)) {
            return true;
        }

        return StringUtils.defaultString(Failsafe.with(Utils.getRetryPolicy(0)).get(actionToScrapeData::call)).matches(regex);
    }

}
