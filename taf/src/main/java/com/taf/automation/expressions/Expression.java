package com.taf.automation.expressions;

public interface Expression {
    /**
     * Parses the expression to see if it is supported and stores the matching criteria if necessary<BR>
     * <B>Note: </B> A <B>null</B> expression will be handled by the implementing class
     *
     * @param expression - Expression to be parsed
     * @return true the expression was parsed successful and matching criteria stored, false in all other cases
     */
    boolean parse(String expression);

    /**
     * Evaluates whether the specified value is considered a match<BR>
     * <B>Note: </B> A <B>null</B> value will be handled by the implementing class as well as an unsupported object
     *
     * @param value - String to evaluate if it is a match
     * @return true if match else false
     */
    <T> boolean eval(T value);

    /**
     * Returns a deep copy of the expression
     *
     * @return Expression
     */
    Expression deepCopy();
}
