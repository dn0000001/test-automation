package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.conditional.Conditional;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.conditional.CriteriaType;
import com.taf.automation.ui.support.conditional.ResultInfo;
import com.taf.automation.ui.support.conditional.ResultType;
import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit testing Conditional class
 */
public class ConditionalTest extends TestNGBase {
    private static final Logger LOG = LoggerFactory.getLogger(ConditionalTest.class);
    private static final By ELEMENT = By.id("headersearch");

    @Features("Conditional")
    @Stories("Single Criteria")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    public void singleCriteriaTest() {
        new Navigation(getContext()).toTrueNorthHockey(Utils.isCleanCookiesSupported());

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forElementReady(ELEMENT));

        LOG.info("Start Single:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        int index = conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Single:  {}", new Date());

        assertThat(index, equalTo(0));
        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.READY));
        assertThat(resultInfo.getAdditionalInfo().get(ResultType.LOCATOR), equalTo(ELEMENT));
    }

    @Features("Conditional")
    @Stories("Multiple Criteria")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void multipleCriteriaTest() {
        new Navigation(getContext()).toTrueNorthHockey(Utils.isCleanCookiesSupported());

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forElementReady(ELEMENT));
        criteria.add(CriteriaMaker.forAlertAccept());
        criteria.add(CriteriaMaker.forPopup(getContext().getDriver()));

        LOG.info("Start Multi:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        int index = conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Multi:  {}", new Date());

        assertThat(index, equalTo(0));
        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.READY));
        assertThat(resultInfo.getAdditionalInfo().get(ResultType.LOCATOR), equalTo(ELEMENT));
    }

    @Test
    public void criteriaMakerConfigUrlsTest() {
        new Navigation(getContext()).toTrueNorthHockey(Utils.isCleanCookiesSupported());
        String value = "aaaa";

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forUrlContains(value));
        criteria.add(CriteriaMaker.forUrlDoesNotContain(".com"));
        criteria.add(CriteriaMaker.forUrlEquals(value));
        criteria.add(CriteriaMaker.forUrlEqualsIgnoreCase(value));
        criteria.add(CriteriaMaker.forUrlRegEx(value));
        criteria.add(CriteriaMaker.forUrlNotEqual(value));

        LOG.info("Start URLS:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop URLS:  {}", new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.URL_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigDropDownTest() {
        new Navigation(getContext()).toRoboFormFill(Utils.isCleanCookiesSupported());
        By locator = By.cssSelector("[name$=__type]"); // Needs to be valid locator to some drop down on the page
        String value = "aaaa"; // Any value that does not match the currently selected drop down option found by the locator

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forDropDownContains(locator, value));
        criteria.add(CriteriaMaker.forDropDownDoesNotContain(locator, "Card")); // Based on currently selected drop down option
        criteria.add(CriteriaMaker.forDropDownEquals(locator, value));
        criteria.add(CriteriaMaker.forDropDownEqualsIgnoreCase(locator, value));
        criteria.add(CriteriaMaker.forDropDownRegEx(locator, value));
        criteria.add(CriteriaMaker.forDropDownNotEqual(locator, value)); // Trigger this criteria

        LOG.info("Start DropDown:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop DropDown:  {}", new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.DROPDOWN_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigAttributeTest() {
        new Navigation(getContext()).toRubyWatirCheckboxes(Utils.isCleanCookiesSupported());
        By locator = By.xpath("//input[@value='golf']"); // Needs to be valid locator to some input on the page
        String value = "aaaa"; // Any input value that does not match the element found by the locator

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forInputContains(locator, value));
        criteria.add(CriteriaMaker.forInputDoesNotContain(locator, ""));
        criteria.add(CriteriaMaker.forInputEquals(locator, value));
        criteria.add(CriteriaMaker.forInputEqualsIgnoreCase(locator, value));
        criteria.add(CriteriaMaker.forInputRegEx(locator, value));
        criteria.add(CriteriaMaker.forInputNotEqual(locator, value)); // Trigger this criteria

        LOG.info("Start Attribute:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Attribute:  {}", new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.ATTRIBUTE_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigTextTest() {
        new Navigation(getContext()).toRubyWatirCheckboxes(Utils.isCleanCookiesSupported());
        By locator = By.xpath("//div[@id='content']//h1"); // Needs to be valid locator to some text on the page
        String value = "aaaa"; // Any text that does not match the element found by the locator

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forTextContains(locator, value));
        criteria.add(CriteriaMaker.forTextDoesNotContain(locator, "Multiple")); // Any text from the element found by the locator
        criteria.add(CriteriaMaker.forTextEquals(locator, value));
        criteria.add(CriteriaMaker.forTextEqualsIgnoreCase(locator, value));
        criteria.add(CriteriaMaker.forTextRegEx(locator, value));
        criteria.add(CriteriaMaker.forTextNotEqual(locator, value)); // Trigger this criteria

        LOG.info("Start Text:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Text:  {}", new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.TEXT_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigElementsTest() {
        new Navigation(getContext()).toTrueNorthHockey(Utils.isCleanCookiesSupported());
        By locator = By.id("unknown");
        WebElement nullElement = null;

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forElementDisplayed(locator));
        criteria.add(CriteriaMaker.forElementRemoved(ELEMENT));
        criteria.add(CriteriaMaker.forElementEnabled(locator));
        criteria.add(CriteriaMaker.forElementDisabled(locator));
        criteria.add(CriteriaMaker.forElementExists(locator));
        criteria.add(CriteriaMaker.forElementSelected(locator));
        criteria.add(CriteriaMaker.forElementUnselected(locator));
        criteria.add(CriteriaMaker.forElementStale(nullElement));
        criteria.add(CriteriaMaker.forElementReady(ELEMENT));

        LOG.info("Start Elements:  {}", new Date());
        Conditional conditional = new Conditional(getContext().getDriver());
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Elements:  {}", new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getAdditionalInfo().get(ResultType.LOCATOR), equalTo(ELEMENT));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.READY));
    }

}
