package com.automation.common.bdd.tests;

import com.taf.automation.bdd.TestNGCucumberBase;
import cucumber.api.CucumberOptions;

/**
 * Sample SOAP test using BDD. Website: http://www.holidaywebservice.com
 */
@CucumberOptions(
        // The full path is necessary for running using RunTests.  (If using IntelliJ, then relative path is necessary.)
        features = {"automation-tests/src/main/resources/data/bdd/holiday.feature"},

        // Package(s) that contains the step definitions
        glue = {"com.automation.common.bdd.stepdefs"},

        // The custom allure cucumber reporter
        plugin = {"com.taf.automation.bdd.AllureCucumberReporter"}
)
public class SoapHolidayBddTest extends TestNGCucumberBase {
}
