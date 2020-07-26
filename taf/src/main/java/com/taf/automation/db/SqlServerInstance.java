package com.taf.automation.db;

import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Class to work with a SQL Server database
 */
public class SqlServerInstance extends DBInstance {
    private static final ReentrantLock lockFile = new ReentrantLock();
    private static final String HOME = System.getProperty("user.home");
    private static final String OS = System.getProperty("os.name").toUpperCase();
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String DLL = "sqljdbc_auth.dll";
    private static final String DLL_RESOURCE_FOLDER = "sql-auth/";
    private static final String INSTALL_DLL = HOME + SEPARATOR + "webdrivers" + SEPARATOR + "sql-auth";
    private static final String JAVA_LIBRARY_PATH = "java.library.path";

    private SqlServerInstance() {
        resetDBConnectionToDefault();
    }

    private static class LazyHolder {
        private static final SqlServerInstance INSTANCE = new SqlServerInstance();
    }

    public static SqlServerInstance getInstance() {
        return SqlServerInstance.LazyHolder.INSTANCE;
    }

    @Override
    protected void createDBConnection(int connectionTimeout, int socketTimeout, int queryTimeout) {
        TestProperties props = TestProperties.getInstance();
        String connectionString = "jdbc:sqlserver://" + props.getDbHost();
        if (props.getDbPort() > 0) {
            connectionString += ":" + props.getDbPort();
        }

        connectionString += ";";
        if (props.getDbName() != null) {
            connectionString += "databaseName=" + props.getDbName() + ";";
        }

        if (props.isDbIntegratedSecurity()) {
            setupForIntegratedSecurity();
            connectionString += "integratedSecurity=true;";
        }

        Properties connectionProps = new Properties();
        connectionProps.setProperty("loginTimeout", String.valueOf(connectionTimeout));
        connectionProps.setProperty("socketTimeout", String.valueOf(socketTimeout));

        DriverManagerDataSource dataSource = new DriverManagerDataSource(connectionString, connectionProps);
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setConnectionProperties(connectionProps);
        dataSource.setUsername(props.getDbUserName());
        dataSource.setPassword(new CryptoUtils().decrypt(props.getDbPassword()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(queryTimeout);
    }

    private void setupForIntegratedSecurity() {
        lockFile.lock();
        try {
            URL source = Thread.currentThread().getContextClassLoader().getResource(getResource());
            File destination = new File(INSTALL_DLL + SEPARATOR + DLL);
            FileUtils.copyURLToFile(source, destination);

            String path = INSTALL_DLL + ";" + System.getProperty(JAVA_LIBRARY_PATH);
            System.setProperty(JAVA_LIBRARY_PATH, path);

            // Note:  java.library.path is only read once by the JVM.  This is a workaround to get it read again
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (Exception ex) {
            String reason = "Could not setup for integrated security due to exception:  " + ex.getMessage();
            LOG.error(reason);
            assertThat(reason, false);
        } finally {
            lockFile.unlock();
        }
    }

    private String getResource() {
        if (!OS.startsWith("WINDOWS")) {
            throw new UnsupportedOperationException("Integrated Security is only supported on Windows");
        }

        // This assumes that on 64-bit machine you installed the 64-bit version of Java
        // If this is not the case, then there will probably be issues.
        boolean is64bit = System.getenv("ProgramFiles(x86)") != null;
        String winOS = (is64bit) ? "win64" : "win32";
        return DLL_RESOURCE_FOLDER + winOS + "/" + DLL;
    }

}
