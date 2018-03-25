package com.taf.automation.ui.app.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.taf.automation.ui.support.conditional.Conditional;
import com.taf.automation.ui.support.conditional.Criteria;
import com.taf.automation.ui.support.conditional.CriteriaMaker;
import com.taf.automation.ui.support.conditional.CriteriaType;
import com.taf.automation.ui.support.conditional.ResultInfo;
import com.taf.automation.ui.support.conditional.ResultType;
import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Unit testing Conditional class
 */
public class ConditionalTest extends TestNGBase {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Features("Conditional")
    @Stories("Single Criteria")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    public void singleCriteriaTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forElementReady(By.name("Insurance")));

        LOG.info("Start Single:  " + new Date());
        Conditional conditional = new Conditional(driver);
        int index = conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Single:  " + new Date());

        assertThat(index, equalTo(0));
        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.READY));
        assertThat(resultInfo.getAdditionalInfo().get(ResultType.LOCATOR), equalTo(By.name("Insurance")));
    }

    @Features("Conditional")
    @Stories("Multiple Criteria")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void multipleCriteriaTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forElementReady(By.name("Insurance")));
        criteria.add(CriteriaMaker.forAlertAccept());
        criteria.add(CriteriaMaker.forPopup(driver));

        LOG.info("Start Multi:  " + new Date());
        Conditional conditional = new Conditional(driver);
        int index = conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Multi:  " + new Date());

        assertThat(index, equalTo(0));
        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.READY));
        assertThat(resultInfo.getAdditionalInfo().get(ResultType.LOCATOR), equalTo(By.name("Insurance")));
    }

    @Test
    public void criteriaMakerConfigUrlsTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        String value = "aaaa";

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forUrlContains(value));
        criteria.add(CriteriaMaker.forUrlDoesNotContain(".com"));
        criteria.add(CriteriaMaker.forUrlEquals(value));
        criteria.add(CriteriaMaker.forUrlEqualsIgnoreCase(value));
        criteria.add(CriteriaMaker.forUrlRegEx(value));
        criteria.add(CriteriaMaker.forUrlNotEqual(value));

        LOG.info("Start URLS:  " + new Date());
        Conditional conditional = new Conditional(driver);
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop URLS:  " + new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.URL_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigDropDownTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        By locator = By.name("season");
        String value = "aaaa";

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forDropDownContains(locator, value));
        criteria.add(CriteriaMaker.forDropDownDoesNotContain(locator, "Summer"));
        criteria.add(CriteriaMaker.forDropDownEquals(locator, value));
        criteria.add(CriteriaMaker.forDropDownEqualsIgnoreCase(locator, value));
        criteria.add(CriteriaMaker.forDropDownRegEx(locator, value));
        criteria.add(CriteriaMaker.forDropDownNotEqual(locator, value));

        LOG.info("Start DropDown:  " + new Date());
        Conditional conditional = new Conditional(driver);
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop DropDown:  " + new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.DROPDOWN_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigAttributeTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        By locator = By.id("division");
        String value = "aaaa";

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forInputContains(locator, value));
        criteria.add(CriteriaMaker.forInputDoesNotContain(locator, ""));
        criteria.add(CriteriaMaker.forInputEquals(locator, value));
        criteria.add(CriteriaMaker.forInputEqualsIgnoreCase(locator, value));
        criteria.add(CriteriaMaker.forInputRegEx(locator, value));
        criteria.add(CriteriaMaker.forInputNotEqual(locator, value));

        LOG.info("Start Attribute:  " + new Date());
        Conditional conditional = new Conditional(driver);
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Attribute:  " + new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.ATTRIBUTE_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigTextTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        By locator = By.xpath("//div[@id='TopDiv']//strong");
        String value = "aaaa";

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forTextContains(locator, value));
        criteria.add(CriteriaMaker.forTextDoesNotContain(locator, "Hockey"));
        criteria.add(CriteriaMaker.forTextEquals(locator, value));
        criteria.add(CriteriaMaker.forTextEqualsIgnoreCase(locator, value));
        criteria.add(CriteriaMaker.forTextRegEx(locator, value));
        criteria.add(CriteriaMaker.forTextNotEqual(locator, value));

        LOG.info("Start Text:  " + new Date());
        Conditional conditional = new Conditional(driver);
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Text:  " + new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.TEXT_NOT_EQUAL));
    }

    @Test
    public void criteriaMakerConfigElementsTest() {
        WebDriver driver = getContext().getDriver();
        driver.get("http://www.truenorthhockey.com/");

        By locator = By.id("unknown");
        WebElement element = null;

        List<Criteria> criteria = new ArrayList<>();
        criteria.add(CriteriaMaker.forElementDisplayed(locator));
        criteria.add(CriteriaMaker.forElementRemoved(By.name("Insurance")));
        criteria.add(CriteriaMaker.forElementEnabled(locator));
        criteria.add(CriteriaMaker.forElementDisabled(locator));
        criteria.add(CriteriaMaker.forElementExists(locator));
        criteria.add(CriteriaMaker.forElementSelected(locator));
        criteria.add(CriteriaMaker.forElementUnselected(locator));
        criteria.add(CriteriaMaker.forElementStale(element));
        criteria.add(CriteriaMaker.forElementReady(By.name("Insurance")));

        LOG.info("Start Elements:  " + new Date());
        Conditional conditional = new Conditional(driver);
        conditional.waitForMatch(criteria);
        ResultInfo resultInfo = conditional.getResultInfo();
        LOG.info("Stop Elements:  " + new Date());

        assertThat(resultInfo.isMatch(), equalTo(true));
        assertThat(resultInfo.getAdditionalInfo().get(ResultType.LOCATOR), equalTo(By.name("Insurance")));
        assertThat(resultInfo.getCriteriaType(), equalTo(CriteriaType.READY));
    }

}
