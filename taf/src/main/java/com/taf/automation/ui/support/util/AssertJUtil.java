package com.taf.automation.ui.support.util;

import com.taf.automation.asserts.CustomSoftAssertions;
import com.taf.automation.asserts.PageComponentAssert;
import com.taf.automation.asserts.WebDriverAssert;
import com.taf.automation.asserts.WebElementAssert;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.pagecomponent.PageComponent;

public class AssertJUtil extends Assertions {
    public static WebElementAssert assertThat(WebElement actual) {
        return new WebElementAssert(actual);
    }

    public static WebDriverAssert assertThat(WebDriver driver) {
        return new WebDriverAssert(driver);
    }

    public static PageComponentAssert assertThat(PageComponent actual) {
        return new PageComponentAssert(actual);
    }

    /**
     * Perform an assertion that is expected to fail (or an exception to be thrown) but continue execution
     * without the need to catch the expected assertion/exception.<BR>
     * <B>Note: </B> This is for parity with Hamcrest Matchers.not functionality (when AssertJ does not have
     * a specific assertion to be the same as using not.)
     *
     * @param log                   - If the assertion does not fail, then this message is used to log it
     * @param runAssertionThatFails - Assertion to be run that is expected to fail
     * @throws AssertionError if the assertion does not fail (or no exception is thrown)
     */
    @SuppressWarnings("java:S5960")
    public static void assertExpectedFailure(String log, final Runnable runAssertionThatFails) {
        boolean result = new CustomSoftAssertions().assertExpectedFailure(log, runAssertionThatFails);
        assertThat(result).as(log).isTrue();
    }

}
