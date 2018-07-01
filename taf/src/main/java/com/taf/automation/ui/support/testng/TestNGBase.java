package com.taf.automation.ui.support.testng;

import com.taf.automation.api.html.HtmlUtils;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.ExpectedConditionsUtil;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.Utils;
import datainstiller.data.DataPersistence;
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
import org.testng.annotations.Listeners;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ui.auto.core.pagecomponent.PageObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Listeners(AllureTestNGListener.class)
public class TestNGBase {
    protected static final Logger LOG = LoggerFactory.getLogger(TestNGBase.class);
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
                        .forEach(Allure.LIFECYCLE::fire);
            } catch (Exception ex) {
                String shortMessage = "Could not take screenshot for " + title;
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                String fullMessage = shortMessage + " due to exception:  \n\n" + sw.toString();
                MakeAttachmentEvent ev = new MakeAttachmentEvent(fullMessage.getBytes(), shortMessage, "text/plain");
                Allure.LIFECYCLE.fire(ev);
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
            } catch (Exception ex) {
                String shortMessage = "Could not take HTML for " + title;
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                String fullMessage = shortMessage + " due to exception:  \n\n" + sw.toString();

                attachmentFile = fullMessage.getBytes();
                attachmentTitle = shortMessage;
                type = "text/plain";
            }

            MakeAttachmentEvent ev = new MakeAttachmentEvent(attachmentFile, attachmentTitle, type);
            Allure.LIFECYCLE.fire(ev);
        }
    }

    /**
     * Install Driver locally from the resources
     *
     * @param driverName         - Driver Name
     * @param driverPropertyName - Driver Property Name
     */
    @SuppressWarnings({"squid:S3457", "squid:S2629", "squid:S00112"})
    private void installDriver(String driverName, String driverPropertyName) {
        String os = System.getProperty("os.name").toUpperCase();
        String subFolder;
        String extension = "";
        if (os.startsWith("MAC")) {
            subFolder = "/webdrivers/mac/";
        } else if (os.startsWith("LINUX")) {
            subFolder = "/webdrivers/linux/";
        } else if (os.startsWith("WINDOWS")) {
            boolean is64bit = System.getenv("ProgramFiles(x86)") != null;
            subFolder = (is64bit) ? "/webdrivers/win64/" : "/webdrivers/win32/";
            extension = ".exe";
        } else {
            // Unsupported OS, the driver will not be found
            subFolder = "/OS_NOT_SUPPORTED/";
            LOG.error("There is NO WebDriver (in project) for OS:  " + os);
        }

        String driverPath = System.getProperty("user.home") + subFolder + driverName + extension;
        try {
            File fileTarget = new File(driverPath);
            File fileSource = File.createTempFile("driverName", "");
            FileUtils.copyInputStreamToFile(getClass().getResourceAsStream(subFolder + driverName + extension), fileSource);
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
        if (System.getenv("JENKINS_HOME") == null) {
            installDriver("geckodriver", "webdriver.gecko.driver");
            installDriver("chromedriver", "webdriver.chrome.driver");
            if (System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) {
                installDriver("IEDriverServer", "webdriver.ie.driver");
                installDriver("MicrosoftWebDriver", "webdriver.edge.driver");
            }

            TestProperties props = TestProperties.getInstance();

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
        if (TestNGBase.testNgContext.get() == null) {
            TestNGBase.testNgContext.set(testNgContext);
        }

        time = System.currentTimeMillis();
    }

    @AfterTest(alwaysRun = true)
    public void closeDriver() {
        time = (System.currentTimeMillis() - time) / 1000;
        if (context() != null && context().getDriver() != null) {
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
        if (context() != null && context().getDriver() == null) {
            context().setTestProperties(props);
            context().init();
            logInfo("+INITIALIZING CONTEXT: " + context().getDriver().toString());
        }

        return context();
    }

    protected void attachDataSet(DataPersistence data, String name) {
        if (context() != null && context().getDriver() != null) {
            byte[] attachment = data.toXML().getBytes();
            MakeAttachmentEvent ev = new MakeAttachmentEvent(attachment, name, "text/xml");
            Allure.LIFECYCLE.fire(ev);
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
