package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.PrimeFacesDashboardDO;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class PrimeFacesDashboardTest extends TestNGBase {
    @Features("Framework")
    @Stories("Page Object with Locators (like a component)")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"data-set"})
    @Test
    public void testPageObjectWithLocator(@Optional("data/ui/PrimeFacesDashboard_TestData.xml") String dataSet) {
        new Navigation(getContext()).toPrimefacesDashboard(Utils.isCleanCookiesSupported());
        PrimeFacesDashboardDO primeFacesDashboardDO = new PrimeFacesDashboardDO(getContext()).fromResource(dataSet);
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateLocatorsNotNull();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateFakePanelLocatorIsNull();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateSportsPanel();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateLifestylePanel();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validatePoliticsPanel();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateFinancePanel();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateWeatherPanel();
        primeFacesDashboardDO.getPrimeFacesDashboardPage().validateDocumentationText();
    }

}
