package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.mutable.MutableInt;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;

public class RetryTest extends TestNGBase {
    private MutableInt attempt;

    @Features("Framework")
    @Stories("Validate the retry functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    public void testThatPassesOnRetry() {
        if (attempt == null) {
            attempt = new MutableInt(0);
            assertThat("Always fails 1st time", false);
        }
    }

}
