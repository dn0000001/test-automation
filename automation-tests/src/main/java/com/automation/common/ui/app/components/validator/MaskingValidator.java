package com.automation.common.ui.app.components.validator;

import com.taf.automation.ui.support.StringMod;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JxltEngine;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This validator asserts that the actual matches the expected after applying masking rules
 */
@SuppressWarnings("java:S3252")
public class MaskingValidator extends Validator {
    private static final Logger LOG = LoggerFactory.getLogger(MaskingValidator.class);
    private static final String DOUBLE_QUOTE = "\"";
    private boolean maskActual;
    private boolean maskExpected;
    private boolean ignoreCase;
    private StringBuilder expression;

    public MaskingValidator() {
        withFailureMessage("Masking Validator");
        reset();
    }

    /**
     * Set flag to apply the expression to both actual &amp; expected values
     *
     * @return MaskingValidator
     */
    public MaskingValidator applyToBoth() {
        maskActual = true;
        maskExpected = true;
        return this;
    }

    /**
     * Set flag to only apply the expression to the actual value
     *
     * @return MaskingValidator
     */
    public MaskingValidator applyOnlyActual() {
        maskActual = true;
        maskExpected = false;
        return this;
    }

    /**
     * Set flag to only apply the expression to the expected value
     *
     * @return MaskingValidator
     */
    public MaskingValidator applyOnlyExpected() {
        maskExpected = true;
        maskActual = false;
        return this;
    }

    /**
     * Set ignore case flag
     *
     * @param ignoreCase - true to ignore case on validation, false for case sensitive validation
     * @return MaskingValidator
     */
    public MaskingValidator withIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    /**
     * Reset configuration back to defaults
     *
     * @return MaskingValidator
     */
    public MaskingValidator reset() {
        applyToBoth();
        withIgnoreCase(false);
        expression = new StringBuilder("sm");
        return this;
    }

    /**
     * Add action to remove non-digits to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionRemoveNonDigits() {
        expression.append(".removeNonDigits()");
        return this;
    }

    /**
     * Add action to remove non-letters to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionRemoveNonLetters() {
        expression.append(".removeNonLetters()");
        return this;
    }

    /**
     * Add action to remove non-alphanumeric to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionRemoveNonAlphanumeric() {
        expression.append(".removeNonAlphanumeric()");
        return this;
    }

    /**
     * Add action to remove all characters matching the regular expression to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionRemoveAll(String regex) {
        expression.append(".removeAll(");
        expression.append(StringUtils.wrap(StringEscapeUtils.escapeJava(regex), DOUBLE_QUOTE));
        expression.append(")");
        return this;
    }

    /**
     * Add action to replace each substring of this string that matches the literal target sequence with the specified
     * literal replacement sequence to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionReplace(String target, String replacement) {
        expression.append(".replace(");
        expression.append(StringUtils.wrap(StringEscapeUtils.escapeJava(target), DOUBLE_QUOTE));
        expression.append(",");
        expression.append(StringUtils.wrap(StringEscapeUtils.escapeJava(replacement), DOUBLE_QUOTE));
        expression.append(")");
        return this;
    }

    /**
     * Add action to replace all characters matching the regular expression to the expression<BR>
     * <B>Notes: </B>
     * <UL>
     * <LI>Each action will be executed in sequence they were added</LI>
     * <LI>If regular expression is invalid, then nothing will be replaced</LI>
     * </UL>
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionReplaceAll(String regex, String replacement) {
        expression.append(".replaceAll(");
        expression.append(StringUtils.wrap(StringEscapeUtils.escapeJava(regex), DOUBLE_QUOTE));
        expression.append(",");
        expression.append(StringUtils.wrap(StringEscapeUtils.escapeJava(replacement), DOUBLE_QUOTE));
        expression.append(")");
        return this;
    }

    /**
     * Add action to trim leading and trailing whitespace to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionTrim() {
        expression.append(".trim()");
        return this;
    }

    /**
     * Add action to trim whitespace (non-visible text) from beginning and end of the string to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionTrimNonVisible() {
        expression.append(".trimNonVisible()");
        return this;
    }

    /**
     * Add action to remove all Invisible Control characters to the expression<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionRemoveInvisibleControl() {
        expression.append(".removeInvisibleControl()");
        return this;
    }

    /**
     * Add all actions to trim<BR>
     * <B>Note: </B> Each action will be executed in sequence they were added
     *
     * @return MaskingValidator
     */
    public MaskingValidator addActionTrimAll() {
        return addActionTrim().addActionTrimNonVisible().addActionRemoveInvisibleControl();
    }

    private String getMaskedValue(String value, String jexlExpression) {
        String result = null;

        JxltEngine jxlt = new JexlBuilder().strict(true).silent(false).create().createJxltEngine();
        JxltEngine.Expression expr = jxlt.createExpression(jexlExpression);
        JexlContext jexlContext = new MapContext();
        jexlContext.set("sm", new StringMod(value));
        try {
            Object objValue = expr.evaluate(jexlContext);
            result = objValue.toString();
        } catch (Exception e) {
            logJexlEvaluationException(e);
        }

        return result;
    }

    private void logJexlEvaluationException(Exception e) {
        if (TestProperties.getInstance().isDebugLogging()) {
            LOG.warn(e.getMessage());
        }
    }

    private String constructJexl(String value) {
        return "${" + value + ".get()}";
    }

    private String getMaskedActual() {
        return maskActual ? getMaskedValue(getActual(), constructJexl(expression.toString())) : getActual();
    }

    private String getMaskedExpected() {
        return maskExpected ? getMaskedValue(getExpected(), constructJexl(expression.toString())) : getExpected();
    }

    @Override
    public void validateData() {
        if (ignoreCase) {
            AssertJUtil.assertThat(getMaskedActual()).as(getFailureMessage()).isEqualToIgnoringCase(getMaskedExpected());
        } else {
            AssertJUtil.assertThat(getMaskedActual()).as(getFailureMessage()).isEqualTo(getMaskedExpected());
        }
    }

    @Override
    public MaskingValidator withFailureMessage(String failureMessage) {
        super.withFailureMessage(failureMessage);
        return this;
    }

    @Override
    public MaskingValidator withActual(String actual) {
        super.withActual(actual);
        return this;
    }

    @Override
    public MaskingValidator withExpected(String expected) {
        super.withExpected(expected);
        return this;
    }

}
