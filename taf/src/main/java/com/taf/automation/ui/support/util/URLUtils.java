package com.taf.automation.ui.support.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Utilities class to work with URIBuilder
 */
public class URLUtils {
    private URLUtils() {
        //
    }

    /**
     * Use URL to return URIBuilder
     *
     * @param url - URL to be used to initialize the URIBuilder
     * @return URIBuilder or assertion failure if initialization fails
     */
    public static URIBuilder getURI(String url) {
        boolean validURL;
        URIBuilder uri;
        try {
            uri = new URIBuilder(url);
            validURL = true;
        } catch (Exception ex) {
            uri = null;
            validURL = false;
        }

        assertThat("Invalid URL:  " + url, validURL);
        return uri;
    }

    /**
     * Set path only if value is <B>not</B> null<BR>
     * <B>Notes: </B> Slash will be added if necessary<BR>
     *
     * @param uri  - URIBuilder to update
     * @param path - Uses the toString() method to convert to string value
     * @param <T>  - Object Type
     */
    public static <T> void setPath(URIBuilder uri, T path) {
        if (path == null) {
            return;
        }

        uri.setPath("/" + StringUtils.removeStart(path.toString(), "/"));
    }

    /**
     * Append to the path only if the value is <B>not</B> null<BR>
     * <B>Notes: </B> Slash will be added if necessary<BR>
     *
     * @param uri  - URIBuilder to update
     * @param path - Uses the toString() method to convert to string value
     * @param <T>  - Object Type
     */
    public static <T> void appendPath(URIBuilder uri, T path) {
        if (path == null) {
            return;
        }

        uri.setPath(StringUtils.defaultString(uri.getPath()) + "/" + StringUtils.removeStart(path.toString(), "/"));
    }

    /**
     * Add parameter only if value is <B>not</B> null
     *
     * @param uri - URIBuilder to update
     * @param nvp - Parameter
     */
    public static void addParameter(URIBuilder uri, NameValuePair nvp) {
        if (nvp == null) {
            return;
        }

        addParameter(uri, nvp.getName(), nvp.getValue());
    }

    /**
     * Add parameter only if value is <B>not</B> null
     *
     * @param uri   - URIBuilder to update
     * @param param - Parameter name
     * @param value - Uses the toString() method to convert to string value
     * @param <T>   - Object Type
     */
    public static <T> void addParameter(URIBuilder uri, String param, T value) {
        if (value == null) {
            return;
        }

        uri.addParameter(param, value.toString());
    }

    /**
     * Add parameters only if their value is <B>not</B> null
     *
     * @param uri  - URIBuilder to update
     * @param nvps - List of Parameters to add
     */
    public static void addParameters(URIBuilder uri, List<NameValuePair> nvps) {
        if (nvps == null || nvps.isEmpty()) {
            return;
        }

        for (NameValuePair parameter : nvps) {
            addParameter(uri, parameter);
        }
    }

}

