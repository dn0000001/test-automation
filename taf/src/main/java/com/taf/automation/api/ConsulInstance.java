package com.taf.automation.api;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.HealthService;
import com.taf.automation.api.network.MultiSshSession;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.TestProperties;
import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to work with consul
 */
public class ConsulInstance {
    private MultiSshSession session;
    private ConsulClient client;

    /**
     * Enumeration to provide easy consistent way to get a specific micro service
     */
    public enum MicroService {
        SERVICE_NAME("service-display-name");
        private String name;

        /**
         * Construct
         *
         * @param name - Name to find micro service
         */
        MicroService(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    public ConsulInstance() {
        createConsulConnection();
    }

    private void createConsulConnection() {
        TestProperties props = TestProperties.getInstance();
        session = new MultiSshSession()
                .withCredentials(props.getSshUser(), new CryptoUtils().decrypt(props.getSshPassword()))
                .withFirstHost(props.getSshHost(), props.getSshPort())
                .withSecondHost(props.getConsulHost(), props.getConsulPort())
                .withTimeout(props.getSshTimeout());
        session.connect();
        client = new ConsulClient(session.getLocalHost(), session.getAssignedPort());
    }

    /**
     * All URIs for the specified micro service
     *
     * @param service - Micro Service to get URI
     * @return empty list if no health micro service found else all the URIs
     */
    public List<URIBuilder> getAllURI(MicroService service) {
        List<URIBuilder> all = new ArrayList<>();

        Response<List<HealthService>> es = client.getHealthServices(service.toString(), true, QueryParams.DEFAULT);
        if (es == null || es.getValue() == null || es.getValue().isEmpty()) {
            return all;
        }

        for (HealthService item : es.getValue()) {
            URIBuilder uri = new URIBuilder();
            uri.setScheme("http");
            uri.setHost(item.getService().getAddress());
            uri.setPort(item.getService().getPort());
            all.add(uri);
        }

        return all;
    }

    /**
     * Get URI for the specified micro service
     *
     * @param service - Micro Service to get URI
     * @return null if no health micro service found else the first URI
     */
    public URIBuilder getURI(MicroService service) {
        List<URIBuilder> all = getAllURI(service);
        return (all.isEmpty()) ? null : all.get(0);
    }

    public void close() {
        if (session != null) {
            session.disconnect();
        }
    }

}
