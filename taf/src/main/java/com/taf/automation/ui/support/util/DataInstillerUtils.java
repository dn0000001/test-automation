package com.taf.automation.ui.support.util;

import com.taf.automation.api.converters.BasicHeaderConverter;
import com.taf.automation.api.converters.EnumConverter;
import com.taf.automation.ui.support.DataAliasesConverterV2;
import com.taf.automation.ui.support.Environment;
import com.taf.automation.ui.support.EnvironmentType;
import com.taf.automation.ui.support.generators.RandomDateGenerator;
import com.taf.automation.ui.support.generators.RandomRealUSAddressGenerator;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.ISO8601GregorianCalendarConverter;
import datainstiller.data.DataGenerator;
import datainstiller.data.DataValueConverter;
import datainstiller.generators.AddressGenerator;
import datainstiller.generators.AlphaNumericGenerator;
import datainstiller.generators.CustomListGenerator;
import datainstiller.generators.DateGenerator;
import datainstiller.generators.File2ListGenerator;
import datainstiller.generators.GeneratorInterface;
import datainstiller.generators.HumanNameGenerator;
import datainstiller.generators.NumberGenerator;
import datainstiller.generators.WordGenerator;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.http.message.BasicHeader;
import ui.auto.core.data.PageComponentDataConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils for DataInstiller related functions
 */
public class DataInstillerUtils {
    /**
     * All Custom Generators Information
     */
    private enum CustomGenerators {
        RANDOM_DATE_GENERATOR("RandomDateGen", "RANDOM_DATE", new RandomDateGenerator()),
        RANDOM_US_ADDRESS_GENERATOR("RealUSAddressGen", "RANDOM_US_ADDRESS", new RandomRealUSAddressGenerator());

        private String jexlKey;
        private String registerKey;
        private GeneratorInterface generator;

        CustomGenerators(String jexlKey, String registerKey, GeneratorInterface generator) {
            this.jexlKey = jexlKey;
            this.registerKey = registerKey;
            this.generator = generator;
        }

        public String getJexlKey() {
            return jexlKey;
        }

        public String getRegisterKey() {
            return registerKey;
        }

        public GeneratorInterface getGenerator() {
            return generator;
        }

    }

    private DataInstillerUtils() {
        //
    }

    /**
     * Get the default XStream<BR>
     * <B>Note: </B> It is necessary to use method <B>processAnnotations(this.getClass())</B> on the returned XStream
     * to prevent exception <B>CannotResolveClassException</B> when using XStreamAlias.
     *
     * @param jexlContext - Jexl Context if null a default will be provided
     * @return XStream
     */
    public static XStream getXStream(JexlContext jexlContext) {
        return getXStream(null, getConverters(), jexlContext);
    }

    /**
     * Get XStream<BR>
     * <B>Notes: </B>
     * <OL>
     * <LI>If XStream variable is null, then an XStream with the default configuration is returned with the converters
     * registered</LI>
     * <LI>If the converters variable is null, then no converters are registered with XStream</LI>
     * <LI>The default aliases are added to XStream</LI>
     * <LI>If the specified XStream is null, then it is necessary to use method
     * <B>processAnnotations(this.getClass())</B> on the returned XStream to prevent exception
     * <B>CannotResolveClassException</B> when using XStreamAlias.</LI>
     * <LI>If the Jexl Context is null, then a Jexl Context with the default configuration is used</LI>
     * </OL>
     *
     * @param existingXStream    - XStream variable to register the converters to
     * @param existingConverters - Existing converters to be registered
     * @param jexlContext        - Jexl Context
     * @return XStream
     */
    public static XStream getXStream(XStream existingXStream, List<DataValueConverter> existingConverters, JexlContext jexlContext) {
        XStream xstream = (existingXStream == null) ? getDefaultXStream(jexlContext) : existingXStream;
        addAliases(xstream);
        registerConverters(xstream, existingConverters);
        return xstream;
    }

