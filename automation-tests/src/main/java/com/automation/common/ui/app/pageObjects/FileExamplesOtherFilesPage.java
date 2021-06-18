package com.automation.common.ui.app.pageObjects;

import com.lazerycode.selenium.filedownloader.FileDownloader;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.http.HttpStatus;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;

import java.io.File;

/**
 * This the Other Files page on the site <a href="https://file-examples.com/">File Examples</a>
 */
@SuppressWarnings("java:S3252")
public class FileExamplesOtherFilesPage extends PageObjectV2 {
    private static final String LINK_HTTP_STATUS = "Link HTTP Status";

    @XStreamOmitField
    @FindBy(css = "[download$='.csv']")
    private WebComponent downloadSampleCsvFile;

    public FileExamplesOtherFilesPage() {
        super();
    }

    public FileExamplesOtherFilesPage(TestContext context) {
        super(context);
    }

    /**
     * Perform download of the CSV file using an element
     *
     * @return the CSV file that was saved to a temporary file which needs to be deleted after
     */
    public File performDownloadOfCsvFileUsingElement() {
        FileDownloader downloader = new FileDownloader(getDriver());
        downloader.withURISpecifiedInAnchorElement(downloadSampleCsvFile.getCoreElement());

        // This is unnecessary and only for testing purposes
        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        // For testing purposes, we are setting the suffix
        return downloader.downloadFile(".csv");
    }

    /**
     * Perform download of the CSV file using a URL
     *
     * @return the CSV file that was saved to a temporary file which needs to be deleted after
     */
    public File performDownloadOfCsvFileUsingUri() {
        FileDownloader downloader = new FileDownloader(getDriver());
        downloader.withURI(downloadSampleCsvFile.getAttribute("href"));

        // This is unnecessary and only for testing purposes
        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        // For testing purposes, we are setting the both the prefix & suffix
        return downloader.downloadFile("auto-", ".csv");
    }

}
