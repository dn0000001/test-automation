package com.taf.automation.ui.support.conditional;

import com.taf.automation.ui.support.util.Utils;
import net.jodah.failsafe.Failsafe;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.Callable;

/**
 * Matching class that takes a Lambda Expression
 */
public class LambdaExpressionMatch implements Match {
    private Criteria criteria;
    private ResultInfo resultInfo;
    private Callable<Boolean> lambda;

    @Override
    public void setDriver(WebDriver driver) {
        // Driver is not necessary for Lambda expressions
    }

    @Override
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean isMatch() {
        if (criteria == null) {
            return false;
        }

        if (criteria.getCriteriaType() != CriteriaType.LAMBDA_EXPRESSION) {
            return false;
        }

        try {
            if (Failsafe.with(Utils.getRetryPolicy(0)).get(getLambda()::call)) {
                resultInfo = new ResultInfo();
                resultInfo.setMatch(true);
                resultInfo.setCriteriaType(criteria.getCriteriaType());
                return true;
            }
        } catch (Exception ignore) {
            // Ignore exception
        }

        return false;
    }

    @Override
    public ResultInfo getResultInfo() {
        if (resultInfo == null) {
            resultInfo = new ResultInfo();
            resultInfo.setMatch(false);
        }

        return resultInfo;
    }

    /**
     * Get Lambda Expression
     *
     * @return Callable&lt;Boolean&gt;
     */
    private Callable<Boolean> getLambda() {
        if (lambda == null) {
            lambda = (Callable<Boolean>) criteria.getOptions();
        }

        return lambda;
    }

}
