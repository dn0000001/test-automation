package com.taf.automation.asserts;

import com.taf.automation.ui.support.util.AssertJUtil;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.pagecomponent.PageComponent;

public class CustomSoftAssertions extends SoftAssertions {
    private int storedFailureCount;

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
     * Store the failure count for later with the expectation that a failure will have occurred
     *
     * @return CustomSoftAssertions
     */
    public CustomSoftAssertions expectFailure() {
        storedFailureCount = getFailureCount();
        return this;
    }

    /**
     * Verifies that at least 1 expected failure has occurred since the method expectFailure was called
     *
     * @return CustomSoftAssertions
     * @throws AssertionError if there is not at least 1 expected failure
     */
    @SuppressWarnings("java:S3252")
    public CustomSoftAssertions assertExpectedFailure() {
        AssertJUtil.assertThat(getFailureCount()).as("Expected Failures").isGreaterThan(storedFailureCount);
        return this;
    }

}
