package com.taf.automation.ui.support;

import com.taf.automation.ui.support.testng.TestParameterValidator;
import com.thoughtworks.xstream.XStream;
import io.qameta.allure.Commands;
import io.qameta.allure.option.ConfigOptions;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.openqa.selenium.net.PortProber;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.reporters.Files;
import ru.yandex.qatools.commons.model.Environment;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    private static final String HOME = System.getProperty("user.home");
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final File REPORT_CLI = new File(HOME + SEPARATOR + "webdrivers" + SEPARATOR + "report-cli");
    private static final String ALLURE2_CLI = "reports/allure2-cli.zip";
    private static final File DEFAULT_RESULTS_DIRECTORY = new File("target/allure-results");
    private static final int DEFAULT_PORT = 8090;
    private String resultsFolder;
    private String reportFolder;
    private int port = -1;

    public TestRunner() {
        TestProperties props = TestProperties.getInstance();
        resultsFolder = DEFAULT_RESULTS_DIRECTORY.getAbsolutePath();
        reportFolder = props.getReportFolder().getAbsolutePath();
    }

    public int runTests(List<String> suites) throws IOException {
        for (String suite : suites) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(suite);
            File file = new File(suite);
            if (is != null) {
                FileUtils.copyInputStreamToFile(is, file);
            } else if (!file.exists()) {
                throw new IOException("Suite file '" + suite + "' does not exists on the file system!");
            }
        }

        TestNG testNg = new TestNG();
        testNg.addListener((ITestNGListener) new TestParameterValidator());
        testNg.setTestSuites(suites);
        testNg.setSuiteThreadPoolSize(1);
        testNg.run();
        saveEnvironment();
        return testNg.getStatus();
    }

    public void generateReport() throws ZipException, IOException {
        generateReport(reportFolder, resultsFolder);
    }

    public void generateReport(String outputFolder, String... inputFolders) throws ZipException, IOException {
        Path reportDirectory = new File(outputFolder).toPath();

        List<Path> resultsDirectories = new ArrayList<>();
        for (String inputFolder : inputFolders) {
            resultsDirectories.add(new File(inputFolder).toPath());
        }

        extractFilesForAllure2ReportGeneration();
        new Commands(REPORT_CLI.toPath()).generate(reportDirectory, resultsDirectories, false, new ConfigOptions());
    }

    /**
     * Extract files for allure 2 report generation<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>
     * For each plugin that are additional files that are required to generate the report properly.
     * For Behaviours, to appear in the allure 2 report there is a Javascript file that is needed.
     * Currently, I am unable to programmatically configure allure 2 such that these Javascript files are added.
     * </LI>
     * <LI>
     * The REPORT_CLI zip file contains the additional files needed to generate the allure 2 report properly.
     * The files come from the zip file that you can download from maven central for the
     * artifact <A HREF="https://search.maven.org/artifact/io.qameta.allure/allure-commandline/2.10.0/jar">
     * io.qameta.allure:allure-commandline:2.10.0</A>.  I have only put the behaviors plugin in the zip file.
     * </LI>
     * </OL>
     */
    private void extractFilesForAllure2ReportGeneration() throws ZipException, IOException {
        URL source = Thread.currentThread().getContextClassLoader().getResource(ALLURE2_CLI);
        File destination = new File(REPORT_CLI + SEPARATOR + ALLURE2_CLI);
        FileUtils.copyURLToFile(source, destination);
        ZipFile zipFile = new ZipFile(destination);
        zipFile.extractAll(REPORT_CLI.getAbsolutePath());
    }

    public void openReport() throws Exception {
        Server server = setUpServer();
        server.start();
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("http://localhost:" + getServerPort()));
        }

        server.join();
    }

    private int getServerPort() {
        if (port > 0) {
            return port;
        }

        try {
            // Try to find random free port
            port = PortProber.findFreePort();
        } catch (Exception ex) {
            // Fallback to default port
            port = DEFAULT_PORT;
        }

        // Check if a specific port is specified
        String p = System.getProperty("local.server.port");
        if (p != null) {
            port = Integer.parseInt(p);
        }

        return port;
    }

    private Server setUpServer() {
        Server server = new Server(getServerPort());
        ResourceHandler handler = new ResourceHandler();
        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(new String[]{"index.html"});
        handler.setResourceBase(reportFolder);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler, new DefaultHandler()});
        server.setStopAtShutdown(true);
        server.setHandler(handlers);
        server.setStopAtShutdown(true);
        return server;
    }

    public void saveEnvironment() throws IOException {
        TestProperties prop = TestProperties.getInstance();
        Environment environment = new Environment().withName("Environment");
        environment.withParameter(prop.getAsParameters());
        XStream xstream = new XStream();
        xstream.addImplicitArray(Environment.class, "parameter", "parameter");
        String xml = xstream.toXML(environment);
        xml = xml.replace("<ru.yandex.qatools.commons.model.Environment>", "<qa:environment xmlns:qa=\"urn:model.commons.qatools.yandex.ru\">");
        xml = xml.replace("</ru.yandex.qatools.commons.model.Environment>", "</qa:environment>");
        xml = xml.replace(" class=\"ru.yandex.qatools.commons.model.Parameter\"", "");
        File file = new File(resultsFolder + "/environment.xml");
        Files.writeFile(xml, file);
    }

    public void deleteResultsFolder() throws IOException {
        File resFolder = new File(resultsFolder);
        if (resFolder.exists()) {
            FileUtils.forceDelete(resFolder);
        }
    }

    public void deleteReportFolder() throws IOException {
        File resFolder = new File(reportFolder);
        if (resFolder.exists()) {
            FileUtils.forceDelete(resFolder);
        }
    }

    public void setReportFolder(String reportFolder) {
        this.reportFolder = reportFolder;
    }

}
