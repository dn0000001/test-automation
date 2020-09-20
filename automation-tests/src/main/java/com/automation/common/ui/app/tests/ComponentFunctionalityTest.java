package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.ComponentsDO;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class ComponentFunctionalityTest extends TestNGBase {
    @Features("Framework")
    @Stories("Validate that components functions properly")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"data-set"})
    @Test
    public void validateCheckBoxBasicFunctionalityTest(@Optional("data/ui/ComponentsFunctionality_TestData.xml") String dataSet) {
        ComponentsDO componentsDO = new ComponentsDO(getContext()).fromResource(dataSet);
        validateComponentCheckBoxBasic(componentsDO);
        validateComponentCheckBoxLabel(componentsDO);
        validateComponentPrimeFacesRadioButtonGroup(componentsDO);
    }

    @Step("Validate Component:  CheckBoxBasic")
    private void validateComponentCheckBoxBasic(ComponentsDO componentsDO) {
        new Navigation(getContext()).toRubyWatirCheckboxes(Utils.isCleanCookiesSupported());
        componentsDO.getRubyWatirMultipleCheckBoxesPage().fill();
    }

    @Step("Validate Component:  CheckBoxLabel")
    private void validateComponentCheckBoxLabel(ComponentsDO componentsDO) {
        new Navigation(getContext()).toSeleniumEasyBasicCheckbox(Utils.isCleanCookiesSupported());
        componentsDO.getSeleniumEasyCheckBoxDemoPage().fill();
    }

    @Step("Validate Component:  PrimeFacesRadioButtonGroup")
    private void validateComponentPrimeFacesRadioButtonGroup(ComponentsDO componentsDO) {
        new Navigation(getContext()).toSeleniumEasyRadioButton(Utils.isCleanCookiesSupported());
        componentsDO.getSeleniumEasyInputFormDemoPage().fill();
    }

}
