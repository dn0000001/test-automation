package com.taf.automation.api.clients;

import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.api.rest.XmlBaseError;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * JAXB Response
 *
 * @param <T>
 */
public class JaxbResponse<T> implements GenericHttpResponse<T> {
    private StatusLine status;
    private String entityXML;
    private T entity;
    private Header[] headers;
    private XmlBaseError apiError;

    /**
     * Constructor for JAXB Response
     *
     * @param response       - Response
     * @param responseEntity - Response Entity
     */
    public JaxbResponse(CloseableHttpResponse response, Class<T> responseEntity) {
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
                    entity = getEntityFromXml(responseEntity.getPackage().getName(), entityXML);
                } else {
                    apiError = getEntityFromXml(XmlBaseError.class.getPackage().getName(), entityXML);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <P> P getEntityFromXml(String entityName, String entityXML) {
        JAXBElement<P> element;
        try {
            JAXBContext context = JAXBContext.newInstance(entityName);
            Unmarshaller u = context.createUnmarshaller();
            StringReader stringReader = new StringReader(entityXML);
            element = (JAXBElement<P>) u.unmarshal(stringReader);
        } catch (JAXBException e) {
            return null;
        }

        return element.getValue();
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
