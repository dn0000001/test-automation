package com.taf.automation.api.clients;

import com.taf.automation.api.ParametersType;
import com.taf.automation.api.ReturnType;
import com.taf.automation.ui.support.TestProperties;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Wrapper class to provide a CloseableHttpClient for use in the FileDownloader class without exposing it to all classes
 */
public class FileDownloaderClient {
    private static final String URL = "https://www.google.com"; // Any valid url
    private static final int TIMEOUT = TestProperties.getInstance().getApiTimeout();
    private final ApiClient apiClient;

    public FileDownloaderClient() {
        apiClient = new ApiClient(ParametersType.GENERAL, ReturnType.GENERAL, URL, null, null, TIMEOUT, TIMEOUT);
    }

    public CloseableHttpClient getClient() {
        return apiClient.getClient();
    }

}
