package com.automation.common.ui.app.tests;

import com.taf.automation.expressions.BasicParser;
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
 * Test the parsing of OR conditions &amp; AND conditions together
 */
@SuppressWarnings({"java:S3252", "java:S1192"})
public class ParsingBasicConditionTest extends TestNGBase {
    private static final String STREET = "123 test street";
    private static final String CALIFORNIA = "STATE==CA";
    private static final String NEW_YORK = "STATE==NY";
    private static final String PENNSYLVANIA = "STATE==PA";
    private static final String BEVERLY_HILLS = "ZIP==90210";
    private static final String NO_MATCHES1 = "No matches";
    private static final String NO_MATCHES2 = "No matches #2";
    private static final String ZIP1 = "90210";
    private static final String ZIP2 = "98765";
    private static final String ZIP3 = "98765-4321";
    private static final String ZIP4 = "12345-6789";
    private static final String ZIP5 = "12345";
    private static final String REASON_ADDRESS_MATCHES = "Address matches";
    private static final String REASON_STATE_ZIP_MISMATCH = "State & Zip mismatch";
    private static final String REASON_STATE_MISMATCH = "State mismatch";
    private static final String REASON_ZIP_MISMATCH = "Zip mismatch";
    private static final String CONDITION_AND_ZIP5 = "&&ZIP5";
    private static final String CONDITION_AND_ZIP_EQUALS = "&&ZIP==";
    private static final String OR = "||";
    private static final String AND = "&&";

    private ExpressionParser getExpressionParser() {
        return new BasicParser()
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
        String conditions = CALIFORNIA;
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("Empty String").isFalse();
    }

