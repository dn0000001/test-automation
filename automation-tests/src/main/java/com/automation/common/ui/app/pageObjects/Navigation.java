package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.TestProperties;
import ru.yandex.qatools.allure.annotations.Step;

public class Navigation {
    private TestContext context;
    private TestProperties props = TestProperties.getInstance();
    private String homeUrl = props.getURL();

    public Navigation(TestContext context) {
        this.context = context;
    }

    @Step("Go to Home Page")
    public void toHome(boolean cleanCookies) {
        if (cleanCookies) {
            context.getDriver().manage().deleteAllCookies();
        }

        context.getDriver().get(homeUrl);
    }

    @Step("Go to Relative Path {0}")
    public void toURL(String path) {
        context.getDriver().get(homeUrl + path);
    }

}
