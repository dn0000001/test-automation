package com.taf.automation.ui.support.csv;

import java.util.List;

/**
 * This interface is used to be able to generically write a CSV file
 */
public interface CsvOutputRecord {
    /**
     * Get all the fields (in order) to be output to a CSV file
     *
     * @return List&lt;String&gt;
     */
    List<String> asList();
}
