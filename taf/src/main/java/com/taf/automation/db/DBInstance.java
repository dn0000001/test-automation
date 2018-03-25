package com.taf.automation.db;

import com.taf.automation.api.network.SSHSession;
import com.taf.automation.ui.support.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Class to work with a MYSQL database
 */
public class DBInstance {
    private static final Logger LOG = LoggerFactory.getLogger(DBInstance.class);
    private SSHSession sshSession;
    private JdbcTemplate jdbcTemplate;
    public static DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private DBInstance() {
        createDBConnection();
    }

    private static class LazyHolder {
        private static final DBInstance INSTANCE = new DBInstance();
    }

    public static DBInstance getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void createDBConnection() {
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
        connectionProps.setProperty("connectTimeout", String.valueOf(props.getDbTimeout()));
        connectionProps.setProperty("socketTimeout", String.valueOf(props.getDbTimeout()));

        DriverManagerDataSource dataSource = new DriverManagerDataSource(uri.toString(), connectionProps);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setConnectionProperties(connectionProps);
        dataSource.setUsername(props.getDbUserName());
        dataSource.setPassword(new CryptoUtils().decrypt(props.getDbPassword()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(props.getDbQueryTimeout());
    }

    public JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }

    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void close() {
        sshSession.close();
    }

    public <T> T queryForObject(String query, SqlParameterSource sqlParameterSource, Class<T> clazz) {
        try {
            return namedParameterJdbcTemplate().queryForObject(query, sqlParameterSource, clazz);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public <T> T queryForObject(String query, SqlParameterSource sqlParameterSource, RowMapper<T> rowMapper) {
        try {
            return namedParameterJdbcTemplate().queryForObject(query, sqlParameterSource, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Query given SQL to create a prepared statement from SQL and a list of arguments to bind to the query, expecting a SqlRowSet.
     *
     * @param query              - SQL Query to be executed
     * @param sqlParameterSource - Container of arguments to bind to the query
     * @return A SqlRowSet representation (possibly a wrapper around a javax.sql.rowset.CachedRowSet)
     */
    public SqlRowSet queryForRowSet(String query, SqlParameterSource sqlParameterSource) {
        return namedParameterJdbcTemplate().queryForRowSet(query, sqlParameterSource);
    }

    public int update(String query, SqlParameterSource sqlParameterSource) {
        return namedParameterJdbcTemplate().update(query, sqlParameterSource);
    }

}
