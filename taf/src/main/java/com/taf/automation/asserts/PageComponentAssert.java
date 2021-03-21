package com.taf.automation.asserts;

import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.util.ExpectedConditionsUtil;
import com.taf.automation.ui.support.util.LocatorUtils;
import com.taf.automation.ui.support.util.Utils;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SuppressWarnings("java:S5803")
public class PageComponentAssert extends AbstractAssert<PageComponentAssert, PageComponent> {
    public PageComponentAssert(PageComponent actual) {
        super(actual, PageComponentAssert.class);
    }

    public static PageComponentAssert assertThat(PageComponent actual) {
        return new PageComponentAssert(actual);
    }

    /**
     * Verifies that the component is displayed
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isDisplayed() {
        isNotNull();

        try {
            if (!actual.isDisplayed()) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected component to be displayed but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the component is enabled
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isEnabled() {
        isNotNull();

        try {
            if (!actual.isEnabled()) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected component to be enabled but was disabled");
        }

        return this;
    }

    /**
     * Verifies that the component is disabled
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isDisabled() {
        isNotNull();

        try {
            if (actual.isEnabled()) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected component to be disabled but was enabled");
        }

        return this;
    }

    /**
     * Verifies that the component is invisible
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isInvisible() {
        isNotNull();

        boolean displayed;
        try {
            displayed = actual.isDisplayed();
        } catch (Exception | AssertionError ex) {
            displayed = false;
        }

        if (displayed) {
            failWithMessage("Expected component to be invisible but was visible");
        }

        return this;
    }

    /**
     * Verifies that the component is clickable (enabled &amp; displayed)
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isClickable() {
        isNotNull();

        try {
            if (!actual.isEnabled() || !actual.isDisplayed()) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected component to be clickable but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the component is NOT clickable
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isNotClickable() {
        isNotNull();

        try {
            if (actual.isEnabled() && actual.isDisplayed()) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected component to not be clickable but was it clickable");
        }

        return this;
    }

    /**
     * Verifies that the component has the specified attribute with specified value
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert hasAttributeValue(String attr, String value) {
        isNotNull();

        try {
            if (!StringUtils.equals(actual.getAttribute(attr), value)) {
                throw new WebDriverException();
            }
        } catch (Exception | AssertionError ex) {
            failWithMessage("Expected component to have attr <%s> value as <%s> but it did NOT", attr, value);
        }

        return this;
    }

    /**
     * Verifies that the component is displayed before timeout
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isDisplayedWait() {
        isNotNull();

        boolean displayed;
        try {
            Utils.until(ExpectedConditionsUtil.displayed(actual));
            displayed = true;
        } catch (Exception | AssertionError ex) {
            displayed = false;
        }

        if (!displayed) {
            failWithMessage("Expected component to be displayed before timeout but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the component is displayed before negative timeout
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isDisplayedFast() {
        isNotNull();

        boolean displayed;
        try {
            Utils.until(ExpectedConditionsUtil.displayed(actual), true);
            displayed = true;
        } catch (Exception | AssertionError ex) {
            displayed = false;
        }

        if (!displayed) {
            failWithMessage("Expected component to be displayed before negative timeout");
        }

        return this;
    }

    /**
     * Verifies that the component is clickable before timeout
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isClickableWait() {
        isNotNull();

        boolean ready;
        try {
            Utils.until(ExpectedConditionsUtil.ready(actual));
            ready = true;
        } catch (Exception | AssertionError ex) {
            ready = false;
        }

        if (!ready) {
            failWithMessage("Expected component to be clickable before timeout but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the component is clickable before negative timeout
     *
     * @return PageComponentAssert
     */
    public PageComponentAssert isClickableFast() {
        isNotNull();

        boolean ready;
        try {
            Utils.until(ExpectedConditionsUtil.ready(actual), true);
            ready = true;
        } catch (Exception | AssertionError ex) {
            ready = false;
        }

        if (!ready) {
            failWithMessage("Expected component to be clickable before negative timeout");
        }

        return this;
    }

    /**
     * Verifies that the component is invisible before timeout
     *
     * @param driver - WebDriver used to get the core element using the component's locator
     * @return PageComponentAssert
     */
    public PageComponentAssert isInvisibleWait(WebDriver driver) {
        isNotNull();

        boolean invisible;
        try {
            invisible = Failsafe.with(Utils.getPollingRetryPolicy()).get(() -> returnWhenInvisible(driver));
        } catch (Exception | AssertionError ex) {
            invisible = false;
        }

        if (!invisible) {
            failWithMessage("Expected element to be invisible before timeout");
        }

        return this;
    }

