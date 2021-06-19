package com.taf.automation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taf.automation.ui.support.testng.Attachment;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.xml.sax.InputSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * API Utilities
 */
@SuppressWarnings({"squid:S00112", "java:S3252"})
public class ApiUtils {
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";
    public static final BasicStatusLine OK = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_OK, "OK");

    private ApiUtils() {
        //
    }

    /**
     * Beautify XML
     *
     * @param inXML - XML to be beautified
     * @return String
     */
    @SuppressWarnings({"java:S2755", "java:S3252"})
    public static String prettifyXML(String inXML) {
        try {
            Transformer trans = SAXTransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(inXML.getBytes())));
            StreamResult sRes = new StreamResult(new ByteArrayOutputStream());
            trans.transform(xmlSource, sRes);
            String xmlOut = new String(((ByteArrayOutputStream) sRes.getOutputStream()).toByteArray());
            xmlOut = xmlOut.replace("\"UTF-8\"?><", "\"UTF-8\"?>\n<");
            xmlOut = xmlOut.replace(" xmlns:", "\n xmlns:");
            return xmlOut;
        } catch (Exception e) {
            return inXML;
        }
    }

    /**
     * Attach Data to report
     *
     * @param xml  - XML
     * @param name - Shown in report as name of attachment
     */
    public static void attachDataXml(String xml, String name) {
        new Attachment().withTitle(name).withType("text/xml").withFile(xml.getBytes()).build();
    }

    /**
     * Attach Data to report
     *
     * @param json - JSON
     * @param name - Shown in report as name of attachment
     */
    public static void attachDataJson(String json, String name) {
        new Attachment().withTitle(name).withType("application/json").withFile(json.getBytes()).build();
    }

    /**
     * Attach Data to report
     *
     * @param text - Text
     * @param name - Shown in report as name of attachment
     */
    public static void attachDataText(String text, String name) {
        new Attachment().withTitle(name).withType("text/plain").withFile(text.getBytes()).build();
    }

    /**
     * Attach Data to report<BR>
     * The following are common MIME types automation may encounter and want to attach:
     * <table style="width:100%" border="1">
     *  <thead>
     *      <tr>
     *          <th>Extension</th>
     *          <th>Kind of document</th>
     *          <th>MIME Type</th>
     *      </tr>
     *  </thead>
     *  <tbody>
     *      <tr>
     *          <td>.csv</td>
     *          <td>Comma-separated values (CSV)</td>
     *          <td>text/csv</td>
     *      </tr>
     *      <tr>
     *          <td>.doc</td>
     *          <td>Microsoft Word</td>
     *          <td>application/msword</td>
     *      </tr>
     *      <tr>
     *          <td>.docx</td>
     *          <td>Microsoft Word (OpenXML)</td>
     *          <td>application/vnd.openxmlformats-officedocument.wordprocessingml.document</td>
     *      </tr>
     *      <tr>
     *          <td>.gif</td>
     *          <td>Graphics Interchange Format (GIF)</td>
     *          <td>image/gif</td>
     *      </tr>
     *      <tr>
     *          <td>.jpg</td>
     *          <td>JPEG images </td>
     *          <td>image/jpeg</td>
     *      </tr>
     *      <tr>
     *          <td>.png</td>
     *          <td>Portable Network Graphics</td>
     *          <td>image/png</td>
     *      </tr>
     *      <tr>
     *          <td>.pdf</td>
     *          <td>Adobe Portable Document Format (PDF)</td>
     *          <td>application/pdf</td>
     *      </tr>
     *      <tr>
     *          <td>.xls</td>
     *          <td>Microsoft Excel</td>
     *          <td>application/vnd.ms-excel</td>
     *      </tr>
     *      <tr>
     *          <td>.xlsx</td>
     *          <td>Microsoft Excel (OpenXML)</td>
     *          <td>application/vnd.openxmlformats-officedocument.spreadsheetml.sheet</td>
     *      </tr>
     *      <tr>
     *          <td>.zip</td>
     *          <td>ZIP archive</td>
     *          <td>application/zip</td>
     *      </tr>
     *  </tbody>
     * </table>
     * There are more at
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types">Common MIME types</a>
     *
     * @param file     - File to be attached
     * @param mimeType - Mime Type of the file
     * @param name     - Shown in report as name of attachment
     */
    public static void attachDataFile(File file, String mimeType, String name) {
        new Attachment().withTitle(name).withType(mimeType).withFile(file).build();
    }

    /**
     * Convert Object to HttpEntity via XML
     *
     * @param xstream - XStream
     * @param entity  - Object to be converted
     * @return HttpEntity
     */
    public static HttpEntity getXMLHttpEntity(XStream xstream, Object entity) {
        HttpEntity httpEntity;

        try {
            String xml = prettifyXML(xstream.toXML(entity));
            httpEntity = new StringEntity(xml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return httpEntity;
    }

    /**
     * Convert Object to HttpEntity via JSON
     *
     * @param entity - Object to be converted
     * @return HttpEntity
     */
    public static HttpEntity getJSONHttpEntity(Object entity) {
        HttpEntity httpEntity;

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(entity);
            httpEntity = new StringEntity(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return httpEntity;
    }

    /**
     * Convert List to HttpEntity
     *
     * @param params - List of parameters
     * @return HttpEntity
     */
    public static HttpEntity getFormHttpEntity(List<NameValuePair> params) {
        HttpEntity httpEntity;

        try {
            if (params == null) {
                httpEntity = new UrlEncodedFormEntity(new ArrayList<>());
            } else {
                httpEntity = new UrlEncodedFormEntity(params);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return httpEntity;
    }

    /**
     * Add the headers for a SOAP Request
     *
     * @param apiDomainObject - API Domain Object to update with the headers for a SOAP Request
     * @param soapAction      - The SOAP action to be added
     */
    public static void updateForSoap(ApiDomainObject apiDomainObject, String soapAction) {
        apiDomainObject.addHeader(new BasicHeader("Content-Type", "text/xml; charset=utf-8"));
        apiDomainObject.addHeader(new BasicHeader("SOAPAction", soapAction));
    }

    /**
     * Assert Equal by converting objects to XML and comparing each line as a string
     *
     * @param actual   - Actual
     * @param expected - Expected
     */
    public static void assertEqual(Object actual, Object expected) {
        if (actual == null && expected == null) {
            return;
        }

        AssertJUtil.assertThat(actual).isNotNull();
        AssertJUtil.assertThat(expected).isNotNull();

        String[] actualLines = new ApiDomainObject().getXstream().toXML(actual).split("\n");
        String[] expectedLines = new ApiDomainObject().getXstream().toXML(expected).split("\n");
        for (int i = 0; i < expectedLines.length; i++) {
            AssertJUtil.assertThat(actualLines[i]).isEqualTo(expectedLines[i]);
        }
    }

    /**
     * Replace prefix ignoring case
     *
     * @param str     - String to search for the prefix
     * @param prefix  - Prefix to be removed if found
     * @param replace - Replacement Prefix to be used
     * @return String with the prefix replaced
     */
    private static String replacePrefixWithIgnoreCase(String str, String prefix, String replace) {
        if (StringUtils.startsWithIgnoreCase(str, prefix)) {
            return replace + StringUtils.removeStartIgnoreCase(str, prefix);
        }

        return str;
    }

    /**
     * Convert an HTTPS url to an HTTP url
     *
     * @param url - URL to make HTTP
     * @return Removes https:// (ignoring case) from start and replaces with http://
     */
    public static String convertToHTTP(String url) {
        return replacePrefixWithIgnoreCase(url, HTTPS, HTTP);
    }

    /**
     * Convert an HTTP url to an HTTPS url
     *
     * @param url - URL to make HTTPS
     * @return Removes http:// (ignoring case) from start and replaces with https://
     */
    public static String convertToHTTPS(String url) {
        return replacePrefixWithIgnoreCase(url, HTTP, HTTPS);
    }

    /**
     * Get the fields to validate from the expected object<BR><BR>
     * <B>Notes: </B><BR>
     * 1) A <B>null</B> value for a field indicates to skip validate<BR>
     * 2) If expected object is <B>null</B>, then an empty list is returned<BR>
     *
     * @param expected - Expected Object to get the fields to validate
     * @return List of fields to validate based on their value in the expected object
     */
    public static List<Field> getFieldsToValidate(Object expected) {
        List<Field> expectedFieldsToValidate = new ArrayList<>();

        if (expected != null) {
            expectedFieldsToValidate.addAll(Arrays.asList(FieldUtils.getAllFields(expected.getClass())));
            expectedFieldsToValidate.removeIf(field -> isNullField(field, expected));
        }

        return expectedFieldsToValidate;
    }

    /**
     * Check if the specified field is null in the expected object
     *
     * @param field    - Field to check
     * @param expected - Expected Object to check specified field
     * @return true if expected field is null, false otherwise
     */
    private static boolean isNullField(Field field, Object expected) {
        Object expectedValue = readField(field, expected);
        return expectedValue == null;
    }

    /**
     * Get specified field Value from the object
     *
     * @param field - Field to get value
     * @param obj   - Object to get value from
     * @return the specified field value from the object
     */
    public static Object readField(Field field, Object obj) {
        String error = "";
        try {
            return FieldUtils.readField(field, obj, true);
        } catch (Exception ignore) {
            error = ignore.getMessage();
        }

        AssertJUtil.fail("Could not read field (" + field.getName() + ") due to error:  " + error);
        return null;
    }

    /**
     * Configure xStream with all the fields as aliases prefixed with specified prefix + ":"<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>This is useful when dealing with SOAP endpoints as normally they are something like <B>abc:field</B>.
     * This normally requires using XStreamAlias on each of the fields.</LI>
     * </OL>
     *
     * @param xStream     - xStream to configure the aliases
     * @param definedIn   - the type that declares the field
     * @param aliasPrefix - the prefix to append to the fields (a colon will be added)
     */
    public static void aliasField(XStream xStream, Class<?> definedIn, String aliasPrefix) {
        aliasField(xStream, definedIn, aliasPrefix, new HashSet<>());
    }

    /**
     * Configure xStream with all the fields as aliases prefixed with specified prefix + ":"<BR>
     * <B>Notes:</B>
     * <OL>
     * <LI>This is useful when dealing with SOAP endpoints as normally they are something like <B>abc:field</B>.
     * This normally requires using XStreamAlias on each of the fields.</LI>
     * </OL>
     *
     * @param xStream       - xStream to configure the aliases
     * @param definedIn     - the type that declares the field
     * @param aliasPrefix   - the prefix to append to the fields (a colon will be added)
     * @param excludeFields - fields to exclude in the alias configuration
     */
    public static void aliasField(XStream xStream, Class<?> definedIn, String aliasPrefix, Set<String> excludeFields) {
        for (Field item : FieldUtils.getAllFields(definedIn)) {
            if (!excludeFields.contains(item.getName())) {
                xStream.aliasField(aliasPrefix + ":" + item.getName(), definedIn, item.getName());
            }
        }
    }

    /**
     * Get XStream configured to ignore the namespace when processing<BR>
     * <B>Note: </B> This should be used with a soap response to be able to map to objects with limited annotations
     *
     * @param apiDomainObject - API Domain Object to get class to set process annotations for
     * @param namespace       - Namespace to be removed
     * @return XStream
     */
    public static XStream getXStream(ApiDomainObject apiDomainObject, String namespace) {
        QNameMap qmap = new QNameMap();
        qmap.setDefaultNamespace(namespace);
        qmap.setDefaultPrefix("");
        StaxDriver staxDriver = new StaxDriver(qmap);
        XStream xstream = new XStream(staxDriver);
        xstream.processAnnotations(apiDomainObject.getClass());
        return xstream;
    }

}
