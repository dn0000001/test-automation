package com.automation.common.mobile.tests;

import com.automation.common.mobile.domainObjects.MobileDO;
import com.automation.common.mobile.pageObjects.CalculatorApp;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

@SuppressWarnings("java:S3252")
public class CalculatorAppTest extends TestNGBase {
    @Features("Appium")
    @Stories("Verify that framework work with the Android Calculator App")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-set")
    @Test
    public void performTest(@Optional("data/mobile/Calculator_TestData.xml") String dataSet) {
        MobileDO mobileDO = new MobileDO(getContext()).fromResource(dataSet);
        CalculatorApp calc = mobileDO.getCalculatorApp();
        calc.clickOne();
        calc.clickPlus();
        calc.clickNine();
        calc.clickMinus();
        calc.clickThree();

        String formula = calc.getFormula();
        AssertJUtil.assertThat(formula).as("Formula").isEqualTo("1+9−3");

        calc.clickEquals();
        String result = calc.getResult();
        AssertJUtil.assertThat(result).as("Result").isEqualTo("7");
        takeScreenshot("Result From Calculator App");
    }

}
