package com.taf.automation.expressions;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Parser that can deal with AND conditions only
 */
public class BasicAndOnlyParser implements ExpressionParser {
    private static final String DEFAULT_AND_SEPARATOR = "&&";
    private String separator;
    private List<Expression> andConditions;
    private Expressions executor;

    /**
     * Default constructor that sets the default separator as "&amp;&amp;"
     */
    public BasicAndOnlyParser() {
        separator = DEFAULT_AND_SEPARATOR;
        andConditions = new ArrayList<>();
        executor = new Expressions();
    }

    /**
     * Set the separator being used
     *
     * @param separator - Separator
     * @return ExpressionParser
     */
    public ExpressionParser withSeparator(String separator) {
        if (!StringUtils.defaultString(separator).equals("")) {
            this.separator = separator;
        }

        return this;
    }

    @Override
    public ExpressionParser withExpression(Expression expression) {
        executor.withExpression(expression);
        return this;
    }

    @Override
    public ExpressionParser withExpression(List<Expression> all) {
        executor.withExpression(all);
        return this;
    }

    @Override
    public ExpressionParser withConditions(String conditions) {
        if (StringUtils.defaultString(conditions).equals("")) {
            return this;
        }

        String[] args = Utils.splitData(conditions, separator);
        for (String condition : args) {
            Expression expression = executor.parse(condition);
            if (expression == null) {
                andConditions.add(new AlwaysFalse());
            } else {
                andConditions.add(expression);
            }
        }

        return this;
    }

    @Override
    public <T> boolean eval(T value) {
        return ExpressionsUtil.and(value, andConditions);
    }

}
