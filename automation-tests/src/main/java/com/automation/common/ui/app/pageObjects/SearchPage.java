package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search Page (Google)
 */
public class SearchPage extends PageObjectV2 {
    private Map<String, String> substitutions;

    @FindBy(css = "${key}")
    List<WebElement> containers;

    public SearchPage() {

    }

    public SearchPage(TestContext context) {
        super(context);
    }

    /**
     * Initialize page for dynamic locators
     *
     * @param context - Context
     */
    public void initPage(TestContext context) {
        Helper.log("Dynamic Locator Initialization", true);
        initPage(context, getSubstitutions());
    }

    private Map<String, String> getSubstitutions() {
        if (substitutions == null) {
            substitutions = new HashMap<>();
        }

        return substitutions;
    }

    public void initDefaultSubstitutions() {
        addSubstitution("key", ".q");
    }

    public void addSubstitution(String key, String value) {
        substitutions = getSubstitutions();
        substitutions.put(key, value);
    }

    public int getContainersSize() {
        return containers.size();
    }

}
