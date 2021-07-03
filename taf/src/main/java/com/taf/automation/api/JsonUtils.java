package com.taf.automation.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.taf.automation.ui.support.testng.Attachment;
import org.apache.commons.codec.binary.Base64;

import java.lang.reflect.Type;

public class JsonUtils {
    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decodeBase64(json.toString());
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeBase64String(src));
        }
    }

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
        Gson gson = new Gson()
                .newBuilder()
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                .create();

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
