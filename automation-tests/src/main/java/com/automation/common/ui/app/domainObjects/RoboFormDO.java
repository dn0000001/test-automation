package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.RoboFormPage;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("robo-form-do")
public class RoboFormDO extends DomainObject {
    private RoboFormPage roboFormPage;

    public RoboFormDO() {
        super();
    }

    public RoboFormDO(TestContext context) {
        super(context);
    }

    public RoboFormPage getRoboFormPage() {
        if (roboFormPage == null) {
            roboFormPage = new RoboFormPage();
        }

        if (roboFormPage.getContext() == null) {
            roboFormPage.initPage(getContext());
        }

        return roboFormPage;
    }

}
