package com.taf.automation.ui.support.util;

import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.ConsulInstance;
import com.taf.automation.api.network.MultiSshSession;
import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.Logging;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.conditional.Conditional;
import com.taf.automation.ui.support.conditional.Criteria;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.utils.URIBuilder;
import org.hamcrest.MatcherAssert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.CheckBox;
import ui.auto.core.components.WebComponent;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;
import ui.auto.core.pagecomponent.PageObject;
import ui.auto.core.pagecomponent.SkipAutoFill;
import ui.auto.core.pagecomponent.SkipAutoValidate;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.taf.automation.ui.support.util.AssertsUtil.isComponentDisplayed;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Class for general helper methods
 */
public class Helper {
    private static final Logger LOG = LoggerFactory.getLogger(Helper.class);
    private static final String DIVIDER_RESOURCE = "divider.png";
    private static final String ASSERT_ERROR = "Assert_Error";
    private static final String ASSERT_FAILED = "Assert_Failed";

    private Helper() {
        //
    }

    /**
     * A convenient method to log anything such that it appears in the report
     *
     * @param value - Value to be logged by allure
     */
    @Step("INFO:  {0}")
    public static void log(String value) {
        // empty method
    }

    /**
     * A method used for debugging purposes when you are not necessarily generating the report
     *
     * @param value   - Value to be logged
     * @param console - true to write value to console
     */
    public static void log(String value, boolean console) {
        log(value);
        if (console) {
            LOG.info(value);
        }
    }

    /**
     * A convenient method to log anything such that it appears in the report
     *
     * @param prefix - Prefix before value
     * @param value  - Value to be logged by allure
     */
    @Step("{0}:  {1}")
    public static void log(String prefix, String value) {
        // empty method
    }

    /**
     * A method used for debugging purposes when you are not necessarily generating the report
     *
     * @param prefix  - Prefix before value
     * @param value   - Value to be logged by allure
     * @param console - true to write value to console
     */
    public static void log(String prefix, String value, boolean console) {
        log(prefix.toUpperCase(), value);
        if (console) {
            String upperPrefix = prefix.toUpperCase();
            LOG.info("{}: {}", upperPrefix, value);
        }
    }

    /**
     * Click element and it appears in the report
     *
     * @param log     - Value that appears in the report
     * @param element - Element to click
     */
    @Step("Click {0}")
    public static void click(String log, WebElement element) {
        element.click();
    }

    /**
     * Click component and it appears in the report
     *
     * @param log       - Value that appears in the report
     * @param component - Component to click
     */
    @Step("Click {0}")
    public static void click(String log, WebComponent component) {
        component.click();
    }

    /**
     * Set Value for component and action appears in the report<BR>
     * <B>Notes:</B><BR>
     * 1) The component's setValue method is being called<BR>
     * 2) Verification is on and it expects the component's getValue to equal getData or getExpectedData<BR>
     * 3) Number of retries set to 3<BR>
     *
     * @param log       - Value that appears in the report
     * @param component - Component to set value
     */
    public static void setValue(String log, PageComponent component) {
        setValue(log, component, true, 3);
    }

    /**
     * Set Value for component and action appears in the report<BR>
     * <B>Notes:</B><BR>
     * 1) The component's setValue method is being called<BR>
     * 2) Verification expects the component's getValue to equal getData or getExpectedData<BR>
     *
     * @param log       - Value that appears in the report
     * @param component - Component to set value
     * @param validate  - true to validate value set properly, false to skip validation
     * @param retries   - Number of retries
     */
    public static void setValue(String log, PageComponent component, boolean validate, int retries) {
        if (component == null || component.getData(DataTypes.Data, true) == null) {
            log("Skipped Set Value for", log);
        } else {
            performSetValue(log, component, validate, retries);
        }
    }

    /**
     * Set Value for component and action appears in the report<BR>
     * <B>Notes:</B><BR>
     * 1) The component's setValue method is being called<BR>
     * 2) Verification expects the component's getValue to equals getData or getExpectedData<BR>
     *
     * @param log       - Value that appears in the report
     * @param component - Component to set value
     * @param validate  - true to validate value set properly, false to skip validation
     * @param retries   - Number of retries
     */
    @SuppressWarnings("java:S135")
    @Step("Set Value for {0}")
    private static void performSetValue(String log, PageComponent component, boolean validate, int retries) {
        String validationData = component.getData(DataTypes.Data, true);
        if (component.getData(DataTypes.Expected, true) != null) {
            validationData = component.getData(DataTypes.Expected, true);
        }

        boolean validationFlag = false;
        for (int i = 0; i < retries; i++) {
            Failsafe.with(Utils.getWriteValueRetryPolicy()).run(component::setValue);
            if (!validate) {
                validationFlag = true;
                break;
            }

            if (component.getValue().equals(validationData)) {
                validationFlag = true;
                break;
            }
        }

        MatcherAssert.assertThat("Set Value validation failed for " + log, validationFlag);
    }

    /**
     * Select or UnSelect checkbox if necessary
     *
     * @param log      - Value that appears in the report
     * @param checkBox - Component to work with
     */
    public static void check(String log, CheckBox checkBox) {
        boolean select = checkBox.getData().equalsIgnoreCase("true");
        WebElement element = checkBox.getCoreElement();
        if (select) {
            if (!element.isSelected()) {
                performCheck(log, element);
            }
        } else {
            if (element.isSelected()) {
                performUncheck(log, element);
            }
        }
    }

