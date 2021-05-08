package com.taf.automation.asserts;

import com.taf.automation.ui.support.StringMod;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.util.Throwables.describeErrors;

public class CustomSoftAssertions extends SoftAssertions {
    public WebElementAssert assertThat(WebElement actual) {
        return proxy(WebElementAssert.class, WebElement.class, actual);
    }

    public WebDriverAssert assertThat(WebDriver driver) {
        return proxy(WebDriverAssert.class, WebDriver.class, driver);
    }

    public PageComponentAssert assertThat(PageComponent actual) {
        return proxy(PageComponentAssert.class, PageComponent.class, actual);
    }

    /**
     * @return true if there were any failures otherwise false
     */
    public boolean anyFailures() {
        return !errorsCollected().isEmpty();
    }

    /**
     * @return true if there were no failures otherwise false
     */
    public boolean allSuccessful() {
        return errorsCollected().isEmpty();
    }

    /**
     * @return the failure count
     */
    public int getFailureCount() {
        return errorsCollected().size();
    }

    /**
     * Perform an assertion that is expected to fail (or an exception to be thrown) but continue execution
     * without the need to catch the assertion/exception.<BR>
     * <B>Note: </B> This is for parity with Hamcrest Matchers.not functionality (when AssertJ does not have
     * a specific assertion to be the same as using not.)
     *
     * @param log                   - If the assertion does not fail, then this message is used to log it
     * @param runAssertionThatFails - Assertion to be run that is expected to fail
     * @return true if the assertion failed (or any exception was thrown) as expected, otherwise false
     */
    public boolean assertExpectedFailure(String log, final Runnable runAssertionThatFails) {
        try {
            runAssertionThatFails.run();
            fail(log);
            return false;
        } catch (Exception | AssertionError ex) {
            return true;
        }
    }

    @Override
    public void assertAll() {
        List<Throwable> errors = errorsCollected();
        if (!errors.isEmpty()) {
            tryThrowingMultipleFailuresError(errors);
            List<String> describeErrors = describeErrors(errors).stream()
                    .map(this::stripText)
                    .collect(Collectors.toList());
            throw new SoftAssertionError(describeErrors);
        }
    }

    private String stripText(String value){
        return new StringMod(value)
                .removeAll("at Helper.assertThatSubset\\(.*\\)\\r\\n")
                .removeAll("at Helper.assertThatList\\(.*\\)\\r\\n")
                .removeAll("at Helper.assertThatObject\\(.*\\)\\r\\n")
                .get();
    }

}
