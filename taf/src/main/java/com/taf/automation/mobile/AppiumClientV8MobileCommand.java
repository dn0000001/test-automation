package com.taf.automation.mobile;

import io.appium.java_client.MobileCommand;

public class AppiumClientV8MobileCommand extends MobileCommand {
    public static String getSession() {
        return GET_SESSION;
    }

    public static String getAllSession() {
        return GET_ALLSESSION;
    }

}
