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
            "ALPHANUMERIC", "crypto"
    );
    private static final String GENERIC = "Variable %s was not evaluated by Jexl for %s";

    @Features("Framework")
    @Stories("Validate that Jexl Expressions being evaluated properly")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test
    public void testJexlExpresionEvaluation(@Optional("data/ui/JexlExpresion_TestData.xml") String dataSet) {
        JexlExpressionDO jexlDO = new JexlExpressionDO(getContext()).fromResource(dataSet);
        validateFieldValue("Some Field", jexlDO.getSomeField(), jexlDO.getSomeFieldExpected());
        validateFieldValue("Another Field", jexlDO.getAnotherField(), jexlDO.getAnotherFieldExpected());
        for (String var : jexlVariables) {
            validateField(var, jexlDO.getSomeField(), jexlDO.getSomeFieldExpected(), jexlDO.getSomeFieldInitial());
            validateField(var, jexlDO.getAnotherField(), jexlDO.getAnotherFieldExpected(), jexlDO.getAnotherFieldInitial());
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
