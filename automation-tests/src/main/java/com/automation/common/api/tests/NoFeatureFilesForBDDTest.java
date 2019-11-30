package com.automation.common.api.tests;

import com.automation.common.api.domainObjects.JsonIpDO;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * This is a test to show how to do API - BDD without feature files
 */
@Listeners(AllureTestNGListener.class)
public class NoFeatureFilesForBDDTest {
    @Features("Framework")
    @Stories("API - BDD without feature files")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test
    public void performTest(@Optional("data/api/NoFeatureFilesForBDDScenario1_TestData.xml") String dataSet) {
        // Load data which contains the BDD information for the test
        new JsonIpDO().fromResource(dataSet);
    }

}
