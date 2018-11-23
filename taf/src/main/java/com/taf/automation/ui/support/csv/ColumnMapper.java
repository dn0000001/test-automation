package com.taf.automation.ui.support.csv;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The enumerations for CSV column mapping need to implement this interface.  The reason for this is enumerations
 * cannot inherit from other enumerations, this somewhat works around the limitation.
 */
public interface ColumnMapper {
    /**
     * Get the Column Name
     *
     * @return String
     */
    String getColumnName();

    /**
     * Get the enum values<BR>
     * <B>Note to Coder: </B> Use values() which is available in an enum
     *
     * @return ColumnMapper[]
     */
    ColumnMapper[] getValues();

    /**
     * Convert string value to enum
     *
     * @param columnName - Column Name
     * @return null if no matching enum else ColumnMapper
     */
    default ColumnMapper toEnum(String columnName) {
        return toEnum(getValues(), columnName);
    }

    /**
     * Convert string value to enum
     *
     * @param values     - Values to used for matching
     * @param columnName - Column Name
     * @return null if no matching enum else ColumnMapper
     */
    default ColumnMapper toEnum(ColumnMapper[] values, String columnName) {
        if (StringUtils.trimToNull(columnName) == null) {
            return null;
        }

        for (ColumnMapper value : values) {
            if (StringUtils.equalsIgnoreCase(StringUtils.trim(columnName), value.getColumnName())) {
                return value;
            }
        }

        return null;
    }

    /**
     * This is debugging method to get all duplicates which can be useful as all CSV headers need to unique
     *
     * @return List of duplicates
     */
    default List<ColumnMapper> getDuplicates() {
        Map<String, List<ColumnMapper>> columnMap = new HashMap<>();
        for (ColumnMapper value : getValues()) {
            String key = value.getColumnName();
            if (columnMap.containsKey(key)) {
                List<ColumnMapper> duplicateList = columnMap.get(key);
                duplicateList.add(value);
                columnMap.put(key, duplicateList);
            } else {
                List<ColumnMapper> startList = new ArrayList<>();
                startList.add(value);
                columnMap.put(key, startList);
            }
        }

        List<ColumnMapper> duplicates = new ArrayList<>();
        for (Map.Entry<String, List<ColumnMapper>> item : columnMap.entrySet()) {
            List<ColumnMapper> stored = item.getValue();
            if (stored.size() > 1) {
                duplicates.addAll(stored);
            }
        }

        return duplicates;
    }

    /**
     * This is a debugging method to check if the column name is unique which can be useful when adding a new column
     * name and you need to ensure that it is unique
     *
     * @param columnName - Column Name to check
     * @return true the column name is unique, false the column name already exists
     */
    default boolean isUnique(String columnName) {
        for (ColumnMapper value : getValues()) {
            if (StringUtils.equalsIgnoreCase(value.getColumnName(), columnName)) {
                return false;
            }
        }

        return true;
    }

}
