package com.taf.automation.ui.support.providers;

import com.taf.automation.expressions.USAddress;
import com.taf.automation.ui.support.TestProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import datainstiller.data.DataPersistence;
import ui.auto.core.support.EnvironmentsSetup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@XStreamAlias("address-provider")
public class AddressProvider extends DataPersistence {
    private static final String ADDRESS_PROVIDER_KEY = "address-provider-config";
    private static final String ADDRESS_PROVIDER_RESOURCE_DEFAULT = "data/ui/sample-addresses.xml";
    private static AddressProvider instance;

    @XStreamImplicit(itemFieldName = "address")
    private List<StateAddresses> addresses;

    @XStreamOmitField
    Map<String, StateAddresses> addressCache;

    private static class StateAddresses {
        @XStreamAsAttribute
        private String state;

        @XStreamImplicit(itemFieldName = "address")
        private List<USAddress> addresses;

        public String getState() {
            return state;
        }

    }

    private AddressProvider() {
        //
    }

    @SuppressWarnings("java:S2168")
    public static AddressProvider getInstance() {
        if (instance == null) {
            synchronized (AddressProvider.class) {
                if (instance == null) {
                    String resource;
                    try {
                        EnvironmentsSetup.Environment env = TestProperties.getInstance().getTestEnvironment();
                        resource = env.getCustom(ADDRESS_PROVIDER_KEY);
                    } catch (Exception ex) {
                        resource = ADDRESS_PROVIDER_RESOURCE_DEFAULT;
                    }

                    instance = new AddressProvider().fromResource(resource);
                }
            }
        }

        return instance;
    }

    @SuppressWarnings("java:S2168")
    private List<StateAddresses> getAddresses() {
        if (addresses == null) {
            synchronized (this) {
                if (addresses == null) {
                    addresses = new ArrayList<>();
                }
            }
        }

        return addresses;
    }

    @SuppressWarnings("java:S2168")
    private Map<String, StateAddresses> getAddressCache() {
        if (addressCache == null) {
            synchronized (this) {
                if (addressCache == null) {
                    addressCache = getAddresses()
                            .stream()
                            .collect(Collectors.toMap(StateAddresses::getState, item -> item, (lhs, rhs) -> rhs));
                }
            }
        }

        return addressCache;
    }

    public USAddress get(String state) {
        return get(state, false);
    }

    public USAddress get(String state, boolean random) {
        List<USAddress> stateAddresses = getAll(state);
        if (random) {
            Collections.shuffle(stateAddresses);
        }

        return stateAddresses.get(0);
    }

    @SuppressWarnings("java:S112")
    public List<USAddress> getAll(String state) {
        StateAddresses stateAddresses = getAddressCache().get(state);
        if (stateAddresses == null || stateAddresses.addresses == null) {
            throw new RuntimeException("There was no address for state:  " + state);
        }

        return stateAddresses.addresses;
    }

    public List<USAddress> getAll() {
        List<USAddress> all = new ArrayList<>();

        for (StateAddresses stateAddresses : getAddresses()) {
            if (stateAddresses.addresses != null) {
                all.addAll(stateAddresses.addresses);
            }
        }

        return all;
    }

}
