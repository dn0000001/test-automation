package com.taf.automation.ui.support.conditional;

/**
 * The supported criteria types
 */
public enum CriteriaType {
    /**
     * Check for an alert
     */
    ALERT,

    /**
     * Element is ready (displayed &amp; enabled)
     */
    READY,

    /**
     * Element is displayed
     */
    DISPLAYED,

    /**
     * Element is removed (not displayed)
     */
    REMOVED,

    /**
     * Element is enabled
     */
    ENABLED,

    /**
     * Element is disabled
     */
    DISABLED,

    /**
     * Element exists in the DOM (but not necessarily displayed)
     */
    EXISTS,

    /**
     * Check if element is selected
     */
    SELECTED,

    /**
     * Check if element is unselected
     */
    UNSELECTED,

    /**
     * Check if element is stale
     */
    STALE,

    /**
     * Perform text check for equals
     */
    TEXT_EQUALS,

    /**
     * Perform text check for equals ignore case
     */
    TEXT_EQUALS_IGNORE_CASE,

    /**
     * Perform text check for regular expression
     */
    TEXT_REGEX,

    /**
     * Perform text check for not equal
     */
    TEXT_NOT_EQUAL,

    /**
     * Perform text check for does not contain
     */
    TEXT_DOES_NOT_CONTAIN,

    /**
     * Perform text check for contains
     */
    TEXT_CONTAINS,

    /**
     * Check for an URL of the current window for equals
     */
    URL_EQUALS,

    /**
     * Check for an URL of the current window for equals ignore case
     */
    URL_EQUALS_IGNORE_CASE,

    /**
     * Check for an URL of the current window for regular expression
     */
    URL_REGEX,

    /**
     * Check for an URL of the current window for not equal
     */
    URL_NOT_EQUAL,

    /**
     * Check for an URL of the current window for does not contain
     */
    URL_DOES_NOT_CONTAIN,

    /**
     * Check for an URL of the current window for contains
     */
    URL_CONTAINS,

    /**
     * Perform attribute check for equals
     */
    ATTRIBUTE_EQUALS,

    /**
     * Perform attribute check for equals ignore case
     */
    ATTRIBUTE_EQUALS_IGNORE_CASE,

    /**
     * Perform attribute check for regular expression
     */
    ATTRIBUTE_REGEX,

    /**
     * Perform attribute check for not equal
     */
    ATTRIBUTE_NOT_EQUAL,

    /**
     * Perform attribute check for does not contain
     */
    ATTRIBUTE_DOES_NOT_CONTAIN,

    /**
     * Perform attribute check for contains
     */
    ATTRIBUTE_CONTAINS,

    /**
     * Check for a popup
     */
    POPUP,

    /**
     * Perform drop down value check
     */
    DROPDOWN_INDEX,

    /**
     * Perform drop down HTML value check for equals
     */
    DROPDOWN_HTML_EQUALS,

    /**
     * Perform drop down HTML value check for equals ignore case
     */
    DROPDOWN_HTML_EQUALS_IGNORE_CASE,

    /**
     * Perform drop down HTML value check for regular expression
     */
    DROPDOWN_HTML_REGEX,

    /**
     * Perform drop down HTML value check for not equal
     */
    DROPDOWN_HTML_NOT_EQUAL,

    /**
     * Perform drop down HTML value check for does not contain
     */
    DROPDOWN_HTML_DOES_NOT_CONTAIN,

    /**
     * Perform drop down HTML value check for contains
     */
    DROPDOWN_HTML_CONTAINS,

    /**
     * Perform drop down (visible text) value check for equals
     */
    DROPDOWN_EQUALS,

    /**
     * Perform drop down (visible text) value check for equals ignore case
     */
    DROPDOWN_EQUALS_IGNORE_CASE,

    /**
     * Perform drop down (visible text) value check for regular expression
     */
    DROPDOWN_REGEX,

    /**
     * Perform drop down (visible text) value check for not equal
     */
    DROPDOWN_NOT_EQUAL,

    /**
     * Perform drop down (visible text) value check for does not contain
     */
    DROPDOWN_DOES_NOT_CONTAIN,

    /**
     * Perform drop down (visible text) value check for contains
     */
    DROPDOWN_CONTAINS,

    /**
     * Element relative to the anchor element is ready (displayed &amp; enabled)
     */
    RELATIVE_READY,

    /**
     * Number of elements is equal to
     */
    ELEMENTS_EQUAL,

    /**
     * Number of elements is in range (inclusive)
     */
    ELEMENTS_RANGE,

    /**
     * Number of elements is less than
     */
    ELEMENTS_LESS_THAN,

    /**
     * ExpectedConditions to be used
     */
    EXPECTED_CONDITIONS,

    /**
     * Number of elements is more than
     */
    ELEMENTS_MORE_THAN,

    /**
     * Lambda expression to evaluate
     */
    LAMBDA_EXPRESSION,

}
