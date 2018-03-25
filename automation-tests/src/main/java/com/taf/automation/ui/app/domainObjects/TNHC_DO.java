package com.taf.automation.ui.app.domainObjects;

import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.app.pageObjects.TNHC_LandingPage;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Object to hold data for True North Hockey Canada
 */
@XStreamAlias("true-north-hockey-canada")
public class TNHC_DO extends DomainObject {
    TNHC_LandingPage landing;

    public TNHC_DO() {

    }

    public TNHC_DO(TestContext context) {
        super(context);
    }

    public TNHC_LandingPage getLanding() {
        if (landing == null) {
            landing = new TNHC_LandingPage();
        }

        landing.initDefaultSubstitutions();

        if (landing.getContext() == null) {
            landing.initPage(getContext());
        }

        return landing;
    }

}
