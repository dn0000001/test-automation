package com.automation.common.ui.app.domainObjects;

import com.automation.common.ui.app.pageObjects.HerokuappRow;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("row-matching-do")
public class RowMatchingDO extends DomainObject {
    @XStreamOmitField
    private HerokuappRow anyRowMatch;

    private HerokuappRow singleRowMatch;
    private HerokuappRow multiRowMatch;
    private HerokuappRow noRowMatch;

    public RowMatchingDO() {
        super();
    }

    public RowMatchingDO(TestContext context) {
        super(context);
    }

    public HerokuappRow getAnyRowMatch() {
        if (anyRowMatch == null) {
            anyRowMatch = new HerokuappRow();
        }

        if (anyRowMatch.getContext() == null) {
            anyRowMatch.initPage(getContext());
        }

        return anyRowMatch;
    }

    public HerokuappRow getSingleRowMatch() {
        if (singleRowMatch == null) {
            singleRowMatch = new HerokuappRow();
        }

        if (singleRowMatch.getContext() == null) {
            singleRowMatch.initPage(getContext());
        }

        return singleRowMatch;
    }

    public HerokuappRow getMultiRowMatch() {
        if (multiRowMatch == null) {
            multiRowMatch = new HerokuappRow();
        }

        if (multiRowMatch.getContext() == null) {
            multiRowMatch.initPage(getContext());
        }

        return multiRowMatch;
    }

    public HerokuappRow getNoRowMatch() {
        if (noRowMatch == null) {
            noRowMatch = new HerokuappRow();
        }

        if (noRowMatch.getContext() == null) {
            noRowMatch.initPage(getContext());
        }

        return noRowMatch;
    }

}
