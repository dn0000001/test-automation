package com.taf.automation.ui.support.util;

import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.DateActions;
import org.apache.commons.lang3.ObjectUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class holds useful Matcher methods for assertions
 */
@SuppressWarnings("squid:S1192")
public class AssertsUtil {
    private AssertsUtil() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Matcher for regular expressions that are expected to match the pattern
     *
     * @param regex - Regular Expression pattern expected to match
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> matchesRegex(final String regex) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("value matches regular expression:  " + regex);
            }

            @Override
            protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
                mismatchDescription.appendText(" value ('" + item + "') did not match regex pattern");
            }

            @Override
            protected boolean matchesSafely(final String item) {
                try {
                    return item.matches(regex);
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for regular expressions that are expected to <B>NOT</B> match the pattern
     *
     * @param regex - Regular Expression pattern expected to <B>NOT</B> match
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> doesNotMatchRegex(final String regex) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("value does NOT match regular expression:  " + regex);
            }

            @Override
            protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
                mismatchDescription.appendText(" value ('" + item + "') matched the regex pattern");
            }

            @Override
            protected boolean matchesSafely(final String item) {
                try {
                    return !item.matches(regex);
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for strings that are expected to contain the substring ignoring case
     *
     * @param substring - Substring expected to be found ignoring case
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> containsStringIgnoringCase(final String substring) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("a string containing \"" + substring + "\" Ignoring Case");
            }

            @Override
            protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
                mismatchDescription.appendText(" was \"" + item + "\"");
            }

            @Override
            protected boolean matchesSafely(final String item) {
                try {
                    return item.contains(substring)
                            || item.toLowerCase().contains(substring.toLowerCase())
                            || item.toUpperCase().contains(substring.toUpperCase());
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for strings that are expected to <B>NOT</B> contain the substring ignoring case
     *
     * @param substring - Substring expected to <B>NOT</B> be found ignoring case
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> doesNotContainStringIgnoringCase(final String substring) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("a string NOT containing \"" + substring + "\" Ignoring Case");
            }

            @Override
            protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
                mismatchDescription.appendText(" was \"" + item + "\"");
            }

            @Override
            protected boolean matchesSafely(final String item) {
                try {
                    return !item.contains(substring)
                            && !item.toLowerCase().contains(substring.toLowerCase())
                            && !item.toUpperCase().contains(substring.toUpperCase());
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for strings that are expected to start with ignoring case
     *
     * @param prefix - Prefix expected to be found with ignoring case
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> startsWithIgnoringCase(final String prefix) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("a string starting with \"" + prefix + "\" Ignoring Case");
            }

            @Override
            protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
                mismatchDescription.appendText(" was \"" + item + "\"");
            }

            @Override
            protected boolean matchesSafely(final String item) {
                try {
                    return item.startsWith(prefix)
                            || item.toLowerCase().startsWith(prefix.toLowerCase())
                            || item.toUpperCase().startsWith(prefix.toUpperCase());
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for strings that are expected to end with ignoring case
     *
     * @param suffix - Suffix expected to be found with ignoring case
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> endsWithIgnoringCase(final String suffix) {
        return new TypeSafeMatcher<String>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("a string ending with \"" + suffix + "\" Ignoring Case");
            }

            @Override
            protected void describeMismatchSafely(final String item, final Description mismatchDescription) {
                mismatchDescription.appendText(" was \"" + item + "\"");
            }

            @Override
            protected boolean matchesSafely(final String item) {
                try {
                    return item.endsWith(suffix)
                            || item.toLowerCase().endsWith(suffix.toLowerCase())
                            || item.toUpperCase().endsWith(suffix.toUpperCase());
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element that is expected to be displayed
     *
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementDisplayed() {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was displayed");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not displayed");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    return element.isDisplayed();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be displayed
     *
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentDisplayed() {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was displayed");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not displayed");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    return component.isDisplayed();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element that is expected to be disabled
     *
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementDisabled() {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was disabled");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was enabled");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    return !element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be disabled
     *
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentDisabled() {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was disabled");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was enabled");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    return !component.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element that is expected to be enabled
     *
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementEnabled() {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was enabled");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was disabled");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    return element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be enabled
     *
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentEnabled() {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was enabled");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was disabled");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    return component.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for list of components that are expected to be enabled/disabled
     *
     * @param enabled - true to verify that all components are enabled, false to verify that all components are disabled
     * @param <T>     Extends PageComponent
     * @return Matcher&lt;List&lt;PageComponent&gt;&gt;
     */
    public static <T extends PageComponent> Matcher<List<T>> isComponent(boolean enabled) {
        return isComponent(enabled, new ArrayList<>());
    }

    /**
     * Matcher for list of components that are expected to be enabled/disabled<BR>
     * <B>Note: </B> Uses get text when checking the excluded option
     *
     * @param enabled  - true to verify that all components are enabled, false to verify that all components are disabled
     * @param excluded - List of regular expressions that are to be excluded from comparison
     * @param <T>      Extends PageComponent
     * @return Matcher&lt;List&lt;PageComponent&gt;&gt;
     */
    public static <T extends PageComponent> Matcher<List<T>> isComponent(boolean enabled, List<String> excluded) {
        return isComponent(false, enabled, excluded);
    }

    /**
     * Matcher for list of components that are expected to be enabled/disabled
     *
     * @param byValue  - true to use get value, false to use get text when checking the excluded option
     * @param enabled  - true to verify that all components are enabled, false to verify that all components are disabled
     * @param excluded - List of regular expressions that are to be excluded from comparison
     * @param <T>      Extends PageComponent
     * @return Matcher&lt;List&lt;PageComponent&gt;&gt;
     */
    public static <T extends PageComponent> Matcher<List<T>> isComponent(boolean byValue, boolean enabled, List<String> excluded) {
        final String state = enabled ? "enabled" : "disabled";
        final String wasState = enabled ? " was disabled" : " was enabled";
        return new TypeSafeMatcher<List<T>>() {
            private String firstProblemOption;

            private String getExcludedOption(PageComponent excludeOption) {
                return byValue ? excludeOption.getValue() : excludeOption.getText();
            }

            private boolean isExcludedOption(String option) {
                return ObjectUtils.clone(excluded).removeIf(option::matches);
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("all components were " + state + " as expected");
            }

            @Override
            protected void describeMismatchSafely(final List<T> components, final Description mismatchDescription) {
                mismatchDescription.appendText(" " + firstProblemOption + wasState);
            }

            @Override
            protected boolean matchesSafely(final List<T> components) {
                try {
                    for (PageComponent component : components) {
                        validateOption(component);
                    }

                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }

            @SuppressWarnings("java:S112")
            private void validateOption(PageComponent component) {
                boolean theState = enabled == component.isEnabled();
                if (!theState && !isExcludedOption(getExcludedOption(component))) {
                    firstProblemOption = component.getText();
                    // Note:  To keep the complexity under the sonar violation it is necessary to throw an exception
                    throw new RuntimeException("Found option with incorrect state");
                }
            }
        };
    }

    /**
     * Matcher for element that is expected to be ready (enabled &amp; displayed)
     *
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementReady() {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was ready");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not ready");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    return element.isDisplayed() && element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be ready (enabled &amp; displayed)
     *
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentReady() {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was ready");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not ready");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    return component.isDisplayed() && component.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element that is expected to be removed<BR>
     * <BR>
     * <B>Note:</B><BR>
     * 1) The element is considered removed if any exception is thrown when checking if it is displayed. This
     * will consider the element being null as removed and the element being stale as removed.<BR>
     *
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementRemoved() {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was removed");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not removed");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    return !element.isDisplayed();
                } catch (Exception ex) {
                    return true;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be removed<BR>
     * <BR>
     * <B>Note:</B><BR>
     * 1) The component is considered removed if any exception is thrown when checking if it is displayed.
     * This will consider the component being null as removed and the component being stale as removed.<BR>
     *
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentRemoved() {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was removed");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not removed");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    return !component.isDisplayed();
                } catch (Exception ex) {
                    return true;
                }
            }
        };
    }

    /**
     * Matcher for element using locator that is expected to be displayed<BR>
     * <B>Notes:</B><BR>
     * 1) Method does single check without waiting for the element to be displayed<BR>
     *
     * @return Matcher&lt;By&gt;
     */
    public static Matcher<By> isElementDisplayed(final WebDriver driver) {
        return new TypeSafeMatcher<By>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was displayed");
            }

            @Override
            protected void describeMismatchSafely(final By locator, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not displayed");
            }

            @Override
            protected boolean matchesSafely(final By locator) {
                try {
                    WebElement element = driver.findElement(locator);
                    return element.isDisplayed();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element using locator that is expected to be disabled<BR>
     * <B>Notes:</B><BR>
     * 1) Method does single check without waiting for the element to be disabled<BR>
     *
     * @return Matcher&lt;By&gt;
     */
    public static Matcher<By> isElementDisabled(final WebDriver driver) {
        return new TypeSafeMatcher<By>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was disabled");
            }

            @Override
            protected void describeMismatchSafely(final By locator, final Description mismatchDescription) {
                mismatchDescription.appendText(" was enabled");
            }

            @Override
            protected boolean matchesSafely(final By locator) {
                try {
                    WebElement element = driver.findElement(locator);
                    return !element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element using locator that is expected to be enabled<BR>
     * <B>Notes:</B><BR>
     * 1) Method does single check without waiting for the element to be enabled<BR>
     *
     * @return Matcher&lt;By&gt;
     */
    public static Matcher<By> isElementEnabled(final WebDriver driver) {
        return new TypeSafeMatcher<By>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was enabled");
            }

            @Override
            protected void describeMismatchSafely(final By locator, final Description mismatchDescription) {
                mismatchDescription.appendText(" was disabled");
            }

            @Override
            protected boolean matchesSafely(final By locator) {
                try {
                    WebElement element = driver.findElement(locator);
                    return element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element using locator that is expected to be ready (enabled &amp; displayed)<BR>
     * <B>Notes:</B><BR>
     * 1) Method does single check without waiting for the element to be ready<BR>
     *
     * @return Matcher&lt;By&gt;
     */
    public static Matcher<By> isElementReady(final WebDriver driver) {
        return new TypeSafeMatcher<By>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was ready");
            }

            @Override
            protected void describeMismatchSafely(final By locator, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not ready");
            }

            @Override
            protected boolean matchesSafely(final By locator) {
                try {
                    WebElement element = driver.findElement(locator);
                    return element.isDisplayed() && element.isEnabled();
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element using locator that is expected to be removed<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Method does single check without waiting for the element to be removed<BR>
     * 2) The element is considered removed if any exception is thrown when checking if it is displayed. This
     * will consider the element being null as removed and the element being stale as removed.<BR>
     *
     * @return Matcher&lt;By&gt;
     */
    public static Matcher<By> isElementRemoved(final WebDriver driver) {
        return new TypeSafeMatcher<By>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was removed");
            }

            @Override
            protected void describeMismatchSafely(final By locator, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not removed");
            }

            @Override
            protected boolean matchesSafely(final By locator) {
                try {
                    WebElement element = driver.findElement(locator);
                    return !element.isDisplayed();
                } catch (Exception ex) {
                    return true;
                }
            }
        };
    }

    /**
     * Matcher for (inclusive) range matching
     *
     * @param min - Minimum value inclusive
     * @param max - Maximum value inclusive
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<Integer> range(final int min, final int max) {
        return new TypeSafeMatcher<Integer>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("value was in range [" + min + "," + max + "]");
            }

            @Override
            protected void describeMismatchSafely(final Integer value, final Description mismatchDescription) {
                mismatchDescription.appendText(" value ('" + value + "') was not in range [" + min + "," + max + "]");
            }

            @Override
            protected boolean matchesSafely(final Integer value) {
                try {
                    return min <= value && value <= max;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for (inclusive) range matching<BR>
     * <B>Notes:</B><BR>
     * 1) null values are considered as infinite as BigDecimal does not have concept of infinite<BR>
     *
     * @param min - Minimum value inclusive
     * @param max - Maximum value inclusive
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<BigDecimal> range(final BigDecimal min, final BigDecimal max) {
        return new BaseMatcher<BigDecimal>() {

            @Override
            public boolean matches(Object item) {
                BigDecimal value = (BigDecimal) item;
                boolean lowerBound = Utils.compareTo(value, min) >= 0;
                boolean upperBound = Utils.compareTo(value, max) <= 0;
                return lowerBound && upperBound;
            }

            @Override
            public void describeTo(Description description) {
                String logMin = (min == null) ? "infinite (null)" : min.toString();
                String logMax = (max == null) ? "infinite (null)" : max.toString();
                description.appendText("value was in range [" + logMin + "," + logMax + "]");
            }

            @Override
            public void describeMismatch(final Object value, final Description mismatchDescription) {
                String logMin = (min == null) ? "infinite (null)" : min.toString();
                String logMax = (max == null) ? "infinite (null)" : max.toString();
                String logValue = (value == null) ? "infinite (null)" : value.toString();
                mismatchDescription.appendText(" value ('" + logValue + "') was not in range [" + logMin + "," + logMax + "]");
            }
        };
    }

    /**
     * Matcher for element that is expected to be displayed before timeout
     *
     * @param wait - WebDriverWait
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementDisplayed(final WebDriverWait wait) {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was displayed before timeout");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not displayed before timeout");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    wait.until(ExpectedConditions.visibilityOf(element));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be displayed before timeout
     *
     * @param wait - WebDriverWait
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentDisplayed(final WebDriverWait wait) {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was displayed before timeout");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not displayed before timeout");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    wait.until(ExpectedConditionsUtil.displayed(component));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for element that is expected to be ready (enabled &amp; displayed) before timeout
     *
     * @param wait - WebDriverWait
     * @return Matcher&lt;WebElement&gt;
     */
    public static Matcher<WebElement> isElementReady(final WebDriverWait wait) {
        return new TypeSafeMatcher<WebElement>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("element was ready before timeout");
            }

            @Override
            protected void describeMismatchSafely(final WebElement element, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not ready before timeout");
            }

            @Override
            protected boolean matchesSafely(final WebElement element) {
                try {
                    wait.until(ExpectedConditionsUtil.ready(element));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be ready (enabled &amp; displayed) before timeout
     *
     * @param wait - WebDriverWait
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentReady(final WebDriverWait wait) {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was ready before timeout");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was not ready before timeout");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    wait.until(ExpectedConditionsUtil.ready(component));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that is expected to be enabled
     *
     * @param wait - WebDriverWait
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> isComponentEnabled(final WebDriverWait wait) {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component was enabled before timeout");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" was disabled before timeout");
            }

            @Override
            protected boolean matchesSafely(final PageComponent component) {
                try {
                    wait.until(ExpectedConditionsUtil.enabled(component));
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for date that is expected to be greater than the actual date
     *
     * @param expectedDate  - Expected Date
     * @param parsePatterns - Parse Patterns for the actual &amp; expected dates
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> dateGreaterThan(final String expectedDate, final String... parsePatterns) {
        return new TypeSafeMatcher<String>() {
            private int compareValue = 1;
            private boolean parsedActual = false;
            private boolean parsedExpected = false;
            private String actualValue = null;

            @Override
            public void describeTo(final Description description) {
                if (parsedActual) {
                    description.appendText("date '" + actualValue + "' greater than");
                    return;
                }

                description.appendText("date '" + actualValue + "' to be a valid date and greater than");
            }

            @Override
            protected void describeMismatchSafely(final String actualDate, final Description mismatchDescription) {
                if (!parsedActual && !parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' or date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedActual) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (compareValue == 0) {
                    mismatchDescription.appendText("it was equal to date '" + expectedDate + "'");
                    return;
                }

                mismatchDescription.appendText("it was less than date '" + expectedDate + "'");
            }

            @SuppressWarnings("squid:S2259")
            @Override
            protected boolean matchesSafely(final String item) {
                actualValue = item;

                Date actual = DateActions.parseDateStrictly(item, parsePatterns);
                parsedActual = actual != null;

                Date expected = DateActions.parseDateStrictly(expectedDate, parsePatterns);
                parsedExpected = expected != null;

                try {
                    compareValue = actual.compareTo(expected);
                    return compareValue > 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for date that is expected to be greater than or equal to the actual date
     *
     * @param expectedDate  - Expected Date
     * @param parsePatterns - Parse Patterns for the actual &amp; expected dates
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> dateGreaterThanOrEqualTo(final String expectedDate, final String... parsePatterns) {
        return new TypeSafeMatcher<String>() {
            private boolean parsedActual = false;
            private boolean parsedExpected = false;
            private String actualValue = null;

            @Override
            public void describeTo(final Description description) {
                if (parsedActual) {
                    description.appendText("date '" + actualValue + "' greater than or equal to");
                    return;
                }

                description.appendText("date '" + actualValue + "' to be a valid date and greater than or equal to");
            }

            @Override
            protected void describeMismatchSafely(final String actualDate, final Description mismatchDescription) {
                if (!parsedActual && !parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' or date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedActual) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                mismatchDescription.appendText("it was less than date '" + expectedDate + "'");
            }

            @SuppressWarnings("squid:S2259")
            @Override
            protected boolean matchesSafely(final String item) {
                actualValue = item;

                Date actual = DateActions.parseDateStrictly(item, parsePatterns);
                parsedActual = actual != null;

                Date expected = DateActions.parseDateStrictly(expectedDate, parsePatterns);
                parsedExpected = expected != null;

                try {
                    return actual.compareTo(expected) >= 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for date that is expected to be less than the actual date
     *
     * @param expectedDate  - Expected Date
     * @param parsePatterns - Parse Patterns for the actual &amp; expected dates
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> dateLessThan(final String expectedDate, final String... parsePatterns) {
        return new TypeSafeMatcher<String>() {
            private int compareValue = -1;
            private boolean parsedActual = false;
            private boolean parsedExpected = false;
            private String actualValue = null;

            @Override
            public void describeTo(final Description description) {
                if (parsedActual) {
                    description.appendText("date '" + actualValue + "' less than");
                    return;
                }

                description.appendText("date '" + actualValue + "' to be a valid date and less than");
            }

            @Override
            protected void describeMismatchSafely(final String actualDate, final Description mismatchDescription) {
                if (!parsedActual && !parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' or date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedActual) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (compareValue == 0) {
                    mismatchDescription.appendText("it was equal to date '" + expectedDate + "'");
                    return;
                }

                mismatchDescription.appendText("it was greater than date '" + expectedDate + "'");
            }

            @SuppressWarnings("squid:S2259")
            @Override
            protected boolean matchesSafely(final String item) {
                actualValue = item;

                Date actual = DateActions.parseDateStrictly(item, parsePatterns);
                parsedActual = actual != null;

                Date expected = DateActions.parseDateStrictly(expectedDate, parsePatterns);
                parsedExpected = expected != null;

                try {
                    compareValue = actual.compareTo(expected);
                    return compareValue < 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for date that is expected to be less than or equal to the actual date
     *
     * @param expectedDate  - Expected Date
     * @param parsePatterns - Parse Patterns for the actual &amp; expected dates
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> dateLessThanOrEqualTo(final String expectedDate, final String... parsePatterns) {
        return new TypeSafeMatcher<String>() {
            private boolean parsedActual = false;
            private boolean parsedExpected = false;
            private String actualValue = null;

            @Override
            public void describeTo(final Description description) {
                if (parsedActual) {
                    description.appendText("date '" + actualValue + "' less than or equal to");
                    return;
                }

                description.appendText("date '" + actualValue + "' to be a valid date and less than or equal to");
            }

            @Override
            protected void describeMismatchSafely(final String actualDate, final Description mismatchDescription) {
                if (!parsedActual && !parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' or date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedActual) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                mismatchDescription.appendText("it was greater than date '" + expectedDate + "'");
            }

            @SuppressWarnings("squid:S2259")
            @Override
            protected boolean matchesSafely(final String item) {
                actualValue = item;

                Date actual = DateActions.parseDateStrictly(item, parsePatterns);
                parsedActual = actual != null;

                Date expected = DateActions.parseDateStrictly(expectedDate, parsePatterns);
                parsedExpected = expected != null;

                try {
                    return actual.compareTo(expected) <= 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for date that is expected to be equal to the actual date
     *
     * @param expectedDate  - Expected Date
     * @param parsePatterns - Parse Patterns for the actual &amp; expected dates
     * @return Matcher&lt;String&gt;
     */
    public static Matcher<String> dateEqualTo(final String expectedDate, final String... parsePatterns) {
        return new TypeSafeMatcher<String>() {
            private int compareValue = 0;
            private boolean parsedActual = false;
            private boolean parsedExpected = false;
            private String actualValue = null;

            @Override
            public void describeTo(final Description description) {
                if (parsedActual) {
                    description.appendText("date '" + actualValue + "' equal to");
                    return;
                }

                description.appendText("date '" + actualValue + "' to be a valid date and equal to");
            }

            @Override
            protected void describeMismatchSafely(final String actualDate, final Description mismatchDescription) {
                if (!parsedActual && !parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' or date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedActual) {
                    mismatchDescription.appendText("could not parse date '" + actualDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (!parsedExpected) {
                    mismatchDescription.appendText("could not parse date '" + expectedDate
                            + "' using any of " + Arrays.toString(parsePatterns));
                    return;
                }

                if (compareValue < 0) {
                    mismatchDescription.appendText("it was less than date '" + expectedDate + "'");
                } else {
                    mismatchDescription.appendText("it was greater than date '" + expectedDate + "'");
                }
            }

            @SuppressWarnings("squid:S2259")
            @Override
            protected boolean matchesSafely(final String item) {
                actualValue = item;

                Date actual = DateActions.parseDateStrictly(item, parsePatterns);
                parsedActual = actual != null;

                Date expected = DateActions.parseDateStrictly(expectedDate, parsePatterns);
                parsedExpected = expected != null;

                try {
                    compareValue = actual.compareTo(expected);
                    return compareValue == 0;
                } catch (Exception ex) {
                    return false;
                }
            }
        };
    }

    /**
     * Matcher for component that cannot be set with specified value<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>This method does not consider validation.
     * So, as long as the value can be set this is a failure even if the validation would fail.</LI>
     * <LI>This method uses setElementValueV2 to set the value.
     * If the setElementValueV2 method is successful, then this is considered a failure.  So, on unexpected failures
     * you should check that the component is not checking if the value is already entered which could be considered a
     * successful setting of the component</LI>
     * <LI>This method should only be used to valid that an option cannot be set because it is not valid
     * and not that it is disabled</LI>
     * </OL>
     *
     * @param valueToUse - Value to use when attempting to set the component
     * @return Matcher&lt;PageComponent&gt;
     */
    public static Matcher<PageComponent> componentCannotBeSet(String valueToUse) {
        return new TypeSafeMatcher<PageComponent>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("component cannot be set");
            }

            @Override
            protected void describeMismatchSafely(final PageComponent component, final Description mismatchDescription) {
                mismatchDescription.appendText(" able to set using value:  " + valueToUse);
            }

            @SuppressWarnings("java:S110")
            @Override
            protected boolean matchesSafely(final PageComponent component) {
                String restoreData;
                String restoreInitialData;
                String restoreExpectedData;

                try {
                    restoreData = component.getData(DataTypes.Data, false);
                    restoreInitialData = component.getData(DataTypes.Initial, false);
                    restoreExpectedData = component.getData(DataTypes.Expected, false);
                } catch (Exception ex) {
                    // Could not get the restore data.  So just consider this a fail
                    return false;
                }

                try {
                    // Initialize component data for the test
                    component.initializeData(valueToUse, null, null);

                    // This is a workaround to access to a page object with implementing or using an existing one
                    ComponentPO page = new ComponentPO() {
                        @Override
                        public boolean hasData() {
                            return false;
                        }

                        @Override
                        public void fill() {
                            setElementValueV2(component, null, 0);
                        }

                        @Override
                        public void validate() {
                            //
                        }
                    };

                    // This should fail as component cannot be set
                    page.fill();

                    // If we reach here, then component could be set and this is a failure
                    return false;
                } catch (Exception | AssertionError ex) {
                    return true;
                } finally {
                    component.initializeData(restoreData, restoreInitialData, restoreExpectedData);
                }
            }
        };
    }

    /**
     * Matcher for string items that are expected to be contained in the actual items list
     *
     * @param expectedItems - Expected items that must be in the actual items list
     * @return Matcher&lt;List&lt;String&gt;&gt;
     */
    public static Matcher<List<String>> contains(final List<String> expectedItems) {
        return new TypeSafeMatcher<List<String>>() {
            Set<String> failures;

            @Override
            public void describeTo(final Description description) {
                description.appendText(expectedItems.toString() + " to be contained in list");
            }

            @Override
            protected void describeMismatchSafely(final List<String> items, final Description mismatchDescription) {
                mismatchDescription.appendText(failures.toString() + " was not contained in list " + items.toString());
            }

            @Override
            protected boolean matchesSafely(final List<String> actualItems) {
                if (expectedItems == null || expectedItems.isEmpty()) {
                    return true;
                }

                Set<String> actual = new HashSet<>(actualItems);
                Set<String> expected = new HashSet<>(expectedItems);
                expected.removeAll(actual);
                failures = new HashSet<>(expected);
                return failures.isEmpty();
            }
        };
    }

}
