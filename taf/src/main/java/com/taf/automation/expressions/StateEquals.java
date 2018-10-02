package com.taf.automation.expressions;

import org.apache.commons.lang3.StringUtils;

/**
 * Expression that matches a state against an address
 */
public class StateEquals implements Expression {
    private static final String STARTS_WITH = "STATE==";
    private String state;

    @Override
    public boolean parse(String expression) {
        if (StringUtils.startsWith(expression, STARTS_WITH)) {
            // ex. "STATE==PA" => state = "PA"
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
            return StringUtils.equals(state, (String) value);
        }

        if (value instanceof USAddress) {
            USAddress address = (USAddress) value;
            return StringUtils.equals(state, address.getState());
        }

        return false;
    }

    @Override
    public Expression deepCopy() {
        StateEquals copy = new StateEquals();
        copy.state = state;
        return copy;
    }

}
