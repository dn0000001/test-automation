package com.taf.automation.db;

import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;

/**
 * Class to work with a Oracle database<BR>
 * <B>Notes:</B><BR>
 * 1) Requires the manual installation of the oracle driver into the local repository.<BR>
 * 2) Need to download the oracle driver.<BR>
 * 3) The POM taf/pom.xml contains sample dependency information<BR>
 * <B>Command to manually install using maven:</B><BR>
 * mvn install:install-file -Dfile=&lt;path-to-file&gt; -DgroupId=&lt;group-id&gt; -DartifactId=&lt;artifact-id&gt;
 * -Dversion=&lt;version&gt; -Dpackaging=&lt;packaging&gt;<BR>
 */
public class OracleInstance extends DBInstance {
    private OracleInstance() {
        resetDBConnectionToDefault();
    }

    private static class LazyHolder {
        private static final OracleInstance INSTANCE = new OracleInstance();
    }

    public static OracleInstance getInstance() {
        return OracleInstance.LazyHolder.INSTANCE;
    }

    @Override
    @SuppressWarnings("squid:CommentedOutCodeLine")
    protected void createDBConnection(int connectionTimeout, int socketTimeout, int queryTimeout) {
        TestProperties props = TestProperties.getInstance();
        String connectionString = "jdbc:oracle:thin:@" + props.getDbHost() + ":" + props.getDbPort();
        if (props.getDbName() != null) {
            // Using service name as such use slash
            connectionString += "/" + props.getDbName();
        }

        Properties connectionProps = new Properties();
        //
        // Preform manual installation of the oracle driver into the local repository before uncommenting these lines.
        //

        // connectionProps.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_CONNECTION_TIMEOUT, String.valueOf(connectionTimeout));
        // connectionProps.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_READ_TIMEOUT, String.valueOf(socketTimeout));

        DriverManagerDataSource dataSource = new DriverManagerDataSource(connectionString, connectionProps);
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setConnectionProperties(connectionProps);
        dataSource.setUsername(props.getDbUserName());
        dataSource.setPassword(new CryptoUtils().decrypt(props.getDbPassword()));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(queryTimeout);
    }

}
