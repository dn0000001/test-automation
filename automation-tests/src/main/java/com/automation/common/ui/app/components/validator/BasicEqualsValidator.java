package com.automation.common.ui.app.components.validator;

import com.taf.automation.ui.support.util.AssertJUtil;

/**
 * This validator just asserts that the actual &amp; expected values are equal with any modifications
 */
@SuppressWarnings("java:S3252")
public class BasicEqualsValidator extends Validator {
    public BasicEqualsValidator() {
        withFailureMessage("Basic Equals Validator");
    }

    @Override
    public void validateData() {
        AssertJUtil.assertThat(getActual()).as(getFailureMessage()).isEqualTo(getExpected());
    }

}
