package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestRunner;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class OpenAllureReportTest {
    @Parameters("allure-report")
    @Test
    public void performOpenReport(@Optional("target/allure-report") String reportFolder) {
        TestRunner runner = new TestRunner();
        runner.setReportFolder(reportFolder);
        try {
            runner.openReport();
        } catch (Exception ex) {
            assertThat("Could not open report due to exception:  " + ex.getMessage(), false);
        }
    }

}
