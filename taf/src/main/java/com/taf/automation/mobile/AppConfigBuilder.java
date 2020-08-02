package com.taf.automation.mobile;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.WebDriverTypeEnum;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * This class builds the Desired Capabilities to launch the Apps
 */
public class AppConfigBuilder {
    private DesiredCapabilities capabilities;
    private TestProperties testProperties;

    public AppConfigBuilder() {
        capabilities = new DesiredCapabilities();
        testProperties = Utils.deepCopy(TestProperties.getInstance());
        withExtraCapabilities(null);  // Prevent any conflicts or unexpected errors

        //
        // Add your defaults
        //

        withPlatformVersion("8.1");
        withAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
    }

    public AppConfigBuilder merge(Capabilities extraCapabilities) {
        capabilities.merge(extraCapabilities);
        return this;
    }

    public AppConfigBuilder withMobileDevice(MobileDevice mobileDevice) {
        capabilities.merge(mobileDevice.getDesiredCapabilities());
        return this;
    }

    public AppConfigBuilder withApp(AppUnderTest app) {
        capabilities.merge(app.getDesiredCapabilities());
        return this;
    }

    public AppConfigBuilder withPlatformVersion(String value) {
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, value);
        return this;
    }

    /**
     * With Automation Name
     *
     * @param value - Value (Recommended to use AutomationName interface to get a valid value)
     * @return AppConfigBuilder
     */
    public AppConfigBuilder withAutomationName(String value) {
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, value);
        return this;
    }

    public AppConfigBuilder withApp(String value) {
        capabilities.setCapability(MobileCapabilityType.APP, value);
        return this;
    }

    public AppConfigBuilder withWebDriverTypeEnum(WebDriverTypeEnum webDriverTypeEnum) {
        Utils.writeField(testProperties, "browserType", webDriverTypeEnum);
        return this;
    }

    public AppConfigBuilder withRemoteURL(String remoteURL) {
        Utils.writeField(testProperties, "remoteURL", remoteURL);
        return this;
    }

    public AppConfigBuilder withExtraCapabilities(String value) {
        Utils.writeField(testProperties, "extraCapabilities", value);
        return this;
    }

    /**
     * With Capability<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>For Capability Name, use MobileCapabilityType, AndroidMobileCapabilityType or IOSMobileCapabilityType
     * instead of using a String</LI>
     * </OL>
     *
     * @param capabilityName - Capability Name
     * @param value          - Value
     * @return AppConfigBuilder
     */
    public AppConfigBuilder withCapability(String capabilityName, String value) {
        capabilities.setCapability(capabilityName, value);
        return this;
    }

    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    public TestProperties getTestProperties() {
        return testProperties;
    }

}
