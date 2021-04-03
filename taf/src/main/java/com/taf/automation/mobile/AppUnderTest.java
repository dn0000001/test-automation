package com.taf.automation.mobile;

import com.taf.automation.ui.support.util.AssertJUtil;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Enumeration to hold your App(s) Under Test
 */
@SuppressWarnings("java:S3252")
public enum AppUnderTest {
    CALCULATOR,
    CLOCK,
    CALCULATOR_THE_GAME;

    public DesiredCapabilities getDesiredCapabilities() {
        if (this == CALCULATOR) {
            return getCalculatorDesiredCapabilities();
        }

        if (this == CLOCK) {
            return getClockDesiredCapabilities();
        }

        if (this == CALCULATOR_THE_GAME) {
            return getCalculatorTheGameDesiredCapabilities();
        }

        AssertJUtil.fail("Unsupported AppUnderTest:  " + this);
        return null;
    }

    private DesiredCapabilities getCalculatorDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.calculator2");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.android.calculator2.Calculator");

        return capabilities;
    }

    private DesiredCapabilities getClockDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.google.android.deskclock");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.android.deskclock.DeskClock");

        return capabilities;
    }

    private DesiredCapabilities getCalculatorTheGameDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(MobileCapabilityType.APP, "/root/tmp/calculator.the.game.1.4.apkpure.com.apk");

        return capabilities;
    }

}
