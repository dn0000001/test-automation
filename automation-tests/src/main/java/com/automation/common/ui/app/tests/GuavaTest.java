package com.automation.common.ui.app.tests;

import com.automation.common.data.Weather;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.testng.Retry;
import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Unit testing things using Guava
 */
@Listeners(AllureTestNGListener.class)
public class GuavaTest {
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
    @SuppressWarnings("java:S112")
    public void transformObjectTest() {
        int id = Rand.randomRange(0, 1000);
        String description = Rand.alphanumeric(10, 20);
        int temperature = Rand.randomRange(-20, 100);
        Weather actual = getRandom(id, description, temperature);
        Weather expected = getRandom(id, description, temperature);
        Helper.assertThat(convertFunction().apply(actual), convertFunction().apply(expected));

        try {
            // This should fail without transformation
            Helper.assertThat(actual, expected);
            throw new RuntimeException("Transformation did NOT fail as expected");
        } catch (AssertionError ae) {
            Helper.log("Transformation failed as expected", true);
        }
    }

    @Features("Guava")
    @Stories("Transforming a list")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    @Retry
    @SuppressWarnings("java:S112")
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

        try {
            // This should fail without transformation
            Helper.assertThat(actual, expected);
            throw new RuntimeException("Transformation did NOT fail as expected");
        } catch (AssertionError ae) {
            Helper.log("Transformation failed as expected", true);
        }

        //
        // Transform both lists to be the same format (and equal based on this format)
        //
        List<Weather> transformedActual = Lists.transform(actual, convertFunction());
        List<Weather> transformedExpected = Lists.transform(expected, convertFunction());
        Helper.assertThat(transformedActual, transformedExpected);

        //
        // Print a few objects to the console just to check that method does not throw an exception
        //
        Helper.print(transformedActual.get(0), transformedExpected.get(0));
    }

    @Features("Guava")
    @Stories("Map Difference from a list")
    @Severity(SeverityLevel.TRIVIAL)
    @Test
    @Retry
    public void mapDifferenceTest() {
        final String textExt = ".txt";
        final String excelExt = ".xls";
        final String file1 = "file1";
        final String file2 = "/home/file2";
        final String file3 = "C:\\temp\\file3";
        final String file4 = "file4";
        final String match = "match";
        final String lhsOnly = "lhs-only";
        final String rhsOnly = "rhs-only";

        List<String> lhsFiles = new ArrayList<>();
        lhsFiles.add(file1 + textExt);
        lhsFiles.add(file2 + textExt);
        lhsFiles.add(file3 + textExt);
        lhsFiles.add(file4 + textExt);
        lhsFiles.add(match + textExt);
        lhsFiles.add(lhsOnly + textExt);

        List<String> rhsFiles = new ArrayList<>();
        rhsFiles.add(match + textExt);
        rhsFiles.add(rhsOnly + excelExt);
        rhsFiles.add(file4 + excelExt);
        rhsFiles.add(file3 + excelExt);
        rhsFiles.add(file2 + excelExt);
        rhsFiles.add(file1 + excelExt);

        Map<String, String> lhsMap = lhsFiles
                .stream()
                .collect(Collectors.toMap(FilenameUtils::getBaseName, item -> item, (lhs, rhs) -> rhs));
        Map<String, String> rhsMap = rhsFiles
                .stream()
                .collect(Collectors.toMap(FilenameUtils::getBaseName, item -> item, (lhs, rhs) -> rhs));

        MapDifference<String, String> diff = Maps.difference(lhsMap, rhsMap);
        Map<String, String> onlyLeft = diff.entriesOnlyOnLeft();
        assertThat("Only Left", onlyLeft.get(lhsOnly), equalTo(lhsOnly + textExt));

        Map<String, String> onlyRight = diff.entriesOnlyOnRight();
        assertThat("Only Right", onlyRight.get(rhsOnly), equalTo(rhsOnly + excelExt));

        Map<String, String> common = diff.entriesInCommon();
        assertThat("Common", common.get(match), equalTo(match + textExt));

        Map<String, MapDifference.ValueDifference<String>> mismatch = diff.entriesDiffering();
        MapDifference.ValueDifference<String> diffFile1 = mismatch.get(FilenameUtils.getBaseName(file1));
        assertThat(file1, diffFile1, notNullValue());
        assertThat(file1 + textExt, diffFile1.leftValue(), equalTo(file1 + textExt));
        assertThat(file1 + excelExt, diffFile1.rightValue(), equalTo(file1 + excelExt));

        MapDifference.ValueDifference<String> diffFile2 = mismatch.get(FilenameUtils.getBaseName(file2));
        assertThat(file2, diffFile2, notNullValue());
        assertThat(file2 + textExt, diffFile2.leftValue(), equalTo(file2 + textExt));
        assertThat(file2 + excelExt, diffFile2.rightValue(), equalTo(file2 + excelExt));

        MapDifference.ValueDifference<String> diffFile3 = mismatch.get(FilenameUtils.getBaseName(file3));
        assertThat(file3, diffFile3, notNullValue());
        assertThat(file3 + textExt, diffFile3.leftValue(), equalTo(file3 + textExt));
        assertThat(file3 + excelExt, diffFile3.rightValue(), equalTo(file3 + excelExt));

        MapDifference.ValueDifference<String> diffFile4 = mismatch.get(FilenameUtils.getBaseName(file4));
        assertThat(file4, diffFile4, notNullValue());
        assertThat(file4 + textExt, diffFile4.leftValue(), equalTo(file4 + textExt));
        assertThat(file4 + excelExt, diffFile4.rightValue(), equalTo(file4 + excelExt));
    }

}
