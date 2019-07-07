package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * This class makes the criteria objects for all the criteria types
 */
public class CriteriaMaker {
    private static final String VALUE = "value";

    private CriteriaMaker() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Generic create criteria
     *
     * @param type      - Criteria Type
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    private static Criteria forGenericCriteria(CriteriaType type, PageComponent component) {
        return forGenericCriteria(type, component.getLocator());
    }

    /**
     * Generic create criteria
     *
     * @param type    - Criteria Type
     * @param locator - Locator to be used
     * @return Criteria
     */
    private static Criteria forGenericCriteria(CriteriaType type, By locator) {
        ElementOptions options = new ElementOptions();
        options.setLocator(locator);
        return forGenericCriteria(type, options);
    }

    /**
     * Generic create criteria
     *
     * @param type    - Criteria Type
     * @param options - Options for the criteria
     * @return Criteria
     */
    private static Criteria forGenericCriteria(CriteriaType type, Object options) {
        Criteria criteria = new Criteria();
        criteria.setCriteriaType(type);
        criteria.setOptions(options);
        return criteria;
    }

    /**
     * Generic create criteria for pattern string matching
     *
     * @param type         - Criteria Type
     * @param component    - Component to get locator to be used
     * @param matchPattern - Matching Pattern String to be used
     * @return Criteria
     */
    private static Criteria forGenericPattern(CriteriaType type, PageComponent component, String matchPattern) {
        return forGenericPattern(type, component.getLocator(), matchPattern);
    }

    /**
     * Generic create criteria for pattern string matching
     *
     * @param type         - Criteria Type
     * @param locator      - Locator to be used
     * @param matchPattern - Matching Pattern String to be used
     * @return Criteria
     */
    private static Criteria forGenericPattern(CriteriaType type, By locator, String matchPattern) {
        ElementOptions options = new ElementOptions();
        options.setLocator(locator);
        options.setPattern(matchPattern);
        return forGenericCriteria(type, options);
    }

    /**
     * Generic create criteria for attribute matching
     *
     * @param type         - Criteria Type
     * @param component    - Component to get locator to be used
     * @param attribute    - Attribute name that will be matched on
     * @param matchPattern - Matching Pattern String to be used
     * @return Criteria
     */
    private static Criteria forGenericAttribute(CriteriaType type, PageComponent component, String attribute, String matchPattern) {
        return forGenericAttribute(type, component.getLocator(), attribute, matchPattern);
    }

    /**
     * Generic create criteria for attribute matching
     *
     * @param type         - Criteria Type
     * @param locator      - Locator to be used
     * @param attribute    - Attribute name that will be matched on
     * @param matchPattern - Matching Pattern String to be used
     * @return Criteria
     */
    private static Criteria forGenericAttribute(CriteriaType type, By locator, String attribute, String matchPattern) {
        ElementOptions options = new ElementOptions();
        options.setLocator(locator);
        options.setAttribute(attribute);
        options.setPattern(matchPattern);
        return forGenericCriteria(type, options);
    }

    /**
     * Create criteria for alert accept
     *
     * @return Criteria
     */
    public static Criteria forAlertAccept() {
        AlertOptions options = new AlertOptions();
        options.setAccept(true);
        return forAlert(options);
    }

    /**
     * Create criteria for alert dismiss
     *
     * @return Criteria
     */
    public static Criteria forAlertDismiss() {
        AlertOptions options = new AlertOptions();
        options.setAccept(false);
        return forAlert(options);
    }

    /**
     * Create criteria for alert
     *
     * @param options - Options for the criteria
     * @return Criteria
     */
    public static Criteria forAlert(AlertOptions options) {
        Criteria criteria = new Criteria();
        criteria.setCriteriaType(CriteriaType.ALERT);
        criteria.setOptions(options);
        return criteria;
    }

