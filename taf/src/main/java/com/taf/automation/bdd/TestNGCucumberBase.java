package com.taf.automation.bdd;

import com.taf.automation.ui.support.testng.TestNGBaseWithoutListeners;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Runs cucumber every detected feature as separated test<BR>
 * <B>Notes: </B>
 * <OL>
 * <LI>This is based on the class AbstractTestNGCucumberTests</LI>
 * <LI>We are using the class TestNGBaseWithoutListeners to prevent duplication in the report.
 * (If the class TestNGBase is extended, then the report will duplicate everything.</LI>
 * </OL>
 */
public class TestNGCucumberBase extends TestNGBaseWithoutListeners {
    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
    public void feature(CucumberFeatureWrapper cucumberFeature) {
        testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
    }

    /**
     * @return returns two dimensional array of {@link CucumberFeatureWrapper} objects.
     */
    @DataProvider
    public Object[][] features() {
        return testNGCucumberRunner.provideFeatures();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }

}
