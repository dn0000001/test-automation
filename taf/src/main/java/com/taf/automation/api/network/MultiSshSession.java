package com.taf.automation.api.network;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * This class creates an SSH Session to the first host and from that session initializes port forwarding to the second host
 */
public class MultiSshSession {
    private static final Logger LOG = LoggerFactory.getLogger(MultiSshSession.class);
    private static final String LOCALHOST = "localhost";
    private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private String host1;
    private int port1;
    private String host2;
    private int port2;
    private String username;
    private String password;
    private int timeout;
    private int assignedPort;
    private Session session;

    /**
     * Sets the first host information used to create the initial SSH session
     *
     * @param host - Host to create the initial SSH session to
     * @param port - Port for the initial SSH session
     * @return MultiSshSession
     */
    public MultiSshSession withFirstHost(String host, int port) {
        host1 = host;
        port1 = port;
        return this;
    }

    /**
     * Sets the second host information used to initialize the port forwarding
     *
     * @param host - Host to initialize the port forwarding
     * @param port - Port for the initialization of the port forwarding
     * @return MultiSshSession
     */
    public MultiSshSession withSecondHost(String host, int port) {
        host2 = host;
        port2 = port;
        return this;
    }

    /**
     * Set the credentials used in creating the SSH session and the port forwarding
     *
     * @param username - Username
     * @param password - Password
     * @return MultiSshSession
     */
    public MultiSshSession withCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Set the timeout for setting up the SSH session
     *
     * @param timeout - Timeout in milliseconds
     * @return MultiSshSession
     */
    public MultiSshSession withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int connect() {
        JSch jSch = new JSch();
        assignedPort = -1;

        try {
            session = jSch.getSession(username, host1, port1);
            session.setPassword(password);
            session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
            session.setTimeout(timeout);
            session.connect();
            assignedPort = session.setPortForwardingL(0, host2, port2);
            LOG.info("Set Port Forwarding on port {}", assignedPort);
        } catch (JSchException e) {
            LOG.error("Create SSH Session failure:  ", e);
        }

        assertThat("SSH Session setup failed as port was less than 1", assignedPort, greaterThan(0));
        return assignedPort;
    }

    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            LOG.info("SSH session on port {} was closed", assignedPort);
        }
    }

    /**
     * Get the localhost string
     *
     * @return String
     */
    public String getLocalHost() {
        return LOCALHOST;
    }

    /**
     * Get the assigned port from the port forwarding
     *
     * @return int
     */
    public int getAssignedPort() {
        return assignedPort;
    }

}
