package com.taf.automation.ui.support.conditional;

public class MatchFactory {
    /**
     * Based on criteria return appropriate match class
     *
     * @param criteria - Criteria to find a match for
     * @return Match class that can work with the criteria to determine if match
     * @throws RuntimeException if criteria type is not supported or null
     */
    public Match getMatch(Criteria criteria) {
        if (criteria == null || criteria.getCriteriaType() == null) {
            throw new RuntimeException("Cannot get Match class for null");
        } else if (criteria.getCriteriaType() == CriteriaType.ALERT) {
            return new AlertMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.READY) {
            return new ElementReadyMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.DISPLAYED) {
            return new ElementDisplayedMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.REMOVED) {
            return new ElementRemovedMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.ENABLED) {
            return new ElementEnabledMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.DISABLED) {
            return new ElementDisabledMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.EXISTS) {
            return new ElementExistsMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.SELECTED) {
            return new ElementSelectedMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.UNSELECTED) {
            return new ElementUnselectedMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.STALE) {
            return new ElementStaleMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.TEXT_EQUALS
                || criteria.getCriteriaType() == CriteriaType.TEXT_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.TEXT_REGEX
                || criteria.getCriteriaType() == CriteriaType.TEXT_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.TEXT_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.TEXT_CONTAINS) {
            return new ElementTextMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.URL_EQUALS
                || criteria.getCriteriaType() == CriteriaType.URL_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.URL_REGEX
                || criteria.getCriteriaType() == CriteriaType.URL_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.URL_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.URL_CONTAINS) {
            return new URL_Match();
        } else if (criteria.getCriteriaType() == CriteriaType.ATTRIBUTE_EQUALS
                || criteria.getCriteriaType() == CriteriaType.ATTRIBUTE_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.ATTRIBUTE_REGEX
                || criteria.getCriteriaType() == CriteriaType.ATTRIBUTE_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.ATTRIBUTE_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.ATTRIBUTE_CONTAINS) {
            return new ElementAttributeMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.POPUP) {
            return new PopupMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.DROPDOWN_EQUALS
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_REGEX
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_CONTAINS
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_INDEX
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_EQUALS
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_EQUALS_IGNORE_CASE
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_REGEX
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_NOT_EQUAL
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_DOES_NOT_CONTAIN
                || criteria.getCriteriaType() == CriteriaType.DROPDOWN_HTML_CONTAINS) {
            return new ElementDropDownMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.RELATIVE_READY) {
            return new ElementReadyRelativeToMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.ELEMENTS_EQUAL
                || criteria.getCriteriaType() == CriteriaType.ELEMENTS_RANGE
                || criteria.getCriteriaType() == CriteriaType.ELEMENTS_LESS_THAN
                || criteria.getCriteriaType() == CriteriaType.ELEMENTS_MORE_THAN) {
            return new NumberOfElementsMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.LAMBDA_EXPRESSION) {
            return new LambdaExpressionMatch();
        } else if (criteria.getCriteriaType() == CriteriaType.EXPECTED_CONDITIONS) {
            return new ExpectedConditionsMatch();
        }

        throw new RuntimeException("Unsupported criteria type:  " + criteria.getCriteriaType());
    }

}
