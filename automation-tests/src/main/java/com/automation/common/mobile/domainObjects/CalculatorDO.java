package com.automation.common.mobile.domainObjects;

import com.automation.common.mobile.pageObjects.CalculatorApp;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Object to hold data for the Calculator App Test
 */
@XStreamAlias("calculator-do")
public class CalculatorDO extends DomainObject {
    private CalculatorApp calculatorApp;

    public CalculatorDO() {
        super();
    }

    public CalculatorDO(TestContext context) {
        super(context);
    }

    public CalculatorApp getCalculatorApp() {
        if (calculatorApp == null) {
            calculatorApp = new CalculatorApp();
        }

        if (calculatorApp.getContext() == null) {
            calculatorApp.initPage(getContext());
        }

        return calculatorApp;
    }

}
