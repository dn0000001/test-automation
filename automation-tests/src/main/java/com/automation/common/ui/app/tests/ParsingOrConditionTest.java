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
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test the parsing of OR conditions
 */
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
        assertThat("No Conditions", parser.eval(value), equalTo(false));
    }

    @Test
    public void performEmptyTest() {
        String conditions = "STATE==CA";
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("Empty String", parser.eval(value), equalTo(false));
    }

    @Test
    public void performNoOrMatchTest() {
        String conditions = "STATE==CA";
        String value = "CA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("No AND in condition match", parser.eval(value), equalTo(true));
    }

    @Test
    public void performNoOrMismatchTest() {
        String conditions = "STATE==CA";
        String value = "PA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("No AND in condition mismatch", parser.eval(value), equalTo(false));
    }

    @Test
    public void performSingleOperatorTest() {
        String conditions = "STATE==CA||ZIP==90210";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        assertThat("Address matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode("12345");
        assertThat("State & Zip mismatch", parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode("90210");
        assertThat("State mismatch", parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode("12345");
        assertThat("Zip mismatch", parser.eval(address), equalTo(true));
    }

    @Test
    public void performMultipleOperatorTest() {
        String conditions = "STATE==CA||ZIP==90210||ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        assertThat("Address matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode("12345");
        assertThat("State & Zip mismatch", parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode("90210-55");
        assertThat("State & Zip mismatch", parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode("90210-1");
        assertThat("State matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode("90210");
        assertThat("Zip matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode("12345-4564");
        assertThat("Zip size matches", parser.eval(address), equalTo(true));
    }

    @Test
    public void performSingleOperatorWithOneUnknownConditionTest() {
        String conditions = "STATE==CA||Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        assertThat("Address matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode("90210");
        address.setStreet("123 test street");
        assertThat("No matches", parser.eval(address), equalTo(false));
    }

}
