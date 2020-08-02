package com.automation.common.api.domainObjects;

import com.automation.common.converters.WeatherConverter;
import com.automation.common.data.Weather;
import com.taf.automation.api.ApiDomainObject;
import com.taf.automation.api.ApiUtils;
import com.taf.automation.api.rest.GenericHttpResponse;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.VTD_XML;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Domain Object to work with http://wsf.cdyne.com/WeatherWS/Weather.asmx?op=GetCityWeatherByZIP
 */
@XStreamAlias("weather-do")
public class WeatherDO extends ApiDomainObject {
    Request request;
    Response response;

    // Here you create an entity for the request
    private static class Request {
        private String zip;
    }

    // Here you create an entity for the response
    private static class Response {
        // The Expected values to verify against
        BasicStatusLine status;

        // Here is the actual response entity
        @SuppressWarnings("rawtypes")
        @XStreamOmitField
        GenericHttpResponse weather;

        @Step("Validate Status")
        private void validateStatus() {
            Helper.validateStatus(weather.getStatus(), status);
        }

        @Step("Validate Response")
        private void validateResponse() {
            String temperature = null;
            boolean success = false;

            try {
                VTD_XML xml = new VTD_XML(weather.getEntityAsString().getBytes());
                temperature = xml.getNodeValue("//Temperature", null);
                success = xml.getNodeValue("//Success", false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            assertThat(temperature, Matchers.notNullValue());
            assertThat(success, Matchers.is(true));
        }
    }

    @SuppressWarnings("rawtypes")
    @Step("Get City Weather By ZIP")
    public void getCityWeatherByZIP() {
        String resourcePath = "/WeatherWS/Weather.asmx/GetCityWeatherByZIP?ZIP=" + request.zip;
        Helper.log(resourcePath);
        response.weather = getClient().get(resourcePath, null, null);
    }

    @SuppressWarnings("rawtypes")
    @Step("POST City Weather By ZIP")
    public void postCityWeatherByZIP() {
        String resourcePath = "/WeatherWS/Weather.asmx/GetCityWeatherByZIP";
        Helper.log(resourcePath);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("ZIP", request.zip));
        HttpEntity entity = ApiUtils.getFormHttpEntity(params);
        response.weather = getClient().post(resourcePath, entity, null, null);
    }

    @SuppressWarnings("rawtypes")
    @Step("SOAP City Weather By ZIP")
    public void soapCityWeatherByZIP() {
        String resourcePath = "/WeatherWS/Weather.asmx";
        Helper.log(resourcePath);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("<soap:Body>");
        sb.append("<GetCityWeatherByZIP xmlns=\"http://ws.cdyne.com/WeatherWS/\">");
        sb.append("<ZIP>");
        sb.append(request.zip);
        sb.append("</ZIP>");
        sb.append("</GetCityWeatherByZIP>");
        sb.append("</soap:Body>");
        sb.append("</soap:Envelope>");

        HttpEntity entity;
        try {
            entity = new StringEntity(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ApiUtils.updateForSoap(this, "http://ws.cdyne.com/WeatherWS/GetCityWeatherByZIP");
        response.weather = getClient().post(resourcePath, entity, null, getHeaders());
    }

    public void validateStatus() {
        response.validateStatus();
    }

    public void validateResponse() {
        response.validateResponse();
    }

    public Weather getResponseWeather() {
        XStream xstream = getXstream();
        xstream.alias("WeatherReturn", Weather.class);
        xstream.alias("soap:Envelope", Weather.class);
        xstream.registerConverter(new WeatherConverter());
        return (Weather) xstream.fromXML(response.weather.getEntityAsString());
    }

}
