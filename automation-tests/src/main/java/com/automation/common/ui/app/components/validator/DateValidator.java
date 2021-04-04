package com.automation.common.ui.app.components.validator;

import com.taf.automation.asserts.AssertJCondition;
import com.taf.automation.ui.support.util.AssertJUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This validator asserts that the actual &amp; expected are the same date after applying the date patterns
 */
@SuppressWarnings("java:S3252")
public class DateValidator extends Validator {
    private final List<String> patterns = new ArrayList<>();

    public DateValidator() {
        withFailureMessage("Date Validator");
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public DateValidator withPattern(String pattern) {
        getPatterns().add(pattern);
        return this;
    }

    @Override
    public void validateData() {
        AssertJUtil.assertThat(getActual())
                .as(getFailureMessage())
                .is(AssertJCondition.dateEqualTo(getExpected(), getPatterns().toArray(new String[0])));
    }

    @Override
    public DateValidator withFailureMessage(String failureMessage) {
        super.withFailureMessage(failureMessage);
        return this;
    }

    @Override
    public DateValidator withActual(String actual) {
        super.withActual(actual);
        return this;
    }

    @Override
    public DateValidator withExpected(String expected) {
        super.withExpected(expected);
        return this;
    }

}
