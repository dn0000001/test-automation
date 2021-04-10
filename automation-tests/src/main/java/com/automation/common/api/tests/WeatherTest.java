package com.automation.common.api.tests;

import com.automation.common.api.domainObjects.WeatherDO;
import com.automation.common.data.Weather;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Sample API test. Website: http://wsf.cdyne.com
 */
@SuppressWarnings("java:S3252")
@Listeners(AllureTestNGListener.class)
public class WeatherTest {
    @Features("Weather")
    @Stories("GET City Weather By ZIP")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-weather")
    @Test
    public void getCityWeatherByZIP(@Optional("data/api/GetCityWeatherByZIP_TestData.xml") String dataSet) {
        WeatherDO weather = new WeatherDO().fromResource(dataSet);
        weather.getCityWeatherByZIP();
        weather.validateStatus();
        weather.validateResponse();

        Weather weatherObj = weather.getResponseWeather();
        AssertJUtil.assertThat(weatherObj.getTemperature()).isNotNull();
        AssertJUtil.assertThat(weatherObj.isSuccess()).isTrue();
    }

    @Features("Weather")
    @Stories("POST City Weather By ZIP")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-weather")
    @Test
    public void postCityWeatherByZIP(@Optional("data/api/GetCityWeatherByZIP_TestData.xml") String dataSet) {
        WeatherDO weather = new WeatherDO().fromResource(dataSet);
        weather.postCityWeatherByZIP();
        weather.validateStatus();
        weather.validateResponse();

        Weather weatherObj = weather.getResponseWeather();
        AssertJUtil.assertThat(weatherObj.getTemperature()).isNotNull();
        AssertJUtil.assertThat(weatherObj.isSuccess()).isTrue();
    }

    @Features("Weather")
    @Stories("SOAP City Weather By ZIP")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-weather")
    @Test
    public void soapCityWeatherByZIP(@Optional("data/api/GetCityWeatherByZIP_TestData.xml") String dataSet) {
        WeatherDO weather = new WeatherDO().fromResource(dataSet);
        weather.soapCityWeatherByZIP();
        weather.validateStatus();
        weather.validateResponse();

        Weather weatherObj = weather.getResponseWeather();
        AssertJUtil.assertThat(weatherObj.getTemperature()).isNotNull();
        AssertJUtil.assertThat(weatherObj.isSuccess()).isTrue();
    }

}
