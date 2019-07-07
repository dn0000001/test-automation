package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.openqa.selenium.By;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.List;

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
        Helper.assertThat(getContext().getDriver(), getAddElementCriteria());
        clickAddElement();
        Helper.assertThat(getContext().getDriver(), getDeleteButtonCriteria());
    }

    private List<Criteria> getAddElementCriteria() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forLambdaExpression(this::isAddElement));
        return criteria;
    }

    private List<Criteria> getDeleteButtonCriteria() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forLambdaExpression(this::isDeleteButton));
        return criteria;
    }

    private boolean isAddElement() {
        return getContext().getDriver().findElement(By.cssSelector(".example > button")).isDisplayed();
    }

    private boolean isDeleteButton() {
        return getContext().getDriver().findElement(By.cssSelector(".added-manually")).isDisplayed();
    }

    private void clickAddElement() {
        getContext().getDriver().findElement(By.cssSelector(".example > button")).click();
    }

}
