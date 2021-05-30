package com.taf.automation.ui.support.util;

/**
 * Class to hold the data from MutationObserver
 */
public class MutationObserverData {
    private boolean attributeAdded;
    private boolean attributeRemoved;

    public boolean isAttributeAdded() {
        return attributeAdded;
    }

    public void setAttributeAdded(boolean attributeAdded) {
        this.attributeAdded = attributeAdded;
    }

    public boolean isAttributeRemoved() {
        return attributeRemoved;
    }

    public void setAttributeRemoved(boolean attributeRemoved) {
        this.attributeRemoved = attributeRemoved;
    }

}