    @Step("Select CheckBox {0}")
    private static void performCheck(String log, WebElement element) {
        element.click();
    }

    @Step("UnSelect CheckBox {0}")
    private static void performUncheck(String log, WebElement element) {
        element.click();
    }

    /**
     * Verifies that the expected object equals the corresponding actual object or skips if the expected object is null
     *
     * @param aggregator - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param reason     - Failure reason
     * @param actual     - Actual Object
     * @param expected   - Expected Object
     */
    public static void assertThat(AssertAggregator aggregator, String reason, Object actual, Object expected) {
        if (expected == null) {
            return;
        }

        if (aggregator == null) {
            MatcherAssert.assertThat(reason, actual, equalTo(expected));
        } else {
            aggregator.assertThat(reason, actual, equalTo(expected));
        }
    }

    /**
     * Verifies that all non-null fields of the expected object equals the corresponding actual object.
     *
     * @param aggregator - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual     - Actual Object
     * @param expected   - Expected Object
     */
    public static void assertThat(AssertAggregator aggregator, Object actual, Object expected) {
        assertThat(aggregator, actual, expected, new ArrayList<>());
    }

    /**
     * Verifies that all non-null fields of the expected object equals the corresponding actual object.
     *
     * @param aggregator    - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual        - Actual Object
     * @param expected      - Expected Object
     * @param excludeFields - Fields that are excluded from the comparison
     */
    public static void assertThat(
            AssertAggregator aggregator,
            Object actual,
            Object expected,
            List<String> excludeFields
    ) {
        assertThat(aggregator, actual, expected, excludeFields, false);
    }

    /**
     * Verifies that all non-null fields of the expected object equals the corresponding actual object<BR>
     * <B>Notes: </B> Including failure steps make debugging failures easier with lists or complex objects
     * as the report will indicate exactly where a validation failed instead of only at the end.
     *
     * @param aggregator    - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual        - Actual Object
     * @param expected      - Expected Object
     * @param excludeFields - Fields that are excluded from the comparison
     * @param failureSteps  - true to include failure steps as they occur
     */
    public static void assertThat(
            AssertAggregator aggregator,
            Object actual,
            Object expected,
            List<String> excludeFields,
            boolean failureSteps
    ) {
        List<Field> fields = ApiUtils.getFieldsToValidate(expected);
        for (Field field : fields) {
            if (excludeFields.contains(field.getName())) {
                continue;
            }

            Object actualValue = (actual == null) ? null : ApiUtils.readField(field, actual);
            Object expectedValue = ApiUtils.readField(field, expected);
            if (aggregator == null) {
                MatcherAssert.assertThat(field.getName(), actualValue, equalTo(expectedValue));
            } else {
                boolean result = aggregator.assertThat(field.getName(), actualValue, equalTo(expectedValue));
                if (failureSteps && !result) {
                    log(ASSERT_FAILED, field.getName(), aggregator.getConsole());
                }
            }
        }
    }

    /**
     * Verifies that the lists are equal.  Logging occurs as necessary.
     *
     * @param type       - Type for logging (if expected is not null)
     * @param aggregator - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual     - List of actual items
     * @param expected   - List of expected items
     * @param sortBy     - Comparator to sort by, null skips sorting the lists
     * @param <T>        - Type
     */
    public static <T> void assertThat(
            String type,
            AssertAggregator aggregator,
            List<T> actual,
            List<T> expected,
            Comparator<? super T> sortBy
    ) {
        List<String> excludeFields = new ArrayList<>();
        assertThat(type, aggregator, actual, expected, sortBy, excludeFields);
    }

    /**
     * Verifies that the lists are equal (excluding the specified fields.)  Logging occurs as necessary.
     *
     * @param type          - Type for logging (if expected is not null)
     * @param aggregator    - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual        - List of actual items
     * @param expected      - List of expected items
     * @param sortBy        - Comparator to sort by, null skips sorting the lists
     * @param excludeFields - The fields in each item that will be excluded from the comparison
     * @param <T>           - Type
     */
    public static <T> void assertThat(
            String type,
            AssertAggregator aggregator,
            List<T> actual,
            List<T> expected,
            Comparator<? super T> sortBy,
            List<String> excludeFields
    ) {
        if (expected == null) {
            // A null expected list means that user does not want to verify the list
            return;
        }

        assertThatList(type, aggregator, actual, expected, sortBy, excludeFields);
    }

