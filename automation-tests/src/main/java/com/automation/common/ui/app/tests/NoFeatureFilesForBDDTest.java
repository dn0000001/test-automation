package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * This is a test to show how to do GUI - BDD without feature files
 */
public class NoFeatureFilesForBDDTest extends TestNGBase {
    @Features("Framework")
    @Stories("GUI - BDD without feature files")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"url", "data-set"})
    @Test
    public void performTest(
            @Optional("https://the-internet.herokuapp.com/tables") String url,
            @Optional("data/ui/NoFeatureFilesForBDDScenario1_TestData.xml") String dataSet
    ) {
        gotoURL(url);

        // Load data which contains the BDD information for the test
        new TNHC_DO(getContext()).fromResource(dataSet);
    }

    @Step("Go to URL:  {0}")
    private void gotoURL(String url) {
        getContext().getDriver().get(url);
    }

}
