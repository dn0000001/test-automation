package com.automation.common.ui.app.tests;

import com.automation.common.api.domainObjects.JsonIpDO;
import com.automation.common.ui.app.domainObjects.AddressProviderDO;
import com.automation.common.ui.app.domainObjects.CSV_DO;
import com.automation.common.ui.app.domainObjects.ComponentsDO;
import com.automation.common.ui.app.domainObjects.JexlExpressionDO;
import com.automation.common.ui.app.domainObjects.LinksDO;
import com.automation.common.ui.app.domainObjects.PrimeFacesDashboardDO;
import com.automation.common.ui.app.domainObjects.RoboFormDO;
import com.automation.common.ui.app.domainObjects.RowMatchingDO;
import com.automation.common.ui.app.domainObjects.TNHC_DO;
import com.automation.common.ui.app.pageObjects.FakeComponentsPage;
import com.automation.common.ui.app.pageObjects.HerokuappDataTablesPage;
import com.automation.common.ui.app.pageObjects.HerokuappRow;
import com.taf.automation.ui.support.testng.TestNGBase;
import datainstiller.data.DataPersistence;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * This class validates the generation of the XML test Data which is a key feature of the framework.  Complex generic
 * classes cannot be generated.  (There may be other types of classes as well.)  So, you should validate the XML
 * generation of the most common (or all) domain objects and any page objects that are of concern.<BR>
 * <B>Note: </B> The generation may fail but the loading of the resource will most likely be fine but this probably
 * should be tested when/if it occurs.
 */
public class GenerateXmlTest extends TestNGBase {
    @Features("Framework")
    @Stories("Validate that XML generation works properly")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void validateGenerationOfXmlTest() {
        generateXml("AddressProviderDO", new AddressProviderDO());
        generateXml("ComponentsDO", new ComponentsDO());
        generateXml("CSV_DO", new CSV_DO());
        generateXml("JexlExpressionDO", new JexlExpressionDO());
        generateXml("LinksDO", new LinksDO());
        generateXml("PrimeFacesDashboardDO", new PrimeFacesDashboardDO());
        generateXml("RoboFormDO", new RoboFormDO());
        generateXml("RowMatchingDO", new RowMatchingDO());
        generateXml("TNHC_DO", new TNHC_DO());

        generateXml("JsonIpDO", new JsonIpDO());

        generateXml("FakeComponentsPage", new FakeComponentsPage());
        generateXml("HerokuappRow", new HerokuappRow());
        generateXml("HerokuappDataTablesPage", new HerokuappDataTablesPage());
    }

    @Step("Generate XML for {0}")
    private void generateXml(String log, DataPersistence object) {
        object.generate();
    }

}
