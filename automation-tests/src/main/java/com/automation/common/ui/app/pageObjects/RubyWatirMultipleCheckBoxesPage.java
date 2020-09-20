package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.CheckBoxBasic;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

public class RubyWatirMultipleCheckBoxesPage extends PageObjectV2 {
    @FindBy(xpath = "//*[@value='soccer']")
    private CheckBoxBasic soccer;

    @FindBy(xpath = "//*[@value='football']")
    private CheckBoxBasic football;

    @FindBy(xpath = "//*[@value='baseball']")
    private CheckBoxBasic baseball;

    @FindBy(xpath = "//*[@value='basketball']")
    private CheckBoxBasic basketball;

    public RubyWatirMultipleCheckBoxesPage() {
        super();
    }

    public RubyWatirMultipleCheckBoxesPage(TestContext context) {
        super(context);
    }

    @Step("Select Soccer")
    public void selectSoccer() {
        setElementValueV2(soccer);
    }

    @Step("Select Football")
    public void selectFootball() {
        setElementValueV2(football);
    }

    @Step("Select Baseball")
    public void selectBaseball() {
        setElementValueV2(baseball);
    }

    @Step("Select Basketball")
    public void selectBasketball() {
        setElementValueV2(basketball);
    }

    @Step("Fill Multiple Checkboxes page")
    public void fill() {
        selectSoccer();
        selectFootball();
        selectBaseball();
        selectBasketball();
    }

}
