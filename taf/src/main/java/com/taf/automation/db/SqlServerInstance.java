package com.taf.automation.db;

import com.taf.automation.ui.support.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;

/**
 * Class to work with a SQL Server database
 */
public class SqlServerInstance extends DBInstance {
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
        String connectionString = "jdbc:sqlserver://" + props.getDbHost() + ":" + props.getDbPort() + ";";
        if (props.getDbName() != null) {
            connectionString += "databaseName=" + props.getDbName() + ";";
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

}
