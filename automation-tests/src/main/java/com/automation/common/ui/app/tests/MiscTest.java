package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.AssertJUtil;
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

/**
 * Misc tests/experiments
 */
@SuppressWarnings("java:S3252")
@Listeners(AllureTestNGListener.class)
public class MiscTest {
    @Features("Misc")
    @Stories("Experiment - Parsing to double value")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testEN() throws ParseException {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        Number num = df.parse("$7,999.58");
        AssertJUtil.assertThat(num.doubleValue()).as("Double Value EN").isEqualTo(7999.58);
    }

    @Features("Misc")
    @Stories("Experiment - Parsing to double value")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testEN2() throws ParseException {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        Number num = df.parse("$7,999");
        AssertJUtil.assertThat(num.doubleValue()).as("Double Value EN2").isEqualTo(7999.0);
    }

    @Features("Misc")
    @Stories("Experiment - Parsing using Utils.parse")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testVariousEN() {
        BigDecimal actualEN1 = Utils.parse("$7,999.58", Locale.CANADA);
        BigDecimal expectedEN1 = new BigDecimal("7999.58");
        AssertJUtil.assertThat(actualEN1).as("EN1").isEqualByComparingTo(expectedEN1);

        BigDecimal actualEN2 = Utils.parse("$7,999", Locale.CANADA);
        BigDecimal expectedEN2 = new BigDecimal("7999");
        AssertJUtil.assertThat(actualEN2).as("EN2").isEqualByComparingTo(expectedEN2);
    }

    @Features("Misc")
    @Stories("Experiment - Parsing using Utils.parse")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testVariousFR() {
        BigDecimal actualFR1 = Utils.parse("7 999,58 $", Locale.CANADA_FRENCH);
        BigDecimal expectedFR1 = new BigDecimal("7999.58");
        AssertJUtil.assertThat(actualFR1).as("FR1").isEqualByComparingTo(expectedFR1);

        BigDecimal actualFR2 = Utils.parse("7 999$", Locale.CANADA_FRENCH);
        BigDecimal expectedFR2 = new BigDecimal("7999");
        AssertJUtil.assertThat(actualFR2).as("FR2").isEqualByComparingTo(expectedFR2);
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
        AssertJUtil.assertThat(num.doubleValue()).as("Double Value FR").isEqualTo(7999.58);
    }

    @Features("Misc")
    @Stories("Experiment - Stack Functionality")
    @Severity(SeverityLevel.MINOR)
    @Test
    @SuppressWarnings("java:S1149")
    public void testStackFunctionality() {
        Stack<String> stack = new Stack<>();
        AssertJUtil.assertThat(stack).as("Empty Stack").isEmpty();

        stack.push("d");
        AssertJUtil.assertThat(stack).as("Stack should have 1 item").isNotEmpty();
        AssertJUtil.assertThat(stack.peek()).as("Stack Peek").isEqualTo("d");

        AssertJUtil.assertThat(stack.pop()).as("Stack Pop").isEqualTo("d");
        AssertJUtil.assertThat(stack).as("Empty Stack #2").isEmpty();

        stack.push("m");
        stack.push("z");
        stack.push("a");
        stack.push("g");

        AssertJUtil.assertThat(stack.search("y")).as("Stack Search - Not Found").isEqualTo(-1);
        AssertJUtil.assertThat(stack.search("z")).as("Stack Search - Found").isEqualTo(3);
        AssertJUtil.assertThat(stack).as("Stack should have 4 items").isNotEmpty();
        AssertJUtil.assertThat(stack.peek()).as("Stack Peek #1").isEqualTo("g");
        AssertJUtil.assertThat(stack.pop()).as("Stack Pop #1").isEqualTo("g");
        AssertJUtil.assertThat(stack.peek()).as("Stack Peek #2").isEqualTo("a");
        AssertJUtil.assertThat(stack.pop()).as("Stack Pop #2").isEqualTo("a");
        AssertJUtil.assertThat(stack.peek()).as("Stack Peek #3").isEqualTo("z");
        AssertJUtil.assertThat(stack.pop()).as("Stack Pop #3").isEqualTo("z");
        AssertJUtil.assertThat(stack.peek()).as("Stack Peek #4").isEqualTo("m");
        AssertJUtil.assertThat(stack.pop()).as("Stack Pop #4").isEqualTo("m");
        AssertJUtil.assertThat(stack).as("Empty Stack #3").isEmpty();
    }

    @Features("Misc")
    @Stories("Experiment - Array to List Primitive")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testArray2ListPrimitive() {
        int[] arrayOfInt = new int[]{1, 2, 8, 10, 5};
        List<Integer> listOfInt = Arrays.stream(arrayOfInt).boxed().collect(Collectors.toList());
        AssertJUtil.assertThat(listOfInt.size()).as("Size").isEqualTo(arrayOfInt.length);
        for (int i = 0; i < arrayOfInt.length; i++) {
            AssertJUtil.assertThat(listOfInt.get(i)).as("Element[" + i + "]").isEqualTo(arrayOfInt[i]);
        }
    }

    @Features("Misc")
    @Stories("Experiment - Array to List Object")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testArray2ListObject() {
        Integer[] arrayOfObjects = new Integer[]{1, 2, 8, 10, 5};
        List<Integer> listOfObjects = Arrays.stream(arrayOfObjects).collect(Collectors.toList());
        AssertJUtil.assertThat(listOfObjects.size()).as("Size").isEqualTo(arrayOfObjects.length);
        for (int i = 0; i < arrayOfObjects.length; i++) {
            AssertJUtil.assertThat(listOfObjects.get(i)).as("Element[" + i + "]").isEqualTo(arrayOfObjects[i]);
        }
    }

}
