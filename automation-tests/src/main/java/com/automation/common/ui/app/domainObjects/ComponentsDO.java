package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.FakeComponentsPage;
import com.automation.common.ui.app.pageObjects.PrimeFacesSelectManyCheckboxPage;
import com.automation.common.ui.app.pageObjects.PrimeFacesSelectOneRadioPage;
import com.automation.common.ui.app.pageObjects.RubyWatirMultipleCheckBoxesPage;
import com.automation.common.ui.app.pageObjects.SeleniumEasyCheckBoxDemoPage;
import com.automation.common.ui.app.pageObjects.SeleniumEasyInputFormDemoPage;
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
    private RubyWatirMultipleCheckBoxesPage rubyWatirMultipleCheckBoxesPage;
    private SeleniumEasyCheckBoxDemoPage seleniumEasyCheckBoxDemoPage;
    private SeleniumEasyInputFormDemoPage seleniumEasyInputFormDemoPage;
    private PrimeFacesSelectManyCheckboxPage primeFacesSelectManyCheckboxPage;
    private PrimeFacesSelectOneRadioPage primeFacesSelectOneRadioPage;

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

    public RubyWatirMultipleCheckBoxesPage getRubyWatirMultipleCheckBoxesPage() {
        if (rubyWatirMultipleCheckBoxesPage == null) {
            rubyWatirMultipleCheckBoxesPage = new RubyWatirMultipleCheckBoxesPage();
        }

        if (rubyWatirMultipleCheckBoxesPage.getContext() == null) {
            rubyWatirMultipleCheckBoxesPage.initPage(getContext());
        }

        return rubyWatirMultipleCheckBoxesPage;
    }

    public SeleniumEasyCheckBoxDemoPage getSeleniumEasyCheckBoxDemoPage() {
        if (seleniumEasyCheckBoxDemoPage == null) {
            seleniumEasyCheckBoxDemoPage = new SeleniumEasyCheckBoxDemoPage();
        }

        if (seleniumEasyCheckBoxDemoPage.getContext() == null) {
            seleniumEasyCheckBoxDemoPage.initPage(getContext());
        }

        return seleniumEasyCheckBoxDemoPage;
    }

    public SeleniumEasyInputFormDemoPage getSeleniumEasyInputFormDemoPage() {
        if (seleniumEasyInputFormDemoPage == null) {
            seleniumEasyInputFormDemoPage = new SeleniumEasyInputFormDemoPage();
        }

        if (seleniumEasyInputFormDemoPage.getContext() == null) {
            seleniumEasyInputFormDemoPage.initPage(getContext());
        }

        return seleniumEasyInputFormDemoPage;
    }

    public PrimeFacesSelectManyCheckboxPage getPrimeFacesSelectManyCheckboxPage() {
        if (primeFacesSelectManyCheckboxPage == null) {
            primeFacesSelectManyCheckboxPage = new PrimeFacesSelectManyCheckboxPage();
        }

        if (primeFacesSelectManyCheckboxPage.getContext() == null) {
            primeFacesSelectManyCheckboxPage.initPage(getContext());
        }

        return primeFacesSelectManyCheckboxPage;
    }

    public PrimeFacesSelectOneRadioPage getPrimeFacesSelectOneRadioPage() {
        if (primeFacesSelectOneRadioPage == null) {
            primeFacesSelectOneRadioPage = new PrimeFacesSelectOneRadioPage();
        }

        if (primeFacesSelectOneRadioPage.getContext() == null) {
            primeFacesSelectOneRadioPage.initPage(getContext());
        }

        return primeFacesSelectOneRadioPage;
    }

}
