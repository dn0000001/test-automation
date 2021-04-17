package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CheckBoxAJAX;
import com.automation.common.ui.app.components.CheckBoxBasic;
import com.automation.common.ui.app.components.CheckBoxLabel;
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

public class SeleniumEasyCheckBoxDemoPage extends PageObjectV2 {
    private static final String VALUE_TO_BE_SET = "true";

    @FindBy(xpath = "//*[text()='Option 1']/..")
    private CheckBoxLabel option1;

    @FindBy(xpath = "//*[text()='Option 2']/..")
    private CheckBoxLabel option2;

    @FindBy(xpath = "//*[text()='Option 3']/..")
    private CheckBoxLabel option3;

    @FindBy(xpath = "//*[text()='Option 4']/..")
    private CheckBoxLabel option4;

    @FindBy(xpath = "//*[text()='Option 1']/input")
    private CheckBoxAJAX option1asAJAX;

    @FindBy(xpath = "//*[text()='Option 1']/input")
    private CheckBoxBasic option1asBasic;

    public SeleniumEasyCheckBoxDemoPage() {
        super();
    }

    public SeleniumEasyCheckBoxDemoPage(TestContext context) {
        super(context);
    }

    @Step("Select Option 1")
    public void selectOption1() {
        setElementValueV2(option1);
    }

    @Step("Select Option 2")
    public void selectOption2() {
        setElementValueV2(option2);
    }

    @Step("Select Option 3")
    public void selectOption3() {
        setElementValueV2(option3);
    }

    @Step("Select Option 4")
    public void selectOption4() {
        setElementValueV2(option4);
    }

    @Step("Fill Checkbox Demo page")
    public void fill() {
        selectOption1();
        selectOption2();
        selectOption3();
        selectOption4();
    }

    private void disableField(PageComponent component) {
        disableField(component.getCoreElement());
    }

    private void disableField(WebElement element) {
        JsUtils.addAttribute(element, "disabled", "");
    }

    @Step("Disable Fields And Validate Cannot Set")
    public void disableFieldsAndValidateCannotSet() {
        disableFieldAndValidateOption1Label();
        disableFieldAndValidateOption1AJAX();
        disableFieldAndValidateOption1Basic();
    }

    @Step("Disable And Validate:  option1 (Label)")
    private void disableFieldAndValidateOption1Label() {
        disableField(option1.getInput());
        assertThat("option1 (Label)", option1, componentCannotBeSetFast(VALUE_TO_BE_SET));
    }

    @Step("Disable And Validate:  option1 (AJAX)")
    private void disableFieldAndValidateOption1AJAX() {
        disableField(option1asAJAX);
        assertThat("option1 (AJAX)", option1asAJAX, componentCannotBeSetFast(VALUE_TO_BE_SET));
    }

    @Step("Disable And Validate:  option1 (Basic)")
    private void disableFieldAndValidateOption1Basic() {
        disableField(option1asBasic);
        assertThat("option1 (Basic)", option1asBasic, componentCannotBeSetFast(VALUE_TO_BE_SET));
    }

    @Step("Disable Fields And Validate Cannot Set using AssertJ")
    public void disableFieldsAndValidateCannotSetAssertJ() {
        disableFieldAndValidateOption1LabelAssertJ();
        disableFieldAndValidateOption1AJAXAssertJ();
        disableFieldAndValidateOption1BasicAssertJ();
    }

    @Step("Disable And Validate:  Option1 (Label) using AssertJ")
    private void disableFieldAndValidateOption1LabelAssertJ() {
        disableField(option1.getInput());
        AssertJUtil.assertThat(option1).as("Option1 (Label)").cannotBeSetFast(VALUE_TO_BE_SET);
    }

    @Step("Disable And Validate:  Option1 (AJAX) using AssertJ")
    private void disableFieldAndValidateOption1AJAXAssertJ() {
        disableField(option1asAJAX);
        AssertJUtil.assertThat(option1asAJAX).as("Option1 (AJAX)").cannotBeSetFast(VALUE_TO_BE_SET);
    }

    @Step("Disable And Validate:  Option1 (Basic) using AssertJ")
    private void disableFieldAndValidateOption1BasicAssertJ() {
        disableField(option1asBasic);
        AssertJUtil.assertThat(option1asBasic).as("Option1 (Basic)").cannotBeSetFast(VALUE_TO_BE_SET);
    }

}
