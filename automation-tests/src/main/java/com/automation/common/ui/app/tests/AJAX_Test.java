package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.SearchPage;
import com.taf.automation.ui.support.util.Helper;
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
 * Testing the framework and how it works with various AJAX behaviours
 */
public class AJAX_Test extends TestNGBase {
    @Features("Framework")
    @Stories("Examine how lists work when elements become stale")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test(enabled = true)
    public void staleListTest(@Optional("data/ui/TNHC_TestData.xml") String dataSet) {
        getContext().getDriver().get("https://www.google.ca");
        SearchPage searchPage = new SearchPage();
        searchPage.initDefaultSubstitutions();
        searchPage.initPage(getContext());

        // Set breakpoint such that you can search for 'test'
        Utils.sleep(3000);
        Helper.log("Before:  " + searchPage.getContainersSize(), true);

        // Set breakpoint such that you can search for 'tiff' (this should make the elements stale.)
        // Note: There should be a different number of results
        Utils.sleep(3000);

        // Note: Accessing the list size gets the element again
        Helper.log("After:  " + searchPage.getContainersSize(), true);
    }

}
