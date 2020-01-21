package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.AddRemoveElementsPage;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Testing Conditional - LambdaExpressionMatch
 */
public class LambdaExpressionMatcherTest extends TestNGBase {
    @Features("Framework")
    @Stories("Conditional - LambdaExpressionMatch")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("url")
    @Test
    public void performTest(@Optional("https://the-internet.herokuapp.com/add_remove_elements/") String url) {
        getContext().getDriver().get(url);
        AddRemoveElementsPage addRemoveElementsPage = new AddRemoveElementsPage();
        addRemoveElementsPage.initPage(getContext());
        addRemoveElementsPage.waitForAddElementUsingLambdaExpression();
        addRemoveElementsPage.clickAddElement();
        addRemoveElementsPage.waitForDeleteButtonUsingLambdaExpression();
        addRemoveElementsPage.clickDeleteButton();
    }

}
