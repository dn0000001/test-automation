package com.taf.automation.ui.support.conditional;

/**
 * Options for matching alerts
 */
public class AlertOptions {
    private boolean accept;

    /**
     * @return true to accept, false to dismiss
     */
    public boolean isAccept() {
        return accept;
    }

    /**
     * @param accept - true to accept, false to dismiss
     */
    public void setAccept(boolean accept) {
        this.accept = accept;
    }

}