    /**
     * Verifies that the component is invisible before negative timeout
     *
     * @param driver - WebDriver used to get the core element using the component's locator
     * @return PageComponentAssert
     */
    public PageComponentAssert isInvisibleFast(WebDriver driver) {
        isNotNull();

        boolean invisible;
        try {
            invisible = Failsafe.with(Utils.getNegativePollingRetryPolicy()).get(() -> returnWhenInvisible(driver));
        } catch (Exception | AssertionError ex) {
            invisible = false;
        }

        if (!invisible) {
            failWithMessage("Expected element to be invisible before negative timeout");
        }

        return this;
    }

    private boolean returnWhenInvisible(WebDriver driver) {
        // For performance, check if the element can be found first
        WebElement coreElement;
        try {
            coreElement = driver.findElement(actual.getLocator());
        } catch (Exception ex) {
            coreElement = null;
        }

        if (coreElement == null) {
            return true;
        }

        // For performance, ensure the core element is not displayed, then use the component
        // specific method to ensure the component is not displayed.
        if (!coreElement.isDisplayed() && !actual.isDisplayed()) {
            return true;
        }

        Assertions.fail("Component was visible");
        return false;
    }

    /**
     * Verifies that the component is not clickable before timeout
     *
     * @param driver - WebDriver used to get the core element using the component's locator
     * @return PageComponentAssert
     */
    public PageComponentAssert isNotClickableWait(WebDriver driver) {
        isNotNull();

        boolean success;
        try {
            success = Failsafe.with(Utils.getPollingRetryPolicy()).get(() -> returnWhenNotClickable(driver));
        } catch (Exception | AssertionError ex) {
            success = false;
        }

        if (!success) {
            failWithMessage("Expected component to be not clickable before timeout but was NOT");
        }

        return this;
    }

    /**
     * Verifies that the component is clickable before negative timeout
     *
     * @param driver - WebDriver used to get the core element using the component's locator
     * @return PageComponentAssert
     */
    public PageComponentAssert isNotClickableFast(WebDriver driver) {
        isNotNull();

        boolean success;
        try {
            success = Failsafe.with(Utils.getNegativePollingRetryPolicy()).get(() -> returnWhenNotClickable(driver));
        } catch (Exception | AssertionError ex) {
            success = false;
        }

        if (!success) {
            failWithMessage("Expected component to be not clickable before negative timeout");
        }

        return this;
    }

    private boolean returnWhenNotClickable(WebDriver driver) {
        // For performance, ensure the core element is displayed, then use the component
        // specific method to check if the component is ready.
        WebElement coreElement = driver.findElement(actual.getLocator());
        if (coreElement.isDisplayed() && actual.isDisplayed() && actual.isEnabled()) {
            Assertions.fail("Component was clickable");
        }

        return true;
    }

    /**
     * Verifies that the component is disabled before timeout
     *
     * @param driver - WebDriver used to get the core element using the component's locator
     * @return PageComponentAssert
     */
    public PageComponentAssert isDisabledWait(WebDriver driver) {
        isNotNull();

        boolean success;
        try {
            success = Failsafe.with(Utils.getPollingRetryPolicy()).get(() -> returnWhenDisabled(driver));
        } catch (Exception | AssertionError ex) {
            success = false;
        }

        if (!success) {
            failWithMessage("Expected component to be disabled before timeout but it was enabled");
        }

        return this;
    }

    /**
     * Verifies that the component is disabled before negative timeout
     *
     * @param driver - WebDriver used to get the core element using the component's locator
     * @return PageComponentAssert
     */
    public PageComponentAssert isDisabledFast(WebDriver driver) {
        isNotNull();

        boolean success;
        try {
            success = Failsafe.with(Utils.getNegativePollingRetryPolicy()).get(() -> returnWhenDisabled(driver));
        } catch (Exception | AssertionError ex) {
            success = false;
        }

        if (!success) {
            failWithMessage("Expected component to be disabled before negative timeout but it was enabled");
        }

        return this;
    }

    @SuppressWarnings("java:S2259")
    private boolean returnWhenDisabled(WebDriver driver) {
        // For performance, check if the element can be found first
        WebElement coreElement;
        try {
            coreElement = driver.findElement(actual.getLocator());
        } catch (Exception ex) {
            coreElement = null;
        }

        if (coreElement == null) {
            Assertions.fail("Element could not be found");
        }

        // For performance, ensure the core element is disabled, then use the component
        // specific method to ensure the component is disabled.
        if (!coreElement.isEnabled() && !actual.isEnabled()) {
            return true;
        }

        Assertions.fail("Component was enabled");
        return false;
    }

