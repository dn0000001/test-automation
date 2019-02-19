package com.taf.automation.api;

import com.google.gson.Gson;
import com.taf.automation.ui.support.testng.Attachment;

public class JsonUtils {
    private JsonUtils() {
        //
    }

    /**
     * Get a Gson with correct configuration for our purposes
     *
     * @return Gson
     */
    @SuppressWarnings("squid:S1488")
    public static Gson getGson() {
        Gson gson = new Gson();

        //
        // Add any configurations of Gson as necessary here
        //

        return gson;
    }

    /**
     * Attach the JSON to the report
     *
     * @param json       - JSON to be attached
     * @param attachName - Attachment name
     */
    public static void attachJsonToReport(String json, String attachName) {
        new Attachment().withTitle(attachName).withType("text/html").withFile(json.getBytes()).build();
    }

}
