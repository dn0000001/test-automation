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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test the parsing of AND conditions
 */
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
        assertThat("No Conditions", parser.eval(value), equalTo(false));
    }

    @Features("ExpressionParser")
    @Stories("Empty String")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performEmptyTest() {
        String conditions = STATE_EQUALS_CALIFORNIA;
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("Empty String", parser.eval(value), equalTo(false));
    }

    @Features("ExpressionParser")
    @Stories("No AND in condition match")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNoAndMatchTest() {
        String conditions = STATE_EQUALS_CALIFORNIA;
        String value = "CA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("No AND in condition match", parser.eval(value), equalTo(true));
    }

    @Features("ExpressionParser")
    @Stories("No AND in condition mismatch")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void performNoAndMismatchTest() {
        String conditions = STATE_EQUALS_CALIFORNIA;
        String value = "PA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("No AND in condition mismatch", parser.eval(value), equalTo(false));
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
        assertThat("Address matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP_CODE_12345);
        assertThat(STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP_CODE_90210);
        assertThat("State mismatch", parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP_CODE_12345);
        assertThat("Zip mismatch", parser.eval(address), equalTo(false));
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
        assertThat("Address matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP_CODE_12345);
        assertThat(STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode("90210-6789");
        assertThat(STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP_CODE_90210);
        assertThat("State mismatch", parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP_CODE_12345);
        assertThat("Zip mismatch", parser.eval(address), equalTo(false));
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
        assertThat("No matches", parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP_CODE_90210);
        address.setStreet(STREET);
        assertThat("No matches #2", parser.eval(address), equalTo(false));
    }

}
