package com.taf.automation.api.network;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Creates and works with SSH Shell
 */
public class SSHShell {
    private static final String PROMPT = "[[-:-]]";
    private static final String NEWLINE = "\n";
    private static final String COMMENT = "# ";

    private ChannelShell shell;
    private InputStream receive;
    private OutputStream send;

    /**
     * Constructor
     *
     * @param session - Used to open the shell immediately
     */
    public SSHShell(Session session) {
        try {
            shell = (ChannelShell) session.openChannel("shell");
            shell.connect();
            receive = shell.getInputStream();
            send = shell.getOutputStream();

            // Change the prompt such that we can know when the response is complete
            executeCommand("PS1=" + PROMPT, false);
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read all the received output
     *
     * @param captureOutput - Output to console
     * @return Status of executing command
     * @throws IOException
     */
    private int readAllReceivedOutput(boolean captureOutput) throws IOException {
        StringBuilder all = new StringBuilder();
        return readAllReceivedOutput(captureOutput, all);
    }

    /**
     * Read all the received output
     *
     * @param captureOutput - Output to console
     * @param all           - Results from executing the command
     * @return Status of executing command
     * @throws IOException
     */
    private int readAllReceivedOutput(boolean captureOutput, StringBuilder all) throws IOException {
        final StringBuilder accumulator = new StringBuilder();
        final StringBuilder cl = new StringBuilder().append(COMMENT);
        boolean eol = true;
        int status = 100;
        while (true) {
            final char ch = (char) receive.read();
            switch (ch) {
                case '\r':
                case '\n':
                    if (!eol) {
                        if (captureOutput) {
                            System.out.println(cl.toString());
                        }

                        // Store the line to be returned
                        accumulator.append(StringUtils.removeStart(cl.toString(), COMMENT) + NEWLINE);

                        // Reset for next line
                        cl.setLength(0);
                    }

                    eol = true;
                    break;
                default:
                    if (eol) {
                        eol = false;
                    }

                    cl.append(ch);
                    break;
            }

            // Look for our predefined line that indicates status of a command such that we can parse it
            if (cl.toString().matches("EXIT STATUS\\[\\d++\\]")) {
                String code = cl.subSequence(cl.indexOf("[") + 1, cl.indexOf("]")).toString();
                status = Integer.parseInt(code);
            }

            // Look for our predefined line that indicates that we are done
            if (cl.toString().equals(PROMPT)) {
                all.append(StringUtils.removeEnd(accumulator.toString(), NEWLINE));
                return status;
            }
        }
    }

    /**
     * Execute command
     *
     * @param command - Command to be executed
     * @return Status of executing command
     */
    public int executeCommand(String command) {
        return executeCommand(command, true);
    }

    /**
     * Execute command
     *
     * @param command       - Command to be executed
     * @param captureOutput - Output to console
     * @return Status of executing command
     */
    public int executeCommand(String command, boolean captureOutput) {
        StringBuilder all = new StringBuilder();
        return executeCommand(command, captureOutput, all);
    }

    /**
     * Execute command
     *
     * @param command       - Command to be executed
     * @param captureOutput - Output to console
     * @param all           - Results from executing the command
     * @return Status of executing command
     */
    public int executeCommand(String command, boolean captureOutput, StringBuilder all) {
        try {
            send.write((command + '\n').getBytes());
            send.flush();
            readAllReceivedOutput(captureOutput, all);

            // Send this command such to know to status of the previous command
            send.write("echo EXIT STATUS[$?]\n".getBytes());
            send.flush();
            return readAllReceivedOutput(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 20;
    }

    /**
     * Close all connections
     */
    public void disconnect() {
        try {
            receive.close();
            send.close();
            if (!shell.isClosed()) {
                shell.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
