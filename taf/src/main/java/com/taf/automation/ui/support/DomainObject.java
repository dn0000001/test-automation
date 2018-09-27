package com.taf.automation.ui.support;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataAliases;
import datainstiller.data.DataGenerator;
import datainstiller.data.DataPersistence;
import org.testng.annotations.Test;
import ui.auto.core.context.PageComponentContext;

import java.io.InputStream;
import java.net.URL;

public class DomainObject extends DataPersistence {
    @XStreamOmitField
    private TestContext context;

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

    private DataGenerator getGenerator() {
        return DataInstillerUtils.getGenerator();
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
    public <T extends DataPersistence> T fromXml(String xml, boolean resolveAliases) {
        TestContext cont = getContext();
        T data = super.fromXml(xml, resolveAliases);
        addToGlobalAliases(data);
        ((DomainObject) data).context = cont;
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromURL(URL url, boolean resolveAliases) {
        TestContext cont = getContext();
        T data = super.fromURL(url, resolveAliases);
        addToGlobalAliases(data);
        ((DomainObject) data).context = cont;
        Utils.attachDataSet(data, url.getPath());
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromInputStream(InputStream inputStream, boolean resolveAliases) {
        TestContext cont = getContext();
        T data = super.fromInputStream(inputStream, resolveAliases);
        addToGlobalAliases(data);
        ((DomainObject) data).context = cont;
        return data;
    }

    @Override
    public <T extends DataPersistence> T fromFile(String filePath, boolean resolveAliases) {
        TestContext cont = getContext();
        T data = super.fromFile(filePath, resolveAliases);
        addToGlobalAliases(data);
        ((DomainObject) data).context = cont;
        Utils.attachDataSet(data, filePath);
        return data;
    }

    @Override
    public void generateData() {
        DataPersistence obj = getGenerator().generate(this.getClass());
        deepCopy(obj, this);
    }

    @Override
    public String generateXML() {
        DataPersistence obj = getGenerator().generate(this.getClass());
        return obj.toXML();
    }

    @Override
    public String toXML() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        XStream xstream = getXstream();
        xstream.aliasSystemAttribute(null, "class");
        String xml = xstream.toXML(this);
        xml = xml.replaceAll(" xmlns.*\"schemaLocation\"", "");
        return header + xml;
    }

    public TestContext getContext() {
        return context;
    }

    public void setContext(TestContext context) {
        this.context = context;
    }

    @Test
    public void generate() {
        System.out.println(generateXML());
    }

}
