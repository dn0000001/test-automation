package com.taf.automation.ui.support;

import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic class to help with extracting column positions for tables do not have an unique way to
 * identify cells on a row.
 */
public interface ColumnPositionsExtractor {
    /**
     * Get the locator to find all the column header elements
     *
     * @return By
     */
    By getColumnHeadersLocator();

    /**
     * Extract the header as key
     *
     * @param header   - Element that contains the header information
     * @param position - Position of header column that is being extracted
     * @return header as key or null if you cannot (or do not want) to extract
     */
    String extractHeaderAsKey(WebElement header, int position);

    /**
     * Get a substitution map using the mapper and column positions
     *
     * @param mapper          - Enumeration that maps the column positions to keys
     * @param columnPositions - Column Positions that contains the map from headers to positions
     * @return Map with the enumeration as the key and the position as the value
     */
    default Map<String, String> getSubstitutions(ColumnMapper mapper, Map<String, String> columnPositions) {
        Map<String, String> substitutions = new HashMap<>();

        for (Map.Entry<String, String> entry : columnPositions.entrySet()) {
            ColumnMapper column = mapper.toEnum(entry.getKey());
            if (column != null) {
                substitutions.put(column.toString(), entry.getValue());
            }
        }

        return substitutions;
    }

    /**
     * Get a map with the extracted column positions
     *
     * @return Map with headers as the key and the position as the value
     */
    default Map<String, String> getMap() {
        Map<String, String> columnPositions = new HashMap<>();

        int position = 1;
        List<WebElement> headers = Utils.getWebDriverWait().until(ExpectedConditions.numberOfElementsToBeMoreThan(getColumnHeadersLocator(), 0));
        for (WebElement header : headers) {
            String key = extractHeaderAsKey(header, position);
            if (key != null) {
                columnPositions.put(key, "" + position);
            }

            position++;
        }

        return columnPositions;
    }

}
