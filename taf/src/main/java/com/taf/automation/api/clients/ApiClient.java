package com.taf.automation.api.clients;

import com.google.gson.Gson;
import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.JsonUtils;
import com.taf.automation.api.ParametersType;
import com.taf.automation.api.ReturnType;
import com.taf.automation.api.TrustAllStrategy;
import com.taf.automation.api.rest.GenericHttpInterface;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.URLUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.util.List;

/**
 * API Client which initializes the target host for sending requests
 */
@SuppressWarnings("squid:S00112")
public class ApiClient implements GenericHttpInterface {
    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-type";
    private CloseableHttpClient client;
    private String basePath;
    private HttpHost targetHost;
    private HttpClientContext clientContext;
    private ParametersType parametersType;
    private ReturnType returnType;
    private String customAcceptHeader;
    private String customContentType;
    private XStream xstream;

    private class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }

    }

    /**
     * Constructor - Configures the variables to send requests
     */
    public ApiClient() {
        this(ParametersType.GENERAL, ReturnType.GENERAL);
    }

    /**
     * Constructor - Configures the variables to send requests
     *
     * @param parametersType - Parameters Type
     * @param returnType     - Return Type
     */
    public ApiClient(ParametersType parametersType, ReturnType returnType) {
        this(
                parametersType,
                returnType,
                TestProperties.getInstance().getApiUrl(),
                TestProperties.getInstance().getApiCredentials().getEmailOrName(),
                new CryptoUtils().decrypt(TestProperties.getInstance().getApiCredentials().getPassword()),
                TestProperties.getInstance().getApiTimeout(),
                TestProperties.getInstance().getApiTimeout()
        );
    }

    /**
     * Constructor - Configures the variables to send requests
     *
     * @param parametersType    - Parameters Type
     * @param returnType        - Return Type
     * @param url               - Base URL
     * @param user              - User for basic authentication, if null skips authentication
     * @param password          - Password for basic authentication, if null skips authentication
     * @param socketTimeout     - Socket Timeout in milliseconds
     * @param connectionTimeout - Connection Timeout in milliseconds
     */
    public ApiClient(
            ParametersType parametersType,
            ReturnType returnType,
            String url,
            String user,
            String password,
            int socketTimeout,
            int connectionTimeout
    ) {
        // The target host does not allow a path value.  So, we will extract the path from the url and store for later.
        URIBuilder uri = URLUtils.getURI(url);
        basePath = uri.getPath();
        targetHost = getTargetHost(StringUtils.removeEnd(url, uri.getPath()));
        client = buildClient(socketTimeout, connectionTimeout);
        clientContext = buildClientContext(targetHost, user, password);
        this.parametersType = parametersType;
        this.returnType = returnType;
    }

    private HttpHost getTargetHost(String url) {
        return HttpHost.create(url);
    }

    private CloseableHttpClient buildClient(int socketTimeout, int connectionTimeout) {
        TestProperties props = TestProperties.getInstance();
        HttpHost proxyHost = null;
        if (props.getCiHttpProxy() != null) {
            proxyHost = HttpHost.create(props.getCiHttpProxy());
        }

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

        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectionTimeout)
                .setProxy(proxyHost)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setSSLSocketFactory(sslConnectionFactory)
                .useSystemProperties()
                .build();
    }

    private HttpClientContext buildClientContext(HttpHost targetHost, String user, String password) {
        HttpClientContext httpClientContext = HttpClientContext.create();
        if (user == null || password == null) {
            return httpClientContext;
        }

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

        httpClientContext.setCredentialsProvider(credentialsProvider);
        httpClientContext.setAuthCache(authCache);
        return httpClientContext;
    }

    /**
     * Execute generic request
     *
     * @param request        - Request information
     * @param entity         - Request Entity to be sent
     * @param responseEntity - Response Entity type
     * @param headers        - Headers
     * @return ApiResponse
     */
    private <T> GenericHttpResponse<T> executeRequest(HttpRequest request, Object entity, Class<T> responseEntity, List<Header> headers) {
        setRequestHeaders(request, headers);

        if (request instanceof HttpEntityEnclosingRequest && entity != null) {
            HttpEntityEnclosingRequest req = (HttpEntityEnclosingRequest) request;
            HttpEntity httpEntity = toHttpEntity(entity);
            req.setEntity(httpEntity);
            setHttpEntityEnclosingRequestHeaders(req);
        }

        StatusLine status = null;
        GenericHttpResponse<T> apiResponse;
        try (CloseableHttpResponse response = client.execute(targetHost, request, clientContext)) {
            status = response.getStatusLine();
            if (returnType == ReturnType.JAXB) {
                apiResponse = new JaxbResponse<>(response, responseEntity);
            } else if (returnType == ReturnType.JSON) {
                apiResponse = new JsonResponse<>(response, responseEntity);
            } else if (returnType == ReturnType.XML) {
                apiResponse = new XmlResponse<>(response, responseEntity, getXstream());
            } else {
                apiResponse = new GenericResponse<>(response, responseEntity);
            }
        } catch (Exception e) {
            String statusLine = (status == null) ? "CONNECTION TIME OUT" : status.toString();
            throw new RuntimeException(statusLine + "; Exception:  " + e.getMessage(), e);
        }

        return apiResponse;
    }

    private void setRequestHeaders(HttpRequest request, List<Header> headers) {
        if (headers == null) {
            return;
        }

        for (Header header : headers) {
            request.setHeader(header);
        }
    }

    private void setHttpEntityEnclosingRequestHeaders(HttpEntityEnclosingRequest request) {
        if (!StringUtils.defaultString(customAcceptHeader).equals("")) {
            request.setHeader(ACCEPT, customAcceptHeader);
        } else if (parametersType == ParametersType.XML) {
            request.setHeader(ACCEPT, "application/xml");
        } else if (parametersType == ParametersType.JSON) {
            request.setHeader(ACCEPT, "application/json");
        }

        if (customContentType != null) {
            // When customContentType is the empty string, this indicates to not set the Content-Type header.
            // However, the Content-Type header may have already been set.
            if (!customContentType.equals("")) {
                // Note:  If this is set to an invalid Content-Type, then the request may not be sent/received.
                request.setHeader(CONTENT_TYPE, customContentType);
            }
        } else if (parametersType == ParametersType.XML) {
            request.setHeader(CONTENT_TYPE, "application/xml");
        } else if (parametersType == ParametersType.JSON) {
            request.setHeader(CONTENT_TYPE, "application/json");
        }
    }

    private HttpEntity toHttpEntity(Object entity) {
        HttpEntity httpEntity;
        if (HttpEntity.class.isAssignableFrom(entity.getClass())) {
            httpEntity = (HttpEntity) entity;
        } else {
            switch (parametersType) {
                case XML:
                    String xml = ApiUtils.prettifyXML(getXstream().toXML(entity));
                    try {
                        ApiUtils.attachDataXml(xml, "REQUEST ENTITY");
                        httpEntity = new StringEntity(xml);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case JSON:
                    Gson gson = JsonUtils.getGson();
                    String json = gson.toJson(entity);
                    try {
                        JsonUtils.attachJsonToReport(json, "REQUEST ENTITY");
                        httpEntity = new StringEntity(json);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    break;
                default:
                    throw new RuntimeException("Unsupported parametersType:  " + parametersType);
            }
        }

        return httpEntity;
    }

    /**
     * Set Parameters Type for the requests
     *
     * @param parametersType - Parameters Type
     */
    public void setParametersType(ParametersType parametersType) {
        this.parametersType = parametersType;
    }

    /**
     * Set Return Type for the requests
     *
     * @param returnType - Return Type
     */
    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    /**
     * Set Custom Accept Header<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>Use <B>null</B> to set Accept header as normal</LI>
     * <LI>The Accept header should only be overridden if the end point requires a custom value</LI>
     * </OL>
     *
     * @param customAcceptHeader - value to be set
     */
    public void setCustomAcceptHeader(String customAcceptHeader) {
        this.customAcceptHeader = customAcceptHeader;
    }

    /**
     * Set Custom Content-Type header<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>Use empty string to use the currently set Content-Type header which prevents it from being overridden</LI>
     * <LI>Use <B>null</B> to set Content-Type header as normal</LI>
     * <LI>The Content-Type header should only be overridden if the default is not correct (or not being set based on
     * entity type)</LI>
     * </OL>
     *
     * @param customContentType - value to be set
     */
    public void setCustomContentType(String customContentType) {
        this.customContentType = customContentType;
    }

    public XStream getXstream() {
        if (xstream == null) {
            xstream = new XStream();
        }

        return xstream;
    }

    public void setXstream(XStream xstream) {
        this.xstream = xstream;
    }

    public String getBasePath() {
        return basePath;
    }

    @Override
    public <T> GenericHttpResponse<T> post(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpPost post = new HttpPost(resourcePath);
        return executeRequest(post, entity, responseEntityType, headers);
    }

    @Override
    public <T> GenericHttpResponse<T> get(String resourcePath, Class<T> responseEntityType, List<Header> headers) {
        HttpGet get = new HttpGet(resourcePath);
        return executeRequest(get, null, responseEntityType, headers);
    }

    @Override
    public <T> GenericHttpResponse<T> put(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpPut put = new HttpPut(resourcePath);
        return executeRequest(put, entity, responseEntityType, headers);
    }

    @Override
    public <T> GenericHttpResponse<T> delete(String resourcePath, Class<T> responseEntityType, List<Header> headers) {
        HttpDelete delete = new HttpDelete(resourcePath);
        return executeRequest(delete, null, responseEntityType, headers);
    }

    @Override
    public <T> GenericHttpResponse<T> patch(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpPatch patch = new HttpPatch(resourcePath);
        return executeRequest(patch, entity, responseEntityType, headers);
    }

    @Override
    public <T> GenericHttpResponse<T> delete(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpDeleteWithBody deleteWithBody = new HttpDeleteWithBody(resourcePath);
        return executeRequest(deleteWithBody, entity, responseEntityType, headers);
    }

}