    /**
     * Verifies that the lists are equal (excluding the specified fields)
     *
     * @param type          - Type for logging
     * @param aggregator    - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual        - List of actual items
     * @param expected      - List of expected items
     * @param sortBy        - Comparator to sort by, null skips sorting the lists
     * @param excludeFields - The fields in each item that will be excluded from the comparison
     * @param <T>           - Type
     */
    @Step("Validate List of {0}")
    private static <T> void assertThatList(
            String type,
            AssertAggregator aggregator,
            List<T> actual,
            List<T> expected,
            Comparator<? super T> sortBy,
            List<String> excludeFields
    ) {
        // Ensure that null list will cause failure
        String reason = "Actual List of " + type + " was null";
        if (aggregator == null) {
            MatcherAssert.assertThat(reason, actual, notNullValue());
        } else {
            boolean result = aggregator.assertThat(reason, actual, notNullValue());
            if (!result) {
                log(ASSERT_FAILED, reason, aggregator.getConsole());
            }
        }

        // Prevent null pointer exceptions
        List<T> actualItems = ObjectUtils.defaultIfNull(actual, new ArrayList<>());
        List<T> expectedItems = ObjectUtils.defaultIfNull(expected, new ArrayList<>());
        if (sortBy != null) {
            actualItems.sort(sortBy);
            expectedItems.sort(sortBy);
        }

        int initialFailureCount = 0;
        int currentFailureCount = 0;
        reason = "Actual Item Size did not match Expected Item Size";
        if (aggregator == null) {
            MatcherAssert.assertThat(reason, actualItems.size(), equalTo(expectedItems.size()));
        } else {
            initialFailureCount = aggregator.getFailureCount();
            aggregator.assertThat(reason, actualItems.size(), equalTo(expectedItems.size()));
            currentFailureCount = aggregator.getFailureCount();
        }

        if (initialFailureCount != currentFailureCount) {
            // The number of items in the lists are different and there is no reason to continue
            log(ASSERT_FAILED, reason, aggregator.getConsole());
            return;
        }

        for (int i = 0; i < expectedItems.size(); i++) {
            assertThatListItem(
                    type + "[" + i + "]",
                    aggregator,
                    actualItems.get(i),
                    expectedItems.get(i),
                    excludeFields,
                    true
            );
        }
    }

    /**
     * Verifies that an item of a list is equal based all non-null fields of the expected object equals the corresponding actual object.
     *
     * @param type          - Type for logging
     * @param aggregator    - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual        - Actual Item
     * @param expected      - Expected Item
     * @param excludeFields - The fields in each item that will be excluded from the comparison
     * @param <T>           - Type
     */
    public static <T> void assertThatListItem(
            String type,
            AssertAggregator aggregator,
            T actual,
            T expected,
            List<String> excludeFields
    ) {
        assertThatListItem(type, aggregator, actual, expected, excludeFields, false);
    }

    /**
     * Verifies that an item of a list is equal based all non-null fields of the expected object equals the corresponding actual object.
     *
     * @param type          - Type for logging
     * @param aggregator    - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual        - Actual Item
     * @param expected      - Expected Item
     * @param excludeFields - The fields in each item that will be excluded from the comparison
     * @param failureSteps  - true to include failure steps as they occur
     * @param <T>           - Type
     */
    @Step("Validate {0}")
    public static <T> void assertThatListItem(
            String type,
            AssertAggregator aggregator,
            T actual,
            T expected,
            List<String> excludeFields,
            boolean failureSteps
    ) {
        assertThat(aggregator, actual, expected, excludeFields, failureSteps);
    }

    /**
     * Verifies that the actual list contains all the expected subset list items (excluding the specified fields.)
     * Logging occurs as necessary.
     *
     * @param type           - Type for logging (if expected is not null)
     * @param aggregator     - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual         - List of actual items
     * @param expectedSubset - List of expected subset items
     * @param excludeFields  - The fields in each item that will be excluded from the comparison
     * @param matchCriteria  - Lambda expression to match an expected item to an actual item.
     *                       (If a match is found, then all fields will be verified.)
     * @param <T>            - Type
     */
    @SuppressWarnings("java:S4276")
    public static <T> void assertThat(
            String type,
            AssertAggregator aggregator,
            List<T> actual,
            List<T> expectedSubset,
            List<String> excludeFields,
            BiFunction<T, T, Boolean> matchCriteria
    ) {
        if (expectedSubset == null) {
            // A null expected subset list means that user does not want to verify anything
            return;
        }

        assertThatSubset(type, aggregator, actual, expectedSubset, matchCriteria, excludeFields);
    }

