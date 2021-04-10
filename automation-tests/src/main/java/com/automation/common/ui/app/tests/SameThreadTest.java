package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
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

/**
 * Test to validate the same thread is being used as the framework is assuming this
 */
@SuppressWarnings("java:S3252")
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
        AssertJUtil.assertThat(Thread.currentThread().getId()).as("During Test").isEqualTo(expectedThreadId);

        MutableInt count = new MutableInt(0);
        Failsafe.with(Utils.getPollingRetryPolicy()).run(() -> validationMethod(count, GREATER_THAN));

        count.setValue(0);
        Failsafe.with(Utils.getPollingRetryPolicy()).get(() -> getSomeValue(count, GREATER_THAN));

        Helper.log("Completed Test", true);
    }

    @Step("Validate ({0} + 1) greater than {1}")
    private void validationMethod(MutableInt count, int value) {
        AssertJUtil.assertThat(Thread.currentThread().getId()).as("Failsafe Run Test #" + count).isEqualTo(expectedThreadId);
        count.increment();
        AssertJUtil.assertThat(count.intValue()).as("Return Value").isGreaterThan(value);
    }

    private int getSomeValue(MutableInt count, int value) {
        AssertJUtil.assertThat(Thread.currentThread().getId()).as("Failsafe Get Test #" + count).isEqualTo(expectedThreadId);
        count.increment();
        AssertJUtil.assertThat(count.intValue()).as("Get Value").isGreaterThan(value);
        return count.intValue();
    }

    @AfterTest
    private void performAfterTestToEnsureTheBrowserIsClosed() {
        Helper.log("Running AfterTest", true);
        AssertJUtil.assertThat(Thread.currentThread().getId()).as("AfterTest").isEqualTo(expectedThreadId);
        Helper.log("Completed AfterTest", true);
    }

    @AfterClass
    private void performAfterClass() {
        Helper.log("Running AfterClass", true);
        AssertJUtil.assertThat(Thread.currentThread().getId()).as("AfterClass").isEqualTo(expectedThreadId);
        Helper.log("Completed AfterClass", true);
    }

}
