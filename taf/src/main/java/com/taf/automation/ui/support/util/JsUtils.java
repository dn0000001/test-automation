package com.taf.automation.ui.support.util;

import com.taf.automation.ui.support.MouseEvent;
import com.taf.automation.ui.support.MouseEventType;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.exceptions.JavaScriptException;
import com.taf.automation.ui.support.testng.TestNGBaseWithoutListeners;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * This class provides utility methods to work with JavaScript
 */
public class JsUtils {
    public static final String ID = "id";
    private static final String SCROLL_INTO_VIEW_WITH_OFFSET = Utils.readResource("JS/ScrollIntoViewWithOffset.js");
    private static final String FOCUS = Utils.readResource("JS/Focus.js");
    private static final String GET_TEXT_ONLY_TOP_LEVEL = Utils.readResource("JS/GetTextOnlyTopLevel.js");
    private static final String EXECUTE_GET_REQUEST = Utils.readResource("JS/ExecuteGetRequest.js");
    private static final String OVERLAPPING = Utils.readResource("JS/Overlapping.js");
    private static final String EXECUTE_MOUSE_EVENT = Utils.readResource("JS/ExecuteMouseEvent.js");
    private static final String SCROLL_TO = Utils.readResource("JS/ScrollTo.js");
    private static final String SCROLL_TO_BOTTOM = Utils.readResource("JS/ScrollToBottom.js");
    private static final String OPEN_NEW_WINDOW = Utils.readResource("JS/OpenNewWindow.js");
    private static final String ADD_ATTRIBUTE_TO_ELEMENT = Utils.readResource("JS/AddAttributeToElement.js");
    private static final String ADD_ELEMENT_TO_PARENT = Utils.readResource("JS/AddElementToParent.js");
    private static final String REMOVE_ELEMENT = Utils.readResource("JS/RemoveElement.js");
    private static final String WAIT_FOR_XHR = Utils.readResource("JS/WaitForXHR.js");

    private JsUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Get WebDriver
     *
     * @return WebDriver
     */
    private static WebDriver getWebDriver() {
        return TestNGBaseWithoutListeners.context().getDriver();
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
        execute(getWebDriver(), FOCUS, element);
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
        return String.valueOf(execute(getWebDriver(), GET_TEXT_ONLY_TOP_LEVEL, element)).trim();
    }

