package com.taf.automation.api.tests;

import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.taf.automation.api.domainObjects.JsonIpDO;
import com.taf.automation.ui.support.testng.AllureTestNGListener;

/**
 * Sample API test. Website: http://jsonip.com
 */
@Listeners(AllureTestNGListener.class)
public class JsonIpTest {
    @Features("IP")
    @Stories("GET IP")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-json-ip")
    @Test
    public void getIP(@Optional("data/api/JsonIp_TestData.xml") String dataSet) {
        JsonIpDO ip = new JsonIpDO().fromResource(dataSet);
        ip.getIP();
        ip.validateStatus();
        ip.validateResponse();
    }

}
