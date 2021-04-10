package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.converters.EnvironmentPropertyConverter;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.AssertJUtil;
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

@SuppressWarnings("java:S3252")
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
        AssertJUtil.assertThat(env).as("test.properties needs the property:  test.env=data/ui/sample-environment.xml:dev").isNotNull();
        AssertJUtil.assertThat(env.getEnvironmentName()).as("environmentName").isEqualToIgnoringCase("DEV");
        AssertJUtil.assertThat(env.getUrl()).as("url").isEqualTo("https://www.google.ca");
        AssertJUtil.assertThat(env.getCustom(CUSTOM_PROP_1)).as(CUSTOM_PROP_1).isEqualTo("https://jsonip.com");
        AssertJUtil.assertThat(env.getCustom(DB_PASSWORD)).as(DB_PASSWORD).isEqualTo("Q27p7DXgP2AzDxxBDw/E0g==");
        AssertJUtil.assertThat(new CryptoUtils().decrypt(env.getCustom(DB_PASSWORD))).as(DB_PASSWORD + " decode").isEqualTo("password");

        AssertJUtil.assertThat(env.getUsers().size()).as("Number of users").isEqualTo(NumberUtils.toInt(env.getCustom(CUSTOM_PROP_3)));
        String user = env.getUser(ROLE_SUPER).getUserName();
        String encodedPassword = env.getUser(ROLE_SUPER).getPassword();
        String decodedPassword = new CryptoUtils().decrypt(encodedPassword);
        AssertJUtil.assertThat(user).as("User").isEqualTo("s2");
        AssertJUtil.assertThat(encodedPassword).as("Encoded Password").isEqualTo("feuGPPZDZlkVmHXJwOISwQ==");
        AssertJUtil.assertThat(decodedPassword).as("Decoded Password").isEqualTo("password1");
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
        AssertJUtil.assertThat(propQA.getUser(ROLE_SUPER)).as(QA + ROLE_SUPER).isNull();
        AssertJUtil.assertThat(propQA.getUser(ROLE_ADMIN).getUserName()).as(QA + ROLE_ADMIN).isEqualTo("a1");
        AssertJUtil.assertThat(propQA.getUser(ROLE_USER).getUserName()).as(QA + ROLE_USER).isEqualTo("u1");
        AssertJUtil.assertThat(propQA.getUser(ROLE_DUPLICATE).getUserName()).as(QA + ROLE_DUPLICATE).isEqualTo("d1");

        AssertJUtil.assertThat(propDEV.getUser(ROLE_SUPER).getUserName()).as(DEV + ROLE_SUPER).isEqualTo("s2");
        AssertJUtil.assertThat(propDEV.getUser(ROLE_ADMIN).getUserName()).as(DEV + ROLE_ADMIN).isEqualTo("a2");
        AssertJUtil.assertThat(propDEV.getUser(ROLE_USER).getUserName()).as(DEV + ROLE_USER).isEqualTo("u2");

        AssertJUtil.assertThat(propPROD.getUser(ROLE_SUPER)).as(PROD + ROLE_SUPER).isNull();
        AssertJUtil.assertThat(propPROD.getUser(ROLE_ADMIN)).as(PROD + ROLE_ADMIN).isNull();
        AssertJUtil.assertThat(propPROD.getUser(ROLE_USER).getUserName()).as(PROD + ROLE_USER).isEqualTo("u3");

        //
        // Test the caching & custom methods to get the properties
        //
        AssertJUtil.assertThat(propQA.isCustom(CUSTOM_PROP_4)).as(QA + CUSTOM_PROP_4).isFalse();
        AssertJUtil.assertThat(propQA.isCustom(CUSTOM_PROP_5)).as(QA + CUSTOM_PROP_5).isTrue();
        AssertJUtil.assertThat(propQA.getCustom(CUSTOM_PROP_1, "custom1")).as(QA + CUSTOM_PROP_1).isEqualTo("http://www.holidaywebservice.com");
        AssertJUtil.assertThat(propQA.getCustom(CUSTOM_PROP_2, "custom2")).as(QA + CUSTOM_PROP_2).isEqualTo("feuGPPZDZlkVmHXJwOISwQ==");
        AssertJUtil.assertThat(propQA.getCustom(CUSTOM_PROP_2, "custom2 decode", true)).as(QA + CUSTOM_PROP_2).isEqualTo("password1");
        AssertJUtil.assertThat(propQA.getCustom(CUSTOM_PROP_3, "-1")).as(QA + CUSTOM_PROP_3).isEqualTo("2");
        AssertJUtil.assertThat(propQA.getCustom(CUSTOM_PROP_DUPLICATE, "default")).as(QA + CUSTOM_PROP_DUPLICATE).isEqualTo("fd1");

        AssertJUtil.assertThat(propDEV.isCustom(CUSTOM_PROP_4)).as(DEV + CUSTOM_PROP_4).isTrue();
        AssertJUtil.assertThat(propDEV.isCustom(CUSTOM_PROP_5)).as(DEV + CUSTOM_PROP_5).isFalse();
        AssertJUtil.assertThat(propDEV.getCustom(CUSTOM_PROP_1, "custom1b")).as(DEV + CUSTOM_PROP_1).isEqualTo("https://jsonip.com");
        AssertJUtil.assertThat(propDEV.getCustom(CUSTOM_PROP_2, "custom2b")).as(DEV + CUSTOM_PROP_2).isEqualTo("Q27p7DXgP2AzDxxBDw/E0g==");
        AssertJUtil.assertThat(propDEV.getCustom(CUSTOM_PROP_2, "custom2b decode", true)).as(DEV + CUSTOM_PROP_2).isEqualTo("password");
        AssertJUtil.assertThat(propDEV.getCustom(CUSTOM_PROP_3, "-1")).as(DEV + CUSTOM_PROP_3).isEqualTo("3");
        AssertJUtil.assertThat(propDEV.getCustom(CUSTOM_PROP_DUPLICATE, "fd2")).as(DEV + CUSTOM_PROP_DUPLICATE).isEqualTo("fd2");

        AssertJUtil.assertThat(propPROD.isCustom(CUSTOM_PROP_4)).as(PROD + CUSTOM_PROP_4).isFalse();
        AssertJUtil.assertThat(propPROD.isCustom(CUSTOM_PROP_5)).as(PROD + CUSTOM_PROP_5).isFalse();
        AssertJUtil.assertThat(propPROD.getCustom(CUSTOM_PROP_1, "custom1c")).as(PROD + CUSTOM_PROP_1).isEqualTo("http://wsf.cdyne.com");
        AssertJUtil.assertThat(propPROD.getCustom(CUSTOM_PROP_2, "custom2c")).as(PROD + CUSTOM_PROP_2).isEqualTo("xzRZ75G077/YqCsRSAHGMw==");
        AssertJUtil.assertThat(propPROD.getCustom(CUSTOM_PROP_2, "custom2c decode", true)).as(PROD + CUSTOM_PROP_2).isEqualTo("password2");
        AssertJUtil.assertThat(propPROD.getCustom(CUSTOM_PROP_3, "-1")).as(PROD + CUSTOM_PROP_3).isEqualTo("1");
        AssertJUtil.assertThat(propPROD.getCustom(CUSTOM_PROP_DUPLICATE, "fd3")).as(PROD + CUSTOM_PROP_DUPLICATE).isEqualTo("fd3");
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
