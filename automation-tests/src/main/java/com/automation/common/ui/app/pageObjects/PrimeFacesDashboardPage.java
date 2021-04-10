package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.WebComponent;

/**
 * This class will hold page objects with locators to test that they can be used just like components
 */
@SuppressWarnings("java:S3252")
public class PrimeFacesDashboardPage extends PageObjectV2 {
    @FindBy(css = "[id$=':sports']")
    private PrimeFacesPanel sportsPanel;

    @FindBy(css = "[id$=':lifestyle']")
    private PrimeFacesPanel lifestylePanel;

    @FindBy(css = "[id$=':politics']")
    private PrimeFacesPanel politicsPanel;

    @FindBy(css = "[id$=':finance']")
    private PrimeFacesPanel financePanel;

    @FindBy(css = "[id$=':weather']")
    private PrimeFacesPanel weatherPanel;

    @XStreamOmitField
    private PrimeFacesPanel fakePanel;

    @FindBy(css = "[id$=':weather']")
    private PrimeFacesPanel weatherPanelWithNoData;

    @XStreamOmitField
    @FindBy(css = "[id$=':finance']")
    private PrimeFacesPanel financePanelWithNoData;

    @XStreamOmitField
    @FindBy(css = ".documentation-link[href*='components']")
    private WebComponent documentation;

    public PrimeFacesDashboardPage() {
        super();
    }

    public PrimeFacesDashboardPage(TestContext context) {
        super(context);
    }

    private PrimeFacesPanel getSportsPanel() {
        if (sportsPanel == null) {
            sportsPanel = new PrimeFacesPanel();
        }

        if (sportsPanel.getContext() == null) {
            sportsPanel.initPage(getContext());
        }

        return sportsPanel;
    }

    private PrimeFacesPanel getLifestylePanel() {
        if (lifestylePanel == null) {
            lifestylePanel = new PrimeFacesPanel();
        }

        if (lifestylePanel.getContext() == null) {
            lifestylePanel.initPage(getContext());
        }

        return lifestylePanel;
    }

    private PrimeFacesPanel getPoliticsPanel() {
        if (politicsPanel == null) {
            politicsPanel = new PrimeFacesPanel();
        }

        if (politicsPanel.getContext() == null) {
            politicsPanel.initPage(getContext());
        }

        return politicsPanel;
    }

    private PrimeFacesPanel getFinancePanel() {
        if (financePanel == null) {
            financePanel = new PrimeFacesPanel();
        }

        if (financePanel.getContext() == null) {
            financePanel.initPage(getContext());
        }

        return financePanel;
    }

    private PrimeFacesPanel getWeatherPanel() {
        if (weatherPanel == null) {
            weatherPanel = new PrimeFacesPanel();
        }

        if (weatherPanel.getContext() == null) {
            weatherPanel.initPage(getContext());
        }

        return weatherPanel;
    }

    private PrimeFacesPanel getFakePanel() {
        if (fakePanel == null) {
            fakePanel = new PrimeFacesPanel();
        }

        if (fakePanel.getContext() == null) {
            fakePanel.initPage(getContext());
        }

        return fakePanel;
    }

    private PrimeFacesPanel getWeatherPanelWithNoData() {
        if (weatherPanelWithNoData == null) {
            weatherPanelWithNoData = new PrimeFacesPanel();
        }

        if (weatherPanelWithNoData.getContext() == null) {
            weatherPanelWithNoData.initPage(getContext());
        }

        return weatherPanelWithNoData;
    }

    private PrimeFacesPanel getFinancePanelWithNoData() {
        if (financePanelWithNoData == null) {
            financePanelWithNoData = new PrimeFacesPanel();
        }

        if (financePanelWithNoData.getContext() == null) {
            financePanelWithNoData.initPage(getContext());
        }

        return financePanelWithNoData;
    }

    @Step("Validate Locators Not Null")
    public void validateLocatorsNotNull() {
        // Due to re-factoring the locators may be changed, this is to ensure that they are not null
        AssertJUtil.assertThat(getSportsPanel().getLocator()).as("Sports Panel Locator").isNotNull();
        AssertJUtil.assertThat(getLifestylePanel().getLocator()).as("Lifestyle Panel Locator").isNotNull();
        AssertJUtil.assertThat(getPoliticsPanel().getLocator()).as("Politics Panel Locator").isNotNull();
        AssertJUtil.assertThat(getFinancePanel().getLocator()).as("Finance Panel Locator").isNotNull();
        AssertJUtil.assertThat(getWeatherPanel().getLocator()).as("Weather Panel Locator").isNotNull();
        AssertJUtil.assertThat(getWeatherPanelWithNoData().getLocator()).as("Weather Panel With No Data Locator").isNotNull();
        AssertJUtil.assertThat(getFinancePanelWithNoData().getLocator()).as("Finance Panel With No Data Locator").isNotNull();
    }

    @Step("Validate the Fake Panel Locator is Null")
    public void validateFakePanelLocatorIsNull() {
        // Due to re-factoring the locators may be changed, this is to ensure that when no locator it remains null
        AssertJUtil.assertThat(getFakePanel().getLocator()).as("Fake Panel Locator").isNull();
    }

    @Step("Validate Sports Panel")
    public void validateSportsPanel() {
        getSportsPanel().validateLocatorsNotNull();
        getSportsPanel().validateTitle();
        getSportsPanel().validateContent();
    }

    @Step("Validate Lifestyle Panel")
    public void validateLifestylePanel() {
        getLifestylePanel().validateLocatorsNotNull();
        getLifestylePanel().validateTitle();
        getLifestylePanel().validateContent();
    }

    @Step("Validate Politics Panel")
    public void validatePoliticsPanel() {
        getPoliticsPanel().validateLocatorsNotNull();
        getPoliticsPanel().validateTitle();
        getPoliticsPanel().validateContent();
    }

    @Step("Validate Finance Panel")
    public void validateFinancePanel() {
        getFinancePanel().validateLocatorsNotNull();
        getFinancePanel().validateTitle();
        getFinancePanel().validateContent();
        AssertJUtil.assertThat(getFinancePanel().getTitle())
                .as("FinancePanelWithNoData - Title")
                .isEqualTo(getFinancePanelWithNoData().getTitle());
        AssertJUtil.assertThat(getFinancePanel().getPanelContent())
                .as("FinancePanelWithNoData - Content")
                .isEqualTo(getFinancePanelWithNoData().getPanelContent());
    }

    @Step("Validate Weather Panel")
    public void validateWeatherPanel() {
        getWeatherPanel().validateLocatorsNotNull();
        getWeatherPanel().validateTitle();
        getWeatherPanel().validateContent();
        AssertJUtil.assertThat(getWeatherPanel().getTitle())
                .as("WeatherPanelWithNoData - Title")
                .isEqualTo(getWeatherPanelWithNoData().getTitle());
        AssertJUtil.assertThat(getWeatherPanel().getPanelContent())
                .as("WeatherPanelWithNoData - Content")
                .isEqualTo(getWeatherPanelWithNoData().getPanelContent());
    }

    @Step("Validate Documentation Text")
    public void validateDocumentationText() {
        AssertJUtil.assertThat(documentation).as("Documentation").isDisplayed();
        AssertJUtil.assertThat(documentation.getText()).as("Documentation").isEqualTo("SERVER API");
    }

}
