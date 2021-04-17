package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.TextBox;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.JsUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.pagecomponent.PageComponent;

import static com.taf.automation.ui.support.util.AssertsUtil.componentCannotBeSetFast;
import static org.hamcrest.MatcherAssert.assertThat;

public class RoboFormLoginPage extends PageObjectV2 {
    @FindBy(id = "username")
    private TextBox emailOrUserId;

    @FindBy(id = "password")
    private TextBox password;

    public RoboFormLoginPage() {
        super();
    }

    public RoboFormLoginPage(TestContext context) {
        super(context);
    }

    @Step("Disable Fields And Validate Cannot Set")
    public void disableFieldsAndValidateCannotSet() {
        disableFieldAndValidateEmailOrUserId();
        disableFieldAndValidatePassword();
    }

    private void disableField(PageComponent component) {
        disableField(component.getCoreElement());
    }

    private void disableField(WebElement element) {
        JsUtils.addAttribute(element, "disabled", "");
    }

    @Step("Disable And Validate:  Email Or User Id")
    private void disableFieldAndValidateEmailOrUserId() {
        disableField(emailOrUserId);
        assertThat("Email Or User Id", emailOrUserId, componentCannotBeSetFast("1235"));
    }

    @Step("Disable And Validate:  Password")
    private void disableFieldAndValidatePassword() {
        disableField(password);
        assertThat("Password", password, componentCannotBeSetFast("67890"));
    }

    @Step("Disable Fields And Validate Cannot Set using AssertJ")
    public void disableFieldsAndValidateCannotSetAssertJ() {
        disableFieldAndValidateEmailOrUserIdAssertJ();
        disableFieldAndValidatePasswordAssertJ();
    }

    @Step("Disable And Validate:  Email Or User Id using AssertJ")
    private void disableFieldAndValidateEmailOrUserIdAssertJ() {
        disableField(emailOrUserId);
        AssertJUtil.assertThat(emailOrUserId).as("Email Or User Id").cannotBeSetFast("1235");
    }

    @Step("Disable And Validate:  Password using AssertJ")
    private void disableFieldAndValidatePasswordAssertJ() {
        disableField(password);
        AssertJUtil.assertThat(password).as("Password").cannotBeSetFast("67890");
    }

}
