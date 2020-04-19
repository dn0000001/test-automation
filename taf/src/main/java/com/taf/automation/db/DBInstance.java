package com.taf.automation.db;

import com.taf.automation.api.network.SSHSession;
import com.taf.automation.ui.support.TestProperties;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public abstract class DBInstance {
    protected static final Logger LOG = LoggerFactory.getLogger(DBInstance.class);
    protected SSHSession sshSession;
    protected JdbcTemplate jdbcTemplate;
    public static final DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

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

    /**
     * Reset the DB connection to use the default timeout values
     */
    public void resetDBConnectionToDefault() {
        TestProperties props = TestProperties.getInstance();
        updateDBConnection(props.getDbTimeout(), props.getDbTimeout(), props.getDbQueryTimeout());
    }

    /**
     * Set the DB connection to use these timeout values.<BR>
     * <B>Notes: </B> This only should be done for long running queries and the settings reset after<BR>
     *
     * @param connectionTimeout - Connection Timeout in milliseconds
     * @param socketTimeout     - Socket Timeout in milliseconds
     * @param queryTimeout      - Query Timeout in seconds
     */
    public void updateDBConnection(int connectionTimeout, int socketTimeout, int queryTimeout) {
        createDBConnection(connectionTimeout, socketTimeout, queryTimeout);
    }

    /**
     * Get the size of the result set<BR>
     * <B>Note: </B> Moves the cursor back to before first
     *
     * @param rs - Result Set
     * @return size of result set
     */
    public int size(SqlRowSet rs) {
        int size = 0;
        if (rs.last()) {
            size = rs.getRow();
            rs.beforeFirst();
        }

        return size;
    }

    /**
     * Create the DB connection with the specified timeout values
     *
     * @param connectionTimeout - Connection Timeout in milliseconds
     * @param socketTimeout     - Socket Timeout in milliseconds
     * @param queryTimeout      - Query Timeout in seconds
     */
    protected abstract void createDBConnection(int connectionTimeout, int socketTimeout, int queryTimeout);

}
