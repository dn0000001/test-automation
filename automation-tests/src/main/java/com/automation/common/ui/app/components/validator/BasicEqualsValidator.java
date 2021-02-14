package com.automation.common.ui.app.components.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * This validator just asserts that the actual &amp; expected values are equal with any modifications
 */
public class BasicEqualsValidator extends Validator {
    public BasicEqualsValidator() {
        withFailureMessage("Basic Equals Validator");
    }

    @Override
    public void validateData() {
        assertThat(getFailureMessage(), getActual(), equalTo(getExpected()));
    }

}
