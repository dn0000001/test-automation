package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.PrimeFacesDashboardPage;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("prime-faces-dashboard-do")
public class PrimeFacesDashboardDO extends DomainObject {
    private PrimeFacesDashboardPage primeFacesDashboardPage;

    public PrimeFacesDashboardDO() {
        super();
    }

    public PrimeFacesDashboardDO(TestContext context) {
        super(context);
    }

    public PrimeFacesDashboardPage getPrimeFacesDashboardPage() {
        if (primeFacesDashboardPage == null) {
            primeFacesDashboardPage = new PrimeFacesDashboardPage();
        }

        if (primeFacesDashboardPage.getContext() == null) {
            primeFacesDashboardPage.initPage(getContext());
        }

        return primeFacesDashboardPage;
    }

}
