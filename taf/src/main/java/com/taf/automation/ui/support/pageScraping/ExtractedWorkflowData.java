package com.taf.automation.ui.support.pageScraping;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.DataPersistenceV2;
import com.taf.automation.ui.support.csv.ColumnMapper;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.taf.automation.ui.support.util.Helper;
import com.taf.automation.ui.support.util.Utils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import datainstiller.data.DataPersistence;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Container Class to hold list of extracted page data
 */
@XStreamAlias("extractedWorkflowData")
public class ExtractedWorkflowData extends DataPersistenceV2 {
    private static final String DEFAULT_PADDING = "null";
    private String flowName;
    private Map<String, ExtractedPageData> pages;
    private String padding;
    private boolean sortIfErrors;

    public ExtractedWorkflowData() {
        pages = new LinkedHashMap<>();
        setPadding(DEFAULT_PADDING);
        setFlowName("");
        setSortIfErrors(true);
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
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

    /**
     * Add a page to the workflow
     *
     * @param page - ExtractedPageData to add to this workflow
     */
    public void addPage(ExtractedPageData page) {
        assertThat("Duplicate Key:  " + page.getPageNameKey(), !pages.containsKey(page.getPageNameKey()));
        pages.put(page.getPageNameKey(), page);
    }

    /**
     * Compare this ExtractedWorkflow Object with another ExtractedWorkflowObject
     *
     * @param actualWorkflow - ExtractedWorkflow Object to compare against this object
     * @param aggregator     - aggregator object used for ExtractedWorkflow comparison
     * @param outputRecords  - list of result records to for the current page
     */
    public void compare(ExtractedWorkflowData actualWorkflow, AssertAggregator aggregator, List<CsvOutputRecord> outputRecords) {
        if (actualWorkflow == null) {
            aggregator.assertThat("Actual Workflow is Null", actualWorkflow, notNullValue());
            return;
        }

        addPadding(this, actualWorkflow, aggregator);
        for (Map.Entry<String, ExtractedPageData> aPage : pages.entrySet()) {
            ExtractedPageData actualPage = actualWorkflow.pages.get(aPage.getKey());
            ExtractedPageData expectedPage = aPage.getValue();
            expectedPage.compare(actualPage, aggregator, outputRecords);
        }
    }

    /**
     * Method to add empty pages padding to a ExpectedWorkflowData's pageList to ensure the pageCount to be the flows are the same
     *
     * @param workflow1  - first workflow to compare against the second workflow
     * @param workflow2  - second workflow to compare against the first workflow
     * @param aggregator - aggregator object used for ExtractedWorkflow comparison
     */
    private void addPadding(ExtractedWorkflowData workflow1, ExtractedWorkflowData workflow2, AssertAggregator aggregator) {
        if (workflow1.pages.size() == workflow2.pages.size()) {
            // Do nothing both workflows have the same number of pages exit
            return;
        }

        boolean workflow1Bigger = workflow1.pages.size() > workflow2.pages.size();
        ExtractedWorkflowData biggerWorkFlow = (workflow1Bigger) ? workflow1 : workflow2;
        ExtractedWorkflowData smallerWorkFlow = (workflow1Bigger) ? workflow2 : workflow1;

        String errReason = "Difference in Row Count between PageSize[Workflow1=" + workflow1.getFlowName() + ", Workflow2=" + workflow2.getFlowName() + "]";
        aggregator.assertThat(errReason, workflow1.pages.size(), equalTo(workflow2.pages.size()));

        for (Map.Entry<String, ExtractedPageData> aPage : biggerWorkFlow.pages.entrySet()) {
            if (!smallerWorkFlow.pages.containsKey(aPage.getKey())) {
                ExtractedPageData emptyPage = new ExtractedPageData();
                emptyPage.setPageNameKey(aPage.getKey());
                emptyPage.setPadding(getPadding());
                emptyPage.setSortIfErrors(isSortIfErrors());
                smallerWorkFlow.addPage(emptyPage);
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
        if (pages.containsKey(key)) {
            return pages.get(key);
        }

        for (Map.Entry<String, ExtractedPageData> entry : pages.entrySet()) {
            if (entry.getValue().contains(key)) {
                return entry.getValue().get(key);
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
        if (pages.containsKey(key)) {
            return pages.remove(key);
        }

        for (Map.Entry<String, ExtractedPageData> entry : pages.entrySet()) {
            if (entry.getValue().contains(key)) {
                return entry.getValue().remove(key);
            }
        }

        return null;
    }

    /**
     * Write object as XML file
     *
     * @param filename - filename for XML to be written
     */
    public void writeToFile(String filename) {
        try {
            File fileTarget = new File(filename);
            FileUtils.write(fileTarget, toXML(), Charset.defaultCharset(), false);
        } catch (Exception ex) {
            assertThat("Could not write to file due to error:  " + ex.getMessage(), false);
        }
    }

    @Override
    public XStream getXstream() {
        XStream xStream = super.getXstream();
        xStream.alias("extractedWorkflowData", ExtractedWorkflowData.class);
        xStream.alias("page", ExtractedPageData.class);
        xStream.alias("table", ExtractedTableData.class);
        xStream.alias("row", ExtractedRowData.class);
        return xStream;
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
        ToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
        return ReflectionToStringBuilder.toStringExclude(this);
    }

    @Override
    public <T extends DataPersistence> T fromResource(String resourceFilePath, boolean resolveAliases) {
        //
        // The default constructor sets some values to non-null which will cause them to be retained.
        // So, It is necessary to set fields that should be overridden from the file to null
        // to prevent fields from being retained
        //
        pages = null;
        setFlowName(null);

        return super.fromResource(Helper.getEnvironmentBasedFile(resourceFilePath), resolveAliases);
    }

    @Override
    public <T extends DataPersistence> T fromURL(URL url, boolean resolveAliases) {
        T data = super.fromURL(url, resolveAliases);
        Utils.attachDataSet(data, url.getPath());
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromFile(String filePath, boolean resolveAliases) {
        T data = super.fromFile(filePath, resolveAliases);
        Utils.attachDataSet(data, filePath);
        return data;
    }

}
