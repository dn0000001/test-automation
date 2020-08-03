package com.automation.common.api.domainObjects;

import com.taf.automation.api.MicroServiceDomainObject;
import com.taf.automation.api.clients.MicroServiceResponse;
import com.taf.automation.ui.support.util.Helper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicStatusLine;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@XStreamAlias("multi-part-entity-do")
public class MultipartEntityDO extends MicroServiceDomainObject {
    // Variable to hold the request parameters
    private RequestParameters request;

    // Here you create an entity for the response
    private Response response;

    private static class ResponseBody {
        private Integer statusCode;
        private String message;
    }

    private static class RequestParameters {
        private String filePath;

        public HttpEntity getEntity() {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            if (filePath != null) {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
                File file = new File(filePath);
                assertThat("Image file '" + filePath + "' was not found!", is, notNullValue());
                try {
                    FileUtils.copyInputStreamToFile(is, file);
                    is.close();
                } catch (IOException ignored) {
                    //
                }

                builder.addBinaryBody("file", file);
            }

            return builder.build();
        }

    }

    // Here you create an entity for the response
    private static class Response {
        private BasicStatusLine status;

        // Here is your response entity
        @XStreamOmitField
        private MicroServiceResponse<ResponseBody> actual;
    }

    private RequestParameters getRequest() {
        if (request == null) {
            request = new RequestParameters();
        }

        return request;
    }

    private Response getResponse() {
        if (response == null) {
            response = new Response();
        }

        return response;
    }

    /**
     * Get the resource path for the request
     *
     * @return String
     */
    private String getResourcePath() {
        return "/upload";
    }

    @Step("POST Request Parameters")
    private void writePostParametersToReport() {
        if (getRequest().filePath != null) {
            Helper.log("Location to image:  " + getRequest().filePath);
        }
    }

    /**
     * Send the request
     */
    public void sendRequest() {
        String resourcePath = getResourcePath();
        Helper.log("POST " + resourcePath);
        writePostParametersToReport();
        HttpEntity entity = getRequest().getEntity();
        setCustomContentType(""); // Necessary to ensure Content-Type header is not overwritten
        getResponse().actual = getClient().post(resourcePath, entity, ResponseBody.class, getHeaders());
    }

}
