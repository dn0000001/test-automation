package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CreditCardFields;
import com.automation.common.ui.app.components.TextBox;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class RoboFormPage extends PageObjectV2 {
    @FindBy(css = "[name$='frstname']")
    private TextBox firstName;

    @FindBy(css = "[name$='lastname']")
    private TextBox lastName;

    private CreditCardFields creditCard;

    public RoboFormPage() {
        super();
    }

    public RoboFormPage(TestContext context) {
        super(context);
    }

    private CreditCardFields getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCardFields();
        }

        if (creditCard.getContext() == null) {
            creditCard.initPage(getContext());
        }

        return creditCard;
    }


    @Step("Enter First Name")
    public void enterFirstName() {
        setElementValueV2(firstName);
    }

    @Step("Enter Last Name")
    public void enterLastName() {
        setElementValueV2(lastName);
    }

    @Step("Fill Robo Form Page")
    public void fill() {
        enterFirstName();
        enterLastName();
        setElementValueV2(getCreditCard());
    }

}
