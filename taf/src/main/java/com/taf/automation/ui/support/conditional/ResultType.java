package com.taf.automation.ui.support.conditional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Enumeration of the additional information stored in the result info
 */
public enum ResultType {
    /**
     * Matching locator if applicable
     */
    LOCATOR(By.class),

    /**
     * Value that may be of interest if applicable such as Alert Message, URL, Window Handle, Attribute Value, etc.
     */
    VALUE(String.class),

    /**
     * Action taken on alert if applicable
     */
    ACCEPTED_ALERT(Boolean.class),

    /**
     * Attribute name that match occurred if applicable
     */
    ATTRIBUTE(String.class),

    /**
     * Matching element if applicable
     */
    ELEMENT(WebElement.class),

    /**
     * Matching elements if applicable
     */
    ELEMENTS(List.class);

    @SuppressWarnings("rawtypes")
    private Class clazz;

    /**
     * Constructor - Sets mapping between enumeration and stored object in ResultInfo
     *
     * @param clazz - Class of the object stored in the ResultInfo for this enumeration
     */
    ResultType(@SuppressWarnings("rawtypes") Class clazz) {
        this.clazz = clazz;
    }

    /**
     * Checks if the enumeration type maps to an assignable object in ResultInfo with the specified object
     *
     * @param obj - Object to check if enumeration type maps to a ResultInfo object that can be cast to this object
     * @return true if the ResultInfo object mapping to this enumeration can be cast to the object
     */
    public boolean isAssignableFrom(Object obj) {
        @SuppressWarnings("rawtypes")
        Class objClass = (obj == null) ? null : obj.getClass();
        return isAssignableFrom(objClass);
    }

    /**
     * Checks if the enumeration type maps to an assignable object in ResultInfo with the specified object
     *
     * @param objClass - Class to check if enumeration type maps to a ResultInfo object that can be cast to this class
     * @return true if the ResultInfo object mapping to this enumeration can be cast to the object
     */
    public boolean isAssignableFrom(@SuppressWarnings("rawtypes") Class objClass) {
        if (objClass == null)
            return false;

        return objClass.getClass().isAssignableFrom(clazz);
    }

}
