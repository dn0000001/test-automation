package com.taf.automation.ui.support;

import com.taf.automation.ui.support.util.DataInstillerUtils;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import datainstiller.data.DataAliases;
import datainstiller.data.PatternUnmarshalException;
import datainstiller.generators.GeneratorInterface;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JxltEngine;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is based on the DataAliasesConverter but there are changes to allow for custom generators to be used
 */
public class DataAliasesConverterV2 implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(DataAliasesConverterV2.class);
    private JexlContext jexlContext;

    public DataAliasesConverterV2(JexlContext jexlContext) {
        if (jexlContext == null) {
            this.jexlContext = new MapContext();
        } else {
            this.jexlContext = jexlContext;
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class type) {
        return (type.equals(DataAliases.class));
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (source == null) {
            return;
        }

        DataAliases aliases = (DataAliases) source;
        for (Map.Entry<String, String> item : aliases.entrySet()) {
            writer.startNode(item.getKey());
            writer.setValue(item.getValue());
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        DataAliases aliases = new DataAliases();
        String nodeName;
        String value;
        Object objValue = null;
        JxltEngine jxlt = new JexlBuilder().strict(true).silent(false).create().createJxltEngine();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            nodeName = reader.getNodeName();
            value = reader.getValue();
            if (value.matches("\\$\\[.+]")) {
                Pattern pattern = Pattern.compile("\\$\\[(.+)\\(\\s*'\\s*(.*)\\s*'\\s*,\\s*'\\s*(.*)\\s*'\\s*\\)");
                Matcher matcher = pattern.matcher(value);
                if (!matcher.find()) {
                    throw new PatternUnmarshalException(value + " - invalid data generation expression!");
                }

                GeneratorInterface genType = DataInstillerUtils.getGenerator(jexlContext).getGenerator(matcher.group(1).trim());
                String init = matcher.group(2);
                String val = matcher.group(3);
                value = genType.generate(init, val);
            } else {
                JxltEngine.Expression expr = jxlt.createExpression(value);
                try {
                    objValue = expr.evaluate(jexlContext);
                    value = objValue.toString();
                } catch (Exception e) {
                    LOG.warn(e.getMessage());
                }
            }

            aliases.put(nodeName, value);
            if (objValue != null) {
                jexlContext.set(nodeName, objValue);
            } else {
                jexlContext.set(nodeName, value);
            }

            objValue = null;
            reader.moveUp();
        }

        return aliases;
    }

}
