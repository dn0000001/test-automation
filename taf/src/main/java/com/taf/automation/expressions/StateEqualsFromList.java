package com.taf.automation.expressions;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Expression that matches list of states against an address
 */
public class StateEqualsFromList implements Expression {
    private static final String STARTS_WITH = "STATE*=";
    private List<String> states;

    @Override
    public boolean parse(String expression) {
        if (StringUtils.startsWith(expression, STARTS_WITH)) {
            // ex. "STATE*=PA,NY" => states = {"PA", "NY"}
            String rawStates = StringUtils.removeStart(expression, STARTS_WITH);
            states = Arrays.asList(Utils.splitData(rawStates, ","));
            return true;
        }

        return false;
    }

    private boolean containsState(String state) {
        if (states == null) {
            return false;
        }

        for (String item : states) {
            if (StringUtils.equals(item, state)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public <T> boolean eval(T value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return containsState((String) value);
        }

        if (value instanceof USAddress) {
            USAddress address = (USAddress) value;
            return containsState(address.getState());
        }

        return false;
    }

    @Override
    public Expression deepCopy() {
        StateEqualsFromList copy = new StateEqualsFromList();
        copy.states = new ArrayList<>(states);
        return copy;
    }

}
