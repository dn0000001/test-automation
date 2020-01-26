package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.RoboFormDO;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

public class RoboFormTest extends TestNGBase {
    @Features("Framework")
    @Stories("Using setElementValueV2 with ComponentPO")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"url", "data-set"})
    @Test
    public void testSetElementValueV2WithComponentPO(
            @Optional("https://www.roboform.com/filling-test-all-fields") String url,
            @Optional("data/ui/RoboForm_TestData.xml") String dataSet
    ) {
        getContext().getDriver().get(url);
        RoboFormDO roboFormDO = new RoboFormDO(getContext()).fromResource(dataSet);
        roboFormDO.getRoboFormPage().fill();
    }

}
