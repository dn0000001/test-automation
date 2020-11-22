package com.taf.automation.ui.support.providers;

import com.taf.automation.ui.support.Parameter;
import com.taf.automation.ui.support.TestProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataPersistence;
import ui.auto.core.support.EnvironmentsSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XStreamAlias("key-value-provider")
public class KeyValueProvider extends DataPersistence {
    private static final String PROVIDER_KEY = "key-value-provider-config";
    private static final String PROVIDER_RESOURCE_DEFAULT = "data/ui/sample-common-test-data-items.xml";
    private static KeyValueProvider instance;

    @XStreamImplicit(itemFieldName = "item")
    private List<Parameter> items;

    @XStreamOmitField
    private Map<String, Parameter> cache;

    private KeyValueProvider() {
        //
    }

    @SuppressWarnings("squid:S2168")
    public static KeyValueProvider getInstance() {
        if (instance == null) {
            synchronized (KeyValueProvider.class) {
                if (instance == null) {
                    String resource;
                    try {
                        EnvironmentsSetup.Environment env = TestProperties.getInstance().getTestEnvironment();
                        resource = env.getCustom(PROVIDER_KEY);
                    } catch (Exception ex) {
                        resource = PROVIDER_RESOURCE_DEFAULT;
                    }

                    instance = new KeyValueProvider().fromResource(resource);
                }
            }
        }

        return instance;
    }

    @SuppressWarnings("squid:S2168")
    private List<Parameter> getItems() {
        if (items == null) {
            synchronized (this) {
                if (items == null) {
                    items = new ArrayList<>();
                }
            }
        }

        return items;
    }

    @SuppressWarnings("squid:S2168")
    private Map<String, Parameter> getCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = getItems()
                            .stream()
                            .collect(Collectors.toMap(Parameter::getParam, item -> item, (lhs, rhs) -> rhs));
                }
            }
        }

        return cache;
    }

    @SuppressWarnings("squid:S00112")
    public String get(String key) {
        Parameter data = getCache().get(key);
        if (data == null) {
            throw new RuntimeException("There was no value for key:  " + key);
        }

        return data.getValue();
    }

    public String getOrDefault(String key, String defaultValue) {
        try {
            return get(key);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

}
