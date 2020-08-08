package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;

/**
 * Misc tests/experiments
 */
@Listeners(AllureTestNGListener.class)
public class MiscTest {
    @Features("Misc")
    @Stories("Experiment - Parsing to double value")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testEN() throws ParseException {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        Number num = df.parse("$7,999.58");
        assertThat("Double Value EN", num.doubleValue(), equalTo(7999.58));
    }

    @Features("Misc")
    @Stories("Experiment - Parsing to double value")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testEN2() throws ParseException {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        Number num = df.parse("$7,999");
        assertThat("Double Value EN2", num.doubleValue(), equalTo(7999.0));
    }

    @Features("Misc")
    @Stories("Experiment - Parsing using Utils.parse")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testVariousEN() {
        BigDecimal actualEN1 = Utils.parse("$7,999.58", Locale.CANADA);
        BigDecimal expectedEN1 = new BigDecimal("7999.58");
        assertThat("EN1", actualEN1, comparesEqualTo(expectedEN1));

        BigDecimal actualEN2 = Utils.parse("$7,999", Locale.CANADA);
        BigDecimal expectedEN2 = new BigDecimal("7999");
        assertThat("EN2", actualEN2, comparesEqualTo(expectedEN2));
    }

    @Features("Misc")
    @Stories("Experiment - Parsing using Utils.parse")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testVariousFR() {
        BigDecimal actualFR1 = Utils.parse("7 999,58 $", Locale.CANADA_FRENCH);
        BigDecimal expectedFR1 = new BigDecimal("7999.58");
        assertThat("FR1", actualFR1, comparesEqualTo(expectedFR1));

        BigDecimal actualFR2 = Utils.parse("7 999$", Locale.CANADA_FRENCH);
        BigDecimal expectedFR2 = new BigDecimal("7999");
        assertThat("FR2", actualFR2, comparesEqualTo(expectedFR2));
    }

    @Features("Misc")
    @Stories("Experiment - Parsing to double value")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testFR() throws ParseException {
        DecimalFormat df = new DecimalFormat("####,00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.CANADA_FRENCH);
        dfs.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        Number num = df.parse("7 999,58 $".replaceAll("\\s", ""));
        assertThat("Double Value FR", num.doubleValue(), equalTo(7999.58));
    }

    @Features("Misc")
    @Stories("Experiment - Stack Functionality")
    @Severity(SeverityLevel.MINOR)
    @Test
    @SuppressWarnings("java:S1149")
    public void testStackFunctionality() {
        Stack<String> stack = new Stack<>();
        assertThat("Empty Stack", stack.empty());

        stack.push("d");
        assertThat("Stack should have 1 item", stack.empty(), equalTo(false));
        assertThat("Stack Peek", stack.peek(), equalTo("d"));

        assertThat("Stack Pop", stack.pop(), equalTo("d"));
        assertThat("Empty Stack #2", stack.empty());

        stack.push("m");
        stack.push("z");
        stack.push("a");
        stack.push("g");

        assertThat("Stack Search - Not Found", stack.search("y"), equalTo(-1));
        assertThat("Stack Search - Found", stack.search("z"), equalTo(3));
        assertThat("Stack should have 4 items", stack.empty(), equalTo(false));
        assertThat("Stack Peek #1", stack.peek(), equalTo("g"));
        assertThat("Stack Pop #1", stack.pop(), equalTo("g"));
        assertThat("Stack Peek #2", stack.peek(), equalTo("a"));
        assertThat("Stack Pop #2", stack.pop(), equalTo("a"));
        assertThat("Stack Peek #3", stack.peek(), equalTo("z"));
        assertThat("Stack Pop #3", stack.pop(), equalTo("z"));
        assertThat("Stack Peek #4", stack.peek(), equalTo("m"));
        assertThat("Stack Pop #4", stack.pop(), equalTo("m"));
        assertThat("Empty Stack #3", stack.empty());
    }

    @Features("Misc")
    @Stories("Experiment - Array to List Primitive")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testArray2ListPrimitive() {
        int[] arrayOfInt = new int[]{1, 2, 8, 10, 5};
        List<Integer> listOfInt = Arrays.stream(arrayOfInt).boxed().collect(Collectors.toList());
        assertThat("Size", listOfInt.size(), equalTo(arrayOfInt.length));
        for (int i = 0; i < arrayOfInt.length; i++) {
            assertThat("Element[" + i + "]", listOfInt.get(i), equalTo(arrayOfInt[i]));
        }
    }

    @Features("Misc")
    @Stories("Experiment - Array to List Object")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testArray2ListObject() {
        Integer[] arrayOfObjects = new Integer[]{1, 2, 8, 10, 5};
        List<Integer> listOfObjects = Arrays.stream(arrayOfObjects).collect(Collectors.toList());
        assertThat("Size", listOfObjects.size(), equalTo(arrayOfObjects.length));
        for (int i = 0; i < arrayOfObjects.length; i++) {
            assertThat("Element[" + i + "]", listOfObjects.get(i), equalTo(arrayOfObjects[i]));
        }
    }

}