    /**
     * Verifies that the actual list contains all the expected subset list items (excluding the specified fields)
     *
     * @param type           - Type for logging
     * @param aggregator     - Use null for immediate failure else the Assert Aggregator that contains all the assertion results
     * @param actual         - List of actual items
     * @param expectedSubset - List of expected subset items
     * @param matchCriteria  - Lambda expression to match an expected item to an actual item.
     *                       (If a match is found, then all fields will be verified.)
     * @param excludeFields  - The fields in each item that will be excluded from the comparison
     * @param <T>            - Type
     */
    @SuppressWarnings({"java:S2259", "java:S4276", "java:S5411"})
    @Step("Validate the Actual List of {0} contains the Expected items")
    private static <T> void assertThatSubset(
            String type,
            AssertAggregator aggregator,
            List<T> actual,
            List<T> expectedSubset,
            BiFunction<T, T, Boolean> matchCriteria,
            List<String> excludeFields
    ) {
        // Ensure that null list will cause failure
        String reason = "Actual List of " + type + " was null";
        if (aggregator == null) {
            MatcherAssert.assertThat(reason, actual, notNullValue());
        } else {
            boolean result = aggregator.assertThat(reason, actual, notNullValue());
            if (!result) {
                log(ASSERT_FAILED, reason, aggregator.getConsole());
            }
        }

        // Prevent null pointer exceptions
        List<T> actualItems = ObjectUtils.defaultIfNull(actual, new ArrayList<>());
        List<T> expectedSubsetItems = ObjectUtils.defaultIfNull(expectedSubset, new ArrayList<>());
        for (int i = 0; i < expectedSubsetItems.size(); i++) {
            T expectedItem = expectedSubsetItems.get(i);

            // Try to find a matching actual item using the lambda expression
            int index = -1;
            for (int j = 0; j < actualItems.size(); j++) {
                if (matchCriteria.apply(expectedItem, actualItems.get(j))) {
                    index = j;
                    break;
                }
            }

            // Cause failure if cannot find a matching actual item
            reason = "Could not find Expected " + type + "[" + i + "] in the actual list of items";
            if (aggregator == null) {
                MatcherAssert.assertThat(reason, index, greaterThanOrEqualTo(0));
            } else {
                aggregator.assertThat(reason, index, greaterThanOrEqualTo(0));
            }

            // No comparison is applicable as no matching actual item was found
            if (index < 0) {
                log(ASSERT_FAILED, reason, aggregator.getConsole());
                continue;
            }

            // Compare the matching actual item to the expected item
            // Note:  The match can be based on a single field.  The verification will be on all the fields.
            T actualItem = actualItems.get(index);
            assertThatListItem(type + "[" + i + "]", aggregator, actualItem, expectedItem, excludeFields, true);
        }
    }

    /**
     * Verifies that all assertions were successful and writes to report/log any assertion failures
     *
     * @param aggregator - Assert Aggregator that contains all the assertion results
     */
    public static void assertThat(AssertAggregator aggregator) {
        for (AssertionError ae : aggregator.getAssertionFailures()) {
            log(ASSERT_ERROR, ae.getMessage(), aggregator.getConsole());
        }

        aggregator.verify();
    }

    /**
     * Verifies that actual &amp; expected objects are the same<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Puts objects in array and calls method that verified lists<BR>
     *
     * @param actual   - Actual
     * @param expected - Expected
     * @param <T>      - Type of object
     */
    public static <T> void assertThat(T actual, T expected) {
        assertThat(Arrays.asList(actual), Arrays.asList(expected));
    }

    /**
     * Verifies that actual &amp; expected lists are the same<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) The lists order matter as such it is advisable to sort the lists before using the method<BR>
     *
     * @param actual   - Actual List
     * @param expected - Expected List
     * @param <T>      - Type of objects in the list to compare
     */
    @SuppressWarnings({"java:S3776", "java:S2259"})
    public static <T> void assertThat(List<T> actual, List<T> expected) {
        // Consider both lists to be equal if both are null
        if (actual == null && expected == null) {
            return;
        }

        // Consider lists to be different if only one list is null
        if (actual == null || expected == null) {
            if (expected == null) {
                log("Expected List was null");
            } else {
                log("Expected List was non-null");
            }

            if (actual == null) {
                log("Actual List was null");
            } else {
                log("Actual List was non-null");
            }

            log("Expected & Actual were different see above for details");
        }

        MatcherAssert.assertThat(actual, notNullValue());
        MatcherAssert.assertThat(expected, notNullValue());

        String output;
        EqualsBuilder results = new EqualsBuilder();
        results.append(actual.size(), expected.size());
        if (results.isEquals()) {
            // Verify each item is equal
            for (int item = 0; item < expected.size(); item++) {
                // Used to hold the current item is equal
                EqualsBuilder resultsItems = new EqualsBuilder();

                // Verify each line is equal for the specific item
                String[] actualLines = new DomainObject(null).getXstream().toXML(actual.get(item)).split("\n");
                String[] expectedLines = new DomainObject(null).getXstream().toXML(expected.get(item)).split("\n");

                // Verify that the number of lines is equal for the specified item
                resultsItems.append(actualLines.length, expectedLines.length);
                if (!resultsItems.isEquals()) {
                    output = "Expected Item[%s] Number of Lines:  %s";
                    log(String.format(output, item, expectedLines.length));

                    output = "Actual Item[%s] Number of Lines:  %s";
                    log(String.format(output, item, actualLines.length));
                }

                int totalLines = Math.min(actualLines.length, expectedLines.length);
                for (int line = 0; line < totalLines; line++) {
                    // Test that the lines are equal
                    EqualsBuilder resultsLine = new EqualsBuilder();
                    resultsLine.append(actualLines[line], expectedLines[line]);

                    // Append to running total of item results
                    resultsItems.append(resultsLine.isEquals(), true);

                    // Append to running total of overall results
                    results.append(resultsLine.isEquals(), true);

                    // Write report information if current line is not correct
                    if (!resultsLine.isEquals()) {
                        output = "Expected Line[%s]:  %s";
                        log(String.format(output, line, expectedLines[line]));

                        output = "Actual Line[%s]:  %s";
                        log(String.format(output, line, actualLines[line]));
                    }
                }

                if (!resultsItems.isEquals()) {
                    output = "Expected[%s] item was not equal to Actual[%s] item see above for details";
                    log(String.format(output, item, item));
                    log("-----");
                }
            }

            if (!results.isEquals()) {
                log("Expected & Actual were different see above for details");
            }
        } else {
            output = "Number of Expected (%s) & Actual (%s) List Items was different";
            log(String.format(output, expected.size(), actual.size()));

            // Find 1st different
            ApiUtils.assertEqual(actual, expected);
        }

        // Ensure that no validation failed
        MatcherAssert.assertThat(results.isEquals(), is(true));
    }

