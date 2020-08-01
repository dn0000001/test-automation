package com.automation.common.ui.app.components;

import com.taf.automation.ui.support.util.Utils;
import org.openqa.selenium.WebElement;
import ui.auto.core.components.WebComponent;

public class TextBox extends WebComponent {
    private boolean sendKeysDelay;
    private int delayInMilliseconds;
    boolean clearUsingBackspace;

    public TextBox() {
        super();
        sendKeysDelay = false;
        delayInMilliseconds = 100;
        clearUsingBackspace = false;
    }

    public TextBox(WebElement element) {
        super(element);
        sendKeysDelay = false;
        delayInMilliseconds = 100;
        clearUsingBackspace = false;
    }

    @Override
    public void setValue() {
        setValue(getData());
    }

    public void setValue(String value) {
        if (clearUsingBackspace) {
            Utils.clearUsingBackspace(getCoreElement());
        } else {
            getCoreElement().clear();
        }

        if (sendKeysDelay) {
            Utils.sendKeysWithDelay(getCoreElement(), delayInMilliseconds, value);
        } else {
            getCoreElement().sendKeys(value);
        }
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

    public void setClearUsingBackspace(boolean clearUsingBackspace) {
        this.clearUsingBackspace = clearUsingBackspace;
    }

}
