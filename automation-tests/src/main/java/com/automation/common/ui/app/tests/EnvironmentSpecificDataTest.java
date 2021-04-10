package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.Environment;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Test;

@SuppressWarnings("java:S3252")
public class EnvironmentSpecificDataTest {
    private static final String ENVIRONMENT_TARGET = "environmentTarget";
    private static final String ENVIRONMENT = "environment";
    private static final String DOES_NOT_EXIST = "abc-does-not-exist.xml";
    private static final String NO_ENV = "data/ui/LinksTestData.xml";
    private static final String MULTI_ENV = "data/ui/AllComponents_TestData.xml";
    private static final String MULTI_ENV_QA = "data/ui/qa-AllComponents_TestData.xml";
    private static final String MULTI_ENV_PROD = "data/ui/prod-AllComponents_TestData.xml";

    @Test
    public void targetEnvironmentIsNotSetWithNonProd() {
        TestProperties useProps = Utils.deepCopy(TestProperties.getInstance());
        Utils.writeField(useProps, ENVIRONMENT_TARGET, "");
        Utils.writeField(useProps, ENVIRONMENT, Environment.QA);

        String actual = Helper.getEnvironmentBasedFile(MULTI_ENV, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentIsNotSetWithNonProd #1").isEqualTo(MULTI_ENV);

        actual = Helper.getEnvironmentBasedFile(NO_ENV, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentIsNotSetWithNonProd #2").isEqualTo(NO_ENV);

        actual = Helper.getEnvironmentBasedFile(DOES_NOT_EXIST, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentIsNotSetWithNonProd #3").isEqualTo(DOES_NOT_EXIST);
    }

    @Test
    public void targetEnvironmentIsNotSetWithProdEnvironmentDataDoesNotExist() {
        TestProperties useProps = Utils.deepCopy(TestProperties.getInstance());
        Utils.writeField(useProps, ENVIRONMENT_TARGET, "");
        Utils.writeField(useProps, ENVIRONMENT, Environment.PROD);
        String actual = Helper.getEnvironmentBasedFile(NO_ENV, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentIsNotSetWithProdEnvironmentDataDoesNotExist #1").isEqualTo(NO_ENV);

        actual = Helper.getEnvironmentBasedFile(DOES_NOT_EXIST, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentIsNotSetWithProdEnvironmentDataDoesNotExist #2").isEqualTo(DOES_NOT_EXIST);
    }

    @Test
    public void targetEnvironmentIsNotSetWithProdEnvironmentDataExists() {
        TestProperties useProps = Utils.deepCopy(TestProperties.getInstance());
        Utils.writeField(useProps, ENVIRONMENT_TARGET, "");
        Utils.writeField(useProps, ENVIRONMENT, Environment.PROD);
        String actual = Helper.getEnvironmentBasedFile(MULTI_ENV, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentIsNotSetWithProdEnvironmentDataDoesNotExist").isEqualTo(MULTI_ENV_PROD);
    }

    @Test
    public void targetEnvironmentSetEnvironmentDataDoesNotExist() {
        TestProperties useProps = Utils.deepCopy(TestProperties.getInstance());
        Utils.writeField(useProps, ENVIRONMENT_TARGET, "QA");
        Utils.writeField(useProps, ENVIRONMENT, Environment.QA);

        String actual = Helper.getEnvironmentBasedFile(NO_ENV, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentSetEnvironmentDataDoesNotExist #1").isEqualTo(NO_ENV);

        actual = Helper.getEnvironmentBasedFile(DOES_NOT_EXIST, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentSetEnvironmentDataDoesNotExist #2").isEqualTo(DOES_NOT_EXIST);
    }

    @Test
    public void targetEnvironmentSetEnvironmentDataExists() {
        TestProperties useProps = Utils.deepCopy(TestProperties.getInstance());
        Utils.writeField(useProps, ENVIRONMENT_TARGET, "QA");
        Utils.writeField(useProps, ENVIRONMENT, Environment.QA);
        String actual = Helper.getEnvironmentBasedFile(MULTI_ENV, useProps);
        AssertJUtil.assertThat(actual).as("targetEnvironmentSetEnvironmentDataExists").isEqualTo(MULTI_ENV_QA);
    }

}
