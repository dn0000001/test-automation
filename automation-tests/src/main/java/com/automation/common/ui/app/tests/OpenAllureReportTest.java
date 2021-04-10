package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestRunner;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@SuppressWarnings("java:S3252")
public class OpenAllureReportTest {
    @Parameters("allure-report")
    @Test
    public void performOpenReport(@Optional("target/allure-report") String reportFolder) {
        TestRunner runner = new TestRunner();
        runner.setReportFolder(reportFolder);
        try {
            runner.openReport();
        } catch (Exception ex) {
            AssertJUtil.fail("Could not open report due to exception:  " + ex.getMessage());
        }
    }

}
