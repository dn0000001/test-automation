package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.RoboFormDO;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.PageObjectUtils;
import com.taf.automation.ui.support.util.Utils;
import org.testng.ITestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class PageObjectUtilsTest extends TestNGBase {
    private static final String PREFIX = "prefix-";
    private static final int EXPECTED_PARAMETERS_WITH_PREFIX = 2;

    private enum ParameterKey implements ColumnMapper {
        SPECIAL("special", "just for testing"),
        FIELD_1("field1", "something"),
        FIRST_NAME("firstName", "Modified"),
        LAST_NAME("lastName", "Test"), // The value in this case is the initial one
        ;

        private final String key;
        private final String value;

        ParameterKey(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getColumnName() {
            return key;
        }

        @Override
        public ColumnMapper[] getValues() {
            return values();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }

    @Features("Framework")
    @Stories("PageObjectUtils")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"base-data-set"})
    @Test
    public void testPageObjectUtils(
            @Optional("data/ui/RoboFormDynamic_TestData.xml") String dataSet,
            ITestContext injectedContext
    ) {
        RoboFormDO roboFormDO = new RoboFormDO(getContext()).fromResource(dataSet);

        Map<String, String> testParameters = injectedContext.getCurrentXmlTest().getAllParameters();
        // This test should have the following additional parameters:
        // <parameter name="add-data-useDynamicPage" value="false"/>
        // <parameter name="add-data-firstName" value="Modified"/>
        // <parameter name="remove-data-lastName" value=""/>
        // <parameter name="prefix-add-data-field1" value="something"/>
        // <parameter name="prefix-special" value="just for testing"/>

        // Normally for performance reasons, you would want to only process if work needs to be done.
        // For this unit test, it is expected to be true
        assertThat("Test must have a parameter to add/remove data", PageObjectUtils.isAnyToProcess(testParameters));

        // Normally if you are processing multiple items in the same test (or are grouping them together for some
        // reason), then you want to have the prefix removed to just have the key.
        Map<String, String> prefixedParameters = PageObjectUtils.getSpecificParameters(PREFIX, testParameters, true);

        // For this unit test, the number of prefixed parameters has a specific size.  Normally, you would not care
        // about how many parameters there are.
        assertThat("Prefixed Parameters", prefixedParameters.size(), equalTo(EXPECTED_PARAMETERS_WITH_PREFIX));

        // For this unit test, it is expected to have a key with a specific value
        String actualValue = prefixedParameters.remove(ParameterKey.SPECIAL.getKey());
        String expectedValue = ParameterKey.SPECIAL.getValue();
        assertThat("Parameter " + PREFIX + ParameterKey.SPECIAL.getKey(), actualValue, equalTo(expectedValue));

        // For this unit test, extract the 2nd prefixed parameter which is expected to be a field to be added
        String getPrefix = PageObjectUtils.getAddDataPrefix();
        Map<String, String> extracted = PageObjectUtils.getSpecificParameters(getPrefix, prefixedParameters, true);
        assertThat("Extracted Parameter", extracted.size(), equalTo(1));

        actualValue = extracted.remove(ParameterKey.FIELD_1.getKey());
        expectedValue = ParameterKey.FIELD_1.getValue();
        String log = "Parameter " + PREFIX + getPrefix + ParameterKey.FIELD_1.getKey();
        assertThat(log, actualValue, equalTo(expectedValue));

        // For this unit test, change the flag from true to false
        assertThat("Initially useDynamicPage needs to be true", roboFormDO.isUseDynamicPage());
        PageObjectUtils.process(roboFormDO, testParameters);
        Helper.log("Modified useDynamicPage in domain object after loading");
        assertThat("Changed useDynamicPage", !roboFormDO.isUseDynamicPage());

        // For this unit test, add & remove test data
        String initialFirstName = roboFormDO.getRoboFormDynamicPage().getTestDataFirstName();
        assertThat("Initial First Name", initialFirstName, not(isEmptyOrNullString()));

        String initialLastName = roboFormDO.getRoboFormDynamicPage().getTestDataLastName();
        assertThat("Initial Last Name", initialLastName, not(isEmptyOrNullString()));

        PageObjectUtils.process(roboFormDO.getRoboFormDynamicPage(), testParameters);
        Helper.log("Modified roboFormDynamicPage in domain object after loading");

        String changedFirstName = roboFormDO.getRoboFormDynamicPage().getTestDataFirstName();
        assertThat("First Name", changedFirstName, equalTo(ParameterKey.FIRST_NAME.getValue()));

        String changedLastName = roboFormDO.getRoboFormDynamicPage().getTestDataLastName();
        assertThat("Last Name", changedLastName, nullValue());

        // Use the modified domain object to perform the test
        new Navigation(getContext()).toRoboFormFill(Utils.isCleanCookiesSupported());
        roboFormDO.getRoboFormDynamicPage().fill();
    }

}
