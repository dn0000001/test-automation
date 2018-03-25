package com.taf.automation.api;

import com.google.gson.Gson;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;

public class JsonUtils {
    /**
     * Get a Gson with correct configuration for our purposes
     *
     * @return Gson
     */
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
        MakeAttachmentEvent ev = new MakeAttachmentEvent(json.getBytes(), attachName, "text/html");
        Allure.LIFECYCLE.fire(ev);
    }

}
