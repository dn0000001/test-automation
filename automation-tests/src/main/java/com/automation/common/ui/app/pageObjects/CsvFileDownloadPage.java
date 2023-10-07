package com.automation.common.ui.app.pageObjects;

import com.lazerycode.selenium.filedownloader.DownloadData;
import com.lazerycode.selenium.filedownloader.FileDownloader;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.DownloadUtils;
import com.taf.automation.ui.support.util.JsUtils;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.http.HttpStatus;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.components.WebComponent;

import java.io.File;

/**
 * CSV Files For Download page on the site <a href="https://www.stats.govt.nz/large-datasets/csv-files-for-download/">Stats NZ</a>
 */
@SuppressWarnings("java:S3252")
public class CsvFileDownloadPage extends PageObjectV2 {
    private static final String LINK_HTTP_STATUS = "Link HTTP Status";

    @XStreamOmitField
    @FindBy(css = "h3 [href$='research-and-development-survey-2022-csv-notes.csv']")
    private WebComponent downloadLink;

    public CsvFileDownloadPage() {
        super();
    }

    public CsvFileDownloadPage(TestContext context) {
        super(context);
    }

    /**
     * Perform download of the CSV file using an element
     *
     * @return the CSV file that was saved to a temporary file which needs to be deleted after
     */
    public File performDownloadOfCsvFileUsingElement() {
        FileDownloader downloader = new FileDownloader(getDriver());
        downloader.withURISpecifiedInAnchorElement(downloadLink.getCoreElement());

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
        downloader.withURI(downloadLink.getAttribute("href"));

        // This is unnecessary and only for testing purposes
        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        // For testing purposes, we are setting the both the prefix & suffix
        return downloader.downloadFile("auto-", ".csv");
    }

    /**
     * Perform download of the CSV file using JavaScript
     *
     * @return the CSV file that was saved to a temporary file which needs to be deleted after
     */
    public File performDownloadOfCsvFileUsingJavaScript() {
        String url = downloadLink.getAttribute("href");
        DownloadData downloadData = JsUtils.executeGetRequestDownload(url);

        // This is unnecessary and only for testing purposes
        int status = downloadData.getStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        File csv = DownloadUtils.createTempFile("auto-", ".csv");
        DownloadUtils.writeFile(csv, downloadData.getFile());

        return csv;
    }

}
