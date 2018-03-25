package com.taf.automation.ui.run;

import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.TestRunner;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RunTests {
    final static Logger logger = LoggerFactory.getLogger(RunTests.class);

    public static boolean isRunningOnJenkins() {
        return System.getenv("JENKINS_HOME") != null;
    }

    private static String generateSOXReport() throws Exception {
        List<TestSuiteResult> testSuiteResults = AllureFileUtils.unmarshalSuites(AllureResultsUtils.getResultsDirectory());
        StringBuilder report = new StringBuilder("\n******************* REPORT START *******************");

        for (TestSuiteResult suiteResult : testSuiteResults) {
            report.append("\n\nTEST SUITE : ").append(suiteResult.getName());
            DateFormat df = new SimpleDateFormat("MMM dd, YYYY HH:mm:ss");
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
        logger.info(String.format(msgTemplate, "Started At:  " + startTime));

        TestProperties props = TestProperties.getInstance();
        TestRunner runner = new TestRunner();

        logger.info(String.format(msgTemplate, "...CLEANING RESULT FOLDER..."));
        runner.deleteResultsFolder();

        logger.info(String.format(msgTemplate, "...CLEANING REPORT FOLDER..."));
        runner.deleteReportFolder();

        logger.info(String.format(msgTemplate, "...STARTING SUITE EXECUTION..."));
        status = runner.runTests(props.getSuites());

        logger.info(String.format(msgTemplate, "...SUITE EXECUTION IS FINISHED, GENERATING ALLURE REPORT..."));
        logger.info(String.format(msgTemplate, "...SUITE EXECUTION IS FINISHED, GENERATING ALLURE REPORT...\n" + generateSOXReport()));
        runner.generateReport();
        logger.info(String.format(msgTemplate, "...ALLURE REPORT IS GENERATED..."));

        Date endTime = new Date();
        logger.info(String.format(msgTemplate, "Completed At:  " + endTime));

        String duration = DurationFormatUtils.formatPeriod(startTime.getTime(), endTime.getTime(), "HH:mm:ss");
        logger.info(String.format(msgTemplate, "Duration:  " + duration));

        if (props.isShowReport() && !isRunningOnJenkins()) {
            logger.info(String.format(msgTemplate, "...OPENING ALLURE REPORT..."));
            runner.openReport();
        }

        System.exit(status);
    }

}
