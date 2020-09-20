package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.PrimeFacesRadioButtonGroup;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class SeleniumEasyInputFormDemoPage extends PageObjectV2 {
    @FindBy(xpath = "//*[@name='hosting']/../..")
    private PrimeFacesRadioButtonGroup doYouHaveHosting;

    public SeleniumEasyInputFormDemoPage() {
        super();
    }

    public SeleniumEasyInputFormDemoPage(TestContext context) {
        super(context);
    }

    @Step("Select Do You Have Hosting?")
    public void selectDoYouHaveHosting() {
        setElementValueV2(doYouHaveHosting);
    }

    @Step("Fill Input Form Demo page")
    public void fill() {
        selectDoYouHaveHosting();
    }

}
