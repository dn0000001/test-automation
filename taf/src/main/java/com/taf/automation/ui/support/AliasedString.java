package com.taf.automation.ui.support;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import ui.auto.core.pagecomponent.PageComponentNoDefaultAction;

@XStreamAlias("aliased-string")
public class AliasedString extends PageComponentNoDefaultAction {
    @Override
    protected void init() {
        // No initialization required
    }

}
