package com.taf.automation.ui.support;

import com.taf.automation.ui.support.converters.Credentials;
import com.taf.automation.ui.support.converters.CredentialsObjectPropertyConverter;
import com.taf.automation.ui.support.converters.CredentialsPropertyConverter;
import com.taf.automation.ui.support.converters.CreditCard;
import com.taf.automation.ui.support.converters.CreditCardPropertyConverter;
import com.taf.automation.ui.support.converters.DynamicCredentials;
import com.taf.automation.ui.support.converters.DynamicCredentialsPropertyConverter;
import com.taf.automation.ui.support.converters.EnumPropertyConverter;
import com.taf.automation.ui.support.converters.EnvironmentPropertyConverter;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.util.URLUtils;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.yandex.qatools.commons.model.Parameter;
import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;
import ru.yandex.qatools.properties.annotations.Use;
import ui.auto.core.support.EnvironmentsSetup;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The class holds all the test properties
 */
@Resource.Classpath("test.properties")
public class TestProperties {
    @Use(EnvironmentPropertyConverter.class)
    @Property("test.env")
    @HideInReport
    private EnvironmentsSetup.Environment testEnvironment;

    @Property("always.install.drivers")
    private boolean alwaysInstallDrivers = false;

    @Property("global.encryption")
    private boolean globalEncryption = false;

    @Property("mail.proxy")
    private String mailProxy;

    @Property("webdriver.http.proxy")
    private String httpProxy;

    @Property("webdriver.https.proxy")
    private String httpsProxy;

    @Property("user.agent")
    private String userAgent;

    @Property("webdriver.remote.url")
    private String remoteURL;

    @Use(EnumPropertyConverter.class)
    @Property("webdriver.browser.platform")
    private Platform platform;

    @Property("webdriver.browser.version")
    private String version;

    @Use(EnumPropertyConverter.class)
    @Property("webdriver.browser.type")
    private WebDriverTypeEnum browserType = WebDriverTypeEnum.FIREFOX;

    @Property("webdriver.extra.capabilities")
    private String extraCapabilities;

    @Property(("webdriver.screen.size"))
    private String screenSize;

    @Property("app.api.url")
    private String apiUrl;

    @Property("app.api.url.prod")
    private String apiUrlProd;

    @Use(EnumPropertyConverter.class)
    @Property("app.environment")
    private Environment environment;

    @Property("app.environment.target")
    private String environmentTarget;

    @Use(CredentialsObjectPropertyConverter.class)
    @Property("app.credentials.api")
    @HideInReport
    private Credentials apiCredentials;

    @Use(CredentialsObjectPropertyConverter.class)
    @Property("app.credentials.api.prod")
    @HideInReport
    private Credentials apiCredentialsProd;

    @Property("ci.http.proxy")
    private String ciHttpProxy;

    @Property("ci.https.proxy")
    private String ciHttpsProxy;

    @Use(CreditCardPropertyConverter.class)
    @Property("app.creditcard.info")
    @HideInReport
    private CreditCard[] appCreditCards;

    @Use(DynamicCredentialsPropertyConverter.class)
    @Property("dynamic.credentials")
    @HideInReport
    private DynamicCredentials[] dynamicCredentials;

    @Use(CredentialsPropertyConverter.class)
    @Property("app.credentials")
    @HideInReport
    private Credentials[] appCredentials;

    @Use(CredentialsPropertyConverter.class)
    @Property("app.credentials.prod")
    @HideInReport
    private Credentials[] appCredentialsProd;

    @Property("timeout.page.load")
    private int pageLoadTimeout = 5; // In minutes

    @SuppressWarnings("squid:S00116")
    @Property("timeout.page")
    private int page_timeout; // In seconds

    @Property("timeout.element")
    private int element_timeout; // In seconds

    @SuppressWarnings("squid:S00116")
    @Property("timeout.negative")
    private int negative_timeout; // In seconds

    @Property("test.suites")
    private String suites;

