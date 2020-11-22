package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for parameters. It could be for SQL queries, HTTP GET/POST requests or something similar
 */
public class Parameter implements Comparable<Parameter> {
    @XStreamAlias("key")
    @XStreamAsAttribute
    private String param;

    @XStreamAsAttribute
    private String value;

    /**
     * Default Constructor - Sets variables to empty String
     */
    public Parameter() {
        withParam("");
        withValue("");
    }

    /**
     * Constructor - Initializes all variables<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) value can be set to null<BR>
     *
     * @param param - Parameter
     * @param value - Value for parameter (null is allowed)
     */
    public Parameter(String param, String value) {
        withParam(param);
        withValue(value);
    }

    /**
     * Constructor - Sets value for parameter to be the empty string<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This method can be used to create a Parameter object from a string to search a List&lt;Parameter&gt;
     * for that string<BR>
     *
     * @param param - Parameter
     */
    public Parameter(String param) {
        withParam(param);
        withValue("");
    }

    /**
     * @return empty string if param is null otherwise param
     */
    public String getParam() {
        return StringUtils.defaultString(param);
    }

    public Parameter withParam(String param) {
        this.param = param;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Parameter withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Returns a copy of the current object that can be changed without affecting the current object
     *
     * @return Parameter
     */
    public Parameter copy() {
        return new Parameter(param, value);
    }

    /**
     * String for logging purposes
     */
    @Override
    public String toString() {
        return StringUtils.defaultString(param, "null") + " = " + StringUtils.defaultString(value, "null");
    }

    /**
     * Parameters are considered equal if the <B>param</B> variables are equal<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This equality is for use with Lists to be able to find/get a Parameter based on param<BR>
     * 2) When not working with lists you need to do an additional check on the value<BR>
     */
    @Override
    public boolean equals(Object objParameter) {
        if (!this.getClass().isInstance(objParameter)) {
            return false;
        }

        Parameter parameter = (Parameter) objParameter;
        return StringUtils.equals(param, parameter.param);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(27, 53);
        builder.append(getParam());
        return builder.toHashCode();
    }

    /**
     * Returns true if all fields are equal
     *
     * @param obj - Object to check for a match
     * @return true if all fields are equal
     */
    public boolean match(Object obj) {
        List<String> excludeFields = new ArrayList<>();
        return Utils.equals(this, obj, excludeFields);
    }

    @Override
    public int compareTo(Parameter arg0) {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(param, arg0.param);
        builder.append(value, arg0.value);
        return builder.toComparison();
    }

}
