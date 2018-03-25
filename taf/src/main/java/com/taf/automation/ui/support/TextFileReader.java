package com.taf.automation.ui.support;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

/**
 * This class reads a plain text files with code (JavaScript, SQL, etc)
 */
public class TextFileReader {
    private String file;
    private String spacer;
    private StringBuilder sb;

    /**
     * Constructor - Sets Spacer to a new line<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If spacer is not new line then comments (--) in a SQL file will cause issues<BR>
     */
    public TextFileReader() {
        setFile("");
        setSpacer("\n");
    }

    /**
     * Set the File to be read
     *
     * @param file - File to be read
     */
    public void setFile(String file) {
        this.file = StringUtils.defaultString(file);
    }

    /**
     * Returns the current file
     *
     * @return String
     */
    public String getFile() {
        return file;
    }

    /**
     * Set the spacer which is used to keep lines separate after reading<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If spacer is not new line then comments (--) in a SQL file will cause issues<BR>
     *
     * @param spacer - spacer to be added to the string buffer after reading each code line
     */
    public void setSpacer(String spacer) {
        this.spacer = StringUtils.defaultString(spacer);
    }

    /**
     * Returns the current spacer
     *
     * @return String
     */
    public String getSpacer() {
        return spacer;
    }

    /**
     * Gets the text read from the file
     *
     * @return String
     */
    public String getText() {
        if (sb != null) {
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * Reads the text file and populates the string buffer
     *
     * @return true if successful else false
     */
    public boolean readFile() {
        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            sb = new StringBuilder();
            fill(br, sb);

            in.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Reading the text file (" + file + ") failed due to exception ["
                    + ex.getClass().getName() + "]:  " + ex.getMessage());
            return false;
        }
    }

    /**
     * Reads the resource text file and populates the string buffer
     *
     * @return true if successful else false
     */
    public boolean readFromResource() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
            DataInputStream in = new DataInputStream(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            sb = new StringBuilder();
            fill(br, sb);

            in.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Reading the resource text file (" + file + ") failed due to exception ["
                    + ex.getClass().getName() + "]:  " + ex.getMessage());
            return false;
        }
    }

    /**
     * Uses the reader to fill the builder
     *
     * @param reader  - Reader that contains the information
     * @param builder - Builder to store the information
     * @throws Exception
     */
    private void fill(BufferedReader reader, StringBuilder builder) throws Exception {
        String line = reader.readLine();
        while (line != null) {
            // Store the code line
            builder.append(line);

            // Need spacer to ensure the code lines on different lines do not join into 1 becoming invalid
            builder.append(spacer);

            // Read next code line
            line = reader.readLine();
        }
    }

}
