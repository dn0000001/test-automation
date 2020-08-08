package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.components.UnitTestComponent;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

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
 * This class is for unit testing the method Utils.splitData
 */
@Listeners(AllureTestNGListener.class)
public class SplitTest {
    @Features("Utils")
    @Stories("splitData - Split Null")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitNull() {
        String[] expectedData = new String[0];
        String[] actualData = Utils.splitData(null, ":", 2, "null");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitNull", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Min Size Negative")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testMinSizeNegative() {
        String[] expectedData = new String[]{"a", "b"};
        String[] actualData = Utils.splitData("a:b", ":");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testMinSizeNegative", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Null Placeholder")
    @Severity(SeverityLevel.MINOR)
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

    @Features("Utils")
    @Stories("splitData - Min Size")
    @Severity(SeverityLevel.MINOR)
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

    @Features("Utils")
    @Stories("splitData - Min Size")
    @Severity(SeverityLevel.MINOR)
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

    @Features("Utils")
    @Stories("splitData - Split Empty String")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitEmptyString() {
        String[] expectedData = new String[]{"", ""};
        String[] actualData = Utils.splitData("", ":", 2, "null");
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("Empty String", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo001() {
        String[] expectedData = new String[]{"", ""};
        String[] actualData = Utils.splitData(":", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_001", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo002() {
        String[] expectedData = new String[]{"ab", ""};
        String[] actualData = Utils.splitData("ab:", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_002", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo003() {
        String[] expectedData = new String[]{"", "cd"};
        String[] actualData = Utils.splitData(":cd", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_003", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo004() {
        String[] expectedData = new String[]{null, null};
        String[] actualData = Utils.splitData("null:null", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_004", builder.isEquals());
        assertThat("testSplitSizeTwo_004[0]", actualData[0], nullValue());
        assertThat("testSplitSizeTwo_004[1]", actualData[1], nullValue());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo005() {
        String[] expectedData = new String[]{"ef", null};
        String[] actualData = Utils.splitData("ef:null", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_005", builder.isEquals());
        assertThat("testSplitSizeTwo_005[1]", actualData[1], nullValue());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo006() {
        String[] expectedData = new String[]{null, "gh"};
        String[] actualData = Utils.splitData("null:gh", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_006", builder.isEquals());
        assertThat("testSplitSizeTwo_006[0]", actualData[0], nullValue());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Two")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeTwo007() {
        String[] expectedData = new String[]{"ij", "kl"};
        String[] actualData = Utils.splitData("ij:kl", ":", 2);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeTwo_007", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Three")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeThree001() {
        String[] expectedData = new String[]{"a", "b", "c"};
        String[] actualData = Utils.splitData("a:b:c", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_001", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Three")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeThree002() {
        String[] expectedData = new String[]{"a", "b", ""};
        String[] actualData = Utils.splitData("a:b:", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_002", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Three")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeThree003() {
        String[] expectedData = new String[]{"", "", ""};
        String[] actualData = Utils.splitData("::", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_003", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Three")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeThree004() {
        String[] expectedData = new String[]{"", "", ""};
        String[] actualData = Utils.splitData(":", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_004", builder.isEquals());
    }

    @Features("Utils")
    @Stories("splitData - Split Size Three")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testSplitSizeThree005() {
        String[] expectedData = new String[]{"a", "b", ""};
        String[] actualData = Utils.splitData("a:b", ":", 3);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testSplitSizeThree_005", builder.isEquals());
    }

    @Features("Utils")
    @Stories("Trim")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testTrim() {
        String[] expectedData = new String[]{"a", "b", null, "c"};
        String[] actualData = Utils.splitData("a :b : null :  c ", ":", 4);
        Utils.trim(actualData);
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(expectedData, actualData);
        assertThat("testTrim", builder.isEquals());
    }

    @Features("Java8")
    @Stories("Sorting Examples")
    @Severity(SeverityLevel.MINOR)
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
        UnitTestComponent actualItem1 = new UnitTestComponent().withText("bbb").withValue("hh");
        UnitTestComponent actualItem2 = new UnitTestComponent().withText("ccc").withValue("ii");
        UnitTestComponent actualItem3 = new UnitTestComponent().withText("zzz").withValue("ee");
        UnitTestComponent actualItem4 = new UnitTestComponent().withText("yyy").withValue("kk");
        UnitTestComponent actualItem5 = new UnitTestComponent().withText("aaa").withValue("ff");

        UnitTestComponent actualItem6 = new UnitTestComponent().withText("zzz").withValue("cc");
        UnitTestComponent actualItem7 = new UnitTestComponent().withText("yyy").withValue("ll");
        UnitTestComponent actualItem8 = new UnitTestComponent().withText("ccc").withValue("jj");
        UnitTestComponent actualItem9 = new UnitTestComponent().withText("aaa").withValue("dd");
        UnitTestComponent actualItem10 = new UnitTestComponent().withText("bbb").withValue("gg");

        List<UnitTestComponent> actualItems = new ArrayList<>();
        actualItems.add(actualItem1);
        actualItems.add(actualItem2);
        actualItems.add(actualItem3);
        actualItems.add(actualItem4);
        actualItems.add(actualItem5);
        actualItems.add(actualItem6);
        actualItems.add(actualItem7);
        actualItems.add(actualItem8);
        actualItems.add(actualItem9);
        actualItems.add(actualItem10);

        UnitTestComponent expectedItem1 = new UnitTestComponent().withText("aaa").withValue("dd");
        UnitTestComponent expectedItem2 = new UnitTestComponent().withText("aaa").withValue("ff");
        UnitTestComponent expectedItem3 = new UnitTestComponent().withText("bbb").withValue("gg");
        UnitTestComponent expectedItem4 = new UnitTestComponent().withText("bbb").withValue("hh");
        UnitTestComponent expectedItem5 = new UnitTestComponent().withText("ccc").withValue("ii");
        UnitTestComponent expectedItem6 = new UnitTestComponent().withText("ccc").withValue("jj");
        UnitTestComponent expectedItem7 = new UnitTestComponent().withText("yyy").withValue("kk");
        UnitTestComponent expectedItem8 = new UnitTestComponent().withText("yyy").withValue("ll");
        UnitTestComponent expectedItem9 = new UnitTestComponent().withText("zzz").withValue("cc");
        UnitTestComponent expectedItem10 = new UnitTestComponent().withText("zzz").withValue("ee");

        List<UnitTestComponent> expectedItems = new ArrayList<>();
        expectedItems.add(expectedItem1);
        expectedItems.add(expectedItem2);
        expectedItems.add(expectedItem3);
        expectedItems.add(expectedItem4);
        expectedItems.add(expectedItem5);
        expectedItems.add(expectedItem6);
        expectedItems.add(expectedItem7);
        expectedItems.add(expectedItem8);
        expectedItems.add(expectedItem9);
        expectedItems.add(expectedItem10);

        actualItems.sort(Comparator.comparing(UnitTestComponent::getText).thenComparing(UnitTestComponent::getValue));
        for (int i = 0; i < expectedItems.size(); i++) {
            UnitTestComponent actualElement = actualItems.get(i);
            UnitTestComponent expectedElement = expectedItems.get(i);
            assertThat("(Text,Value) Text[" + i + "]", actualElement.getText(), equalTo(expectedElement.getText()));
            assertThat("(Text,Value) Value[" + i + "]", actualElement.getValue(), equalTo(expectedElement.getValue()));
        }

        actualItems.sort(Comparator.comparing(UnitTestComponent::getValue).thenComparing(UnitTestComponent::getText));
        expectedItems.sort(Comparator.comparing(UnitTestComponent::getValue).thenComparing(UnitTestComponent::getText));
        for (int i = 0; i < expectedItems.size(); i++) {
            UnitTestComponent actualElement = actualItems.get(i);
            UnitTestComponent expectedElement = expectedItems.get(i);
            assertThat("(Value,Text) Value[" + i + "]", actualElement.getValue(), equalTo(expectedElement.getValue()));
            assertThat("(Value,Text) Text[" + i + "]", actualElement.getText(), equalTo(expectedElement.getText()));
        }

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
