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

    @Step("Go to {0} Page")
    private void toURL(String page, String url, boolean cleanCookies) {
        if (cleanCookies) {
            context.getDriver().manage().deleteAllCookies();
        }

        assertThat("URL", url, not(isEmptyOrNullString()));
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
