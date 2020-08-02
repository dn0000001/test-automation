package com.automation.common.api.domainObjects;

import com.taf.automation.api.ApiDomainObject;
import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.ParametersType;
import com.taf.automation.api.ReturnType;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.ui.support.util.Helper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.http.message.BasicStatusLine;
import ru.yandex.qatools.allure.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Domain Object to work with http://www.holidaywebservice.com//HolidayService_v2/HolidayService2.asmx?op=GetHolidayDate
 */
@XStreamAlias("get-holiday-date-do")
public class SoapGetHolidayDateDO extends ApiDomainObject {
    private Request request;
    private Response response;

    /**
     * Here you create an entity for the request.  We are modeling the following:
     * <pre>
     * POST /HolidayService_v2/HolidayService2.asmx HTTP/1.1
     * Host: www.holidaywebservice.com
     * Content-Type: text/xml; charset=utf-8
     * Content-Length: <font class="value">length</font>
     * SOAPAction: "http://www.holidaywebservice.com/HolidayService_v2/GetHolidayDate"
     *
     * &lt;?xml version="1.0" encoding="utf-8"?&gt;
     * &lt;soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"&gt;
     *   &lt;soap:Body&gt;
     *     &lt;GetHolidayDate xmlns="http://www.holidaywebservice.com/HolidayService_v2/"&gt;
     *       &lt;countryCode&gt;<font class="value">Canada</font> or <font class="value">GreatBritain</font> or <font class="value">IrelandNorthern</font> or <font class="value">IrelandRepublicOf</font> or <font class="value">Scotland</font> or <font class="value">UnitedStates</font>&lt;/countryCode&gt;
     *       &lt;holidayCode&gt;<font class="value">string</font>&lt;/holidayCode&gt;
     *       &lt;year&gt;<font class="value">int</font>&lt;/year&gt;
     *     &lt;/GetHolidayDate&gt;
     *   &lt;/soap:Body&gt;
     * &lt;/soap:Envelope&gt;</pre>
     */
    private static class Request {
        @XStreamAlias("soap:Envelope")
        private SOAPEnvelopeRequest envelope;
    }

    @XStreamAlias("soap:Envelope")
    private static class SOAPEnvelopeRequest {
        @XStreamAlias("xmlns:xsi")
        @XStreamAsAttribute
        private String xsi;

        @XStreamAlias("xmlns:xsd")
        @XStreamAsAttribute
        private String xsd;

        @XStreamAlias("xmlns:soap")
        @XStreamAsAttribute
        private String soap;

        @XStreamAlias("soap:Body")
        private SOAPBodyRequest body;
    }

    private static class SOAPBodyRequest {
        @XStreamAlias("GetHolidayDate")
        private GetHolidayDateRequest add;
    }

    @SuppressWarnings("squid:S1068")
    private static class GetHolidayDateRequest {
        @XStreamAsAttribute
        private String xmlns;

        private String countryCode;
        private String holidayCode;
        private int year;
    }

    /**
     * We are modeling the following:
     * <pre>
     * HTTP/1.1 200 OK
     * Content-Type: text/xml; charset=utf-8
     * Content-Length: <font class="value">length</font>
     *
     * &lt;?xml version="1.0" encoding="utf-8"?&gt;
     * &lt;soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"&gt;
     *   &lt;soap:Body&gt;
     *     &lt;GetHolidayDateResponse xmlns="http://www.holidaywebservice.com/HolidayService_v2/"&gt;
     *       &lt;GetHolidayDateResult&gt;<font class="value">dateTime</font>&lt;/GetHolidayDateResult&gt;
     *     &lt;/GetHolidayDateResponse&gt;
     *   &lt;/soap:Body&gt;
     * &lt;/soap:Envelope&gt;</pre>
     */
    @XStreamAlias("soap:Envelope")
    public static class SOAPEnvelopeResponse {
        @XStreamAlias("xmlns:xsi")
        @XStreamAsAttribute
        private String xsi;

        @XStreamAlias("xmlns:xsd")
        @XStreamAsAttribute
        private String xsd;

        @XStreamAlias("xmlns:soap")
        @XStreamAsAttribute
        private String soap;

        @XStreamAlias("soap:Body")
        private SOAPBodyResponse body;
    }

    private static class SOAPBodyResponse {
        @XStreamAlias("GetHolidayDateResponse")
        private GetHolidayDateResponse dateResponse;
    }

    private static class GetHolidayDateResponse {
        @XStreamAsAttribute
        private String xmlns;

        @XStreamAlias("GetHolidayDateResult")
        private String date;
    }

    // Here you create an entity for the response
    private static class Response {
        // The Expected values to verify against
        private BasicStatusLine status;

        private String expectedDate;

        @XStreamOmitField
        private GenericHttpResponse<SOAPEnvelopeResponse> theResponse;

        @Step("Validate Status")
        private void validateStatus() {
            Helper.validateStatus(theResponse.getStatus(), status);
        }

        @Step("Validate Response")
        private void validateResponse() {
            assertThat("Response", theResponse, notNullValue());
            assertThat("Response Entity", theResponse.getEntity(), notNullValue());
            assertThat("Response Entity - Body", theResponse.getEntity().body, notNullValue());
            assertThat("Response Entity - Body - Add", theResponse.getEntity().body.dateResponse, notNullValue());
            assertThat("Add Result", theResponse.getEntity().body.dateResponse.date, equalTo(expectedDate));
        }

    }

    private Request getRequest() {
        if (request == null) {
            request = new Request();
        }

        return request;
    }

    private Response getResponse() {
        if (response == null) {
            response = new Response();
        }

        return response;
    }

    public void sendRequest() {
        String resourcePath = "/HolidayService_v2/HolidayService2.asmx";
        Helper.log(resourcePath);

        getClient().getXstream().alias("soap:Envelope", SOAPEnvelopeResponse.class);
        getClient().setParametersType(ParametersType.XML);
        getClient().setReturnType(ReturnType.XML);
        getClient().setCustomContentType("text/xml; charset=utf-8");
        ApiUtils.updateForSoap(this, "http://www.holidaywebservice.com/HolidayService_v2/GetHolidayDate");
        getResponse().theResponse = getClient().post(resourcePath, getRequest().envelope, SOAPEnvelopeResponse.class, getHeaders());
    }

    public void validateStatus() {
        response.validateStatus();
    }

    public void validateResponse() {
        response.validateResponse();
    }

}
