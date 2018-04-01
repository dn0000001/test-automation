package com.taf.automation.ui.support;

import com.taf.automation.ui.support.exceptions.JavaScriptException;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class provides utility methods to work with JavaScript
 */
public class JsUtils {
    private static final String _JS_ScrollIntoViewWithOffset = Utils.readResource("JS/ScrollIntoViewWithOffset.js");
    private static final String _JS_Focus = Utils.readResource("JS/Focus.js");
    private static final String _JS_GetTextOnlyTopLevel = Utils.readResource("JS/GetTextOnlyTopLevel.js");
    private static final String _JS_ExecuteGetRequest = Utils.readResource("JS/ExecuteGetRequest.js");
    private static final String _JS_Overlapping = Utils.readResource("JS/Overlapping.js");
    private static final String _JS_ExecuteMouseEvent = Utils.readResource("JS/ExecuteMouseEvent.js");

    private JsUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Get WebDriver
     *
     * @return WebDriver
     */
    private static WebDriver getWebDriver() {
        return TestNGBase.context().getDriver();
    }

    /**
     * Get WebDriverWait
     *
     * @return WebDriverWait
     */
    private static WebDriverWait getWebDriverWait() {
        return new WebDriverWait(getWebDriver(), TestProperties.getInstance().getElementTimeout(), 100L);
    }

    /**
     * Executes JavaScript command
     *
     * @param driver
     * @param sJS    - JavaScript to execute.
     */
    public static void execute(WebDriver driver, String sJS) {
        if (!execute(driver, sJS, true)) {
            throw new JavaScriptException("Javascript execution failed");
        }
    }

