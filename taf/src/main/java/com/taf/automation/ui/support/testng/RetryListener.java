package com.taf.automation.ui.support.testng;

import com.taf.automation.ui.support.Lookup;
import org.testng.ITestResult;
import org.testng.util.RetryAnalyzerCount;

public class RetryListener extends RetryAnalyzerCount {
    private static final String LOOKUP_KEY = "testng-retry-value";

    /**
     * The number of retries is based on the lookup key from the Lookup class.  If the key is not found, then
     * zero will be used which causes no retries to occur.
     */
    public RetryListener() {
        setCount((int) Lookup.getInstance().getOrDefault(getLookupKey(), 0));
    }

    public static String getLookupKey() {
        return LOOKUP_KEY;
    }

    @Override
    public boolean retryMethod(ITestResult result) {
        // No special logic retry is necessary just always retry
        return true;
    }

}
