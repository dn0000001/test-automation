package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.AddressProviderDO;
import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.Lookup;
import com.taf.automation.ui.support.providers.AddressProvider;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.Matchers.equalTo;

public class AddressProviderTest extends TestNGBase {
    @Features("Framework")
    @Stories("Validate that custom provider works in Jexl Expressions")
    @Severity(SeverityLevel.NORMAL)
    @Parameters({
            "data-set",
            "state"
    })
    @Test
    public void testProvider(
            @Optional("data/ui/GenericAddress_TestData.xml") String dataSet,
            @Optional("CO") String state
    ) {
        // Store the state based on the parameter such that the test data knows the state to use
        Lookup.getInstance().put("state", state);

        // For this test, we want to resolve the aliases on load because we are using not using a component
        AddressProviderDO addressProviderDO = new AddressProviderDO(getContext()).fromResource(dataSet, true);

        // Due to the address data some fields will be null which means they are treated as literal strings by JEXL
        // So, use the Helper.assertThat which skips expected fields that are null to avoid validation issues
        AssertAggregator aggregator = new AssertAggregator();
        Helper.assertThat(aggregator, addressProviderDO.getAddressInfo(), AddressProvider.getInstance().get(state));
        aggregator.assertThat(
                "State Address Count",
                NumberUtils.toInt(addressProviderDO.getAddressCount(), -1),
                equalTo(AddressProvider.getInstance().getAll(state).size())
        );
        Helper.assertThat(aggregator);
    }

}
