package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.RoboFormDO;
import com.automation.common.ui.app.pageObjects.Navigation;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Utils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class RoboFormTest extends TestNGBase {
    @Features("Framework")
    @Stories("Using setElementValueV2 with ComponentPO")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"data-set"})
    @Test
    public void testSetElementValueV2WithComponentPO(@Optional("data/ui/RoboForm_TestData.xml") String dataSet) {
        new Navigation(getContext()).toRoboFormFill(Utils.isCleanCookiesSupported());
        RoboFormDO roboFormDO = new RoboFormDO(getContext()).fromResource(dataSet);
        if (roboFormDO.isUseDynamicPage()) {
            fillUsingDynamicComponentPO(roboFormDO);
        } else {
            fillUsingNormalComponentPO(roboFormDO);
        }
    }

    @Step("Fill Using Normal ComponentPO")
    private void fillUsingNormalComponentPO(RoboFormDO roboFormDO) {
        roboFormDO.getRoboFormPage().fill();
    }

    @Step("Fill Using Dynamic ComponentPO")
    private void fillUsingDynamicComponentPO(RoboFormDO roboFormDO) {
        roboFormDO.getRoboFormDynamicPage().fill();
    }

}
