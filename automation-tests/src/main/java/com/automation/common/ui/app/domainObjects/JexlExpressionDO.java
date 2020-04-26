package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.AliasedString;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.jexl3.JexlContext;
import ui.auto.core.data.DataTypes;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("jexl-expression-do")
public class JexlExpressionDO extends DomainObject {
    private AliasedString someField;
    private AliasedString anotherField;
    private AliasedString extraField1;
    private AliasedString extraField2;

    public JexlExpressionDO() {
        super();
    }

    public JexlExpressionDO(TestContext context) {
        super(context);
    }

    @Override
    protected void initJexlContext(JexlContext jexlContext) {
        super.initJexlContext(jexlContext);
        jexlContext.set("str1", "aaa");
        jexlContext.set("str2", "bbb");
    }

    public String getSomeField() {
        return someField.getData();
    }

    public String getSomeFieldExpected() {
        return someField.getData(DataTypes.Expected, true);
    }

    public String getSomeFieldInitial() {
        return someField.getData(DataTypes.Initial, true);
    }

    public String getAnotherField() {
        return anotherField.getData();
    }

    public String getAnotherFieldExpected() {
        return anotherField.getData(DataTypes.Expected, true);
    }

    public String getAnotherFieldInitial() {
        return anotherField.getData(DataTypes.Initial, true);
    }

    public String getExtraField1() {
        return extraField1.getData();
    }

    public String getExtraField1Expected() {
        return extraField1.getData(DataTypes.Expected, true);
    }

    public String getExtraField1Initial() {
        return extraField1.getData(DataTypes.Initial, true);
    }

    public String getExtraField2() {
        return extraField2.getData();
    }

    public String getExtraField2Expected() {
        return extraField2.getData(DataTypes.Expected, true);
    }

    public String getExtraField2Initial() {
        return extraField2.getData(DataTypes.Initial, true);
    }

}
