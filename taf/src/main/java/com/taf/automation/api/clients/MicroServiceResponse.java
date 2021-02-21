package com.taf.automation.api.clients;

import com.taf.automation.api.JsonUtils;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.api.rest.JsonBaseError;
import com.taf.automation.api.rest.JsonError;
import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicroServiceResponse<T> implements GenericHttpResponse<T> {
    private static final Logger LOG = LoggerFactory.getLogger(MicroServiceResponse.class);
    private StatusLine status;
    private String rawJSON;
    private T entity;
    private Header[] headers;
    private JsonBaseError error;

    @SuppressWarnings("squid:S00112")
    public MicroServiceResponse(CloseableHttpResponse response, Class<T> responseEntity) {
        status = response.getStatusLine();
        headers = response.getAllHeaders();

        if (response.getEntity() == null) {
            return;
        }

        try {
            rawJSON = EntityUtils.toString(response.getEntity());
            JsonUtils.attachJsonToReport(rawJSON, "RESPONSE");
            if (responseEntity != null) {
                if (status.getStatusCode() < 400) {
                    entity = getEntityFromJSON(responseEntity, rawJSON);
                } else {
                    if (StringUtils.startsWithIgnoreCase(rawJSON, "<html>")) {
                        error = new JsonError();
                        error.setError(rawJSON);
                    } else {
                        error = getEntityFromJSON(JsonError.class, rawJSON);
                    }
                }
            }
        } catch (Exception e) {
            if (TestProperties.getInstance().isDebugLogging()) {
                LOG.warn(e.getMessage());
            }

            throw new RuntimeException(e);
        }
    }

    /**
     * Convert the raw JSON to an entity
     *
     * @param responseEntity - class type
     * @param rawJSON        - Raw JSON
     * @param <P>            - same as response entity type
     * @return entity
     */
    private <P> P getEntityFromJSON(Class<P> responseEntity, String rawJSON) {
        return JsonUtils.getGson().fromJson(rawJSON, responseEntity);
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
        return rawJSON;
    }

    @Override
    public Header[] getAllHeaders() {
        return headers;
    }

    @Override
    public Header getHeader(String name) {
        for (Header header : headers) {
            if (header.getName().equals(name)) {
                return header;
            }
        }

        return null;
    }

    /**
     * Get Error
     *
     * @return null if no error, else JsonBaseError
     */
    public JsonBaseError getError() {
        return error;
    }

}
