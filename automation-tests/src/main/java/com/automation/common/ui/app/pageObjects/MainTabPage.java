package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

/**
 * Generic Main Page that has multiple tabs
 */
public abstract class MainTabPage extends PageObjectV2 {
    public MainTabPage() {
        super();
    }

    public MainTabPage(TestContext context) {
        super(context);
    }

    /**
     * Get all tabs
     *
     * @return List&lt;SubTabPage&gt;
     */
    protected abstract List<SubTabPage> getTabs();

    /**
     * Get all tab names
     *
     * @return List&lt;String&gt;
     */
    protected List<String> getTabsList() {
        List<String> all = new ArrayList<>();

        for (SubTabPage tab : getTabs()) {
            all.add(tab.getTabName());
        }

        return all;
    }

    /**
     * Find the tab containing the specified name ignoring case
     *
     * @param tabName - Tab Name
     * @return -1 if no tab with specified name found, else greater or equal to 0
     */
    protected int findTab(String tabName) {
        int index = -1;
        for (int i = 0; i < getTabs().size(); i++) {
            if (StringUtils.containsIgnoreCase(getTabs().get(i).getTabName(), tabName)) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Click Tab that contains specified name
     *
     * @param tabName - Tab Name to click
     */
    protected void clickTab(String tabName) {
        int index = findTab(tabName);
        assertThat("Could not find tab containing name:  " + tabName, index, greaterThanOrEqualTo(0));

        List<SubTabPage> allTabs = getTabs();
        assertThat("Invalid Tab Index", index, lessThan(allTabs.size()));

        if (!allTabs.get(index).isCurrentlySelected()) {
            allTabs.get(index).click();
        }
    }

}
