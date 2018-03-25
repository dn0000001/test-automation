package com.taf.automation.ui.app.tests;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.taf.automation.data.Weather;
import com.taf.automation.ui.support.Helper;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Unit testing things using Guava
 */
public class GuavaTest extends TestNGBase {
    private Function<Weather, Weather> convertFunction() {
        return input -> {
            Weather converted = new Weather();

            converted.setWeatherID(input.getWeatherID());
            converted.setDescription(input.getDescription());
            converted.setTemperature(input.getTemperature());

            return converted;
        };
    }

    private Weather getRandom(int id, String description, int temperature) {
        Weather obj = new Weather();

        obj.setWeatherID(id);
        obj.setDescription(description);
        obj.setTemperature(temperature);

        // All other fields are set to random values
        obj.setSuccess(Rand.randomBoolean());
        obj.setResponseText(Rand.alphanumeric(10, 20));
        obj.setState(Rand.letters(5, 10));
        obj.setCity(Rand.letters(5, 10));
        obj.setWeatherStationCity(Rand.letters(5, 10));
        obj.setRelativeHumidity(Rand.randomRange(0, 100));
        obj.setWind(Rand.alphanumeric(10, 20));
        obj.setPressure(Rand.alphanumeric(10, 20));
        obj.setVisibility(Rand.alphanumeric(10, 20));
        obj.setWindChill(Rand.alphanumeric(10, 20));
        obj.setRemarks(Rand.alphanumeric(10, 20));

        return obj;
    }

    @Features("Guava")
    @Stories("Transforming a single object")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    public void transformObjectTest() {
        int id = Rand.randomRange(0, 1000);
        String description = Rand.alphanumeric(10, 20);
        int temperature = Rand.randomRange(-20, 100);
        Weather actual = getRandom(id, description, temperature);
        Weather expected = getRandom(id, description, temperature);
        Helper.assertThat(convertFunction().apply(actual), convertFunction().apply(expected));

        // This should fail without transformation
        // Helper.assertThat(actual, expected);
    }

    @Features("Guava")
    @Stories("Transforming a list")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    public void transformListTest() {
        //
        // Generate some data for the test
        //
        List<Weather> actual = new ArrayList<>();
        List<Weather> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //
            // We want both object to be the same for the transformation fields but the other fields will be
            // different.
            //
            String description = Rand.alphanumeric(10, 20);
            int temperature = Rand.randomRange(0, 100);

            actual.add(getRandom(i, description, temperature));
            expected.add(getRandom(i, description, temperature));
        }

        // This should fail without transformation
        // Helper.assertThat(actual, expected);

        //
        // Transform both lists to be the same format (and equal based on this format)
        //
        List<Weather> transformedActual = Lists.transform(actual, convertFunction());
        List<Weather> transformedExpected = Lists.transform(expected, convertFunction());
        Helper.assertThat(transformedActual, transformedExpected);
    }

}
