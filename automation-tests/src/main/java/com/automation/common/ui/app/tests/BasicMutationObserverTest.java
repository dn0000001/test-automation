package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.Navigation;
import com.automation.common.ui.app.pageObjects.SeleniumEasyCheckBoxDemoPage;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.DomainObjectUtils;
import com.taf.automation.ui.support.util.Utils;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class BasicMutationObserverTest extends TestNGBase {
    @Features("JsUtils")
    @Stories("Validate basic usage of MutationObserver")
    @Severity(SeverityLevel.TRIVIAL)
    @Test
    public void testMutationObserver(ITestContext injectedContext) {
        DomainObjectUtils.overwriteTestName(injectedContext);
        new Navigation(getContext()).toSeleniumEasyBasicCheckbox(Utils.isCleanCookiesSupported());
        SeleniumEasyCheckBoxDemoPage seleniumEasyCheckBoxDemoPage = new SeleniumEasyCheckBoxDemoPage(getContext());
        seleniumEasyCheckBoxDemoPage.attachMutationObserverToOption1();
        seleniumEasyCheckBoxDemoPage.waitForDisableEnableOption1();
    }

}