    /**
     * Verifies that the actual list contains all the items in the subset list<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Neither list should contain null objects or an exception may occur<BR>
     *
     * @param actual - Actual List
     * @param subset - List of all items that must exist in the actual list
     * @param <T>    - Type of objects in the list to compare
     */
    @SuppressWarnings({"java:S3776", "java:S2259"})
    public static <T> void assertThatSubset(List<T> actual, List<T> subset) {
        // Consider null or empty subset to be satisfying the assert
        if (subset == null || subset.isEmpty()) {
            return;
        }

        // If the subset was not null, then actual list needs to be non-null
        if (actual == null) {
            log("The Actual List was null but the Subset List was not empty");
        }

        MatcherAssert.assertThat(actual, notNullValue());

        EqualsBuilder results = new EqualsBuilder();
        HashMap<String, Integer> cache = new HashMap<>();
        int createCacheIndex = 0;

        for (T item : subset) {
            // Assume that item is not in the list
            boolean found = false;

            // First check if item already exists in the cache
            String key = new DomainObject(null).getXstream().toXML(item);
            Integer value = cache.get(key);
            if (value == null) {
                // Not in cache, continue to build cache as we check the actual items
                for (int i = createCacheIndex; i < actual.size(); i++) {
                    // Put new actual item into the cache
                    String cacheKey = new DomainObject(null).getXstream().toXML(actual.get(i));
                    cache.put(cacheKey, i);

                    // Increment index for actual items added to cache such that no additional processing
                    // is necessary to continue building the cache later
                    createCacheIndex = i + 1;

                    // Check if the item search for is now in the cache
                    value = cache.get(key);
                    if (value != null) {
                        // Set flag to indicate item was found & stop building cache
                        found = true;
                        break;
                    }
                }

                // If item was not found, then log error information
                if (!found) {
                    log("Could not find item:  " + key);
                }
            } else {
                // It already was in the cache
                found = true;
            }

            // Append to running total of overall results
            results.append(found, true);
        }

        if (!results.isEquals()) {
            log("The Actual list did not contain all the Subset Items.  See above for details.");
        }

        // Ensure that all subset items were found in the actual list
        MatcherAssert.assertThat(results.isEquals(), is(true));
    }

    /**
     * Verifies that the actual list does not contain any of the items in the excluded list<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Neither list should contain null objects or an exception may occur<BR>
     *
     * @param actual   - Actual List
     * @param excluded - List of all items that must not exist in the actual list
     * @param <T>      - Type of objects in the list to compare
     */
    public static <T> void assertThatExcluded(List<T> actual, List<T> excluded) {
        // Consider null or empty excluded to be satisfying the assert
        if (excluded == null || excluded.isEmpty()) {
            return;
        }

        // If the excluded was not null, then actual list needs to be non-null
        if (actual == null) {
            log("The Actual List was null but the Excluded List was not empty");
        }

        MatcherAssert.assertThat(actual, notNullValue());

        EqualsBuilder results = new EqualsBuilder();
        Map<String, Integer> cache = getCache(actual);
        for (T item : excluded) {
            String key = new DomainObject(null).getXstream().toXML(item);
            boolean notFound = cache.get(key) == null;
            if (!notFound) {
                log("Found item:  " + key);
            }

            // Append to running total of overall results
            results.append(notFound, true);
        }

        if (!results.isEquals()) {
            log("The Actual list contained Excluded Items.  See above for details.");
        }

        // Ensure that all excluded items were not found in the actual list
        MatcherAssert.assertThat(results.isEquals(), is(true));
    }

    /**
     * Runs validation action only if based on field has data<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>If based on field is null, then method just returns</LI>
     * <LI>The validation action is run unless, the type is supported and there is no data</LI>
     * <LI>Supported types are ComponentPO, PageComponent, String Objects</LI>
     * </OL>
     *
     * @param basedOnField     - Based On Field
     * @param validationAction - Validation Action to be run if necessary
     * @param <T>              - Type
     */
    public static <T> void assertThat(T basedOnField, Runnable validationAction) {
        if (basedOnField == null) {
            return;
        }

        boolean runValidation = true;
        if (PageComponent.class.isAssignableFrom(basedOnField.getClass())) {
            runValidation = Utils.isNotBlank((PageComponent) basedOnField);
        } else if (ComponentPO.class.isAssignableFrom(basedOnField.getClass())) {
            runValidation = ((ComponentPO) basedOnField).hasData();
        } else if (String.class.isAssignableFrom(basedOnField.getClass())) {
            runValidation = StringUtils.isNotBlank((String) basedOnField);
        }

        if (runValidation) {
            validationAction.run();
        }
    }

