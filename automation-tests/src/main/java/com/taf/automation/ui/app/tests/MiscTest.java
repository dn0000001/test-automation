package com.taf.automation.ui.app.tests;

import com.taf.automation.ui.support.Helper;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

}
