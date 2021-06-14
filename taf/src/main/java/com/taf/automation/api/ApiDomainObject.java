package com.taf.automation.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.taf.automation.api.clients.ApiClient;
import com.taf.automation.api.clients.ApiLoginSession;
import com.taf.automation.api.clients.UserLogin;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.locking.UserLockManager;
import com.taf.automation.ui.support.DataPersistenceV2;
import com.taf.automation.ui.support.Lookup;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.converters.Credentials;
import com.taf.automation.ui.support.converters.CreditCard;
import com.taf.automation.ui.support.converters.DynamicCredentials;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.util.DomainObjectUtils;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.Data;
import datainstiller.data.DataAliases;
import datainstiller.data.DataPersistence;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The API Domain Object from which other API domain objects should be extended from
 */
public class ApiDomainObject extends DataPersistenceV2 {
    /**
     * If tokens are tied to roles, then the role should be used as the key, otherwise pick something to make it unique
     * Notes:  You may want to change the expire timeout to be specific to the application being tested.
     */
    private static final LoadingCache<String, String> tokenCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {
                public String load(String key) {
                    return key;
                }
            });

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
    protected void initJexlContext(JexlContext jexlContext) {
        jexlContext.set("crypto", new CryptoUtils());
        jexlContext.set("userLockManager", UserLockManager.getInstance());
        jexlContext.set("lookup", Lookup.getInstance());
    }

    @Override
    public String toXML() {
        createGlobalAliasses();
        String xml = super.toXML().replace(" xmlns=\"xmlns\" xmlns:xsi=\"xsi\" xsi:schemaLocation=\"schemaLocation\"", "");

        for (String key : dataAliases.keySet()) {
            String alias = "${" + key + "}";
            String value = dataAliases.getAsString(key);
            xml = xml.replace(alias, value);
        }

        return xml;
    }

    @Override
    public <T extends DataPersistence> T fromResource(String resourceFile) {
        String useResourceFile = Helper.getEnvironmentBasedFile(resourceFile);

        // When resolve alias is true, then the aliases will be made null after loading
        // as such get the aliases before loading
        useAliasesXstreamFlag();
        T temp = super.fromResource(useResourceFile, false);
        DataAliases aliases = temp.getDataAliases();
        useNormalXstreamFlag();

        T dataSet = super.fromResource(useResourceFile, true);
        DomainObjectUtils.overwriteTestParameters(aliases);
        Utils.attachDataSet(dataSet, useResourceFile);
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

        Credentials[] credentials = ObjectUtils.defaultIfNull(props.getAppCredentials(), new Credentials[0]);
        for (int i = 0; i < credentials.length; i++) {
            dataAliases.put("email_" + (i + 1), credentials[i].getEmailOrName());
            dataAliases.put("password_" + (i + 1), credentials[i].getPassword());
        }

        CreditCard[] creditCards = ObjectUtils.defaultIfNull(props.getCreditCards(), new CreditCard[0]);
        for (int i = 0; i < creditCards.length; i++) {
            String num = (i > 0) ? "_" + i + 1 : "";
            dataAliases.put("card_number" + num, creditCards[i].getNumber());
            dataAliases.put("card_month" + num, creditCards[i].getMonth());
            dataAliases.put("card_year" + num, creditCards[i].getYear());
            dataAliases.put("card_code" + num, creditCards[i].getCode());
            dataAliases.put("card_name" + num, creditCards[i].getCardHolder());
        }

        DynamicCredentials[] dynamicCredentials = ObjectUtils.defaultIfNull(props.getDynamicCredentials(), new DynamicCredentials[0]);
        for (DynamicCredentials item : dynamicCredentials) {
            dataAliases.put("user_" + item.getRole(), item.getUser());
            String password = (item.isDecrypt()) ? new CryptoUtils().decrypt(item.getPassword()) : item.getPassword();
            dataAliases.put("password_" + item.getRole(), password);
        }
    }

    protected String getLoginUser() {
        return loginUser;
    }

    protected ApiClient getClient() {
        if (client == null) {
            client = new ApiClient();
            client.setRequestXStream(getXstream());
            client.setResponseXStream(getXstream());
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

    /**
     * This method will get the token from the cache if it exists otherwise it will use client to get the token
     *
     * @param client    - Client to get token if necessary and client will be updated with token
     * @param key       - Key to get token.  If tokens are tied to roles, then the role should be used as the key,
     *                  otherwise pick something to make it unique
     * @param userEmail - User/Email to get token.  (In a real application, this should be in the client)
     * @param password  - Password to get token.  (In a real application, this should be in the client)
     */
    protected static void cacheToken(ApiLoginSession client, String key, String userEmail, String password) {
        String token = tokenCache.getIfPresent(key);
        if (token == null) {
            synchronized (ApiDomainObject.class) {
                token = tokenCache.getIfPresent(key);
                if (token == null) {
                    // Example to request token.  In a real application, this would be customized in a specific method
                    token = client.login(userEmail, password).getEntity().getToken();
                    tokenCache.put(key, token);
                }
            }
        }

        client.setToken(token);
    }

    @SuppressWarnings("squid:S106")
    @Override
    @Test
    public void generate() {
        System.out.println(generateXML());
    }

    /**
     * Use the CSV data to set the variables in the domain object
     *
     * @param csvTestData - CSV test data
     */
    public void setData(CsvTestData csvTestData) {
        // Method should be overridden in extending classes
    }

}
