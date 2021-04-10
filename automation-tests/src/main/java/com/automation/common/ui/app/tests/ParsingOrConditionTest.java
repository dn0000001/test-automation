package com.automation.common.ui.app.tests;

import com.taf.automation.expressions.BasicOrOnlyParser;
import com.taf.automation.expressions.ExpressionParser;
import com.taf.automation.expressions.StateEquals;
import com.taf.automation.expressions.StateEqualsFromList;
import com.taf.automation.expressions.StateNotEquals;
import com.taf.automation.expressions.USAddress;
import com.taf.automation.expressions.ZipCodeEquals;
import com.taf.automation.expressions.ZipCodeEqualsFromList;
import com.taf.automation.expressions.ZipCodeNotEquals;
import com.taf.automation.expressions.ZipCodeSizeEquals5;
import com.taf.automation.expressions.ZipCodeSizeEquals9;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Test;

/**
 * Test the parsing of OR conditions
 */
@SuppressWarnings({"java:S3252", "java:S1192"})
public class ParsingOrConditionTest extends TestNGBase {
    private ExpressionParser getExpressionParser() {
        return new BasicOrOnlyParser()
                .withExpression(new StateEquals())
                .withExpression(new StateNotEquals())
                .withExpression(new StateEqualsFromList())
                .withExpression(new ZipCodeEquals())
                .withExpression(new ZipCodeNotEquals())
                .withExpression(new ZipCodeEqualsFromList())
                .withExpression(new ZipCodeSizeEquals5())
                .withExpression(new ZipCodeSizeEquals9());
    }

    @Test
    public void performNoValidConditionsTest() {
        String conditions = "FALSE"; // This is not a valid condition
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No Conditions").isFalse();
    }

    @Test
    public void performEmptyTest() {
        String conditions = "STATE==CA";
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("Empty String").isFalse();
    }

    @Test
    public void performNoOrMatchTest() {
        String conditions = "STATE==CA";
        String value = "CA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No AND in condition match").isTrue();
    }

    @Test
    public void performNoOrMismatchTest() {
        String conditions = "STATE==CA";
        String value = "PA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No AND in condition mismatch").isFalse();
    }

    @Test
    public void performSingleOperatorTest() {
        String conditions = "STATE==CA||ZIP==90210";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        AssertJUtil.assertThat(parser.eval(address)).as("Address matches").isTrue();

        address.setState("NY");
        address.setZipCode("12345");
        AssertJUtil.assertThat(parser.eval(address)).as("State & Zip mismatch").isFalse();

        address.setState("NY");
        address.setZipCode("90210");
        AssertJUtil.assertThat(parser.eval(address)).as("State mismatch").isTrue();

        address.setState("CA");
        address.setZipCode("12345");
        AssertJUtil.assertThat(parser.eval(address)).as("Zip mismatch").isTrue();
    }

    @Test
    public void performMultipleOperatorTest() {
        String conditions = "STATE==CA||ZIP==90210||ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        AssertJUtil.assertThat(parser.eval(address)).as("Address matches").isTrue();

        address.setState("NY");
        address.setZipCode("12345");
        AssertJUtil.assertThat(parser.eval(address)).as("State & Zip mismatch").isFalse();

        address.setState("NY");
        address.setZipCode("90210-55");
        AssertJUtil.assertThat(parser.eval(address)).as("State & Zip mismatch").isFalse();

        address.setState("CA");
        address.setZipCode("90210-1");
        AssertJUtil.assertThat(parser.eval(address)).as("State matches").isTrue();

        address.setState("NY");
        address.setZipCode("90210");
        AssertJUtil.assertThat(parser.eval(address)).as("Zip matches").isTrue();

        address.setState("NY");
        address.setZipCode("12345-4564");
        AssertJUtil.assertThat(parser.eval(address)).as("Zip size matches").isTrue();
    }

    @Test
    public void performSingleOperatorWithOneUnknownConditionTest() {
        String conditions = "STATE==CA||Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        AssertJUtil.assertThat(parser.eval(address)).as("Address matches").isTrue();

        address.setState("NY");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        AssertJUtil.assertThat(parser.eval(address)).as("No matches").isFalse();
    }

}