    /**
     * Verify that component is displayed and component <B>text</B> is equal to test data<BR>
     * <B>Note: </B> The validations are only executed if the component is not null &amp; the data is not blank
     *
     * @param reason    - Reason
     * @param component - Component to validate against
     */
    public static void assertThatText(String reason, PageComponent component) {
        assertThat(component, () -> {
            MatcherAssert.assertThat(reason, component, isComponentDisplayed());
            MatcherAssert.assertThat(reason, component.getText(), equalTo(component.getData()));
        });
    }

    /**
     * Verify that component is displayed and component <B>value</B> is equal to test data<BR>
     * <B>Note: </B> The validations are only executed if the component is not null &amp; the data is not blank
     *
     * @param reason    - Reason
     * @param component - Component to validate against
     */
    public static void assertThatValue(String reason, PageComponent component) {
        assertThat(component, () -> {
            MatcherAssert.assertThat(reason, component, isComponentDisplayed());
            MatcherAssert.assertThat(reason, component.getValue(), equalTo(component.getData()));
        });
    }

    /**
     * Create a cache of the items. The key is the xml representation of the object
     *
     * @param items - List to generate a cache from
     * @param <T>   - Type of object
     * @return non-null HashMap
     */
    public static <T> Map<String, Integer> getCache(List<T> items) {
        Map<String, Integer> cache = new HashMap<>();

        if (items != null) {
            // Go in reverse order such that cache contains the 1st index for duplicates when complete
            for (int i = items.size() - 1; i >= 0; i--) {
                String key = new DomainObject(null).getXstream().toXML(items.get(i));
                cache.put(key, i);
            }
        }

        return cache;
    }

    /**
     * Prints objects (as XML) to console for debugging purposes
     *
     * @param objs - Object(s) to print
     * @param <T>  - Object
     */
    @SuppressWarnings("unchecked")
    public static <T> void print(T... objs) {
        if (objs == null) {
            LOG.info("null parameter passed");
        }

        for (T item : objs) {
            if (item == null) {
                LOG.info("null object");
            } else {
                String key = new DomainObject(null).getXstream().toXML(item);
                LOG.info(key);
            }
        }
    }

    @Step("Switch to window:  {1}")
    public static void switchToWindow(WebDriver driver, String windowName, String handle) {
        boolean error = true;
        try {
            driver.switchTo().window(handle);
            error = false;
        } finally {
            if (error) {
                String handles = driver.getWindowHandles().toString();
                log("Existing handles:  " + handles, true);
                log("Could not find handle:  " + handle, true);
            }
        }
    }

    @Step("Close window:  {1}")
    public static void closeWindow(WebDriver driver, String windowName) {
        driver.close();
    }

    @Step("Resize Window:  {1}")
    public static void resizeWindow(WebDriver driver, String windowName) {
        String res = TestProperties.getInstance().getScreenSize();
        if (res != null) {
            String[] resWH = res.toLowerCase().split("x");
            int width = Integer.parseInt(resWH[0].trim());
            int height = Integer.parseInt(resWH[1].trim());
            Dimension dim = new Dimension(width, height);
            driver.manage().window().setSize(dim);
        } else {
            driver.manage().window().maximize();
        }
    }

    /**
     * Validate Response Status
     *
     * @param actual   - Actual
     * @param expected - Expected
     */
    public static void validateStatus(StatusLine actual, StatusLine expected) {
        AssertAggregator aggregator = new AssertAggregator();
        aggregator.assertThat(actual.getStatusCode(), equalTo(expected.getStatusCode()));
        aggregator.assertThat(actual.getReasonPhrase(), equalTo(expected.getReasonPhrase()));
        assertThat(aggregator);
    }

    /**
     * Auto Fill Page with logging in the report<BR>
     * <B>Notes:</B><BR>
     * 1) Skips field if annotation XStreamOmitField or SkipAutoFill is present<BR>
     * 2) Skips field if not a PageComponent type<BR>
     * 3) Validates entered fields<BR>
     * 4) Number of retries set to 3<BR>
     * 5) <B>No inherited fields are auto populated</B><BR>
     *
     * @param page - Page Object to auto fill
     */
    public static void autoFillPage(PageObject page) {
        autoFillPage(page, true);
    }

    /**
     * Auto Fill Page with logging in the report<BR>
     * <B>Notes:</B><BR>
     * 1) Skips field if annotation XStreamOmitField or SkipAutoFill is present<BR>
     * 2) Skips field if not a PageComponent type<BR>
     * 3) Number of retries set to 3<BR>
     * 4) <B>No inherited fields are auto populated</B><BR>
     *
     * @param page     - Page Object to auto fill
     * @param validate - true to validate
     */
    public static void autoFillPage(PageObject page, boolean validate) {
        autoFillPage(page, validate, 3);
    }

    /**
     * Auto Fill Page with logging in the report<BR>
     * <B>Notes:</B><BR>
     * 1) Skips field if annotation XStreamOmitField or SkipAutoFill is present<BR>
     * 2) Skips field if not a PageComponent type<BR>
     * 3) If field has annotation SkipAutoValidate, then no validation is done on this specific field<BR>
     * 4) <B>No inherited fields are auto populated</B><BR>
     *
     * @param page     - Page Object to auto fill
     * @param validate - true to validate (individual fields can be overridden by annotation)
     * @param retries  - Number of times to retry if entering the field fails (either during input or validation)
     */
    public static void autoFillPage(PageObject page, boolean validate, int retries) {
        autoFillPage(page, validate, retries, false);
    }

