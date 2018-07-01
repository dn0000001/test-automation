package com.automation.common.api.tests;

import com.automation.common.api.domainObjects.SoapGetHolidayDateDO;
import com.taf.automation.ui.support.testng.AllureTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * Sample SOAP test. Website: http://www.holidaywebservice.com
 */
@Listeners(AllureTestNGListener.class)
public class SoapHolidayTest {
    @Features("Holiday")
    @Stories("Get the date of a specific holiday.")
    @Severity(SeverityLevel.CRITICAL)
    @Parameters("data-soap-get-holiday-date")
    @Test
    public void performGetHolidayTest(@Optional("data/api/SoapGetHolidayDateTestData.xml") String dataSet) {
        SoapGetHolidayDateDO getHolidayDateDO = new SoapGetHolidayDateDO().fromResource(dataSet);
        getHolidayDateDO.sendRequest();
        getHolidayDateDO.validateStatus();
        getHolidayDateDO.validateResponse();
    }

}
