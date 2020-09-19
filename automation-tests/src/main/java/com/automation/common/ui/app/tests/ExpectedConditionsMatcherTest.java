package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.AddRemoveElementsPage;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Testing Conditional - ExpectedConditionsMatch
 */
public class ExpectedConditionsMatcherTest extends TestNGBase {
    @Features("Framework")
    @Stories("Conditional - ExpectedConditionsMatch")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest() {
        new Navigation(getContext()).toHerokuappElements(Utils.isCleanCookiesSupported());
        AddRemoveElementsPage addRemoveElementsPage = new AddRemoveElementsPage();
        addRemoveElementsPage.initPage(getContext());
        addRemoveElementsPage.waitForAddElementUsingExpectedCondition();
        addRemoveElementsPage.clickAddElement();
        addRemoveElementsPage.waitForDeleteButtonUsingExpectedCondition();
        addRemoveElementsPage.clickDeleteButton();
    }

}