    @Property("report.folder")
    protected File reportFolder = new File("target/allure-report");

    @Property("report.show")
    private boolean showReport;

    @Property("report.its.pattern")
    private String issueTrackingSystemPattern;

    @Property("allure.tests.management.pattern")
    private String testsManagementPattern;

    @Property("report.port")
    private int reportPort = 8090;

    @Property("test.default.retry")
    private int testDefaultRetry = 2;

    @Property("db.host")
    @HideInReport
    private String dbHost;

    @Property("db.port")
    @HideInReport
    private int dbPort;

    @Property("db.user")
    @HideInReport
    private String dbUserName;

    @Property("db.password")
    @HideInReport
    private String dbPassword;

    @Property("db.integrated.security")
    @HideInReport
    private boolean dbIntegratedSecurity = false;

    @Property("db.name")
    private String dbName; // Database (name) to use

    @Property("db.timeout")
    private int dbTimeout = 60000; // Connection & Socket Timeout in milliseconds. If 0 no timeout.

    @Property("db.query.timeout")
    private int dbQueryTimeout = 120; // Connection & Socket Timeout in seconds. If -1 no timeout.

    @Property("api.timeout")
    private int apiTimeout = 10000; // Connection & Socket Timeout in milliseconds. If 0 no timeout.

    @Property("ssh.host")
    @HideInReport
    private String sshHost;

    @Property("ssh.host.prod")
    @HideInReport
    private String sshHostProd;

    @Property("ssh.port")
    @HideInReport
    private int sshPort;

    @Property("ssh.user")
    @HideInReport
    private String sshUser = "tunnel";

    @Property("ssh.encrypt.password")
    @HideInReport
    private String sshPassword;

    @Property("ssh.timeout")
    @HideInReport
    private int sshTimeout = 10000; // In milliseconds

    @Property("app.url")
    private String url;

    @Property("app.url.prod")
    private String urlProd;

    @Property("test.parallel.threads")
    private Integer threadCount;

    @Property("firefox.bin")
    private String firefoxBin;

    @Property("firefox.no.marionette")
    private boolean useNoMarionette;

    @SuppressWarnings("squid:S00116")
    @Property("firefox.ntlm.auto")
    private boolean firefox_NTLM_Auto;

    @SuppressWarnings("squid:S00116")
    @Property("firefox.ntlm.uris")
    private String firefox_NTLM_URIS;

    @Property("webdriver.screenshot.view.port.only")
    private boolean viewPortOnly = true;

    // May need edit configuration for the working directory when running from IDE.
    // Use same working directory as RunTests.
    @Property("source.js")
    private String sourceJS = "taf/src/main/resources/JS";

    @Property("source.sql")
    private String sourceSQL = "taf/src/main/resources/SQL";

    @Property("documentation.mode")
    private boolean documentationMode = false;

    @Property("mail.server")
    private String mailServer;

    @Property("mail.server.port")
    private int mailServerPort;

    @Property("mail.password")
    @HideInReport
    private String mailPassword;

    @Property("mail.timeout")
    private int mailTimeout = 30000; // In milliseconds

    @Property("axe.on")
    private boolean axeOn = false;

    @Property("axe.violations.only.log")
    private boolean axeViolationsOnlyLog = true;

    @Property("axe.logging.delayed")
    private boolean axeLoggingDelayed = true;

    @Property("automation.key")
    @HideInReport
    private String automationKey;

    @Property("consul.host")
    @HideInReport
    private String consulHost = "consul001";

    @Property("consul.port")
    @HideInReport
    private int consulPort = 8500;

    @Property("browser.mob.proxy.flag")
    private boolean useBrowserMobProxy = false;

    @Property("browser.mob.proxy.write.file")
    private boolean writeBrowserMobProxyLogToFile = false;

    /**
     * Stores the Browser Mob Proxy for each thread
     */
    @SuppressWarnings("squid:S1149")
    private Map<Long, BrowserMobProxy> browserMobProxies = new Hashtable<>();

