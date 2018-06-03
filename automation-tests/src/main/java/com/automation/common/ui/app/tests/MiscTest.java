package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.Helper;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Misc tests/experiments
 */
public class MiscTest {
    public static BigDecimal parse(String amount, Locale... locales) {
        for (Locale locale : locales) {
            try {
                return parse(amount, locale);
            } catch (Exception ignore) {

            }
        }

        throw new RuntimeException("Could not parse (" + amount + ") using any of the specified locales");
    }

    public static BigDecimal parse(String amount, Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""));
    }

    @Test
    public void test_EN() throws Exception {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        Number num = df.parse("$7,999.58");
        Helper.log(String.valueOf(num.doubleValue()), true);
    }

    @Test
    public void test_EN2() throws Exception {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        Number num = df.parse("$7,999");
        Helper.log(String.valueOf(num.doubleValue()), true);
    }

    @Test
    public void test_variousEN() throws Exception {
        List<String> valuesEN = new ArrayList<>();
        valuesEN.add("$7,999.58");
        valuesEN.add("$7,999");

        for (String item : valuesEN) {
            Helper.log(parse(item, Locale.CANADA).toString(), true);
        }
    }

    @Test
    public void test_variousFR() throws Exception {
        List<String> valuesFR = new ArrayList<>();
        valuesFR.add("7 999,58 $");
        valuesFR.add("7 999$");

        for (String item : valuesFR) {
            Helper.log(parse(item, Locale.CANADA_FRENCH).toString(), true);
        }
    }

    @Test
    public void test_FR() throws Exception {
        DecimalFormat df = new DecimalFormat("####,00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.CANADA_FRENCH);
        dfs.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        Number num = df.parse("7 999,58 $".replaceAll("\\s", ""));
        Helper.log(String.valueOf(num.doubleValue()), true);
    }

    @Test
    public void test_StackFunctionality() {
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

    @Test
    public void test_array2ListPrimitive() {
        int[] arrayOfInt = new int[]{1, 2, 8, 10, 5};
        List<Integer> listOfInt = Arrays.stream(arrayOfInt).boxed().collect(Collectors.toList());
        assertThat("Size", listOfInt.size(), equalTo(arrayOfInt.length));
        for (int i = 0; i < arrayOfInt.length; i++) {
            assertThat("Element[" + i + "]", listOfInt.get(i), equalTo(arrayOfInt[i]));
        }
    }

    @Test
    public void test_array2ListObject() {
        Integer[] arrayOfObjects = new Integer[]{1, 2, 8, 10, 5};
        List<Integer> listOfObjects = Arrays.stream(arrayOfObjects).collect(Collectors.toList());
        assertThat("Size", listOfObjects.size(), equalTo(arrayOfObjects.length));
        for (int i = 0; i < arrayOfObjects.length; i++) {
            assertThat("Element[" + i + "]", listOfObjects.get(i), equalTo(arrayOfObjects[i]));
        }
    }

}
