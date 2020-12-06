package com.taf.automation.ui.support.testng;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseCanceledEvent;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.testng.AllureTestListener;

import java.lang.reflect.Field;
import java.util.Set;

public class AllureTestNGListener extends AllureTestListener {
    @Override
    public void onStart(ITestContext iTestContext) {
        super.onStart(iTestContext);
        for (ITestNGMethod method : iTestContext.getAllTestMethods()) {
            Retry retry = method.getConstructorOrMethod().getMethod().getAnnotation(Retry.class);
            if (isCandidateForRetry(method) && method.getRetryAnalyzer() == null && retry != null) {
                int times = (retry.value() == 0) ? TestProperties.getInstance().getTestDefaultRetry() : retry.value();
                method.setRetryAnalyzer(new RetryListener(times));
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        super.onTestSuccess(iTestResult);
        if (iTestResult.getMethod().getRetryAnalyzer() != null) {
            // Workaround issue with retries in the TestNG logic added in 6.12
            iTestResult.getTestContext().getSkippedTests().removeResult(iTestResult.getMethod());
            iTestResult.getTestContext().getFailedTests().removeResult(iTestResult.getMethod());
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        iTestResult.setThrowable(ExceptionUtils.clean(iTestResult.getThrowable()));
        Allure.LIFECYCLE.fire(new TestCaseFailureEvent().withThrowable(iTestResult.getThrowable()));
        TestNGBaseWithoutListeners.takeScreenshot("Failed Test Screenshot");
        TestNGBaseWithoutListeners.takeHTML("Failed Test HTML Source");
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent());
    }

    @SuppressWarnings({"java:S3011", "java:S2259"})
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Set<String> startedTestNames = null;
        try {
            Field field = getClass().getSuperclass().getDeclaredField("startedTestNames");
            field.setAccessible(true);
            startedTestNames = (Set<String>) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (!startedTestNames.contains(getName(iTestResult))) {
            onTestStart(iTestResult);
        }

        if (iTestResult.getMethod().getRetryAnalyzer() != null) {
            iTestResult.getTestContext().getSkippedTests().removeResult(iTestResult.getMethod());
            TestNGBaseWithoutListeners.takeScreenshot("Skipped Test Screenshot");
            TestNGBaseWithoutListeners.takeHTML("Skipped Test HTML Source");
        }

        fireTestCaseCancel(iTestResult);
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent());
    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
        TestNGBaseWithoutListeners.takeScreenshot("Configuration Failure Screenshot");
        TestNGBaseWithoutListeners.takeHTML("Configuration Failure HTML Source");
        iTestResult.setThrowable(ExceptionUtils.clean(iTestResult.getThrowable()));
        super.onConfigurationFailure(iTestResult);
    }

    @Override
    public void onConfigurationSkip(ITestResult iTestResult) {
        // Configuration method will be shown in the report only on its failure in any other situation it is not listed
        // Note:  If this is not the behavior desire and you want the default Allure behavior then remove this method.
    }

    @SuppressWarnings("java:S2177")
    private void fireTestCaseCancel(ITestResult iTestResult) {
        Throwable skipMessage = iTestResult.getThrowable();
        if (skipMessage == null) {
            skipMessage = new Throwable() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getMessage() {
                    return "Skipped due to dependency on other skipped or failed test methods";
                }
            };
        }

        Allure.LIFECYCLE.fire(new TestCaseCanceledEvent().withThrowable(skipMessage));
    }

    @SuppressWarnings("java:S2177")
    private String getName(ITestResult iTestResult) {
        String suitePrefix = getCurrentSuiteTitle(iTestResult.getTestContext());
        StringBuilder sb = new StringBuilder(suitePrefix);
        sb.append(iTestResult.getName());
        Object[] parameters = iTestResult.getParameters();
        if (parameters != null && parameters.length > 0) {
            sb.append("[");
            for (Object parameter : parameters) {
                sb.append(parameter).append(",");
            }

            sb.replace(sb.length() - 1, sb.length(), "]");
        }

        return sb.toString();
    }

    @SuppressWarnings("java:S2177")
    private String getCurrentSuiteTitle(ITestContext iTestContext) {
        String suite = iTestContext.getSuite().getName();
        String xmlTest = iTestContext.getCurrentXmlTest().getName();
        String params = "";

        if (!iTestContext.getCurrentXmlTest().getLocalParameters().isEmpty()) {
            params = iTestContext.getCurrentXmlTest().getLocalParameters()
                    .toString().replace("{", "[").replace("}", "]");
        }

        return "{" + suite + " : " + xmlTest + params + "}";
    }

    /**
     * Check if method is a candidate for retry
     *
     * @return true if method can could be retried, false if retry should never be attempted
     */
    @SuppressWarnings("java:S1126")
    private boolean isCandidateForRetry(ITestNGMethod method) {
        // Annotation:  @Test
        if (method.isTest()) {
            return true;
        }

        // Annotation:  @BeforeTest
        if (method.isBeforeTestConfiguration()) {
            return true;
        }

        // Annotation:  @AfterTest
        if (method.isAfterTestConfiguration()) {
            return true;
        }

        // Annotation:  @BeforeMethod
        if (method.isBeforeMethodConfiguration()) {
            return true;
        }

        // Annotation:  @AfterMethod
        if (method.isAfterMethodConfiguration()) {
            return true;
        }

        return false;
    }

}
