package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.automation.common.ui.app.pageObjects.TNHC_LandingPage;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Unit testing framework
 */
public class TNHC_Test extends TestNGBase {
    /**
     * Time in milliseconds to pause the test such that you can see multiple tests executed at same time.
     */
    private static final int PAUSE_TEST = 7000;

    @Features("Framework")
    @Stories("General Framework unit testing")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void generalUnitTest(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        TNHC_DO hnhc = new TNHC_DO(getContext()).fromResource(dataSet);
        getContext().getDriver().get(TestProperties.getInstance().getURL());

        TNHC_LandingPage landing = hnhc.getLanding();
        Helper.log("someField:  " + landing.getSomeField(), true);
        Helper.log("someField2:  " + landing.getSomeField2(), true);

        landing.setPlayerJS();
        landing.setTeam();
        Utils.sleep(PAUSE_TEST);
    }

    @Features("Framework")
    @Stories("General Framework unit testing #2")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void generalUnitTest2(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        TNHC_DO hnhc = new TNHC_DO(getContext()).fromResource(dataSet, true);
        getContext().getDriver().get(TestProperties.getInstance().getURL());

        TNHC_LandingPage landing = hnhc.getLanding();
        Helper.log("__someField:  " + landing.getSomeField(), true);
        Helper.log("__someField2:  " + landing.getSomeField2(), true);

        landing.setPlayerJS();
        landing.setTeam();
        Utils.sleep(PAUSE_TEST);
    }

    @Features("Framework")
    @Stories("General Framework unit testing #3")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void generalUnitTest3(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        TNHC_DO hnhc = new TNHC_DO(getContext()).fromResource(dataSet);
        getContext().getDriver().get(TestProperties.getInstance().getURL());

        TNHC_LandingPage landing = hnhc.getLanding();
        landing.performSearch();
        Utils.sleep(PAUSE_TEST);
    }

    @Features("Framework")
    @Stories("Dynamic Locator Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void dynamicLocatorTest(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        TNHC_DO hnhc = new TNHC_DO(getContext()).fromResource(dataSet);
        getContext().getDriver().get(TestProperties.getInstance().getURL());

        TNHC_LandingPage landing = hnhc.getLanding();
        landing.setDivision();
        Utils.sleep(PAUSE_TEST);

        landing.performAccessibilityTest();
        getContext().getAccessibility().verify();
    }

    @Features("Framework")
    @Stories("Switching Dynamic Locator Test")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void dynamicLocatorSwitchingTest(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        TNHC_DO tnhc = new TNHC_DO(getContext()).fromResource(dataSet);
        getContext().getDriver().get(TestProperties.getInstance().getURL());
        TNHC_LandingPage landing = tnhc.getLanding();

        landing.addSubstitution("ALTERNATE", "player");
        landing.initPage(getContext());
        landing.setAlternate();

        landing.addSubstitution("ALTERNATE", "team");
        landing.initPage(getContext());
        landing.setAlternate();
        Utils.sleep(PAUSE_TEST);
    }

}
