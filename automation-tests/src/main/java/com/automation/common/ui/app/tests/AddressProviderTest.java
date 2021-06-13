package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.AddressProviderDO;
import com.taf.automation.asserts.CustomSoftAssertions;
import com.taf.automation.ui.support.Lookup;
import com.taf.automation.ui.support.providers.AddressProvider;
import com.taf.automation.ui.support.testng.TestNGBase;
import com.taf.automation.ui.support.util.Helper;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;

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
        CustomSoftAssertions softly = new CustomSoftAssertions();
        Helper.assertThatObject("Address Info", softly, addressProviderDO.getAddressInfo(), AddressProvider.getInstance().get(state));
        softly.assertThat(NumberUtils.toInt(addressProviderDO.getAddressCount(), -1))
                .as("State Address Count")
                .isEqualTo(AddressProvider.getInstance().getAll(state).size());
        softly.assertThat(addressProviderDO.getPi())
                .as("PI (to 11 decimal places)")
                .isEqualByComparingTo("3.14159265359");
        softly.assertAll();
    }

}
