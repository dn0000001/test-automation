package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.GenericRow;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.WebComponent;
import ui.auto.core.data.DataTypes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Example of using dynamic locators to work with a row in a table.<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 * Table from Example 2
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class HerokuappRow extends GenericRow {
    @FindBy(css = "[id='${row}'] .last-name")
    private WebComponent lastName;

    @FindBy(css = "[id='${row}'] .first-name")
    private WebComponent firstName;

    @FindBy(css = "[id='${row}'] .email")
    private WebComponent email;

    @FindBy(css = "[id='${row}'] .dues")
    private WebComponent dues;

    @FindBy(css = "[id='${row}'] .web-site")
    private WebComponent website;

    public HerokuappRow() {
        super();
    }

    public HerokuappRow(TestContext context) {
        super(context);
    }

    public String getLastName() {
        return lastName.getText();
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getEmail() {
        return email.getText();
    }

    public String getDues() {
        return dues.getText();
    }

    public String getWebsite() {
        return website.getText();
    }

    @Step("Validate Locators Not Null")
    public void validateLocatorsNotNull() {
        // Due to re-factoring the locators may be changed, this is to ensure that they are not null
        assertThat("Last Name Locator", lastName.getLocator(), notNullValue());
        assertThat("First Name Locator", firstName.getLocator(), notNullValue());
        assertThat("Email Locator", email.getLocator(), notNullValue());
        assertThat("Dues Locator", dues.getLocator(), notNullValue());
        assertThat("Website Locator", website.getLocator(), notNullValue());
    }

    public boolean isMatch(HerokuappRow rowToMatch) {
        return isMatch(this::getLastName, rowToMatch.lastName.getData(DataTypes.Data, true)) &&
                isMatch(this::getFirstName, rowToMatch.firstName.getData(DataTypes.Data, true)) &&
                isMatch(this::getEmail, rowToMatch.email.getData(DataTypes.Data, true)) &&
                isMatch(this::getDues, rowToMatch.dues.getData(DataTypes.Data, true)) &&
                isMatch(this::getWebsite, rowToMatch.website.getData(DataTypes.Data, true));
    }

}
