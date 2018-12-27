package com.taf.automation.expressions;

/**
 * Expression that will always return false
 */
public class AlwaysFalse implements Expression {
    @Override
    public boolean parse(String expression) {
        // This expression will never match an expression.  It must be manually added to the list of expressions.
        return false;
    }

    @Override
    public <T> boolean eval(T value) {
        return false;
    }

    @Override
    public Expression deepCopy() {
        return this;
    }

}
