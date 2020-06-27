package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.JexlExpressionDO;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class JexlExpressionTest extends TestNGBase {
    private static final List<String> jexlVariables = Arrays.asList(
            "str1", "str2", "RANDOM_DATE", "RANDOM_US_ADDRESS",
            "ALPHANUMERIC", "crypto", "lookup", "dateActions", "random_date_pattern", "dateUtils"
    );
    private static final String GENERIC = "Variable %s was not evaluated by Jexl for %s";

    @Features("Framework")
    @Stories("Validate that Jexl Expressions being evaluated properly")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test
    public void testJexlExpresionEvaluation(@Optional("data/ui/JexlExpresion_TestData.xml") String dataSet) {
        // For convenience (and/or no extra data files), you may want to re-use an existing data file
        // to perform the lookup actions for the test data that you need them
        JexlExpressionDO usedForLookupActions = new JexlExpressionDO().fromResourceSilent(dataSet);
        usedForLookupActions.validateCurrentSystemDateBeforeLoad();
        usedForLookupActions.performPreFromResourceActions();

        // All the lookup variables have been set and we can load the test data
        JexlExpressionDO jexlDO = new JexlExpressionDO(getContext()).fromResource(dataSet);
        jexlDO.validateCurrentSystemDateAfterLoad();
        validateFieldValue("Some Field", jexlDO.getSomeField(), jexlDO.getSomeFieldExpected());
        validateFieldValue("Another Field", jexlDO.getAnotherField(), jexlDO.getAnotherFieldExpected());
        validateFieldValue("Extra Field 1", jexlDO.getExtraField1(), jexlDO.getExtraField1Expected());
        validateFieldValue("Extra Field 2", jexlDO.getExtraField2(), jexlDO.getExtraField2Expected());
        validateFieldValue("Extra Field 3", jexlDO.getExtraField3(), jexlDO.getExtraField3Expected());
        validateFieldValue("Extra Field 4", jexlDO.getExtraField4(), jexlDO.getExtraField4Expected());

        validateFieldValue("User Field 1", jexlDO.getUserField1(), jexlDO.getUserField1Expected());
        validateFieldValue("User Field 2", jexlDO.getUserField2(), jexlDO.getUserField2Expected());
        validateFieldValue("User Field 3", jexlDO.getUserField3(), jexlDO.getUserField3Expected());

        validateFieldValue("BigDecimal Field 1", jexlDO.getBdField1(), jexlDO.getBdField1Expected());
        validateFieldValue("BigDecimal Field 2", jexlDO.getBdField2(), jexlDO.getBdField2Expected());

        for (String var : jexlVariables) {
            validateField(var, jexlDO.getSomeField(), jexlDO.getSomeFieldExpected(), jexlDO.getSomeFieldInitial());
            validateField(var, jexlDO.getAnotherField(), jexlDO.getAnotherFieldExpected(), jexlDO.getAnotherFieldInitial());
            validateField(var, jexlDO.getExtraField1(), jexlDO.getExtraField1Expected(), jexlDO.getExtraField1Initial());
            validateField(var, jexlDO.getExtraField2(), jexlDO.getExtraField2Expected(), jexlDO.getExtraField2Initial());
            validateField(var, jexlDO.getExtraField3(), jexlDO.getExtraField3Expected(), jexlDO.getExtraField3Initial());
            validateField(var, jexlDO.getExtraField4(), jexlDO.getExtraField4Expected(), jexlDO.getExtraField4Initial());
        }
    }

    @Step("Validate Field ({0}) Value has correct value")
    private void validateFieldValue(String field, String actual, String expected) {
        assertThat(field, actual, equalTo(expected));
    }

    @Step("Validate Field {0} was evaluated properly")
    private void validateField(String var, String data, String expected, String initial) {
        assertThat(String.format(GENERIC, var, "DataTypes.Data"), data, not(containsString(var)));
        assertThat(String.format(GENERIC, var, "DataTypes.Expected"), expected, not(containsString(var)));
        assertThat(String.format(GENERIC, var, "DataTypes.Initial"), initial, not(containsString(var)));
    }

}
