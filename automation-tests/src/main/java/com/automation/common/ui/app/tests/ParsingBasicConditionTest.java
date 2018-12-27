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
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test the parsing of OR conditions &amp; AND conditions together
 */
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
        assertThat("No Conditions", parser.eval(value), equalTo(false));
    }

    @Test
    public void performEmptyTest() {
        String conditions = CALIFORNIA;
        String value = "";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("Empty String", parser.eval(value), equalTo(false));
    }

    @Test
    public void performNoAndOrMatchTest() {
        String conditions = CALIFORNIA;
        String value = "CA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("No OR/AND in condition match", parser.eval(value), equalTo(true));
    }

    @Test
    public void performNoAndOrMismatchTest() {
        String conditions = CALIFORNIA;
        String value = "PA";
        ExpressionParser parser = getExpressionParser().withConditions(conditions);
        assertThat("No OR/AND in condition mismatch", parser.eval(value), equalTo(false));
    }

    @Test
    public void performSingleOrOperatorTest() {
        String conditions = CALIFORNIA + OR + "ZIP==" + ZIP1;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        assertThat(REASON_STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1);
        assertThat(REASON_STATE_MISMATCH, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP5);
        assertThat(REASON_ZIP_MISMATCH, parser.eval(address), equalTo(true));
    }

    @Test
    public void performSingleAndOperatorTest() {
        String conditions = CALIFORNIA + AND + BEVERLY_HILLS;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        assertThat(REASON_STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1);
        assertThat(REASON_STATE_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP5);
        assertThat(REASON_ZIP_MISMATCH, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleOrOperatorTest() {
        String conditions = CALIFORNIA + OR + "ZIP==" + ZIP1 + "||ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        assertThat(REASON_STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1 + "-55");
        assertThat(REASON_STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP1 + "-1");
        assertThat("State matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP1);
        assertThat("Zip matches", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode("12345-4564");
        assertThat("Zip size matches", parser.eval(address), equalTo(true));
    }

    @Test
    public void performMultipleAndOperatorTest() {
        String conditions = CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1 + CONDITION_AND_ZIP5;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        assertThat(REASON_STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1 + "-6789");
        assertThat(REASON_STATE_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1);
        assertThat(REASON_STATE_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP5);
        assertThat(REASON_ZIP_MISMATCH, parser.eval(address), equalTo(false));
    }

    @Test
    public void performSingleMixedOperatorTest() {
        String conditions = CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1 + OR + "ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        assertThat("ZIP9", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        assertThat("ZIP9 #2", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP1);
        assertThat(REASON_STATE_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP1 + "-1");
        assertThat(REASON_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1 + "-2");
        assertThat("No Conditions match", parser.eval(address), equalTo(false));
    }

    @Test
    public void performSingleMixedOperatorReverseTest() {
        String conditions = "ZIP9" + OR + CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        assertThat("ZIP9", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP4);
        address.setStreet(STREET);
        assertThat("ZIP9 #2", parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP1);
        assertThat(REASON_STATE_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP1 + "-1");
        assertThat(REASON_ZIP_MISMATCH, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1 + "-2");
        assertThat("No Conditions match", parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsEqualTest() {
        String conditions = CALIFORNIA + "&&" + BEVERLY_HILLS + OR + NEW_YORK + "&&ZIP==12345||ZIP9";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA + CONDITION_AND_ZIP_EQUALS + ZIP1, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NEW_YORK + "&&ZIP==12345", parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode("44444-5555");
        address.setStreet(STREET);
        assertThat("ZIP9", parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode("66666");
        address.setStreet(STREET);
        assertThat("No matching conditions", parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsMoreAndScenario1Test() {
        String conditions = CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5 + OR + NEW_YORK;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(NEW_YORK, parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsMoreAndScenario2Test() {
        String conditions = CALIFORNIA + AND + BEVERLY_HILLS + OR + "ZIP5&&" + NEW_YORK;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA + AND + BEVERLY_HILLS, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat("ZIP5&&" + NEW_YORK, parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode("12345-5678");
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsMoreAndScenario3Test() {
        String conditions = NEW_YORK + OR + CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA + AND + BEVERLY_HILLS + CONDITION_AND_ZIP5, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NEW_YORK, parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsMoreOrScenario1Test() {
        String conditions = NEW_YORK + OR + CALIFORNIA + OR + PENNSYLVANIA + CONDITION_AND_ZIP5;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NEW_YORK, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA, parser.eval(address), equalTo(true));

        address.setState("PA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        assertThat(PENNSYLVANIA + CONDITION_AND_ZIP5, parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));

        address.setState("AK");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        assertThat(NO_MATCHES2, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsMoreOrScenario2Test() {
        String conditions = NEW_YORK + OR + PENNSYLVANIA + CONDITION_AND_ZIP5 + OR + CALIFORNIA;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NEW_YORK, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA, parser.eval(address), equalTo(true));

        address.setState("PA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        assertThat(PENNSYLVANIA + CONDITION_AND_ZIP5, parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));

        address.setState("AK");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        assertThat(NO_MATCHES2, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleMixedOperatorsMoreOrScenario3Test() {
        String conditions = PENNSYLVANIA + CONDITION_AND_ZIP5 + OR + NEW_YORK + OR + CALIFORNIA;
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NEW_YORK, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(CALIFORNIA, parser.eval(address), equalTo(true));

        address.setState("PA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        assertThat(PENNSYLVANIA + CONDITION_AND_ZIP5, parser.eval(address), equalTo(true));

        address.setState("AK");
        address.setZipCode(ZIP5);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));

        address.setState("AK");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        assertThat(NO_MATCHES2, parser.eval(address), equalTo(false));
    }

    @Test
    public void performSingleOperatorOrWithOneUnknownConditionTest() {
        String conditions = CALIFORNIA + "||Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performSingleOperatorAndWithOneUnknownConditionTest() {
        String conditions = CALIFORNIA + "&&Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(NO_MATCHES2, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleOperatorsWithOneUnknownConditionInAndTest() {
        String conditions = CALIFORNIA + "&&Unknown||ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES + " #2", parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP3);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleOperatorsWithOneUnknownConditionInOrTest() {
        String conditions = CALIFORNIA + "&&ZIP5||Unknown";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("NY");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performMultipleOperatorsWithMultipleUnknownConditionsTest() {
        String conditions = "ZIP==" + ZIP1 + "||Unknown1||" + CALIFORNIA + "&&Unknown2&&ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("NY");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES, parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(REASON_ADDRESS_MATCHES + " #2", parser.eval(address), equalTo(true));

        address.setState("CA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));
    }

    @Test
    public void performUnknownConditionsAlwaysCauseFailureTest() {
        String conditions = "Unknown1&&ZIP==" + ZIP1 + "||Unknown2||" + CALIFORNIA + "&&Unknown3&&ZIP5";
        USAddress address = new USAddress();
        ExpressionParser parser = getExpressionParser().withConditions(conditions);

        address.setState("CA");
        address.setZipCode(ZIP2);
        address.setStreet(STREET);
        assertThat(NO_MATCHES1, parser.eval(address), equalTo(false));

        address.setState("CA");
        address.setZipCode(ZIP1);
        address.setStreet(STREET);
        assertThat(NO_MATCHES2, parser.eval(address), equalTo(false));
    }

}
