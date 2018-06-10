package com.taf.automation.ui.support;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

public enum WebDriverTypeEnum {
    CHROME("chrome", ChromeDriver.class),
    EDGE("MicrosoftEdge", EdgeDriver.class),
    FIREFOX("firefox", FirefoxDriver.class),
    IE("internet explorer", InternetExplorerDriver.class),
    OPERA_BLINK("operablink", OperaDriver.class),
    SAFARI("safari", SafariDriver.class);

    String driverName;
    Class<? extends WebDriver> driverClass;

    WebDriverTypeEnum(String driverName, Class<? extends WebDriver> driverClass) {
        this.driverClass = driverClass;
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public WebDriver getNewWebDriver() {
        return getNewWebDriver(TestProperties.getInstance());
    }

    public WebDriver getNewWebDriver(TestProperties prop) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        Proxy proxy = null;
        if (prop.getHttpProxy() != null) {
            proxy = new Proxy();
            proxy.setHttpProxy(prop.getHttpProxy());
        }

        if (prop.getHttpsProxy() != null) {
            if (proxy == null) {
                proxy = new Proxy();
            }
            proxy.setSslProxy(prop.getHttpsProxy());
        }

        if (proxy == null && prop.isBrowserMobProxy()) {
            prop.startBrowserMobProxyForThread();
            proxy = prop.getSeleniumProxyForThread();
        }

        if (proxy != null) {
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }

        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        if (prop.getRemoteURL() != null) {
            capabilities.merge(getCapabilities(prop));
            try {
                return new RemoteWebDriver(new URL(prop.getRemoteURL()), capabilities);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL!", e);
            }
        }

        switch (this) {
            case FIREFOX:
                return getFirefoxDriver(prop, capabilities);
            case CHROME:
                return getChromeDriver(prop, capabilities);
            case SAFARI:
                return getSafariDriver(prop, capabilities);
            default:
                try {
                    Constructor<? extends WebDriver> constructor = driverClass.getConstructor(Capabilities.class);
                    return constructor.newInstance(capabilities);
                } catch (Exception e) {
                    String message = "Failed to instantiate WebDriver:  " + driverClass.getName() + "; Error:  " + e.getMessage();
                    throw new RuntimeException(message, e);
                }
        }
    }

    private DesiredCapabilities getCapabilities(TestProperties prop) {
        Platform platform = prop.getBrowserPlatform();
        if (platform == null) {
            platform = Platform.ANY;
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(getDriverName());
        capabilities.setVersion(prop.getBrowserVersion());
        capabilities.setPlatform(platform);
        capabilities.merge(prop.getExtraCapabilities());
        return capabilities;
    }

    private FirefoxDriver getFirefoxDriver(TestProperties prop, DesiredCapabilities capabilities) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("focusmanager.testmode", true);
        profile.setPreference("dom.max_chrome_script_run_time", 0);
        profile.setPreference("dom.max_script_run_time", 0);
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        if (prop.getUserAgent() != null) {
            profile.setPreference("general.useragent.override", prop.getUserAgent());
        }

        if (prop.getFirefoxBin() != null) {
            System.setProperty("webdriver.firefox.bin", prop.getFirefoxBin());
        }

        if (prop.noMarionette()) {
            System.setProperty("webdriver.firefox.marionette", "false");
        }

        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.merge(capabilities);
        firefoxOptions.setProfile(profile);
        firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.WARN);
        firefoxOptions.addArguments("--log fatal");

        return new FirefoxDriver(firefoxOptions);
    }

    private ChromeDriver getChromeDriver(TestProperties prop, DesiredCapabilities capabilities) {
        if (prop.getUserAgent() != null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-agent=" + prop.getUserAgent());
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        }

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.merge(capabilities);
        chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);

        return new ChromeDriver(chromeOptions);
    }

    private SafariDriver getSafariDriver(TestProperties prop, DesiredCapabilities capabilities) {
        SafariOptions options = new SafariOptions();
        options.merge(capabilities);
        return new SafariDriver(options);
    }

}
