package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.WebDriverTypeEnum;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

@SuppressWarnings("java:S3252")
public class ChangeBrowserTest extends TestNGBase {
    private static final String SITE_1 = "https://www.google.ca";
    private static final String SITE_1_VALIDATION = "google";
    private static final String SITE_2 = "https://the-internet.herokuapp.com/broken_images";
    private static final String SITE_2_VALIDATION = "the-internet.herokuapp.com";
    private static final WebDriverTypeEnum CHANGE_BROWSER = TestProperties.getInstance().getBrowserType();

    /**
     * The CHANGE_BROWSER should always be the same.  However, for testing purposes to see a failure change
     * the EXPECTED_BROWSER to something different.
     */
    private static final WebDriverTypeEnum EXPECTED_BROWSER = TestProperties.getInstance().getBrowserType();

    @Features("Framework")
    @Stories("Verify changing of browsers")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest() {
        getContext().getDriver().get(SITE_1);
        AssertJUtil.assertThat(getContext().getDriver().getCurrentUrl()).as("Initial Context URL").contains(SITE_1_VALIDATION);

        Utils.restoreBrowser();
        AssertJUtil.assertThat(getContext().getDriver().getCurrentUrl()).as("Restore Browser No Effect").contains(SITE_1_VALIDATION);
        AssertJUtil.assertThat(Utils.getStoredTestProperties().getBrowserType()).as("Test Properties No Effect").isEqualTo(TestProperties.getInstance().getBrowserType());

        Utils.changeBrowser(CHANGE_BROWSER);
        getContext().getDriver().get(SITE_2);
        AssertJUtil.assertThat(getContext().getDriver().getCurrentUrl()).as("Changed Context URL").contains(SITE_2_VALIDATION);
        AssertJUtil.assertThat(Utils.getStoredTestProperties().getBrowserType()).as("Changed Test Properties").isEqualTo(EXPECTED_BROWSER);

        // Random actions to prevent 'Failed to shutdown Driver Command Executor' that seems to occur
        // if you shutdown the browser too fast after launching.
        new HerokuappDataTableEqualsTest().verifyTable1EqualsTable2Test();

        Utils.restoreBrowser();
        AssertJUtil.assertThat(getContext().getDriver().getCurrentUrl()).as("Restored Context URL Diff").doesNotContain(SITE_2_VALIDATION);
        AssertJUtil.assertThat(getContext().getDriver().getCurrentUrl()).as("Restored Context URL Same").contains(SITE_1_VALIDATION);
        AssertJUtil.assertThat(Utils.getStoredTestProperties().getBrowserType()).as("Restored Test Properties").isEqualTo(TestProperties.getInstance().getBrowserType());
    }

}
