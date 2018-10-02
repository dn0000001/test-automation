package com.taf.automation.expressions;

import java.util.List;

public interface ExpressionParser {
    /**
     * Add expression to be available
     *
     * @param expression - Expression that will be available to parse the conditions
     * @return ExpressionParser
     */
    ExpressionParser withExpression(Expression expression);

    /**
     * Add expressions to be available
     *
     * @param all - List of expressions that will be available to parse the conditions
     * @return ExpressionParser
     */
    ExpressionParser withExpression(List<Expression> all);

    /**
     * Parse the conditions into expressions
     *
     * @param conditions - Conditions to be parsed
     * @return ExpressionParser
     */
    ExpressionParser withConditions(String conditions);

    /**
     * Evaluates whether the specified value is considered a match
     *
     * @param value - String to evaluate if it is a match
     * @return true if match else false
     */
    <T> boolean eval(T value);
}
