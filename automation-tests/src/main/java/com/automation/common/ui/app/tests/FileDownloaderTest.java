package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.CsvFileDownloadPage;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.lazerycode.selenium.filedownloader.FileDownloader;
import com.taf.automation.api.ApiUtils;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.DomainObjectUtils;
import com.taf.automation.ui.support.util.DownloadUtils;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Testing file downloading
 */
@SuppressWarnings("java:S3252")
public class FileDownloaderTest extends TestNGBase {
    @Features("Framework")
    @Stories("FileDownloader")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest(ITestContext injectedContext) {
        DomainObjectUtils.overwriteTestName(injectedContext);
        new Navigation(getContext()).toHerokuappDownloads(Utils.isCleanCookiesSupported());
        downloadImageFromElement();

        new Navigation(getContext()).toCsvFileDownloadUrl(Utils.isCleanCookiesSupported());
        downloadImageFromHref();
        File csvUri = downloadFileFromUri();
        File csvJs = downloadFileUsingJavaScript();
        validateCsvFilesAreEqual(csvUri, csvJs);
    }

    @Step("Perform Download Image from Element")
    private void downloadImageFromElement() {
        FileDownloader downloader = new FileDownloader(getContext().getDriver());
        WebElement element = getContext().getDriver().findElement(By.cssSelector("[alt='Fork me on GitHub']"));
        downloader.withURISpecifiedInImageElement(element);
        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as("Link HTTP Status").isEqualTo(HttpStatus.SC_OK);

        File img = downloader.downloadFile();
        img.deleteOnExit();
        AssertJUtil.assertThat(img).exists().isFile();
        ApiUtils.attachDataFile(img, "image/png", "Fork Me On GitHub Image");
    }

    @Step("Perform Download CSV from HREF")
    private void downloadImageFromHref() {
        File csv = new CsvFileDownloadPage(getContext()).performDownloadOfCsvFileUsingElement();
        csv.deleteOnExit();
        AssertJUtil.assertThat(csv).exists().isFile();
        ApiUtils.attachDataFile(csv, "text/csv", "CSV from HREF");
    }

    @Step("Perform Download File from URI")
    private File downloadFileFromUri() {
        File csv = new CsvFileDownloadPage(getContext()).performDownloadOfCsvFileUsingUri();
        csv.deleteOnExit();
        AssertJUtil.assertThat(csv).exists().isFile();
        genericCsvValidations(csv, "CSV from URI");
        return csv;
    }

    @Step("Perform Download File Using JavaScript")
    private File downloadFileUsingJavaScript() {
        File csv = new CsvFileDownloadPage(getContext()).performDownloadOfCsvFileUsingJavaScript();
        csv.deleteOnExit();
        AssertJUtil.assertThat(csv).exists().isFile();
        genericCsvValidations(csv, "CSV using JavaScript");
        return csv;
    }

    private void genericCsvValidations(File csv, String attachmentName) {
        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        CsvUtils.read(csv.getAbsolutePath(), records, headers);
        AssertJUtil.assertThat(records).as("Records").isNotEmpty();
        AssertJUtil.assertThat(headers).as("Headers").isNotEmpty();
        ApiUtils.attachDataFile(csv, "text/csv", attachmentName);
    }

    @Step("Validate CSV Files Are Equal")
    private void validateCsvFilesAreEqual(File csvUri, File csvJs) {
        boolean result = DownloadUtils.contentEquals(csvUri, csvJs);
        AssertJUtil.assertThat(result).as("CSV URI vs. CSV JS").isTrue();
    }

}
