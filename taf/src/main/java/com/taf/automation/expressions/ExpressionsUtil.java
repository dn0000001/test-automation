package com.taf.automation.expressions;

import java.util.Arrays;
import java.util.List;

/**
 * Class to evaluate multiple expressions
 */
public class ExpressionsUtil {
    private ExpressionsUtil() {
        //
    }

    /**
     * Test the value/object against all expressions
     *
     * @param value       - The value/object to be tested against
     * @param expressions - All the Expressions that need to be true
     * @param <T>         - Type
     * @return true if ALL expressions are true with the value/object else false
     */
    public static <T> boolean and(T value, Expression... expressions) {
        return (expressions == null) || and(value, Arrays.asList(expressions));
    }

    /**
     * Test the value/object against all expressions
     *
     * @param value       - The value/object to be tested against
     * @param expressions - All the Expressions that need to be true
     * @param <T>         - Type
     * @return true if ALL expressions are true with the value/object else false
     */
    public static <T> boolean and(T value, List<Expression> expressions) {
        if (expressions == null) {
            return true;
        }

        for (Expression expression : expressions) {
            if (!expression.eval(value)) {
                return false;
            }
        }

        return !expressions.isEmpty();
    }

    /**
     * Test the value/object against all expressions
     *
     * @param value       - The value/object to be tested against
     * @param expressions - Any of the Expressions that need to be true
     * @param <T>         - Type
     * @return true if ANY of the expressions are true with the value/object else false
     */
    public static <T> boolean or(T value, Expression... expressions) {
        return (expressions == null) || or(value, Arrays.asList(expressions));
    }

    /**
     * Test the value/object against all expressions
     *
     * @param value       - The value/object to be tested against
     * @param expressions - Any of the Expressions that need to be true
     * @param <T>         - Type
     * @return true if ANY of the expressions are true with the value/object else false
     */
    public static <T> boolean or(T value, List<Expression> expressions) {
        if (expressions == null) {
            return true;
        }

        for (Expression expression : expressions) {
            if (expression.eval(value)) {
                return true;
            }
        }

        return false;
    }

}
