package com.taf.automation.ui.support;

import com.thoughtworks.xstream.XStream;
import datainstiller.data.DataGenerator;
import datainstiller.data.DataPersistence;

/**
 * The purpose of this class is to revert the removal of xml namespaces added to DataPersistence.  This was causing
 * issues with SOAP requests that had xml namespaces.  This should not be an issue for page objects as it would be
 * unusual to use xml namespaces (i.e. the attribute "xmlns".)
 */
public abstract class DataPersistenceV2 extends DataPersistence {
    protected DataPersistenceV2() {
        //
    }

    /**
     * This method serializes this object to the given XML string
     *
     * @return XML representation of this object
     */
    @Override
    public String toXML() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
        XStream xstream = getXstream();
        String xml = xstream.toXML(this);
        return header + xml;
    }

    @Override
    public XStream getXstream() {
        XStream xStream = DataInstillerUtils.getXStream();
        xStream.processAnnotations(this.getClass());
        return xStream;
    }

    @Override
    public void generateData() {
        DataPersistence obj = DataInstillerUtils.getGenerator().generate(this.getClass());
        deepCopy(obj, this);
    }

    @Override
    public String generateXML() {
        DataGenerator generator = DataInstillerUtils.getGenerator();
        DataPersistence obj = generator.generate(this.getClass());
        return (obj.toXML());
    }

}
