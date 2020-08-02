package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import ui.auto.core.data.DataTypes;
import ui.auto.core.pagecomponent.PageComponent;

/**
 * This is a special text box in which after entering the value you must send ENTER to perform the search
 */
public class SearchTextBox extends PageComponent {
    private boolean sendKeysDelay;
    private int delayInMilliseconds;

    public SearchTextBox() {
        super();
        sendKeysDelay = false;
        delayInMilliseconds = 100;
    }

    public SearchTextBox(WebElement element) {
        super(element);
        sendKeysDelay = false;
        delayInMilliseconds = 100;
    }

    /**
     * Enable or Disable the send keys delay
     *
     * @param sendKeyDelay - true to enable send keys delay, false to disable any delay
     */
    public void setSendKeysDelay(boolean sendKeyDelay) {
        this.sendKeysDelay = sendKeyDelay;
    }

    /**
     * Change the send keys delay (in milliseconds)<BR>
     * <B>Note: </B> It is necessary to enable send keys delay for this to have any effect.
     *
     * @param delayInMilliseconds - Delay in milliseconds between sending each key
     */
    public void setSendKeysDelay(int delayInMilliseconds) {
        this.delayInMilliseconds = delayInMilliseconds;
    }

    @Override
    protected void init() {
        // No initialization required
    }

    public void setValue(String value) {
        getCoreElement().clear();
        if (sendKeysDelay) {
            Utils.sendKeysWithDelay(getCoreElement(), delayInMilliseconds, value + Keys.ENTER);
        } else {
            getCoreElement().sendKeys(value + Keys.ENTER);
        }
    }

    @Override
    public void setValue() {
        setValue(getData());
    }

    @Override
    public String getValue() {
        String value = getCoreElement().getAttribute("value");
        if (value == null) {
            value = getCoreElement().getText();
        }

        return value;
    }

    @Override
    public void validateData(DataTypes validationMethod) {
        // No Validation
    }

}
