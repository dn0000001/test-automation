package com.taf.automation.ui.support.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.util.ByteArrayBuffer;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotUtil {
    private static final int MAX_SCROLLS = 20;
    private static final String DIVIDER_RESOURCE = "divider.png";

    private ScreenshotUtil() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Manually scroll the entire page taking screenshots of the visible viewport as you go
     *
     * @param driver
     * @return List of screenshots of all the visible viewports
     * @throws IOException if for any reason a screenshot of a viewport cannot be taken
     */
    public static List<ByteArrayBuffer> getAllViewPortScreenshots(WebDriver driver) throws IOException {
        int scrollHeight = getScrollHeight(driver);
        if (scrollHeight == 0) {
            throw new IOException("Invalid Scroll Height Calculated");
        }

        int viewportHeight = getViewPortHeight(driver);
        if (viewportHeight == 0) {
            throw new IOException("Invalid Viewport Height Calculated");
        }

        return getAllViewPortScreenshots(driver, 0, scrollHeight, viewportHeight, MAX_SCROLLS);
    }

    /**
     * Get the JavaScript for scroll height (because it is really long and does not fit nicely on a single line.)
     *
     * @return String
     */
    private static String getJavaScriptForScrollHeight() {
        StringBuilder sb = new StringBuilder();

        sb.append("return Math.max( ");
        sb.append("document.body.scrollHeight, ");
        sb.append("document.body.offsetHeight, ");
        sb.append("document.documentElement.clientHeight, ");
        sb.append("document.documentElement.scrollHeight, ");
        sb.append("document.documentElement.offsetHeight ");
        sb.append(");");

        return sb.toString();
    }

    /**
     * Get Scroll Height
     *
     * @param driver
     * @return scroll height of the document
     */
    private static int getScrollHeight(WebDriver driver) {
        String rawScrollHeight = String.valueOf(((JavascriptExecutor) driver).executeScript(getJavaScriptForScrollHeight()));
        return NumberUtils.toInt(rawScrollHeight, 0);
    }

    /**
     * Get the JavaScript for the viewport height
     *
     * @return String
     */
    private static String getJavaScriptForViewPortHeight() {
        return "return Math.max(document.documentElement.clientHeight, window.innerHeight || 0);";
    }

    /**
     * Get the viewport height
     *
     * @param driver
     * @return visible viewport height
     */
    private static int getViewPortHeight(WebDriver driver) {
        String rawViewPortHeight = String.valueOf(((JavascriptExecutor) driver).executeScript(getJavaScriptForViewPortHeight()));
        return NumberUtils.toInt(rawViewPortHeight, 0);
    }

    /**
     * Scroll to desired viewport and wait for screen to be redrawn
     *
     * @param driver
     * @param offset - Offset to scroll to
     */
    private static void scrollTo(WebDriver driver, int offset) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, arguments[0]);", offset);
        Utils.sleep(100);
    }

    /**
     * Take screenshot of the current visible view port and return as ByteArrayBuffer
     *
     * @param driver
     * @return ByteArrayBuffer
     */
    private static ByteArrayBuffer getScreenshotAsByteArrayBuffer(WebDriver driver) {
        byte[] visibleViewport = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        ByteArrayBuffer item = new ByteArrayBuffer(0);
        item.append(visibleViewport, 0, visibleViewport.length);
        return item;
    }

    /**
     * Manually scroll the range taking screenshots of the visible viewport as you go
     *
     * @param driver
     * @param startOffset - The offset at which to start, 0 is top of page
     * @param stopOffset  - The offset at which to stop
     * @param scrollDown  - Scroll down using this size
     * @param maxScrolls  - Max Scrolls before throwing exception (to prevent to many screenshots)
     * @return List of screenshots of all the visible viewports in the range
     * @throws IOException if for any reason a screenshot of a viewport cannot be taken
     */
    private static List<ByteArrayBuffer> getAllViewPortScreenshots(
            WebDriver driver,
            int startOffset,
            int stopOffset,
            int scrollDown,
            int maxScrolls
    ) throws IOException {

        if (scrollDown < 1) {
            throw new IOException("Invalid Scroll Down size");
        }

        if (maxScrolls < 1) {
            throw new IOException("Invalid Max Scrolls size");
        }

        List<ByteArrayBuffer> partialScreenshots = new ArrayList<>();
        int scrolls = 0;
        int offset = startOffset;
        while (offset < stopOffset) {
            // Scroll to desired viewport and wait for screen to be redrawn
            scrollTo(driver, offset);

            // Take the screenshot and add it to the list
            ByteArrayBuffer visibleViewport = getScreenshotAsByteArrayBuffer(driver);
            partialScreenshots.add(visibleViewport);

            // Calculate the next offset
            offset += scrollDown;

            // Prevent too many screenshots which could indicate a problem with scroll down value
            scrolls++;
            if (scrolls > maxScrolls) {
                throw new IOException("Max Scrolls exceeded");
            }
        }

        return partialScreenshots;
    }

    /**
     * Get the full screenshot like in old versions of the FirefoxDriver before Marionette
     * which is useful in debugging issues.
     *
     * @return the full screenshot (not just the visible viewport)
     * @throws IOException if for any reason the full screenshot could not be returned
     */
    public static byte[] getFullScreenshotAs(WebDriver driver) throws IOException {
        int scrollHeight = getScrollHeight(driver);
        if (scrollHeight == 0) {
            throw new IOException("Invalid Scroll Height Calculated");
        }

        int viewportHeight = getViewPortHeight(driver);
        if (viewportHeight == 0) {
            throw new IOException("Invalid Viewport Height Calculated");
        }

        // Read the divider that will be used and start by assuming this is the max width
        BufferedImage divider = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(DIVIDER_RESOURCE));
        int maxWidth = divider.getWidth();

        // Calculate the total height to allocate space for the full screenshot
        int totalHeight = 0;
        List<BufferedImage> bufferedImages = new ArrayList<>();
        List<ByteArrayBuffer> partialScreenshots = getAllViewPortScreenshots(driver, 0, scrollHeight, viewportHeight, MAX_SCROLLS);
        for (ByteArrayBuffer item : partialScreenshots) {
            // Read image and store for later
            BufferedImage currentImage = ImageIO.read(new ByteArrayInputStream(item.toByteArray()));
            bufferedImages.add(currentImage);

            // For each image we will later put a divider after as such add to this to the height as well
            totalHeight += currentImage.getHeight() + divider.getHeight();

            // Update max width
            maxWidth = Math.max(maxWidth, currentImage.getWidth());
        }

        if (maxWidth == 0) {
            throw new IOException("Invalid Max Width Calculated");
        }

        if (totalHeight == 0) {
            throw new IOException("Invalid Total Height Calculated");
        }

        //
        // Combine all the partial screenshots into a single huge screenshot
        //
        int y = 0;
        BufferedImage fullScreenshot = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = fullScreenshot.getGraphics();
        for (BufferedImage image : bufferedImages) {
            graphics.drawImage(image, 0, y, null);
            y += image.getHeight();

            graphics.drawImage(divider, 0, y, null);
            y += divider.getHeight();
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(fullScreenshot, "png", output);
        output.flush();
        output.close();
        return output.toByteArray();
    }

    /**
     * Convert a png screenshot to a jpg screenshot with compression quality of factor 0.7
     *
     * @param png - the png screenshot to be converted
     * @return screenshot in jpg format with compression quality of factor 0.7
     */
    public static byte[] convertPngToJpg(byte[] png) {
        return convertPngToJpg(png, 0.7f);
    }

    /**
     * Convert a png screenshot to a jpg screenshot with specified compression quality factor
     *
     * @param png - the png screenshot to be converted
     * @return screenshot in jpg format with specified compression quality factor
     */
    public static byte[] convertPngToJpg(byte[] png, float compressionQuality) {
        ByteArrayInputStream bais = new ByteArrayInputStream(png);
        BufferedImage image;
        try {
            image = ImageIO.read(bais);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to read the png:  " + ex.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios;
        try {
            ios = ImageIO.createImageOutputStream(baos);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to create image output stream for the jpg:  " + ex.getMessage());
        }

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(compressionQuality);
        jpgWriter.setOutput(ios);
        try {
            jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to convert the png to jpg:  " + ex.getMessage());
        }

        byte[] data = baos.toByteArray();
        jpgWriter.dispose();
        return data;
    }

}
