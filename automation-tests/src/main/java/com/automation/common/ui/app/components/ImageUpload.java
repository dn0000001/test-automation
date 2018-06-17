package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.Utils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import ui.auto.core.pagecomponent.PageComponentNoDefaultAction;
import ui.auto.core.utils.WebDriverUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Example of how to upload images that uses a standard system dialog
 */
public class ImageUpload extends PageComponentNoDefaultAction {
    private RemoteWebElement inputUploadFile;
    private RemoteWebDriver driver;
    private List<String> errors;

    @Override
    protected void init() {
        driver = (RemoteWebDriver) WebDriverUtils.getDriverFromElement(getCoreElement());
        inputUploadFile = (RemoteWebElement) driver.findElement(By.cssSelector("input[type=file]"));
    }

    public List<String> getErrors() {
        return errors;
    }

    public void uploadFiles(List<String> imageFiles) {
        errors = new ArrayList<>();
        for (String filePath : imageFiles) {
            assertThat("Upload Button is not enabled!", coreElement.isEnabled(), is(true));

            //
            // Read the file to upload from the resources in the JAR file
            //
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            File file = new File(filePath);
            if (is == null) {
                throw new RuntimeException("Image file '" + filePath + "' was not found!");
            }

            //
            // Need to copy the resource from the JAR to the local file system such that it can be uploaded
            //
            try {
                FileUtils.copyInputStreamToFile(is, file);
                is.close();
            } catch (IOException ignored) {

            }

            //
            // Only necessary if using Grid
            //
            if (TestProperties.getInstance().getRemoteURL() != null) {
                inputUploadFile.setFileDetector(new LocalFileDetector());
            }

            //
            // Your custom logic to know when the upload is complete
            //
            inputUploadFile.sendKeys(file.getAbsolutePath());
            List<WebElement> images = driver.findElements(By.cssSelector(".spinner"));
            if (images.size() > 0) {
                Utils.waitForElementToHide(images.get(0));
            }

            //
            // Your custom logic to find any errors if necessary
            //
            WebElement error = driver.findElement(By.cssSelector((".error")));
            if (error.isDisplayed()) {
                errors.add(error.getText());
            }
        }
    }

    public void uploadFiles(String imageFiles) {
        String[] files = imageFiles.split(",");
        Utils.trim(files);
        uploadFiles(Arrays.asList(files));
    }

    @Override
    public void setValue() {
        uploadFiles(getData());
    }

}