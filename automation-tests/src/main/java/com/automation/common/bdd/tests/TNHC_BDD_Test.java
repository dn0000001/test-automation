package com.automation.common.bdd.tests;

import com.taf.automation.bdd.TestNGCucumberBase;
import cucumber.api.CucumberOptions;

/**
 * True North Hockey Canada BDD Test
 */
@CucumberOptions(
        // The full path is necessary for running using RunTests.  (If using IntelliJ, then relative path is necessary.)
        features = {"automation-tests/src/main/resources/data/bdd/tnhc.feature"},

        // Package(s) that contains the step definitions
        glue = {"com.automation.common.bdd.stepdefs"},

        // The custom allure cucumber reporter
        plugin = {"com.taf.automation.bdd.AllureCucumberReporter"}
)
public class TNHC_BDD_Test extends TestNGCucumberBase {
}
