package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.RoboFormDynamicPage;
import com.automation.common.ui.app.pageObjects.RoboFormPage;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.providers.KeyValueProvider;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.jexl3.JexlContext;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("robo-form-do")
public class RoboFormDO extends DomainObject {
    private boolean useDynamicPage;
    private RoboFormPage roboFormPage;
    private RoboFormDynamicPage roboFormDynamicPage;

    public RoboFormDO() {
        super();
    }

    public RoboFormDO(TestContext context) {
        super(context);
    }

    @Override
    protected void initJexlContext(JexlContext jexlContext) {
        super.initJexlContext(jexlContext);
        jexlContext.set("keyValues", KeyValueProvider.getInstance());
    }

    public boolean isUseDynamicPage() {
        return useDynamicPage;
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

    public RoboFormDynamicPage getRoboFormDynamicPage() {
        if (roboFormDynamicPage == null) {
            roboFormDynamicPage = new RoboFormDynamicPage();
        }

        if (roboFormDynamicPage.getContext() == null) {
            roboFormDynamicPage.initPage(getContext());
        }

        return roboFormDynamicPage;
    }

}
