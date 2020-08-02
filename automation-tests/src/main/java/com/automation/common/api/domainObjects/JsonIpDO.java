package com.automation.common.api.domainObjects;

import com.google.gson.annotations.SerializedName;
import com.taf.automation.api.ApiDomainObject;
import com.taf.automation.api.ParametersType;
import com.taf.automation.api.ReturnType;
import com.taf.automation.api.clients.ApiClient;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.WsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.http.message.BasicStatusLine;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Domain Object to work with http://jsonip.com/
 */
@XStreamAlias("json-ip-do")
public class JsonIpDO extends ApiDomainObject {
    private Response response;

    private static class IP_Details {
        private String ip;

        @SerializedName("geo-ip")
        private String geoIP;

        @SerializedName("API Help")
        private String apiHelp;
    }

    // Here you create an entity for the response
    private static class Response {
        // The Expected values to verify against
        BasicStatusLine status;
        String geoIP;
        String apiHelp;

        // Here is the actual response entity
        @XStreamOmitField
        GenericHttpResponse<IP_Details> ip;

        @Step("Validate Status")
        private void validateStatus() {
            Helper.validateStatus(ip.getStatus(), status);
        }

        @Step("Validate Response")
        private void validateResponse() {
            //
            // Accepted way to do validations
            //

            IP_Details actualDetails = ip.getEntity();
            assertThat("Response Entity", actualDetails, notNullValue());
            assertThat("Invalid IP Address", Pattern.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$", actualDetails.ip), equalTo(true));
            assertThat("GEO IP Incorrect", actualDetails.geoIP, equalTo(geoIP));
            assertThat("API Help Incorrect", actualDetails.apiHelp, equalTo(apiHelp));

            //
            // Not Recommended way just to show another to get the information
            //

            String json = ip.getEntityAsString();
            Map<String, Object> actual = WsUtils.toMap(json);
            String actualIP = String.valueOf(actual.get("ip"));
            String actualGeoIp = String.valueOf(actual.get("geo-ip"));
            String actualApiHelp = String.valueOf(actual.get("API Help"));

            Helper.log("Actual IP:  " + actualIP);
            assertThat("Invalid IP Address", Pattern.matches("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$", actualIP), equalTo(true));
            assertThat("GEO IP Incorrect", actualGeoIp, equalTo(geoIP));
            assertThat("API Help Incorrect", actualApiHelp, equalTo(apiHelp));
        }
    }

    @Step("Get IP")
    public void getIP() {
        String resourcePath = "/";
        ApiClient client = getClient();
        client.setParametersType(ParametersType.GENERAL);
        client.setReturnType(ReturnType.JSON);
        response.ip = client.get(resourcePath, IP_Details.class, getHeaders());
    }

    public void validateStatus() {
        response.validateStatus();
    }

    public void validateResponse() {
        response.validateResponse();
    }

}
