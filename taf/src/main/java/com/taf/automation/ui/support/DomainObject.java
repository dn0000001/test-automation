package com.taf.automation.ui.support;

import com.taf.automation.ui.support.csv.CsvTestData;
import com.thoughtworks.xstream.XStream;
import datainstiller.data.DataAliases;
import datainstiller.data.DataPersistence;
import ui.auto.core.context.PageComponentContext;
import ui.auto.core.support.DomainObjectModel;

import java.net.URL;

public class DomainObject extends DomainObjectModel {
    public DomainObject(TestContext context) {
        this.context = context;
    }

    protected DomainObject() {
        //
    }

    @Override
    public XStream getXstream() {
        XStream xStream = DataInstillerUtils.getXStream();
        xStream.processAnnotations(this.getClass());
        return xStream;
    }

    private void addToGlobalAliases(DataPersistence data) {
        DataAliases global = PageComponentContext.getGlobalAliases();
        DataAliases local = data.getDataAliases();
        if (local != null) {
            global.putAll(local);
        }
    }

    /**
     * Add To Global Aliases from data set<BR><BR>
     * <B>History:</B><BR>
     * You can either resolve aliases or not when loading from a data set.  If you do not resolve aliases, then
     * they are added to the global aliases and remain in the object fields and require processing to replace the
     * aliases with the actual values.<BR><BR>
     * For GUI, this is fine because all the objects are desendents of PageComponent which handles replacing the
     * aliases with the actual values behind the scenes.  (This is why there is the AliasedString class for when you
     * have strings that contain aliases.)<BR><BR>
     * For non-GUI, this is problematic because no objects are descendents of PageComponent and this mechanism of
     * replacing the aliases is not available.  So, by default aliases are resolved when loading from a data set
     * for a non-GUI domain object.<BR><BR>
     * <B>Purpose of method:</B><BR>
     * Provide a mechanism to add to the global aliases should use of the aliases from the data set be necessary
     * by some other method.<BR><BR>
     * <B>Additional Notes: </B><BR>
     * This method can be simulated by loading from a data set without resolving the aliases first before again loading
     * from a data set with resolving the aliases.  However, this will attach the data set without resolved as well in
     * the report.  This method will only attach the data set with the resolved aliases to the report.
     *
     * @param dataSet - Path to data set
     * @param <T>     - Domain Object
     */
    public <T extends DataPersistence> void addToGlobalAliases(String dataSet) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(dataSet);
        T data = (T) getXstream().fromXML(url);
        addToGlobalAliases(data);
    }

    @Override
    public <T extends DataPersistence> T fromResource(String resourceFilePath, boolean resolveAliases) {
        return super.fromResource(Helper.getEnvironmentBasedFile(resourceFilePath), resolveAliases);
    }

    public void setContext(TestContext context) {
        this.context = context;
    }

    /**
     * Use the CSV data to set the variables in the domain object
     *
     * @param csvTestData - CSV test data
     */
    public void setData(CsvTestData csvTestData) {
        // Method should be overridden in extending classes
    }

}
