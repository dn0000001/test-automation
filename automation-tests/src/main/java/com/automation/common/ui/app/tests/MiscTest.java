package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.BigDecimalUtils;
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
import java.util.ArrayList;
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
        BigDecimal actualEN1 = BigDecimalUtils.parse("$7,999.58", Locale.CANADA);
        BigDecimal expectedEN1 = new BigDecimal("7999.58");
        AssertJUtil.assertThat(actualEN1).as("EN1").isEqualByComparingTo(expectedEN1);

        BigDecimal actualEN2 = BigDecimalUtils.parse("$7,999", Locale.CANADA);
        BigDecimal expectedEN2 = new BigDecimal("7999");
        AssertJUtil.assertThat(actualEN2).as("EN2").isEqualByComparingTo(expectedEN2);
    }

    @Features("Misc")
    @Stories("Experiment - Parsing using Utils.parse")
    @Severity(SeverityLevel.MINOR)
    @Test
    public void testVariousFR() {
        BigDecimal actualFR1 = BigDecimalUtils.parse("7 999,58 $", Locale.CANADA_FRENCH);
        BigDecimal expectedFR1 = new BigDecimal("7999.58");
        AssertJUtil.assertThat(actualFR1).as("FR1").isEqualByComparingTo(expectedFR1);

        BigDecimal actualFR2 = BigDecimalUtils.parse("7 999$", Locale.CANADA_FRENCH);
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

    @Features("BigDecimalUtils")
    @Stories("Verify min function")
    @Severity(SeverityLevel.MINOR)
    @Test
    @SuppressWarnings("java:S5669")
    public void testBigDecimalMin() {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal onePointZeroOne = new BigDecimal("1.01");
        BigDecimal zeroPointZeroOne = new BigDecimal("0.01");
        BigDecimal zeroPointZeroTwo = new BigDecimal("0.02");
        BigDecimal negativeOne = new BigDecimal("-1");
        BigDecimal negativeTwo = new BigDecimal("-2");

        BigDecimal result = BigDecimalUtils.min(one);
        AssertJUtil.assertThat(one).as("Min of single item").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(negativeOne);
        AssertJUtil.assertThat(negativeOne).as("Min of negative single item").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(one, null);
        AssertJUtil.assertThat(one).as("Min - other items are null").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(one, onePointZeroOne);
        AssertJUtil.assertThat(one).as("Min - single other item is larger").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(one, zeroPointZeroOne);
        AssertJUtil.assertThat(zeroPointZeroOne).as("Min - single other item is smaller").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(negativeTwo, negativeOne);
        AssertJUtil.assertThat(negativeTwo).as("Min - negative - single other item is larger").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(negativeOne, negativeTwo);
        AssertJUtil.assertThat(negativeTwo).as("Min - negative - single other item is smaller").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(one, zeroPointZeroTwo, zeroPointZeroOne);
        AssertJUtil.assertThat(zeroPointZeroOne).as("Min - multiple other items - last min").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(zeroPointZeroOne, one, zeroPointZeroTwo);
        AssertJUtil.assertThat(zeroPointZeroOne).as("Min - multiple other items - first min").isEqualByComparingTo(result);

        result = BigDecimalUtils.min(zeroPointZeroTwo, zeroPointZeroOne, one);
        AssertJUtil.assertThat(zeroPointZeroOne).as("Min - multiple other items - middle min").isEqualByComparingTo(result);

        List<BigDecimal> all = new ArrayList<>();
        all.add(zeroPointZeroOne);
        all.add(negativeOne);
        all.add(one);
        result = BigDecimalUtils.min(zeroPointZeroTwo, all.toArray(new BigDecimal[0]));
        AssertJUtil.assertThat(negativeOne).as("Min - negative value").isEqualByComparingTo(result);
    }

    @Features("BigDecimalUtils")
    @Stories("Verify max function")
    @Severity(SeverityLevel.MINOR)
    @Test
    @SuppressWarnings("java:S5669")
    public void testBigDecimalMax() {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal onePointZeroOne = new BigDecimal("1.01");
        BigDecimal zeroPointZeroOne = new BigDecimal("0.01");
        BigDecimal zeroPointZeroTwo = new BigDecimal("0.02");
        BigDecimal negativeOne = new BigDecimal("-1");
        BigDecimal negativeTwo = new BigDecimal("-2");

        BigDecimal result = BigDecimalUtils.max(one);
        AssertJUtil.assertThat(one).as("Max of single item").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(negativeOne);
        AssertJUtil.assertThat(negativeOne).as("Max of negative single item").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(one, null);
        AssertJUtil.assertThat(one).as("Max - other items are null").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(one, onePointZeroOne);
        AssertJUtil.assertThat(onePointZeroOne).as("Max - single other item is larger").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(one, zeroPointZeroOne);
        AssertJUtil.assertThat(one).as("Max - single other item is smaller").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(negativeTwo, negativeOne);
        AssertJUtil.assertThat(negativeOne).as("Max - negative - single other item is larger").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(negativeOne, negativeTwo);
        AssertJUtil.assertThat(negativeOne).as("Max - negative - single other item is smaller").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(zeroPointZeroTwo, zeroPointZeroOne, one);
        AssertJUtil.assertThat(one).as("Max - multiple other items - last max").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(one, zeroPointZeroTwo, zeroPointZeroOne);
        AssertJUtil.assertThat(one).as("Max - multiple other items - first max").isEqualByComparingTo(result);

        result = BigDecimalUtils.max(zeroPointZeroTwo, one, zeroPointZeroOne);
        AssertJUtil.assertThat(one).as("Max - multiple other items - middle max").isEqualByComparingTo(result);

        List<BigDecimal> all = new ArrayList<>();
        all.add(zeroPointZeroOne);
        all.add(negativeOne);
        all.add(one);
        result = BigDecimalUtils.max(zeroPointZeroTwo, all.toArray(new BigDecimal[0]));
        AssertJUtil.assertThat(one).as("Max - negative value").isEqualByComparingTo(result);
    }

}
