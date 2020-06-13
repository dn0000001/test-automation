package com.automation.common.ui.app.domainObjects;

import com.taf.automation.expressions.USAddress;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.providers.AddressProvider;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.jexl3.JexlContext;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("address-provider-do")
public class AddressProviderDO extends DomainObject {
    private USAddress addressInfo;

    /**
     * The address count stored as a string.  Since we are using an alias to calculate this value,
     * it must be a string because all aliases are strings when assigned to the variable.
     */
    private String addressCount;

    public AddressProviderDO() {
        super();
    }

    public AddressProviderDO(TestContext context) {
        super(context);
    }

    @Override
    protected void initJexlContext(JexlContext jexlContext) {
        super.initJexlContext(jexlContext);
        jexlContext.set("addresses", AddressProvider.getInstance());
    }

    public USAddress getAddressInfo() {
        if (addressInfo == null) {
            addressInfo = new USAddress();
        }

        return addressInfo;
    }

    public String getAddressCount() {
        return addressCount;
    }

}
