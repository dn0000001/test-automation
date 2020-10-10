package com.taf.automation.api.html;

import com.taf.automation.api.TrustAllStrategy;
import com.taf.automation.ui.support.TestProperties;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class to work with HTML page directly
 */
public class HtmlUtils {
    private Document doc;
    private int timeout;
    private SSLSocketFactory sslSocketFactory;

    public HtmlUtils() {
        doc = null;
        setTimeout((int) TimeUnit.SECONDS.toMillis(TestProperties.getInstance().getElementTimeout()));
        setProxy();
        setSslSocketFactory(getDefaultSocketFactory());
    }

    public HtmlUtils(String url) {
        setTimeout((int) TimeUnit.SECONDS.toMillis(TestProperties.getInstance().getElementTimeout()));
        setProxy();
        setSslSocketFactory(getDefaultSocketFactory());
        navigate(url);
    }

    public void setTimeout(int timeoutInMilliSeconds) {
        timeout = timeoutInMilliSeconds;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    @SuppressWarnings("java:S112")
    private SSLSocketFactory getDefaultSocketFactory() {
        SSLContext sslContext;

        try {
            TrustStrategy trustStrategy;
            if (TestProperties.getInstance().getEnvironment().isProdEnv()) {
                trustStrategy = new TrustSelfSignedStrategy();
            } else {
                trustStrategy = new TrustAllStrategy();
            }

            sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sslContext.getSocketFactory();
    }

    private Connection getConnection(String url) {
        return Jsoup.connect(url).sslSocketFactory(sslSocketFactory).ignoreContentType(true).timeout(timeout);
    }

    @SuppressWarnings("java:S112")
    public Connection.Response navigate(String url, String userAgent) {
        try {
            Connection.Response response = getConnection(url)
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .userAgent(userAgent)
                    .execute();
            doc = response.parse();
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112")
    public void navigate(String url) {
        try {
            doc = getConnection(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Element findElementById(String id) {
        return doc.getElementById(id);
    }

    public Elements findElementsByAttribute(String attribute) {
        return doc.getElementsByAttribute(attribute);
    }

    public Element findElementByAttribute(String attribute) {
        return findElementsByAttribute(attribute).first();
    }

    public Elements findElementsByCss(String cssSelector) {
        return doc.select(cssSelector);
    }

    public Element findElementByCss(String cssSelector) {
        return findElementsByCss(cssSelector).first();
    }

    public static String getElementText(String url, String cssSelector) {
        HtmlUtils htmlUtils = new HtmlUtils(url);
        return htmlUtils.findElementByCss(cssSelector).text();
    }

    private void setProxy() {
        TestProperties props = TestProperties.getInstance();
        String proxyHttp = props.getCiHttpProxy();
        if (proxyHttp != null) {
            String[] proxyProps = proxyHttp.split(":");
            System.setProperty("http.proxyHost", proxyProps[0]);
            System.setProperty("http.proxyPort", proxyProps[1]);
        }

        String proxyHttps = props.getCiHttpsProxy();
        if (proxyHttps != null) {
            String[] proxyProps = proxyHttps.split(":");
            System.setProperty("https.proxyHost", proxyProps[0]);
            System.setProperty("https.proxyPort", proxyProps[1]);
        }
    }

    @SuppressWarnings("java:S112")
    public static Map<String, String> getQueryParams(String url) {
        Map<String, String> qValues = new HashMap<>();
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        qValues.put("host", uri.getHost());
        List<NameValuePair> params = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
        for (NameValuePair param : params) {
            qValues.put(param.getName(), param.getValue());
        }

        return qValues;
    }

    /**
     * Execute the URL as a POST request
     *
     * @param url             - URL
     * @param followRedirects - true if server redirects should be followed
     */
    @SuppressWarnings("java:S112")
    public void post(String url, boolean followRedirects) {
        try {
            doc = getConnection(url).followRedirects(followRedirects).post();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStoredHTML() {
        if (doc == null) {
            return null;
        }

        return doc.toString();
    }

}