    /**
     * Auto Fill Page with logging in the report<BR>
     * <B>Notes:</B><BR>
     * 1) Skips field if annotation XStreamOmitField or SkipAutoFill is present<BR>
     * 2) Skips field if not a PageComponent type<BR>
     * 3) If field has annotation SkipAutoValidate, then no validation is done on this specific field<BR>
     *
     * @param page     - Page Object to auto fill
     * @param validate - true to validate (individual fields can be overridden by annotation)
     * @param retries  - Number of times to retry if entering the field fails (either during input or validation)
     * @param all      - true to get all fields (including inherited fields), false to get only declared fields
     */
    @SuppressWarnings({"java:S3776", "java:S112", "java:S3011"})
    public static void autoFillPage(PageObject page, boolean validate, int retries, boolean all) {
        Field[] fields;
        if (all) {
            fields = FieldUtils.getAllFields(page.getClass());
        } else {
            fields = page.getClass().getDeclaredFields();
        }

        for (Field field : fields) {
            boolean inputField = true;
            if (field.isAnnotationPresent(XStreamOmitField.class) ||
                    field.isAnnotationPresent(SkipAutoFill.class) ||
                    !PageComponent.class.isAssignableFrom(field.getType())
            ) {
                inputField = false;
            }

            if (inputField) {
                String logFieldName = field.getName();
                if (field.isAnnotationPresent(Logging.class)) {
                    logFieldName = field.getAnnotation(Logging.class).value();
                }

                field.setAccessible(true);
                PageComponent component;
                try {
                    component = (PageComponent) field.get(page);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                boolean fieldValidation = validate;
                if (field.isAnnotationPresent(SkipAutoValidate.class)) {
                    fieldValidation = false;
                }

                setValue(logFieldName, component, fieldValidation, retries);
            }
        }
    }

    @Step("Headers Information")
    public static void logHeaders(List<Header> headers) {
        if (headers == null || headers.isEmpty()) {
            log("There were no headers");
            return;
        }

        for (Header header : headers) {
            log("header", header.toString(), false);
        }
    }

    /**
     * Get Micro Service URI
     *
     * @param service - Micro Service to lookup URI
     * @return URIBuilder
     */
    public static URIBuilder getMicroServiceUri(ConsulInstance.MicroService service) {
        ConsulInstance consul = new ConsulInstance();
        URIBuilder uri = consul.getURI(service);
        consul.close();
        String reason = "Could not get information from Consul for Micro Service:  " + service.toString();
        MatcherAssert.assertThat(reason, uri, notNullValue());
        MatcherAssert.assertThat(service.toString() + " Port Invalid", uri.getPort(), greaterThan(0));
        return uri;
    }

    /**
     * Get Micro Service URI list
     *
     * @param service - Micro Service to lookup URI list
     * @return List&lt;URIBuilder&gt;
     */
    public static List<URIBuilder> getMicroServiceUriList(ConsulInstance.MicroService service) {
        ConsulInstance consul = new ConsulInstance();
        List<URIBuilder> all = consul.getAllURI(service);
        consul.close();
        for (int i = 0; i < all.size(); i++) {
            URIBuilder item = all.get(i);
            String reason = "Could not get information from Consul for Micro Service:  " + service.toString() + "[" + i + "]";
            MatcherAssert.assertThat(reason, item, notNullValue());
            MatcherAssert.assertThat(service.toString() + "[" + i + "]" + " Port Invalid", item.getPort(), greaterThan(0));
        }

        return all;
    }

    /**
     * Get domain specific test properties that can be used to create a specific context for a test.
     *
     * @param user      - SSH User
     * @param password  - SSH Password
     * @param sshHost   - SSH Host
     * @param sshPort   - SSH Port
     * @param proxyPort - Proxy Port on the SSH machine
     * @return TestProperties to create a specific context
     */
    public static TestProperties getDomainSpecificTestProperties(String user, String password, String sshHost, int sshPort, int proxyPort) {
        MultiSshSession session = new MultiSshSession()
                .withCredentials(user, password)
                .withFirstHost(sshHost, sshPort)
                .withSecondHost(new MultiSshSession().getLocalHost(), proxyPort);
        session.connect();
        String proxy = session.getLocalHost() + ":" + session.getAssignedPort();
        TestProperties domainSpecificTestProperties = Utils.deepCopy(TestProperties.getInstance());
        domainSpecificTestProperties.setHttpProxy(proxy);
        domainSpecificTestProperties.setHttpsProxy(proxy);
        return domainSpecificTestProperties;
    }

    /**
     * Assert That one of the criteria matches before timeout occurs
     *
     * @param driver   - Driver used to initialize the Conditional Class
     * @param criteria - List of criteria to check against current condition
     * @return Index of the 1st criteria that matches the current condition (&gt;=0)
     */
    public static int assertThat(WebDriver driver, List<Criteria> criteria) {
        return assertThat(driver, criteria, TestProperties.getInstance().getElementTimeout());
    }

    /**
     * Assert That one of the criteria matches before timeout occurs
     *
     * @param driver   - Driver used to initialize the Conditional Class
     * @param criteria - List of criteria to check against current condition
     * @param timeout  - timeout (in seconds)
     * @return Index of the 1st criteria that matches the current condition (&gt;=0)
     */
    public static int assertThat(WebDriver driver, List<Criteria> criteria, int timeout) {
        Conditional conditional = new Conditional(driver, timeout);
        int result = conditional.waitForMatch(criteria, false);
        String criteriaList = (criteria == null || criteria.isEmpty()) ? "No Criteria Specified." : Arrays.toString(criteria.toArray());
        MatcherAssert.assertThat("No match found using criteria:  " + criteriaList, result, greaterThanOrEqualTo(0));
        return result;
    }

    /**
     * Mask a file by using the divider resource to create a fake image
     *
     * @param filename - File to be masked as an image
     * @return Masked file location
     */
    public static String maskFile(String filename) {
        return maskFile(filename, DIVIDER_RESOURCE);
    }

    /**
     * Mask a file by using the specified image resource to create a fake image
     *
     * @param filename      - File to be masked as an image
     * @param imageResource - Image to be used in the masking process
     * @return Masked file location
     */
    public static String maskFile(String filename, String imageResource) {
        try {
            byte[] image = FileUtils.readFileToByteArray(getFile(imageResource));
            byte[] file = FileUtils.readFileToByteArray(getFile(filename));
            String randomFilename = "IMAGE_" + System.currentTimeMillis() + ".jpg";
            File output = new File(randomFilename);
            FileUtils.writeByteArrayToFile(output, image, false);
            FileUtils.writeByteArrayToFile(output, file, true);
            FileUtils.writeByteArrayToFile(output, image, true);
            return randomFilename;
        } catch (Exception ex) {
            MatcherAssert.assertThat("Could not create fake image due to exception:  " + ex.getMessage(), false);
            return null;
        }
    }

    /**
     * Unmask the file using the divider resource
     *
     * @param input     - File that was previously masked
     * @param extension - The extension for the unmasked output file
     * @return Unmasked file location
     */
    public static String unmaskFile(String input, String extension) {
        String randomFilename = "REVERT_" + System.currentTimeMillis() + extension;
        return unmaskFile(input, DIVIDER_RESOURCE, randomFilename);
    }

    /**
     * Unmask the file using the specified image resource
     *
     * @param input          - File that was previously masked
     * @param imageResource  - Image used in the initial masking process
     * @param outputFilename - Output filename for the unmasked file
     * @return Unmasked file location
     */
    public static String unmaskFile(String input, String imageResource, String outputFilename) {
        try {
            byte[] image = FileUtils.readFileToByteArray(getFile(imageResource));
            byte[] file = FileUtils.readFileToByteArray(getFile(input));
            int from = image.length;
            int to = file.length - image.length;
            byte[] realFile = Arrays.copyOfRange(file, from, to);
            File output = new File(outputFilename);
            FileUtils.writeByteArrayToFile(output, realFile, false);
            return outputFilename;
        } catch (Exception ex) {
            MatcherAssert.assertThat("Could not revert fake image due to exception:  " + ex.getMessage(), false);
            return null;
        }
    }

    /**
     * Get File object
     *
     * @param readFolder - Read folder from resources or absolute location
     * @return File (if resource a temp file is created that will be deleted on exit.)
     */
    public static File getFile(String readFolder) {
        File temp = null;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(readFolder);
            temp = File.createTempFile("auto", null);
            temp.deleteOnExit();
            FileUtils.copyInputStreamToFile(is, temp);
            return temp;
        } catch (Exception ex) {
            FileUtils.deleteQuietly(temp);
            return new File(readFolder);
        }
    }

