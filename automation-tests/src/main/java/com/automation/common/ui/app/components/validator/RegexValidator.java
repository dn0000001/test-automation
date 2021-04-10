package com.automation.common.ui.app.components.validator;

import com.taf.automation.ui.support.util.AssertJUtil;

/**
 * This validator asserts that the actual matches the expected regular expression
 */
@SuppressWarnings("java:S3252")
public class RegexValidator extends Validator {
    public RegexValidator() {
        withFailureMessage("Regex Validator");
    }

    @Override
    public void validateData() {
        AssertJUtil.assertThat(getActual()).as(getFailureMessage()).matches(getExpected());
    }

}
