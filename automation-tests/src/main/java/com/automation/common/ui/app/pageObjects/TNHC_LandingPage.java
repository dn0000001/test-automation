package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.Search;
import com.taf.automation.ui.support.AliasedString;
import com.taf.automation.ui.support.JsUtils;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * True North Hockey Canada Landing Page
 */
public class TNHC_LandingPage extends PageObjectV2 {
    private Map<String, String> substitutions;

    @XStreamOmitField
    @FindBy(id = "test")
    WebElement nonExistant;

    // Default is to user field name as id or name
    WebComponent player;

    @FindBy(id = "team")
    WebComponent team;

    @XStreamAlias("search-fields")
    @FindBy(xpath = "//form")
    Search searchFields;

    /**
     * Dynamic locator test field
     */
    @FindBy(id = "${key}")
    WebComponent division;

    /**
     * Dynamic locator test field #2
     */
    @FindBy(id = "${ALTERNATE}")
    WebComponent alternate;

    /**
     * If field has an alias it will not be replaced unless when loading from resources you use the resolve aliases flag
     */
    private String someField;

    /**
     * If the field needs to support aliases, then it is recommended to use AliasedString.
     */
    private AliasedString someField2;

    public TNHC_LandingPage() {

    }

    public TNHC_LandingPage(TestContext context) {
        super(context);
    }

    /**
     * Initialize page for dynamic locators
     *
     * @param context
     */
    public void initPage(TestContext context) {
        initPage(context, getSubstitutions());
    }

    private Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    public void initDefaultSubstitutions() {
        addSubstitution("key", "division");
    }

    public void addSubstitution(String key, String value) {
        substitutions = getSubstitutions();
        substitutions.put(key, value);
    }

    public WebElement getNonExistant() {
        return nonExistant;
    }

    public WebComponent getPlayer() {
        return player;
    }

    public void setPlayer() {
        player.setValue();
        player.validateData();
    }

    public void setPlayerJS() {
        String sJS = "document.getElementById('player').value = '" + player.getData() + "';";
        JsUtils.execute(getDriver(), sJS);
        player.validateData();
    }

    public void setTeam() {
        team.setValue();
        team.validateData();
    }

    public void setDivision() {
        division.setValue();
        division.validateData();
    }

    public void performSearch() {
        searchFields.setValue();
        searchFields.clickSearch();
    }

    public String getSomeField() {
        return someField;
    }

    public String getSomeField2() {
        return someField2.getData();
    }

    public void setAlternate() {
        alternate.setValue();
        alternate.validateData();
    }

}
