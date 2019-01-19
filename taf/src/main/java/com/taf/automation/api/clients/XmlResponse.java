package com.taf.automation.api.clients;

import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.api.rest.XmlBaseError;
import com.taf.automation.api.rest.XmlError;
import com.thoughtworks.xstream.XStream;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

/**
 * XML Response
 *
 * @param <T>
 */
@SuppressWarnings("squid:S00112")
public class XmlResponse<T> implements GenericHttpResponse<T> {
    private StatusLine status;
    private String entityXML;
    private T entity;
    private Header[] headers;
    private XmlBaseError apiError;
    private XStream xstream;

    /**
     * Constructor for XML Response
     *
     * @param response       - Response
     * @param responseEntity - Response Entity
     */
    public XmlResponse(CloseableHttpResponse response, Class<T> responseEntity) {
        this(response, responseEntity, null);
    }

    /**
     * Constructor for XML Response
     *
     * @param response          - Response
     * @param responseEntity    - Response Entity
     * @param customizedXstream - XStream
     */
    public XmlResponse(CloseableHttpResponse response, Class<T> responseEntity, XStream customizedXstream) {
        xstream = customizedXstream;
        status = response.getStatusLine();
        headers = response.getAllHeaders();

        if (response.getEntity() == null) {
            return;
        }

        try {
            entityXML = ApiUtils.prettifyXML(EntityUtils.toString(response.getEntity()));
            ApiUtils.attachDataXml(entityXML, "RESPONSE");
            if (responseEntity != null) {
                if (status.getStatusCode() < 400) {
                    entity = getEntityFromXml(responseEntity, entityXML);
                } else {
                    apiError = getEntityFromXml(XmlError.class, entityXML);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "squid:S1172"})
    private <T> T getEntityFromXml(Class<T> responseEntity, String entityXML) {
        return (T) getXstream().fromXML(entityXML);
    }

    @Override
    public XStream getXstream() {
        if (xstream == null) {
            xstream = new XStream();
        }

        return xstream;
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
        return entityXML;
    }

    @Override
    public Header[] getAllHeaders() {
        return headers;
    }

    public XmlBaseError getErrorEntity() {
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
