package com.taf.automation.mobile;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Enumeration to hold your App(s) Under Test
 */
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

        assertThat("Unsupported AppUnderTest:  " + this, false);
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
