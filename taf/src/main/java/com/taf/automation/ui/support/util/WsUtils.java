package com.taf.automation.ui.support.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taf.automation.ui.support.Parameter;
import com.taf.automation.ui.support.VTD_XML;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class provides useful functionality working with web services
 */
public class WsUtils {
    private WsUtils() {
        // Prevent initialization of class as all public methods should be static
    }

    /**
     * Reads a file and returns it as a String<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) UTF-8 encoding is used<BR>
     *
     * @param sFile - Location to file
     * @return null if error else File as a String
     */
    public static String toString(String sFile) {
        return toString(sFile, "UTF-8");
    }

    /**
     * Reads a file and returns it as a String
     *
     * @param sFile     - Location to file
     * @param sEncoding - Encoding of file
     * @return null if error else File as a String
     */
    public static String toString(String sFile, String sEncoding) {
        try {
            File file = new File(sFile);
            FileInputStream fis = new FileInputStream(file);
            return IOUtils.toString(fis, sEncoding);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Uses the list of parameters to do replacements (1st occurrence) in the string<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Parameter.param contains the <B>regular expression</B> used to find the text and must be escaped as
     * necessary<BR>
     * 2) Parameter.value contains the replacement text<BR>
     *
     * @param sTemplate - String to be worked on
     * @param subst     - List of Parameters that contains regular expressions used to find the text &amp; the
     *                  replacement text
     * @return if sTemplate is null then null else string with all variables replaced as specified
     */
    public static String replace1st(String sTemplate, List<Parameter> subst) {
        return replace(sTemplate, subst, false);
    }

    /**
     * Uses the list of parameters to do replacements (all occurrences) in the string<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Parameter.param contains the text to find<BR>
     * 2) Parameter.value contains the replacement text<BR>
     *
     * @param sTemplate - String to be worked on
     * @param subst     - List of Parameters that contains text to find &amp; the replacement text
     * @return if sTemplate is null then null else string with all variables replaced as specified
     */
    public static String replaceAll(String sTemplate, List<Parameter> subst) {
        return replace(sTemplate, subst, true);
    }

    /**
     * Uses the list of parameters to do replacements in the string<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Parameter.param contains the text to find<BR>
     * 2) Parameter.value contains the replacement text<BR>
     * 3) if bReplaceAll is false, then Parameter.param is a regular expression and needs to be escaped when
     * applicable<BR>
     *
     * @param sTemplate   - String to be worked on
     * @param subst       - List of Parameters that contains text to find & the replacement text
     * @param bReplaceAll - true to replace all occurrences, false only replaces the 1st occurrence
     * @return if sTemplate is null then null else string with all variables replaced as specified
     */
    private static String replace(String sTemplate, List<Parameter> subst, boolean bReplaceAll) {
        // Handle null
        if (sTemplate == null)
            return null;

        // For better performance
        if (sTemplate.equals(""))
            return sTemplate;

        String sWorking = sTemplate;
        for (Parameter p : subst) {
            if (bReplaceAll) {
                sWorking = sWorking.replace(p.param, p.value);
            } else {
                sWorking = sWorking.replaceFirst(p.param, p.value);
            }
        }

        return sWorking;
    }

    /**
     * Replaces all the occurrences in the _REPLACE_ALL list and then replaces the 1st occurrence in the
     * _REPLACE1ST list
     *
     * @param sTemplate    - String to be worked on
     * @param _REPLACE_ALL - List of Parameters that contains text to find &amp; the replacement text
     * @param _REPLACE1ST  - List of Parameters that contains regular expressions used to find the text &amp;
     *                     the replacement text
     * @return if sTemplate is null then null else string with all variables replaced as specified
     */
    public static String replace(String sTemplate, List<Parameter> _REPLACE_ALL, List<Parameter> _REPLACE1ST) {
        String sWorking;
        sWorking = replaceAll(sTemplate, _REPLACE_ALL);
        sWorking = replace1st(sWorking, _REPLACE1ST);
        return sWorking;
    }

    /**
     * Gets Parameter from an XML file
     *
     * @param xml           - XML object
     * @param sXpathBase    - xpath base (must end with /)
     * @param sDefaultParam - Default Param value if not specified in XML file
     * @param sDefaultValue - Default Value value if not specified in XML file
     * @return Parameter
     */
    private static Parameter getParameter(VTD_XML xml, String sXpathBase, String sDefaultParam, String sDefaultValue) {
        String param = xml.getNodeValue(sXpathBase + "Param", sDefaultParam);
        String value = xml.getNodeValue(sXpathBase + "Value", sDefaultValue);
        return new Parameter(param, value);
    }

    /**
     * Gets a List of Parameter from an XML file<BR>
     * <BR>
     * <B>Example:</B><BR>
     * Get a list of parameters from following XML (test.xml):<BR>
     * &lt;Parameters&gt;<BR>
     * &nbsp;&nbsp;&lt;Parameter&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Param&gt;REPLACE&lt;/Param&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Value&gt;90210&lt;/Value&gt;<BR>
     * &nbsp;&nbsp;&lt;/Parameter&gt;<BR>
     * &nbsp;&nbsp;&lt;Parameter&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Value&gt;something&lt;/Value&gt;<BR>
     * &nbsp;&nbsp;&lt;/Parameter&gt;<BR>
     * &nbsp;&nbsp;&lt;Parameter&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Param&gt;ALL&lt;/Param&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;Value&gt;aaa&lt;/Value&gt;<BR>
     * &nbsp;&nbsp;&lt;/Parameter&gt;<BR>
     * &lt;/Parameters&gt;<BR>
     * <BR>
     * <B>Code:</B><BR>
     * VTD_XML xml = new VTD_XML("test.xml");<BR>
     * getParameters(xml, "/Parameters/Parameter/", "REPLACE", "");<BR>
     * <BR>
     *
     * @param xml           - XML object
     * @param sXpathBase    - xpath base (must end with /)
     * @param sDefaultParam - Default Param value if not specified in XML file
     * @param sDefaultValue - Default Value value if not specified in XML file
     * @return List&lt;Parameter&gt;
     */
    public static List<Parameter> getParameters(VTD_XML xml, String sXpathBase, String sDefaultParam, String sDefaultValue) {
        List<Parameter> parameters = new ArrayList<>();

        String sXpath_Common = StringUtils.removeEnd(sXpathBase, "/");
        int nNodes = xml.getNodesCount(sXpath_Common);
        for (int i = 0; i < nNodes; i++) {
            parameters.add(getParameter(xml, sXpath_Common + "[" + (i + 1) + "]/", sDefaultParam, sDefaultValue));
        }

        return parameters;
    }

    /**
     * Reads the input stream, returns a string and closes stream<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
     * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
     *
     * @param inputStream - input stream
     * @param charsetName - The name of a supported charset to use
     * @param bDebug      - true to log warning if exception reading stream
     * @return empty string if an error occurs else input stream as a string
     */
    public static String toString(InputStream inputStream, String charsetName, boolean bDebug) {
        InputStreamReader isr = null;
        try {
            // Put input stream into buffer to read easily
            isr = new InputStreamReader(inputStream, charsetName);
            BufferedReader in = new BufferedReader(isr);

            String responseString = "";
            String outputString = "";
            while ((responseString = in.readLine()) != null) {
                outputString += responseString;
            }

            return outputString;
        } catch (Exception ex) {
            if (bDebug) {
                Helper.log("Input Stream to String caused following exception:  " + ex, true);
            }

            return "";
        } finally {
            try {
                isr.close();
            } catch (Exception e) {

            }
        }
    }

    /**
     * Reads the input stream, returns a string and closes stream<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Use Charset.availableCharsets() to get the available charsets that can be used<BR>
     * 2) Use Charset.defaultCharset() if you need to know the default charset for the machine<BR>
     *
     * @param inputStream - input stream
     * @param charsetName - The name of a supported charset to use
     * @return empty string if an error occurs else input stream as a string
     */
    public static String toString(InputStream inputStream, String charsetName) {
        return toString(inputStream, charsetName, false);
    }

    /**
     * Reads the input stream, returns a string and closes stream<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Uses unicode charset (UTF-8)<BR>
     *
     * @param inputStream - input stream
     * @return empty string if an error occurs else input stream as a string
     */
    public static String toString(InputStream inputStream) {
        return toString(inputStream, "UTF-8");
    }

    /**
     * Reads the PDF from the input stream, returns a string by parsing the PDF and closes stream
     *
     * @param inputStream - input stream that contains PDF
     * @return empty string if an error occurs else input stream (PDF) as a string
     */
    public static String toStringFromPDF(InputStream inputStream) {
        try {
            PDFParser parser = new PDFParser(inputStream);
            parser.parse();
            PDDocument pdf = parser.getPDDocument();
            PDFTextStripper strip = new PDFTextStripper();
            String sConvertedPDF = strip.getText(pdf);
            pdf.close();
            return sConvertedPDF;
        } catch (Exception ex) {
            return "";
        } finally {
            try {
                inputStream.close();
            } catch (Exception ex) {

            }
        }
    }

    /**
     * Determines whether URL is for a secure site (HTTPS)
     *
     * @param sUrl - URL to check if using HTTPS (secure site)
     * @return true if URL starts with https (case insensitive) and more than 5 characters else false
     */
    public static boolean isSecureSite(String sUrl) {
        /*
         * Need to determine if secure site or not
         */
        boolean bSecureSite = false;
        if (sUrl.length() > 5) {
            String sSecurePrefix = sUrl.substring(0, 5);
            if (sSecurePrefix.equalsIgnoreCase("https")) {
                bSecureSite = true;
            }
        }

        return bSecureSite;
    }

    /**
     * Gets the Base URL<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * <BR>
     * 1) If _URL is not in proper format (i.e. cause an exception) then _URL is just returned.<BR>
     * <BR>
     * <B>Example:</B><BR>
     * IF _URL = "https://test.com/Ajax/somthing/service" THEN "https://test.com" will be returned<BR>
     *
     * @param _URL - URL to get Base URL from
     * @return String
     */
    public static String getBaseURL(String _URL) {
        try {
            return getBaseURL(new URL(_URL));
        } catch (Exception ex) {
            return _URL;
        }
    }

    /**
     * Gets the Base URL<BR>
     * <BR>
     * <B>Example:</B><BR>
     * IF _URL = "https://test.com/Ajax/somthing/service" THEN "https://test.com" will be returned<BR>
     *
     * @param _URL - URL to get Base URL from
     * @return String
     */
    public static String getBaseURL(URL _URL) {
        String sPort;
        if (_URL.getPort() < 0) {
            sPort = "";
        } else {
            sPort = ":" + _URL.getPort();
        }

        return _URL.getProtocol() + "://" + _URL.getHost() + sPort;
    }

    /**
     * Uses the current URL to return an URL object<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Runtime exception occurs if the current URL is invalid and an URL object cannot be created<BR>
     *
     * @param driver
     * @return URL
     */
    public static URL getURL(WebDriver driver) {
        try {
            return new URL(driver.getCurrentUrl());
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid URL as following exception [");
            sb.append(ex.getClass().getName());
            sb.append("] occurred:  ");
            sb.append(ex.getMessage());

            Helper.log(sb.toString(), true);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Converts an object to a JSON String
     *
     * @param data              - Data object to be converted to a JSON String
     * @param bExceptionOnError - true to throw exception if any error occurs converting object to JSON String
     * @return JSON String
     */
    public static String toJSON(Object data, boolean bExceptionOnError) {
        try {
            // Used to marshal and unmarshal JSON
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data);
        } catch (Exception ex) {
            if (bExceptionOnError) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to construct JSON to be sent due to following exception [");
                sb.append(ex.getClass().getName());
                sb.append("]:  ");
                sb.append(ex.getMessage());

                Helper.log(sb.toString(), true);
                throw new RuntimeException(ex);
            }
        }

        return "";
    }

    /**
     * Converts an object to a JSON String
     *
     * @param data - Data object to be converted to a JSON String
     * @return non-null
     */
    public static String toJSON(Object data) {
        return StringUtils.defaultString(toJSON(data, false));
    }

    /**
     * Converts class to a JSON String<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Uses reflection to extract class name which is part of the JSON String<BR>
     * 2) This method can be used to write the class to the log<BR>
     *
     * @param data - Class to be converted to a JSON String
     * @return non-null
     */
    public static String toLogAsJSON(Object data) {
        // Get the class name with package
        String sClassName = data.getClass().getName();

        // Attempt to get class name without package
        int nLastPeriod = sClassName.lastIndexOf(".");
        if (nLastPeriod >= 0) {
            sClassName = sClassName.substring(nLastPeriod + 1);
        }

        return "{\"" + sClassName + "\":" + toJSON(data) + "}";
    }

    /**
     * Converts class to a JSON array item String<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This method can be used to write the class to the log<BR>
     *
     * @param sItemName - Array Item Name
     * @param data      - Class to be converted to a JSON String
     * @return non-null
     */
    public static String toJSON(String sItemName, Object data) {
        return "\"" + sItemName + "\":" + toJSON(data);
    }

    /**
     * Converts class to a JSON array item String<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This method can be used to write the objects to the log<BR>
     *
     * @param names - List of all the names to be used
     * @param items - List of the corresponding classes to be converted to a JSON String
     * @return empty string if any error occurs else non-null
     */
    public static String toJSON(List<String> names, List<Object> items) {
        try {
            String sJSON = "";

            for (int i = 0; i < items.size(); i++) {
                sJSON += toJSON(names.get(i), items.get(i)) + ",";
            }

            return StringUtils.removeEnd(sJSON, ",");
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Converts an array of JSON items to a JSON String<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This method can be used to write the class to the log<BR>
     * 2) The JSON array items should have been created using toJSON method that does this<BR>
     *
     * @param mainName         - Item Name for all the array items (or null/empty for no name)
     * @param _JSON_arrayItems - JSON items
     * @return non-null
     */
    public static String toLogAsJSON(String mainName, String... _JSON_arrayItems) {
        String sJSON = "{\"" + mainName + "\":{";
        if (mainName == null || mainName.equals("")) {
            sJSON = "{";
        } else {
            sJSON = "{\"" + mainName + "\":{";
        }

        for (String item : _JSON_arrayItems) {
            sJSON += item + ",";
        }

        sJSON = StringUtils.removeEnd(sJSON, ",") + "}";
        if (mainName != null && !mainName.equals("")) {
            sJSON += "}";
        }

        return sJSON;
    }

    /**
     * Converts string to JSON Date for request<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If the String cannot be parsed, then an invalid value will be returned (NaN)<BR>
     * <BR>
     * <B>Examples of valid date strings:</B><BR>
     * 1) "4/9/2014 15:17:33 GMT"<BR>
     * 2) "4/9/2014 15:17:33"<BR>
     * 3) "4/9/2014 2:16:09 PM GMT"<BR>
     * 4) "4/9/2014 2:16:09 PM EST"<BR>
     * 5) "4/9/2014 2:16:09 AM GMT"<BR>
     *
     * @param driver
     * @param dateString - Properly Formated String
     * @return String that can be used in a request
     */
    public static String toJSON_DateString(WebDriver driver, String dateString) {
        String sJS = "return Date.parse(\"" + dateString + "\");";
        String milliseconds = String.valueOf(JsUtils.execute(driver, sJS, (Object[]) null));
        String sJSON = "/Date(" + StringUtils.defaultString(milliseconds) + ")/";
        return sJSON;
    }

    /**
     * Converts a JSON Date into a Java Date object<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If the String is not in the proper format, then Date returned may be invalid<BR>
     * <BR>
     * <B>Example of valid JSON Date string: </B>"/Date(1397056653000)/"<BR>
     *
     * @param driver
     * @param sJSON_Date - JSON Date
     * @return Date
     */
    public static Date toDate(WebDriver driver, String sJSON_Date) {
        StringBuffer sb = new StringBuffer();
        sb.append("var jsonDate = arguments[0];");
        sb.append("var date = new Date(parseInt(jsonDate.substr(6)));");
        sb.append("return date.valueOf();");

        String milliseconds = String.valueOf(JsUtils.execute(driver, sb.toString(), sJSON_Date));
        return new Date(NumberUtils.toLong(milliseconds, -1L));
    }

    /**
     * Parses the JSON into a Map<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) JSON.stringify may not return a string that can be parsed (or even a valid JSON which tends to occur
     * if an array of objects is returned.)<BR>
     * 2) Use the appropriate overloaded toMap method if the string is generated using JSON.stringify<BR>
     *
     * @param sJSON - JSON to be parsed
     * @return Map&lt;String, Object&gt;
     * @throws RuntimeException if parsing causes an exception
     */
    public static Map<String, Object> toMap(String sJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            @SuppressWarnings("unchecked")
            Map<String, Object> data = mapper.readValue(sJSON, Map.class);

            // Something is wrong if the Map is null
            if (data != null) {
                return data;
            } else {
                throw new Exception("Returned Map was null");
            }
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("Parsing JSON caused the following exception [");
            sb.append(ex.getClass().getName());
            sb.append("]:  ");
            sb.append(ex.getMessage());

            Helper.log("JSON:  " + sJSON, true);
            Helper.log(sb.toString(), true);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Parses the JSON into a Map
     *
     * @param _root      - Root Node that will contain all the data
     * @param _stringify - String produced by using JSON.stringify
     * @return Map&lt;String, Object&gt;
     * @throws RuntimeException if parsing causes an exception
     */
    public static Map<String, Object> toMap(String _root, String _stringify) {
        return toMap("{\"" + _root + "\":" + _stringify + "}");
    }

    /**
     * Parses a string that was produced using JSON.stringify into a list<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Returned List will never be null<BR>
     *
     * @param _stringify - The string to parse
     * @return List&lt;Map&lt;String, Object&gt;&gt;
     * @throws RuntimeException if parsing causes an exception
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> toMapList(String _stringify) {
        Map<String, Object> dom = WsUtils.toMap("root", _stringify);
        if (dom == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) dom.get("root");
        if (data == null) {
            return new ArrayList<>();
        } else {
            return data;
        }
    }

    /**
     * Opens specified file and returns content as base64 encoded string
     *
     * @param filename - File that content is to be encoded
     * @return null if an exception occurs else string that is base64 encoded
     */
    public static String encode(String filename) {
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            byte[] binaryFile = new byte[(int) file.length()];
            fis.read(binaryFile);
            fis.close();
            byte[] bytesEncoded = Base64.encodeBase64(binaryFile);
            return new String(bytesEncoded);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Decode the base64 encoded string and create the specified file
     *
     * @param encoded  - A base64 encoded string
     * @param filename - File to be created
     * @return true if file with decoded content is created successfully else false
     */
    public static boolean decode(String encoded, String filename) {
        try {
            byte[] bytesDecoded = Base64.decodeBase64(encoded);
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(bytesDecoded);
            fos.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Parses the JSON into a List<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) This method should be only used on JSON strings that are in a list format. For example, ["a", 1,
     * "c"]<BR>
     * 2) Cannot have any empty values. For example, ["a", , 1]<BR>
     *
     * @param sJSON - JSON to be parsed
     * @return List&lt;Object&gt;
     * @throws RuntimeException if parsing causes an exception
     */
    public static List<Object> toArrayList(String sJSON) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            @SuppressWarnings("unchecked")
            List<Object> data = mapper.readValue(sJSON, List.class);

            // Something is wrong if the List is null
            if (data != null) {
                return data;
            } else {
                throw new Exception("Returned List was null");
            }
        } catch (Exception ex) {
            StringBuilder sb = new StringBuilder();
            sb.append("Parsing JSON caused the following exception [");
            sb.append(ex.getClass().getName());
            sb.append("]:  ");
            sb.append(ex.getMessage());

            Helper.log("JSON:  " + sJSON, true);
            Helper.log(sb.toString(), true);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Set Request Property
     *
     * @param connection - Connection
     * @param headers    - List of headers
     */
    public static void setRequestProperty(URLConnection connection, List<Parameter> headers) {
        for (Parameter header : headers) {
            connection.setRequestProperty(header.param, header.value);
        }
    }

}
