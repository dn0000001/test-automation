package com.taf.automation.ui.support;

import com.deque.axe.AXE;
import com.taf.automation.ui.support.testng.Attachment;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.pagecomponent.PageComponent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * This class is for testing page accessibility<BR>
 * <B>Note: </B> If running multiple tests in parallel, then this class should only be initialized once per thread.<BR>
 */
public class Accessibility {
    private static final URL SCRIPT_URL = Thread.currentThread().getContextClassLoader().getResource("JS/axe.min.js");
    private WebDriver driver;
    private AXE.Builder builder;
    private boolean axeOn;
    private boolean axeViolationsOnlyLog;
    private boolean axeLoggingDelayed;
    private List<ViolationDetails> storedViolations;

    private class ViolationDetails {
        private String pageName;
        private JSONArray violations;

        public ViolationDetails(String pageName, JSONArray violations) {
            this.pageName = pageName;
            this.violations = violations;
        }

        public String getPageName() {
            return pageName;
        }

        public JSONArray getViolations() {
            return violations;
        }

    }

    public Accessibility(WebDriver driver) {
        setDriver(driver);
        resetBuilder();
        setAxeOn(false);
        setAxeViolationsOnlyLog(true);
        setAxeLoggingDelayed(true);
        storedViolations = new ArrayList<>();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Get the current builder such that custom options can be set<BR>
     * <B>Note: </B>Any changes to the builder affect the accessibility tests globally.  So, if specific
     * custom options are necessary, reset the builder immediately after the specific page test.<BR>
     *
     * @return AXE.Builder
     */
    public AXE.Builder getBuilder() {
        return builder;
    }

    /**
     * Reset the builder to the default
     */
    public void resetBuilder() {
        setBuilder(new AXE.Builder(driver, SCRIPT_URL));
    }

    public void setBuilder(AXE.Builder builder) {
        this.builder = builder;
    }

    /**
     * Turn accessibility testing ON or OFF
     *
     * @param axeOn - true to have accessibility tests run, false to skip
     */
    public void setAxeOn(boolean axeOn) {
        this.axeOn = axeOn;
    }

    /**
     * Control whether violations are only logged or violations cause the test to fail<BR>
     * <B>Note:</B> The accessibility testing flag needs to be set to true for this flag to be applied.<BR>
     *
     * @param axeViolationsOnlyLog
     */
    public void setAxeViolationsOnlyLog(boolean axeViolationsOnlyLog) {
        this.axeViolationsOnlyLog = axeViolationsOnlyLog;
    }

    /**
     * Control when violations are logged<BR>
     * <B>Note:</B> The accessibility testing flag needs to be set to true for this flag to be applied.<BR>
     *
     * @param axeLoggingDelayed
     */
    public void setAxeLoggingDelayed(boolean axeLoggingDelayed) {
        this.axeLoggingDelayed = axeLoggingDelayed;
    }

    /**
     * Analyze the current full page
     *
     * @param pageName - Page Name for logging
     */
    public void analyze(String pageName) {
        if (!axeOn) {
            return;
        }

        analyzeFullPage(pageName);
    }

    /**
     * Analyze the specific component on the current page
     *
     * @param pageName      - Page Name for logging
     * @param componentName - Component Name for logging
     * @param component     - Component to get element to perform the accessibility test on
     */
    public void analyze(String pageName, String componentName, PageComponent component) {
        if (!axeOn) {
            return;
        }

        analyze(pageName, componentName, component.getCoreElement());
    }

    /**
     * Analyze the specific element on the current page
     *
     * @param pageName    - Page Name for logging
     * @param elementName - Element Name for logging
     * @param element     - Element to perform the accessibility test on
     */
    public void analyze(String pageName, String elementName, WebElement element) {
        if (!axeOn) {
            return;
        }

        analyzePartialPage(pageName + " -> " + elementName, element);
    }


    @Step("Analyze Current Full Page Accessibility:  {0}")
    private void analyzeFullPage(String pageName) {
        analyzeGeneric(pageName, null);
    }

    @Step("Analyze Current Partial Page Accessibility:  {0}")
    private void analyzePartialPage(String pageName, WebElement element) {
        analyzeGeneric(pageName, element);
    }

    /**
     * Generic method to perform the accessibility tests
     *
     * @param pageName - Page Name for logging
     * @param element  - If null, then full page test else only test the element
     */
    private void analyzeGeneric(String pageName, WebElement element) {
        JSONObject responseJSON = (element != null) ? builder.analyze(element) : builder.analyze();
        JSONArray violations = responseJSON.getJSONArray("violations");
        if (axeLoggingDelayed) {
            storedViolations.add(new ViolationDetails(pageName, violations));
            return;
        }

        attachViolationDetailsToReport(violations);
        if (axeViolationsOnlyLog) {
            return;
        }

        assertThat("Violations", violations.length(), equalTo(0));
    }

    @Step("Violations for Page Name:  {0}")
    private void attachViolationDetailsToReport(String pageName, JSONArray violations) {
        attachViolationDetailsToReport(violations);
    }

    private void attachViolationDetailsToReport(JSONArray violations) {
        if (violations.length() == 0) {
            return;
        }

        String violationDetails = AXE.report(violations);
        new Attachment().withTitle("Violations").withType("text/plain").withFile(violationDetails.getBytes()).build();
    }

    /**
     * Verify that there were no accessibility violations if necessary
     */
    public void verify() {
        if (!axeOn) {
            return;
        }

        if (!axeLoggingDelayed) {
            return;
        }

        compileAccessibilityValidationResults();
    }

    @Step("Compile Accessibility Validation Results")
    private void compileAccessibilityValidationResults() {
        boolean noViolations = axeViolationsOnlyLog || storedViolations.isEmpty();
        for (ViolationDetails details : storedViolations) {
            if (details.getViolations().length() > 0) {
                attachViolationDetailsToReport(details.getPageName(), details.getViolations());
            }
        }

        assertThat("Detected Violations.  See report for details.", noViolations);
    }

}
