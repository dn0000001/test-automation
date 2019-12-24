package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * This class will hold page objects with locators to test that they can be used just like components
 */
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

    @Step("Validate Sports Panel")
    public void validateSportsPanel() {
        getSportsPanel().validateTitle();
        getSportsPanel().validateContent();
    }

    @Step("Validate Lifestyle Panel")
    public void validateLifestylePanel() {
        getLifestylePanel().validateTitle();
        getLifestylePanel().validateContent();
    }

    @Step("Validate Politics Panel")
    public void validatePoliticsPanel() {
        getPoliticsPanel().validateTitle();
        getPoliticsPanel().validateContent();
    }

    @Step("Validate Finance Panel")
    public void validateFinancePanel() {
        getFinancePanel().validateTitle();
        getFinancePanel().validateContent();
    }

    @Step("Validate Weather Panel")
    public void validateWeatherPanel() {
        getWeatherPanel().validateTitle();
        getWeatherPanel().validateContent();
    }

}
