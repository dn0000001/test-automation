package com.automation.common.mobile.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.MobileComponent;

/**
 * This works with the Calculator App in Android 8.1 for Samsung Galaxy S6
 */
public class CalculatorApp extends PageObjectV2 {
    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_1")
    private MobileComponent one;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_2")
    private MobileComponent two;

    @XStreamOmitField
    @FindBy(id = "com.android.calculator2:id/digit_3")
    private MobileComponent three;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_4")
    private MobileComponent four;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_5")
    private MobileComponent five;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_6")
    private MobileComponent six;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_7")
    private MobileComponent seven;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_8")
    private MobileComponent eight;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_9")
    private MobileComponent nine;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/digit_0")
    private MobileComponent zero;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "point")
    private MobileComponent period;

    @XStreamOmitField
    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc='plus']")
    private MobileComponent plus;

    @XStreamOmitField
    @FindBy(xpath = "//android.widget.Button[@content-desc='minus']")
    private MobileComponent minus;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "multiply")
    private MobileComponent multiply;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "divide")
    private MobileComponent divide;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "delete")
    private MobileComponent delete;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "equals")
    private MobileComponent equals;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "clear")
    private MobileComponent clear;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/formula")
    private MobileComponent formula;

    @XStreamOmitField
    @AndroidFindBy(id = "com.android.calculator2:id/result")
    private MobileComponent result;

    public CalculatorApp() {
        super();
    }

    public CalculatorApp(TestContext context) {
        super(context);
    }

    public void clickOne() {
        one.click();
    }

    public void clickTwo() {
        two.click();
    }

    public void clickThree() {
        three.click();
    }

    public void clickFour() {
        four.click();
    }

    public void clickFive() {
        five.click();
    }

    public void clickSix() {
        six.click();
    }

    public void clickSeven() {
        seven.click();
    }

    public void clickEight() {
        eight.click();
    }

    public void clickNine() {
        nine.click();
    }

    public void clickZero() {
        zero.click();
    }

    public void clickPeriod() {
        period.click();
    }

    public void clickPlus() {
        plus.click();
    }

    public void clickMinus() {
        minus.click();
    }

    public void clickMultiply() {
        multiply.click();
    }

    public void clickDivide() {
        divide.click();
    }

    public void clickDelete() {
        delete.click();
    }

    public void clickEquals() {
        equals.click();
    }

    public void clickClear() {
        clear.click();
    }

    public String getFormula() {
        return formula.getText();
    }

    public String getResult() {
        return result.getText();
    }

}
