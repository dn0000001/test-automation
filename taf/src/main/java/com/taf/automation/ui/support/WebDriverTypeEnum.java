package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.Utils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

public enum WebDriverTypeEnum {
    CHROME(BrowserType.CHROME, ChromeDriver.class),
    EDGE(BrowserType.EDGE, EdgeDriver.class),
    FIREFOX(BrowserType.FIREFOX, FirefoxDriver.class),
    IE(BrowserType.IE, InternetExplorerDriver.class),
    OPERA_BLINK(BrowserType.OPERA_BLINK, OperaDriver.class),
    SAFARI(BrowserType.SAFARI, SafariDriver.class),
    ANDROID(BrowserType.ANDROID, AndroidDriver.class),
    IPHONE(BrowserType.IPHONE, IOSDriver.class),
    IPAD(BrowserType.IPAD, IOSDriver.class),
    ;

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
        TestProperties prop = Utils.deepCopy(TestProperties.getInstance());
        Utils.writeField(prop, "browserType", this);
        return getNewWebDriver(prop);
    }

    /**
     * Get a New Web Driver based on the passed Test Properties
     *
     * @param prop - Test Properties to be used to get the new WebDriver
     * @return WebDriver
     */
    public WebDriver getNewWebDriver(TestProperties prop) {
        return getNewWebDriver(prop, null);
    }

    /**
     * Get a New Web Driver based on the passed Test Properties &amp; Desired Capabilities<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>The extra Desired Capabilities is applied first.  The Test Properties Extra Capabilities is applied after</LI>
     * </OL>
     *
     * @param prop  - Test Properties to be used to get the new WebDriver
     * @param extra - The extra Desired Capabilities (not applied to remote execution)
     * @return WebDriver
     */
    @SuppressWarnings("squid:S00112")
    public WebDriver getNewWebDriver(TestProperties prop, DesiredCapabilities extra) {
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

        if (prop.getRemoteURL() != null && !prop.getBrowserType().isAppiumDriver()) {
            try {
                return new RemoteWebDriver(new URL(prop.getRemoteURL()), getRemoteCapabilities(prop, capabilities));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL!", e);
            }
        }

        // Note:  Null desired capabilities will just skip
        capabilities.merge(extra);

        switch (prop.getBrowserType()) {
            case FIREFOX:
                return getFirefoxDriver(prop, capabilities);
            case CHROME:
                return getChromeDriver(prop, capabilities);
            case SAFARI:
                return getSafariDriver(prop, capabilities);
            case EDGE:
                return getEdgeDriver(prop, capabilities);
            case ANDROID:
                return getAndroidDriver(prop, capabilities);
            case IPAD:
            case IPHONE:
                return getIOSDriver(prop, capabilities);
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

    private DesiredCapabilities getRemoteCapabilities(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        Platform platform = prop.getBrowserPlatform();
        if (platform == null) {
            platform = Platform.ANY;
        }

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(prop.getBrowserType().getDriverName());
        capabilities.setVersion(prop.getBrowserVersion());
        capabilities.setPlatform(platform);
        capabilities.merge(prop.getExtraCapabilities());

        if (prop.getBrowserType() == FIREFOX) {
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, getFirefoxOptions(prop, mergeCapabilities, false));
        } else if (prop.getBrowserType() == CHROME) {
            capabilities.setCapability(ChromeOptions.CAPABILITY, getChromeOptions(prop, mergeCapabilities));
        } else if (prop.getBrowserType() == SAFARI) {
            capabilities.setCapability(SafariOptions.CAPABILITY, getSafariOptions(prop, mergeCapabilities));
        } else {
            capabilities.merge(mergeCapabilities);
        }

        return capabilities;
    }

    private FirefoxDriver getFirefoxDriver(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        FirefoxOptions firefoxOptions = getFirefoxOptions(prop, mergeCapabilities, true);
        return new FirefoxDriver(firefoxOptions);
    }

    private FirefoxOptions getFirefoxOptions(TestProperties prop, DesiredCapabilities mergeCapabilities, boolean local) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("focusmanager.testmode", true);
        profile.setPreference("dom.max_chrome_script_run_time", 0);
        profile.setPreference("dom.max_script_run_time", 0);

        // Handle SSO on specified sites
        String uris = prop.getFirefoxNTLM_URIS();
        if (StringUtils.isNotBlank(uris)) {
            profile.setPreference("network.automatic-ntlm-auth.trusted-uris", uris);
        }

        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        if (prop.getUserAgent() != null) {
            profile.setPreference("general.useragent.override", prop.getUserAgent());
        }

        if (local) {
            setFirefoxLocalProperties(prop);
        }

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.merge(mergeCapabilities);
        firefoxOptions.setProfile(profile);
        firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.WARN);
        firefoxOptions.addArguments("--log fatal");
        return firefoxOptions;
    }

    private void setFirefoxLocalProperties(TestProperties prop) {
        if (prop.getFirefoxBin() != null) {
            System.setProperty("webdriver.firefox.bin", prop.getFirefoxBin());
        }

        if (prop.noMarionette()) {
            System.setProperty("webdriver.firefox.marionette", "false");
        }

        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
    }

    private ChromeDriver getChromeDriver(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        ChromeOptions chromeOptions = getChromeOptions(prop, mergeCapabilities);
        return new ChromeDriver(chromeOptions);
    }

    private ChromeOptions getChromeOptions(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--dns-prefetch-disable");

        if (prop.getUserAgent() != null) {
            chromeOptions.addArguments("user-agent=" + prop.getUserAgent());
        }

        chromeOptions.merge(mergeCapabilities);
        chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        return chromeOptions;
    }

    private SafariDriver getSafariDriver(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        SafariOptions options = getSafariOptions(prop, mergeCapabilities);
        return new SafariDriver(options);
    }

    @SuppressWarnings("squid:S1172")
    private SafariOptions getSafariOptions(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        SafariOptions options = new SafariOptions();
        options.merge(mergeCapabilities);
        return options;
    }

    private EdgeDriver getEdgeDriver(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        EdgeOptions options = getEdgeOptions(prop, mergeCapabilities);
        return new EdgeDriver(options);
    }

    @SuppressWarnings("squid:S1172")
    private EdgeOptions getEdgeOptions(TestProperties prop, DesiredCapabilities mergeCapabilities) {
        EdgeOptions options = new EdgeOptions();
        options.merge(mergeCapabilities);
        return options;
    }

    private AndroidDriver getAndroidDriver(TestProperties prop, DesiredCapabilities capabilities) {
        capabilities.setBrowserName(null);
        capabilities.setPlatform(null);
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, Platform.ANDROID);
        capabilities.merge(prop.getExtraCapabilities());
        return new AndroidDriver(getRemoteURL(prop), capabilities);
    }

    private IOSDriver getIOSDriver(TestProperties prop, DesiredCapabilities capabilities) {
        capabilities.setBrowserName(null);
        capabilities.setPlatform(null);
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, Platform.IOS);
        capabilities.merge(prop.getExtraCapabilities());
        return new IOSDriver(getRemoteURL(prop), capabilities);
    }

    @SuppressWarnings("squid:S00112")
    private URL getRemoteURL(TestProperties prop) {
        try {
            return new URL(prop.getRemoteURL());
        } catch (Exception ex) {
            String message = "webdriver.remote.url property had invalid URL:  " + prop.getRemoteURL();
            throw new RuntimeException(message, ex);
        }
    }

    public boolean isAppiumDriver() {
        return AppiumDriver.class.isAssignableFrom(driverClass);
    }

}
