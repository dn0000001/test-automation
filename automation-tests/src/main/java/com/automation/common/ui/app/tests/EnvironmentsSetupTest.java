package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ui.auto.core.support.EnvironmentsSetup;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;

@Listeners(AllureTestNGListener.class)
public class EnvironmentsSetupTest {
    private static final String DB_PASSWORD = "dbPassword";

    @Features("Framework")
    @Stories("Validate that Environments Setup properly")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testEnvironmentsSetup() {
        EnvironmentsSetup.Environment env = TestProperties.getInstance().getTestEnvironment();
        assertThat("test.properties needs the property:  test.env=data/ui/sample-environment.xml:dev", env, notNullValue());
        assertThat("environmentName", env.getEnvironmentName(), equalToIgnoringCase("DEV"));
        assertThat("url", env.getUrl(), equalTo("https://www.google.ca"));
        assertThat("apiUrl", env.getCustom("apiUrl"), equalTo("https://jsonip.com"));
        assertThat(DB_PASSWORD, env.getCustom(DB_PASSWORD), equalTo("Q27p7DXgP2AzDxxBDw/E0g=="));
        assertThat(DB_PASSWORD + " decode", new CryptoUtils().decrypt(env.getCustom(DB_PASSWORD)), equalTo("password"));
    }

}
