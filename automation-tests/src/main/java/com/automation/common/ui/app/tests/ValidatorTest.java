package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.components.validator.BasicCurrencyValidator;
import com.automation.common.ui.app.components.validator.BasicEqualsValidator;
import com.automation.common.ui.app.components.validator.CaseInsensitiveValidator;
import com.automation.common.ui.app.components.validator.DateValidator;
import com.automation.common.ui.app.components.validator.MaskingValidator;
import com.automation.common.ui.app.components.validator.RegexValidator;
import com.automation.common.ui.app.components.validator.Validator;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.RegExUtils;
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
    private static final String BBB_UPPERCASE = "BBB";
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
    private static final String A = "a";
    private static final String B = "b";
    private static final String C = "c";
    private static final String D = "d";
    private static final String A_UPPERCASE = "A";
    private static final String B_UPPERCASE = "B";

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

    @Features("Validator")
    @Stories("Validate Masking Validator")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateMaskingValidator() {
        MaskingValidator validator = new MaskingValidator();

        // no masking - pass/fail
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        // no masking - ignore case - pass/fail
        validator.withIgnoreCase(true);
        validateSuccess(validator.withActual(CCC).withExpected(CCC_UPPERCASE));
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        // only actual - pass
        validator.reset().applyOnlyActual();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));

        // modify the actual to equal expected
        validator.reset().applyOnlyActual().addActionReplace(A, B);
        validateSuccess(validator.withActual(AAA).withExpected(BBB));

        validator.reset().withIgnoreCase(true).applyOnlyActual().addActionReplace(A, B_UPPERCASE);
        validateSuccess(validator.withActual(AAA).withExpected(BBB));

        // only actual  - fail
        validator.reset().applyOnlyActual();
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        validator.reset().applyOnlyActual().addActionReplace(A, C);
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        validator.reset().applyOnlyActual().withIgnoreCase(true).addActionReplace(A, D);
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        // only expected - pass
        validator.reset().applyOnlyExpected();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));

        // modify the expected to equal actual
        validator.reset().applyOnlyExpected().addActionReplace(B, A);
        validateSuccess(validator.withActual(AAA).withExpected(BBB));

        validator.reset().withIgnoreCase(true).applyOnlyExpected().addActionReplace(B, A_UPPERCASE);
        validateSuccess(validator.withActual(AAA).withExpected(BBB));

        // only expected - fail
        validator.reset().applyOnlyExpected();
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        validator.reset().applyOnlyExpected().addActionReplace(B, C);
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        validator.reset().applyOnlyExpected().withIgnoreCase(true).addActionReplace(B, D);
        validateFailure(validator.withActual(AAA).withExpected(BBB));

        // Remove Non-Digits
        validator.reset().addActionRemoveNonDigits();
        validateSuccess(validator.withActual("111").withExpected("111"));
        validateSuccess(validator.withActual("abc123def").withExpected("z1yx2def3"));
        validateFailure(validator.withActual("22").withExpected("33"));
        validateFailure(validator.withActual("abc123def").withExpected("z4yx5def6"));

        // Remove Non-Letters
        validator.reset().addActionRemoveNonLetters();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("abc456def").withExpected("1ab2cd3ef4"));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("abc456def").withExpected("1zz2yy3xx4"));

        // Remove Non-Alphanumeric
        validator.reset().addActionRemoveNonAlphanumeric();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("[abc987def]").withExpected("{abc987def}"));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("[abc987def]").withExpected("{abz789yef}"));

        // Remove All
        validator.reset().addActionRemoveAll(RegExUtils.NOT_ALPHANUMERIC);
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("$abc987def&").withExpected("+abc987def="));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("$abc987def&").withExpected("+abz789yef="));

        // Replace
        validator.reset().addActionReplace("test", "333");
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("$abctestdef&").withExpected("$abc333def&"));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("$abctestdef&").withExpected("$abc444def&"));

        // Add 2nd replace
        validator.addActionReplace("**", "333");
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("test123xyz").withExpected("**123xyz"));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("test123xyz").withExpected("**456xyz"));

        // Replace All
        validator.reset().addActionReplaceAll(RegExUtils.NOT_ALPHANUMERIC, "*");
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("$xyz987def&").withExpected("+xyz987def="));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("$xyz987def&").withExpected("xyz+987=def"));

        // Trim All
        validator.reset().addActionTrimAll();
        validateSuccess(validator.withActual(AAA).withExpected(AAA));
        validateSuccess(validator.withActual("    " + AAA).withExpected(AAA + "  "));
        validateFailure(validator.withActual(AAA).withExpected(BBB));
        validateFailure(validator.withActual("    " + AAA).withExpected(BBB + "  "));

        // sequences - pass
        // removeNonLetters, replace (BBB, CC33+=DD), removeNonAlphanumeric, removeNonDigits
        // aaa222BBB -> aaaBBB -> aaaCC33+=DD -> aaaCC33DD -> 33
        final String actual = "aaa222BBB";
        final String expected = "aaa555BBB";
        final String replacement = "CC33+=DD";
        validator.reset()
                .applyOnlyActual()
                .addActionRemoveNonLetters()
                .addActionReplace(BBB_UPPERCASE, replacement)
                .addActionRemoveNonAlphanumeric()
                .addActionRemoveNonDigits();
        validateSuccess(validator.withActual(actual).withExpected("33"));

        validator.reset()
                .addActionRemoveNonLetters()
                .addActionReplace(BBB_UPPERCASE, replacement)
                .addActionRemoveNonAlphanumeric()
                .addActionRemoveNonDigits();
        validateSuccess(validator.withActual(actual).withExpected(expected));

        // sequences - fail
        // removeNonLetters, replace (BBB, CC33+=DD), removeNonAlphanumeric, removeNonDigits
        // aaa222BBB -> aaaBBB -> aaaCC33+=DD -> aaaCC33DD -> 33 != 44
        validator.reset()
                .applyOnlyActual()
                .addActionRemoveNonLetters()
                .addActionReplace(BBB_UPPERCASE, replacement)
                .addActionRemoveNonAlphanumeric()
                .addActionRemoveNonDigits();
        validateFailure(validator.withActual(actual).withExpected("44"));

        validator.reset()
                .addActionRemoveNonLetters()
                .addActionReplace(BBB_UPPERCASE, replacement)
                .addActionRemoveNonAlphanumeric()
                .addActionRemoveNonDigits();
        validateFailure(validator.withActual(actual).withExpected(replacement));
    }

}
