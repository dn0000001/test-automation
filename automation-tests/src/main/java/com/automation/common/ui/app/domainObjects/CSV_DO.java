package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.FakeLoginPage;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.csv.CsvUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Object to hold data for CSV testing
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("csv-do")
public class CSV_DO extends DomainObject {
    private List<FakeLoginPage> userLogins;
    private List<FakeLoginPage> adminLogins;

    public CSV_DO() {
        super();
    }

    public CSV_DO(TestContext context) {
        super(context);
    }

    public List<FakeLoginPage> getUserLogins() {
        if (userLogins == null) {
            userLogins = new ArrayList<>();
        }

        for (FakeLoginPage item : userLogins) {
            if (item.getContext() == null) {
                item.initPage(getContext());
            }
        }

        return userLogins;
    }

    public List<FakeLoginPage> getAdminLogins() {
        if (adminLogins == null) {
            adminLogins = new ArrayList<>();
        }

        for (FakeLoginPage item : adminLogins) {
            if (item.getContext() == null) {
                item.initPage(getContext());
            }
        }

        return adminLogins;
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

        int size = CsvUtils.getListSize(csv, CsvColumnMapping.USER_LOGINS_EMAIL, CsvColumnMapping.USER_LOGINS_PASSWORD);
        userLogins = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            FakeLoginPage item = new FakeLoginPage(getContext());
            item.setData(csvTestData, i, true);
            userLogins.add(item);
        }

        size = CsvUtils.getListSize(csv, CsvColumnMapping.ADMIN_LOGINS_EMAIL, CsvColumnMapping.ADMIN_LOGINS_PASSWORD);
        adminLogins = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            FakeLoginPage item = new FakeLoginPage(getContext());
            item.setData(csvTestData, i, false);
            adminLogins.add(item);
        }
    }

}
