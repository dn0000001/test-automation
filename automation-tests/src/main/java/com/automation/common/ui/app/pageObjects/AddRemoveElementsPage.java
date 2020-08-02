package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.util.ExpectedConditionsUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.WebComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Add/Remove Elements Page at
 * <a href="https://the-internet.herokuapp.com/add_remove_elements/">https://the-internet.herokuapp.com/add_remove_elements/</a>
 */
public class AddRemoveElementsPage extends PageObjectV2 {
    @XStreamOmitField
    @FindBy(css = ".example > button")
    private WebComponent addElementButton;

    @FindBy(css = ".added-manually")
    private WebComponent deleteButton;

    public AddRemoveElementsPage() {
        super();
    }

    public AddRemoveElementsPage(TestContext context) {
        super(context);
    }

    @Step("Click Add Element")
    public void clickAddElement() {
        click(addElementButton);
    }

    @Step("Click Delete Button")
    public void clickDeleteButton() {
        WebElement element = click(deleteButton);
        Utils.getWebDriverWait().until(ExpectedConditions.invisibilityOf(element));
    }

    @Step("Wait For Add Element Using Lambda Expression")
    public void waitForAddElementUsingLambdaExpression() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forLambdaExpression(this::isAddElementForLambdaExpression));
        Helper.assertThat(getContext().getDriver(), criteria);
    }

    private boolean isAddElementForLambdaExpression() {
        return getDriver().findElement(addElementButton.getLocator()).isDisplayed();
    }

    @Step("Wait For Delete Button Using Lambda Expression")
    public void waitForDeleteButtonUsingLambdaExpression() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forLambdaExpression(this::isDeleteButtonForLambdaExpression));
        Helper.assertThat(getContext().getDriver(), criteria);
    }

    private boolean isDeleteButtonForLambdaExpression() {
        return getDriver().findElement(deleteButton.getLocator()).isDisplayed();
    }

    @Step("Wait For Add Element Using Expected Condition")
    public void waitForAddElementUsingExpectedCondition() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forExpectedConditions(isAddElementForExpectedCondition()));
        Helper.assertThat(getContext().getDriver(), criteria);
    }

    private ExpectedCondition<WebElement> isAddElementForExpectedCondition() {
        return ExpectedConditions.visibilityOfElementLocated(addElementButton.getLocator());
    }

    @Step("Wait For Delete Button Using Expected Condition")
    public void waitForDeleteButtonUsingExpectedCondition() {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forExpectedConditions(isDeleteButtonForExpectedCondition()));
        Helper.assertThat(getContext().getDriver(), criteria);
    }

    private ExpectedCondition<WebElement> isDeleteButtonForExpectedCondition() {
        return ExpectedConditionsUtil.ready(deleteButton.getLocator());
    }

}
