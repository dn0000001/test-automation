package com.taf.automation.ui.support;

import com.taf.automation.ui.support.converters.Credentials;
import com.taf.automation.ui.support.converters.CreditCard;
import com.taf.automation.ui.support.converters.DynamicCredentials;
import com.taf.automation.ui.support.util.CryptoUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import ui.auto.core.context.PageComponentContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestContext extends PageComponentContext {
    private TestProperties props = TestProperties.getInstance();
    private Documenter documenter;
    private Accessibility accessibility;

    public TestContext(WebDriver driver) {
        super(driver);
        setTimeouts();
        setDefaultAliases();
    }

    public TestContext() {
        this(null);
    }

    private void setDefaultAliases() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        setAlias("today", sdf.format(today));
        int days = 10;
        for (int i = 1; i <= (days); i++) {
            setAlias("today_plus_" + i, sdf.format(DateUtils.addDays(today, i)));
            setAlias("today_minus_" + i, sdf.format(DateUtils.addDays(today, -i)));
        }

        Credentials[] credentials = ObjectUtils.defaultIfNull(props.getAppCredentials(), new Credentials[0]);
        for (int i = 0; i < credentials.length; i++) {
            setAlias("email_" + (i + 1), credentials[i].getEmailOrName());
            setAlias("password_" + (i + 1), credentials[i].getPassword());
        }

        CreditCard[] creditCards = ObjectUtils.defaultIfNull(props.getCreditCards(), new CreditCard[0]);
        for (int i = 0; i < creditCards.length; i++) {
            String num = (i > 0) ? "_" + i + 1 : "";
            setAlias("card_number" + num, creditCards[i].getNumber());
            setAlias("card_month" + num, creditCards[i].getMonth());
            setAlias("card_year" + num, creditCards[i].getYear());
            setAlias("card_code" + num, creditCards[i].getCode());
            setAlias("card_name" + num, creditCards[i].getCardHolder());
        }

        DynamicCredentials[] dynamicCredentials = ObjectUtils.defaultIfNull(props.getDynamicCredentials(), new DynamicCredentials[0]);
        for (DynamicCredentials item : dynamicCredentials) {
            setAlias("user_" + item.getRole(), item.getUser());
            String password = (item.isDecrypt()) ? new CryptoUtils().decrypt(item.getPassword()) : item.getPassword();
            setAlias("password_" + item.getRole(), password);
        }
    }

    public TestProperties getProps() {
        return props;
    }

    public void setTestProperties(TestProperties props) {
        this.props = props;
    }

    public void init() {
        driver = props.getBrowserType().getNewWebDriver(props);
        if (props.getBrowserType().isAppiumDriver()) {
            // Appium (as of 1.14.0) does not support setting timeouts or window size
            return;
        }

        if (props.getPageLoadTimeout() > 0) {
            driver.manage().timeouts().pageLoadTimeout(props.getPageLoadTimeout(), TimeUnit.MINUTES);
        }

        String res = props.getScreenSize();
        if (res != null) {
            String[] resWH = res.toLowerCase().split("x");
            int width = Integer.parseInt(resWH[0].trim());
            int height = Integer.parseInt(resWH[1].trim());
            Dimension dim = new Dimension(width, height);
            driver.manage().window().setSize(dim);
        } else {
            driver.manage().window().maximize();
        }
    }

    public String getAlias(String key) {
        return getGlobalAliases().get(key);
    }

    public void setAlias(String key, String value) {
        getGlobalAliases().put(key, value);
    }

    private void setTimeouts() {
        if (props.getElementTimeout() > 0) {
            setAjaxTimeOut(props.getElementTimeout());
        }
        if (props.getPageTimeout() > 0) {
            setWaitForUrlTimeOut(props.getPageTimeout() * 1000);
        }
    }

    public Documenter getDocumenter() {
        if (documenter == null) {
            documenter = new Documenter();
            documenter.setDocumentationMode(props.isDocumentationMode());
        }

        return documenter;
    }

    public Accessibility getAccessibility() {
        if (accessibility == null) {
            accessibility = new Accessibility(driver);
            accessibility.setAxeOn(props.isAxeOn());
            accessibility.setAxeViolationsOnlyLog(props.isAxeViolationsOnlyLog());
            accessibility.setAxeLoggingDelayed(props.isAxeLoggingDelayed());
        }

        return accessibility;
    }

}
