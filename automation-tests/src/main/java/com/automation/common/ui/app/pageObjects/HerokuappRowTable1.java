package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Example of using dynamic locators to work with a row in a table.<BR>
 * Site:  <a href="https://the-internet.herokuapp.com/tables">https://the-internet.herokuapp.com/tables</a><BR>
 * Table from Example 2
 */
public class HerokuappRowTable1 extends PageObjectV2 {
    private Map<String, String> substitutions;

    @XStreamOmitField
    @FindBy(xpath = "//*[@id='${row-id}']/*[position()=${LAST_NAME}]")
    private WebComponent lastName;

    @XStreamOmitField
    @FindBy(xpath = "//*[@id='${row-id}']/*[position()=${FIRST_NAME}]")
    private WebComponent firstName;

    @XStreamOmitField
    @FindBy(xpath = "//*[@id='${row-id}']/*[position()=${EMAIL}]")
    private WebComponent email;

    @XStreamOmitField
    @FindBy(xpath = "//*[@id='${row-id}']/*[position()=${DUE}]")
    private WebComponent dues;

    @XStreamOmitField
    @FindBy(xpath = "//*[@id='${row-id}']/*[position()=${WEB_SITE}]")
    private WebComponent website;

    public HerokuappRowTable1() {
        super();
    }

    private Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    public void updateRowIdKey(String value) {
        getSubstitutions().put("row-id", value);
    }

    public void updateSubstitutions(Map<String, String> additions) {
        getSubstitutions().putAll(additions);
    }

    public void initPage(TestContext context) {
        initPage(context, getSubstitutions());
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
