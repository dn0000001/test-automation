package com.automation.common.ui.app.pageObjects;

import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.AssertJUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic Main Page that has multiple tabs
 */
@SuppressWarnings("java:S3252")
public abstract class MainTabPage extends PageObjectV2 {
    protected MainTabPage() {
        super();
    }

    protected MainTabPage(TestContext context) {
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
        AssertJUtil.assertThat(index).as("Could not find tab containing name:  " + tabName).isGreaterThanOrEqualTo(0);

        List<SubTabPage> allTabs = getTabs();
        AssertJUtil.assertThat(index).as("Invalid Tab Index").isLessThan(allTabs.size());

        if (!allTabs.get(index).isCurrentlySelected()) {
            allTabs.get(index).click();
        }
    }

}
