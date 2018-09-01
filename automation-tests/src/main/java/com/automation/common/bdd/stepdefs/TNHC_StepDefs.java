package com.automation.common.bdd.stepdefs;

import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.automation.common.ui.app.pageObjects.TNHC_LandingPage;
import com.taf.automation.bdd.TestNGCucumberBase;
import com.taf.automation.ui.support.TestProperties;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class to hold the True North Hockey Canada Step Definitions specifically<BR>
 * <B>Notes: </B>
 * <OL>
 * <LI>The template methods were generated by using IntelliJ from tnhc.feature</LI>
 * <LI>The Given lines of code must be unique across all step def files or an error will occur (no features.)</LI>
 * </OL>
 */
public class TNHC_StepDefs extends TestNGCucumberBase {
    private TNHC_DO hnhc;
    private TNHC_LandingPage landing;

    @Given("^Using the dataSet from the \"([^\"]*)\" file for the True North Hockey Canada Test$")
    public void using_the_dataSet_from_the_file_for_the_True_North_Hockey_Canada_Test(String dataSet) {
        hnhc = new TNHC_DO(getContext()).fromResource(dataSet);
    }

    @Given("^I am on the True North Hockey Canada website$")
    public void i_am_on_the_True_North_Hockey_Canada_website() {
        getContext().getDriver().get(TestProperties.getInstance().getURL());
    }

    @When("^I enter the search fields$")
    public void i_enter_the_search_fields() {
        // This is not necessary but I am too lazy to split out this action from the perform search method
        // and I just want to show filling all the methods
        landing = hnhc.getLanding();
    }

    @Then("^I can perform a search$")
    public void i_can_perform_a_search() {
        landing.performSearch();
    }

}
