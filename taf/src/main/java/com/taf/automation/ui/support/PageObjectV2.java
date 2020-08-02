package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.util.ExpectedConditionsUtil;
import com.taf.automation.ui.support.util.Utils;
import io.appium.java_client.AppiumDriver;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.AjaxVisibleElementLocatorFactory;
import ui.auto.core.pagecomponent.PageComponent;
import ui.auto.core.pagecomponent.WidgetFieldDecorator;
import ui.auto.core.support.PageObjectModel;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * This is an enhanced version of the PageObject that handles the JavascriptException thrown by geckodriver sometimes
 * instead of the StaleElementReferenceException.  Also, it has an initialization method to handle dynamic locators.
 */
public class PageObjectV2 extends PageObjectModel {
    protected <T extends PageComponentContext> PageObjectV2(T context) {
        initPage(context);
    }

    protected <T extends PageComponentContext> PageObjectV2(T context, String expectedUrl) {
        initPage(context, expectedUrl);
    }

    protected <T extends PageComponentContext> PageObjectV2(T context, boolean ajaxIsUsed) {
        initPage(context, ajaxIsUsed);
    }

    protected <T extends PageComponentContext> PageObjectV2(T context, String expectedUrl, boolean ajaxIsUsed) {
        initPage(context, expectedUrl, ajaxIsUsed);
    }

    protected PageObjectV2() {
        // Default Constructor
    }

    @Override
    public <T extends PageComponentContext> void initPage(T context) {
        initPage(context, true);
    }

    @Override
    public <T extends PageComponentContext> void initPage(T context, String expectedUrl) {
        initPage(context, expectedUrl, true);
    }

    @SuppressWarnings("squid:S2177")
    private <T extends PageComponentContext> void initPage(T context, String expectedUrl, boolean ajaxIsUsed) {
        initPage(context, ajaxIsUsed);
        if (expectedUrl != null) {
            waitForUrl(expectedUrl);
        }
    }

