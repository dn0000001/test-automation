package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.AliasedString;
import com.taf.automation.ui.support.DateActions;
import com.taf.automation.ui.support.DomainObject;
import com.taf.automation.ui.support.Lookup;
import com.taf.automation.ui.support.Rand;
import com.taf.automation.ui.support.TestContext;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.lang3.time.DateUtils;
import ru.yandex.qatools.allure.annotations.Step;
import ui.auto.core.data.DataTypes;

import java.util.Date;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("jexl-expression-do")
public class JexlExpressionDO extends DomainObject {
    private AliasedString someField;
    private AliasedString anotherField;
    private AliasedString extraField1;
    private AliasedString extraField2;
    private AliasedString extraField3;

    public JexlExpressionDO() {
        super();
    }

    public JexlExpressionDO(TestContext context) {
        super(context);
    }

    @SuppressWarnings("java:S2440")
    @Override
    protected void initJexlContext(JexlContext jexlContext) {
        super.initJexlContext(jexlContext);
        jexlContext.set("str1", "aaa");
        jexlContext.set("str2", "bbb");
        jexlContext.set("dateActions", new DateActions());
        jexlContext.set("dateUtils", new DateUtils());
        jexlContext.set("today", "" + DateActions.format(new Date(), "MM/dd/yyyy"));
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

    public String getExtraField3() {
        return extraField3.getData();
    }

    public String getExtraField3Expected() {
        return extraField3.getData(DataTypes.Expected, true);
    }

    public String getExtraField3Initial() {
        return extraField3.getData(DataTypes.Initial, true);
    }

    @Step("Perform Pre From Resource Actions")
    public void performPreFromResourceActions() {
        Lookup.getInstance().put("beforeLoadResource1", Rand.alphanumeric(5, 10));
        Lookup.getInstance().put("beforeLoadResource2", Rand.alphanumeric(5, 10));
        Lookup.getInstance().put("currentSystemDate", RandomDateUtil.getInstance().random());
    }

}
