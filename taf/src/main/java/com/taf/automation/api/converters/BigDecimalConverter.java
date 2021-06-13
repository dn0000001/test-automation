package com.taf.automation.api.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataValueConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Converter for BigDecimal
 */
public class BigDecimalConverter implements DataValueConverter {
    /**
     * Parses the specified string into an object (BigDecimal)
     *
     * @param str   - String to be parsed
     * @param cls   - Is not used in this specific implementation
     * @param field - Is not used in this specific implementation
     * @return BigDecimal
     */
    @Override
    public <T> T fromString(String str, Class<T> cls, Field field) {
        BigDecimal bd;
        try {
            bd = (StringUtils.isNotBlank(str)) ? new BigDecimal(str) : null;
        } catch (Exception ex) {
            bd = BigDecimal.ZERO;
        }

        return (T) bd;
    }

    /**
     * Convert an object (BigDecimal) to textual data.
     *
     * @param source  The object to be marshalled.
     * @param writer  A stream to write to.
     * @param context A context that allows nested objects to be processed by XStream.
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(String.valueOf(source));
    }

    /**
     * Convert textual data back into an object.
     *
     * @param reader  The stream to read the text from.
     * @param context A context that allows nested objects to be processed by XStream.
     * @return The resulting object (BigDecimal)
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return fromString(reader.getValue(), BigDecimal.class, null);
    }

    @Override
    public boolean canConvert(Class type) {
        return BigDecimal.class.equals(type);
    }

}
