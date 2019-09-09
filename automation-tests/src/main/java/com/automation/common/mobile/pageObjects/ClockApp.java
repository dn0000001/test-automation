package com.automation.common.mobile.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import io.appium.java_client.pagefactory.AndroidFindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.MobileComponent;

/**
 * This works with the Clock App in Android 8.1 for Samsung Galaxy S6
 */
public class ClockApp extends PageObjectV2 {
    @XStreamOmitField
    @AndroidFindBy(accessibility = "Alarm")
    private MobileComponent alarm;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "Clock")
    private MobileComponent clock;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "Timer")
    private MobileComponent timer;

    @XStreamOmitField
    @AndroidFindBy(accessibility = "Stopwatch")
    private MobileComponent stopwatch;

    public ClockApp() {
        super();
    }

    public ClockApp(TestContext context) {
        super(context);
    }

    @Step("Click Alarm")
    public void clickAlarm() {
        alarm.click();
    }

    @Step("Click Clock")
    public void clickClock() {
        clock.click();
    }

    @Step("Click Timer")
    public void clickTimer() {
        timer.click();
    }

    @Step("Click Stopwatch")
    public void clickStopwatch() {
        stopwatch.click();
    }

}
