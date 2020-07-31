package com.automation.common.ui.app.domainObjects;

import com.taf.automation.ui.support.AliasedString;
import com.taf.automation.ui.support.util.BigDecimalUtils;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@XStreamAlias("jexl-expression-do")
public class JexlExpressionDO extends DomainObject {
    private static final String TODAY = DateActions.format(new Date(), "MM/dd/yyyy");
    private static final String CURRENT_SYSTEM_DATE = "CURRENT_SYSTEM_DATE";
    private AliasedString someField;
    private AliasedString anotherField;
    private AliasedString extraField1;
    private AliasedString extraField2;
    private AliasedString extraField3;
    private AliasedString extraField4;
    private AliasedString currentSystemDate;
    private AliasedString userField1;
    private AliasedString userField2;
    private AliasedString userField3;
    private AliasedString bdField1;
    private AliasedString bdField2;

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
        jexlContext.set("today", TODAY);
        jexlContext.set("bdUtils", BigDecimalUtils.getInstance());
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

    public String getExtraField4() {
        return extraField4.getData();
    }

    public String getExtraField4Expected() {
        return extraField4.getData(DataTypes.Expected, true);
    }

    public String getExtraField4Initial() {
        return extraField4.getData(DataTypes.Initial, true);
    }

    public String getUserField1() {
        return userField1.getData();
    }

    public String getUserField1Expected() {
        return userField1.getData(DataTypes.Expected, true);
    }

    public String getUserField2() {
        return userField2.getData();
    }

    public String getUserField2Expected() {
        return userField2.getData(DataTypes.Expected, true);
    }

    public String getUserField3() {
        return userField3.getData();
    }

    public String getUserField3Expected() {
        return userField3.getData(DataTypes.Expected, true);
    }

    public String getBdField1() {
        return bdField1.getData();
    }

    public String getBdField1Expected() {
        return bdField1.getData(DataTypes.Expected, true);
    }

    public String getBdField2() {
        return bdField2.getData();
    }

    public String getBdField2Expected() {
        return bdField2.getData(DataTypes.Expected, true);
    }

    @Step("Validate Current System Date Before Load")
    public void validateCurrentSystemDateBeforeLoad() {
        assertThat("Current System Date Before Load", currentSystemDate.getData(), equalTo(TODAY));
    }

    @SuppressWarnings("java:S1905")
    @Step("Validate Current System Date After Load")
    public void validateCurrentSystemDateAfterLoad() {
        String expected = (String) Lookup.getInstance().get(CURRENT_SYSTEM_DATE);
        assertThat("Current System Date After Load", currentSystemDate.getData(), equalTo(expected));
    }

    @Step("Perform Pre From Resource Actions")
    public void performPreFromResourceActions() {
        Lookup.getInstance().put("beforeLoadResource1", Rand.alphanumeric(5, 10));
        Lookup.getInstance().put("beforeLoadResource2", Rand.alphanumeric(5, 10));
        Lookup.getInstance().put("int100", 100);
        Lookup.getInstance().put("int200", 200);
        Lookup.getInstance().put(CURRENT_SYSTEM_DATE, RandomDateUtil.getInstance().random());
    }

}
