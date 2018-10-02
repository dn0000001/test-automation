package com.taf.automation.expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all available expressions and returns the ones that were used
 */
public class Expressions {
    private List<Expression> available;

    public Expressions() {
        available = new ArrayList<>();
    }

    /**
     * Add the expression to the available expressions to test against
     *
     * @param expression - Expression to be added
     * @return Expressions
     */
    public Expressions withExpression(Expression expression) {
        available.add(expression);
        return this;
    }

    /**
     * Add all the expressions to the available expressions to test against
     *
     * @param all - Expressions to be added
     * @return Expressions
     */
    public Expressions withExpression(List<Expression> all) {
        available.addAll(all);
        return this;
    }

    /**
     * Parse the expression to find the first available expression that can parse it
     *
     * @param expression - Expression to be parsed
     * @return the 1st expression implementation that parsed the specified expression else null
     */
    public Expression parse(String expression) {
        for (Expression item : available) {
            if (item.parse(expression)) {
                return item.deepCopy();
            }
        }

        return null;
    }

}
