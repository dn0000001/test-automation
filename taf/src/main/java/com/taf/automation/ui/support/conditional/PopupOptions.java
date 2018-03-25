package com.taf.automation.ui.support.conditional;

import java.util.ArrayList;
import java.util.List;

/**
 * Options to wait for a popup window
 */
public class PopupOptions {
    private List<String> excluding;

    /**
     * @return the excluding window list
     */
    public List<String> getExcluding() {
        if (excluding == null) {
            excluding = new ArrayList<>();
        }

        return excluding;
    }

}
