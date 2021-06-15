package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.FileExamplesOtherFilesPage;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.lazerycode.selenium.filedownloader.FileDownloader;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.DomainObjectUtils;
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

        new Navigation(getContext()).toFileExamplesOtherFiles(Utils.isCleanCookiesSupported());
        downloadImageFromHref();
        downloadFileFromUri();
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
    }

    @Step("Perform Download CSV from HREF")
    private void downloadImageFromHref() {
        File img = new FileExamplesOtherFilesPage(getContext()).performDownloadOfCsvFileUsingElement();
        img.deleteOnExit();
        AssertJUtil.assertThat(img).exists().isFile();
    }

    @Step("Perform Download File from URI")
    private void downloadFileFromUri() {
        File excel = new FileExamplesOtherFilesPage(getContext()).performDownloadOfCsvFileUsingUri();
        excel.deleteOnExit();
        AssertJUtil.assertThat(excel).exists().isFile();

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        CsvUtils.read(excel.getAbsolutePath(), records, headers);
        AssertJUtil.assertThat(records).as("Records").isNotEmpty();
        AssertJUtil.assertThat(headers).as("Headers").isNotEmpty();
    }

}
