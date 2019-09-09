package com.automation.common.mobile.domainObjects;

import com.automation.common.mobile.pageObjects.CalculatorApp;
import com.automation.common.mobile.pageObjects.ClockApp;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Object to hold data for the Mobile App Tests
 */
@XStreamAlias("mobile-do")
public class MobileDO extends DomainObject {
    private CalculatorApp calculatorApp;
    private ClockApp clockApp;

    public MobileDO() {
        super();
    }

    public MobileDO(TestContext context) {
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

    public ClockApp getClockApp() {
        if (clockApp == null) {
            clockApp = new ClockApp();
        }

        if (clockApp.getContext() == null) {
            clockApp.initPage(getContext());
        }

        return clockApp;
    }

}
