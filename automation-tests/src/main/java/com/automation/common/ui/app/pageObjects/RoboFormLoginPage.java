package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CheckBoxAJAX;
import com.automation.common.ui.app.components.CheckBoxLabel;
import com.automation.common.ui.app.components.TextBox;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.JsUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.pagecomponent.PageComponent;

import static com.taf.automation.ui.support.util.AssertsUtil.componentCannotBeSetFast;
import static org.hamcrest.MatcherAssert.assertThat;

public class RoboFormLoginPage extends PageObjectV2 {
    private static final String VALUE_TO_BE_SET = "false";

    @FindBy(id = "username")
    private TextBox emailOrUserId;

    @FindBy(id = "password")
    private TextBox password;

    @FindBy(id = "safe_device")
    private CheckBoxAJAX rememberAJAX;

    @FindBy(xpath = "//*[@id='safe_device']/..")
    private CheckBoxLabel rememberLabel;

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
        disableFieldAndValidateRememberLabel();
        disableFieldAndValidateRememberAJAX();
    }

    private void disableField(PageComponent component) {
        disableField(component.getCoreElement());
    }

    private void disableField(WebElement element) {
        JsUtils.addAttribute(element, "disabled", "");
    }

    private void makeCheckBoxVisible(By locator) {
        WebElement element = getDriver().findElement(locator);
        JsUtils.execute(getDriver(), "arguments[0].style.opacity = 1;", element);
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

    @Step("Disable And Validate:  Remember (Label)")
    private void disableFieldAndValidateRememberLabel() {
        disableField(rememberLabel.getInput());
        assertThat("Remember (Label)", rememberLabel, componentCannotBeSetFast(VALUE_TO_BE_SET));
    }

    @Step("Disable And Validate:  Remember (AJAX)")
    private void disableFieldAndValidateRememberAJAX() {
        // On this page the element is considered hidden, it is necessary to make it displayed for the test
        makeCheckBoxVisible(rememberAJAX.getLocator());
        disableField(rememberAJAX);
        assertThat("Remember (AJAX)", rememberAJAX, componentCannotBeSetFast(VALUE_TO_BE_SET));
    }

}
