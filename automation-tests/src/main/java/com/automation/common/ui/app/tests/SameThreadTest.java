package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.mutable.MutableInt;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Test to validate the same thread is being used as the framework is assuming this
 */
public class SameThreadTest extends TestNGBase {
    private static final int GREATER_THAN = 4;
    private long expectedThreadId;

    @BeforeTest
    private void performSetExpectedThreadId() {
        // The context is set using BeforeTest as such set the expected thread id
        expectedThreadId = Thread.currentThread().getId();
    }

    @Features("Framework")
    @Stories("Validate the same thread is being used")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest() {
        Helper.log("Running Test", true);
        assertThat("During Test", Thread.currentThread().getId(), equalTo(expectedThreadId));

        MutableInt count = new MutableInt(0);
        Failsafe.with(Utils.getPollingRetryPolicy()).run(() -> validationMethod(count, GREATER_THAN));

        count.setValue(0);
        Failsafe.with(Utils.getPollingRetryPolicy()).get(() -> getSomeValue(count, GREATER_THAN));

        Helper.log("Completed Test", true);
    }

    @Step("Validate ({0} + 1) greater than {1}")
    private void validationMethod(MutableInt count, int value) {
        assertThat("Failsafe Run Test #" + count, Thread.currentThread().getId(), equalTo(expectedThreadId));
        count.increment();
        assertThat("Return Value", count.intValue(), greaterThan(value));
    }

    private int getSomeValue(MutableInt count, int value) {
        assertThat("Failsafe Get Test #" + count, Thread.currentThread().getId(), equalTo(expectedThreadId));
        count.increment();
        assertThat("Get Value", count.intValue(), greaterThan(value));
        return count.intValue();
    }

    @AfterTest
    private void performAfterTestToEnsureTheBrowserIsClosed() {
        Helper.log("Running AfterTest", true);
        assertThat("AfterTest", Thread.currentThread().getId(), equalTo(expectedThreadId));
        Helper.log("Completed AfterTest", true);
    }

    @AfterClass
    private void performAfterClass() {
        Helper.log("Running AfterClass", true);
        assertThat("AfterClass", Thread.currentThread().getId(), equalTo(expectedThreadId));
        Helper.log("Completed AfterClass", true);
    }

}
