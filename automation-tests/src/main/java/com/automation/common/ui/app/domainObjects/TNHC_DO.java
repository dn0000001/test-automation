package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.TNHC_LandingPage;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.csv.CSVRecord;

/**
 * Object to hold data for True North Hockey Canada
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("true-north-hockey-canada")
public class TNHC_DO extends DomainObject {
    private TNHC_LandingPage landing;
    private String user;
    private String pass;

    public TNHC_DO() {

    }

    public TNHC_DO(TestContext context) {
        super(context);
    }

    public TNHC_LandingPage getLanding() {
        if (landing == null) {
            landing = new TNHC_LandingPage();
        }

        landing.initDefaultSubstitutions();

        if (landing.getContext() == null) {
            landing.initPage(getContext());
        }

        return landing;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    /**
     * Use the CSV data to set the variables in the domain object
     *
     * @param csvTestData - CSV test data
     */
    @Override
    public void setData(CsvTestData csvTestData) {
        CSVRecord csv = csvTestData.getRecord();
        if (csv == null) {
            return;
        }

        if (csv.isMapped(CsvColumnMapping.USER.getColumnName())) {
            user = csv.get(CsvColumnMapping.USER.getColumnName());
        }

        if (csv.isMapped(CsvColumnMapping.PASS.getColumnName())) {
            pass = csv.get(CsvColumnMapping.PASS.getColumnName());
        }

        getLanding().setData(csvTestData);
    }

}