    /**
     * Create criteria for element ready
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementReady(By locator) {
        return forGenericCriteria(CriteriaType.READY, locator);
    }

    /**
     * Create criteria for element ready
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementReady(PageComponent component) {
        return forGenericCriteria(CriteriaType.READY, component);
    }

    /**
     * Create criteria for element displayed
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementDisplayed(By locator) {
        return forGenericCriteria(CriteriaType.DISPLAYED, locator);
    }

    /**
     * Create criteria for element displayed
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementDisplayed(PageComponent component) {
        return forGenericCriteria(CriteriaType.DISPLAYED, component);
    }

    /**
     * Create criteria for element removed
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementRemoved(By locator) {
        return forGenericCriteria(CriteriaType.REMOVED, locator);
    }

    /**
     * Create criteria for element removed
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementRemoved(PageComponent component) {
        return forGenericCriteria(CriteriaType.REMOVED, component);
    }

    /**
     * Create criteria for element enabled
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementEnabled(By locator) {
        return forGenericCriteria(CriteriaType.ENABLED, locator);
    }

    /**
     * Create criteria for element enabled
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementEnabled(PageComponent component) {
        return forGenericCriteria(CriteriaType.ENABLED, component);
    }

    /**
     * /**
     * Create criteria for element disabled
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementDisabled(By locator) {
        return forGenericCriteria(CriteriaType.DISABLED, locator);
    }

    /**
     * Create criteria for element disabled
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementDisabled(PageComponent component) {
        return forGenericCriteria(CriteriaType.DISABLED, component);
    }

    /**
     * Create criteria for element exists in the DOM (but not necessarily displayed)
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementExists(By locator) {
        return forGenericCriteria(CriteriaType.EXISTS, locator);
    }

    /**
     * Create criteria for element exists in the DOM (but not necessarily displayed)
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementExists(PageComponent component) {
        return forGenericCriteria(CriteriaType.EXISTS, component);
    }

    /**
     * Create criteria for element is selected
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementSelected(By locator) {
        return forGenericCriteria(CriteriaType.SELECTED, locator);
    }

    /**
     * Create criteria for element is selected
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementSelected(PageComponent component) {
        return forGenericCriteria(CriteriaType.SELECTED, component);
    }

    /**
     * Create criteria for element is unselected
     *
     * @param locator - Locator to be used
     * @return Criteria
     */
    public static Criteria forElementUnselected(By locator) {
        return forGenericCriteria(CriteriaType.UNSELECTED, locator);
    }

    /**
     * Create criteria for element is unselected
     *
     * @param component - Component to get locator to be used
     * @return Criteria
     */
    public static Criteria forElementUnselected(PageComponent component) {
        return forGenericCriteria(CriteriaType.UNSELECTED, component);
    }

    /**
     * Create criteria for element is stale
     *
     * @param element - the un-proxied element to check for staleness
     * @return Criteria
     */
    public static Criteria forElementStale(WebElement element) {
        ElementRelativeOptions options = new ElementRelativeOptions();
        options.setAnchor(element);
        return forGenericCriteria(CriteriaType.STALE, options);
    }

    /**
     * Create criteria for element has text equals
     *
     * @param locator - Locator to be used
     * @param value   - Expected text value
     * @return Criteria
     */
    public static Criteria forTextEquals(By locator, String value) {
        return forGenericPattern(CriteriaType.TEXT_EQUALS, locator, value);
    }

