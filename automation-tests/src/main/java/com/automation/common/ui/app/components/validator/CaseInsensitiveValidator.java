package com.automation.common.ui.app.components.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

/**
 * This validator asserts that the actual &amp; expected values are equal ignoring case
 */
public class CaseInsensitiveValidator extends Validator {
    public CaseInsensitiveValidator() {
        withFailureMessage("Case Insensitive Validator");
    }

    @Override
    public void validateData() {
        assertThat(getFailureMessage(), getActual(), equalToIgnoringCase(getExpected()));
    }

}
