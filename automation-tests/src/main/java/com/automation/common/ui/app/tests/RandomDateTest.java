package com.automation.common.ui.app.tests;

import com.automation.common.ui.app.domainObjects.RandomDateUtil;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.TestContext;
import org.testng.annotations.Test;

/**
 * Random Date tests.<BR>
 * <B>Note: </B> Only 1 test can be run at a time as random date is only generated once per thread<BR>
 */
public class RandomDateTest {
    @Test
    public void testRandomDates() {
        Helper.log(RandomDateUtil.getInstance().random(), true);
    }

    @Test
    public void testRandomDates_MinOnly() {
        TestContext.getGlobalAliases().put("random_date_range_min", "10");
        Helper.log(RandomDateUtil.getInstance().random(), true);
    }

    @Test
    public void testRandomDates_MaxOnly() {
        TestContext.getGlobalAliases().put("random_date_range_max", "-10");
        Helper.log(RandomDateUtil.getInstance().random(), true);
    }

    @Test
    public void testRandomDates_PatternOnly() {
        TestContext.getGlobalAliases().put("random_date_pattern", "MMMM dd, yyyy");
        Helper.log(RandomDateUtil.getInstance().random(), true);
    }

    @Test
    public void testRandomDates_MinMax() {
        TestContext.getGlobalAliases().put("random_date_range_min", "-30");
        TestContext.getGlobalAliases().put("random_date_range_max", "30");
        Helper.log(RandomDateUtil.getInstance().random(), true);
    }

    @Test
    public void testRandomDates_All() {
        TestContext.getGlobalAliases().put("random_date_range_min", "-90");
        TestContext.getGlobalAliases().put("random_date_range_max", "90");
        TestContext.getGlobalAliases().put("random_date_pattern", "MMMM dd, yyyy");
        Helper.log(RandomDateUtil.getInstance().random(), true);
    }
}
