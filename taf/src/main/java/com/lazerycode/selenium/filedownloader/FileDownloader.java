package com.lazerycode.selenium.filedownloader;

import com.taf.automation.api.clients.FileDownloaderClient;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.DownloadUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The original code came from <a href="https://github.com/Ardesco/Powder-Monkey">https://github.com/Ardesco/Powder-Monkey</a>
 * which was found on the <a href="https://ardesco.lazerycode.com/testing/webdriver/2012/07/25/how-to-download-files-with-selenium-and-why-you-shouldnt.html">blog by Mark Collin</a>
 * <BR>
 * Some modifications were made to remove use of deprecated methods and throwing of exceptions
 */
@SuppressWarnings("java:S3252")
public class FileDownloader {
    private final WebDriver driver;
    private boolean followRedirects = true;
    private boolean mimicWebDriverCookieState = true;
    private int successfulStatusCodes = HttpStatus.SC_BAD_REQUEST;
    private RequestMethod httpRequestMethod = RequestMethod.GET;
    private final List<Header> headers = new ArrayList<>();
    private URI fileURI;
    private HttpEntity httpEntity;

    public FileDownloader(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Flag to indicate to follow re-directs
     *
     * @param followRedirects true to follow re-directs
     * @return FileDownloader
     */
    public FileDownloader withFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    /**
     * Mimic the cookie state of WebDriver (Defaults to true)
     * This will enable you to access files that are only available when logged in.
     * If set to false the connection will be made as an anonymous user
     *
     * @param mimicWebDriverCookieState true to mimic the cookie state of WebDriver
     * @return FileDownloader
     */
    public FileDownloader withMimicWebDriverCookieState(boolean mimicWebDriverCookieState) {
        this.mimicWebDriverCookieState = mimicWebDriverCookieState;
        return this;
    }

    /**
     * Set the HTTP Request Method
     *
     * @param requestType - Request Type
     * @return FileDownloader
     */
    public FileDownloader withHTTPRequestMethod(RequestMethod requestType) {
        httpRequestMethod = requestType;
        return this;
    }

    /**
     * Set the HttpEntity<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>
     * This is only applicable to certain request types such as post.
     * There is no logic to prevent use with invalid request types.
     * </LI>
     * <LI>
     * A common HttpEntity that might be used is UrlEncodedFormEntity for forms.
     * There exists the method <B>getFormHttpEntity</B> in the class <B>ApiUtils</B> that may be of use
     * </LI>
     * <LI>
     * For JSON use StringEntity. Example usage:<BR>
     * <CODE>
     * HttpEntity httpEntity = new StringEntity(JsonUtils.getGson().toJson(entity));
     * </CODE>
     * </LI>
     * <LI>
     * For XML use StringEntity. Example usage:<BR>
     * <CODE>
     * HttpEntity httpEntity = new StringEntity(new XStream().toXML(entity));
     * </CODE>
     * </LI>
     * </OL>
     *
     * @param httpEntity - Http Entity (Use null to clear a previously set Http Entity.)
     * @return FileDownloader
     */
    public FileDownloader withHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return this;
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
     *
     * @param linkToFile String
     * @return FileDownloader
     */
    public FileDownloader withURI(String linkToFile) {
        try {
            fileURI = new URI(linkToFile);
        } catch (URISyntaxException ex) {
            AssertJUtil.fail("Could not create URI from (%s) due to exception:  %s", linkToFile, ex.getMessage());
        }

        return this;
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
     *
     * @param linkToFile URI
     * @return FileDownloader
     */
    public FileDownloader withURI(URI linkToFile) {
        fileURI = linkToFile;
        return this;
    }

    /**
     * Specify a URL that you want to perform an HTTP Status Check upon/Download a file from
     *
     * @param linkToFile URL
     * @return FileDownloader
     */
    public FileDownloader withURI(URL linkToFile) {
        try {
            fileURI = linkToFile.toURI();
        } catch (URISyntaxException ex) {
            AssertJUtil.fail("Could not get URI (%s) due to exception:  %s", linkToFile, ex.getMessage());
        }

        return this;
    }

    /**
     * Perform an HTTP Status Check upon/Download the file specified in the href attribute of a WebElement
     *
     * @param anchorElement Selenium WebElement
     * @return FileDownloader
     */
    public FileDownloader withURISpecifiedInAnchorElement(WebElement anchorElement) {
        if (anchorElement.getTagName().equals("a")) {
            String href = anchorElement.getAttribute("href");
            try {
                fileURI = new URI(href);
            } catch (URISyntaxException ex) {
                AssertJUtil.fail("Could not parse href (%s) due to exception:  %s", href, ex.getMessage());
            }
        } else {
            AssertJUtil.fail("You have not specified an <a> element!");
        }

        return this;
    }

    /**
     * Perform an HTTP Status Check upon/Download the image specified in the src attribute of a WebElement
     *
     * @param imageElement Selenium WebElement
     * @return FileDownloader
     */
    public FileDownloader withURISpecifiedInImageElement(WebElement imageElement) {
        if (imageElement.getTagName().equals("img")) {
            String src = imageElement.getAttribute("src");
            try {
                fileURI = new URI(src);
            } catch (URISyntaxException ex) {
                AssertJUtil.fail("Could not parse src (%s) due to exception:  %s", src, ex.getMessage());
            }
        } else {
            AssertJUtil.fail("You have not specified an <img> element!");
        }

        return this;
    }

    /**
     * Add Request Header to be sent
     *
     * @param header - Request Header
     * @return FileDownloader
     */
    public FileDownloader withRequestHeader(Header header) {
        headers.add(header);
        return this;
    }

    /**
     * Add Request Header to be sent
     *
     * @param name  - Header Name
     * @param value - Header value
     * @return FileDownloader
     */
    public FileDownloader withRequestHeader(String name, String value) {
        Header header = new BasicHeader(name, value);
        return withRequestHeader(header);
    }

    /**
     * Reset the request headers back to the empty list
     *
     * @return FileDownloader
     */
    public FileDownloader resetRequestHeaders() {
        headers.clear();
        return this;
    }

    /**
     * Set the status codes that are considered successful<BR>
     * <B>Note: </B> The default status code for success is 400 (HttpStatus.SC_BAD_REQUEST)
     *
     * @param successfulStatusCodes - All status codes that are less than the value are considered successful
     * @return FileDownloader
     */
    public FileDownloader withSuccessfulStatusCodes(int successfulStatusCodes) {
        this.successfulStatusCodes = successfulStatusCodes;
        return this;
    }

    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet Set&lt;Cookie&gt;
     * @return BasicCookieStore
     */
    private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
        BasicCookieStore copyOfWebDriverCookieStore = new BasicCookieStore();
        for (Cookie seleniumCookie : seleniumCookieSet) {
            BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
            duplicateCookie.setDomain(seleniumCookie.getDomain());
            duplicateCookie.setSecure(seleniumCookie.isSecure());
            duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
            duplicateCookie.setPath(seleniumCookie.getPath());
            copyOfWebDriverCookieStore.addCookie(duplicateCookie);
        }

        return copyOfWebDriverCookieStore;
    }

    private HttpResponse getHTTPResponse() throws IOException {
        AssertJUtil.assertThat(fileURI).as("No file URI specified").isNotNull();

        HttpClient client = new FileDownloaderClient().getClient();
        BasicHttpContext localContext = new BasicHttpContext();

        // Clear down the local cookie store every time to make sure we don't have any left over cookies influencing the test
        localContext.setAttribute(HttpClientContext.COOKIE_STORE, null);
        if (mimicWebDriverCookieState) {
            localContext.setAttribute(HttpClientContext.COOKIE_STORE, mimicCookieState(driver.manage().getCookies()));
        }

        HttpRequestBase requestMethod = httpRequestMethod.getRequestMethod();
        requestMethod.setURI(fileURI);
        requestMethod.setConfig(RequestConfig.custom().setRedirectsEnabled(followRedirects).build());
        headers.forEach(requestMethod::setHeader);
        if (requestMethod instanceof HttpEntityEnclosingRequest && httpEntity != null) {
            ((HttpEntityEnclosingRequest) requestMethod).setEntity(httpEntity);
        }

        return client.execute(requestMethod, localContext);
    }

    /**
     * Gets the HTTP status code returned when trying to access the specified URI
     *
     * @return -1 if IOException occurs else status code
     */
    public int getLinkHTTPStatus() {
        HttpResponse fileToDownload;
        try {
            fileToDownload = getHTTPResponse();
        } catch (IOException io) {
            return -1;
        }

        try {
            return fileToDownload.getStatusLine().getStatusCode();
        } finally {
            if (fileToDownload.getEntity() != null) {
                try {
                    fileToDownload.getEntity().getContent().close();
                } catch (IOException ignore) {
                    // Ignore
                }
            }
        }
    }

    /**
     * Download a file from the specified URI to a temp file<BR>
     * <B>Note: </B> The file should be marked for deletion using the deleteOnExit method.
     *
     * @return File
     * @throws AssertionError in case of a problem or the connection was aborted
     */
    @SuppressWarnings("java:S2259")
    public File downloadFile() {
        return downloadFile(null);
    }

    /**
     * Download a file from the specified URI to a temp file<BR>
     * <B>Note: </B> The file should be marked for deletion using the deleteOnExit method.
     *
     * @param suffix - Suffix/Extension used to create the temp file.  (If null, then ".tmp" is used)
     * @return File
     * @throws AssertionError in case of a problem or the connection was aborted
     */
    @SuppressWarnings("java:S2259")
    public File downloadFile(String suffix) {
        return downloadFile(null, suffix);
    }

    /**
     * Download a file from the specified URI to a temp file<BR>
     * <B>Note: </B> The file should be marked for deletion using the deleteOnExit method.
     *
     * @param prefix - The prefix string to be used in generating the temp file's name
     *               which must be at least three characters long. (If null, then "download" is used)
     * @param suffix - Suffix/Extension used to create the temp file.  (If null, then ".tmp" is used)
     * @return File
     * @throws AssertionError in case of a problem or the connection was aborted
     */
    @SuppressWarnings("java:S2259")
    public File downloadFile(String prefix, String suffix) {
        File downloadedFile = DownloadUtils.createTempFile(prefix == null ? "download" : prefix, suffix);

        HttpResponse fileToDownload = null;
        try {
            fileToDownload = getHTTPResponse();
        } catch (IOException io) {
            downloadedFile.deleteOnExit();
            AssertJUtil.fail("Failed to download file to due exception:  %s", io.getMessage());
        }

        AssertJUtil.assertThat(fileToDownload.getStatusLine())
                .as("Download File - Status Line")
                .isNotNull();
        AssertJUtil.assertThat(fileToDownload.getStatusLine().getStatusCode())
                .as("Download File - Status Code")
                .isLessThan(successfulStatusCodes);

        InputStream source;
        String error = "";
        try {
            source = fileToDownload.getEntity().getContent();
        } catch (IOException io) {
            source = null;
            error = io.getMessage();
        }

        AssertJUtil.assertThat(source)
                .as("Could not get Content of Download File due to:  %s", error)
                .isNotNull();
        DownloadUtils.writeFile(source, downloadedFile);

        return downloadedFile;
    }

}
