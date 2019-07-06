package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.ExpectedConditionsUtil;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
 * Testing Conditional - ExpectedConditionsMatch
 */
public class ExpectedConditionsMatcherTest extends TestNGBase {
    @Features("Framework")
    @Stories("Conditional - ExpectedConditionsMatch")
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
        criteria.add(CriteriaMaker.forExpectedConditions(isAddElement()));
        return criteria;
    }

    private List<Criteria> getDeleteButtonCriteria() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forExpectedConditions(isDeleteButton()));
        return criteria;
    }

    private ExpectedCondition<WebElement> isAddElement() {
        return ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".example > button"));
    }

    private ExpectedCondition<WebElement> isDeleteButton() {
        return ExpectedConditionsUtil.ready(By.cssSelector(".added-manually"));
    }

    private void clickAddElement() {
        getContext().getDriver().findElement(By.cssSelector(".example > button")).click();
    }

}
