package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.mutable.MutableInt;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class RetryTest extends TestNGBase {
    private MutableInt attempt;

    @Features("Framework")
    @Stories("Validate the retry functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("url")
    @Test
    @Retry
    public void testThatPassesOnRetry(@Optional("https://duckduckgo.com/") String url) {
        assertThat("test.default.retry", TestProperties.getInstance().getTestDefaultRetry(), greaterThan(0));
        goToUrl(url);
        performActionThatFailsIntermittently();
        performOtherActionsOfTest();
    }

    @Step("Go to URL:  {0}")
    private void goToUrl(String url) {
        getContext().getDriver().get(url);
    }

    @Step("Perform Action That Fails Intermittently")
    private void performActionThatFailsIntermittently() {
        if (attempt == null) {
            attempt = new MutableInt(0);
            assertThat("Always fails 1st time", false);
        }
    }

    @Step("Perform Other Actions Of Test")
    private void performOtherActionsOfTest() {
        assertThat("The variable attempt was not initialized properly", attempt, notNullValue());
    }

}
