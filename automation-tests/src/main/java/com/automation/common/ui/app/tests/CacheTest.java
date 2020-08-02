package com.automation.common.ui.app.tests;

import com.google.common.collect.Maps;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Cache creation using Guava and Java 8 using lambda
 */
@Listeners(AllureTestNGListener.class)
public class CacheTest {
    private static final String AAA = "aaa";
    private static final String BBB = "bbb";
    private static final String CCC = "ccc";
    private static final String DDD = "ddd";
    private static final String EEE = "eee";
    private static final String FFF = "fff";

    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";
    private static final String VALUE3 = "value3";
    private static final String VALUE4 = "value4";
    private static final String VALUE5 = "value5";
    private static final String VALUE6 = "lastValueFor " + AAA;
    private static final String VALUE7 = "lastValueFor " + EEE;
    private static final String VALUE8 = "value8";

    private void removeExpectedNonIntersectingParameters(List<NameValuePair> actual, List<NameValuePair> expected) {
        // Create cache for better performance
        Map<String, NameValuePair> cache = Maps.uniqueIndex(actual, NameValuePair::getName);

        // Remove all the expected items based on parameter that are not in the cache
        expected.removeIf(item -> cache.get(item.getName()) == null);
    }

    private List<NameValuePair> getTestList(int size) {
        List<NameValuePair> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            items.add(new BasicNameValuePair(Rand.alphanumeric(5, 10), Rand.alphanumeric(20, 30)));
        }

