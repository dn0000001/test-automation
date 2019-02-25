package com.automation.common.ui.run;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.TestRunner;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RunTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunTests.class);

    public static boolean isRunningOnJenkins() {
        return System.getenv("JENKINS_HOME") != null;
    }

    private static String generateSOXReport() throws IOException {
        List<TestSuiteResult> testSuiteResults = AllureFileUtils.unmarshalSuites(AllureResultsUtils.getResultsDirectory());
        StringBuilder report = new StringBuilder("\n******************* REPORT START *******************");

        for (TestSuiteResult suiteResult : testSuiteResults) {
            report.append("\n\nTEST SUITE : ").append(suiteResult.getName());
            DateFormat df = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
            String date = df.format(new Date(suiteResult.getStart()));
            report.append("\nEXECUTED ON: ").append(date);
            for (TestCaseResult testCaseResult : suiteResult.getTestCases()) {
                report.append("\n\t* TEST CASE: ");
                report.append(testCaseResult.getName());
                report.append("\t\tRESULT: ").append(testCaseResult.getStatus());
            }

            report.append("\n");
        }

        report.append("\n******************* REPORT END *******************");
        return report.toString();
    }

    public static void main(String[] args) throws Exception {
        int status;
        String msgTemplate = "\u001B[34m%s\u001B[0m";

        Date startTime = new Date();
        logInfo(String.format(msgTemplate, "Started At:  " + startTime));

        TestProperties props = TestProperties.getInstance();
        TestRunner runner = new TestRunner();

        logInfo(String.format(msgTemplate, "...CLEANING RESULT FOLDER..."));
        runner.deleteResultsFolder();

        logInfo(String.format(msgTemplate, "...CLEANING REPORT FOLDER..."));
        runner.deleteReportFolder();

        logInfo(String.format(msgTemplate, "...STARTING SUITE EXECUTION..."));
        status = runner.runTests(props.getSuites());
        logInfo(String.format(msgTemplate, "Status:  " + status));

        logInfo(String.format(msgTemplate, "...SUITE EXECUTION IS FINISHED, GENERATING ALLURE REPORT...\n" + generateSOXReport()));
        runner.generateReport();
        logInfo(String.format(msgTemplate, "...ALLURE REPORT IS GENERATED..."));

        Date endTime = new Date();
        logInfo(String.format(msgTemplate, "Completed At:  " + endTime));

        String duration = DurationFormatUtils.formatPeriod(startTime.getTime(), endTime.getTime(), "HH:mm:ss");
        logInfo(String.format(msgTemplate, "Duration:  " + duration));

        if (props.isShowReport() && !isRunningOnJenkins()) {
            logInfo(String.format(msgTemplate, "...OPENING ALLURE REPORT..."));
            runner.openReport();
        }

        System.exit(status);
    }

    /**
     * Wrapper method to log info message to fix the sonar violation "Invoke method(s) only conditionally"
     *
     * @param message - Message to be logged
     */
    private static void logInfo(String message) {
        LOGGER.info(message);
    }

}
