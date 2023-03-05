package com.taf.automation.ui.support.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

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

    /**
     * Wrapper of FileUtils.copyInputStreamToFile that does not require handling of the exception
     *
     * @param source      - the InputStream to copy bytes from, must not be null, will be closed
     * @param destination - the non-directory File to write bytes to (possibly overwriting), must not be null
     */
    public static void writeFile(InputStream source, File destination) {
        try {
            FileUtils.copyInputStreamToFile(source, destination);
        } catch (IOException io) {
            AssertJUtil.fail("Failed to copy the stream to the file to due exception:  %s", io.getMessage());
        }
    }

    /**
     * Wrapper of FileUtils.copyFile that does not require handling of the exception
     *
     * @param fileSource - an existing file to copy, must not be null
     * @param fileTarget - the new file, must not be null
     */
    public static void copyFile(File fileSource, File fileTarget) {
        try {
            FileUtils.copyFile(fileSource, fileTarget);
        } catch (IOException io) {
            AssertJUtil.fail("Failed to copy the file to due exception:  %s", io.getMessage());
        }
    }

    /**
     * Wrapper of FileUtils.copyURLToFile that does not require handling of the exception
     *
     * @param source      - the URL to copy bytes from, must not be null
     * @param destination - the non-directory File to write bytes to (possibly overwriting), must not be null
     */
    public static void copyURLToFile(URL source, File destination) {
        try {
            FileUtils.copyURLToFile(source, destination);
        } catch (IOException io) {
            AssertJUtil.fail("Failed to copy the URL to file to due exception:  %s", io.getMessage());
        }
    }

    /**
     * Wrapper of FileUtils.contentEquals that does not require handling of the exception
     *
     * @param lhs - the first file
     * @param rhs - the second file
     * @return true if the content of the files are equal or they both don't exist, false otherwise
     */
    public static boolean contentEquals(File lhs, File rhs) {
        try {
            return FileUtils.contentEquals(lhs, rhs);
        } catch (IOException io) {
            AssertJUtil.fail("Failed to read the files to due exception:  %s", io.getMessage());
            return false;
        }
    }

    /**
     * Extract Zip file contents to temporary folder with prefix
     *
     * @param zip    - Zip file to extract contents
     * @param prefix - the prefix string to be used in generating the temporary directory's name; may be null
     * @return temporary folder that needs to be deleted when complete (Use FileUtils.deleteQuietly)
     */
    public static File extractZipToTemp(File zip, String prefix) {
        File tempDir = null;

        try {
            tempDir = Files.createTempDirectory(prefix).toFile();
            ZipFile zipFile = new ZipFile(zip);
            zipFile.extractAll(tempDir.getAbsolutePath());
        } catch (ZipException | IOException ex) {
            FileUtils.deleteQuietly(tempDir);
            AssertJUtil.fail("Failed to extract zip contents due to exception:  %s", ex.getMessage());
        }

        return tempDir;
    }

    /**
     * Get file object with exact filename in a sub-folder in the temp folder
     *
     * @param readFolder - Read folder from resources or absolute location
     * @return File (if resource a temp file is created that will be deleted on exit.)
     */
    public static File getFileWithExactFilename(String readFolder) {
        File temp = null;
        try {
            String first = FileUtils.getTempDirectory().getAbsolutePath();
            Path pathUUID = FileSystems.getDefault().getPath(first, UUID.randomUUID().toString());
            File tempDir = pathUUID.toFile();
            FileUtils.forceMkdir(tempDir);
            tempDir.deleteOnExit();

            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(readFolder);
            String name = FilenameUtils.getName(readFolder);
            Path pathTemp = FileSystems.getDefault().getPath(tempDir.getAbsolutePath(), name);
            temp = Files.createFile(pathTemp).toFile();
            temp.deleteOnExit();
            FileUtils.copyInputStreamToFile(is, temp);
            return temp;
        } catch (Exception ex) {
            FileUtils.deleteQuietly(temp);
            return new File(readFolder);
        }
    }

}
