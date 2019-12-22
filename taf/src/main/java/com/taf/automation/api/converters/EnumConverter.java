package com.taf.automation.api.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataValueConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Converter for Generic Enumeration
 */
public class EnumConverter implements DataValueConverter {
    @SuppressWarnings("rawtypes")
    private Class type;
    private Enum<?> typeEnum;

    /**
     * Constructor
     *
     * @param type     - Enumeration class
     * @param typeEnum - Specific enumeration
     */
    @SuppressWarnings("rawtypes")
    public EnumConverter(Class type, Enum typeEnum) {
        this.type = type;
        this.typeEnum = typeEnum;
    }

    /**
     * Parses the specified string into an object (Enum)
     *
     * @param str   - String to be parsed
     * @param cls   - Specific enumeration
     * @param field - Is not used in this specific implementation
     * @return Specific enumeration
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> T fromString(String str, Class<T> cls, Field field) {
        Enum enumValue = null;
        try {
            enumValue = Enum.valueOf(typeEnum.getClass(), str);
        } catch (Exception e) {
        }

        if (enumValue == null) {
            enumValue = typeEnum;
        }

        Object type;
        try {
            type = cls.newInstance();
            Method m = cls.getMethod("setValue", typeEnum.getClass());
            m.invoke(type, enumValue);
            return (T) type;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert an object (Enumeration) to textual data.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) The enumeration must have method <B>getValue</B><BR>
     *
     * @param source  The object to be marshalled.
     * @param writer  A stream to write to.
     * @param context A context that allows nested objects to be processed by XStream.
     */
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Method m = null;
        String value = null;
        try {
            m = source.getClass().getMethod("getValue");
            Object r = m.invoke(source);
            if (r.getClass().equals(typeEnum.getClass())) {
                value = ((Enum<?>) r).name();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        writer.startNode("value");
        writer.setValue(value);
        writer.endNode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        reader.moveDown();
        Object o = fromString(reader.getValue(), type, null);
        reader.moveUp();
        return o;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return type.equals(this.type);
    }

}
