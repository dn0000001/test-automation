package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CheckBoxLabel;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class SeleniumEasyCheckBoxDemoPage extends PageObjectV2 {
    @FindBy(xpath = "//*[text()='Option 1']/..")
    private CheckBoxLabel option1;

    @FindBy(xpath = "//*[text()='Option 2']/..")
    private CheckBoxLabel option2;

    @FindBy(xpath = "//*[text()='Option 3']/..")
    private CheckBoxLabel option3;

    @FindBy(xpath = "//*[text()='Option 4']/..")
    private CheckBoxLabel option4;

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

}
