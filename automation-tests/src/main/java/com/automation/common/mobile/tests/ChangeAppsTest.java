package com.automation.common.mobile.tests;

import com.automation.common.mobile.domainObjects.MobileDO;
import com.automation.common.mobile.pageObjects.CalculatorApp;
import com.automation.common.mobile.pageObjects.ClockApp;
import com.taf.automation.mobile.AppConfigBuilder;
import com.taf.automation.mobile.AppUnderTest;
import com.taf.automation.mobile.MobileDevice;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.WebDriverTypeEnum;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class ChangeAppsTest extends TestNGBase {
    private static final String DEFAULT_APPIUM_SERVER = "http://127.0.0.1:4723/wd/hub";

    @Features("Appium")
    @Stories("Verify testing in 2 Apps in single test")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest() {
        AppConfigBuilder calcConfig = new AppConfigBuilder()
                .withApp(AppUnderTest.CALCULATOR)
                .withMobileDevice(MobileDevice.SAMSUNG_GALAXY_S6)
                .withWebDriverTypeEnum(WebDriverTypeEnum.ANDROID)
                .withRemoteURL(DEFAULT_APPIUM_SERVER);

        AppConfigBuilder clockConfig = new AppConfigBuilder()
                .withApp(AppUnderTest.CLOCK)
                .withMobileDevice(MobileDevice.SAMSUNG_GALAXY_S6)
                .withWebDriverTypeEnum(WebDriverTypeEnum.ANDROID)
                .withRemoteURL(DEFAULT_APPIUM_SERVER);

        // Note:  Normally, you would not do this but I want to test that specified test properties are used
        // Also, I am using a regular browser in the default test properties as such I don't want it stored
        Utils.changeBrowser(calcConfig.getTestProperties(), calcConfig.getCapabilities(), false);

        MobileDO mobileDO = new MobileDO(getContext(calcConfig.getTestProperties()));
        CalculatorApp calc = mobileDO.getCalculatorApp();
        calc.clickOne();
        calc.clickPlus();
        calc.clickNine();
        calc.clickMinus();
        calc.clickThree();
        takeScreenshot("Calculator App - Entered Formula");
        takeHTML("Calculator App - Entered Formula - Source");

        Utils.changeBrowser(clockConfig);
        ClockApp clock = mobileDO.getClockApp();
        clock.clickAlarm();
        clock.clickClock();
        takeScreenshot("Clock App - At Clock tab");
        takeHTML("Clock App - At Clock tab - Source");

        // Note:  Appium does not seem to store the session as such restoring the browser does not work properly
        // and leads to an error when trying to working with the previous app.
    }

}
