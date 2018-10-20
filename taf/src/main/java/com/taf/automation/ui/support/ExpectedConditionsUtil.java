package com.taf.automation.ui.support;

import com.taf.automation.ui.support.conditional.Conditional;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.conditional.ResultType;
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
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class has custom ExpectedConditions and any new additions to the ExpectedConditions that are later than our current version.<BR>
 */
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
     * An expectation for checking WebElement found using the component's locator is ready (enabled &amp; displayed)
     *
     * @param component - Component used to get locator
     * @return non-null WebElement when element found using locator is ready else null
     */
    public static ExpectedCondition<WebElement> ready(PageComponent component) {
        return ready(component.getLocator());
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
    public static ExpectedCondition<List<MakeAttachmentEvent>> takeScreenshot(final String title) {
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
    public static ExpectedCondition<List<MakeAttachmentEvent>> takeScreenshot(final String title, final boolean viewPortOnly) {
        return new ExpectedCondition<List<MakeAttachmentEvent>>() {
            @Override
            public List<MakeAttachmentEvent> apply(WebDriver driver) {
                try {
                    List<Criteria> criteria = new ArrayList<>();
                    criteria.add(CriteriaMaker.forAlertDismiss());
                    criteria.add(CriteriaMaker.forUrlRegEx(".*"));
                    Conditional conditional = new Conditional(driver);
                    int index = conditional.match(criteria);
                    if (index < 0) {
                        return null;
                    }

                    List<MakeAttachmentEvent> screenshots = new ArrayList<>();
                    if (index == 0) {
                        String alertText = (String) conditional.getResultInfo().getAdditionalInfo().get(ResultType.VALUE);
                        screenshots.add(new MakeAttachmentEvent(alertText.getBytes(), "Alert on " + title, "text/plain"));
                    }

                    byte[] attachment;
                    String append = "";

                    try {
                        if (viewPortOnly) {
                            throw new Exception("User wants view port only screenshot");
                        }

                        attachment = ScreenshotUtil.getFullScreenshotAs(driver);
                    } catch (Exception ex) {
                        // Fall back to only visible view port if cannot get all the partial screenshots
                        attachment = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        append = "_ViewPortOnly";
                    }

                    screenshots.add(new MakeAttachmentEvent(ScreenshotUtil.convertPngToJpg(attachment), title + append, "image/jpeg"));
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
                        if (!child.isDisplayed()) {
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
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
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

}
