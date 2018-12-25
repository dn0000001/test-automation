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

    /**
     * Pad Lists with empty items<BR>
     * <B>Explanation: </B> When outputting a list to a CSV file it is necessary to pad the lists such that they are
     * all the same size across all the records being output to ensure the data aligns with the headers.
     *
     * @param totals - For each list in the object, the total number of items there needs to be
     */
    default void padListsWithEmptyItems(int... totals) {
        // Assume that object does not have an list variables.
        // Should the object have a list, then the implementation will require this method to be implemented
        // with custom logic to pad the lists.
    }

}
