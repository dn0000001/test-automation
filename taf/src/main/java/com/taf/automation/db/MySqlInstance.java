package com.taf.automation.db;

import com.taf.automation.api.network.SSHSession;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Class to work with a MYSQL database
 */
@SuppressWarnings("squid:S00112")
public class MySqlInstance extends DBInstance {
    private MySqlInstance() {
        resetDBConnectionToDefault();
    }

    private static class LazyHolder {
        private static final MySqlInstance INSTANCE = new MySqlInstance();
    }

    public static MySqlInstance getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    protected void createDBConnection(int connectionTimeout, int socketTimeout, int queryTimeout) {
        TestProperties props = TestProperties.getInstance();
        int dbPort = props.getDbPort();
        String dbHost = props.getDbHost();
        if (System.getenv("JENKINS_HOME") == null && props.getSshHost() != null) {
            sshSession = new SSHSession(dbHost, dbPort);
            dbHost = "localhost";
            dbPort = sshSession.getPort();
        } else {
            dbPort = props.getDbPort();
            dbHost = props.getDbHost();
        }

        // Database (name) to be used
        String database = StringUtils.defaultString(props.getDbName());

        // Construct Parameters
        // Note: It will produce "?param1=value1&param2=value2&paramN=valueN" or empty string
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme("jdbc:mysql")
                    .setHost(dbHost)
                    .setPort(dbPort)
                    .setPath(database)
                    //.addParameter("connectTimeout", String.valueOf(props.getDbTimeout()))
                    //.addParameter("socketTimeout", String.valueOf(props.getDbTimeout()))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Properties connectionProps = new Properties();
        connectionProps.setProperty("connectTimeout", String.valueOf(connectionTimeout));
        connectionProps.setProperty("socketTimeout", String.valueOf(socketTimeout));

        DriverManagerDataSource dataSource = new DriverManagerDataSource(uri.toString(), connectionProps);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setConnectionProperties(connectionProps);
        dataSource.setUsername(props.getDbUserName());
        dataSource.setPassword(new CryptoUtils().decrypt(props.getDbPassword()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(queryTimeout);
    }

}
