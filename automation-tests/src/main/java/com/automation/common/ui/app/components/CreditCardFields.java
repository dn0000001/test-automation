package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.JsUtils;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.pagecomponent.PageComponent;

import static com.taf.automation.ui.support.util.AssertsUtil.componentCannotBeSet;
import static com.taf.automation.ui.support.util.AssertsUtil.componentCannotBeSetFast;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * A page object that acts as a component and contains a sub-page object component
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CreditCardFields extends ComponentPO {
    private static final String DISCOVER = "Discover";

    @FindBy(css = "select[name$='type']")
    private SelectEnhanced creditCardType;

    @XStreamAlias("credit-card-number")
    @FindBy(css = "[name$='ccnumber']")
    private TextBox creditCardNumber;

    @FindBy(css = "[name$='cvc']")
    private TextBox cardVerificationCode;

    @XStreamAlias("card-expiration-date")
    @FindBy(xpath = "//*[select[contains(@name, 'ccexp_yy')] and select[contains(@name, 'ccexp_mm')]]")
    private CardExpirationDateFields cardExpirationDate;

    @FindBy(css = "[name$='cc_uname']")
    private TextBox cardUserName;

    @FindBy(css = "select[name$='type']")
    private SelectEnhancedAJAX creditCardTypeFake;

    public CreditCardFields() {
        super();
    }

    public CreditCardFields(TestContext context) {
        super(context);
    }

    @Override
    public boolean hasData() {
        return Utils.isNotBlank(creditCardType)
                || Utils.isNotBlank(creditCardNumber)
                || Utils.isNotBlank(cardVerificationCode)
                || getCardExpirationDate().hasData()
                || Utils.isNotBlank(cardUserName)
                ;
    }

    private CardExpirationDateFields getCardExpirationDate() {
        if (cardExpirationDate == null) {
            cardExpirationDate = new CardExpirationDateFields();
        }

        if (cardExpirationDate.getContext() == null) {
            cardExpirationDate.initPage(getContext());
        }

        return cardExpirationDate;
    }

    @Step("Select Credit Card Type")
    public void selectCreditCardType() {
        setElementValueV2(creditCardType);
    }

    @Step("Enter Credit Card Number")
    public void enterCreditCardNumber() {
        setElementValueV2(creditCardNumber);
    }

    @Step("Enter Card Verification Code")
    public void enterCardVerificationCode() {
        setElementValueV2(cardVerificationCode);
    }

    @Step("Enter Card User Name")
    public void enterCardUserName() {
        setElementValueV2(cardUserName);
    }

    @Override
    @Step("Fill Card Card Fields")
    public void fill() {
        selectCreditCardType();
        enterCreditCardNumber();
        enterCardVerificationCode();
        setElementValueV2(getCardExpirationDate());
        enterCardUserName();
    }

    @Override
    public void validate() {
        // No actions required to validate
    }

    @Step("Validate Credit Card Type - SelectEnhanced.setValue")
    public void validateCreditCardTypeSelectEnhancedSetValue() {
        final String CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE = "Visa (Preferred)";
        final String CREDIT_CARD_TYPE_HTML_VALUE = "9";
        final String CREDIT_CARD_TYPE_INDEX_VALUE = "1";
        final String CREDIT_CARD_TYPE_REGEX_VISUAL_TEXT_VALUE = "Visa \\(Preferred\\)";
        final String CREDIT_CARD_TYPE_INDEX_0_OPTION = "(Select Card Type)";
        final String CREDIT_CARD_TYPE_INDEX_4_OPTION = DISCOVER;
        final String CREDIT_CARD_TYPE_INDEX_5_OPTION = "Diners Club";

        final String VISIBLE_TEXT = "VISIBLE_TEXT >>> ";
        final String VALUE_HTML = "VALUE_HTML >>> ";
        final String INDEX = "INDEX >>> ";
        final String VISIBLE_TEXT_REGEX = "VISIBLE_TEXT_REGEX >>> ";
        final String VALUE_HTML_REGEX = "VALUE_HTML_REGEX >>> ";
        final String RANDOM_INDEX = "RANDOM_INDEX >>> ";
        final String RANDOM_INDEX_VALUES = "RANDOM_INDEX_VALUES >>> ";
        final String RANDOM_INDEX_RANGE = "RANDOM_INDEX_RANGE >>> ";

        //
        // Basic common ways to select the value
        //

        String reason = "Credit Card Type - Visible Text (default)";
        String value = CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE;
        String expectedValue = CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetCreditCardType();

        reason = "Credit Card Type - Visible Text (Explicit)";
        value = VISIBLE_TEXT + CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetCreditCardType();

        reason = "Credit Card Type - Value";
        value = VALUE_HTML + CREDIT_CARD_TYPE_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetCreditCardType();

        reason = "Credit Card Type - Index";
        value = INDEX + CREDIT_CARD_TYPE_INDEX_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetCreditCardType();

        reason = "Credit Card Type - RegEx Visible Text";
        value = VISIBLE_TEXT_REGEX + CREDIT_CARD_TYPE_REGEX_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetCreditCardType();

        reason = "Credit Card Type - RegEx Value";
        value = VALUE_HTML_REGEX + CREDIT_CARD_TYPE_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetCreditCardType();

        //
        // Random ways to select the value
        //

        reason = "Credit Card Type - Random Index";
        value = RANDOM_INDEX + "1";
        assertThat(reason, setCreditCardType(value), not(equalTo(CREDIT_CARD_TYPE_INDEX_0_OPTION)));
        resetCreditCardType();

        reason = "Credit Card Type - Random Index Values";
        value = RANDOM_INDEX_VALUES + "1,4";
        assertThat(
                reason,
                setCreditCardType(value),
                anyOf(equalTo(CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE), equalTo(CREDIT_CARD_TYPE_INDEX_4_OPTION))
        );
        resetCreditCardType();

        reason = "Credit Card Type - Random Index Range";
        value = RANDOM_INDEX_RANGE + "4:5";
        assertThat(
                reason,
                setCreditCardType(value),
                anyOf(equalTo(CREDIT_CARD_TYPE_INDEX_4_OPTION), equalTo(CREDIT_CARD_TYPE_INDEX_5_OPTION))
        );
        resetCreditCardType();

        //
        // Value already selected tests
        //
        resetCreditCardType();
        resetCreditCardType();

        // Set the value for the tests that follow
        setCreditCardType(CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE);

        reason = "Credit Card Type - Already Selected - Visible Text (default)";
        value = CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Credit Card Type - Already Selected - Visible Text (Explicit)";
        value = VISIBLE_TEXT + CREDIT_CARD_TYPE_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Credit Card Type - Already Selected - Value";
        value = VALUE_HTML + CREDIT_CARD_TYPE_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Credit Card Type - Already Selected - Index";
        value = INDEX + CREDIT_CARD_TYPE_INDEX_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Credit Card Type - Already Selected - RegEx Visible Text";
        value = VISIBLE_TEXT_REGEX + CREDIT_CARD_TYPE_REGEX_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Credit Card Type - Already Selected - RegEx Value";
        value = VALUE_HTML_REGEX + CREDIT_CARD_TYPE_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);
    }

    @Step("Validate SetValue:  {0}")
    private void validateSetValue(String reason, String value, String expectedValue) {
        assertThat(reason, setCreditCardType(value), equalTo(expectedValue));
    }

    @Step("Set Credit Card Type:  {0}")
    private String setCreditCardType(String value) {
        setElementValueV2(value, creditCardType);
        return creditCardType.getValue();
    }

    @Step("Reset Credit Card Type")
    private void resetCreditCardType() {
        String value = "Master Card";
        assertThat("Credit Card Type - Master Card", setCreditCardType(value), equalTo(value));
    }

    @Step("Validate Negative SelectEnhanced.setValue")
    public void validateNegativeSelectEnhancedSetValue() {
        final String WALMART_CARD = "Walmart Card";
        final String VISIBLE_TEXT = "VISIBLE_TEXT >>> ";
        final String VALUE_HTML = "VALUE_HTML >>> ";
        final String INDEX = "INDEX >>> ";
        final String VISIBLE_TEXT_REGEX = "VISIBLE_TEXT_REGEX >>> ";
        final String VALUE_HTML_REGEX = "VALUE_HTML_REGEX >>> ";

        String reason = "Visible Text does not exist";
        String value = WALMART_CARD;
        validateCannotSetInvalidOption(value, reason);

        reason = "Visible Text (Explicit) does not exist";
        value = VISIBLE_TEXT + WALMART_CARD;
        validateCannotSetInvalidOption(value, reason);

        reason = "Value does not exist";
        value = VALUE_HTML + WALMART_CARD;
        validateCannotSetInvalidOption(value, reason);

        reason = "Index greater than available options";
        value = INDEX + "1000";
        validateCannotSetInvalidOption(value, reason);

        reason = "Index less than 0";
        value = INDEX + "-5";
        validateCannotSetInvalidOption(value, reason);

        reason = "Index not a number";
        value = INDEX + "testValue";
        validateCannotSetInvalidOption(value, reason);

        reason = "RegEx Visible Text does not match";
        value = VISIBLE_TEXT_REGEX + WALMART_CARD;
        validateCannotSetInvalidOption(value, reason);

        reason = "RegEx Value does not match";
        value = VALUE_HTML_REGEX + WALMART_CARD;
        validateCannotSetInvalidOption(value, reason);
    }

    @SuppressWarnings("java:S112")
    @Step("Validate Cannot Set Invalid Option:  {0}")
    private void validateCannotSetInvalidOption(String value, String reason) {
        try {
            setElementValueV2(value, creditCardType);
            throw new RuntimeException("Assertion did not fail:  " + reason);
        } catch (AssertionError ae) {
            Helper.log("setElementValueV2 failed as expected - " + reason, true);
        }
    }

    public void validateYearSelectEnhancedSetValue() {
        getCardExpirationDate().validateYearSelectEnhancedSetValue();
    }

    @Step("Disable Fields And Validate Cannot Set")
    public void disableFieldsAndValidateCannotSet() {
        disableFieldAndValidateCreditCardNumber();
        disableFieldAndValidateCreditCardType();
        disableFieldAndValidateCreditCardTypeFake();
    }

    private void disableField(PageComponent component) {
        JsUtils.addAttribute(component.getCoreElement(), "disabled", "");
    }

    private void configureCreditCardType(PageComponent temp) {
        ((SelectEnhanced) temp).useNegativeRetryPolicy();
    }

    @Step("Disable And Validate:  Credit Card Number")
    private void disableFieldAndValidateCreditCardNumber() {
        disableField(creditCardNumber);
        assertThat("Credit Card Number", creditCardNumber, componentCannotBeSetFast("1235"));
    }

    @Step("Disable And Validate:  Credit Card Type")
    private void disableFieldAndValidateCreditCardType() {
        disableField(creditCardType);
        assertThat("Credit Card Type", creditCardType, componentCannotBeSet(DISCOVER, this::configureCreditCardType));
    }

    @Step("Disable And Validate:  Credit Card Type (Fake)")
    private void disableFieldAndValidateCreditCardTypeFake() {
        disableField(creditCardTypeFake);
        assertThat("Credit Card Type (Fake)", creditCardTypeFake, componentCannotBeSet(DISCOVER, this::configureCreditCardType));
    }

    @Step("Disable Fields And Validate Cannot Set using AssertJ")
    public void disableFieldsAndValidateCannotSetAssertJ() {
        disableFieldAndValidateCreditCardNumberAssertJ();
        disableFieldAndValidateCreditCardTypeAssertJ();
        disableFieldAndValidateCreditCardTypeFakeAssertJ();
    }

    @Step("Disable And Validate:  Credit Card Number using AssertJ")
    private void disableFieldAndValidateCreditCardNumberAssertJ() {
        disableField(creditCardNumber);
        AssertJUtil.assertThat(creditCardNumber).as("Credit Card Number").cannotBeSetFast("1235");
    }

    @Step("Disable And Validate:  Credit Card Type using AssertJ")
    private void disableFieldAndValidateCreditCardTypeAssertJ() {
        disableField(creditCardType);
        AssertJUtil.assertThat(creditCardType).as("Credit Card Type").cannotBeSet(DISCOVER, this::configureCreditCardType);
    }

    @Step("Disable And Validate:  Credit Card Type (Fake) using AssertJ")
    private void disableFieldAndValidateCreditCardTypeFakeAssertJ() {
        disableField(creditCardTypeFake);
        AssertJUtil.assertThat(creditCardTypeFake).as("Credit Card Type (Fake)").cannotBeSet(DISCOVER, this::configureCreditCardType);
    }

}
