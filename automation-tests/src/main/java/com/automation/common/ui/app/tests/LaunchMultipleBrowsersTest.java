package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.WebDriverTypeEnum;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class LaunchMultipleBrowsersTest extends TestNGBase {
    private static final String GOOGLE = "google";
    private List<WebDriverTypeEnum> browsersToBeTested;

    @BeforeTest
    public void browsersToBeTestedSetup() {
        browsersToBeTested = new ArrayList<>();
        browsersToBeTested.add(WebDriverTypeEnum.FIREFOX);
        browsersToBeTested.add(WebDriverTypeEnum.CHROME);

        // Repeat some to see if they can still be launched
        browsersToBeTested.add(WebDriverTypeEnum.FIREFOX);
        browsersToBeTested.add(WebDriverTypeEnum.CHROME);
    }

    @DataProvider(name = "browsersToBeTestedData", parallel = true)
    public Iterator<Object[]> dataProvider() {
        List<Object[]> browsers = new ArrayList<>();

        int i = 0;
        for (WebDriverTypeEnum browser : browsersToBeTested) {
            browsers.add(new Object[]{i, browser});
            i++;
        }

        return browsers.iterator();
    }

    @Features("Framework")
    @Stories("Verify launching of browsers")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "browsersToBeTestedData")
    public void performTest(int i, WebDriverTypeEnum browser) {
        WebDriver driver = browser.getNewWebDriver();
        driver.get("https://www.google.ca");
        assertThat(browser.getDriverName() + " - URL", driver.getCurrentUrl(), containsString(GOOGLE));
        driver.quit();
    }

}
