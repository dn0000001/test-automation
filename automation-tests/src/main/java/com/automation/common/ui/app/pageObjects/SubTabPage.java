package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;

/**
 * Generic Sub-Tab Page
 */
public abstract class SubTabPage extends PageObjectV2 {
    public SubTabPage() {
        super();
    }

    public SubTabPage(TestContext context) {
        super(context);
    }

    /**
     * Clicks the tab to make it become currently selected
     */
    public abstract void click();

    /**
     * Checks if the tab is currently selected
     *
     * @return true if currently selected else false
     */
    public abstract boolean isCurrentlySelected();

    /**
     * Get the tab name<BR>
     * <B>Note: </B> This should return a unique (in the tab grouping) hardcoded tab name of this page object
     *
     * @return the tab name
     */
    public abstract String getTabName();
}
