package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

@SuppressWarnings("java:S3252")
public class RetryTest extends TestNGBase {
    private MutableInt attempt;

    @Features("Framework")
    @Stories("Validate the retry functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    public void testThatPassesOnRetry() {
        AssertJUtil.assertThat(TestProperties.getInstance().getTestDefaultRetry()).as("test.default.retry").isGreaterThan(0);
        new Navigation(getContext()).toDuckDuckGo(Utils.isCleanCookiesSupported());
        performActionThatFailsIntermittently();
        performOtherActionsOfTest();
    }

    @Step("Perform Action That Fails Intermittently")
    private void performActionThatFailsIntermittently() {
        if (attempt == null) {
            attempt = new MutableInt(0);
            AssertJUtil.fail("Always fails 1st time");
        }
    }

    @Step("Perform Other Actions Of Test")
    private void performOtherActionsOfTest() {
        AssertJUtil.assertThat(attempt).as("The variable attempt was not initialized properly").isNotNull();
    }

}
