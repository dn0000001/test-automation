package com.taf.automation.api.network;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.taf.automation.ui.support.TestProperties;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles creation of SSH Sessions
 */
public class SSHSession {
    private static final Logger LOG = LoggerFactory.getLogger(SSHSession.class);
    private Session session;
    private int port;
    private static int sessionTimeOut = TestProperties.getInstance().getSshTimeout();

    private static final String SSH_HOST = "SSH_HOST";
    private static final String SSH_PORT = "SSH_PORT";
    private Map<String, String> connectionInfo;

    /**
     * Create SSH Session and SSH Tunnel if necessary
     *
     * @param host - If SSH Tunnel is created, then this is the host address for local port forwarding
     *             else SSH Session Host
     * @param port - If SSH Tunnel is created, then this is the remote port number for local port forwarding
     *             else SSH Session port
     */
    public SSHSession(String host, int port) {
        connectionInfo = new HashMap<>();
        connectionInfo.put(SSH_HOST, host);
        connectionInfo.put(SSH_PORT, String.valueOf(port));

        TestProperties props = TestProperties.getInstance();
        String os = System.getProperty("os.name").toUpperCase();
        if (os.startsWith("MAC")) {
            this.port = createSshTunnelOnMac(props.getSshHost(), props.getSshPort(), host, port);
        }

        if (os.startsWith("LINUX")) {
            // If SSH Host is null, then just create SSH session instead of SSH tunnel
            // Note: This indicates that test is run in cloud
            if (props.getSshHost() == null) {
                createSshSessionOnLinux(host, port);
                this.port = port;
            } else {
                this.port = createSshTunnelOnLinux(props.getSshHost(), props.getSshPort(), host, port);
            }
        }
    }

    /**
     * Create SSH Tunnel on Mac using keychain
     *
     * @param sshHost  - SSH Host to connect to
     * @param sshPort  - SSH Port which the connection will be made
     * @param destHost - Host address for local port forwarding
     * @param destPort - Remote port number for local port forwarding
     * @return tunnel port
     */
    private int createSshTunnelOnMac(String sshHost, int sshPort, String destHost, int destPort) {
        createSshSessionOnMac(sshHost, sshPort);
        int tunnelPort;

        try {
            tunnelPort = session.setPortForwardingL(0, destHost, destPort);
            LOG.info("Set Port Forwarding on port " + tunnelPort);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        return tunnelPort;
    }

    /**
     * Create SSH Tunnel on Linux using SSH key
     *
     * @param sshHost  - SSH Host to connect to
     * @param sshPort  - SSH Port which the connection will be made
     * @param destHost - Host address for local port forwarding
     * @param destPort - Remote port number for local port forwarding
     * @return tunnel port
     */
    private int createSshTunnelOnLinux(String sshHost, int sshPort, String destHost, int destPort) {
        createSshSessionOnLinux(sshHost, sshPort);
        int tunnelPort;

        try {
            tunnelPort = session.setPortForwardingL(0, destHost, destPort);
            LOG.info("Set Port Forwarding on port " + tunnelPort);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        return tunnelPort;
    }

    /**
     * Create SSH Session on Mac using keychain
     *
     * @param host - SSH Host to connect to
     * @param port - SSH Port which the connection will be made
     */
    private void createSshSessionOnMac(String host, int port) {
        try {
            byte[] buffer = new byte[100];
            int i = Runtime.getRuntime().exec("security find-generic-password -wa ssh.pswd").getInputStream().read(buffer);
            if (i <= 0) {
                throw new RuntimeException("There is no ssh password in the keychain," +
                        " please create ssh password using Keychain Access application.");
            }

            String password = new String(buffer, 0, i - 1);
            String userName = System.getProperty("user.name");
            JSch jSch = new JSch();
            session = jSch.getSession(userName, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(sessionTimeOut);
            session.connect();
            LOG.info("Created SSH session on port " + port);
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create SSH Session on Linux using SSH key
     *
     * @param host - SSH Host to connect to
     * @param port - SSH Port which the connection will be made
     */
    private void createSshSessionOnLinux(String host, int port) {
        TestProperties props = TestProperties.getInstance();
        String user = props.getSshUser();
        String privateKey = System.getProperty("user.home") + "/.ssh/" + user;
        JSch jSch = new JSch();
        try {
            jSch.addIdentity(privateKey);
            session = jSch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(sessionTimeOut);
            session.connect();
            LOG.info("Created SSH session on port " + port);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create SSH Session
     *
     * @param host     - SSH Host to connect to
     * @param port     - SSH Port which the connection will be made
     * @param user     - SSH User
     * @param password - Password
     */
    private void createSshSession(String host, int port, String user, String password) {
        try {
            JSch jSch = new JSch();
            session = jSch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(sessionTimeOut);
            session.connect();
            LOG.info("Created SSH session on port " + port);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the Tunnel port or SSH session port
     *
     * @return Tunnel port or SSH session port
     */
    public int getPort() {
        return port;
    }

    /**
     * Closes the session<BR>
     * <B>Note: </B> This should only be called there is no more need of the session<BR>
     */
    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            LOG.info("SSH session on port " + port + " was closed");
        }
    }

    /**
     * Get SSH Shell to run commands
     *
     * @return SSHShell
     */
    public SSHShell getShell() {
        return new SSHShell(session);
    }

    /**
     * Get SSH Exec to run commands
     *
     * @return SSHExec
     */
    public SSHExec getExec() {
        return new SSHExec(session);
    }

    /**
     * Get SSH Exec to run commands
     *
     * @param user     - SSH User
     * @param password - Password
     * @return SSHExec
     */
    public SSHExec getExec(String user, String password) {
        close();
        createSshSession(connectionInfo.get(SSH_HOST), NumberUtils.toInt(connectionInfo.get(SSH_PORT)), user, password);
        return new SSHExec(session);
    }

}
