package com.taf.automation.ui.support;

import java.util.ArrayList;
import java.util.List;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class is for parameters. It could be for SQL queries, HTTP GET/POST requests or something similar
 */
public class Parameter implements Comparable<Parameter> {
    public String param;
    public String value;

    /**
     * Default Constructor - Sets variables to empty String
     */
    public Parameter() {
        init("", "");
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
        init(param, value);
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
        init(param, "");
    }

    /**
     * Initializes all variables<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) value can be set to null<BR>
     *
     * @param param - Parameter
     * @param value - Value for parameter (null is allowed)
     */
    protected void init(String param, String value) {
        this.param = StringUtils.defaultString(param);
        this.value = value;
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
    public String toString() {
        String sParam, sValue;
        if (param == null)
            sParam = "null";
        else
            sParam = param;

        if (value == null)
            sValue = "null";
        else
            sValue = value;

        return sParam + " = " + sValue;
    }

    /**
     * Parameters are considered equal if the <B>param</B> variables are equal<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This equality is for use with Lists to be able to find/get a Parameter based on param<BR>
     * 2) When not working with lists you need to do an additional check on the value<BR>
     */
    public boolean equals(Object objParameter) {
        if (!this.getClass().isInstance(objParameter))
            return false;

        Parameter parameter = (Parameter) objParameter;
        if (this.param.equals(parameter.param))
            return true;
        else
            return false;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(27, 53);
        builder.append(param);
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
        int nParamCompare = this.param.compareTo(arg0.param);
        if (nParamCompare < 0) {
            return -1;
        } else if (nParamCompare > 0) {
            return 1;
        } else {
            int nValueCompare = StringUtils.defaultString(this.value).compareTo(
                    StringUtils.defaultString(arg0.value));
            if (nValueCompare < 0)
                return -1;
            else if (nValueCompare > 0)
                return 1;
            else
                return 0;
        }
    }

}
