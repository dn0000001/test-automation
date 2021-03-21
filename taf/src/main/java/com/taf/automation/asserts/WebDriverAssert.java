package com.taf.automation.asserts;

import com.taf.automation.ui.support.util.Utils;
import org.assertj.core.api.AbstractAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("java:S5803")
public class WebDriverAssert extends AbstractAssert<WebDriverAssert, WebDriver> {
    private static final String EXCEPTION_OCCURRED = " but the following exception occurred validating:  %s";

    public WebDriverAssert(WebDriver driver) {
        super(driver, WebDriverAssert.class);
    }

    public static WebDriverAssert assertThat(WebDriver driver) {
        return new WebDriverAssert(driver);
    }

    private WebElement getWebElement(By locator) {
        WebElement element;
        try {
            element = actual.findElement(locator);
        } catch (Exception ex) {
            element = null;
        }

        if (element == null) {
            failWithMessage("Could not find element with locator:  %s", locator);
        }

        return element;
    }

    /**
     * Verifies that using the locator an element is found and it is displayed
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    @SuppressWarnings("java:S2259")
    public WebDriverAssert isDisplayed(By locator) {
        isNotNull();

        WebElement element = getWebElement(locator);
        try {
            if (!element.isDisplayed()) {
                failWithMessage("Expected element to be displayed but was NOT");
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to be displayed" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that using the locator an element is found and it is enabled
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    @SuppressWarnings("java:S2259")
    public WebDriverAssert isEnabled(By locator) {
        isNotNull();

        WebElement element = getWebElement(locator);
        try {
            if (!element.isEnabled()) {
                failWithMessage("Expected element to be enabled but was NOT");
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to be enabled" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that using the locator an element is found and it is clickable (enabled &amp; displayed)
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    @SuppressWarnings("java:S2259")
    public WebDriverAssert isClickable(By locator) {
        isNotNull();

        WebElement element = getWebElement(locator);
        try {
            if (!element.isEnabled() || !element.isDisplayed()) {
                failWithMessage("Expected element to be clickable but was NOT");
            }
        } catch (Exception ex) {
            // Catch any WebDriver exception like a stale element exception
            failWithMessage("Expected element to be clickable" + EXCEPTION_OCCURRED, ex.getMessage());
        }

        return this;
    }

    /**
     * Verifies that using the locator an element is found and it is displayed before timeout
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    public WebDriverAssert isDisplayedWait(By locator) {
        isNotNull();

        boolean displayed;
        try {
            Utils.until(ExpectedConditions.visibilityOfElementLocated(locator));
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
     * Verifies that using the locator an element is found and it is displayed before negative timeout
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    public WebDriverAssert isDisplayedFast(By locator) {
        isNotNull();

        boolean displayed;
        try {
            Utils.until(ExpectedConditions.visibilityOfElementLocated(locator), true);
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
     * Verifies that using the locator an element is found and it is clickable before timeout
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    public WebDriverAssert isClickableWait(By locator) {
        isNotNull();

        boolean ready;
        try {
            Utils.until(ExpectedConditions.elementToBeClickable(locator));
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
     * Verifies that using the locator an element is found and it is clickable before negative timeout
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    public WebDriverAssert isClickableFast(By locator) {
        isNotNull();

        boolean ready;
        try {
            Utils.until(ExpectedConditions.elementToBeClickable(locator), true);
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
     * Verifies that either the locator finds an element and it is invisible before timeout OR
     * the element is not found before timeout
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    public WebDriverAssert isInvisibleWait(By locator) {
        isNotNull();

        boolean invisible;
        try {
            Utils.until(ExpectedConditions.invisibilityOfElementLocated(locator));
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
     * Verifies that either the locator finds an element and it is invisible before negative timeout OR
     * the element is not found before negative timeout
     *
     * @param locator - Locator to find element
     * @return WebDriverAssert
     */
    public WebDriverAssert isInvisibleFast(By locator) {
        isNotNull();

        boolean invisible;
        try {
            Utils.until(ExpectedConditions.invisibilityOfElementLocated(locator), true);
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
