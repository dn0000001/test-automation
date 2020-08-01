package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.LinksDO;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.testng.TestNGBase;
import net.jodah.failsafe.Failsafe;
import org.apache.commons.lang3.mutable.MutableInt;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Example test using a data provider
 */
public class DataProviderTest extends TestNGBase {
    private List<String> links;

    @BeforeTest
    public void setup(ITestContext context) {
        String dataSet = context.getCurrentXmlTest().getParameter(context.getName());
        LinksDO linksDO = new LinksDO().fromResource(dataSet);
        links = linksDO.getLinks();
    }

    @DataProvider(name = "linkData", parallel = true)
    public Iterator<Object[]> dataProvider() {
        List<Object[]> linkList = new ArrayList<>();
        for (String link : links) {
            linkList.add(new String[]{link});
        }

        return linkList.iterator();
    }

    @Features("TestNG")
    @Stories("Data Provider")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "linkData")
    public void testLinks(String link) {
        MutableInt attempt = new MutableInt(1);
        Failsafe.with(Utils.getRetryPolicy()).run(() -> testLinks(attempt, link));
    }

    /**
     * Perform validation of links
     *
     * @param attempt - Attempt number
     * @param link    - Link to validate
     */
    @Step("Validate URL - Attempt #{0}")
    private void testLinks(MutableInt attempt, String link) {
        attempt.increment();
        LinksDO.validateUrl(link);
    }

}
