package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestNgRunnerTest extends TestNGBase {
    private TestScenario scenarioUnderTest;

    private enum TestScenario implements ColumnMapper {
        NO_FAILURE("NO_FAILURE", "https://yandex.com/"),
        STANDARD_FAILURE("STANDARD_FAILURE", "https://www.google.ca/"),
        BEFORE_CLASS_FAILURE("BEFORE_CLASS_FAILURE", "https://www.baidu.com/"),
        BEFORE_TEST_FAILURE("BEFORE_TEST_FAILURE", "https://www.bing.com/"),
        AFTER_TEST_FAILURE("AFTER_TEST_FAILURE", "https://ca.search.yahoo.com/"),
        AFTER_CLASS_FAILURE("AFTER_CLASS_FAILURE", "https://duckduckgo.com/"),
        ;
        private final String columnName;
        private final String url;

        TestScenario(String columnName, String url) {
            this.columnName = columnName;
            this.url = url;
        }

        @Override
        public String getColumnName() {
            return columnName;
        }

        @Override
        public ColumnMapper[] getValues() {
            return values();
        }

        public String getUrl() {
            return url;
        }

    }

    private TestScenario getScenarioUnderTest(String scenario) {
        if (scenarioUnderTest == null) {
            scenarioUnderTest = (TestScenario) TestScenario.NO_FAILURE.toEnum(scenario);
        }

        return scenarioUnderTest;
    }

    @Features("Framework")
    @Stories("Validate various scenarios of the test runner (TestNG)")
    @Severity(SeverityLevel.TRIVIAL)
    @Parameters({"scenario"})
    @BeforeClass
    private void runBeforeClass(String scenario) {
        Helper.log("Running BeforeClass", true);

        if (TestScenario.BEFORE_CLASS_FAILURE == getScenarioUnderTest(scenario)) {
            getContext().getDriver().get(TestScenario.BEFORE_CLASS_FAILURE.getUrl());
            assertThat("Failure in before class attaches screenshot & html", false);
        }

        Helper.log("Completed BeforeClass", true);
    }

    @Features("Framework")
    @Stories("Validate various scenarios of the test runner (TestNG)")
    @Severity(SeverityLevel.BLOCKER)
    @Parameters({"scenario"})
    @BeforeTest
    private void runBeforeTest(String scenario) {
        Helper.log("Running BeforeTest", true);

        if (TestScenario.BEFORE_TEST_FAILURE == getScenarioUnderTest(scenario)) {
            getContext().getDriver().get(TestScenario.BEFORE_TEST_FAILURE.getUrl());
            assertThat("Failure in before test attaches screenshot & html", false);
        }

        Helper.log("Completed BeforeTest", true);
    }

    @Features("Framework")
    @Stories("Validate various scenarios of the test runner (TestNG)")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"scenario"})
    @Test
    public void performTest(String scenario) {
        getContext().getDriver().get(TestScenario.NO_FAILURE.getUrl());
        if (TestScenario.STANDARD_FAILURE == getScenarioUnderTest(scenario)) {
            getContext().getDriver().get(TestScenario.STANDARD_FAILURE.getUrl());
            assertThat("Regular Failure in test attaches screenshot & html", false);
        }
    }

    @Features("Framework")
    @Stories("Validate various scenarios of the test runner (TestNG)")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({"scenario"})
    @AfterTest
    private void runAfterTest(String scenario) {
        Helper.log("Running AfterTest", true);

        if (TestScenario.AFTER_TEST_FAILURE == getScenarioUnderTest(scenario)) {
            getContext().getDriver().get(TestScenario.AFTER_TEST_FAILURE.getUrl());
            assertThat("Failure in after test attaches screenshot & html", false);
        }

        Helper.log("Completed AfterTest", true);
    }

    @Features("Framework")
    @Stories("Validate various scenarios of the test runner (TestNG)")
    @Severity(SeverityLevel.MINOR)
    @Parameters({"scenario"})
    @AfterClass
    private void runAfterClass(String scenario) {
        Helper.log("Running AfterClass", true);

        if (TestScenario.AFTER_CLASS_FAILURE == getScenarioUnderTest(scenario)) {
            getContext().getDriver().get(TestScenario.AFTER_CLASS_FAILURE.getUrl());
            assertThat("Failure in after class attaches screenshot & html", false);
        }

        Helper.log("Completed AfterClass", true);
    }

}
