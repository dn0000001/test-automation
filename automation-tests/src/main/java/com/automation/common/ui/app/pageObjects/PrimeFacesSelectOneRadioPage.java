package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.PrimeFacesRadioButtonGroup;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class PrimeFacesSelectOneRadioPage extends PageObjectV2 {
    @FindBy(css = ".ui-selectoneradio[id$=':console'] td")
    private PrimeFacesRadioButtonGroup basic;

    public PrimeFacesSelectOneRadioPage() {
        super();
    }

    public PrimeFacesSelectOneRadioPage(TestContext context) {
        super(context);
    }

    @Step("Select Basic")
    public void selectBasic() {
        setElementValueV2(basic);
    }

    @Step("Fill SelectOneRadio page")
    public void fill() {
        selectBasic();
    }

}
