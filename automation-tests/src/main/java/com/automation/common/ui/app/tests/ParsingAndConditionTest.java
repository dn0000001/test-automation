package com.automation.common.ui.app.tests;

import com.taf.automation.expressions.BasicAndOnlyParser;
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
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Test the parsing of AND conditions
 */
@SuppressWarnings("java:S3252")
@Listeners(AllureTestNGListener.class)
public class ParsingAndConditionTest extends TestNGBase {
    private static final String STATE_EQUALS_CALIFORNIA = "STATE==CA";
    private static final String ZIP_CODE_90210 = "90210";
    private static final String ZIP_CODE_12345 = "12345";
    private static final String STREET = "123 test street";
    private static final String STATE_ZIP_MISMATCH = "State & Zip mismatch";

    private ExpressionParser getExpressionParser() {
        return new BasicAndOnlyParser()
                .withExpression(new StateEquals())
                .withExpression(new StateNotEquals())
                .withExpression(new StateEqualsFromList())
                .withExpression(new ZipCodeEquals())
                .withExpression(new ZipCodeNotEquals())
                .withExpression(new ZipCodeEqualsFromList())
                .withExpression(new ZipCodeSizeEquals5())
                .withExpression(new ZipCodeSizeEquals9());
    }

    @Features("ExpressionParser")
    @Stories("No Conditions")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNoValidConditionsTest() {
        String conditions = "FALSE"; // This is not a valid condition
        String value = "Does not matcher";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No Conditions").isFalse();
    }

    @Features("ExpressionParser")
    @Stories("Empty String")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performEmptyTest() {
        String conditions = STATE_EQUALS_CALIFORNIA;
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("Empty String").isFalse();
    }

    @Features("ExpressionParser")
    @Stories("No AND in condition match")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNoAndMatchTest() {
        String conditions = STATE_EQUALS_CALIFORNIA;
        String value = "CA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No AND in condition match").isTrue();
    }

    @Features("ExpressionParser")
    @Stories("No AND in condition mismatch")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNoAndMismatchTest() {
        String conditions = STATE_EQUALS_CALIFORNIA;
        String value = "PA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No AND in condition mismatch").isFalse();
    }

    @Features("ExpressionParser")
    @Stories("Single Operator")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performSingleOperatorTest() {
        String conditions = "STATE==CA&&ZIP==90210";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP_CODE_90210);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("Address matches").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP_CODE_12345);
        AssertJUtil.assertThat(parser.eval(address)).as(STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP_CODE_90210);
        AssertJUtil.assertThat(parser.eval(address)).as("State mismatch").isFalse();

        address.setState("CA");
        address.setZipCode(ZIP_CODE_12345);
        AssertJUtil.assertThat(parser.eval(address)).as("Zip mismatch").isFalse();
    }

    @Features("ExpressionParser")
    @Stories("Multiple Operator")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performMultipleOperatorTest() {
        String conditions = "STATE==CA&&ZIP==90210&&ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP_CODE_90210);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("Address matches").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP_CODE_12345);
        AssertJUtil.assertThat(parser.eval(address)).as(STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode("90210-6789");
        AssertJUtil.assertThat(parser.eval(address)).as(STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP_CODE_90210);
        AssertJUtil.assertThat(parser.eval(address)).as("State mismatch").isFalse();

        address.setState("CA");
        address.setZipCode(ZIP_CODE_12345);
        AssertJUtil.assertThat(parser.eval(address)).as("Zip mismatch").isFalse();
    }

    @Features("ExpressionParser")
    @Stories("Single Operator With One Unknown Condition")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performSingleOperatorWithOneUnknownConditionTest() {
        String conditions = "STATE==CA&&Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP_CODE_90210);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("No matches").isFalse();

        address.setState("NY");
        address.setZipCode(ZIP_CODE_90210);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("No matches #2").isFalse();
    }

}
