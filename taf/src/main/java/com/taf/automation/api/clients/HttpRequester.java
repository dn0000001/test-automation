package com.taf.automation.api.clients;

import com.taf.automation.api.network.MultiSshSession;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;

@SuppressWarnings("java:S3252")
public class HttpRequester {
    private CloseableHttpClient client;
    private HttpHost targetHost;
    private HttpClientContext clientContext;

    /**
     * Constructor
     *
     * @param session - Session that is connected already
     */
    public HttpRequester(MultiSshSession session) {
        AssertJUtil.assertThat(session).as("Session").isNotNull();
        AssertJUtil.assertThat(session.getAssignedPort())
                .as("Session was not connected as assigned port was less than 1")
                .isGreaterThan(0);
        targetHost = HttpHost.create(session.getLocalHost() + ":" + session.getAssignedPort());
        client = buildClient();
        clientContext = buildClientContext(targetHost);
    }

    /**
     * Constructor
     *
     * @param url - URL
     */
    public HttpRequester(String url) {
        targetHost = HttpHost.create(url);
        client = buildClient();
        clientContext = buildClientContext(targetHost);
    }

    private CloseableHttpClient buildClient() {
        TestProperties props = TestProperties.getInstance();
        HttpHost proxyHost = null;
        if (props.getCiHttpProxy() != null) {
            proxyHost = HttpHost.create(props.getCiHttpProxy());
        }

        SSLContext sslContext;

        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(props.getApiTimeout())
                .setConnectTimeout(props.getApiTimeout())
                .setProxy(proxyHost)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setSSLSocketFactory(sslConnectionFactory)
                .useSystemProperties()
                .build();
    }

    private HttpClientContext buildClientContext(HttpHost targetHost) {
        HttpClientContext context = HttpClientContext.create();

        //
        // Add any configurations of the HttpClientContext as necessary here
        //

        return context;
    }

    public void setClientContext(HttpClientContext context) {
        clientContext = context;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public HttpHost getTargetHost() {
        return targetHost;
    }

    public HttpClientContext getClientContext() {
        return clientContext;
    }

}