    /**
     * For better performance, cache the users.  Only use the getter to access
     */
    private Map<String, EnvironmentsSetup.User> cachedUsers;

    /**
     * For better performance, cache the custom properties.  Only use the getter to access
     */
    private Map<String, EnvironmentsSetup.Property> cachedCustomProps;

    /**
     * Constructor
     */
    private TestProperties() {
        populateEnvProp();
        PropertyLoader.populate(this);
        if (issueTrackingSystemPattern != null) {
            System.setProperty("allure.issues.tracker.pattern", issueTrackingSystemPattern);
        }

        if (testsManagementPattern != null) {
            System.setProperty("allure.tests.management.pattern", testsManagementPattern);
        }
    }

    private static class LazyHolder {
        private static final TestProperties INSTANCE = new TestProperties();
    }

    public static TestProperties getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Populate Environment/System variables. Environment/System variables takes precedence.
     */
    private void populateEnvProp() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Property.class)) {
                String prop = field.getAnnotation(Property.class).value();
                String value = getEnvValue(prop.replace(".", "_"));
                if (value != null) {
                    System.setProperty(prop, value);
                }
            }
        }
    }

    /**
     * Get Environment/System Property
     *
     * @param prop - Property to get
     * @return null if environment/system property does not exist else value
     */
    private String getEnvValue(String prop) {
        for (String key : System.getenv().keySet()) {
            if (prop.equalsIgnoreCase(key)) {
                return System.getenv(key);
            }
        }

        return null;
    }

    public EnvironmentsSetup.Environment getTestEnvironment() {
        return testEnvironment;
    }

    /**
     * Get the field with fallback to environment property
     *
     * @param field            - Field from this class
     * @param replaceTargetENV - true to replace the target environment in the field
     * @param key              - the environment custom key to lookup value if necessary
     * @param decode           - true to decode the environment value if necessary
     * @return String
     */
    private String getFieldWithFallBack(String field, boolean replaceTargetENV, String key, boolean decode) {
        String value = (replaceTargetENV) ? replaceWithTargetEnv(field) : field;
        if (value == null && key != null) {
            value = getEnvironmentCustom(key, decode);
        }

        return value;
    }

    /**
     * Get the field with fallback to environment property
     *
     * @param field             - Field from this class
     * @param fallbackAttemptOn - if the field equals this value, then use environment to lookup value
     * @param key               - the environment custom key to lookup value if necessary
     * @param decode            - true to decode the environment value if necessary
     * @return int
     */
    private int getFieldWithFallBack(int field, int fallbackAttemptOn, String key, boolean decode) {
        if (field == fallbackAttemptOn) {
            String value = getEnvironmentCustom(key, decode);
            return NumberUtils.toInt(value, field);
        } else {
            return field;
        }
    }

    public boolean isCustom(String key) {
        return BooleanUtils.toBoolean(getEnvironmentCustom(key, false));
    }

    public String getCustom(String key, String defaultValue) {
        return getCustom(key, defaultValue, false);
    }

    public String getCustom(String key, String defaultValue, boolean decode) {
        String value = getEnvironmentCustom(key, decode);
        return (value == null) ? defaultValue : value;
    }

    private String getEnvironmentCustom(String key, boolean decode) {
        if (getTestEnvironment() != null) {
            try {
                String value = getCachedCustomProps().get(key).getValue();
                return (decode) ? new CryptoUtils().decrypt(value) : value;
            } catch (Exception ex) {
                return null;
            }
        }

        return null;
    }

    @SuppressWarnings("java:S2168")
    private Map<String, EnvironmentsSetup.Property> getCachedCustomProps() {
        if (cachedCustomProps == null) {
            synchronized (this) {
                if (cachedCustomProps == null) {
                    try {
                        Field fieldCustom = FieldUtils.getField(EnvironmentsSetup.Environment.class, "custom", true);
                        cachedCustomProps = ((List<EnvironmentsSetup.Property>) FieldUtils.readField(fieldCustom, getTestEnvironment(), true))
                                .stream()
                                .collect(Collectors.toMap(EnvironmentsSetup.Property::getName, item -> item, (lhs, rhs) -> lhs));
                    } catch (Exception ex) {
                        cachedCustomProps = new HashMap<>();
                    }
                }
            }
        }

        return cachedCustomProps;
    }

    public EnvironmentsSetup.User getUser(String role) {
        return getCachedUsers().get(role.toLowerCase());
    }

    @SuppressWarnings("java:S2168")
    private Map<String, EnvironmentsSetup.User> getCachedUsers() {
        if (cachedUsers == null) {
            synchronized (this) {
                if (cachedUsers == null) {
                    try {
                        cachedUsers = getTestEnvironment()
                                .getUsers()
                                .stream()
                                .collect(Collectors.toMap(EnvironmentsSetup.User::getRole, item -> item, (lhs, rhs) -> lhs));
                    } catch (Exception ex) {
                        cachedUsers = new HashMap<>();
                    }
                }
            }
        }

        return cachedUsers;
    }

    public boolean isAlwaysInstallDrivers() {
        return alwaysInstallDrivers;
    }

    public boolean isGlobalEncryption() {
        return globalEncryption;
    }

    public String getMailProxy() {
        return mailProxy;
    }

    public String getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(String httpProxy) {
        this.httpProxy = httpProxy;
    }

    public String getHttpsProxy() {
        return httpsProxy;
    }

    public void setHttpsProxy(String httpsProxy) {
        this.httpsProxy = httpsProxy;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public Platform getBrowserPlatform() {
        return platform;
    }

    public String getBrowserVersion() {
        return version;
    }

    public WebDriverTypeEnum getBrowserType() {
        return browserType;
    }

    public Capabilities getExtraCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (extraCapabilities != null) {
            String[] params = extraCapabilities.split(",");
            for (String param : params) {
                String[] values = param.split("=", 2);
                capabilities.setCapability(values[0].trim(), values[1].trim());
            }
        }

        return capabilities;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public boolean isProdEnv() {
        return environment.isProdEnv();
    }

    public String getEnvironmentTarget() {
        return environmentTarget;
    }

    private String replaceWithTargetEnv(String url) {
        if (url == null || environmentTarget == null) {
            return url;
        }

        return url.replaceAll("(?i)\\$\\{env}", environmentTarget);
    }

    public String getApiUrl() {
        String value = (isProdEnv()) ? apiUrlProd : replaceWithTargetEnv(apiUrl);
        if (value == null) {
            value = getEnvironmentCustom("apiUrl", false);
        }

        return value;
    }

    public Credentials getApiCredentials() {
        if (isProdEnv()) {
            return apiCredentialsProd;
        } else {
            return apiCredentials;
        }
    }

    public String getCiHttpProxy() {
        return ciHttpProxy;
    }

    public String getCiHttpsProxy() {
        return ciHttpsProxy;
    }

    public CreditCard[] getCreditCards() {
        return appCreditCards;
    }

    public DynamicCredentials[] getDynamicCredentials() {
        return dynamicCredentials;
    }

    public Credentials[] getAppCredentials() {
        Credentials[] credentials;
        if (isProdEnv()) {
            credentials = appCredentialsProd;
        } else {
            credentials = appCredentials;
        }

        return credentials;
    }

    private String fromArray(Object array) {
        StringBuilder sb = new StringBuilder();
        for (Object value : (Object[]) array) {
            sb.append(value.toString());
            sb.append(", ");
        }

        return StringUtils.removeEnd(sb.toString(), ", ");
    }

    public List<Parameter> getAsParameters() {
        List<Parameter> params = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Property.class) && !field.isAnnotationPresent(HideInReport.class)) {
                String property = field.getAnnotation(Property.class).value();
                String value;
                try {
                    if (field.getType().isArray()) {
                        value = fromArray(field.get(this));
                    } else {
                        value = field.get(this).toString();
                    }
                } catch (Exception e) {
                    value = "";
                }

                if (StringUtils.isNotBlank(value)) {
                    params.add(new Parameter().withKey(property).withName(property).withValue(value));
                }
            }
        }

        return params;
    }

    public boolean isShowReport() {
        return showReport;
    }

    public File getReportFolder() {
        return reportFolder;
    }

    public int getReportPort() {
        return reportPort;
    }

    public String getScreenSize() {
        return screenSize;
    }

    /**
     * Get the page load timeout in minutes<BR>
     * <B>Notes:</B> Sometimes resources (or requests) may take a long time (or never load.)  In these cases,
     * the page load timeout can be applied to how long WebDriver waits for the page to indicate it is complete
     * after various actions (like get) to prevent very long or infinite page load times.<BR>
     *
     * @return the number of minutes to allow the page to be loaded before an exception will be thrown
     */
    public int getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public int getPageTimeout() {
        return page_timeout;
    }

    public int getElementTimeout() {
        return element_timeout;
    }

    public int getNegativeTimeout() {
        return negative_timeout;
    }

    @SuppressWarnings("squid:S00112")
    public List<String> getSuites() {
        List<String> suitesList = new ArrayList<>();
        if (this.suites != null) {
            String[] theSuites = this.suites.split(",");
            for (String s : theSuites) {
                suitesList.add(s.trim().replace("\"", "").replace("\'", ""));
                if (isProdEnv() && !s.toLowerCase().contains("prod.xml")) {
                    throw new RuntimeException("The suite file name should end with 'PROD' to be able to run on PRODUCTION ENVIRONMENT!");
                }
            }
        }

        return suitesList;
    }

    public int getTestDefaultRetry() {
        return testDefaultRetry;
    }

    public String getDbHost() {
        return dbHost;
    }

    public int getDbPort() {
        return dbPort;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public String getDbPassword() {
        // Field is encrypted and classes that use decrypt as such leaving encrypted
        return getFieldWithFallBack(dbPassword, false, "dbPassword", false);
    }

    public boolean isDbIntegratedSecurity() {
        return dbIntegratedSecurity;
    }

    public String getDbName() {
        return dbName;
    }

    public int getDbTimeout() {
        return dbTimeout;
    }

    public int getDbQueryTimeout() {
        return dbQueryTimeout;
    }

    public int getApiTimeout() {
        return apiTimeout;
    }

    public String getSshHost() {
        if (isProdEnv()) {
            return sshHostProd;
        } else {
            return sshHost;
        }
    }

    public int getSshPort() {
        return sshPort;
    }

    public String getSshUser() {
        return sshUser;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public int getSshTimeout() {
        return sshTimeout;
    }

    public String getURL() {
        String value = (isProdEnv()) ? urlProd : replaceWithTargetEnv(url);
        if (value == null && getTestEnvironment() != null) {
            value = getTestEnvironment().getUrl();
        }

        return value;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public String getFirefoxBin() {
        return firefoxBin;
    }

    public boolean noMarionette() {
        return useNoMarionette;
    }

    @SuppressWarnings("squid:S00100")
    public String getFirefoxNTLM_URIS() {
        if (firefox_NTLM_Auto) {
            List<String> uris = new ArrayList<>();

            if (StringUtils.isNotBlank(getURL())) {
                uris.add(getURL());
            }

            StringBuilder sb = new StringBuilder();
            for (String uri : uris) {
                sb.append(URLUtils.getURI(StringUtils.trim(uri)).getHost());
                sb.append(",");
            }

            return StringUtils.removeEnd(sb.toString(), ",");
        }

        return firefox_NTLM_URIS;
    }

    public boolean isViewPortOnly() {
        return viewPortOnly;
    }

    /**
     * Get the Source (folder) for JavaScript files
     *
     * @return JavaScript folder (ends with single /)
     */
    public String getSourceJS() {
        return StringUtils.removeEnd(StringUtils.defaultString(sourceJS), "/") + "/";
    }

    /**
     * Get the Source (folder) for SQL files
     *
     * @return SQL folder (ends with single /)
     */
    public String getSourceSQL() {
        return StringUtils.removeEnd(StringUtils.defaultString(sourceSQL), "/") + "/";
    }

    public boolean isDocumentationMode() {
        return documentationMode;
    }

    public String getMailServer() {
        return getFieldWithFallBack(mailServer, true, "mailServer", false);
    }

    public int getMailServerPort() {
        return getFieldWithFallBack(mailServerPort, 0, "mailServerPort", false);
    }

    public String getMailPassword() {
        // Field is encrypted and classes that use decrypt as such leaving encrypted
        return getFieldWithFallBack(mailPassword, false, "mailPassword", false);
    }

    public int getMailTimeout() {
        return mailTimeout;
    }

    public boolean isAxeOn() {
        return axeOn;
    }

    public boolean isAxeViolationsOnlyLog() {
        return axeViolationsOnlyLog;
    }

    public boolean isAxeLoggingDelayed() {
        return axeLoggingDelayed;
    }

    public String getAutomationKey() {
        return automationKey;
    }

    public String getConsulHost() {
        return consulHost;
    }

    public int getConsulPort() {
        return consulPort;
    }

    public boolean isBrowserMobProxy() {
        return useBrowserMobProxy;
    }

    private BrowserMobProxy getBrowserMobProxyForThread() {
        Long threadId = Thread.currentThread().getId();
        return browserMobProxies.computeIfAbsent(threadId, k -> new BrowserMobProxyServer());
    }

    private void removeBrowserMobProxyForThread() {
        browserMobProxies.remove(Thread.currentThread().getId());
    }

    public Proxy getSeleniumProxyForThread() {
        return ClientUtil.createSeleniumProxy(getBrowserMobProxyForThread());
    }

    /**
     * Start the browser mob proxy if necessary for the thread
     */
    public void startBrowserMobProxyForThread() {
        if (isBrowserMobProxy() && !getBrowserMobProxyForThread().isStarted()) {
            BrowserMobProxy browserMobProxy = getBrowserMobProxyForThread();
            browserMobProxy.setTrustAllServers(true);
            browserMobProxy.start();
            browserMobProxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT);
            browserMobProxy.newHar("Thread-" + Thread.currentThread().getId() + "-" + System.currentTimeMillis());
        }
    }

    /**
     * Stop the browser mob proxy if necessary for the thread
     */
    public void stopBrowserMobProxyForThread() {
        if (isBrowserMobProxy() && getBrowserMobProxyForThread().isStarted()) {
            BrowserMobProxy browserMobProxy = getBrowserMobProxyForThread();
            browserMobProxy.stop();
            browserMobProxy.endHar();
            removeBrowserMobProxyForThread();
        }
    }

    public Har getHarForThread() {
        return getBrowserMobProxyForThread().getHar();
    }

    /**
     * Writes the Browser Mob Proxy Log to file if necessary for thread
     *
     * @param filename - File to write to
     */
    @SuppressWarnings("squid:S00112")
    public void performWriteBrowserMobProxyLogToFile(String filename) {
        if (isBrowserMobProxy() && writeBrowserMobProxyLogToFile) {
            try {
                Har har = getHarForThread();
                FileOutputStream fos = new FileOutputStream(filename);
                har.writeTo(fos);
            } catch (Exception ex) {
                String error = "Unable to write browser mob proxy log to file ("
                        + filename
                        + ") due to exception:  "
                        + ex.getMessage();
                throw new RuntimeException(error);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Property.class) && !field.isAnnotationPresent(HideInReport.class)) {
                String prop = field.getAnnotation(Property.class).value();
                String value = null;
                try {
                    value = field.get(this).toString();
                } catch (Exception e) {
                    // do nothing
                }

                str.append(prop).append(" = ").append(value).append("\n");
            }
        }

        return str.toString();
    }

}
