package com.taf.automation.api.clients;

import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.JsonUtils;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.api.rest.JsonBaseError;
import com.taf.automation.api.rest.JsonError;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * JSON Response
 *
 * @param <T>
 */
public class JsonResponse<T> implements GenericHttpResponse<T> {
    private StatusLine status;
    private String entityJSON;
    private T entity;
    private Header[] headers;
    private JsonBaseError apiError;

    @SuppressWarnings({"unchecked", "squid:S00112"})
    public JsonResponse(CloseableHttpResponse response, Class<T> responseEntity) {
        status = response.getStatusLine();
        headers = response.getAllHeaders();

        if (response.getEntity() == null) {
            return;
        }

        try {
            entityJSON = EntityUtils.toString(response.getEntity());
            ApiUtils.attachDataJson(entityJSON, "RESPONSE");
            if (responseEntity != null) {
                if (status.getStatusCode() < 400) {
                    entity = getEntityFromJson(responseEntity, entityJSON);
                } else {
                    apiError = getEntityFromJson((Class<T>) JsonError.class, entityJSON);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <P> P getEntityFromJson(Class<T> responseEntity, String entityJSON) {
        try {
            return (P) JsonUtils.getGson().fromJson(entityJSON, responseEntity);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public StatusLine getStatus() {
        return status;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public String getEntityAsString() {
        return entityJSON;
    }

    @Override
    public Header[] getAllHeaders() {
        return headers;
    }

    public JsonBaseError getErrorEntity() {
        return apiError;
    }

    public Header getHeader(String name) {
        for (Header header : headers) {
            if (header.getName().equals(name)) {
                return header;
            }
        }

        return null;
    }

}