    /**
     * Create criteria for element has text equals
     *
     * @param component - Component to get locator to be used
     * @param value     - Expected text value
     * @return Criteria
     */
    public static Criteria forTextEquals(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.TEXT_EQUALS, component, value);
    }

    /**
     * Create criteria for element has text equals ignore case
     *
     * @param locator - Locator to be used
     * @param value   - Expected text value ignoring case
     * @return Criteria
     */
    public static Criteria forTextEqualsIgnoreCase(By locator, String value) {
        return forGenericPattern(CriteriaType.TEXT_EQUALS_IGNORE_CASE, locator, value);
    }

    /**
     * Create criteria for element has text equals ignore case
     *
     * @param component - Component to get locator to be used
     * @param value     - Expected text value ignoring case
     * @return Criteria
     */
    public static Criteria forTextEqualsIgnoreCase(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.TEXT_EQUALS_IGNORE_CASE, component, value);
    }

    /**
     * Create criteria for element has text that matches regular expression
     *
     * @param locator - Locator to be used
     * @param regex   - Regular Expression used to determine match
     * @return Criteria
     */
    public static Criteria forTextRegEx(By locator, String regex) {
        return forGenericPattern(CriteriaType.TEXT_REGEX, locator, regex);
    }

    /**
     * Create criteria for element has text that matches regular expression
     *
     * @param component - Component to get locator to be used
     * @param regex     - Regular Expression used to determine match
     * @return Criteria
     */
    public static Criteria forTextRegEx(PageComponent component, String regex) {
        return forGenericPattern(CriteriaType.TEXT_REGEX, component, regex);
    }

    /**
     * Create criteria for element has text NOT equal
     *
     * @param locator - Locator to be used
     * @param value   - Text value is NOT
     * @return Criteria
     */
    public static Criteria forTextNotEqual(By locator, String value) {
        return forGenericPattern(CriteriaType.TEXT_NOT_EQUAL, locator, value);
    }

    /**
     * Create criteria for element has text NOT equal
     *
     * @param component - Component to get locator to be used
     * @param value     - Text value is NOT
     * @return Criteria
     */
    public static Criteria forTextNotEqual(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.TEXT_NOT_EQUAL, component, value);
    }

    /**
     * Create criteria for element has text NOT containing
     *
     * @param locator - Locator to be used
     * @param value   - Does Not Contain text value
     * @return Criteria
     */
    public static Criteria forTextDoesNotContain(By locator, String value) {
        return forGenericPattern(CriteriaType.TEXT_DOES_NOT_CONTAIN, locator, value);
    }

    /**
     * Create criteria for element has text NOT containing
     *
     * @param component - Component to get locator to be used
     * @param value     - Does Not Contain text value
     * @return Criteria
     */
    public static Criteria forTextDoesNotContain(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.TEXT_DOES_NOT_CONTAIN, component, value);
    }

    /**
     * Create criteria for element has text containing
     *
     * @param locator - Locator to be used
     * @param value   - Contains text value
     * @return Criteria
     */
    public static Criteria forTextContains(By locator, String value) {
        return forGenericPattern(CriteriaType.TEXT_CONTAINS, locator, value);
    }

    /**
     * Create criteria for element has text containing
     *
     * @param component - Component to get locator to be used
     * @param value     - Contains text value
     * @return Criteria
     */
    public static Criteria forTextContains(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.TEXT_CONTAINS, component, value);
    }

    /**
     * Create criteria for URL of the current window has equal value
     *
     * @param value - Expected URL value
     * @return Criteria
     */
    public static Criteria forUrlEquals(String value) {
        // Not used but needed for compiler to pick correct overloaded method
        By locator = null;
        return forGenericPattern(CriteriaType.URL_EQUALS, locator, value);
    }

    /**
     * Create criteria for URL of the current window has equal value ignoring case
     *
     * @param value - Expected URL value ignoring case
     * @return Criteria
     */
    public static Criteria forUrlEqualsIgnoreCase(String value) {
        // Not used but needed for compiler to pick correct overloaded method
        By locator = null;
        return forGenericPattern(CriteriaType.URL_EQUALS_IGNORE_CASE, locator, value);
    }

    /**
     * Create criteria for URL of the current window that matches regular expression
     *
     * @param regex - Regular Expression used to determine match
     * @return Criteria
     */
    public static Criteria forUrlRegEx(String regex) {
        // Not used but needed for compiler to pick correct overloaded method
        By locator = null;
        return forGenericPattern(CriteriaType.URL_REGEX, locator, regex);
    }

    /**
     * Create criteria for URL of the current window does not have equal value
     *
     * @param value - URL does not equal
     * @return Criteria
     */
    public static Criteria forUrlNotEqual(String value) {
        // Not used but needed for compiler to pick correct overloaded method
        By locator = null;
        return forGenericPattern(CriteriaType.URL_NOT_EQUAL, locator, value);
    }

    /**
     * Create criteria for URL of the current window does not contain value
     *
     * @param value - URL does not contain value
     * @return Criteria
     */
    public static Criteria forUrlDoesNotContain(String value) {
        // Not used but needed for compiler to pick correct overloaded method
        By locator = null;
        return forGenericPattern(CriteriaType.URL_DOES_NOT_CONTAIN, locator, value);
    }

    /**
     * Create criteria for URL of the current window contains value
     *
     * @param value - URL contains value
     * @return Criteria
     */
    public static Criteria forUrlContains(String value) {
        // Not used but needed for compiler to pick correct overloaded method
        By locator = null;
        return forGenericPattern(CriteriaType.URL_CONTAINS, locator, value);
    }

    /**
     * Create criteria for element with attribute that equals value
     *
     * @param locator   - Locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element equals value
     * @return Criteria
     */
    public static Criteria forAttributeEquals(By locator, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS, locator, attribute, value);
    }

    /**
     * Create criteria for element with attribute that equals value
     *
     * @param component - Component to get locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element equals value
     * @return Criteria
     */
    public static Criteria forAttributeEquals(PageComponent component, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS, component, attribute, value);
    }

    /**
     * Create criteria for element with attribute that equals ignoring case value
     *
     * @param locator   - Locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element equals ignoring case value
     * @return Criteria
     */
    public static Criteria forAttributeEqualsIgnoreCase(By locator, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS_IGNORE_CASE, locator, attribute, value);
    }

    /**
     * Create criteria for element with attribute that equals ignoring case value
     *
     * @param component - Component to get locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element equals ignoring case value
     * @return Criteria
     */
    public static Criteria forAttributeEqualsIgnoreCase(PageComponent component, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS_IGNORE_CASE, component, attribute, value);
    }

    /**
     * Create criteria for element with attribute that matches regular expression
     *
     * @param locator   - Locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element matches regular expression
     * @return Criteria
     */
    public static Criteria forAttributeRegEx(By locator, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_REGEX, locator, attribute, value);
    }

    /**
     * Create criteria for element with attribute that matches regular expression
     *
     * @param component - Component to get locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param regex     - Attribute on element matches regular expression
     * @return Criteria
     */
    public static Criteria forAttributeRegEx(PageComponent component, String attribute, String regex) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_REGEX, component, attribute, regex);
    }

    /**
     * Create criteria for element with attribute that does not equal value
     *
     * @param locator   - Locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element does not equal value
     * @return Criteria
     */
    public static Criteria forAttributeNotEqual(By locator, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_NOT_EQUAL, locator, attribute, value);
    }

    /**
     * Create criteria for element with attribute that does not equal value
     *
     * @param component - Component to get locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element does not equal value
     * @return Criteria
     */
    public static Criteria forAttributeNotEqual(PageComponent component, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_NOT_EQUAL, component, attribute, value);
    }

    /**
     * Create criteria for element with attribute that does not contain value
     *
     * @param locator   - Locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element does not have value containing
     * @return Criteria
     */
    public static Criteria forAttributeDoesNotContain(By locator, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_DOES_NOT_CONTAIN, locator, attribute, value);
    }

    /**
     * Create criteria for element with attribute that does not contain value
     *
     * @param component - Component to get locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element does not have value containing
     * @return Criteria
     */
    public static Criteria forAttributeDoesNotContain(PageComponent component, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_DOES_NOT_CONTAIN, component, attribute, value);
    }

    /**
     * Create criteria for element with attribute has value contains
     *
     * @param locator   - Locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element has value containing
     * @return Criteria
     */
    public static Criteria forAttributeContains(By locator, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_CONTAINS, locator, attribute, value);
    }

    /**
     * Create criteria for element with attribute has value contains
     *
     * @param component - Component to get locator to be used
     * @param attribute - Attribute name that will be matched on
     * @param value     - Attribute on element has value containing
     * @return Criteria
     */
    public static Criteria forAttributeContains(PageComponent component, String attribute, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_CONTAINS, component, attribute, value);
    }

    /**
     * Create criteria for waiting for a popup
     *
     * @param driver - WebDriver to get current window handle to exclude
     * @return Criteria
     */
    public static Criteria forPopup(WebDriver driver) {
        List<String> exclusions = new ArrayList<>();
        exclusions.add(driver.getWindowHandle());
        return forPopup(exclusions);
    }

    /**
     * Create criteria for waiting for a popup<BR>
     * <B>Notes:</B><BR>
     * 1) It is recommended to have the current window handle in the excluded list or it will match
     * immediately<BR>
     *
     * @param exclusions - List of excluded window handles
     * @return Criteria
     */
    public static Criteria forPopup(List<String> exclusions) {
        PopupOptions options = new PopupOptions();
        options.getExcluding().addAll(exclusions);
        return forPopup(options);
    }

    /**
     * Create criteria for waiting for a popup
     *
     * @param options - Options for the criteria
     * @return Criteria
     */
    private static Criteria forPopup(PopupOptions options) {
        Criteria criteria = new Criteria();
        criteria.setCriteriaType(CriteriaType.POPUP);
        criteria.setOptions(options);
        return criteria;
    }

    /**
     * Create criteria for drop down (Index) value equals
     *
     * @param locator - Locator to be used
     * @param value   - Expected index
     * @return Criteria
     */
    public static Criteria forDropDownIndexEquals(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_INDEX, locator, value);
    }

    /**
     * Create criteria for drop down (Index) value equals
     *
     * @param component - Component to get locator to be used
     * @param value     - Expected index
     * @return Criteria
     */
    public static Criteria forDropDownIndexEquals(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_INDEX, component, value);
    }

    /**
     * Create criteria for drop down (HTML) value equals
     *
     * @param locator - Locator to be used
     * @param value   - HTML Value of selected option equals value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_Equals(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_EQUALS, locator, value);
    }

    /**
     * Create criteria for drop down (HTML) value equals
     *
     * @param component - Component to get locator to be used
     * @param value     - HTML Value of selected option equals value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_Equals(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_EQUALS, component, value);
    }

    /**
     * Create criteria for drop down (HTML) value equals ignore case
     *
     * @param locator - Locator to be used
     * @param value   - HTML Value of selected option equals ignoring case value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_EqualsIgnoreCase(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_EQUALS_IGNORE_CASE, locator, value);
    }

    /**
     * Create criteria for drop down (HTML) value equals ignore case
     *
     * @param component - Component to get locator to be used
     * @param value     - HTML Value of selected option equals ignoring case value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_EqualsIgnoreCase(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_EQUALS_IGNORE_CASE, component, value);
    }

    /**
     * Create criteria for drop down (HTML) that matches regular expression
     *
     * @param locator - Locator to be used
     * @param regex   - HTML Value of selected option matches this Regular Expression
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_RegEx(By locator, String regex) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_REGEX, locator, regex);
    }

    /**
     * Create criteria for drop down (HTML) that matches regular expression
     *
     * @param component - Component to get locator to be used
     * @param regex     - HTML Value of selected option matches this Regular Expression
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_RegEx(PageComponent component, String regex) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_REGEX, component, regex);
    }

    /**
     * Create criteria for drop down (HTML) value NOT equal
     *
     * @param locator - Locator to be used
     * @param value   - HTML Value of selected option does NOT equal value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_NotEqual(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_NOT_EQUAL, locator, value);
    }

    /**
     * Create criteria for drop down (HTML) value NOT equal
     *
     * @param component - Component to get locator to be used
     * @param value     - HTML Value of selected option does NOT equal value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_NotEqual(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_NOT_EQUAL, component, value);
    }

    /**
     * Create criteria for drop down (HTML) value NOT containing
     *
     * @param locator - Locator to be used
     * @param value   - HTML Value of selected option does NOT contains value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_DoesNotContain(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_DOES_NOT_CONTAIN, locator, value);
    }

    /**
     * Create criteria for drop down (HTML) value NOT containing
     *
     * @param component - Component to get locator to be used
     * @param value     - HTML Value of selected option does NOT contains value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_DoesNotContain(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_DOES_NOT_CONTAIN, component, value);
    }

    /**
     * Create criteria for drop down (HTML) value contains
     *
     * @param locator - Locator to be used
     * @param value   - HTML Value of selected option contains value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_Contains(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_CONTAINS, locator, value);
    }

    /**
     * Create criteria for drop down (HTML) value contains
     *
     * @param component - Component to get locator to be used
     * @param value     - HTML Value of selected option contains value
     * @return Criteria
     */
    @SuppressWarnings("squid:S00100")
    public static Criteria forDropDownHTML_Contains(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_HTML_CONTAINS, component, value);
    }

    /**
     * Create criteria for drop down (visible text) value equals
     *
     * @param locator - Locator to be used
     * @param value   - Visible Text of selected option equals value
     * @return Criteria
     */
    public static Criteria forDropDownEquals(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_EQUALS, locator, value);
    }

    /**
     * Create criteria for drop down (visible text) value equals
     *
     * @param component - Component to get locator to be used
     * @param value     - Visible Text of selected option equals value
     * @return Criteria
     */
    public static Criteria forDropDownEquals(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_EQUALS, component, value);
    }

    /**
     * Create criteria for drop down (visible text) value equals ignore case
     *
     * @param locator - Locator to be used
     * @param value   - Visible Text of selected option equals ignoring case value
     * @return Criteria
     */
    public static Criteria forDropDownEqualsIgnoreCase(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_EQUALS_IGNORE_CASE, locator, value);
    }

    /**
     * Create criteria for drop down (visible text) value equals ignore case
     *
     * @param component - Component to get locator to be used
     * @param value     - Visible Text of selected option equals ignoring case value
     * @return Criteria
     */
    public static Criteria forDropDownEqualsIgnoreCase(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_EQUALS_IGNORE_CASE, component, value);
    }

    /**
     * Create criteria for drop down (visible text) that matches regular expression
     *
     * @param locator - Locator to be used
     * @param regex   - Visible Text of selected option matches this Regular Expression
     * @return Criteria
     */
    public static Criteria forDropDownRegEx(By locator, String regex) {
        return forGenericPattern(CriteriaType.DROPDOWN_REGEX, locator, regex);
    }

    /**
     * Create criteria for drop down (visible text) that matches regular expression
     *
     * @param component - Component to get locator to be used
     * @param regex     - Visible Text of selected option matches this Regular Expression
     * @return Criteria
     */
    public static Criteria forDropDownRegEx(PageComponent component, String regex) {
        return forGenericPattern(CriteriaType.DROPDOWN_REGEX, component, regex);
    }

    /**
     * Create criteria for drop down (visible text) value NOT equal
     *
     * @param locator - Locator to be used
     * @param value   - Visible Text of selected option does NOT equal value
     * @return Criteria
     */
    public static Criteria forDropDownNotEqual(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_NOT_EQUAL, locator, value);
    }

    /**
     * Create criteria for drop down (visible text) value NOT equal
     *
     * @param component - Component to get locator to be used
     * @param value     - Visible Text of selected option does NOT equal value
     * @return Criteria
     */
    public static Criteria forDropDownNotEqual(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_NOT_EQUAL, component, value);
    }

    /**
     * Create criteria for drop down (visible text) value NOT containing
     *
     * @param locator - Locator to be used
     * @param value   - Visible Text of selected option does NOT contains value
     * @return Criteria
     */
    public static Criteria forDropDownDoesNotContain(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_DOES_NOT_CONTAIN, locator, value);
    }

    /**
     * Create criteria for drop down (visible text) value NOT containing
     *
     * @param component - Component to get locator to be used
     * @param value     - Visible Text of selected option does NOT contains value
     * @return Criteria
     */
    public static Criteria forDropDownDoesNotContain(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_DOES_NOT_CONTAIN, component, value);
    }

    /**
     * Create criteria for drop down (visible text) value contains
     *
     * @param locator - Locator to be used
     * @param value   - Visible Text of selected option contains value
     * @return Criteria
     */
    public static Criteria forDropDownContains(By locator, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_CONTAINS, locator, value);
    }

    /**
     * Create criteria for drop down (visible text) value contains
     *
     * @param component - Component to get locator to be used
     * @param value     - Visible Text of selected option contains value
     * @return Criteria
     */
    public static Criteria forDropDownContains(PageComponent component, String value) {
        return forGenericPattern(CriteriaType.DROPDOWN_CONTAINS, component, value);
    }

    /**
     * Create criteria for input element that equals value
     *
     * @param locator - Locator to be used
     * @param value   - Input value equals
     * @return Criteria
     */
    public static Criteria forInputEquals(By locator, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS, locator, VALUE, value);
    }

    /**
     * Create criteria for input element that equals value
     *
     * @param component - Component to get locator to be used
     * @param value     - Input value equals
     * @return Criteria
     */
    public static Criteria forInputEquals(PageComponent component, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS, component, VALUE, value);
    }

    /**
     * Create criteria for input element that equals ignoring case value
     *
     * @param locator - Locator to be used
     * @param value   - Input value equals ignoring case value
     * @return Criteria
     */
    public static Criteria forInputEqualsIgnoreCase(By locator, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS_IGNORE_CASE, locator, VALUE, value);
    }

    /**
     * Create criteria for input element that equals ignoring case value
     *
     * @param component - Component to get locator to be used
     * @param value     - Input value equals ignoring case value
     * @return Criteria
     */
    public static Criteria forInputEqualsIgnoreCase(PageComponent component, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_EQUALS_IGNORE_CASE, component, VALUE, value);
    }

    /**
     * Create criteria for input element that matches regular expression
     *
     * @param locator - Locator to be used
     * @param value   - Input value matches regular expression
     * @return Criteria
     */
    public static Criteria forInputRegEx(By locator, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_REGEX, locator, VALUE, value);
    }

    /**
     * Create criteria for input element that matches regular expression
     *
     * @param component - Component to get locator to be used
     * @param regex     - Input value matches regular expression
     * @return Criteria
     */
    public static Criteria forInputRegEx(PageComponent component, String regex) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_REGEX, component, VALUE, regex);
    }

    /**
     * Create criteria for input element that does not equal value
     *
     * @param locator - Locator to be used
     * @param value   - Input value does not equal value
     * @return Criteria
     */
    public static Criteria forInputNotEqual(By locator, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_NOT_EQUAL, locator, VALUE, value);
    }

    /**
     * Create criteria for input element that does not equal value
     *
     * @param component - Component to get locator to be used
     * @param value     - Input value does not equal value
     * @return Criteria
     */
    public static Criteria forInputNotEqual(PageComponent component, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_NOT_EQUAL, component, VALUE, value);
    }

    /**
     * Create criteria for input element that does not contain value
     *
     * @param locator - Locator to be used
     * @param value   - Input value does not have value containing
     * @return Criteria
     */
    public static Criteria forInputDoesNotContain(By locator, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_DOES_NOT_CONTAIN, locator, VALUE, value);
    }

    /**
     * Create criteria for input element that does not contain value
     *
     * @param component - Component to get locator to be used
     * @param value     - Input value does not have value containing
     * @return Criteria
     */
    public static Criteria forInputDoesNotContain(PageComponent component, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_DOES_NOT_CONTAIN, component, VALUE, value);
    }

    /**
     * Create criteria for input element that has value contains
     *
     * @param locator - Locator to be used
     * @param value   - Input value containing
     * @return Criteria
     */
    public static Criteria forInputContains(By locator, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_CONTAINS, locator, VALUE, value);
    }

    /**
     * Create criteria for input element that has value contains
     *
     * @param component - Component to get locator to be used
     * @param value     - Input value containing
     * @return Criteria
     */
    public static Criteria forInputContains(PageComponent component, String value) {
        return forGenericAttribute(CriteriaType.ATTRIBUTE_CONTAINS, component, VALUE, value);
    }

    /**
     * Create criteria for relative element from anchor element is ready
     *
     * @param anchor   - the anchor element used to find match relative to
     * @param relative - the relative locator used to find match from the anchor element
     * @return Criteria
     */
    public static Criteria forElementReady(WebElement anchor, By relative) {
        ElementRelativeOptions options = new ElementRelativeOptions();
        options.setAnchor(anchor);
        options.setRelative(relative);
        return forGenericCriteria(CriteriaType.RELATIVE_READY, options);
    }

    /**
     * Create criteria for number of elements being equal to the value
     *
     * @param locator - Locator to be used
     * @param value   - Number of elements to be equal to
     * @return Criteria
     */
    public static Criteria forNumberOfElements(By locator, Integer value) {
        NumberOptions options = new NumberOptions();
        options.setLocator(locator);
        options.setMin(value);
        return forGenericCriteria(CriteriaType.ELEMENTS_EQUAL, options);
    }

    /**
     * Create criteria for number of elements being in the range (inclusive)
     *
     * @param locator - Locator to be used
     * @param min     - Minimum number of elements (inclusive)
     * @param max     - Maximum number of elements (inclusive)
     * @return Criteria
     */
    public static Criteria forNumberOfElements(By locator, Integer min, Integer max) {
        NumberOptions options = new NumberOptions();
        options.setLocator(locator);
        options.setMin(min);
        options.setMax(max);
        return forGenericCriteria(CriteriaType.ELEMENTS_RANGE, options);
    }

    /**
     * Create criteria for number of elements to be less than the value
     *
     * @param locator - Locator to be used
     * @param value   - Number of elements to be less than
     * @return Criteria
     */
    public static Criteria forNumberOfElementsLessThan(By locator, Integer value) {
        NumberOptions options = new NumberOptions();
        options.setLocator(locator);
        options.setMin(value);
        return forGenericCriteria(CriteriaType.ELEMENTS_LESS_THAN, options);
    }

    /**
     * Create criteria for number of elements to be more than the value
     *
     * @param locator - Locator to be used
     * @param value   - Number of elements to be more than
     * @return Criteria
     */
    public static Criteria forNumberOfElementsMoreThan(By locator, Integer value) {
        NumberOptions options = new NumberOptions();
        options.setLocator(locator);
        options.setMin(value);
        return forGenericCriteria(CriteriaType.ELEMENTS_MORE_THAN, options);
    }

    /**
     * Create criteria to use a lambda expression to check if it matches
     *
     * @param lambda - Lambda expression to check if it matches
     * @return Criteria
     */
    public static Criteria forLambdaExpression(Callable<Boolean> lambda) {
        return forGenericCriteria(CriteriaType.LAMBDA_EXPRESSION, lambda);
    }

    /**
     * Create criteria that uses ExpectedConditions
     *
     * @param expectedCondition - ExpectedCondition to be used
     * @return Criteria
     */
    public static Criteria forExpectedConditions(ExpectedCondition expectedCondition) {
        return forGenericCriteria(CriteriaType.EXPECTED_CONDITIONS, expectedCondition);
    }

}