    /**
     * Get Environment Based File
     *
     * @param readFrom - Read from resources or absolute location
     * @return environment based file if one exists or the specified variable otherwise
     */
    public static String getEnvironmentBasedFile(String readFrom) {
        return getEnvironmentBasedFile(readFrom, TestProperties.getInstance());
    }

    /**
     * Get Environment Based File
     *
     * @param readFrom - Read from resources or absolute location
     * @param props    - Test Properties to be used
     * @return environment based file if one exists or the specified variable otherwise
     */
    public static String getEnvironmentBasedFile(String readFrom, TestProperties props) {
        String env = (props.isProdEnv()) ? "prod" : props.getEnvironmentTarget();
        if (StringUtils.isBlank(env)) {
            return readFrom;
        }

        //
        // Construct the path to the environment specific data file.
        // This should can be customized to suit your needs.
        // Currently, we are just prefixing the data file with the environment name in lowercase.
        // Example with environment as QA:  sampleTestData.xml -> qa-sampleTestData.xml
        //
        String environmentSpecific = FilenameUtils.getFullPath(readFrom)
                + env.toLowerCase() + "-" + FilenameUtils.getName(readFrom);

        //
        // Check if the specific data file exists for the environment
        //
        URL url = Thread.currentThread().getContextClassLoader().getResource(environmentSpecific);
        if (url != null) {
            return environmentSpecific;
        }

        File file = new File(environmentSpecific);
        if (file.exists() && !file.isDirectory()) {
            return environmentSpecific;
        }

        // No specific data file exists for the environment
        return readFrom;
    }

}
