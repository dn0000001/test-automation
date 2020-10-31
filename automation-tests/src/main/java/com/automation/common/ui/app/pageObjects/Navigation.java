package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.TestProperties;
import ru.yandex.qatools.allure.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public class Navigation {
    private TestContext context;
    private TestProperties props = TestProperties.getInstance();
    private String homeUrl = props.getURL();

    public Navigation(TestContext context) {
        this.context = context;
    }

    public void toURL(String page, String url, boolean cleanCookies) {
        toURL(page, url, cleanCookies, false);
    }

    /**
     * Go to specified URL<BR>
     * <B>Notes: </B> Selenium only deletes cookies from the current domain.  This could be an issue if you are
     * testing against multiple sites at the same time in the test and you want to have the cookies deleted.  Normally,
     * you want all cookies deleted for all sites but probably most important is for the site you are navigating to.
     * The different site flag combined with the clean cookies flag will ensure the site you are navigating to has the
     * cookies deleted.  This should ensure that login page is displayed as the cookies for the site were deleted
     * prior to landing there.
     *
     * @param page          - Page name for logging purposes
     * @param url           - URL to navigate to
     * @param cleanCookies  - true to clean cookies
     * @param differentSite - true if coming from a different site
     */
    @Step("Go to {0} Page")
    public void toURL(String page, String url, boolean cleanCookies, boolean differentSite) {
        assertThat("URL", url, not(isEmptyOrNullString()));

        if (differentSite && cleanCookies) {
            // Go to URL first to ensure we delete cookies for that domain
            context.getDriver().get(url);
        }

        if (cleanCookies) {
            // This deletes the cookies for the current domain
            context.getDriver().manage().deleteAllCookies();
        }

        context.getDriver().get(url);
    }

    public void toHome(boolean cleanCookies) {
        toURL("Home", homeUrl, cleanCookies);
    }

    @Step("Go to Relative Path {0}")
    public void toURL(String path) {
        context.getDriver().get(homeUrl + path);
    }

    public void toHerokuappTables(boolean cleanCookies) {
        toURL("Herokuapp - Tables", props.getCustom("herokuapp-tables-url", null), cleanCookies);
    }

    public void toHerokuappElements(boolean cleanCookies) {
        toURL("Herokuapp - Elements", props.getCustom("herokuapp-elements-url", null), cleanCookies);
    }

    public void toDuckDuckGo(boolean cleanCookies) {
        toURL("Duck Duck Go", props.getCustom("duckduckgo-url", null), cleanCookies);
    }

    public void toPrimefacesDashboard(boolean cleanCookies) {
        toURL("Primefaces Dashboard", props.getCustom("primefaces-dashboard-url", null), cleanCookies);
    }

    public void toRoboFormFill(boolean cleanCookies) {
        toURL("RoboForm - Fill", props.getCustom("roboform-fill-url", null), cleanCookies);
    }

    public void toRoboFormLogin(boolean cleanCookies) {
        toURL("RoboForm - Login", props.getCustom("roboform-login-url", null), cleanCookies);
    }

    public void toTrueNorthHockey(boolean cleanCookies) {
        toURL("True North Hockey", props.getCustom("true-north-hockey-url", null), cleanCookies);
    }

    public void toRubyWatirCheckboxes(boolean cleanCookies) {
        toURL("Ruby Watir - Checkboxes", props.getCustom("rubywatir-checkboxes-url", null), cleanCookies);
    }

    public void toSeleniumEasyRadioButton(boolean cleanCookies) {
        toURL("Selenium Easy - Radio Button", props.getCustom("seleniumeasy-radio-button-url", null), cleanCookies);
    }

    public void toSeleniumEasyBasicCheckbox(boolean cleanCookies) {
        toURL("Selenium Easy - Basic Checkbox", props.getCustom("seleniumeasy-basic-checkbox-url", null), cleanCookies);
    }

}
