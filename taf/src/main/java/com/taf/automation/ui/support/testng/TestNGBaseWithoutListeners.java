package com.taf.automation.ui.support.testng;

import com.taf.automation.api.html.HtmlUtils;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.util.ExpectedConditionsUtil;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import datainstiller.data.DataPersistence;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import ui.auto.core.pagecomponent.PageObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The TestNG base class for both TDD &amp; BDD<BR>
 * <B>Notes: </B> The Listeners are not added here because it causes duplicate results with BDD.
 */
public class TestNGBaseWithoutListeners {
    protected static final Logger LOG = LoggerFactory.getLogger(TestNGBaseWithoutListeners.class);
    private static final ThreadLocal<TestContext> context = ThreadLocal.withInitial(TestContext::new);
    private static final ThreadLocal<ITestContext> testNgContext = new ThreadLocal<>();
    private long time;

    public static TestContext context() {
        return context.get();
    }

    public static void takeScreenshot(String title) {
        if (context() != null && context().getDriver() != null) {
            try {
                Utils.getWebDriverWait()
                        .until(ExpectedConditionsUtil.takeScreenshot(title))
                        .forEach(Attachment::build);
            } catch (Exception ex) {
                String shortMessage = "Could not take screenshot for " + title;
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                String fullMessage = shortMessage + " due to exception:  \n\n" + sw.toString();
                new Attachment().withTitle(shortMessage).withType("text/plain").withFile(fullMessage.getBytes()).build();
            }
        }
    }

    public static void takeHTML(String title) {
        if (context() != null && context().getDriver() != null) {
            byte[] attachmentFile;
            String attachmentTitle;
            String type;

            try {
                attachmentFile = (context().getDriver().getPageSource()).getBytes();
                attachmentTitle = title;
                type = "text/html";
                if (AppiumDriver.class.isAssignableFrom(context().getDriver().getClass())) {
                    type = "text/xml";
                }
            } catch (Exception ex) {
                String shortMessage = "Could not take HTML for " + title;
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                String fullMessage = shortMessage + " due to exception:  \n\n" + sw.toString();

                attachmentFile = fullMessage.getBytes();
                attachmentTitle = shortMessage;
                type = "text/plain";
            }

            new Attachment().withTitle(attachmentTitle).withType(type).withFile(attachmentFile).build();
        }
    }

