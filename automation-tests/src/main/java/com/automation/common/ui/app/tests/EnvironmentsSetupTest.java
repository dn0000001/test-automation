package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.converters.EnvironmentPropertyConverter;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;
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
import static org.hamcrest.Matchers.nullValue;

@Listeners(AllureTestNGListener.class)
public class EnvironmentsSetupTest {
    private static final String DB_PASSWORD = "dbPassword";
    private static final String CONFIG_QA = "data/ui/sample-environment.xml:QA";
    private static final String CONFIG_DEV = "data/ui/sample-environment.xml:dEv";
    private static final String CONFIG_PROD = "data/ui/sample-environment.xml:ProD";
    private static final String ROLE_SUPER = "super";
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_USER = "user";
    private static final String ROLE_DUPLICATE = "duplicate";
    private static final String CUSTOM_PROP_1 = "apiUrl";
    private static final String CUSTOM_PROP_2 = "dbPassword";
    private static final String CUSTOM_PROP_3 = "user-count";
    private static final String CUSTOM_PROP_4 = "feature-x";
    private static final String CUSTOM_PROP_5 = "feature-y";
    private static final String CUSTOM_PROP_DUPLICATE = "feature-duplicate";
    private static final String QA = "QA - ";
    private static final String DEV = "DEV - ";
    private static final String PROD = "PROD - ";

    @Features("Framework")
    @Stories("Validate that Environments Setup properly")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void testEnvironmentsSetup() {
        EnvironmentsSetup.Environment env = TestProperties.getInstance().getTestEnvironment();
        assertThat("test.properties needs the property:  test.env=data/ui/sample-environment.xml:dev", env, notNullValue());
        assertThat("environmentName", env.getEnvironmentName(), equalToIgnoringCase("DEV"));
        assertThat("url", env.getUrl(), equalTo("https://www.google.ca"));
        assertThat(CUSTOM_PROP_1, env.getCustom(CUSTOM_PROP_1), equalTo("https://jsonip.com"));
        assertThat(DB_PASSWORD, env.getCustom(DB_PASSWORD), equalTo("Q27p7DXgP2AzDxxBDw/E0g=="));
        assertThat(DB_PASSWORD + " decode", new CryptoUtils().decrypt(env.getCustom(DB_PASSWORD)), equalTo("password"));

        assertThat("Number of users", env.getUsers().size(), equalTo(NumberUtils.toInt(env.getCustom(CUSTOM_PROP_3))));
        String user = env.getUser(ROLE_SUPER).getUserName();
        String encodedPassword = env.getUser(ROLE_SUPER).getPassword();
        String decodedPassword = new CryptoUtils().decrypt(encodedPassword);
        assertThat("User", user, equalTo("s2"));
        assertThat("Encoded Password", encodedPassword, equalTo("feuGPPZDZlkVmHXJwOISwQ=="));
        assertThat("Decoded Password", decodedPassword, equalTo("password1"));
    }

