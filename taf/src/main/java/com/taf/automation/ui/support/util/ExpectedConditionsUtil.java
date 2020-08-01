package com.taf.automation.ui.support.util;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.conditional.Conditional;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.conditional.ResultType;
import com.taf.automation.ui.support.testng.Attachment;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * This class has custom ExpectedConditions and any new additions to the ExpectedConditions that are later than our current version.<BR>
 */
@SuppressWarnings("squid:S1168")
public class ExpectedConditionsUtil {
    private ExpectedConditionsUtil() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * An expectation for checking WebElement is ready (enabled &amp; displayed)
     *
     * @param element - Element to check if ready
     * @return Boolean true when element is ready else false
     */
    public static ExpectedCondition<Boolean> ready(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return element.isDisplayed() && element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to be ready (enabled & displayed)";
            }
        };
    }

    /**
     * An expectation for checking component is ready (enabled &amp; displayed)
     *
     * @param component - Component used to checked ready (enabled &amp; displayed)
     * @return non-null WebElement (using component locator) when component is ready else null
     */
    public static ExpectedCondition<WebElement> ready(PageComponent component) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver.findElements(component.getLocator());
                    if (!elements.isEmpty() && component.isDisplayed() && component.isEnabled()) {
                        return elements.get(0);
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "component to be ready (enabled & displayed) using locator:  " + component.getLocator();
            }
        };
    }

    /**
     * An expectation for checking WebElement found using the locator is ready (enabled &amp; displayed)
     *
     * @param locator - Locator used to find the element to check
     * @return non-null WebElement when element found using locator is ready else null
     */
    public static ExpectedCondition<WebElement> ready(final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(locator);
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "element to be ready (enabled & displayed) using locator:  " + locator;
            }
        };
    }

    /**
     * An expectation for checking WebElement found is ready (enabled &amp; displayed)
     *
     * @param anchor   - The anchor element from which the element is located from
     * @param relative - Used to find the relative element
     * @return non-null WebElement when element found is ready else null
     */
    public static ExpectedCondition<WebElement> ready(final WebElement anchor, final By relative) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = anchor.findElement(relative);
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "relative element to be ready (enabled & displayed) using relative locator:  " + relative;
            }
        };
    }

    /**
     * An expectation for checking number of WebElements with given locator from anchor element being greater than defined number
     *
     * @param anchor   - The anchor element from which the elements are located from
     * @param relative - Used to find the relative elements
     * @param number   - Wait until number of elements is greater than this value
     * @return Boolean true when size of elements list is greater than specified
     */
    public static ExpectedCondition<List<WebElement>> numberOfElementsToBeMoreThan(
            final WebElement anchor,
            final By relative,
            final Integer number
    ) {
        return new ExpectedCondition<List<WebElement>>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver driver) {
                List<WebElement> elements = anchor.findElements(relative);
                currentNumber = elements.size();
                return currentNumber > number ? elements : null;
            }

            @Override
            public String toString() {
                return String.format("number to be more than \"%s\". Current number: \"%s\"", number, currentNumber);
            }
        };
    }

    /**
     * An expectation for checking number of WebElements with given locator being greater than defined number
     * and matching the predicate<BR>
     * <B>Example to get all element with attribute that id starts with lang:</B><BR>
     * List<WebElement> all = Utils.getWebDriverWait().until(ExpectedConditionsUtil.numberOfElementsToBeMoreThan(
     * By.xpath("//input"),
     * 0,
     * e -> StringUtils.startsWith(e.getAttribute("id"), "lang"))
     * );
     *
     * @param locator - used to find the elements
     * @param number  - used to define minimum number of elements
     * @param include - predicate to check if the element is to be included
     * @return Boolean true when size of elements list is greater than specified and matching the predicate
     */
    public static ExpectedCondition<List<WebElement>> numberOfElementsToBeMoreThan(
            final By locator,
            final Integer number,
            final Predicate<WebElement> include
    ) {
        return new ExpectedCondition<List<WebElement>>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver driver) {
                List<WebElement> elementsThatMatchPredicate = new ArrayList<>();

                List<WebElement> elements = driver.findElements(locator);
                for (WebElement item : elements) {
                    if (include.test(item)) {
                        elementsThatMatchPredicate.add(item);
                    }
                }

                currentNumber = elementsThatMatchPredicate.size();
                return currentNumber > number ? elementsThatMatchPredicate : null;
            }

            @Override
            public String toString() {
                return String.format("number of elements to be more than %s with predicate. Current number: %s", number, currentNumber);
            }
        };
    }

    /**
     * An expectation for checking number of WebElements with given locator from anchor element being less than defined number
     *
     * @param anchor   - The anchor element from which the elements are located from
     * @param relative - Used to find the relative elements
     * @param number   - Wait until number of elements is less than this value
     * @return Boolean true when size of elements list is less than defined
     */
    public static ExpectedCondition<List<WebElement>> numberOfElementsToBeLessThan(
            final WebElement anchor,
            final By relative,
            final Integer number
    ) {
        return new ExpectedCondition<List<WebElement>>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver driver) {
                List<WebElement> elements = anchor.findElements(relative);
                currentNumber = elements.size();
                return currentNumber < number ? elements : null;
            }

            @Override
            public String toString() {
                return String.format("number to be less than \"%s\". Current number: \"%s\"", number, currentNumber);
            }
        };
    }

    /**
     * An expectation for checking number of WebElements with given locator from anchor element being equal to defined number
     *
     * @param anchor   - The anchor element from which the elements are located from
     * @param relative - Used to find the relative elements
     * @param number   - Wait until number of elements is equal to this value
     * @return Boolean true when size of elements list is equal to defined
     */
    public static ExpectedCondition<List<WebElement>> numberOfElementsToBe(
            final WebElement anchor,
            final By relative,
            final Integer number
    ) {
        return new ExpectedCondition<List<WebElement>>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver driver) {
                List<WebElement> elements = anchor.findElements(relative);
                currentNumber = elements.size();
                return currentNumber.equals(number) ? elements : null;
            }

            @Override
            public String toString() {
                return String.format("number to be \"%s\". Current number: \"%s\"", number, currentNumber);
            }
        };
    }

    /**
     * An expectation for checking number of WebElements with given locator being in the range
     *
     * @param locator - Used to find the elements
     * @param min     - Minimum number of elements expected (inclusive)
     * @param max     - Maximum number of elements expected (inclusive)
     * @return Boolean true when size of elements list is in defined range
     */
    public static ExpectedCondition<List<WebElement>> numberOfElementsToBeInRange(
            final By locator,
            final Integer min,
            final Integer max
    ) {
        return new ExpectedCondition<List<WebElement>>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver webDriver) {
                List<WebElement> elements = webDriver.findElements(locator);
                currentNumber = elements.size();
                if (min <= currentNumber && currentNumber <= max) {
                    return elements;
                } else {
                    return null;
                }
            }

            @Override
            public String toString() {
                String error = "waiting for number of elements to be in range [%s, %s]. Current number:  %s";
                return String.format(error, min, max, currentNumber);
            }
        };
    }

    /**
     * An expectation that the JavaScript will return true
     *
     * @param javaScript - JavaScript to be executed (and needs to return true/false)
     * @return true when the JavaScript returns true
     */
    public static ExpectedCondition<Boolean> jsReturnsResult(final String javaScript) {
        return jsReturnsResult(javaScript, true);
    }

    /**
     * An expectation that the JavaScript will return a boolean value that matches the expected result
     *
     * @param javaScript     - JavaScript to be executed (and needs to return true/false)
     * @param expectedResult - Expected Result of executing the JavaScript
     * @return true when the JavaScript returns the expected result
     */
    public static ExpectedCondition<Boolean> jsReturnsResult(final String javaScript, final boolean expectedResult) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    Boolean actualResult = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);
                    return actualResult == expectedResult;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("js result (%s) using script:  %s", expectedResult, javaScript);
            }
        };
    }

    /**
     * An expectation that a screenshot can be taken<BR>
     * <B>Note: </B>If there is an alert, then the alert is dismissed and this will also be an attachment<BR>
     *
     * @param title - Title for the screenshot
     * @return List of attachments
     */
    public static ExpectedCondition<List<Attachment>> takeScreenshot(final String title) {
        return takeScreenshot(title, TestProperties.getInstance().isViewPortOnly());
    }

    /**
     * An expectation that a screenshot can be taken<BR>
     * <B>Note: </B>If there is an alert, then the alert is dismissed and this will also be an attachment<BR>
     *
     * @param title        - Title for the screenshot
     * @param viewPortOnly - true to only take screenshot of the view port, false to attempt full screenshot
     * @return List of attachments
     */
    @SuppressWarnings({"squid:S1141", "squid:S00112"})
    public static ExpectedCondition<List<Attachment>> takeScreenshot(final String title, final boolean viewPortOnly) {
        return new ExpectedCondition<List<Attachment>>() {
            @Override
            public List<Attachment> apply(WebDriver driver) {
                try {
                    boolean appium = AppiumDriver.class.isAssignableFrom(driver.getClass());

                    List<Criteria> criteria = new ArrayList<>();
                    criteria.add(CriteriaMaker.forAlertDismiss());
                    criteria.add(CriteriaMaker.forUrlRegEx(".*"));
                    criteria.add(CriteriaMaker.forLambdaExpression(() -> appium));
                    Conditional conditional = new Conditional(driver);
                    int index = conditional.match(criteria);
                    if (index < 0) {
                        return null;
                    }

                    List<Attachment> screenshots = new ArrayList<>();
                    if (index == 0) {
                        String alertText = (String) conditional.getResultInfo().getAdditionalInfo().get(ResultType.VALUE);
                        Attachment alertAttachment = new Attachment()
                                .withTitle("Alert on " + title)
                                .withType("text/plain")
                                .withFile(alertText.getBytes());
                        screenshots.add(alertAttachment);
                    }

                    byte[] attachment;
                    String append = "";

                    try {
                        if (viewPortOnly || appium) {
                            throw new Exception("User wants view port only screenshot");
                        }

                        attachment = ScreenshotUtil.getFullScreenshotAs(driver);
                    } catch (Exception ex) {
                        // Fall back to only visible view port if cannot get all the partial screenshots
                        attachment = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        append = "_ViewPortOnly";
                    }

                    Attachment jpeg = new Attachment()
                            .withTitle(title + append)
                            .withType("image/jpeg")
                            .withFile(ScreenshotUtil.convertPngToJpg(attachment));
                    screenshots.add(jpeg);
                    return screenshots;
                } catch (Exception ex) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return "screenshot to be taken";
            }
        };
    }

    /**
     * An expectation for checking number of drop down options for the given Select element found using the
     * specified locator being greater than defined number
     *
     * @param dropDownLocator - The locator to get the drop down element to check the available options
     * @param number          - Wait until number of drop down options is greater than this value
     * @return List of options from the drop down
     */
    public static ExpectedCondition<List<WebElement>> numberOfOptionsToBeMoreThan(final By dropDownLocator, final Integer number) {
        return new ExpectedCondition<List<WebElement>>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(dropDownLocator);
                    Select selectElement = new Select(element);
                    List<WebElement> elements = selectElement.getOptions();
                    currentNumber = elements.size();
                    return currentNumber > number ? elements : null;
                } catch (Exception ex) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return String.format("number to be more than \"%s\". Current number: \"%s\"", number, currentNumber);
            }
        };
    }

    /**
     * An expectation that the locator will return the same number of items<BR>
     * <B>Note: </B>If JavaScript is updating the DOM and you don't know when it will complete, then method
     * assumes that it is complete once the number of items remains the same after a pause for JavaScript processing<BR>
     *
     * @param locator - Locator to find the items
     * @return List&lt;WebElement&gt;
     */
    public static ExpectedCondition<List<WebElement>> numberOfItemsToRemainSame(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(WebDriver driver) {
                List<WebElement> initial = driver.findElements(locator);
                Utils.delayForJavaScriptProcessing();
                List<WebElement> current = driver.findElements(locator);
                if (initial.size() == current.size()) {
                    return current;
                }

                return null;
            }

            @Override
            public String toString() {
                return "the items to not be in flux with locator:  " + locator;
            }
        };
    }

    /**
     * An expectation for checking that an element is present on the DOM of a page.
     * This does not necessarily mean that the element is visible.
     *
     * @param component - Component used to get locator
     * @return the WebElement once it is located
     */
    public static ExpectedCondition<WebElement> presenceOfElementLocated(PageComponent component) {
        return ExpectedConditions.presenceOfElementLocated(component.getLocator());
    }

    /**
     * An expectation for getting window handle of 1st new window that appears after triggering the action<BR><BR>
     * <B>Notes:</B><BR>
     * The action to trigger the new window is only executed once.<BR><BR>
     * <B>Example actionToTriggerNewWindow value:</B><BR>
     * () -&gt; element.click()<BR>
     *
     * @param actionToTriggerNewWindow - Lambda expression to trigger new window
     * @return window handle of 1st new window that appears
     */
    public static ExpectedCondition<String> handle(final Runnable actionToTriggerNewWindow) {
        return new ExpectedCondition<String>() {
            private Set<String> existing;
            private boolean firstTime = true;

            @Override
            public String apply(WebDriver driver) {
                try {
                    if (firstTime) {
                        firstTime = false;
                        existing = driver.getWindowHandles();
                        actionToTriggerNewWindow.run();
                    }

                    if (driver.getWindowHandles().size() > existing.size()) {
                        String handle = Utils.findNewWindowHandle(existing);
                        if (StringUtils.isNotEmpty(handle)) {
                            return handle;
                        }
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "new window to appear and have non-empty handle";
            }
        };
    }

    /**
     * An expectation for getting window handle of 1st new window that appears after triggering the action<BR><BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>The action to trigger the new window is only executed once.</LI>
     * <LI>This method attempts to handle case in which there is only 1 window opened but the handle changes for
     * some unknown reason.  (It could be that the site is checking for a pop-up blocker and new window is opened
     * and closed so fast that it is not visible to the user before opening the desired window.</LI>
     * <LI>Because this method is checking for another handle, if there are multiple windows opened, then the handle to
     * the second window opened will always be return.</LI>
     * </OL>
     * <B>Example actionToTriggerNewWindow value:</B><BR>
     * () -&gt; element.click()<BR>
     *
     * @param actionToTriggerNewWindow - Lambda expression to trigger new window
     * @return window handle of 1st new window that appears
     */
    public static ExpectedCondition<String> handles(final Runnable actionToTriggerNewWindow) {
        return new ExpectedCondition<String>() {
            private Set<String> existing;
            private boolean firstTime = true;

            @Override
            public String apply(WebDriver driver) {
                try {
                    if (firstTime) {
                        firstTime = false;
                        existing = driver.getWindowHandles();
                        actionToTriggerNewWindow.run();
                    }

                    if (driver.getWindowHandles().size() > existing.size()) {
                        String firstFoundHandle = Utils.findNewWindowHandle(existing);
                        if (StringUtils.isNotEmpty(firstFoundHandle)) {
                            // Give time for handle to change
                            Utils.delayForJavaScriptProcessing();

                            // Add the first found handle to the list of exclusions
                            existing.add(firstFoundHandle);

                            // Check if there is another handle now
                            String anotherHandle = Utils.findNewWindowHandle(existing);
                            return (anotherHandle == null) ? firstFoundHandle : anotherHandle;
                        }
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "new window to appear and have non-empty handle";
            }
        };
    }

    /**
     * An expectation for the URL to change after triggering action<BR><BR>
     * <B>Notes:</B><BR>
     * The action to trigger the URL change is only executed once.<BR><BR>
     * <B>Example actionToTriggerUrlChange value:</B><BR>
     * () -&gt; element.click()<BR>
     *
     * @param actionToTriggerUrlChange - Lambda expression to trigger URL change
     * @return <code>true</code> when the URL has changed from initial value
     */
    public static ExpectedCondition<Boolean> urlToChange(final Runnable actionToTriggerUrlChange) {
        return new ExpectedCondition<Boolean>() {
            private boolean firstTime = true;
            private String initialUrl = "";
            private String currentUrl = "";

            @Override
            public Boolean apply(WebDriver driver) {
                if (firstTime) {
                    firstTime = false;
                    initialUrl = driver.getCurrentUrl();
                    actionToTriggerUrlChange.run();
                }

                currentUrl = driver.getCurrentUrl();
                return !StringUtils.contains(currentUrl, initialUrl);
            }

            @Override
            public String toString() {
                return String.format("url to change from \"%s\". Current url: \"%s\"", initialUrl, currentUrl);
            }
        };
    }

    /**
     * An expectation for checking child WebElement as a part of parent element to be visible
     *
     * @param element      used as parent element. For example table with locator By.xpath("//table")
     * @param childLocator used to find child element. For example td By.xpath("./tr/td")
     * @return visible subelement
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfAllNestedElementsLocatedBy(final WebElement element, final By childLocator) {
        return new ExpectedCondition<List<WebElement>>() {

            @Override
            public List<WebElement> apply(WebDriver webDriver) {
                List<WebElement> allChildren = element.findElements(childLocator);

                if (!allChildren.isEmpty()) {
                    // Ensure all child elements are displayed
                    for (WebElement child : allChildren) {
                        try {
                            if (!child.isDisplayed()) {
                                return null;
                            }
                        } catch (Exception ex) {
                            return null;
                        }
                    }

                    return allChildren;
                }

                return null;
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s -> %s", element, childLocator);
            }
        };
    }

    /**
     * An expectation for checking that a visible element is found using the locator to get all elements.
     * Visibility means that the element is not only displayed but also has a height and width that is
     * greater than 0.
     *
     * @param locator used to find the element
     * @return the 1st WebElement that is visible using the locator
     */
    public static ExpectedCondition<WebElement> visibilityOfFirstElementLocatedBy(final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    for (WebElement element : elements) {
                        if (element.isDisplayed()) {
                            return element;
                        }
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "could not find any element that was visible with locator:  " + locator;
            }
        };
    }

    /**
     * An expectation for checking all elements found by the locator are invisible
     *
     * @param locator - Locator to find the elements to check are invisible
     * @return true when all elements are invisible
     */
    public static ExpectedCondition<Boolean> invisibilityOfAllElements(final By locator) {
        return new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                // Check that all elements are invisible
                if (!isAllInvisible(driver)) {
                    return false;
                }

                // Delay before checking again to limit issues where the element becomes visible again
                Utils.sleep(1000);

                // All elements have remained invisible for at least 1 second in a row
                return isAllInvisible(driver);
            }

            private boolean isAllInvisible(WebDriver driver) {
                List<WebElement> elements = driver.findElements(locator);
                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed()) {
                            return false;
                        }
                    } catch (Exception ignore) {
                        // Consider stale element as invisible
                    }
                }

                return true;
            }

            @Override
            public String toString() {
                return String.format("invisibility of elements located by %s", locator);
            }
        };
    }

    /**
     * Expectation for the URL to match any of the regular expressions
     *
     * @param regex the regular expressions to match the URL against
     * @return <code>true</code> if the URL matches any regular expression
     */
    public static ExpectedCondition<Boolean> urlMatchesAny(final String... regex) {
        return new ExpectedCondition<Boolean>() {
            private String currentUrl;
            private int debuggingCode;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    boolean match = false;
                    debuggingCode = 0;
                    currentUrl = driver.getCurrentUrl();
                    debuggingCode = 1;
                    for (String value : regex) {
                        debuggingCode = 2;
                        boolean eval = currentUrl.matches(value);
                        debuggingCode = 3;
                        if (eval) {
                            match = true;
                            break;
                        }
                    }

                    return match;
                } catch (Exception ex) {
                    return false;
                }
            }

            @Override
            public String toString() {
                String debugInfo;
                if (debuggingCode == 0) {
                    debugInfo = ".  (Getting Current URL threw exception)";
                } else if (debuggingCode == 1) {
                    debugInfo = ".  (There were no regular expressions)";
                } else if (debuggingCode == 2) {
                    debugInfo = ".  (There was an invalid regular expression)";
                } else {
                    debugInfo = ":  " + Arrays.asList(regex).toString();
                }

                return "url to match any of the regular expressions"
                        + debugInfo
                        + "  Current URL:  " + currentUrl;
            }
        };
    }

    /**
     * An expectation for checking component is enabled<BR>
     * <B>Note: </B> If using the framework to get the component, then component must always be displayed
     * which makes this work the same as the component ready expectation even though there is no explicit check
     * for displayed<BR>
     *
     * @param component - Component used to checked enabled
     * @return non-null WebElement (using component locator) when component is enabled else null
     */
    public static ExpectedCondition<WebElement> enabled(PageComponent component) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver.findElements(component.getLocator());
                    if (!elements.isEmpty() && component.isEnabled()) {
                        return elements.get(0);
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "component to be enabled using locator:  " + component.getLocator();
            }
        };
    }

    /**
     * An expectation for checking component is displayed
     *
     * @param component - Component used to checked displayed
     * @return non-null WebElement (using component locator) when component is displayed else null
     */
    public static ExpectedCondition<WebElement> displayed(PageComponent component) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver.findElements(component.getLocator());
                    if (!elements.isEmpty() && component.isDisplayed()) {
                        return elements.get(0);
                    }
                } catch (Exception ex) {
                    //
                }

                return null;
            }

            @Override
            public String toString() {
                return "component to be displayed using locator:  " + component.getLocator();
            }
        };
    }

}
