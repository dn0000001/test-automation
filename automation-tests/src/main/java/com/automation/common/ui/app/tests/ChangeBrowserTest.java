package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.Utils;
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
    private static final String GOOGLE = "google";
    private static final String TSN = "tsn.ca";

    @Features("Framework")
    @Stories("Verify changing of browsers")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest() {
        getContext().getDriver().get("https://www.google.ca");
        assertThat("Initial Context URL", getContext().getDriver().getCurrentUrl(), containsString(GOOGLE));

        Utils.restoreBrowser();
        assertThat("Restore Browser No Effect", getContext().getDriver().getCurrentUrl(), containsString(GOOGLE));
        assertThat("Test Properties No Effect", Utils.getStoredTestProperties().getBrowserType(), equalTo(TestProperties.getInstance().getBrowserType()));

        Utils.changeBrowser(WebDriverTypeEnum.CHROME);
        getContext().getDriver().get("https://www.tsn.ca");
        assertThat("Changed Context URL", getContext().getDriver().getCurrentUrl(), containsString(TSN));
        assertThat("Changed Test Properties", Utils.getStoredTestProperties().getBrowserType(), equalTo(WebDriverTypeEnum.CHROME));

        Utils.restoreBrowser();
        assertThat("Restored Context URL Diff", getContext().getDriver().getCurrentUrl(), not(containsString(TSN)));
        assertThat("Restored Context URL Same", getContext().getDriver().getCurrentUrl(), containsString(GOOGLE));
        assertThat("Restored Test Properties", Utils.getStoredTestProperties().getBrowserType(), equalTo(TestProperties.getInstance().getBrowserType()));
    }

}
