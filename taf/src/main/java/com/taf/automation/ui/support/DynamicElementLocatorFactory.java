package com.taf.automation.ui.support;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Implementation of ElementLocatorFactory for dynamic locators
 */
public class DynamicElementLocatorFactory implements ElementLocatorFactory {
    private final SearchContext searchContext;
    private final Map<String, String> substitutions;
    private final int timeOutInSeconds;

    public DynamicElementLocatorFactory(final SearchContext searchContext, final int timeOutInSeconds, final Map<String, String> substitutions) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutInSeconds;
        this.substitutions = substitutions;
    }

    public DynamicElementLocator createLocator(final Field field) {
        return new DynamicElementLocator(searchContext, field, timeOutInSeconds, substitutions);
    }

}
