package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestRunner;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

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
            assertThat("Could not create report due to exception:  " + ex.getMessage(), false);
        }
    }

}