    /**
     * Install Driver locally from the resources
     *
     * @param driverName         - Driver Name
     * @param driverPropertyName - Driver Property Name
     */
    @SuppressWarnings({"squid:S3457", "squid:S2629", "squid:S00112", "squid:S899"})
    private void installDriver(String driverName, String driverPropertyName) {
        String os = System.getProperty("os.name").toUpperCase();
        String replaceOS = "<REPLACE_OS>";
        String replaceSeparator = "<REPLACE_SEPARATOR>";
        String separator = System.getProperty("file.separator");
        String genericSubFolder = replaceSeparator + "webdrivers" + replaceSeparator + replaceOS + replaceSeparator;
        String subFolder;
        String resourceSubFolder;
        String extension = "";
        if (os.startsWith("MAC")) {
            subFolder = genericSubFolder.replace(replaceSeparator, separator).replace(replaceOS, "mac");
            resourceSubFolder = genericSubFolder.replace(replaceSeparator, "/").replace(replaceOS, "mac");
        } else if (os.startsWith("LINUX")) {
            subFolder = genericSubFolder.replace(replaceSeparator, separator).replace(replaceOS, "linux");
            resourceSubFolder = genericSubFolder.replace(replaceSeparator, "/").replace(replaceOS, "linux");
        } else if (os.startsWith("WINDOWS")) {
            boolean is64bit = System.getenv("ProgramFiles(x86)") != null;
            String winOS = (is64bit) ? "win64" : "win32";
            subFolder = genericSubFolder.replace(replaceSeparator, separator).replace(replaceOS, winOS);
            resourceSubFolder = genericSubFolder.replace(replaceSeparator, "/").replace(replaceOS, winOS);
            extension = ".exe";
        } else {
            // Unsupported OS, the driver will not be found
            subFolder = separator + "OS_NOT_SUPPORTED" + separator;
            resourceSubFolder = "/OS_NOT_SUPPORTED/";
            LOG.error("There is NO WebDriver (in project) for OS:  " + os);
        }

        String driverPath = System.getProperty("user.home") + subFolder + driverName + extension;
        try {
            File fileTarget = new File(driverPath);
            File fileSource = File.createTempFile("driverName", "");
            FileUtils.copyInputStreamToFile(getClass().getResourceAsStream(resourceSubFolder + driverName + extension), fileSource);
            if (!FileUtils.contentEquals(fileSource, fileTarget)) {
                FileUtils.copyFile(fileSource, fileTarget);
                fileTarget.setExecutable(true, true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.setProperty(driverPropertyName, driverPath);
    }

    /**
     * Setup the drivers for local use only
     */
    private void setUpDrivers() {
        TestProperties props = TestProperties.getInstance();
        if (props.isAlwaysInstallDrivers() || System.getenv("JENKINS_HOME") == null) {
            installDriver("geckodriver", "webdriver.gecko.driver");
            installDriver("chromedriver", "webdriver.chrome.driver");
            if (System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) {
                installDriver("IEDriverServer", "webdriver.ie.driver");
                installDriver("MicrosoftWebDriver", "webdriver.edge.driver");
            }

            if (props.getFirefoxBin() != null) {
                System.setProperty("webdriver.firefox.bin", props.getFirefoxBin());
            }

            if (props.noMarionette()) {
                System.setProperty("webdriver.firefox.marionette", "false");
            }
        }
    }

    @BeforeSuite
    public void initSuite(ITestContext testNgContext) {
        setUpDrivers();
        Integer threadCount = TestProperties.getInstance().getThreadCount();
        if (threadCount != null) {
            testNgContext.getSuite().getXmlSuite().setThreadCount(threadCount);
        }
    }

    @BeforeTest
    public void initTest(ITestContext testNgContext) {
        if (TestNGBaseWithoutListeners.testNgContext.get() == null) {
            TestNGBaseWithoutListeners.testNgContext.set(testNgContext);
        }

        time = System.currentTimeMillis();
    }

    @AfterTest(alwaysRun = true)
    public void closeDriver() {
        time = (System.currentTimeMillis() - time) / 1000;
        if (context() != null && context().getDriver() != null) {
            Utils.restoreBrowser(); // If browser was changed, ensure it is closed
            logInfo("-CLOSING CONTEXT: " + context().getDriver().toString());
            context().getDriver().quit();
        }

        context.remove();
        testNgContext.remove();
        stopBrowserMobProxyForThread();
    }

    private void stopBrowserMobProxyForThread() {
        String browserMobProxyLog = "proxy-log-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis() + ".har";
        TestProperties.getInstance().performWriteBrowserMobProxyLogToFile(browserMobProxyLog);
        TestProperties.getInstance().stopBrowserMobProxyForThread();
    }

    protected void setAttribute(String key, Object value) {
        testNgContext.get().getSuite().setAttribute(key, value);
    }

    protected Object getAttribute(String key) {
        Object obj = testNgContext.get().getSuite().getAttribute(key);
        if (obj instanceof PageObject) {
            TestContext objContext = ((PageObject) obj).getContext();
            if (objContext != null && !objContext.equals(context())) {
                LOG.error("Page Object Context Collision Detected");
            }
        }

        if (obj instanceof DomainObject) {
            TestContext objContext = ((DomainObject) obj).getContext();
            if (objContext != null && !objContext.equals(context())) {
                LOG.error("Domain Object Context Collision Detected");
            }
        }

        return obj;
    }

    protected TestContext getContext() {
        return getContext(TestProperties.getInstance());
    }

    protected TestContext getContext(TestProperties props) {
        if (isDriverInitializationRequired(props.getBrowserType().isAppiumDriver())) {
            context().setTestProperties(props);
            context().init();
            logInfo("+INITIALIZING CONTEXT: " + context().getDriver().toString());
        }

        return context();
    }

    /**
     * Check if driver Initialization required (or possible)
     *
     * @param appiumDriver - true if appium driver
     * @return true if initialization of the driver is required else false
     */
    private boolean isDriverInitializationRequired(boolean appiumDriver) {
        if (context() == null) {
            // If context is null, then we cannot initialize the driver
            return false;
        }

        if (context().getDriver() == null) {
            // If the driver is null, then initialization of the driver is required
            return true;
        }

        try {
            // Try to switch to the current window
            // Note:  Appium (as of 1.14.0) does not support getting the window handle
            if (!appiumDriver) {
                context().getDriver().switchTo().window(context().getDriver().getWindowHandle());
            }

            // Able to switch to the current window as such no initialization of the driver is necessary
            return false;
        } catch (Exception ex) {
            // If an exception occurs, then initialization of the driver is required
            return true;
        }
    }

    protected void attachDataSet(DataPersistence data, String name) {
        if (context() != null && context().getDriver() != null) {
            byte[] attachment = data.toXML().getBytes();
            new Attachment().withTitle(name).withType("text/xml").withFile(attachment).build();
        }
    }

    private String logNodeIp() {
        try {
            String url = TestProperties.getInstance().getRemoteURL();
            url = url.replace("/wd/hub", "/grid/api/testsession?session=" + ((RemoteWebDriver) getContext().getDriver()).getSessionId());
            String jsonResp = HtmlUtils.getElementText(url, "html");
            Pattern pattern = Pattern.compile("proxyId\":\"(.*)\"}");
            Matcher matcher = pattern.matcher(jsonResp);
            if (matcher.find()) {
                return ("\nSELENIUM SERVER NODE: " + matcher.group(1));
            }
        } catch (Exception ignore) {
            //
        }

        return "";
    }

    private StringBuilder getFailedConfigOrTests(Set<ITestResult> results) {
        StringBuilder log = new StringBuilder();
        log.append(" ---> \u001B[31mTEST FAILED :(\u001B[0m");
        log.append("\n");
        for (ITestResult result : results) {
            StringWriter stack = new StringWriter();
            result.getThrowable().printStackTrace(new PrintWriter(new PrintWriter(stack)));
            log.append("\n").append(stack);
        }

        return log;
    }

    @SuppressWarnings("squid:S2629")
    private void logInfo(String msg) {
        StringBuilder log = new StringBuilder();
        String delim = "\n" + StringUtils.repeat("=", msg.length());
        log.append("ThreadId: ").append(Thread.currentThread().getId());
        log.append(delim);
        log.append("\nTEST: ").append(getTestInfo());
        log.append("\n").append(msg);
        log.append(logNodeIp());
        if (msg.startsWith(("-CLOSING"))) {
            log.append("\nCOMPLETED AT:  " + new Date());
            log.append("\nTEST DURATION: ").append(time).append(" seconds");
            if (testNgContext.get().getFailedTests().size() > 0 || testNgContext.get().getFailedConfigurations().size() > 0) {
                log.append(getFailedConfigOrTests(testNgContext.get().getFailedConfigurations().getAllResults()));
                log.append(getFailedConfigOrTests(testNgContext.get().getFailedTests().getAllResults()));
            } else {
                log.append(" ---> \u001B[32mTEST PASSED :)\u001B[0m");
            }
        }

        log.append(delim).append("\n");
        LOG.info(log.toString());
    }

    public static String getTestInfo() {
        String testInfo = testNgContext.get().getCurrentXmlTest().getName();
        testInfo += " " + testNgContext.get().getCurrentXmlTest().getLocalParameters().toString();
        return testInfo;
    }

}
