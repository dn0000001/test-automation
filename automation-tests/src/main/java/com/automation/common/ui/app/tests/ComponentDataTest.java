package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.ComponentsDO;
import com.automation.common.ui.app.pageObjects.FakeComponentsPage;
import com.taf.automation.asserts.CustomSoftAssertions;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

@SuppressWarnings("java:S3252")
public class ComponentDataTest extends TestNGBase {
    private ComponentsDO componentsDO;

    @Features("Component")
    @Stories("Verify that accessing component data does not generate CodeGenerationException")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test
    public void performTest(@Optional("data/ui/AllComponents_TestData.xml") String dataSet) {
        //
        // Note:  The test data for this test exists for app.environment.target=QA
        // If test data does not appear to be correct, then check that you modified
        // data/ui/qa-AllComponents_TestData.xml when app.environment.target=QA
        //
        AssertJUtil.assertThat(TestProperties.getInstance().getEnvironmentTarget())
                .as("test.properties needs the property:  app.environment.target=QA")
                .isEqualToIgnoringCase("QA");
        componentsDO = new ComponentsDO(getContext()).fromResource(dataSet);
        performCheckForCodeGenerationException();
        performCheckThatAliasNotResolved();
        performCheckThatAliasResolved();
    }

    @Step("Validate that no CodeGenerationException occurs")
    private void performCheckForCodeGenerationException() {
        componentsDO.getFakeComponentsPage().hasData();
    }

    /**
     * Validate that aliases are not resolved when flag is false.<BR>
     * <B>Note: </B> The test data needs to contain aliases for each component.
     */
    @Step("Validate that aliases are not resolved when flag is false")
    private void performCheckThatAliasNotResolved() {
        CustomSoftAssertions softly = new CustomSoftAssertions();

        for (FakeComponentsPage.Type item : FakeComponentsPage.Type.values()) {
            String actual = componentsDO.getFakeComponentsPage().getTestData(item, false);
            String expected = "${" + item.toString() + "}";
            softly.assertThat(actual).as("Component " + expected).isEqualTo(expected);
        }

        softly.assertAll();
    }

    /**
     * Validate that aliases are resolved when flag is true.<BR>
     * <B>Note: </B> The test data needs to contain aliases for each component
     * and the expected data needs to be configured in the method getExpectedTestData
     */
    @Step("Validate that aliases are resolved when flag is true")
    private void performCheckThatAliasResolved() {
        CustomSoftAssertions softly = new CustomSoftAssertions();

        for (FakeComponentsPage.Type item : FakeComponentsPage.Type.values()) {
            String actual = componentsDO.getFakeComponentsPage().getTestData(item, true);
            String expected = getExpectedTestData(item);
            softly.assertThat(actual).as("Component " + item).isEqualTo(expected);
        }

        softly.assertAll();
    }

    @SuppressWarnings("java:S3776")
    private String getExpectedTestData(FakeComponentsPage.Type component) {
        if (component == FakeComponentsPage.Type.CHECK_BOX_AJAX) {
            return "a1";
        }

        if (component == FakeComponentsPage.Type.CHECK_BOX_BASIC) {
            return "p16";
        }

        if (component == FakeComponentsPage.Type.CHECK_BOX_LABEL) {
            return "b2";
        }

        if (component == FakeComponentsPage.Type.IMAGE_UPLOAD) {
            return "c3";
        }

        if (component == FakeComponentsPage.Type.PRIME_FACES_RADIO_BUTTON_GROUP) {
            return "d4";
        }

        if (component == FakeComponentsPage.Type.RADIO_BUTTON_GROUP) {
            return "e5";
        }

        if (component == FakeComponentsPage.Type.RADIO_OPTION) {
            return "f6";
        }

        if (component == FakeComponentsPage.Type.SEARCH_TEXT_BOX) {
            return "g7";
        }

        if (component == FakeComponentsPage.Type.SELECT) {
            return "h8";
        }

        if (component == FakeComponentsPage.Type.SELECT_ENHANCED) {
            return "i9";
        }

        if (component == FakeComponentsPage.Type.SELECT_ENHANCED_JAX) {
            return "j10";
        }

        if (component == FakeComponentsPage.Type.TAB_OFF_TEXT_BOX) {
            return "k11";
        }

        if (component == FakeComponentsPage.Type.TEXT_BOX) {
            return "l12";
        }

        if (component == FakeComponentsPage.Type.TEXT_BOX_BACKSPACE) {
            return "m13";
        }

        if (component == FakeComponentsPage.Type.WEB_COMPONENT) {
            return "n14";
        }

        if (component == FakeComponentsPage.Type.ALIASED_STRING) {
            return "o15";
        }

        AssertJUtil.fail("Unsupported Component Type:  " + component);
        return null;
    }

}
