package com.automation.common.ui.app.components.validator;

import static com.taf.automation.ui.support.util.AssertsUtil.matchesRegex;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * This validator asserts that the actual matches the expected regular expression
 */
public class RegexValidator extends Validator {
    public RegexValidator() {
        withFailureMessage("Regex Validator");
    }

    @Override
    public void validateData() {
        assertThat(getFailureMessage(), getActual(), matchesRegex(getExpected()));
    }

}
