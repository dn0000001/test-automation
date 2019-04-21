package com.automation.common.db;

import com.taf.automation.db.MySqlInstance;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("squid:S00100")
public class SampleQueries {
    private MySqlInstance db = MySqlInstance.getInstance();
    private static final String COLUMN_A_VALUE = "aValue";
    private static final String COLUMN_A_ID = "aID";
    private static final String SELECT = "select aID, aValue from aTable where aID=:aID";
    private static final String UPDATE = "update aTable set aValue=:aValue where aID=:aID";
    private static final String DELETE = "delete from aTable where aID=:aID";
    private static final String INSERT = "insert into aTable (aValue, aValue2) values (:aValue, :aValue2)";
    private static final String BULK_INSERT = "insert into aTable (aValue, aValue2) values (?, ?)";

    public static class SampleData {
        private Integer aID;
        private String aValue;
        private String aValue2;

        public Integer get_aID() {
            return aID;
        }

        public void set_aID(Integer aID) {
            this.aID = aID;
        }

        public String get_aValue() {
            return aValue;
        }

        public void set_aValue(String aValue) {
            this.aValue = aValue;
        }

        public String get_aValue2() {
            return aValue2;
        }

        public void set_aValue2(String aValue2) {
            this.aValue2 = aValue2;
        }

    }

    private RowMapper<SampleData> getSampleDataRowMapper() {
        return (rs, rowNum) -> {
            SampleData data = new SampleData();

            data.set_aID(rs.getInt(COLUMN_A_ID));
            data.set_aValue(rs.getString(COLUMN_A_VALUE));

            return data;
        };
    }

    public List<SampleData> getAllDataUsingRowMapper(String aID) {
        return db.namedParameterJdbcTemplate().query(
                SELECT,
                new MapSqlParameterSource().addValue("aID", aID),
                getSampleDataRowMapper()
        );
    }

    public List<SampleData> getAllDataUsingRowSet(String aID) {
        List<SampleData> all = new ArrayList<>();

        SqlRowSet rowSet = db.queryForRowSet(SELECT, new MapSqlParameterSource().addValue("aID", aID));
        while (rowSet.next()) {
            SampleData item = new SampleData();
            item.set_aID(rowSet.getInt(COLUMN_A_ID));
            item.set_aValue(rowSet.getString(COLUMN_A_VALUE));
            all.add(item);
        }

        return all;
    }

    public String getSingleValueUsingRowSet(String aID) {
        SqlRowSet rowSet = db.queryForRowSet(SELECT, new MapSqlParameterSource().addValue("aID", aID));
        if (rowSet.next()) {
            return rowSet.getString(COLUMN_A_VALUE);
        }

        return null;
    }

    public String getSingleValueUsingQueryForObject(String aID) {
        return db.queryForObject(SELECT, new MapSqlParameterSource().addValue("aID", aID), String.class);
    }

    public int updateTable(String aID) {
        return db.update(UPDATE, new MapSqlParameterSource().addValue("aID", aID));
    }

    public int deleteFromTable(String aID) {
        return db.update(DELETE, new MapSqlParameterSource().addValue("aID", aID));
    }

    @SuppressWarnings("squid:S1192")
    public int singleRowInsert(String aValue, String aValue2) {
        return db.update(INSERT, new MapSqlParameterSource()
                .addValue("aValue", aValue)
                .addValue("aValue2", aValue2));
    }

    public void bulkInsertUsingPreparedStatement(List<SampleData> items) {
        db.jdbcTemplate().batchUpdate(BULK_INSERT, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SampleData data = items.get(i);
                ps.setString(1, data.get_aValue());
                ps.setString(2, data.get_aValue2());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

}
