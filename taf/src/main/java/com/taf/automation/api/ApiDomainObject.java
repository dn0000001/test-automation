package com.taf.automation.api;

import com.taf.automation.api.clients.ApiClient;
import com.taf.automation.api.clients.ApiLoginSession;
import com.taf.automation.api.clients.UserLogin;
import com.taf.automation.api.converters.BasicHeaderConverter;
import com.taf.automation.api.converters.EnumConverter;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.ui.support.Credentials;
import com.taf.automation.ui.support.CreditCard;
import com.taf.automation.ui.support.DataPersistenceV2;
import com.taf.automation.ui.support.Environment;
import com.taf.automation.ui.support.EnvironmentType;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.Data;
import datainstiller.data.DataAliases;
import datainstiller.data.DataGenerator;
import datainstiller.data.DataPersistence;
import datainstiller.data.DataValueConverter;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The API Domain Object from which other API domain objects should be extended from
 */
public class ApiDomainObject extends DataPersistenceV2 {
    @Data(alias = "user-email")
    protected String loginUser;
    protected String loginPassword;
    protected List<BasicHeader> headers;

    @XStreamOmitField
    private ApiClient client;

    @XStreamOmitField
    private ApiLoginSession loginSession;

    @XStreamOmitField
    private DataAliases dataAliases;

    @Override
    public String toXML() {
        createGlobalAliasses();
        String xml = super.toXML().replace(" xmlns=\"xmlns\" xmlns:xsi=\"xsi\" xsi:schemaLocation=\"schemaLocation\"", "");

        for (Map.Entry<String, String> entry : dataAliases.entrySet()) {
            String alias = "${" + entry.getKey() + "}";
            String value = entry.getValue();
            xml = xml.replace(alias, value);
        }

        return xml;
    }

    @Override
    public <T extends DataPersistence> T fromResource(String resourceFile) {
        T dataSet = super.fromResource(resourceFile, true);
        Utils.attachDataSet(dataSet, resourceFile);
        return dataSet;
    }

    private void createGlobalAliasses() {
        dataAliases = new DataAliases();
        TestProperties props = TestProperties.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        dataAliases.put("today", sdf.format(today));
        int days = 10;
        for (int i = 1; i <= (days); i++) {
            dataAliases.put("today_plus_" + i, sdf.format(DateUtils.addDays(today, i)));
            dataAliases.put("today_minus_" + i, sdf.format(DateUtils.addDays(today, -i)));
        }

        Credentials[] credentials = props.getAppCredentials();
        for (int i = 0; i < credentials.length; i++) {
            dataAliases.put("email_" + (i + 1), credentials[i].getEmailOrName());
            dataAliases.put("password_" + (i + 1), credentials[i].getPassword());
        }

        CreditCard[] creditCards = props.getCreditCards();
        for (int i = 0; i < creditCards.length; i++) {
            String num = (i > 0) ? "_" + i + 1 : "";
            dataAliases.put("card_number" + num, creditCards[i].getNumber());
            dataAliases.put("card_month" + num, creditCards[i].getMonth());
            dataAliases.put("card_year" + num, creditCards[i].getYear());
            dataAliases.put("card_code" + num, creditCards[i].getCode());
            dataAliases.put("card_name" + num, creditCards[i].getCardHolder());
        }
    }

    protected String getLoginUser() {
        return loginUser;
    }

    protected ApiClient getClient() {
        if (client == null) {
            client = new ApiClient();
        }

        return client;
    }

    protected ApiLoginSession getLoginSession() {
        if (loginSession == null) {
            loginSession = new ApiLoginSession();
            if (headers != null) {
                loginSession.addHeaders(headers);
            }
        }

        return loginSession;
    }

    public void addHeaders(List<BasicHeader> headers) {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }

        this.headers.addAll(headers);
    }

    public void addHeader(BasicHeader header) {
        if (headers == null) {
            headers = new ArrayList<>();
        }

        this.headers.add(header);
    }

    /**
     * Get the headers<BR>
     * <B>Note: </B>Any modifications to this list will <B>NOT</B> affect the headers in this class
     *
     * @return new list of headers
     */
    protected List<Header> getHeaders() {
        if (headers == null) {
            headers = new ArrayList<>();
        }

        // This is returning a deep copy of the headers (based on testing.)
        return headers.stream().collect(Collectors.toList());
    }

    protected GenericHttpResponse<UserLogin> apiLogin() {
        getLoginSession();
        return loginSession.login(loginUser, loginPassword);
    }

    private List<DataValueConverter> setUpConverters() {
        List<DataValueConverter> converters = new ArrayList<>();

        converters.add(new BasicHeaderConverter());

        //
        // Add application specific converters here
        //

        converters.add(new EnumConverter(EnvironmentType.class, Environment.QA));

        return converters;
    }

    @Override
    public XStream getXstream() {
        XStream xstream = super.getXstream();
        for (DataValueConverter converter : setUpConverters()) {
            xstream.alias("header", BasicHeader.class);
            xstream.registerConverter(converter);
        }

        return xstream;
    }

    @Override
    public String generateXML() {
        DataGenerator generator = new DataGenerator(setUpConverters());
        DataPersistence obj = generator.generate(this.getClass());
        return (obj.toXML());
    }

    /**
     * Get the Global Aliases<BR>
     * <BR>
     * <B>Note:</B> The super method getDataAliases cannot be used as it always returns null after
     * initialization<BR>
     *
     * @return Global Aliases
     */
    public DataAliases getGlobalAliases() {
        return dataAliases;
    }

    @SuppressWarnings("squid:S106")
    @Test
    public void gen() {
        System.out.println(generateXML());
    }

}
