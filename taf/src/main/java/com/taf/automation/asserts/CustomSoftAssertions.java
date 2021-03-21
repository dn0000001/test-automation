package com.taf.automation.asserts;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.pagecomponent.PageComponent;

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

}
