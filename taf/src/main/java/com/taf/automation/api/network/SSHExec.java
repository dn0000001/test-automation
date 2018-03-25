package com.taf.automation.api.network;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to execute commands over a SSH session
 * http://stackoverflow.com/questions/2405885/run-a-command-over-ssh-with-jsch/11902536#11902536
 */
public class SSHExec {
    private static final Logger LOG = LoggerFactory.getLogger(SSHExec.class);
    private Session session;

    public SSHExec(Session session) {
        this.session = session;
    }

    /**
     * Execute command
     *
     * @param command - Command to be executed
     * @return Status of executing command
     */
    public String executeCommand(String command) {
        StringBuilder outputBuffer = new StringBuilder();

        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();
        } catch (IOException | JSchException e) {
            LOG.warn(e.getMessage());
            return null;
        }

        return outputBuffer.toString();
    }

    /**
     * Close all connections
     */
    public void disconnect() {
        session.disconnect();
    }

}
