package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.components.validator.BasicCurrencyValidator;
import com.automation.common.ui.app.components.validator.BasicEqualsValidator;
import com.automation.common.ui.app.components.validator.CaseInsensitiveValidator;
import com.automation.common.ui.app.components.validator.DateValidator;
import com.automation.common.ui.app.components.validator.RegexValidator;
import com.automation.common.ui.app.components.validator.Validator;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.Helper;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

@Listeners(AllureTestNGListener.class)
public class ValidatorTest {
    private static final String AAA = "aaa";
    private static final String BBB = "bbb";
    private static final String CCC = "ccc";
    private static final String CCC_UPPERCASE = "CCC";
    private static final String FIVE_CURRENCY = "$5";
    private static final String FIVE_INT = "5";
    private static final String FIVE_CURRENCY_FLOAT = "$5.00";
    private static final String FIVE_FLOAT = "5.00";
    private static final String FIVE_ZERO_ONE = "5.01";
    private static final String DD_MM_YYYY = "MM/dd/yyyy";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String DEC_31_2018_FORMAT1 = "12/31/2018";
    private static final String DEC_31_2018_FORMAT2 = "2018-12-31";
    private static final String DEC_31_2019_FORMAT1 = "12/31/2019";
    private static final String DEC_31_2019_FORMAT2 = "2019-12-31";

    @Step("Validate Success")
    private void validateSuccess(Validator validator) {
        validator.validateData();
    }

    @SuppressWarnings("java:S112")
    @Step("Validate Failure")
    private void validateFailure(Validator validator) {
        try {
            validator.validateData();
            throw new RuntimeException("Assertion did not fail");
        } catch (AssertionError ae) {
            Helper.log("Validation failed as expected", true);
        }
    }

    @Features("Validator")
    @Stories("Validate Basic Equals Validator")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateBasicEqualsValidator() {
        Validator validator = new BasicEqualsValidator();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateFailure(validator.withActual(BBB).withExpected(CCC));
        validateFailure(validator.withActual(CCC).withExpected(CCC_UPPERCASE));
    }

    @Features("Validator")
    @Stories("Validate Basic Currency Validator")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateBasicCurrencyValidator() {
        Validator validator = new BasicCurrencyValidator();
        validateSuccess(validator.withActual(FIVE_CURRENCY).withExpected(FIVE_CURRENCY));
        validateSuccess(validator.withActual(FIVE_CURRENCY).withExpected(FIVE_INT));
        validateSuccess(validator.withActual(FIVE_CURRENCY).withExpected(FIVE_CURRENCY_FLOAT));
        validateFailure(validator.withActual(FIVE_FLOAT).withExpected(FIVE_ZERO_ONE));
        validateFailure(validator.withActual(FIVE_CURRENCY_FLOAT).withExpected(FIVE_ZERO_ONE));
    }

    @Features("Validator")
    @Stories("Validate Case Insensitive Validator")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateCaseInsensitiveValidator() {
        Validator validator = new CaseInsensitiveValidator();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual(CCC).withExpected(CCC_UPPERCASE));
        validateFailure(validator.withActual(BBB).withExpected(CCC));
    }

    @Features("Validator")
    @Stories("Validate Date Validator")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateDateValidator() {
        DateValidator validator = new DateValidator();

        validator.withPattern(DD_MM_YYYY);
        validateSuccess(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2018_FORMAT1));

        validator.withPattern(YYYY_MM_DD);
        validateSuccess(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2018_FORMAT1));
        validateSuccess(validator.withActual(DEC_31_2018_FORMAT2).withExpected(DEC_31_2018_FORMAT2));
        validateSuccess(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2018_FORMAT2));
        validateSuccess(validator.withActual(DEC_31_2018_FORMAT2).withExpected(DEC_31_2018_FORMAT1));

        validateFailure(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2019_FORMAT1));
        validateFailure(validator.withActual(DEC_31_2019_FORMAT1).withExpected(DEC_31_2018_FORMAT1));
        validateFailure(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2019_FORMAT2));
        validateFailure(validator.withActual(DEC_31_2019_FORMAT2).withExpected(DEC_31_2018_FORMAT1));

        validator.getPatterns().clear();
        validator.withPattern(DD_MM_YYYY);  // Only Format 1
        validateFailure(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2019_FORMAT1));
        validateFailure(validator.withActual(DEC_31_2019_FORMAT1).withExpected(DEC_31_2018_FORMAT1));
        validateFailure(validator.withActual(DEC_31_2018_FORMAT1).withExpected(DEC_31_2018_FORMAT2));
        validateFailure(validator.withActual(DEC_31_2018_FORMAT2).withExpected(DEC_31_2018_FORMAT2));
    }

    @Features("Validator")
    @Stories("Validate Regex Validator")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateRegexValidator() {
        Validator validator = new RegexValidator();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual(AAA + BBB + CCC).withExpected(".*" + BBB + ".*"));
        validateFailure(validator.withActual(CCC).withExpected(CCC_UPPERCASE));
        validateFailure(validator.withActual(BBB).withExpected(CCC));
        validateFailure(validator.withActual(AAA + BBB + CCC).withExpected(".*" + BBB));
    }

}
