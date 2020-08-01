package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.WebDriverTypeEnum;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

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
        assertThat("Initial Context URL", getContext().getDriver().getCurrentUrl(), containsString(SITE_1_VALIDATION));

        Utils.restoreBrowser();
        assertThat("Restore Browser No Effect", getContext().getDriver().getCurrentUrl(), containsString(SITE_1_VALIDATION));
        assertThat("Test Properties No Effect", Utils.getStoredTestProperties().getBrowserType(), equalTo(TestProperties.getInstance().getBrowserType()));

        Utils.changeBrowser(CHANGE_BROWSER);
        getContext().getDriver().get(SITE_2);
        assertThat("Changed Context URL", getContext().getDriver().getCurrentUrl(), containsString(SITE_2_VALIDATION));
        assertThat("Changed Test Properties", Utils.getStoredTestProperties().getBrowserType(), equalTo(EXPECTED_BROWSER));

        Utils.restoreBrowser();
        assertThat("Restored Context URL Diff", getContext().getDriver().getCurrentUrl(), not(containsString(SITE_2_VALIDATION)));
        assertThat("Restored Context URL Same", getContext().getDriver().getCurrentUrl(), containsString(SITE_1_VALIDATION));
        assertThat("Restored Test Properties", Utils.getStoredTestProperties().getBrowserType(), equalTo(TestProperties.getInstance().getBrowserType()));
    }

}