    /**
     * Executes JavaScript command (Logging is specified)
     *
     * @param driver
     * @param sJS    - JavaScript to execute
     * @param bLog   - true for logging
     * @return false if any exception occurred else true
     */
    public static boolean execute(WebDriver driver, String sJS, boolean bLog) {
        try {
            ((JavascriptExecutor) driver).executeScript(sJS);
            if (bLog) {
                Helper.log("Executed:  " + StringUtils.removeEnd(sJS, "\n"), true);
            }

            return true;
        } catch (Exception ex) {
            if (bLog) {
                Helper.log("Exception occurred executing following javascript:  " + sJS, true);
                Helper.log("Exception was following:  " + ex.getMessage(), true);
            }
        }

        return false;
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window. The script fragment
     * provided will be executed as the body of an anonymous function.<BR>
     * <BR>
     * Within the script, use document to refer to the current document. Note that local variables will not be
     * available once the script has finished executing, though global variables will persist.<BR>
     * <BR>
     * If the script has a return value (i.e. if the script contains a return statement), then the following
     * steps will be taken:<BR>
     * 1) For an HTML element, this method returns a WebElement<BR>
     * 2) For a decimal, a Double is returned<BR>
     * 3) For a non-decimal number, a Long is returned<BR>
     * 4) For a boolean, a Boolean is returned<BR>
     * 5) For all other cases, a String is returned.<BR>
     * 6) For an array, return a List&lt;Object&gt; with each object following the rules above. We support
     * nested lists.<BR>
     * 7) Unless the value is null or there is no return value, in which null is returned<BR>
     * <BR>
     * Arguments must be a number, a boolean, a String, WebElement, or a List of any combination of the above.
     * If not, then the script will not run and return null; Pass null for args if no arguments.<BR>
     * <BR>
     * <B>Additional Notes:</B><BR>
     * 1) You can return an object and cast it to a Map<BR>
     * 2) In the JavaScript the arguments can be accessed using the variable <B>arguments[X]</B> where X is
     * the index of the variable<BR>
     *
     * @param driver
     * @param sJS    - JavaScript to execute
     * @param args   - Arguments that will be made available to the JavaScript (Use null if no arguments.)
     * @return null or One of Boolean, Long, String, List or WebElement
     */
    public static Object execute(WebDriver driver, String sJS, Object... args) {
        try {
            if (driver == null)
                throw new JavaScriptException("");

            if (args == null)
                return ((JavascriptExecutor) driver).executeScript(sJS);
            else
                return ((JavascriptExecutor) driver).executeScript(sJS, args);
        } catch (JavaScriptException jse) {
            Helper.log("The JavaScript could not be executed because the WebDriver was null", true);
            return null;
        } catch (Exception ex) {
            Helper.log("Exception occurred executing following javascript:  " + sJS, true);
            Helper.log("Exception was following:  " + ex.getMessage(), true);
            return null;
        }
    }

    /**
     * Focus the element using JavaScript
     *
     * @param element - Element to focus
     */
    public static void focus(WebElement element) {
        execute(getWebDriver(), _JS_Focus, element);
    }

    /**
     * Click the element using JavaScript
     *
     * @param element - Element to click
     */
    public static void click(WebElement element) {
        execute(getWebDriver(), "arguments[0].click()", element);
    }

    /**
     * Click the element when it is ready using JavaScript<BR>
     * <B>Notes: </B> This method should only be used if WebElement click is failing due
     * WebDriver's logic in usability (or click does nothing) but in the application it appears to be usable.<BR>
     *
     * @param element - Element to click
     * @return WebElement that was clicked
     */
    public static WebElement clickWhenReady(WebElement element) {
        getWebDriverWait().until(ExpectedConditionsUtil.ready(element));
        click(element);
        return element;
    }

    /**
     * Click the element when it is ready using JavaScript<BR>
     * <B>Notes: </B> This method should only be used if WebElement click is failing due
     * WebDriver's logic in usability (or click does nothing) but in the application it appears to be usable.<BR>
     *
     * @param locator - Locator to find element
     * @return WebElement that was clicked
     */
    public static WebElement clickWhenReady(By locator) {
        WebElement element = getWebDriverWait().until(ExpectedConditionsUtil.ready(locator));
        click(element);
        return element;
    }

    /**
     * Click the relative element from anchor element when it is ready using JavaScript<BR>
     * <B>Notes: </B> This method should only be used if WebElement click is failing due
     * WebDriver's logic in usability (or click does nothing) but in the application it appears to be usable.<BR>
     *
     * @param anchor   - The anchor element from which the element is located from
     * @param relative - Used to find the relative element
     * @return WebElement that was clicked
     */
    public static WebElement clickWhenReady(WebElement anchor, By relative) {
        WebElement element = getWebDriverWait().until(ExpectedConditionsUtil.ready(anchor, relative));
        click(element);
        return element;
    }

    /**
     * Click the element when it is in the DOM (but not necessarily visible or enabled)<BR>
     * <B>Notes: </B> This method should only be used if WebElement click is failing due
     * WebDriver's logic in usability but in the application it appears to be usable.<BR>
     *
     * @param locator - Locator to find element
     * @return WebElement that was clicked
     */
    public static WebElement clickWhenPresent(By locator) {
        WebElement element = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        click(element);
        return element;
    }

    /**
     * Click the element when it is visible (but not necessarily enabled)<BR>
     * <B>Notes: </B> This method should only be used if WebElement click is failing due
     * WebDriver's logic in usability but in the application it appears to be usable.<BR>
     *
     * @param element - Element to click
     * @return WebElement that was clicked
     */
    public static WebElement clickWhenVisible(WebElement element) {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(element));
        click(element);
        return element;
    }

    /**
     * Click the element when it is visible (but not necessarily enabled)<BR>
     * <B>Notes: </B> This method should only be used if WebElement click is failing due
     * WebDriver's logic in usability but in the application it appears to be usable.<BR>
     *
     * @param locator - Locator to find element
     * @return WebElement that was clicked
     */
    public static WebElement clickWhenVisible(By locator) {
        WebElement element = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        click(element);
        return element;
    }

    /**
     * Get text of element using JavaScript to only get the top level nodeType = 3 text<BR><BR>
     * <B>Use Case: </B>WebDriver will always get the visible text of the node and all of its children.
     * There are cases in which this is not desirable and you cannot easily remove the nested children text.
     * This method will only get the top level text and exclude the nested children nodes.<BR><BR>
     * <B>Additional Note: </B> Using this JavaScript as an example you can modify to extract other hard to get text<BR>
     *
     * @param element - Element to get only top level nodeType = 3 text
     * @return String
     */
    public static String getText(WebElement element) {
        return String.valueOf(execute(getWebDriver(), _JS_GetTextOnlyTopLevel, element)).trim();
    }

    /**
     * Execute GET request using specified URL
     *
     * @param url - URL to send GET request to
     * @return Response Text from the GET request to the specified URL
     */
    public static String executeGetRequest(String url) {
        return String.valueOf(execute(getWebDriver(), _JS_ExecuteGetRequest, url));
    }

    /**
     * Execute JavaScript on the element to make it scrollable by making the style by absolute.<BR>
     * <B>Notes:</B><BR>
     * This method should only be used as a workaround for application/WebDriver issue<BR>
     *
     * @param element - Element to change style
     */
    public static void makeScrollable(WebElement element) {
        final String sJS_FIX = "arguments[0].style.position = 'absolute';";
        execute(getWebDriver(), sJS_FIX, element);
    }

    /**
     * Method to use when the element does not full come into view and causes issues (like click not working due to
     * element not quite on the screen.)
     *
     * @param element - WebElement to scroll into view and scroll by
     * @param offset  - Pixels to scroll vertically (positive values scrolls down, negative values scrolls up)
     */
    public static void scrollIntoView(WebElement element, String offset) {
        execute(getWebDriver(), _JS_ScrollIntoViewWithOffset, element, true, offset);
    }

    /**
     * Execute JavaScript on the element to make it hidden by making the style display value none.<BR>
     * <B>Notes:</B><BR>
     * This method should only be used as a workaround for application/WebDriver issues<BR>
     *
     * @param element - Element to hide using style
     */
    public static void makeHidden(WebElement element) {
        execute(getWebDriver(), "arguments[0].style.display = 'none';", element);
    }

    /**
     * Check if the elements overlap<BR>
     * <B>Note: </B> Both elements must be displayed or they will not be considered overlapped
     *
     * @param rect1 - Element (rectangle) #1
     * @param rect2 - Element (rectangle) #2
     * @return true if elements overlap else false
     */
    public static boolean isOverlapping(WebElement rect1, WebElement rect2) {
        return isOverlapping(rect1, rect2, true);
    }

    /**
     * Check if the elements overlap
     *
     * @param rect1                   - Element (rectangle) #1
     * @param rect2                   - Element (rectangle) #2
     * @param checkElementsVisibility - true to check that the elements are displayed before the overlap check
     * @return true if elements overlap else false
     */
    public static boolean isOverlapping(WebElement rect1, WebElement rect2, boolean checkElementsVisibility) {
        if (checkElementsVisibility && (!rect1.isDisplayed() || !rect2.isDisplayed())) {
            return false;
        }

        return BooleanUtils.toBooleanObject(String.valueOf(execute(getWebDriver(), _JS_Overlapping, rect1, rect2)));
    }

    /**
     * Execute Mouse Event
     *
     * @param element - Element to perform mouse event on
     * @param event   - Mouse Event to execute
     */
    private static void executeMouseEvent(WebElement element, MouseEvent event) {
        execute(getWebDriver(), _JS_ExecuteMouseEvent,
                element,
                event.getType().toString(),
                event.isBubbles(),
                event.isCancelable(),
                event.getView(),
                event.getDetail(),
                event.getScreenX(),
                event.getScreenY(),
                event.getClientX(),
                event.getClientY(),
                event.isCtrlKey(),
                event.isAltKey(),
                event.isShiftKey(),
                event.isMetaKey(),
                event.getButton(),
                event.getRelatedTarget()
        );
    }

    /**
     * Mouse Over the element using JavaScript
     *
     * @param element - Element to mouse over
     */
    public static void mouseOver(WebElement element) {
        MouseEvent event = new MouseEvent(MouseEventType.MOUSE_OVER);
        executeMouseEvent(element, event);
    }

    /**
     * Mouse Out the element using JavaScript
     *
     * @param element - Element to mouse out
     */
    public static void mouseOut(WebElement element) {
        MouseEvent event = new MouseEvent(MouseEventType.MOUSE_OUT);
        executeMouseEvent(element, event);
    }

}
