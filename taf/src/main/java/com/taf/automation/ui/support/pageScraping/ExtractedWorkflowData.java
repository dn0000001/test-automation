package com.taf.automation.ui.support.pageScraping;

import com.taf.automation.ui.support.AssertAggregator;
import com.taf.automation.ui.support.DataPersistenceV2;
import com.taf.automation.ui.support.Utils;
import com.taf.automation.ui.support.csv.CsvOutputRecord;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.File;
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
     * Write object as XML file
     *
     * @param filename - filename for XML to be written
     */
    public void writeToFile(String filename) {
        try {
            File fileTarget = new File(filename);
            FileUtils.write(fileTarget, toXML(), false);
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
        ReflectionToStringBuilder.setDefaultStyle(Utils.getDefaultStyle());
        return ReflectionToStringBuilder.toStringExclude(this);
    }

}
