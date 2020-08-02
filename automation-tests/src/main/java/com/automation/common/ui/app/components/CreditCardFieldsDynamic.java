package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

/**
 * A dynamic page object that acts as a component and contains a dynamic sub-page object component
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CreditCardFieldsDynamic extends ComponentPO {
    @FindBy(css = "select[name$='type']")
    private SelectEnhanced creditCardType;

    @XStreamAlias("credit-card-number")
    @FindBy(css = "[name$='ccnumber']")
    private TextBox creditCardNumber;

    @FindBy(css = "[name$='${cvc}']")
    private TextBox cardVerificationCode;

    @XStreamAlias("card-expiration-date")
    @FindBy(xpath = "//*[select[contains(@name, 'ccexp_yy')] and select[contains(@name, 'ccexp_mm')]]")
    private CardExpirationDateFieldsDynamic cardExpirationDate;

    @FindBy(css = "[name$='cc_uname']")
    private TextBox cardUserName;

    public CreditCardFieldsDynamic() {
        super();
    }

    public CreditCardFieldsDynamic(TestContext context) {
        super(context);
    }

    public void updateCvcKey(String value) {
        getSubstitutions().put("cvc", value);
    }

    public void updateCommonFieldKey(String value) {
        getSubstitutions().put(CardExpirationDateFieldsDynamic.getCommonFieldKey(), value);
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

    private CardExpirationDateFieldsDynamic getCardExpirationDate() {
        if (cardExpirationDate == null) {
            cardExpirationDate = new CardExpirationDateFieldsDynamic();
        }

        if (cardExpirationDate.getContext() == null) {
            String key = getSubstitutions().get(CardExpirationDateFieldsDynamic.getCommonFieldKey());
            assertThat("Common Field Key", key, not(isEmptyOrNullString()));
            cardExpirationDate.updateCommonFieldKey(key);
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

}
