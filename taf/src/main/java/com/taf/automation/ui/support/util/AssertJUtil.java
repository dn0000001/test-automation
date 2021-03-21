package com.taf.automation.ui.support.util;

import com.taf.automation.asserts.PageComponentAssert;
import com.taf.automation.asserts.WebDriverAssert;
import com.taf.automation.asserts.WebElementAssert;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ui.auto.core.pagecomponent.PageComponent;

public class AssertJUtil extends Assertions {
    public static WebElementAssert assertThat(WebElement actual) {
        return new WebElementAssert(actual);
    }

    public static WebDriverAssert assertThat(WebDriver driver) {
        return new WebDriverAssert(driver);
    }

    public static PageComponentAssert assertThat(PageComponent actual) {
        return new PageComponentAssert(actual);
    }

}
