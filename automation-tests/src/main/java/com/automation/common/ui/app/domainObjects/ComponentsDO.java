package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.FakeComponentsPage;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Object to hold data for components data test
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("components-do")
public class ComponentsDO extends DomainObject {
    private FakeComponentsPage fakeComponentsPage;

    public ComponentsDO() {
        super();
    }

    public ComponentsDO(TestContext context) {
        super(context);
    }

    public FakeComponentsPage getFakeComponentsPage() {
        if (fakeComponentsPage == null) {
            fakeComponentsPage = new FakeComponentsPage();
        }

        if (fakeComponentsPage.getContext() == null) {
            fakeComponentsPage.initPage(getContext());
        }

        return fakeComponentsPage;
    }

}