        return items;
    }

    @Features("Cache")
    @Stories("Cache creation example")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void createCacheExample() {
        List<NameValuePair> items = getTestList(10);
        Map<String, NameValuePair> cache = Maps.uniqueIndex(items, NameValuePair::getName);
        Helper.log("items:  " + Arrays.toString(items.toArray()), true);
        Helper.log("cache:  " + cache.toString(), true);
        Helper.log("", true);
    }

    @Features("Cache")
    @Stories("Get a subset of a list using a cache")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void subsetExample() {
        int createItems = 10;
        List<NameValuePair> actual = getTestList(createItems);
        Helper.log("Actual:  " + Arrays.toString(actual.toArray()), true);

        int max = Rand.randomRange(1, 5);
        Helper.log("Number of actual items to add to the expected list:  " + max, true);
        List<NameValuePair> expected = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            expected.add(new BasicNameValuePair(actual.get(i).getName(), actual.get(i).getValue()));
        }

        for (int i = 0; i < createItems - max; i++) {
            expected.add(new BasicNameValuePair(Rand.alphanumeric(5, 10), Rand.alphanumeric(20, 30)));
        }

        //
        // Same size
        //
        Collections.shuffle(expected);
        Helper.log("Expected Count #1:   " + expected.size(), true);
        Helper.log("Expected Before #1:  " + Arrays.toString(expected.toArray()), true);
        removeExpectedNonIntersectingParameters(actual, expected);
        Helper.assertThatSubset(actual, expected);
        Helper.log("Expected After #1:   " + Arrays.toString(expected.toArray()), true);
        Helper.log("", true);

        //
        // Smaller minus 1 item to be found
        //
        expected.remove(0);
        Collections.shuffle(expected);
        Helper.log("Expected Count #2:   " + expected.size(), true);
        removeExpectedNonIntersectingParameters(actual, expected);
        Helper.assertThatSubset(actual, expected);
        Helper.log("Expected After #2:   " + Arrays.toString(expected.toArray()), true);
        Helper.log("", true);

        //
        // Larger
        //
        for (int i = 0; i < createItems; i++) {
            expected.add(new BasicNameValuePair(Rand.alphanumeric(5, 10), Rand.alphanumeric(20, 30)));
        }

        Collections.shuffle(expected);
        Helper.log("Expected Count #3:   " + expected.size(), true);
        Helper.log("Expected Before #3:  " + Arrays.toString(expected.toArray()), true);
        removeExpectedNonIntersectingParameters(actual, expected);
        Helper.assertThatSubset(actual, expected);
        Helper.log("Expected After #3:   " + Arrays.toString(expected.toArray()), true);
        Helper.log("", true);
    }

    @Features("Cache")
    @Stories("Cache creation example using Java 8 with lambda")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void createCacheExampleUsingJava8WithLambda() {
        // Actual items that will be converted to a map (cache) with duplicates
        List<NameValuePair> actualItems = new ArrayList<>();
        actualItems.add(new BasicNameValuePair(AAA, VALUE1));
        actualItems.add(new BasicNameValuePair(BBB, VALUE2));
        actualItems.add(new BasicNameValuePair(CCC, VALUE3));
        actualItems.add(new BasicNameValuePair(DDD, VALUE4));
        actualItems.add(new BasicNameValuePair(EEE, VALUE5));
        actualItems.add(new BasicNameValuePair(AAA, VALUE6));
        actualItems.add(new BasicNameValuePair(EEE, VALUE7));

        // The Expected map (cache) after conversion
        Map<String, NameValuePair> expectedCache = new HashMap<>();
        expectedCache.put("aaa", new BasicNameValuePair(AAA, VALUE6));
        expectedCache.put("bbb", new BasicNameValuePair(BBB, VALUE2));
        expectedCache.put("ccc", new BasicNameValuePair(CCC, VALUE3));
        expectedCache.put("ddd", new BasicNameValuePair(DDD, VALUE4));
        expectedCache.put("eee", new BasicNameValuePair(EEE, VALUE7));

        // Convert the Actual items to the map (cache)
        Map<String, NameValuePair> actualCache = actualItems
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, item -> item, (lhs, rhs) -> rhs));

        // Verify that the caches are the same
        assertThat("Cache created using lambda", actualCache, equalTo(expectedCache));
    }

    @Features("Helper")
    @Stories("Validate Helper.assertThatExcluded functions properly")
    @Severity(SeverityLevel.MINOR)
    @Test
    @SuppressWarnings("java:S112")
    public void assertThatExcludedTest() {
        List<NameValuePair> actualItems = new ArrayList<>();
        actualItems.add(new BasicNameValuePair(AAA, VALUE1));
        actualItems.add(new BasicNameValuePair(BBB, VALUE2));
        actualItems.add(new BasicNameValuePair(CCC, VALUE3));
        actualItems.add(new BasicNameValuePair(DDD, VALUE4));
        actualItems.add(new BasicNameValuePair(EEE, VALUE5));
        actualItems.add(new BasicNameValuePair(AAA, VALUE6));
        actualItems.add(new BasicNameValuePair(EEE, VALUE7));

        List<NameValuePair> excludedItems = new ArrayList<>();
        excludedItems.add(new BasicNameValuePair(FFF, VALUE8));

        // The excluded list is considered first
        Helper.assertThatExcluded(null, null);
        Helper.assertThatExcluded(null, new ArrayList<>());

        try {
            Helper.assertThatExcluded(null, excludedItems);
            throw new RuntimeException("The actual list was null but the exclude was non-empty");
        } catch (AssertionError ae) {
            Helper.log("The actual list cannot be null if exclude is non-empty", true);
        }

        // An empty actual list will always pass validation
        Helper.assertThatExcluded(new ArrayList<>(), excludedItems);

        // No excluded items in the actual list
        Helper.assertThatExcluded(actualItems, excludedItems);

        // No excluded items in the actual list
        excludedItems.add(new BasicNameValuePair(FFF, VALUE8));
        excludedItems.add(new BasicNameValuePair(AAA, VALUE7));
        excludedItems.add(new BasicNameValuePair(VALUE6, EEE));
        Helper.assertThatExcluded(actualItems, excludedItems);

        try {
            excludedItems.add(new BasicNameValuePair(AAA, VALUE1));
            Helper.assertThatExcluded(actualItems, excludedItems);
            throw new RuntimeException("The actual list contained a single excluded item");
        } catch (AssertionError ae) {
            Helper.log("Single excluded item in the actual list", true);
        }

        try {
            excludedItems.add(new BasicNameValuePair(AAA, VALUE6));
            excludedItems.add(new BasicNameValuePair(DDD, VALUE4));
            Helper.assertThatExcluded(actualItems, excludedItems);
            throw new RuntimeException("The actual list contained multiple excluded items");
        } catch (AssertionError ae) {
            Helper.log("Multiple excluded items in the actual list", true);
        }
    }

    @Features("Helper")
    @Stories("Validate Helper.assertThatSubset functions properly")
    @Severity(SeverityLevel.MINOR)
    @Test
    @SuppressWarnings("java:S112")
    public void assertThatSubsetTest() {
        List<NameValuePair> actualItems = new ArrayList<>();
        actualItems.add(new BasicNameValuePair(AAA, VALUE1));
        actualItems.add(new BasicNameValuePair(BBB, VALUE2));
        actualItems.add(new BasicNameValuePair(CCC, VALUE3));
        actualItems.add(new BasicNameValuePair(DDD, VALUE4));
        actualItems.add(new BasicNameValuePair(EEE, VALUE5));
        actualItems.add(new BasicNameValuePair(AAA, VALUE6));
        actualItems.add(new BasicNameValuePair(EEE, VALUE7));

        List<NameValuePair> subsetItems = new ArrayList<>();
        subsetItems.add(new BasicNameValuePair(AAA, VALUE1));

        // The subset list is considered first
        Helper.assertThatSubset(null, null);
        Helper.assertThatSubset(null, new ArrayList<>());

        try {
            Helper.assertThatSubset(null, subsetItems);
            throw new RuntimeException("The actual list was null but the subset was non-empty");
        } catch (AssertionError ae) {
            Helper.log("The actual list cannot be null if subset is non-empty", true);
        }

        try {
            Helper.assertThatSubset(new ArrayList<>(), subsetItems);
            throw new RuntimeException("The actual list was empty and could not contain any subset items");
        } catch (AssertionError ae) {
            Helper.log("An empty actual list will always fail validation if subset is non-empty", true);
        }

        // No missing subset items
        Helper.assertThatSubset(actualItems, subsetItems);

        // No missing subset items
        subsetItems.add(new BasicNameValuePair(EEE, VALUE7));
        subsetItems.add(new BasicNameValuePair(DDD, VALUE4));
        Helper.assertThatSubset(actualItems, subsetItems);

        try {
            subsetItems.add(new BasicNameValuePair(FFF, VALUE8));
            Helper.assertThatSubset(actualItems, subsetItems);
            throw new RuntimeException("The actual list contained the missing subset item");
        } catch (AssertionError ae) {
            Helper.log("Single subset item was missing", true);
        }

        try {
            subsetItems.add(new BasicNameValuePair(DDD, VALUE7));
            subsetItems.add(new BasicNameValuePair(VALUE4, DDD));
            subsetItems.add(new BasicNameValuePair(VALUE7, EEE));
            Helper.assertThatSubset(actualItems, subsetItems);
            throw new RuntimeException("The actual list contained multiple missing subset items");
        } catch (AssertionError ae) {
            Helper.log("Multiple subset items were missing", true);
        }
    }

}
