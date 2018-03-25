package com.taf.automation.api.clients;

import com.taf.automation.api.ApiUtils;
import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicStatusLine;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

public class BasicHTTPSRequest {
    private URL url;
    private SSLContext sc = null;
    private String method;
    private List<Header> headers;

    public BasicHTTPSRequest(String method, URL url, String supportedTLSVer, List<Header> headers) {
        this.method = method.toUpperCase();
        this.url = url;
        this.headers = headers;
        if (supportedTLSVer != null && !supportedTLSVer.isEmpty()) {
            try {
                sc = SSLContext.getInstance(supportedTLSVer);
                sc.init(null, null, new SecureRandom());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private HttpsURLConnection getConnection() {
        String httpsProxy = TestProperties.getInstance().getCiHttpsProxy();
        if (httpsProxy != null) {
            String[] proxy = httpsProxy.split(":");
            System.setProperty("https.proxyHost", proxy[0]);
            System.setProperty("https.proxyPort", proxy[1]);
        }

        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            if (sc != null) {
                connection.setSSLSocketFactory(sc.getSocketFactory());
            }

            connection.setRequestMethod(method.toUpperCase());
            if (headers != null && !headers.isEmpty()) {
                for (Header header : headers) {
                    connection.setRequestProperty(header.getName(), header.getValue());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return connection;
    }

    public BasicResponse execute(String entity) {
        HttpsURLConnection connection = getConnection();
        if (entity != null) {
            connection.setDoOutput(true);
            try {
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(entity);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        StatusLine statusLine;
        BasicResponse response;
        String sEntity = null;
        Map<String, List<String>> resHeaders;

        try {
            statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), connection.getResponseCode(), connection.getResponseMessage());
            InputStream is;
            if (statusLine.getStatusCode() >= 400) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }

            if (is != null) {
                sEntity = IOUtils.toString(is, Charset.defaultCharset());
            }

            if (sEntity != null && sEntity.startsWith("<?xml")) {
                sEntity = ApiUtils.prettifyXML(sEntity);
            }

            resHeaders = connection.getHeaderFields();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response = new BasicResponse(statusLine, sEntity, resHeaders);
        return response;
    }

}
