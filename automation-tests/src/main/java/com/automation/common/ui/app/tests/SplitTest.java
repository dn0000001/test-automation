package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.components.SelectEnhanced;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by user on 13/08/16.
 */
public class SplitTest {
    @Test
    public void testSplitNull() {
        String[] expectedData = new String[0];
        String[] actualData = Utils.splitData(null, ":", 2, "null");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitNull", builder.isEquals());
    }

    @Test
    public void testMinSizeNegative() {
        String[] expectedData = new String[]{"a", "b"};
        String[] actualData = Utils.splitData("a:b", ":");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testMinSizeNegative", builder.isEquals());
    }

    @Test
    public void testNullPlaceholder() {
        String[] expectedData = new String[]{"a", "null"};
        String[] actualData = Utils.splitData("a:null", ":", 2, null);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testNullPlaceholder", builder.isEquals());
        assertThat("testNullPlaceholder[0]", actualData[0], is("a"));
        assertThat("testNullPlaceholder[1]", actualData[1], is("null"));
    }

    @Test
    public void testMinSize() {
        String[] expectedData = new String[]{null, ""};
        String[] actualData = Utils.splitData("null", ":", 2, "null");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testMinSize", builder.isEquals());
        assertThat("testMinSize[0]", actualData[0], nullValue());
        assertThat("testMinSize[1]", actualData[1], isEmptyString());
    }

    @Test
    public void testMinSize2() {
        String[] expectedData = new String[]{null, "", ""};
        String[] actualData = Utils.splitData("null", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testMinSize2", builder.isEquals());
        assertThat("testMinSize2[0]", actualData[0], nullValue());
        assertThat("testMinSize2[1]", actualData[1], isEmptyString());
        assertThat("testMinSize2[1]", actualData[2], isEmptyString());
    }

    @Test
    public void testSplitEmptyString() {
        String[] expectedData = new String[]{"", ""};
        String[] actualData = Utils.splitData("", ":", 2, "null");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("Empty String", builder.isEquals());
    }

    @Test
    public void testSplitSizeTwo_001() {
        String[] expectedData = new String[]{"", ""};
        String[] actualData = Utils.splitData(":", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_001", builder.isEquals());
    }

    @Test
    public void testSplitSizeTwo_002() {
        String[] expectedData = new String[]{"ab", ""};
        String[] actualData = Utils.splitData("ab:", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_002", builder.isEquals());
    }

    @Test
    public void testSplitSizeTwo_003() {
        String[] expectedData = new String[]{"", "cd"};
        String[] actualData = Utils.splitData(":cd", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_003", builder.isEquals());
    }

    @Test
    public void testSplitSizeTwo_004() {
        String[] expectedData = new String[]{null, null};
        String[] actualData = Utils.splitData("null:null", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_004", builder.isEquals());
        assertThat("testSplitSizeTwo_004[0]", actualData[0], nullValue());
        assertThat("testSplitSizeTwo_004[1]", actualData[1], nullValue());
    }

    @Test
    public void testSplitSizeTwo_005() {
        String[] expectedData = new String[]{"ef", null};
        String[] actualData = Utils.splitData("ef:null", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_005", builder.isEquals());
        assertThat("testSplitSizeTwo_005[1]", actualData[1], nullValue());
    }

    @Test
    public void testSplitSizeTwo_006() {
        String[] expectedData = new String[]{null, "gh"};
        String[] actualData = Utils.splitData("null:gh", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_006", builder.isEquals());
        assertThat("testSplitSizeTwo_006[0]", actualData[0], nullValue());
    }

    @Test
    public void testSplitSizeTwo_007() {
        String[] expectedData = new String[]{"ij", "kl"};
        String[] actualData = Utils.splitData("ij:kl", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_007", builder.isEquals());
    }

    @Test
    public void testSplitSizeThree_001() {
        String[] expectedData = new String[]{"a", "b", "c"};
        String[] actualData = Utils.splitData("a:b:c", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_001", builder.isEquals());
    }

    @Test
    public void testSplitSizeThree_002() {
        String[] expectedData = new String[]{"a", "b", ""};
        String[] actualData = Utils.splitData("a:b:", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_002", builder.isEquals());
    }

    @Test
    public void testSplitSizeThree_003() {
        String[] expectedData = new String[]{"", "", ""};
        String[] actualData = Utils.splitData("::", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_003", builder.isEquals());
    }

    @Test
    public void testSplitSizeThree_004() {
        String[] expectedData = new String[]{"", "", ""};
        String[] actualData = Utils.splitData(":", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_004", builder.isEquals());
    }

    @Test
    public void testSplitSizeThree_005() {
        String[] expectedData = new String[]{"a", "b", ""};
        String[] actualData = Utils.splitData("a:b", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_005", builder.isEquals());
    }

    @Test
    public void testTrim() {
        String[] expectedData = new String[]{"a", "b", null, "c"};
        String[] actualData = Utils.splitData("a :b : null :  c ", ":", 4);
        Utils.trim(actualData);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testTrim", builder.isEquals());
    }

    @Test
    public void testSort() {
        List<String> actual = new ArrayList<>();
        actual.add("aaa");
        actual.add("zzz");
        actual.add("ddd");

        List<String> expected = new ArrayList<>();
        expected.add("ddd");
        expected.add("aaa");
        expected.add("zzz");

        actual.sort(String::compareToIgnoreCase);
        expected.sort(String::compareToIgnoreCase);
        assertThat("sort #1", actual, equalTo(expected));

        // Example of how to sort by multiple fields
        List<SelectEnhanced> someList = new ArrayList<>();
        someList.sort(Comparator.comparing(SelectEnhanced::getValue).thenComparing(se -> se.getData()));

        // Example of how to split on each character of a string to create a list of strings
        String value = "test";
        List<String> actualValues = Arrays.asList(value.split(""));
        actualValues.sort(String::compareToIgnoreCase);

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("e");
        expectedValues.add("s");
        expectedValues.add("t");
        expectedValues.add("t");

        assertThat("sort#2", actualValues, equalTo(expectedValues));
    }

}
