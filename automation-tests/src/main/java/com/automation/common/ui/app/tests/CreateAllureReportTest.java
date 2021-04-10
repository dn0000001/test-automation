package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestRunner;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@SuppressWarnings("java:S3252")
public class CreateAllureReportTest {
    @Parameters({"output-folder", "allure-results"})
    @Test
    public void performCreateReport(
            @Optional("target/output-folder") String outputFolder,
            @Optional("target/allure-results") String resultsFolders
    ) {
        TestRunner runner = new TestRunner();
        try {
            String[] inputFolders = resultsFolders.split(",");
            runner.generateReport(outputFolder, inputFolders);
        } catch (Exception ex) {
            AssertJUtil.fail("Could not create report due to exception:  " + ex.getMessage());
        }
    }

}
