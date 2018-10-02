package com.taf.automation.expressions;

import org.apache.commons.lang3.StringUtils;

/**
 * Expression that does not match a state against an address
 */
public class StateNotEquals implements Expression {
    private static final String STARTS_WITH = "STATE!=";
    private String state;

    @Override
    public boolean parse(String expression) {
        if (StringUtils.startsWith(expression, STARTS_WITH)) {
            // ex. "STATE!=PA" => state = "PA"
            state = StringUtils.removeStart(expression, STARTS_WITH);
            return true;
        }

        return false;
    }

    @Override
    public <T> boolean eval(T value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return !StringUtils.equals(state, (String) value);
        }

        if (value instanceof USAddress) {
            USAddress address = (USAddress) value;
            return !StringUtils.equals(state, address.getState());
        }

        return false;
    }

    @Override
    public Expression deepCopy() {
        StateNotEquals copy = new StateNotEquals();
        copy.state = state;
        return copy;
    }

}
