package com.automation.common.ui.app.components.validator;

import java.util.ArrayList;
import java.util.List;

import static com.taf.automation.ui.support.util.AssertsUtil.dateEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This validator asserts that the actual &amp; expected are the same date after applying the date patterns
 */
public class DateValidator extends Validator {
    private final List<String> patterns = new ArrayList<>();

    public DateValidator() {
        withFailureMessage("Date Validator");
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public Validator withPattern(String pattern) {
        getPatterns().add(pattern);
        return this;
    }

    @Override
    public void validateData() {
        assertThat(getFailureMessage(), getActual(), dateEqualTo(getExpected(), getPatterns().toArray(new String[0])));
    }

}
