package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.ComponentPO;
import com.taf.automation.ui.support.TestContext;
import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * A dynamic page object that acts as a component
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class CardExpirationDateFieldsDynamic extends ComponentPO {
    private static final String COMMON_FIELD_KEY = "common-field-key";

    @FindBy(css = "[name$='${common-field-key}mm']")
    private SelectEnhanced month;

    @FindBy(css = "[name$='${common-field-key}yy']")
    private SelectEnhanced year;

    public CardExpirationDateFieldsDynamic() {
        super();
    }

    public CardExpirationDateFieldsDynamic(TestContext context) {
        super(context);
    }

    public static String getCommonFieldKey() {
        return COMMON_FIELD_KEY;
    }

    public void updateCommonFieldKey(String value) {
        getSubstitutions().put(getCommonFieldKey(), value);
    }

    @Override
    public boolean hasData() {
        return Utils.isNotBlank(month)
                || Utils.isNotBlank(year)
                ;
    }

    @Step("Select Month")
    public void selectMonth() {
        setElementValueV2(month);
    }

    @Step("Select Year")
    public void selectYear() {
        setElementValueV2(year);
    }

    @Override
    @Step("Fill Card Expiration Date Fields")
    public void fill() {
        selectMonth();
        selectYear();
    }

    @Override
    public void validate() {
        // No actions required to validate
    }

    public void updateMonthTestData(String value) {
        month.initializeData(value, null, null);
    }

}
