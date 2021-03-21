package com.taf.automation.asserts;

import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.AbstractAssert;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("java:S5803")
public class WebElementAssert extends AbstractAssert<WebElementAssert, WebElement> {
    private static final String EXCEPTION_OCCURRED = " but the following exception occurred validating:  %s";

    public WebElementAssert(WebElement actual) {
        super(actual, WebElementAssert.class);
    }

    public static WebElementAssert assertThat(WebElement actual) {
        return new WebElementAssert(actual);
    }

    /**
     * Verifies that the element is displayed
     *
     * @return WebElementAssert
     */
    public WebElementAssert isDisplayed() {
        isNotNull();

        try {
            if (!actual.isDisplayed()) {
                failWithMessage("Expected element to be displayed but was NOT");
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to be displayed" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that the element is enabled
     *
     * @return WebElementAssert
     */
    public WebElementAssert isEnabled() {
        isNotNull();

        try {
            if (!actual.isEnabled()) {
                failWithMessage("Expected element to be enabled but was NOT");
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to be enabled" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that the element is disabled
     *
     * @return WebElementAssert
     */
    public WebElementAssert isDisabled() {
        isNotNull();

        try {
            if (actual.isEnabled()) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected element to be disabled but was enabled");
        }

        return this;
    }

    /**
     * Verifies that the element is invisible
     *
     * @return WebElementAssert
     */
    public WebElementAssert isInvisible() {
        isNotNull();

        boolean displayed;
        try {
            displayed = actual.isDisplayed();
        } catch (Exception | AssertionError ex) {
            displayed = false;
        }

        if (displayed) {
            failWithMessage("Expected element to be invisible but was visible");
        }

        return this;
    }

    /**
     * Verifies that the element is clickable (enabled &amp; displayed)
     *
     * @return WebElementAssert
     */
    public WebElementAssert isClickable() {
        isNotNull();

        try {
            if (!actual.isEnabled() || !actual.isDisplayed()) {
                failWithMessage("Expected element to be clickable but was NOT");
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to be clickable" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that the element has the specified attribute with specified value
     *
     * @return WebElementAssert
     */
    public WebElementAssert hasAttributeValue(String attr, String value) {
        isNotNull();

        try {
            if (!StringUtils.equals(actual.getAttribute(attr), value)) {
                failWithMessage("Expected element to have attr <%s> value as <%s> but it did NOT", attr, value);
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to have attr <%s> value as <%s>" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that the element is displayed before timeout
     *
     * @return WebElementAssert
     */
    public WebElementAssert isDisplayedWait() {
        isNotNull();

        boolean displayed;
        try {
            Utils.until(ExpectedConditions.visibilityOf(actual));
            displayed = true;
        } catch (Exception | AssertionError ex) {
            displayed = false;
        }

        if (!displayed) {
            failWithMessage("Expected element to be displayed before timeout but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the element is displayed before negative timeout
     *
     * @return WebElementAssert
     */
    public WebElementAssert isDisplayedFast() {
        isNotNull();

        boolean displayed;
        try {
            Utils.until(ExpectedConditions.visibilityOf(actual), true);
            displayed = true;
        } catch (Exception | AssertionError ex) {
            displayed = false;
        }

        if (!displayed) {
            failWithMessage("Expected element to be displayed before negative timeout");
        }

        return this;
    }

    /**
     * Verifies that the element is clickable before timeout
     *
     * @return WebElementAssert
     */
    public WebElementAssert isClickableWait() {
        isNotNull();

        boolean ready;
        try {
            Utils.until(ExpectedConditions.elementToBeClickable(actual));
            ready = true;
        } catch (Exception | AssertionError ex) {
            ready = false;
        }

        if (!ready) {
            failWithMessage("Expected element to be clickable before timeout but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the element is clickable before negative timeout
     *
     * @return WebElementAssert
     */
    public WebElementAssert isClickableFast() {
        isNotNull();

        boolean ready;
        try {
            Utils.until(ExpectedConditions.elementToBeClickable(actual), true);
            ready = true;
        } catch (Exception | AssertionError ex) {
            ready = false;
        }

        if (!ready) {
            failWithMessage("Expected element to be clickable before negative timeout");
        }

        return this;
    }

    /**
     * Verifies that the element is invisible before timeout
     *
     * @return WebElementAssert
     */
    public WebElementAssert isInvisibleWait() {
        isNotNull();

        boolean invisible;
        try {
            Utils.until(ExpectedConditions.invisibilityOf(actual));
            invisible = true;
        } catch (Exception | AssertionError ex) {
            invisible = false;
        }

        if (!invisible) {
            failWithMessage("Expected element to be invisible before timeout but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the element is invisible before negative timeout
     *
     * @return WebElementAssert
     */
    public WebElementAssert isInvisibleFast() {
        isNotNull();

        boolean invisible;
        try {
            Utils.until(ExpectedConditions.invisibilityOf(actual), true);
            invisible = true;
        } catch (Exception | AssertionError ex) {
            invisible = false;
        }

        if (!invisible) {
            failWithMessage("Expected element to be invisible before negative timeout");
        }

        return this;
    }

}
