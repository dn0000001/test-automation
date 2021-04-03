package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.AssertJUtil;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Basic CheckBox that prevents toggling the state when it is disabled<BR>
 * <B>Note: </B> The component ui.auto.core.components.CheckBox does not handle the disabled case.
 */
@SuppressWarnings("java:S3252")
public class CheckBoxBasic extends PageComponent {
    public CheckBoxBasic() {
        super();
    }

    public CheckBoxBasic(WebElement element) {
        super(element);
    }

    @Override
    protected void init() {
        // No initialization required
    }

    @Override
    public void setValue() {
        check(getData().equalsIgnoreCase("true"));
    }

    @Override
    public String getValue() {
        return String.valueOf(isSelected());
    }

    public void check(boolean value) {
        if (value) {
            check();
        } else {
            uncheck();
        }
    }

    public void check() {
        if (!isSelected()) {
            assertThat("CheckBoxBasic was disabled", isEnabled());
            getCoreElement().click();
        }
    }

    public void uncheck() {
        if (isSelected()) {
            assertThat("CheckBoxBasic was disabled", isEnabled());
            getCoreElement().click();
        }
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        AssertJUtil.assertThat(isSelected()).isEqualTo(validationMethod.getData(this).toLowerCase().trim().equals("true"));
    }

}
