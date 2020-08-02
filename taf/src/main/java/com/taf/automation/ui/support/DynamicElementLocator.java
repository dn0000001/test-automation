package com.taf.automation.ui.support;

import com.google.common.collect.Lists;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of ElementLocator for dynamic locators
 */
public class DynamicElementLocator implements ElementLocator {
    private final SearchContext searchContext;
    private final int timeOutInSeconds;
    private final boolean shouldCache;
    private final By by;
    private final BasicClock clock;
    private WebElement cachedElement;
    private List<WebElement> cachedElementList;

    public DynamicElementLocator(SearchContext searchContext, Field field, int timeOutInSeconds, Map<String, String> substitutions) {
        this.searchContext = searchContext;
        this.timeOutInSeconds = timeOutInSeconds;
        DynamicAnnotations annotations = new DynamicAnnotations(field, substitutions);
        shouldCache = annotations.isLookupCached();
        by = annotations.buildBy();
        clock = new BasicClock();
    }

    public WebElement findElement() {
        if (cachedElement != null && shouldCache) {
            return cachedElement;
        }

        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(timeOutInSeconds));
        while (clock.isNowBefore(end)) {
            try {
                WebElement element = searchContext.findElement(by);
                if (!element.isDisplayed()) {
                    throw new Exception("Element was not displayed");
                }

                if (shouldCache) {
                    cachedElement = element;
                }

                return element;
            } catch (Exception ex) {
                Utils.sleep(250);
            }
        }

        String error = "Timed out after " + timeOutInSeconds + " seconds.  Could not get usable element using:  " + by.toString();
        throw new NoSuchElementException(error);
    }

    public List<WebElement> findElements() {
        if (cachedElementList != null && shouldCache) {
            return cachedElementList;
        }

        List<WebElement> elements = Lists.newArrayList();
        long end = clock.laterBy(TimeUnit.SECONDS.toMillis(timeOutInSeconds));
        while (clock.isNowBefore(end)) {
            elements = searchContext.findElements(by);
            if (elements.size() > 0 && isAllDisplayed(elements)) {
                break;
            }

            Utils.sleep(250);
        }

        if (shouldCache) {
            cachedElementList = elements;
        }

        return elements;
    }

    private boolean isAllDisplayed(List<WebElement> elements) {
        try {
            for (WebElement element : elements) {
                if (!element.isDisplayed()) {
                    throw new Exception("Found Element in list that was not displayed");
                }
            }

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
