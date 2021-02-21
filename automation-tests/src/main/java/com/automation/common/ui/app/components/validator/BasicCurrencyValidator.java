package com.automation.common.ui.app.components.validator;

import com.taf.automation.ui.support.util.Utils;

import java.math.BigDecimal;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

/**
 * This validator asserts that the actual &amp; expected are the same value after cleaning invalid characters.<BR>
 * <B>Note: </B> The minus sign is considered an invalid character as such no values will be negative.  If amounts
 * can be negative, then you should create another Validator to these scenarios.
 */
public class BasicCurrencyValidator extends Validator {
    private Locale locale;

    public BasicCurrencyValidator() {
        withFailureMessage("Basic Currency Validator");
    }

    public Locale getLocale() {
        if (locale == null) {
            locale = Locale.CANADA;
        }

        return locale;
    }

    public BasicCurrencyValidator withLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    private BigDecimal getActualBigDecimal() {
        return Utils.parse(getActual(), getLocale());
    }

    private BigDecimal getExpectedBigDecimal() {
        return Utils.parse(getExpected(), getLocale());
    }

    @Override
    public void validateData() {
        assertThat(getFailureMessage(), getActualBigDecimal(), comparesEqualTo(getExpectedBigDecimal()));
    }

    @Override
    public BasicCurrencyValidator withFailureMessage(String failureMessage) {
        super.withFailureMessage(failureMessage);
        return this;
    }

    @Override
    public BasicCurrencyValidator withActual(String actual) {
        super.withActual(actual);
        return this;
    }

    @Override
    public BasicCurrencyValidator withExpected(String expected) {
        super.withExpected(expected);
        return this;
    }

}