    /**
     * Verifies that the component cannot be set using the specified value<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>This method does not consider validation.
     * So, as long as the value can be set this is a failure even if the validation would fail.</LI>
     * <LI>This method uses setElementValueV2 to set the value.
     * If the setElementValueV2 method is successful, then this is considered a failure.  So, on unexpected failures
     * you should check that the component is not checking if the value is already entered which could be considered a
     * successful setting of the component</LI>
     * <LI>This method should only be used to valid that an option cannot be set because it is not valid
     * and not that it is disabled</LI>
     * </OL>
     *
     * @param valueToUse - Value to use when attempting to set the component
     * @return PageComponentAssert
     */
    @SuppressWarnings("java:S110")
    public PageComponentAssert cannotBeSet(String valueToUse) {
        isNotNull();

        String restoreData = null;
        String restoreInitialData = null;
        String restoreExpectedData = null;

        try {
            restoreData = actual.getData(DataTypes.Data, false);
            restoreInitialData = actual.getData(DataTypes.Initial, false);
            restoreExpectedData = actual.getData(DataTypes.Expected, false);
        } catch (Exception ex) {
            // Could not get the restore data.  So just consider this a fail
            failWithMessage("Could not get the restore data from the component");
        }

        boolean successful;
        try {
            // Initialize component data for the test
            actual.initializeData(valueToUse, null, null);

            // This is a workaround to access to a page object with implementing or using an existing one
            ComponentPO page = new ComponentPO() {
                @Override
                public boolean hasData() {
                    return false;
                }

                @Override
                public void fill() {
                    setElementValueV2(actual, null, 0);
                }

                @Override
                public void validate() {
                    //
                }
            };

            // This should fail as component cannot be set
            page.fill();

            // If we reach here, then component could be set and this is a failure
            successful = true;
        } catch (Exception | AssertionError ex) {
            successful = false;
        } finally {
            actual.initializeData(restoreData, restoreInitialData, restoreExpectedData);
        }

        if (successful) {
            failWithMessage("Able to be set value for component but it should not have been allowed");
        }

        return this;
    }

    public PageComponentAssert cannotBeSetFast(String valueToUse) {
        return cannotBeSet(valueToUse, doNothing -> { });
    }

    public PageComponentAssert cannotBeSet(String valueToUse, Consumer<PageComponent> actionBeforeSetValue) {
        return cannotBeSet(valueToUse, actionBeforeSetValue, 3);
    }

    public PageComponentAssert cannotBeSet(String valueToUse, Consumer<PageComponent> actionBeforeSetValue, int attempts) {
        isNotNull();

        boolean successful = true;
        int max = Math.max(1, attempts);
        for (int i = 0; i < max; i++) {
            successful = canSetComponent(valueToUse, actual, actionBeforeSetValue);
            if (successful) {
                break;
            }
        }

        if (successful) {
            failWithMessage("Able to be set value for component but it should not have been allowed");
        }

        return this;
    }

    @SuppressWarnings("java:S2259")
    private boolean canSetComponent(String valueToUse, PageComponent component, Consumer<PageComponent> actionBeforeSetValue) {
        PageComponent temp = getTempComponent(component);
        try {
            // Initialize component data for the test
            temp.initializeData(valueToUse, null, null);

            // Set Locator for AJAX components
            LocatorUtils.setLocator(temp, component.getLocator());

            // Execute Custom configuration of the component
            actionBeforeSetValue.accept(temp);

            // This should fail as component cannot be set
            temp.setValue();

            // If we reach here, then component could be set
            return true;
        } catch (Exception | AssertionError ex) {
            // Component could not be set
            return false;
        }
    }

    @SuppressWarnings("java:S3011")
    private PageComponent getTempComponent(final PageComponent component) {
        PageComponent temp;
        try {
            WebElement core = Utils.until(ExpectedConditions.visibilityOfElementLocated(component.getLocator()), true);
            temp = ConstructorUtils.invokeConstructor(component.getClass());
            Method m = PageComponent.class.getDeclaredMethod("initComponent", WebElement.class);
            m.setAccessible(true);
            m.invoke(temp, core);
        } catch (Exception ex) {
            temp = null;
        }

        if (temp == null) {
            failWithMessage("Unable to create temp component & initialize for set value");
        }

        return temp;
    }

}
