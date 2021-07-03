package com.taf.automation.ui.support.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("java:S3252")
public class DownloadUtils {
    private DownloadUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Wrapper of File.createTempFile that does not require handling of the exception
     *
     * @param prefix - The prefix string to be used in generating the file's name;
     *               must be at least three characters long
     * @param suffix - he suffix string to be used in generating the file's name;
     *               may be null, in which case the suffix ".tmp" will be used
     * @return An abstract pathname denoting a newly-created empty file
     * @throws AssertionError if temp file cannot be created
     */
    public static File createTempFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix);
        } catch (IOException io) {
            AssertJUtil.fail("Failed to create temp file to due exception:  %s", io.getMessage());
        }

        return null;
    }

    /**
     * Wrapper of FileUtils.writeByteArrayToFile that does not require handling of the exception
     *
     * @param file - the file to write to
     * @param data - the content to write to the file
     * @throws AssertionError if file cannot be written to
     */
    public static void writeFile(File file, byte[] data) {
        try {
            FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException io) {
            AssertJUtil.fail("Failed to write file to due exception:  %s", io.getMessage());
        }
    }

}
