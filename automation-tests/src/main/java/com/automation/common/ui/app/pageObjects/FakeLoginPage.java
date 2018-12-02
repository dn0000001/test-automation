package com.automation.common.ui.app.pageObjects;

import com.automation.common.ui.app.components.TextBox;
import com.automation.common.ui.app.domainObjects.CsvColumnMapping;
import com.taf.automation.ui.support.PageObjectV2;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.csv.CsvTestData;
import com.taf.automation.ui.support.csv.CsvUtils;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.support.FindBy;
import ui.auto.core.data.DataTypes;

/**
 * This is a fake login page used to test CSV with list objects
 */
public class FakeLoginPage extends PageObjectV2 {
    @FindBy(id = "email")
    private TextBox email;

    @FindBy(id = "password")
    private TextBox password;

    public FakeLoginPage() {
        super();
    }

    public FakeLoginPage(TestContext context) {
        super(context);
    }

    public String getEmailData() {
        return email.getData(DataTypes.Data, true);
    }

    public String getPasswordData() {
        return password.getData(DataTypes.Data, true);
    }

    /**
     * Use the CSV data to set the variables in the domain object
     *
     * @param csvTestData - CSV test data
     * @param index       - Index of list item
     * @param user        - true if setting user login, false for setting admin login
     */
    public void setData(CsvTestData csvTestData, int index, boolean user) {
        CSVRecord csv = csvTestData.getRecord();
        if (user) {
            CsvUtils.setData(csv, CsvColumnMapping.USER_LOGINS_EMAIL, index, email);
            CsvUtils.setData(csv, CsvColumnMapping.USER_LOGINS_PASSWORD, index, password);
        } else {
            CsvUtils.setData(csv, CsvColumnMapping.ADMIN_LOGINS_EMAIL, index, email);
            CsvUtils.setData(csv, CsvColumnMapping.ADMIN_LOGINS_PASSWORD, index, password);
        }
    }

}
