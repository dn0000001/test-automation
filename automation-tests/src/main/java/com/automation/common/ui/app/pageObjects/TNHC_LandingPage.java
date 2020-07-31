package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.Search;
import com.automation.common.ui.app.domainObjects.CsvColumnMapping;
import com.taf.automation.ui.support.AliasedString;
import com.taf.automation.ui.support.util.JsUtils;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.components.WebComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * True North Hockey Canada Landing Page
 */
public class TNHC_LandingPage extends PageObjectV2 {
    @XStreamOmitField
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
     * @param context - Context
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

    @Step("Set Player")
    public void setPlayer() {
        setElementValueV2(player);
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

    @Step("Set Division")
    public void setDivision() {
        division.setValue();
        validateData(division);
    }

    @Step("Perform Search")
    public void performSearch() {
        setElementValueV2(searchFields);
        searchFields.clickSearch();
    }

    public String getSomeField() {
        return someField;
    }

    public String getSomeField2() {
        return someField2.getData();
    }

    public void setAlternate() {
        setElementValueV2(alternate);
    }

    public void performAccessibilityTest() {
        performAccessibilityTest("True North Hockey Page");
    }

    /**
     * Use the CSV data to set the variables in the domain object
     *
     * @param csvTestData - CSV test data
     */
    public void setData(CsvTestData csvTestData) {
        CSVRecord csv = csvTestData.getRecord();
        CsvUtils.setData(csv, CsvColumnMapping.PLAYER, player);
        CsvUtils.setData(csv, CsvColumnMapping.TEAM, team);
    }

}
