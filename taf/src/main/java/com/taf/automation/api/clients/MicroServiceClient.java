package com.taf.automation.api.clients;

import com.google.gson.Gson;
import com.taf.automation.api.JsonUtils;
import com.taf.automation.api.rest.GenericHttpInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.net.URI;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Client which does not initialize the target host for sending requests instead you must you the setRequester method
 * to set this information.
 */
@SuppressWarnings("squid:S00112")
public class MicroServiceClient implements GenericHttpInterface {
    private HttpRequester requester;
    private String customAcceptHeader;
    private String customContentType;

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

    public void setRequester(HttpRequester requester) {
        this.requester = requester;
    }

    /**
     * Set Custom Accept Header<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>Use <B>null</B> to set Accept header as normal</LI>
     * <LI>The Accept header should only be overridden if the micro service requires a custom value</LI>
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
     * <LI>The Content-Type header should only be overridden if the default of "application/json" is not correct (or
     * not being set based on entity type)</LI>
     * </OL>
     *
     * @param customContentType - value to be set
     */
    public void setCustomContentType(String customContentType) {
        this.customContentType = customContentType;
    }

    private <T> MicroServiceResponse<T> executeRequest(HttpRequest request, Object entity, Class<T> responseEntity, List<Header> headers) {
        setRequestHeaders(request, headers);

        if (request instanceof HttpEntityEnclosingRequest && entity != null) {
            HttpEntityEnclosingRequest req = (HttpEntityEnclosingRequest) request;
            HttpEntity httpEntity = toHttpEntity(entity);
            if (!StringUtils.defaultString(customAcceptHeader).equals("")) {
                req.setHeader("Accept", customAcceptHeader);
            } else {
                req.setHeader("Accept", "application/json");
            }

            if (customContentType != null) {
                // When customContentType is the empty string, this indicates to not set the Content-Type header.
                // However, the Content-Type header may have already been set.
                if (!customContentType.equals("")) {
                    // Note:  If this is set to an invalid Content-Type, then the request may not be sent/received.
                    req.setHeader("Content-type", customContentType);
                }
            } else if (!(httpEntity instanceof UrlEncodedFormEntity)) {
                req.setHeader("Content-type", "application/json");
            }

            req.setEntity(httpEntity);
        }

        assertThat("HttpRequester was not initialized (via setRequester)", requester, notNullValue());
        CloseableHttpClient client = requester.getClient();
        HttpHost targetHost = requester.getTargetHost();
        HttpClientContext clientContext = requester.getClientContext();
        StatusLine status = null;
        MicroServiceResponse<T> microServiceResponse;
        try (CloseableHttpResponse response = client.execute(targetHost, request, clientContext)) {
            status = response.getStatusLine();
            microServiceResponse = new MicroServiceResponse<>(response, responseEntity);
        } catch (Exception e) {
            String statusLine = (status == null) ? "ERROR" : status.toString();
            throw new RuntimeException(statusLine + "; Exception:  " + e.getMessage(), e);
        }

        return microServiceResponse;
    }

    private void setRequestHeaders(HttpRequest request, List<Header> headers) {
        if (headers == null) {
            return;
        }

        for (Header header : headers) {
            request.setHeader(header);
        }
    }

    private HttpEntity toHttpEntity(Object entity) {
        HttpEntity httpEntity;
        if (HttpEntity.class.isAssignableFrom(entity.getClass())) {
            httpEntity = (HttpEntity) entity;
        } else {
            Gson gson = JsonUtils.getGson();
            String json = gson.toJson(entity);
            try {
                JsonUtils.attachJsonToReport(json, "MICRO SERVICE REQUEST ENTITY");
                httpEntity = new StringEntity(json);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return httpEntity;
    }

    @Override
    public <T> MicroServiceResponse<T> post(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpPost post = new HttpPost(resourcePath);
        return executeRequest(post, entity, responseEntityType, headers);
    }

    @Override
    public <T> MicroServiceResponse<T> get(String resourcePath, Class<T> responseEntityType, List<Header> headers) {
        HttpGet get = new HttpGet(resourcePath);
        return executeRequest(get, null, responseEntityType, headers);
    }

    @Override
    public <T> MicroServiceResponse<T> put(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpPut put = new HttpPut(resourcePath);
        return executeRequest(put, entity, responseEntityType, headers);
    }

    @Override
    public <T> MicroServiceResponse<T> delete(String resourcePath, Class<T> responseEntityType, List<Header> headers) {
        HttpDelete delete = new HttpDelete(resourcePath);
        return executeRequest(delete, null, responseEntityType, headers);
    }

    @Override
    public <T> MicroServiceResponse<T> delete(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpDeleteWithBody deleteWithBody = new HttpDeleteWithBody(resourcePath);
        return executeRequest(deleteWithBody, entity, responseEntityType, headers);
    }

    @Override
    public <T> MicroServiceResponse<T> patch(String resourcePath, Object entity, Class<T> responseEntityType, List<Header> headers) {
        HttpPatch patch = new HttpPatch(resourcePath);
        return executeRequest(patch, entity, responseEntityType, headers);
    }

}
