package com.taf.automation.bdd;

import com.taf.automation.ui.support.testng.TestNGBase;
import cucumber.runtime.StepDefinitionMatch;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import ru.yandex.qatools.allure.cucumberjvm.AllureReporter;

/**
 * Allure reporting plugin for cucumber-jvm
 */
public class AllureCucumberReporter extends AllureReporter {
    private static final String FAILED = "failed";
    private StepDefinitionMatch match;

    @Override
    public void result(Result result) {
        if (match != null) {
            if (FAILED.equals(result.getStatus())) {
                TestNGBase.takeScreenshot("Failed Test Screenshot");
                TestNGBase.takeHTML("Failed Test HTML Source");
            }

            super.result(result);
        }
    }

    @Override
    public void match(Match match) {
        if (match instanceof StepDefinitionMatch) {
            this.match = (StepDefinitionMatch) match;
            super.match(match);
        }
    }

}
