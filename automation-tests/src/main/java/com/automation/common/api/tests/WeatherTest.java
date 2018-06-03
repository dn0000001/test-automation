package com.automation.common.api.tests;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.automation.common.data.Weather;
import com.automation.common.api.domainObjects.WeatherDO;
import com.taf.automation.ui.support.testng.AllureTestNGListener;

/**
 * Sample API test. Website: http://wsf.cdyne.com
 */
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
        assertThat(weatherObj.getTemperature(), Matchers.notNullValue());
        assertThat(weatherObj.isSuccess(), Matchers.is(true));
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
        assertThat(weatherObj.getTemperature(), Matchers.notNullValue());
        assertThat(weatherObj.isSuccess(), Matchers.is(true));
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
        assertThat(weatherObj.getTemperature(), Matchers.notNullValue());
        assertThat(weatherObj.isSuccess(), Matchers.is(true));
    }

}
