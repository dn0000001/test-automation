package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CheckBoxAJAX;
import com.automation.common.ui.app.components.CheckBoxBasic;
import com.automation.common.ui.app.components.CheckBoxLabel;
import com.taf.automation.ui.support.BasicClock;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.JsUtils;
import com.taf.automation.ui.support.util.MutationObserverData;
import com.taf.automation.ui.support.util.Utils;
import net.jodah.failsafe.Failsafe;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.pagecomponent.PageComponent;

import java.util.concurrent.TimeUnit;

import static com.taf.automation.ui.support.util.AssertsUtil.componentCannotBeSetFast;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("java:S3252")
public class SeleniumEasyCheckBoxDemoPage extends PageObjectV2 {
    private static final String VALUE_TO_BE_SET = "true";
    private static final BasicClock clock = new BasicClock();

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

    private void enableField(PageComponent component) {
        enableField(component.getCoreElement());
    }

    private void enableField(WebElement element) {
        JsUtils.execute(getDriver(), "arguments[0].removeAttribute('disabled');", element);
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

    @Step("Attach Mutation Observer To Option1")
    public void attachMutationObserverToOption1() {
        JsUtils.attachMutationObserverAddRemoveAttribute(option1.getCoreElement());
    }


    @Step("Wait For Disable & Enable of Option1")
    public void waitForDisableEnableOption1() {
        // We will wait until negative timeout before triggering the disable & enable on option 1 to
        // simulate what would normally occur in an application in which this would be needed
        long triggerTime = clock.laterBy(TimeUnit.SECONDS.toMillis(TestProperties.getInstance().getNegativeTimeout()));
        Failsafe.with(Utils.getPollingRetryPolicy())
                .run(() -> validateDisableEnableOption1(triggerTime));
    }

    private void validateDisableEnableOption1(long triggerTime) {
        // We will wait until the specified time to simulate an application doing sometime that we need to
        // wait for completion before continuing.  This would not be in normal code on the assertion after it
        if (!clock.isNowBefore(triggerTime)) {
            performDisableEnableOption1();
        }

        // Check if the application has completed doing the action
        AssertJUtil.assertThat(wasDisableEnableOption1()).as("Option1 did not become disabled & enabled").isTrue();
    }

    private boolean wasDisableEnableOption1() {
        // Get the current data from the DOM and check if the application has completed the action.
        // In this case, we are waiting for any attribute to added & any attribute to be remove in any order.
        // In a real application, you would observe the data that would indicate completion.
        MutationObserverData mutationObserverData = JsUtils.getMutationObserverData();
        return mutationObserverData.isAttributeAdded() && mutationObserverData.isAttributeRemoved();
    }

    @Step("Perform Disable & Enable of Option1")
    private void performDisableEnableOption1() {
        // This simulates adding an attribute.
        // A real application might add an attribute to indicate it is processing.
        disableField(option1);

        // This simulates removing an attribute.
        // A real application might remove an attribute to indicate that it is complete.
        enableField(option1);
    }

}
