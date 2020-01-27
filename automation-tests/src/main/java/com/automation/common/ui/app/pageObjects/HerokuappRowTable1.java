package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.GenericRow;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;

/**
 * Example of using dynamic locators to work with a row in a table.<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 * Table from Example 2
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class HerokuappRowTable1 extends GenericRow {
    @XStreamOmitField
    @FindBy(xpath = "//*[@auto='${row-id}']/*[position()=${LAST_NAME}]")
    private WebComponent lastName;

    @XStreamOmitField
    @FindBy(xpath = "//*[@auto='${row-id}']/*[position()=${FIRST_NAME}]")
    private WebComponent firstName;

    @XStreamOmitField
    @FindBy(xpath = "//*[@auto='${row-id}']/*[position()=${EMAIL}]")
    private WebComponent email;

    @XStreamOmitField
    @FindBy(xpath = "//*[@auto='${row-id}']/*[position()=${DUE}]")
    private WebComponent dues;

    @XStreamOmitField
    @FindBy(xpath = "//*[@auto='${row-id}']/*[position()=${WEB_SITE}]")
    private WebComponent website;

    public HerokuappRowTable1() {
        super();
    }

    public HerokuappRowTable1(TestContext context) {
        super(context);
    }

    @Override
    public String getRowKey() {
        return "row-id";
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

}
