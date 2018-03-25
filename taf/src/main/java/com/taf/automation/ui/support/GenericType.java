package com.taf.automation.ui.support;

/**
 * Generic Wrapper Class for single variable. Mainly this should be used for enumerations that need to be
 * converted using the EnumConverter class.
 *
 * @param <T> - Object type to wrap
 */
public class GenericType<T> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
