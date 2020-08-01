package com.taf.automation.expressions;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic Parser that can deal with OR conditions &amp; AND conditions
 */
public class BasicParser implements ExpressionParser {
    private static final String DEFAULT_OR_SEPARATOR = "||";
    private static final String DEFAULT_AND_SEPARATOR = "&&";
    private String orSeparator;
    private String andSeparator;
    private List<Expression> orConditions;
    private Map<Integer, List<Expression>> entireAndConditions;
    private Expressions executor;

    /**
     * Default constructor that sets the default OR separator as "||" &amp; the default AND separator as "&amp;&amp;"
     */
    public BasicParser() {
        orSeparator = DEFAULT_OR_SEPARATOR;
        andSeparator = DEFAULT_AND_SEPARATOR;
        orConditions = new ArrayList<>();
        entireAndConditions = new HashMap<>();
        executor = new Expressions();
    }

    /**
     * Set the OR separator being used
     *
     * @param orSeparator - Separator
     * @return ExpressionParser
     */
    public ExpressionParser withOrSeparator(String orSeparator) {
        if (!StringUtils.defaultString(orSeparator).equals("")) {
            this.orSeparator = orSeparator;
        }

        return this;
    }

    /**
     * Set the AND separator being used
     *
     * @param andSeparator - Separator
     * @return ExpressionParser
     */
    public ExpressionParser withAndSeparator(String andSeparator) {
        if (!StringUtils.defaultString(andSeparator).equals("")) {
            this.andSeparator = andSeparator;
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

        int index = 0;
        String[] orArgs = Utils.splitData(conditions, orSeparator);
        for (String singleCondition : orArgs) {
            if (singleCondition.contains(andSeparator)) {
                parseAndConditions(singleCondition, index);
                index++;
            } else {
                Expression expression = executor.parse(singleCondition);
                if (expression != null) {
                    orConditions.add(expression);
                }
            }
        }

        return this;
    }

    private void parseAndConditions(String conditions, int index) {
        List<Expression> entireAndCondition = new ArrayList<>();

        String[] andArgs = Utils.splitData(conditions, andSeparator);
        for (String condition : andArgs) {
            Expression expression = executor.parse(condition);
            if (expression == null) {
                entireAndCondition.add(new AlwaysFalse());
            } else {
                entireAndCondition.add(expression);
            }
        }

        entireAndConditions.put(index, entireAndCondition);
    }

    @Override
    public <T> boolean eval(T value) {
        return ExpressionsUtil.or(value, orConditions) || anyEntireAndCondition(value);
    }

    private <T> boolean anyEntireAndCondition(T value) {
        for (Map.Entry<Integer, List<Expression>> entry : entireAndConditions.entrySet()) {
            if (ExpressionsUtil.and(value, entry.getValue())) {
                return true;
            }
        }

        return false;
    }

}