    /**
     * Execute GET request using specified URL
     *
     * @param url - URL to send GET request to
     * @return Response Text from the GET request to the specified URL
     */
    public static String executeGetRequest(String url) {
        return String.valueOf(execute(getWebDriver(), EXECUTE_GET_REQUEST, url));
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
        execute(getWebDriver(), SCROLL_INTO_VIEW_WITH_OFFSET, element, true, offset);
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
     * Execute JavaScript on the element to make it visible by making the style display value block.<BR>
     * <B>Notes:</B><BR>
     * This method should only be used as a workaround for application/WebDriver issues<BR>
     *
     * @param element - Element to hide using style
     */
    public static void makeVisible(WebElement element) {
        execute(getWebDriver(), "arguments[0].style.display = 'block';", element);
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

        return BooleanUtils.toBooleanObject(String.valueOf(execute(getWebDriver(), OVERLAPPING, rect1, rect2)));
    }

    /**
     * Execute Mouse Event
     *
     * @param element - Element to perform mouse event on
     * @param event   - Mouse Event to execute
     */
    private static void executeMouseEvent(WebElement element, MouseEvent event) {
        execute(getWebDriver(), EXECUTE_MOUSE_EVENT,
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

    /**
     * Scroll To using JavaScript
     *
     * @param offset - Offset to scroll to
     */
    public static void scrollTo(String offset) {
        execute(getWebDriver(), SCROLL_TO, offset);
    }

    /**
     * Scroll To Top of the page using JavaScript
     */
    public static void scrollToTop() {
        scrollTo("0");
    }

    /**
     * Scroll To Bottom of the page using JavaScript
     */
    public static void scrollToBottom() {
        execute(getWebDriver(), SCROLL_TO_BOTTOM, (Object[]) null);
    }

    /**
     * Open New Window<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>Popup blockers must be disabled or an exception made for the current site</LI>
     * <LI>To work with the new window use the returned handle to switch to it</LI>
     * </OL>
     *
     * @param url - URL of new window
     * @return window handle of 1st new window that appears
     */
    public static String openNewWindow(String url) {
        return Utils.getWebDriverWait().until(ExpectedConditionsUtil.handle(
                () -> execute(getWebDriver(), OPEN_NEW_WINDOW, url)
        ));
    }

    /**
     * Add attribute to element<BR><BR>
     * <B>Use Case Scenario:</B> You have a list of elements but no way to uniquely identify them for use with
     * dynamic locators in a page object.  You can use this method to set an attribute on the element such that it is
     * possible to construct dynamic locators in the page object.<BR><BR>
     * <B>Example: </B>You are using a page object with dynamic locators to represent an editable row in table.  It
     * tends to be easy to get all the rows.  However, the rows probably have no ids to be referenced.  In this case,
     * you can add ids to the rows and then it is possible to construct the dynamic locators for the page object.<BR>
     *
     * @param element   - Element to add attribute to
     * @param attribute - Attribute to be added
     * @param value     - Value for attribute
     */
    public static void addAttribute(WebElement element, String attribute, String value) {
        execute(getWebDriver(), ADD_ATTRIBUTE_TO_ELEMENT, element, attribute, value);
    }

    /**
     * Add id attribute to the element<BR>
     * <B>Note: </B> You should not use this method if the element already has an id because this could break the
     * website's functionality as the underlining JavaScript may not be able to find this element.
     *
     * @param element - Element to add id to
     * @param value   - Value for id
     */
    public static void addAttributeId(WebElement element, String value) {
        addAttribute(element, ID, value);
    }

    /**
     * Add child element with specified attribute to the given parent element<BR>
     * <B>Use Case Scenario: </B> You need to scroll to the end of a section (or specific place) such that some
     * element that randomly or constantly generates the exception about it taking the click/action.  This method can
     * be used to scroll to the end of the section (or specific place) preventing the error.
     *
     * @param parent    - Parent element to which a child element will be added
     * @param attribute - Attribute to be added
     * @param value     - Value for attribute
     */
    public static void addElement(WebElement parent, String attribute, String value) {
        execute(getWebDriver(), ADD_ELEMENT_TO_PARENT, parent, attribute, value);
    }

    /**
     * Add child element with specified id attribute to the given parent element
     *
     * @param parent - Parent element to which a child element will be added
     * @param value  - Value for attribute
     */
    public static void addElementWithId(WebElement parent, String value) {
        addElement(parent, ID, value);
    }

    /**
     * Remove element from the DOM
     *
     * @param element - Element to be deleted
     */
    public static void removeElement(WebElement element) {
        execute(getWebDriver(), REMOVE_ELEMENT, element);
    }

    /**
     * Focus Window using alert<BR>
     * <B>Note: </B> This method should only be used if application really needs focus
     */
    public static void focusWindowUsingAlert() {
        execute(getWebDriver(), "alert('Focus Window Workaround');", false);
        Utils.dismissAlertIfPresent(getWebDriver());
    }

    public static void waitForXHR() {
        waitForXHR(TestProperties.getInstance().getElementTimeout());
    }

    public static void waitForXHR(int maxDurationInSeconds) {
        execute(getWebDriver(), WAIT_FOR_XHR, (Object[]) null);
        Failsafe.with(Utils.getPollingRetryPolicy(maxDurationInSeconds)).run(JsUtils::validateXHR);
    }

    private static void validateXHR() {
        WebElement element = getWebDriver().findElement(By.cssSelector("body"));
        String ajaxCounter = element.getAttribute("ajaxcounter");
        assertThat("AJAX Counter", NumberUtils.toInt(ajaxCounter, -1), equalTo(0));
    }

}
