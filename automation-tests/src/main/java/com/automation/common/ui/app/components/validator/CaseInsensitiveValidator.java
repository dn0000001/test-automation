package com.automation.common.ui.app.components.validator;

import com.taf.automation.ui.support.util.AssertJUtil;

/**
 * This validator asserts that the actual &amp; expected values are equal ignoring case
 */
@SuppressWarnings("java:S3252")
public class CaseInsensitiveValidator extends Validator {
    public CaseInsensitiveValidator() {
        withFailureMessage("Case Insensitive Validator");
    }

    @Override
    public void validateData() {
        AssertJUtil.assertThat(getActual()).as(getFailureMessage()).isEqualToIgnoringCase(getExpected());
    }

}