    @Features("Framework")
    @Stories("Validate that Environments Setup properly")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performCustomFunctionalityValidation() {
        //
        // Test the EnvironmentPropertyConverter
        //
        EnvironmentsSetup.Environment envQA = getEnvironment(CONFIG_QA);
        TestProperties propQA = Utils.deepCopy(TestProperties.getInstance());
        reinitializeTestProperties(propQA, envQA);

        EnvironmentsSetup.Environment envDEV = getEnvironment(CONFIG_DEV);
        TestProperties propDEV = Utils.deepCopy(TestProperties.getInstance());
        reinitializeTestProperties(propDEV, envDEV);

        EnvironmentsSetup.Environment envPROD = getEnvironment(CONFIG_PROD);
        TestProperties propPROD = Utils.deepCopy(TestProperties.getInstance());
        reinitializeTestProperties(propPROD, envPROD);

        //
        // Test the caching & custom methods to get the user
        //
        assertThat(QA + ROLE_SUPER, propQA.getUser(ROLE_SUPER), nullValue());
        assertThat(QA + ROLE_ADMIN, propQA.getUser(ROLE_ADMIN).getUserName(), equalTo("a1"));
        assertThat(QA + ROLE_USER, propQA.getUser(ROLE_USER).getUserName(), equalTo("u1"));
        assertThat(QA + ROLE_DUPLICATE, propQA.getUser(ROLE_DUPLICATE).getUserName(), equalTo("d1"));

        assertThat(DEV + ROLE_SUPER, propDEV.getUser(ROLE_SUPER).getUserName(), equalTo("s2"));
        assertThat(DEV + ROLE_ADMIN, propDEV.getUser(ROLE_ADMIN).getUserName(), equalTo("a2"));
        assertThat(DEV + ROLE_USER, propDEV.getUser(ROLE_USER).getUserName(), equalTo("u2"));

        assertThat(PROD + ROLE_SUPER, propPROD.getUser(ROLE_SUPER), nullValue());
        assertThat(PROD + ROLE_ADMIN, propPROD.getUser(ROLE_ADMIN), nullValue());
        assertThat(PROD + ROLE_USER, propPROD.getUser(ROLE_USER).getUserName(), equalTo("u3"));

        //
        // Test the caching & custom methods to get the properties
        //
        assertThat(QA + CUSTOM_PROP_4, propQA.isCustom(CUSTOM_PROP_4), equalTo(false));
        assertThat(QA + CUSTOM_PROP_5, propQA.isCustom(CUSTOM_PROP_5), equalTo(true));
        assertThat(QA + CUSTOM_PROP_1, propQA.getCustom(CUSTOM_PROP_1, "custom1"), equalTo("http://www.holidaywebservice.com"));
        assertThat(QA + CUSTOM_PROP_2, propQA.getCustom(CUSTOM_PROP_2, "custom2"), equalTo("feuGPPZDZlkVmHXJwOISwQ=="));
        assertThat(QA + CUSTOM_PROP_2, propQA.getCustom(CUSTOM_PROP_2, "custom2 decode", true), equalTo("password1"));
        assertThat(QA + CUSTOM_PROP_3, propQA.getCustom(CUSTOM_PROP_3, "-1"), equalTo("2"));
        assertThat(QA + CUSTOM_PROP_DUPLICATE, propQA.getCustom(CUSTOM_PROP_DUPLICATE, "default"), equalTo("fd1"));

        assertThat(DEV + CUSTOM_PROP_4, propDEV.isCustom(CUSTOM_PROP_4), equalTo(true));
        assertThat(DEV + CUSTOM_PROP_5, propDEV.isCustom(CUSTOM_PROP_5), equalTo(false));
        assertThat(DEV + CUSTOM_PROP_1, propDEV.getCustom(CUSTOM_PROP_1, "custom1b"), equalTo("https://jsonip.com"));
        assertThat(DEV + CUSTOM_PROP_2, propDEV.getCustom(CUSTOM_PROP_2, "custom2b"), equalTo("Q27p7DXgP2AzDxxBDw/E0g=="));
        assertThat(DEV + CUSTOM_PROP_2, propDEV.getCustom(CUSTOM_PROP_2, "custom2b decode", true), equalTo("password"));
        assertThat(DEV + CUSTOM_PROP_3, propDEV.getCustom(CUSTOM_PROP_3, "-1"), equalTo("3"));
        assertThat(DEV + CUSTOM_PROP_DUPLICATE, propDEV.getCustom(CUSTOM_PROP_DUPLICATE, "fd2"), equalTo("fd2"));

        assertThat(PROD + CUSTOM_PROP_4, propPROD.isCustom(CUSTOM_PROP_4), equalTo(false));
        assertThat(PROD + CUSTOM_PROP_5, propPROD.isCustom(CUSTOM_PROP_5), equalTo(false));
        assertThat(PROD + CUSTOM_PROP_1, propPROD.getCustom(CUSTOM_PROP_1, "custom1c"), equalTo("http://wsf.cdyne.com"));
        assertThat(PROD + CUSTOM_PROP_2, propPROD.getCustom(CUSTOM_PROP_2, "custom2c"), equalTo("xzRZ75G077/YqCsRSAHGMw=="));
        assertThat(PROD + CUSTOM_PROP_2, propPROD.getCustom(CUSTOM_PROP_2, "custom2c decode", true), equalTo("password2"));
        assertThat(PROD + CUSTOM_PROP_3, propPROD.getCustom(CUSTOM_PROP_3, "-1"), equalTo("1"));
        assertThat(PROD + CUSTOM_PROP_DUPLICATE, propPROD.getCustom(CUSTOM_PROP_DUPLICATE, "fd3"), equalTo("fd3"));
    }

    private EnvironmentsSetup.Environment getEnvironment(String testEnv) {
        return (EnvironmentsSetup.Environment) new EnvironmentPropertyConverter()
                .convert(EnvironmentsSetup.Environment.class, testEnv);
    }

    private void reinitializeTestProperties(TestProperties props, EnvironmentsSetup.Environment env) {
        Utils.writeField(props, "testEnvironment", env);
        Utils.writeField(props, "cachedCustomProps", null);
        Utils.writeField(props, "cachedUsers", null);
    }

}
