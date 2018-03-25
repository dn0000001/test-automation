package com.taf.automation.api.clients;

import com.taf.automation.api.rest.GenericHttpResponse;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicResponse implements GenericHttpResponse {
    private StatusLine status;
    private String entity;
    private Map<String, List<String>> headers;

    public BasicResponse(StatusLine status, String entity, Map<String, List<String>> headers) {
        this.status = status;
        this.entity = entity;
        this.headers = headers;
    }

    @Override
    public StatusLine getStatus() {
        return status;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public String getEntityAsString() {
        return entity;
    }

    @Override
    public Header[] getAllHeaders() {
        List<Header> hList = new ArrayList<>();
        for (String header : this.headers.keySet()) {
            String name = (header == null) ? "null" : header;
            Header h = new BasicHeader(name, this.headers.get(header).toString());
            hList.add(h);
        }

        return hList.toArray(new Header[hList.size()]);
    }

    @Override
    public Header getHeader(String name) {
        List<String> value = this.headers.get(name);
        if (value == null) {
            return null;
        }

        return new BasicHeader(name, value.toString());
    }


    @Override
    public String toString() {
        return "BasicResponse{" +
                "\n status=" + status +
                "\n ,entity=" + entity +
                "\n ,headers=" + headers +
                "\n}";
    }

}
