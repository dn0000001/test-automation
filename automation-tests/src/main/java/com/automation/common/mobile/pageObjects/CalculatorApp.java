package com.automation.common.mobile.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
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

    @Step("Click 1")
    public void clickOne() {
        one.click();
    }

    @Step("Click 2")
    public void clickTwo() {
        two.click();
    }

    @Step("Click 3")
    public void clickThree() {
        three.click();
    }

    @Step("Click 4")
    public void clickFour() {
        four.click();
    }

    @Step("Click 5")
    public void clickFive() {
        five.click();
    }

    @Step("Click 6")
    public void clickSix() {
        six.click();
    }

    @Step("Click 7")
    public void clickSeven() {
        seven.click();
    }

    @Step("Click 8")
    public void clickEight() {
        eight.click();
    }

    @Step("Click 9")
    public void clickNine() {
        nine.click();
    }

    @Step("Click 0")
    public void clickZero() {
        zero.click();
    }

    @Step("Click .")
    public void clickPeriod() {
        period.click();
    }

    @Step("Click +")
    public void clickPlus() {
        plus.click();
    }

    @Step("Click -")
    public void clickMinus() {
        minus.click();
    }

    @Step("Click X")
    public void clickMultiply() {
        multiply.click();
    }

    @Step("Click Divide")
    public void clickDivide() {
        divide.click();
    }

    @Step("Click DEL")
    public void clickDelete() {
        delete.click();
    }

    @Step("Click =")
    public void clickEquals() {
        equals.click();
    }

    @Step("Click CLR")
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
