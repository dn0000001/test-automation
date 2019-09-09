package com.taf.automation.mobile;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Enumeration to hold your supported mobile devices
 */
public enum MobileDevice {
    SAMSUNG_GALAXY_S10(DeviceType.PHONE, "Samsung Galaxy S10"),
    SAMSUNG_GALAXY_S9(DeviceType.PHONE, "Samsung Galaxy S9"),
    SAMSUNG_GALAXY_S8(DeviceType.PHONE, "Samsung Galaxy S8"),
    SAMSUNG_GALAXY_S7_EDGE(DeviceType.PHONE, "Samsung Galaxy S7 Edge"),
    SAMSUNG_GALAXY_S7(DeviceType.PHONE, "Samsung Galaxy S7"),
    SAMSUNG_GALAXY_S6(DeviceType.PHONE, "Samsung Galaxy S6"),
    NEXUS_4(DeviceType.PHONE, "Nexus 4"),
    NEXUS_5(DeviceType.PHONE, "Nexus 5"),
    NEXUS_ONE(DeviceType.PHONE, "Nexus One"),
    NEXUS_S(DeviceType.PHONE, "Nexus S"),
    NEXUS_7(DeviceType.TABLET, "Nexus 7");

    DeviceType deviceType;
    String deviceName;

    public enum DeviceType {
        PHONE("Phone"),
        TABLET("Tablet");

        String type;

        DeviceType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

    }

    MobileDevice(DeviceType deviceType, String deviceName) {
        this.deviceType = deviceType;
        this.deviceName = deviceName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getDeviceName());
        return capabilities;
    }

}
