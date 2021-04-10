package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.WebComponent;

/**
 * Using this class like a component to test functionality of page object with locator which should make all locators
 * in this class relative
 */
@SuppressWarnings("java:S3252")
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
        AssertJUtil.assertThat(title.getLocator()).as("Title Locator").isNotNull();
        AssertJUtil.assertThat(panelContent.getLocator()).as("Panel Content Locator").isNotNull();
    }

    @Step("Validate Title")
    public void validateTitle() {
        AssertJUtil.assertThat(getTitle()).as("Title").isEqualTo(title.getData());
    }

    @Step("Validate Content")
    public void validateContent() {
        AssertJUtil.assertThat(getPanelContent()).as("Content").isEqualTo(panelContent.getData());
    }

}
