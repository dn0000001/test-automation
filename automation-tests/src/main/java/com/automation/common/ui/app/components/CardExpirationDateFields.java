package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * A page object that acts as a component
 */
@SuppressWarnings({"squid:MaximumInheritanceDepth", "java:S3252"})
public class CardExpirationDateFields extends ComponentPO {
    @FindBy(css = "[name$='ccexp_mm']")
    private SelectEnhanced month;

    @FindBy(css = "[name$='ccexp_yy']")
    private SelectEnhanced year;

    public CardExpirationDateFields() {
        super();
    }

    public CardExpirationDateFields(TestContext context) {
        super(context);
    }

    @Override
    public boolean hasData() {
        return Utils.isNotBlank(month)
                || Utils.isNotBlank(year)
                ;
    }

    @Step("Select Month")
    public void selectMonth() {
        setElementValueV2(month);
    }

    @Step("Select Year")
    public void selectYear() {
        setElementValueV2(year);
    }

    @Override
    @Step("Fill Card Expiration Date Fields")
    public void fill() {
        selectMonth();
        selectYear();
    }

    @Override
    public void validate() {
        // No actions required to validate
    }

    public void updateMonthTestData(String value) {
        month.initializeData(value, null, null);
    }

    @Step("Validate Year - SelectEnhanced.setValue")
    public void validateYearSelectEnhancedSetValue() {
        final String YEAR_VISUAL_TEXT_VALUE = "2021";
        final String YEAR_HTML_VALUE = "2021";
        final String YEAR_INDEX_VALUE = "2";
        final String YEAR_REGEX_VISUAL_TEXT_VALUE = ".*21";
        final String YEAR_REGEX_HTML_VALUE = ".*21";

        final String VISIBLE_TEXT = "VISIBLE_TEXT >>> ";
        final String VALUE_HTML = "VALUE_HTML >>> ";
        final String INDEX = "INDEX >>> ";
        final String VISIBLE_TEXT_REGEX = "VISIBLE_TEXT_REGEX >>> ";
        final String VALUE_HTML_REGEX = "VALUE_HTML_REGEX >>> ";
        final String RANDOM_INDEX = "RANDOM_INDEX >>> ";
        final String RANDOM_INDEX_VALUES = "RANDOM_INDEX_VALUES >>> ";
        final String RANDOM_INDEX_RANGE = "RANDOM_INDEX_RANGE >>> ";

        //
        // Basic common ways to select the value
        //

        String reason = "Year - Visible Text (default)";
        String value = YEAR_VISUAL_TEXT_VALUE;
        String expectedValue = YEAR_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetYear();

        reason = "Year - Visible Text (Explicit)";
        value = VISIBLE_TEXT + YEAR_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetYear();

        reason = "Year - Value";
        value = VALUE_HTML + YEAR_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetYear();

        reason = "Year - Index";
        value = INDEX + YEAR_INDEX_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetYear();

        reason = "Year - RegEx Visible Text";
        value = VISIBLE_TEXT_REGEX + YEAR_REGEX_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetYear();

        reason = "Year - RegEx Value";
        value = VALUE_HTML_REGEX + YEAR_REGEX_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);
        resetYear();

        //
        // Random ways to select the value
        //

        reason = "Year - Random Index";
        value = RANDOM_INDEX + "7";
        int actualYear = NumberUtils.toInt(setYear(value));
        AssertJUtil.assertThat(actualYear).as(reason).isGreaterThanOrEqualTo(2026);
        resetYear();

        reason = "Year - Random Index Values";
        value = RANDOM_INDEX_VALUES + "7,8";
        AssertJUtil.assertThat(setYear(value)).as(reason).isIn("2026", "2027");
        resetYear();

        reason = "Year - Random Index Range";
        value = RANDOM_INDEX_RANGE + "7:12";
        actualYear = NumberUtils.toInt(setYear(value));
        AssertJUtil.assertThat(actualYear).as(reason).isGreaterThanOrEqualTo(2026);
        AssertJUtil.assertThat(actualYear).as(reason).isLessThan(2031);
        resetYear();

        //
        // Value already selected tests
        //
        resetYear();
        resetYear();

        // Set the value for the tests that follow
        setYear(YEAR_VISUAL_TEXT_VALUE);

        reason = "Year - Already Selected - Visible Text (default)";
        value = YEAR_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Year - Already Selected - Visible Text (Explicit)";
        value = VISIBLE_TEXT + YEAR_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Year - Already Selected - Value";
        value = VALUE_HTML + YEAR_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Year - Already Selected - Index";
        value = INDEX + YEAR_INDEX_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Year - Already Selected - RegEx Visible Text";
        value = VISIBLE_TEXT_REGEX + YEAR_REGEX_VISUAL_TEXT_VALUE;
        validateSetValue(reason, value, expectedValue);

        reason = "Year - Already Selected - RegEx Value";
        value = VALUE_HTML_REGEX + YEAR_HTML_VALUE;
        validateSetValue(reason, value, expectedValue);
    }

    @Step("Validate SetValue:  {0}")
    private void validateSetValue(String reason, String value, String expectedValue) {
        AssertJUtil.assertThat(setYear(value)).as(reason).isEqualTo(expectedValue);
    }

    @Step("Set Year:  {0}")
    private String setYear(String value) {
        setElementValueV2(value, year);
        return year.getValue();
    }

    @Step("Reset Year")
    private void resetYear() {
        String value = "2025";
        AssertJUtil.assertThat(setYear(value)).as("Year - 2025").isEqualTo(value);
    }

}
