package com.taf.automation.api.clients;

import com.google.gson.Gson;
import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.JsonUtils;
import com.taf.automation.api.ParametersType;
import com.taf.automation.api.ReturnType;
import com.taf.automation.api.rest.GenericHttpInterface;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.ui.support.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import com.thoughtworks.xstream.XStream;
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
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
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
public class ApiClient implements GenericHttpInterface {
    private CloseableHttpClient client;
    private HttpHost targetHost;
    private HttpClientContext clientContext;
    private ParametersType parametersType;
    private ReturnType returnType;

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
        targetHost = getTargetHost();
        client = buildClient();
        clientContext = buildClientContext(targetHost);
        this.parametersType = parametersType;
        this.returnType = returnType;
    }

    private HttpHost getTargetHost() {
        TestProperties props = TestProperties.getInstance();
        return HttpHost.create(props.getApiUrl());
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

        RequestConfig config = RequestConfig.custom() //
                .setSocketTimeout(props.getApiTimeout()) //
                .setConnectTimeout(props.getApiTimeout()) //
                .setProxy(proxyHost) //
                .build();

        return HttpClients.custom() //
                .setDefaultRequestConfig(config) //
                .setSSLSocketFactory(sslConnectionFactory) //
                .useSystemProperties() //
                .build();
    }

    private HttpClientContext buildClientContext(HttpHost targetHost) {
        //
        // If application does not require API Credentials, then this may need to be removed
        //
        TestProperties props = TestProperties.getInstance();
        String apiUser = props.getApiCredentials().getEmailOrName();
        String apiPassword = new CryptoUtils().decrypt(props.getApiCredentials().getPassword());
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(apiUser, apiPassword);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);

        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());

        HttpClientContext clientContext = HttpClientContext.create();
        clientContext.setCredentialsProvider(credentialsProvider);
        clientContext.setAuthCache(authCache);
        return clientContext;
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
        if (headers != null) {
            for (Header header : headers) {
                request.setHeader(header);
            }
        }

        if (request instanceof HttpEntityEnclosingRequest && entity != null) {
            HttpEntityEnclosingRequest req = (HttpEntityEnclosingRequest) request;
            HttpEntity httpEntity = toHttpEntity(entity);
            req.setEntity(httpEntity);
            if (parametersType == ParametersType.XML) {
                req.setHeader("Accept", "application/xml");
                req.setHeader("Content-type", "application/xml");
            } else if (parametersType == ParametersType.JSON) {
                req.setHeader("Accept", "application/json");
                req.setHeader("Content-type", "application/json");
            }
        }

        StatusLine status = null;
        GenericHttpResponse<T> apiResponse;
        try (CloseableHttpResponse response = client.execute(targetHost, request, clientContext)) {
            status = response.getStatusLine();
            if (returnType == ReturnType.XML) {
                apiResponse = new XmlResponse<T>(response, responseEntity);
            } else if (returnType == ReturnType.JSON) {
                apiResponse = new JsonResponse<T>(response, responseEntity);
            } else {
                apiResponse = new GenericResponse<T>(response, responseEntity);
            }
        } catch (Exception e) {
            String statusLine = (status == null) ? "CONNECTION TIME OUT" : status.toString();
            throw new RuntimeException(statusLine + "; Exception:  " + e.getMessage(), e);
        }

        return apiResponse;
    }

    private HttpEntity toHttpEntity(Object entity) {
        HttpEntity httpEntity;
        if (HttpEntity.class.isAssignableFrom(entity.getClass())) {
            httpEntity = (HttpEntity) entity;
        } else {
            switch (parametersType) {
                case XML:
                    XStream xstream = new XStream();
                    String xml = ApiUtils.prettifyXML(xstream.toXML(entity));
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

    public <T> GenericHttpResponse<T> delete(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpDeleteWithBody deleteWithBody = new HttpDeleteWithBody(resourcePath);
        return executeRequest(deleteWithBody, (HttpEntity) entity, responseEntityType, headers);
    }

}
