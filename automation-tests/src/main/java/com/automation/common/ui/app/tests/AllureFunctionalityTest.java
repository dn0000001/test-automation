package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.automation.common.ui.app.pageObjects.TNHC_LandingPage;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.BooleanUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Issue;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;

public class AllureFunctionalityTest extends TestNGBase {
    private static final int MAIN_STEPS = 10;
    private static final int NESTED_METHODS = 5;
    private static final int NESTED_STEPS_BEFORE = 3;
    private static final int NESTED_STEPS_AFTER = 2;
    private static final String RANDOM_ACTION = "Random action ";

    @Features("Allure")
    @Stories("Have the allure report with all functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"data-set", "status"})
    @Issue("Issue-001")
    @TestCaseId("TestCase-001")
    @Description("This is a test description")
    @Test
    @Retry
    public void performTest(@Optional("data/ui/TNHC_TestData.xml") String dataSet, @Optional("true") String status) {
        // Attachment test
        new TNHC_DO(getContext()).fromResource(dataSet);

        // Test that page-components library still works
        performPageComponentLibraryConflictCheck();

        getContext().getDriver().get(TestProperties.getInstance().getURL());

        performLogin();
        performStatusValidation(status);
        for (int i = 0; i < MAIN_STEPS; i++) {
            writeToReport("Perform Random Action:  " + Rand.alphanumeric(5));
        }

        generateNestedSteps();
        performSignOut();
    }

    @Step("Perform page-component library conflict check")
    private void performPageComponentLibraryConflictCheck() {
        TNHC_LandingPage page = new TNHC_LandingPage();
        page.initPage(getContext(), true);
    }

    @Step("Perform Login")
    private void performLogin() {
        writeToReport("Enter Login");
        writeToReport("Enter Password");
        writeToReport("Click Login");
    }

    @Step("Perform Status Parameter Validation")
    private void performStatusValidation(String status) {
        assertThat("Status Parameter", BooleanUtils.toBoolean(status));
    }

    @Step("Generate Nested Steps")
    private void generateNestedSteps() {
        for (int i = 0; i < NESTED_STEPS_BEFORE; i++) {
            writeToReport(RANDOM_ACTION + Rand.alphanumeric(4));
        }

        generateNestedSteps(1, NESTED_METHODS);

        for (int i = 0; i < NESTED_STEPS_AFTER; i++) {
            writeToReport(RANDOM_ACTION + Rand.alphanumeric(2));
        }
    }

    @Step("Generate Nested Steps:  Start({0}), Stop({1})")
    private void generateNestedSteps(int start, int stop) {
        if (start == stop) {
            return;
        }

        for (int i = 0; i < NESTED_STEPS_BEFORE; i++) {
            writeToReport(RANDOM_ACTION + Rand.alphanumeric(3));
        }

        generateNestedSteps(start + 1, stop);

        for (int i = 0; i < NESTED_STEPS_AFTER; i++) {
            writeToReport(RANDOM_ACTION + Rand.alphanumeric(5));
        }
    }

    @Step("Perform Sign Out")
    private void performSignOut() {
        //
    }

    @Step("{0}")
    private void writeToReport(String value) {
        // Just write to report
    }

}
