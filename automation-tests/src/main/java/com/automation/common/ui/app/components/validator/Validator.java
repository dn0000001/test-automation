package com.automation.common.ui.app.components.validator;

/**
 * The Validator class handles validating the entered data is as expected.  In most cases, the actual entered data
 * should exactly match the expected data.  However, if the field has masking or JavaScript cleaning the data, then
 * they may not match exactly and some transformation/cleaning may need to be done for validation purposes.<BR>
 * <B>Note: </B> This is mainly useful for input fields.
 */
public abstract class Validator {
    private String failureMessage;
    private String actual;
    private String expected;

    /**
     * @return the failure message that would be used in case of validation failure
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Set the failure message to used in the case of validation failure
     *
     * @param failureMessage - Failure Message
     * @return Validator
     */
    public Validator withFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
        return this;
    }

    /**
     * @return the actual value without any modifications
     */
    public String getActual() {
        return actual;
    }

    /**
     * Set the actual value without modifications to be used in the validation
     *
     * @param actual - Actual value without modifications
     * @return Validator
     */
    public Validator withActual(String actual) {
        this.actual = actual;
        return this;
    }

    /**
     * @return the expected value
     */
    public String getExpected() {
        return expected;
    }

    /**
     * Set the expected value to be used in the validation
     *
     * @param expected - Expected value
     * @return Validator
     */
    public Validator withExpected(String expected) {
        this.expected = expected;
        return this;
    }

    /**
     * Uses the actual &amp; expect data to validate the values are equivalent.<BR>
     * <B>Note: </B>  Equivalence is determined by the implementing class
     *
     * @throws AssertionError with the failure message if the validation fails
     */
    public abstract void validateData();
}
