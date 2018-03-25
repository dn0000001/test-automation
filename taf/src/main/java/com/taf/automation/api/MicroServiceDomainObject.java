package com.taf.automation.api;

import com.taf.automation.api.clients.HttpRequester;
import com.taf.automation.api.clients.MicroServiceClient;
import com.taf.automation.api.network.MultiSshSession;
import com.taf.automation.ui.support.DomainObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataPersistence;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("micro-service-client")
public class MicroServiceDomainObject extends DomainObject {
    private List<BasicHeader> headers;
    private String customAcceptHeader;
    private String customContentType;

    @XStreamOmitField
    private MultiSshSession session;

    @XStreamOmitField
    private HttpRequester requester;

    @XStreamOmitField
    private MicroServiceClient client;

    protected List<Header> getHeaders() {
        if (headers == null) {
            headers = new ArrayList<>();
        }

        return headers.stream().collect(Collectors.toList());
    }

    public void setSession(MultiSshSession session) {
        this.session = session;
    }

    private HttpRequester getRequester() {
        if (requester == null) {
            requester = new HttpRequester(session);
        }

        return requester;
    }

    /**
     * Set the Requester (for debugging purposes)
     *
     * @param requester - Requester to be used
     */
    protected void setRequester(HttpRequester requester) {
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
    protected void setCustomAcceptHeader(String customAcceptHeader) {
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
    protected void setCustomContentType(String customContentType) {
        this.customContentType = customContentType;
    }

    /**
     * Get client to execute requests for micro services
     *
     * @return MicroServiceClient
     */
    protected MicroServiceClient getClient() {
        if (client == null) {
            client = new MicroServiceClient();
            client.setRequester(getRequester());
            client.setCustomAcceptHeader(customAcceptHeader);
            client.setCustomContentType(customContentType);
        }

        return client;
    }

    @Override
    public <T extends DataPersistence> T fromResource(String resourceFilePath) {
        return fromResource(resourceFilePath, true);
    }

}