    @SuppressWarnings("squid:S00112")
    @Override
    public <T extends PageComponentContext> void initPage(T context, boolean ajaxIsUsed) {
        try {
            FieldUtils.writeField(this, "context", context, true);
            FieldUtils.writeField(this, "ajaxIsUsed", true, true);
            if (AppiumDriver.class.isAssignableFrom(context.getDriver().getClass())) {
                PageFactory.initElements(new WidgetFieldDecorator(context, this), this);
                return;
            }

            FieldUtils.writeField(this, "currentUrl", context.getDriver().getCurrentUrl(), true);
            if (ajaxIsUsed) {
                AjaxVisibleElementLocatorFactory ajaxVisibleElementLocatorFactory = new AjaxVisibleElementLocatorFactory(context.getDriver(), context.getAjaxTimeOut());
                PageFactory.initElements(new ComponentFieldDecoratorV2(ajaxVisibleElementLocatorFactory, this), this);
            } else {
                DefaultElementLocatorFactory defLocFactory = new DefaultElementLocatorFactory(context.getDriver());
                PageFactory.initElements(new ComponentFieldDecoratorV2(defLocFactory, this), this);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Initialize Page Object for dynamic locators
     *
     * @param context       - Context
     * @param substitutions - Substitutions map of keys/values
     * @param <T>           - Object Type
     */
    @SuppressWarnings("squid:S00112")
    public <T extends PageComponentContext> void initPage(T context, Map<String, String> substitutions) {
        try {
            FieldUtils.writeField(this, "context", context, true);
            FieldUtils.writeField(this, "ajaxIsUsed", true, true);
            FieldUtils.writeField(this, "currentUrl", context.getDriver().getCurrentUrl(), true);
            DynamicElementLocatorFactory dynamicFactory = new DynamicElementLocatorFactory(context.getDriver(), context.getAjaxTimeOut(), substitutions);
            ComponentFieldDecoratorV2 componentFieldDecorator = new ComponentFieldDecoratorV2(dynamicFactory, this);
            PageFactory.initElements(componentFieldDecorator, this);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Uses the substitutions map to replace the dynamic pieces of the locator for the specified field in the page object
     *
     * @param fieldName     - Field to get dynamic locator
     * @param substitutions - Substitutions map of keys/values
     * @return locator with dynamic pieces substituted
     */
    protected By getResolvedLocator(String fieldName, Map<String, String> substitutions) {
        Field field = FieldUtils.getField(this.getClass(), fieldName, true);
        DynamicAnnotations dynamicAnnotations = new DynamicAnnotations(field, substitutions);
        return dynamicAnnotations.buildBy();
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     * 2) If data is decrypted, then it will be reset to the encrypted value after being entered<BR>
     *
     * @param component - Component to set value and validate
     * @param decrypt   - true to decrypt the components data before entering it
     */
    protected void setElementValueV2(PageComponent component, boolean decrypt) {
        setElementValueV2(component, decrypt, DataTypes.Data, 3);
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     * 2) If data is decrypted, then it will be reset to the encrypted value after being entered<BR>
     *
     * @param component        - Component to set value and validate
     * @param decrypt          - true to decrypt the components data before entering it
     * @param validationMethod - Validation Method (if null then no validation is performed)
     * @param tries            - Number of attempts to set value &amp; validate
     */
    protected void setElementValueV2(PageComponent component, boolean decrypt, DataTypes validationMethod, int tries) {
        String enterData = component.getData(DataTypes.Data, true);
        String initialData = component.getData(DataTypes.Initial, true);
        String expectedData = component.getData(DataTypes.Expected, true);

        if (decrypt) {
            component.initializeData(new CryptoUtils().decrypt(enterData), initialData, expectedData);
        }

        try {
            setElementValueV2(component, validationMethod, tries);
        } finally {
            component.initializeData(enterData, initialData, expectedData);
        }
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     *
     * @param value     - Value to be set using the specified component
     * @param component - Component to be used
     */
    protected void setElementValueV2(String value, PageComponent component) {
        String restoreData = component.getData(DataTypes.Data, false);
        String restoreInitialData = component.getData(DataTypes.Initial, false);
        String restoreExpectedData = component.getData(DataTypes.Expected, false);
        try {
            component.initializeData(value, null, null);
            setElementValueV2(component);
        } finally {
            component.initializeData(restoreData, restoreInitialData, restoreExpectedData);
        }
    }

    /**
     * Set value (with truncation as necessary) and validate using component's validation method<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>Similar to the PageObject.setElementValue except validation occurs using component's validation method</LI>
     * <LI>If max length is less than or equal to zero, then no truncation will occur. This may lead to a validation failure</LI>
     * <LI>Truncation occurs if specified value length is greater than the max length</LI>
     * </OL>
     *
     * @param value     - Value to be set using the specified component
     * @param component - Component to be used
     * @param maxLength - Max Length for field
     */
    protected void setElementValueV2(String value, PageComponent component, int maxLength) {
        if (maxLength > 0 && StringUtils.length(value) > maxLength) {
            setElementValueV2(StringUtils.substring(value, 0, maxLength), component);
        } else {
            setElementValueV2(value, component);
        }
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     *
     * @param useComponent  - Component to be used to find the element and enter the data
     * @param dataComponent - Data is extracted from this component and injected into the use component
     */
    protected void setElementValueV2(PageComponent useComponent, PageComponent dataComponent) {
        String restoreData = useComponent.getData(DataTypes.Data, false);
        String restoreInitialData = useComponent.getData(DataTypes.Initial, false);
        String restoreExpectedData = useComponent.getData(DataTypes.Expected, false);
        try {
            String data = dataComponent.getData(DataTypes.Data, true);
            String initialData = dataComponent.getData(DataTypes.Initial, true);
            String expectedData = dataComponent.getData(DataTypes.Expected, true);
            useComponent.initializeData(data, initialData, expectedData);
            setElementValueV2(useComponent);
        } finally {
            useComponent.initializeData(restoreData, restoreInitialData, restoreExpectedData);
        }
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     *
     * @param component            - Component to set value and validate
     * @param actionBeforeSetValue - Lambda expression to run before setting value
     */
    protected void setElementValueV2(PageComponent component, final Runnable actionBeforeSetValue) {
        if (component == null || component.getData(DataTypes.Data, true) == null || component.getData(DataTypes.Data, true).isEmpty()) {
            return;
        }

        actionBeforeSetValue.run();
        setElementValueV2(component);
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     *
     * @param component - Component to set value and validate
     */
    protected void setElementValueV2(PageComponent component) {
        if (component == null || component.getData(DataTypes.Data, true) == null || component.getData(DataTypes.Data, true).isEmpty()) {
            return;
        }

        DataTypes validationMethod = (component.getData(DataTypes.Expected, true) != null) ? DataTypes.Expected : DataTypes.Data;
        setElementValueV2(component, validationMethod, 3);
    }

    /**
     * Set value and validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Similar to the PageObject.setElementValue except validation occurs using component's validation method<BR>
     *
     * @param component        - Component to set value and validate
     * @param validationMethod - Validation Method (if null then no validation is performed)
     * @param tries            - Number of attempts to set value &amp; validate
     */
    @SuppressWarnings("squid:S00112")
    protected void setElementValueV2(PageComponent component, DataTypes validationMethod, int tries) {
        if (component == null || component.getData(DataTypes.Data, true) == null || component.getData(DataTypes.Data, true).isEmpty()) {
            return;
        }

        String exception = "";
        int attempt = 0;
        boolean validated;
        do {
            Failsafe.with(Utils.getWriteValueRetryPolicy()).run(component::setValue);
            if (validationMethod != null) {
                try {
                    // Execute validation using the component specific logic
                    component.validateData(validationMethod);

                    // The method does not return true/false as such we are assuming that if no exception
                    // is thrown that validation was successful
                    validated = true;
                } catch (Exception | AssertionError ignore) {
                    // Catch any thrown error or assertion error
                    validated = false;
                    exception = ignore.getMessage();
                }
            } else {
                // No validation is necessary just set flag to be true
                validated = true;
            }

            if (validated) {
                return;
            }

            attempt++;
        } while (attempt < tries);

        String error = "Unable to set (" + component.getClass().getSimpleName() + ")"
                + " & validate (" + validationMethod + ")"
                + " after " + tries + " attempts:  " + exception;
        throw new RuntimeException(error);
    }

    /**
     * Fill and Validate Page Object Component
     *
     * @param component - Page Object component to fill
     */
    protected void setElementValueV2(ComponentPO component) {
        if (component == null || !component.hasData()) {
            return;
        }

        String exception = "";
        boolean successful;

        try {
            Failsafe.with(Utils.getWriteValueRetryPolicy()).run(() -> {
                component.fill();
                component.validate();
            });
            successful = true;
        } catch (Exception | AssertionError ignore) {
            // Catch any thrown error or assertion error
            successful = false;
            exception = ignore.getMessage();
        }

        String error = "Unable to fill (" + component.getClass().getSimpleName() + ")"
                + " & validate before timeout:  " + exception;
        assertThat(error, successful);
    }

    /**
     * Set the value of all the pageComponents(if displayed and enabled) to the specified data type.
     * This method will not modify the original values in the data types (initial, data, expected)
     *
     * @param components - list of components to be updated
     * @param dataType   - the data type to set the value to
     */
    protected void setElementValueToDataType(List<PageComponent> components, DataTypes dataType) {
        if (components == null || components.isEmpty()) {
            return;
        }

        switch (dataType) {
            case Data:
                components.forEach(c -> setElementValueToDataType(c, DataTypes.Data));
                break;
            case Initial:
                components.forEach(c -> setElementValueToDataType(c, DataTypes.Initial));
                break;
            case Expected:
                components.forEach(c -> setElementValueToDataType(c, DataTypes.Expected));
                break;
        }
    }

    /**
     * Set element value to the dataType specified while maintaining the original data sets (initial, data, expected) in place
     *
     * @param component - component to set the value
     * @param type      - DataType data to set to value
     */
    protected void setElementValueToDataType(PageComponent component, DataTypes type) {
        if (component == null || component.getData(type, true) == null || component.getData(type, true).isEmpty()) {
            return;
        }

        if (isDisplayed(component) && component.isEnabled()) {
            //storing the current values of the data so that they can be restored later
            String expected = component.getData(DataTypes.Expected, true);
            String initial = component.getData(DataTypes.Initial, true);
            String data = component.getData();

            //set component value to the specified data type
            component.initializeData(component.getData(type, true), null, null);
            setElementValueV2(component);

            //restore the component data to the original values
            component.initializeData(data, initial, expected);
        }
    }

    /**
     * Checks if the component is displayed
     *
     * @param component - Component
     * @return true if element is displayed else false
     */
    @SuppressWarnings("squid:S00112")
    protected boolean isDisplayed(PageComponent component) {
        By by = component.getLocator();
        if (by == null) {
            throw new RuntimeException("Given component doesn't have locator!");
        }

        List<WebElement> elements = getDriver().findElements(by);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Get Text
     *
     * @param component - Component
     * @return empty if component is not displayed (height=0) else the text of the component
     */
    protected String getText(PageComponent component) {
        return isDisplayed(component) ? component.getText() : "";
    }

    /**
     * Check if the page object can handle the current page<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>By default this method will return false.</LI>
     * <LI>If it is necessary to perform this check, then the extending page object should override this method.</LI>
     * </OL>
     *
     * @return true if the page object can handle the current page else false
     */
    public boolean atPage() {
        return false;
    }

    /**
     * Perform Accessibility Test on current page.  Only displayed elements are verified.
     *
     * @param pageName - Page Name for logging
     */
    public void performAccessibilityTest(String pageName) {
        ((TestContext) getContext()).getAccessibility().analyze(pageName);
    }

    /**
     * Perform Accessibility Test on current page for specified component.  Only displayed elements are verified.
     *
     * @param pageName      - Page Name for logging
     * @param componentName - Component Name for logging
     * @param component     - Component to get element to perform the accessibility test on
     */
    public void performAccessibilityTest(String pageName, String componentName, PageComponent component) {
        ((TestContext) getContext()).getAccessibility().analyze(pageName, componentName, component);
    }

    /**
     * Perform Accessibility Test on current page for specified element.  Only displayed elements are verified.
     *
     * @param pageName    - Page Name for logging
     * @param elementName - Element Name for logging
     * @param element     - Element to perform the accessibility test on
     */
    public void performAccessibilityTest(String pageName, String elementName, WebElement element) {
        ((TestContext) getContext()).getAccessibility().analyze(pageName, elementName, element);
    }

    /**
     * Copy all the data from the source into the destination PageComponent
     *
     * @param source      - PageComponent from which the data will be copied from
     * @param destination - PageComponent that will have it's data overridden
     */
    protected void copy(PageComponent source, PageComponent destination) {
        String data = source.getData(DataTypes.Data, false);
        String initialData = source.getData(DataTypes.Initial, false);
        String expectedData = source.getData(DataTypes.Expected, false);
        destination.initializeData(data, initialData, expectedData);
    }

    /**
     * Validate using component's validation method<BR>
     * <B>Notes:</B><BR>
     * 1) Validation is skipped if the component was not used to set the value<BR>
     * 2) Validation method is DataTypes.Expected if not null else DataTypes.Data<BR>
     *
     * @param component - Component to validate
     */
    protected void validateData(PageComponent component) {
        if (component == null || component.getData(DataTypes.Data, true) == null || component.getData(DataTypes.Data, true).isEmpty()) {
            // This indicates that the component was not used to set the value as such skip validation
            return;
        }

        DataTypes validationMethod = (component.getData(DataTypes.Expected, true) != null) ? DataTypes.Expected : DataTypes.Data;
        component.validateData(validationMethod);
    }

    /**
     * Click component when it is ready.  Method uses a retry policy to handle
     * click specific exceptions (ElementClickInterceptedException and ElementNotInteractableException) if they occur
     *
     * @param component - Component to click when ready
     * @return the element using component locator which in most cases will have been clicked but the component is
     * controlling the click logic and sometimes this may not be the case based on the component
     */
    protected WebElement click(PageComponent component) {
        assertThat("Click Component", component, notNullValue());
        assertThat("Click Component's Locator", component.getLocator(), notNullValue());
        return Failsafe.with(Utils.getClickRetryPolicy()).get(() -> {
            WebElement element = Utils.getWebDriverWait().until(ExpectedConditionsUtil.ready(component));
            component.click();
            return element;
        });
    }

}
