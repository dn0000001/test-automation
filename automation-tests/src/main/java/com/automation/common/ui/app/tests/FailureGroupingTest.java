package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.TestNGBase;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * This class contains tests that will fail to test the Allure Failure Grouping on Categories tab
 */
public class FailureGroupingTest extends TestNGBase {
    private static final By FAILURE_GROUP_1 = By.id("Failure Group 1");
    private static final By FAILURE_GROUP_2 = By.cssSelector("[data='Failure Group 1']");

    @Features("AllureTestNGListener")
    @Stories("Group 1 - Validate that the build info is removed such that failure groups occur on similar exceptions")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void failure1forGroup1() {
        performSomeActionThatFails(FAILURE_GROUP_1);
    }

    @Features("AllureTestNGListener")
    @Stories("Group 2 - Validate that the build info is removed such that failure groups occur on similar exceptions")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void failure1forGroup2() {
        performSomeActionThatFails(FAILURE_GROUP_2);
    }

    @Features("AllureTestNGListener")
    @Stories("Group 1 - Validate that the build info is removed such that failure groups occur on similar exceptions")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void failure2forGroup1() {
        performSomeActionThatFails(FAILURE_GROUP_1);
    }

    @Features("AllureTestNGListener")
    @Stories("Group 2 - Validate that the build info is removed such that failure groups occur on similar exceptions")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void failure2forGroup2() {
        performSomeActionThatFails(FAILURE_GROUP_2);
    }

    @Step("Perform Some Action That Fails")
    private void performSomeActionThatFails(By locator) {
        getContext().getDriver().findElement(locator);
    }

}
