package com.automation.common.ui.app.tests;

import com.taf.automation.ui.support.testng.Retry;
import com.taf.automation.ui.support.testng.TestNGBase;
import org.apache.commons.lang3.mutable.MutableInt;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class RetryTest extends TestNGBase {
    private MutableInt attempt;

    @Test
    @Retry
    public void testThatPassesOnRetry() {
        if (attempt == null) {
            attempt = new MutableInt(0);
            assertThat("Always fails 1st time", false);
        }
    }

}
