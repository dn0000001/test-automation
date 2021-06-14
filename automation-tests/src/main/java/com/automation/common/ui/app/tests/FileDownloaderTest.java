package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.pageObjects.Navigation;
import com.lazerycode.selenium.filedownloader.FileDownloader;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.DomainObjectUtils;
import com.taf.automation.ui.support.util.FilloUtils;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
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
    private static final String LINK_HTTP_STATUS = "Link HTTP Status";

    @Features("Framework")
    @Stories("FileDownloader")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void performTest(ITestContext injectedContext) {
        DomainObjectUtils.overwriteTestName(injectedContext);
        new Navigation(getContext()).toHerokuappDownloads(Utils.isCleanCookiesSupported());
        FileDownloader downloader = new FileDownloader(getContext().getDriver());
        downloadImageFromElement(downloader);
        downloadImageFromHref(downloader);
        downloadFileFromUri(downloader);
    }

    @Step("Perform Download Image from Element")
    private void downloadImageFromElement(FileDownloader downloader) {
        WebElement element = getContext().getDriver().findElement(By.cssSelector("[alt='Fork me on GitHub']"));
        downloader.withURISpecifiedInImageElement(element);
        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        File img = downloader.downloadFile();
        img.deleteOnExit();
        AssertJUtil.assertThat(img).exists().isFile();
    }

    @Step("Perform Download Image from HREF")
    private void downloadImageFromHref(FileDownloader downloader) {
        WebElement element = getContext().getDriver().findElement(By.xpath("//a[@href='download/chow.jpg']"));
        downloader.withURISpecifiedInAnchorElement(element);
        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        File img = downloader.downloadFile(".jpg");
        img.deleteOnExit();
        AssertJUtil.assertThat(img).exists().isFile();
    }

    @Step("Perform Download File from URI")
    private void downloadFileFromUri(FileDownloader downloader) {
        WebElement element = getContext().getDriver().findElement(By.xpath("//a[@href='download/people.xlsx']"));
        downloader.withURI(element.getAttribute("href"));

        // Test adding headers & then resetting back to empty
        downloader.withRequestHeader("testA", "something").withRequestHeader(new BasicHeader("testB", "xyz"));
        downloader.resetRequestHeaders();

        int status = downloader.getLinkHTTPStatus();
        AssertJUtil.assertThat(status).as(LINK_HTTP_STATUS).isEqualTo(HttpStatus.SC_OK);

        File excel = downloader.downloadFile("auto-", ".xlsx");
        excel.deleteOnExit();
        AssertJUtil.assertThat(excel).exists().isFile();

        List<CSVRecord> records = new ArrayList<>();
        Map<String, Integer> headers = new HashMap<>();
        FilloUtils.read(excel.getAbsolutePath(), "records", records, headers);
        AssertJUtil.assertThat(records).as("Records").isNotEmpty();
        AssertJUtil.assertThat(headers).as("Headers").isNotEmpty();
    }

}
