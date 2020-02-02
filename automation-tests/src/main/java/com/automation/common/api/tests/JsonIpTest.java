package com.automation.common.api.tests;

import com.automation.common.api.domainObjects.JsonIpDO;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

/**
 * Sample API test. Website: https://jsonip.com
 */
@Listeners(AllureTestNGListener.class)
public class JsonIpTest {
    @Features("IP")
    @Stories("GET IP")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters({"api-url", "data-json-ip"})
    @Test
    public void getIP(
            @Optional("https://jsonip.com") String apiUrl,
            @Optional("data/api/JsonIp_TestData.xml") String dataSet
    ) {
        assertThat("API URL", apiUrl, equalToIgnoringCase(TestProperties.getInstance().getApiUrl()));
        JsonIpDO ip = new JsonIpDO().fromResource(dataSet);
        ip.getIP();
        ip.validateStatus();
        ip.validateResponse();
    }

}
