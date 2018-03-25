package com.taf.automation.ui.app.tests;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.Rand;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.*;

/**
 * Cache creation using Guava
 */
public class CacheTest {

    Function<NameValuePair, String> getNameValuePairKey() {
        return nameValuePair -> nameValuePair.getName();
    }

    public void removeExpectedNonIntersectingParameters(List<NameValuePair> actual, List<NameValuePair> expected) {
        // Create cache for better performance
        Map<String, NameValuePair> cache = Maps.uniqueIndex(actual, getNameValuePairKey());

        // Remove all the expected items based on parameter that are not in the cache
        expected.removeIf(item -> cache.get(item.getName()) == null);
    }

    List<NameValuePair> getTestList(int size) {
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
        Map<String, NameValuePair> cache = Maps.uniqueIndex(items, getNameValuePairKey());
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

}