    /**
     * Get Default configured XStream<BR>
     * <B>Note: </B> It is necessary to use method <B>processAnnotations(this.getClass())</B> on the returned XStream
     * to prevent exception <B>CannotResolveClassException</B> when using XStreamAlias.
     *
     * @param existingJexlContext - Jexl Context
     * @return XStream
     */
    private static XStream getDefaultXStream(JexlContext existingJexlContext) {
        JexlContext jexlContext = (existingJexlContext == null) ? getDefaultJexlContext() : existingJexlContext;
        addCustomGenerators(jexlContext);
        XStream xstream = new XStream();

        xstream.registerConverter(new DataAliasesConverterV2(jexlContext));
        xstream.registerConverter(new ISO8601GregorianCalendarConverter());

        return xstream;
    }

    /**
     * Get the Default JexlContext
     *
     * @return JexlContext
     */
    private static JexlContext getDefaultJexlContext() {
        JexlContext jContext = new MapContext();

        // Default Generators from DataPersistence
        jContext.set("AddressGen", new AddressGenerator());
        jContext.set("AlphaNumericGen", new AlphaNumericGenerator());
        jContext.set("ListGen", new CustomListGenerator());
        jContext.set("DateGen", new DateGenerator());
        jContext.set("HumanNameGen", new HumanNameGenerator());
        jContext.set("NumberGen", new NumberGenerator());
        jContext.set("WordGen", new WordGenerator());
        jContext.set("File2ListGen", new File2ListGenerator());
        jContext.set("now", LocalDateTime.now());
        jContext.set("DateTimeFormatter", DateTimeFormatter.BASIC_ISO_DATE);

        return jContext;
    }

    /**
     * Add the custom generators to the JexlContext
     *
     * @param jContext - JexlContext to add the custom generators to
     */
    private static void addCustomGenerators(JexlContext jContext) {
        for (CustomGenerators item : CustomGenerators.values()) {
            jContext.set(item.getJexlKey(), item.getGenerator());
        }
    }

    /**
     * Register the custom generators to the specified data generator
     *
     * @param existingGenerator - Data Generator to register the custom generators to
     */
    private static void registerCustomGenerators(DataGenerator existingGenerator) {
        for (CustomGenerators item : CustomGenerators.values()) {
            existingGenerator.registerGenerator(item.getRegisterKey(), item.getGenerator());
        }
    }

    /**
     * Add Aliases to XStream<BR>
     * <B>Note: </B> Add application specific aliases to this method
     *
     * @param xstream - XStream variable to add the aliases
     */
    private static void addAliases(XStream xstream) {
        xstream.alias("header", BasicHeader.class);
    }

    /**
     * Register converters with XStream
     *
     * @param xstream    - XStream variable to register the converters
     * @param converters - Converters to be registered
     */
    private static void registerConverters(XStream xstream, List<DataValueConverter> converters) {
        if (xstream == null || converters == null) {
            return;
        }

        for (DataValueConverter converter : converters) {
            xstream.registerConverter(converter);
        }
    }

    /**
     * Get the default converters<BR>
     * <B>Note: </B> Add application specific converters to this method
     *
     * @return List&lt;DataValueConverter&gt;
     */
    public static List<DataValueConverter> getConverters() {
        List<DataValueConverter> converters = new ArrayList<>();

        converters.add(new PageComponentDataConverter());
        converters.add(new BasicHeaderConverter());
        converters.add(new EnumConverter(EnvironmentType.class, Environment.QA));

        return converters;
    }

    /**
     * Get the default generator<BR>
     * <B>Note:  </B> If the Jexl Context is null, then a Jexl Context with the default configuration is used
     *
     * @param jexlContext - Jexl Context
     * @return DataGenerator
     */
    public static DataGenerator getGenerator(JexlContext jexlContext) {
        DataGenerator generator = new DataGenerator(getXStream(jexlContext), getConverters());
        registerCustomGenerators(generator);
        return generator;
    }

}
