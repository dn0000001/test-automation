package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.RowMatchingDO;
import com.automation.common.ui.app.pageObjects.HerokuappDataTablesPage;
import com.automation.common.ui.app.pageObjects.HerokuappRow;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

@SuppressWarnings("java:S3252")
public class RowMatchingTest extends TestNGBase {
    private RowMatchingDO rowMatchingDO;
    private HerokuappDataTablesPage tablesPage;

    @Features("Framework")
    @Stories("Matching rows in tables")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"data-set"})
    @Test
    public void performRowMatching(@Optional("data/ui/RowMatching_TestData.xml") String dataSet) {
        new Navigation(getContext()).toHerokuappTables(Utils.isCleanCookiesSupported());
        rowMatchingDO = new RowMatchingDO(getContext()).fromResource(dataSet);
        tablesPage = new HerokuappDataTablesPage(getContext());

        performAnyRowMatchTest();
        performSingleRowMatchTest();
        performMultiRowMatchTest();
        performNoRowMatchTest();
    }

    @Step("Find Row:  {0}")
    private void findRowDetails(HerokuappRow find) {
        //
    }

    @Step("Matching Row:  {0}, {1}, {2}, {3}, {4}")
    private void matchDetails(String lastName, String firstName, String email, String dues, String website) {
        //
    }

    private void performGenericMatchTest(HerokuappRow find) {
        findRowDetails(find);
        HerokuappRow match = tablesPage.findTableRow(find);
        matchDetails(match.getLastName(), match.getFirstName(), match.getEmail(), match.getDues(), match.getWebsite());
    }

    @Step("Perform Any Row Match Test")
    private void performAnyRowMatchTest() {
        performGenericMatchTest(rowMatchingDO.getAnyRowMatch());
    }

    @Step("Perform Single Row Match Test")
    private void performSingleRowMatchTest() {
        performGenericMatchTest(rowMatchingDO.getSingleRowMatch());
    }

    @Step("Perform Multi Row Match Test")
    private void performMultiRowMatchTest() {
        performGenericMatchTest(rowMatchingDO.getMultiRowMatch());
    }

    @Step("Perform No Row Match Test")
    private void performNoRowMatchTest() {
        findRowDetails(rowMatchingDO.getNoRowMatch());
        HerokuappRow match = tablesPage.findTableRow(rowMatchingDO.getNoRowMatch(), false);
        if (match != null) {
            matchDetails(match.getLastName(), match.getFirstName(), match.getEmail(), match.getDues(), match.getWebsite());
        }

        AssertJUtil.assertThat(match).as("Found unexpected matching row").isNull();
    }

}
