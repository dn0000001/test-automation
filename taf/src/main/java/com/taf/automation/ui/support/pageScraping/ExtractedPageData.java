package com.taf.automation.ui.support.pageScraping;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.util.Utils;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@XStreamAlias("page")
public class ExtractedPageData {
    private static final String DEFAULT_PADDING = "null";
    private String pageNameKey;
    private Map<String, Object> pageData;
    private String padding;
    private boolean sortIfErrors;

    public ExtractedPageData() {
        pageData = new LinkedHashMap<>();
        setPageNameKey("");
        setPadding(DEFAULT_PADDING);
        setSortIfErrors(true);
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getPadding() {
        return padding;
    }

    public void setSortIfErrors(boolean sortIfErrors) {
        this.sortIfErrors = sortIfErrors;
    }

    public boolean isSortIfErrors() {
        return sortIfErrors;
    }

    public String getPageNameKey() {
        return pageNameKey;
    }

    public void setPageNameKey(String pageNameKey) {
        this.pageNameKey = pageNameKey;
    }

    public void setPageNameKey(ColumnMapper enumKeyObject) {
        setPageNameKey(enumKeyObject.getColumnName());
    }

    /**
     * Method to associate a ColunmMapperKey with the current page
     *
     * @param key   - Unique key used to associate object with this page
     * @param value - value of the field linked to this page
     */
    public void addField(ColumnMapper key, Object value) {
        addField(key.getColumnName(), value);
    }

    /**
     * Method to associate a field with the current page.
     * <B>Note:</B> fieldNameKey must be unique
     *
     * @param fieldNameKey - unique name of a field to link to this page (eg NameTextBox)
     * @param value        - value of the field linked to this page (eg MyName)
     */
    public void addField(String fieldNameKey, Object value) {
        assertThat("Duplicate Key:  " + fieldNameKey, !pageData.containsKey(fieldNameKey));
        pageData.put(fieldNameKey, value);
    }

    /**
     * Method to associate a table record with the current page
     *
     * @param table - ExtractedTableData Object to associate to this page
     */
    public void addTable(ExtractedTableData table) {
        assertThat("Duplicate Key:  " + table.getTableNameKey(), !pageData.containsKey(table.getTableNameKey()));
        pageData.put(table.getTableNameKey(), table);
    }

    /**
     * Method used to compare this ExtractedPage objects (fields, tables) with another ExtractedPage Object (fields, tables)
     *
     * @param actualPage    - page used to compare the current ExpectedPage against
     * @param aggregator    - aggregator object used for field + table comparison
     * @param outputRecords - list of result records to for the current page
     */
    public void compare(ExtractedPageData actualPage, AssertAggregator aggregator, List<CsvOutputRecord> outputRecords) {
        if (actualPage == null) {
            aggregator.assertThat("Actual Page is Null", actualPage, notNullValue());
            return;
        }

        addPadding(this, actualPage, aggregator);
        for (Map.Entry<String, Object> expectedField : pageData.entrySet()) {
            if (expectedField.getValue() instanceof ExtractedTableData) {
                ExtractedTableData actualTable = (ExtractedTableData) actualPage.pageData.get(expectedField.getKey());
                ExtractedTableData expectedTable = (ExtractedTableData) expectedField.getValue();
                expectedTable.compare(actualTable, aggregator, getPageNameKey(), outputRecords);
            } else {
                Object actualValue = actualPage.pageData.get(expectedField.getKey());
                ExtractedRowData.compareField(expectedField.getKey(), expectedField.getValue(), actualValue, aggregator, getPageNameKey(), "Field", outputRecords);
            }
        }
    }

    /**
     * Method to add empty table/field padding to a ExpectedPageData's pageMap to ensure the two page sizes are the same
     *
     * @param page1      - first page object used to compare against the second page
     * @param page2      - second page object used to compare against the first page
     * @param aggregator - aggregator object used for field + table comparison
     */
    private void addPadding(ExtractedPageData page1, ExtractedPageData page2, AssertAggregator aggregator) {
        if (page1.pageData.size() == page2.pageData.size()) {
            // Do nothing both pageMaps are same sizes exit
            return;
        }

        boolean page1Bigger = page1.pageData.size() > page2.pageData.size();
        ExtractedPageData biggerPage = (page1Bigger) ? page1 : page2;
        ExtractedPageData smallerPage = (page1Bigger) ? page2 : page1;

        String errReason = "Difference in Field + Table Count between pages[Page1=" + page1.getPageNameKey() + ", Page2=" + page2.getPageNameKey() + "]";
        aggregator.assertThat(errReason, page1.pageData.size(), equalTo(page2.pageData.size()));

        for (Map.Entry<String, Object> aField : biggerPage.pageData.entrySet()) {
            if (!smallerPage.pageData.containsKey(aField.getKey())) {
                if (aField.getValue() instanceof ExtractedTableData) {
                    ExtractedTableData newTable = new ExtractedTableData();
                    newTable.setTableNameKey(aField.getKey());
                    newTable.setPadding(getPadding());
                    newTable.setSortIfErrors(isSortIfErrors());
                    smallerPage.addTable(newTable);
                } else {
                    smallerPage.addField(aField.getKey(), getPadding());
                }
            }
        }
    }

    /**
     * Returns the 1st value to which the specified key is mapped, or null if this map contains no mapping for the key
     * <BR><B>Note: </B> Recursively searches all ExtractedPageData only
     *
     * @param key - Using the column name as the key
     * @return The value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public Object get(ColumnMapper key) {
        return get(key.getColumnName());
    }

    /**
     * Returns the 1st value to which the specified key is mapped, or null if this map contains no mapping for the key
     * <BR><B>Note: </B> Recursively searches all ExtractedPageData only
     *
     * @param key - The key whose associated value is to be returned
     * @return The value to which the specified key is mapped, or null if this map contains no mapping for the key
     */
    public Object get(String key) {
        if (pageData.containsKey(key)) {
            return pageData.get(key);
        }

        for (Map.Entry<String, Object> entry : pageData.entrySet()) {
            if (entry.getValue() instanceof ExtractedPageData) {
                ExtractedPageData page = (ExtractedPageData) entry.getValue();
                if (page.getPageNameKey().equals(key)) {
                    return page;
                }

                if (page.contains(key)) {
                    return page.get(key);
                }
            }
        }

        return null;
    }

    /**
     * Removes the 1st mapping for the key
     * <BR><B>Note: </B> Recursively searches all ExtractedPageData only
     *
     * @param key - Using the column name as the key
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    public Object remove(ColumnMapper key) {
        return remove(key.getColumnName());
    }

    /**
     * Removes the 1st mapping for the key
     * <BR><B>Note: </B> Recursively searches all ExtractedPageData only
     *
     * @param key - The key whose mapping is to be removed from the map
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    public Object remove(String key) {
        if (pageData.containsKey(key)) {
            return pageData.remove(key);
        }

        for (Map.Entry<String, Object> entry : pageData.entrySet()) {
            if (entry.getValue() instanceof ExtractedPageData) {
                ExtractedPageData page = (ExtractedPageData) entry.getValue();
                if (page.getPageNameKey().equals(key)) {
                    return pageData.remove(entry.getKey());
                }

                if (page.contains(key)) {
                    return page.remove(key);
                }
            }
        }

        return null;
    }

    /**
     * Returns true if this map contains a mapping for the specified key
     * <BR><B>Note: </B> Recursively searches all ExtractedPageData only
     *
     * @param key - Using the column name as the key
     * @return true if this map contains a mapping for the specified
     */
    public boolean contains(ColumnMapper key) {
        return contains(key.getColumnName());
    }

    /**
     * Returns true if this map contains a mapping for the specified key
     * <BR><B>Note: </B> Recursively searches all ExtractedPageData only
     *
     * @param key - Key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified
     */
    public boolean contains(String key) {
        if (pageData.containsKey(key)) {
            return true;
        }

        for (Map.Entry<String, Object> entry : pageData.entrySet()) {
            if (entry.getValue() instanceof ExtractedPageData) {
                ExtractedPageData page = (ExtractedPageData) entry.getValue();
                if (page.getPageNameKey().equals(key)) {
                    return true;
                }

                if (page.contains(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object object) {
        List<String> excludeFields = new ArrayList<>();
        return Utils.equals(this, object, excludeFields);
    }

    @Override
    public int hashCode() {
        List<String> excludeFields = new ArrayList<>();
        return HashCodeBuilder.reflectionHashCode(this, excludeFields);
    }

    @Override
    public String toString() {
        ReflectionToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
        return ReflectionToStringBuilder.toStringExclude(this);
    }

}
