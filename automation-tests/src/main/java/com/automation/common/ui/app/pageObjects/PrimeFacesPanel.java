package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.WebComponent;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Using this class like a component to test functionality of page object with locator which should make all locators
 * in this class relative
 */
public class PrimeFacesPanel extends PageObjectV2 {
    @FindBy(css = ".ui-panel-title")
    private WebComponent title;

    @FindBy(css = ".ui-panel-content")
    private WebComponent panelContent;

    public PrimeFacesPanel() {
        super();
    }

    public PrimeFacesPanel(TestContext context) {
        super(context);
    }

    public String getTitle() {
        return title.getText();
    }

    public String getPanelContent() {
        return panelContent.getText();
    }

    @Step("Validate Locators Not Null")
    public void validateLocatorsNotNull() {
        // Due to re-factoring the locators may be changed, this is to ensure that they are not null
        assertThat("Title Locator", title.getLocator(), notNullValue());
        assertThat("Panel Content Locator", panelContent.getLocator(), notNullValue());
    }

    @Step("Validate Title")
    public void validateTitle() {
        assertThat("Title", getTitle(), equalTo(title.getData()));
    }

    @Step("Validate Content")
    public void validateContent() {
        assertThat("Content", getPanelContent(), equalTo(panelContent.getData()));
    }

}