    @Test
    public void performNoAndOrMatchTest() {
        String conditions = CALIFORNIA;
        String value = "CA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No OR/AND in condition match").isTrue();
    }

    @Test
    public void performNoAndOrMismatchTest() {
        String conditions = CALIFORNIA;
        String value = "PA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        AssertJUtil.assertThat(parser.eval(value)).as("No OR/AND in condition mismatch").isFalse();
    }

    @Test
    public void performSingleOrOperatorTest() {
        String conditions = CALIFORNIA + OR + "ZIP==" + ZIP1;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_MISMATCH).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ZIP_MISMATCH).isTrue();
    }

    @Test
    public void performSingleAndOperatorTest() {
        String conditions = CALIFORNIA + AND + BEVERLY_HILLS;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_MISMATCH).isFalse();

        address.setState("CA");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ZIP_MISMATCH).isFalse();
    }

    @Test
    public void performMultipleOrOperatorTest() {
        String conditions = CALIFORNIA + OR + "ZIP==" + ZIP1 + "||ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1 + "-55");
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_ZIP_MISMATCH).isFalse();

        address.setState("CA");
        address.setZipCode(ZIP1 + "-1");
        AssertJUtil.assertThat(parser.eval(address)).as("State matches").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP1);
        AssertJUtil.assertThat(parser.eval(address)).as("Zip matches").isTrue();

        address.setState("NY");
        address.setZipCode("12345-4564");
        AssertJUtil.assertThat(parser.eval(address)).as("Zip size matches").isTrue();
    }

    @Test
    public void performMultipleAndOperatorTest() {
        String conditions = CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1 + CONDITION_AND_ZIP5;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1 + "-6789");
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_MISMATCH).isFalse();

        address.setState("CA");
        address.setZipCode(ZIP5);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ZIP_MISMATCH).isFalse();
    }

    @Test
    public void performSingleMixedOperatorTest() {
        String conditions = CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1 + OR + "ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("ZIP9").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("ZIP9 #2").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP1);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_MISMATCH).isFalse();

        address.setState("CA");
        address.setZipCode(ZIP1 + "-1");
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1 + "-2");
        AssertJUtil.assertThat(parser.eval(address)).as("No Conditions match").isFalse();
    }

    @Test
    public void performSingleMixedOperatorReverseTest() {
        String conditions = "ZIP9" + OR + CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("ZIP9").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("ZIP9 #2").isTrue();

        address.setState("NY");
        address.setZipCode(ZIP1);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_STATE_MISMATCH).isFalse();

        address.setState("CA");
        address.setZipCode(ZIP1 + "-1");
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ZIP_MISMATCH).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1 + "-2");
        AssertJUtil.assertThat(parser.eval(address)).as("No Conditions match").isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsEqualTest() {
        String conditions = CALIFORNIA + "&&" + BEVERLY_HILLS + OR + NEW_YORK + "&&ZIP==12345||ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NEW_YORK + "&&ZIP==12345").isTrue();

        address.setState("AK");
        address.setZipCode("44444-5555");
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("ZIP9").isTrue();

        address.setState("AK");
        address.setZipCode("66666");
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("No matching conditions").isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsMoreAndScenario1Test() {
        String conditions = CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5 + OR + NEW_YORK;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NEW_YORK).isTrue();

        address.setState("AK");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsMoreAndScenario2Test() {
        String conditions = CALIFORNIA + AND + BEVERLY_HILLS + OR + "ZIP5&&" + NEW_YORK;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA + AND + BEVERLY_HILLS).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as("ZIP5&&" + NEW_YORK).isTrue();

        address.setState("AK");
        address.setZipCode("12345-5678");
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsMoreAndScenario3Test() {
        String conditions = NEW_YORK + OR + CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NEW_YORK).isTrue();

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsMoreOrScenario1Test() {
        String conditions = NEW_YORK + OR + CALIFORNIA + OR + PENNSYLVANIA + CONDITION_AND_ZIP5;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NEW_YORK).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA).isTrue();

        address.setState("PA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(PENNSYLVANIA + CONDITION_AND_ZIP5).isTrue();

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();

        address.setState("AK");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES2).isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsMoreOrScenario2Test() {
        String conditions = NEW_YORK + OR + PENNSYLVANIA + CONDITION_AND_ZIP5 + OR + CALIFORNIA;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NEW_YORK).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA).isTrue();

        address.setState("PA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(PENNSYLVANIA + CONDITION_AND_ZIP5).isTrue();

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();

        address.setState("AK");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES2).isFalse();
    }

    @Test
    public void performMultipleMixedOperatorsMoreOrScenario3Test() {
        String conditions = PENNSYLVANIA + CONDITION_AND_ZIP5 + OR + NEW_YORK + OR + CALIFORNIA;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NEW_YORK).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(CALIFORNIA).isTrue();

        address.setState("PA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(PENNSYLVANIA + CONDITION_AND_ZIP5).isTrue();

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();

        address.setState("AK");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES2).isFalse();
    }

    @Test
    public void performSingleOperatorOrWithOneUnknownConditionTest() {
        String conditions = CALIFORNIA + "||Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performSingleOperatorAndWithOneUnknownConditionTest() {
        String conditions = CALIFORNIA + "&&Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES2).isFalse();
    }

    @Test
    public void performMultipleOperatorsWithOneUnknownConditionInAndTest() {
        String conditions = CALIFORNIA + "&&Unknown||ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES + " #2").isTrue();

        address.setState("CA");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performMultipleOperatorsWithOneUnknownConditionInOrTest() {
        String conditions = CALIFORNIA + "&&ZIP5||Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("NY");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performMultipleOperatorsWithMultipleUnknownConditionsTest() {
        String conditions = "ZIP==" + ZIP1 + "||Unknown1||" + CALIFORNIA + "&&Unknown2&&ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES).isTrue();

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(REASON_ADDRESS_MATCHES + " #2").isTrue();

        address.setState("CA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();
    }

    @Test
    public void performUnknownConditionsAlwaysCauseFailureTest() {
        String conditions = "Unknown1&&ZIP==" + ZIP1 + "||Unknown2||" + CALIFORNIA + "&&Unknown3&&ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES1).isFalse();

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        AssertJUtil.assertThat(parser.eval(address)).as(NO_MATCHES2).isFalse();
    }

}
