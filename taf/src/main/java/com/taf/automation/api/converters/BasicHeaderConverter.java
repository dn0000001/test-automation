package com.taf.automation.api.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataValueConverter;
import org.apache.http.message.BasicHeader;

import java.lang.reflect.Field;

/**
 * Converter for BasicHeader
 */
public class BasicHeaderConverter implements DataValueConverter {

    /**
     * Parses the specified string into an object (BasicHeader)
     *
     * @param str   - String to be parsed
     * @param cls   - Is not used in this specific implementation
     * @param field - Is not used in this specific implementation
     * @return BasicHeader
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromString(String str, Class<T> cls, Field field) {
        String name = "";
        String value = "";
        String[] parts = str.split("\\|");
        if (parts.length >= 2) {
            name = parts[0];
            value = parts[1];
        } else {
            name = parts[0];
        }

        BasicHeader basicHeader = new BasicHeader(name, value);
        return (T) basicHeader;
    }

    /**
     * Convert an object (BasicHeader) to textual data.
     *
     * @param source  The object to be marshalled.
     * @param writer  A stream to write to.
     * @param context A context that allows nested objects to be processed by XStream.
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        BasicHeader header = (BasicHeader) source;
        writer.addAttribute("name", header.getName());
        writer.addAttribute("value", header.getValue());
    }

    /**
     * Convert textual data back into an object.
     *
     * @param reader  The stream to read the text from.
     * @param context
     * @return The resulting object (BasicHeader)
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String name = reader.getAttribute("name");
        String value = reader.getAttribute("value");
        return fromString(name + "|" + value, BasicHeader.class, null);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return BasicHeader.class.equals(type);
    }

}
