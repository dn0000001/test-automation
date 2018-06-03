package com.automation.common.api.domainObjects;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;

import ru.yandex.qatools.allure.annotations.Step;

import com.taf.automation.api.clients.ApiClient;
import com.taf.automation.api.ApiDomainObject;
import com.taf.automation.api.clients.JsonResponse;
import com.taf.automation.api.ParametersType;
import com.taf.automation.api.ReturnType;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.WsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Domain Object to work with http://jsonip.com/
 */
@XStreamAlias("json-ip-do")
public class JsonIpDO extends ApiDomainObject {
    Response response;

    // Here you create an entity for the response
    private static class Response {
        // The Expected values to verify against
        BasicStatusLine status;
        String about;
        String pro;

        // Here is the actual response entity
        @SuppressWarnings("rawtypes")
        @XStreamOmitField
        JsonResponse ip;

        @Step("Validate Status")
        private void validateStatus() {
            Helper.validateStatus(ip.getStatus(), status);
        }

        @Step("Validate Response")
        private void validateResponse() {
            String json = ip.getEntityAsString();
            Map<String, Object> actual = WsUtils.toMap(json);
            String actualIP = String.valueOf(actual.get("ip"));
            String actualAbout = String.valueOf(actual.get("about"));
            String actualPro = String.valueOf(actual.get("Pro!"));

            Helper.log("Actual IP:  " + actualIP);
            assertThat("Invalid IP Address", Pattern.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$", actualIP), Matchers.equalTo(true));
            assertThat("About Incorrect", actualAbout, Matchers.equalTo(about));
            assertThat("Pro Incorrect", actualPro, Matchers.equalTo(pro));
        }
    }

    @SuppressWarnings("rawtypes")
    @Step("Get IP")
    public void getIP() {
        String resourcePath = "/";
        ApiClient client = getClient();
        client.setParametersType(ParametersType.GENERAL);
        client.setReturnType(ReturnType.JSON);
        response.ip = (JsonResponse) client.get(resourcePath, null, null);
    }

    public void validateStatus() {
        response.validateStatus();
    }

    public void validateResponse() {
        response.validateResponse();
    }

}
