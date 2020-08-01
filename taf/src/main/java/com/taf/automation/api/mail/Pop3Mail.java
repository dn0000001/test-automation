package com.taf.automation.api.mail;

import com.sun.mail.pop3.POP3Store;
import com.taf.automation.api.network.SSHSession;
import com.taf.automation.ui.support.util.CryptoUtils;
import com.taf.automation.ui.support.Environment;
import com.taf.automation.ui.support.TestProperties;
import com.taf.automation.ui.support.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Pop3Mail {
    private static final Logger LOG = LoggerFactory.getLogger(Pop3Mail.class);
    private SSHSession sshSession;
    private POP3Store store;
    private final long mailTimeOut = TestProperties.getInstance().getMailTimeout();

    public Pop3Mail() {
        TestProperties tProps = TestProperties.getInstance();
        Properties props = System.getProperties();
        String mailServer = tProps.getMailServer();
        int port = (tProps.getMailServerPort() > 0) ? tProps.getMailServerPort() : 110;

        if (tProps.getSshHost() != null) {
            sshSession = new SSHSession(mailServer, port);
            port = sshSession.getPort();
            mailServer = "localhost";
        }

        props.setProperty("mail.pop3.host", mailServer);
        props.setProperty("mail.pop3.port", "" + port);
        Session session = Session.getDefaultInstance(props);
        try {
            store = (POP3Store) session.getStore("pop3");
        } catch (NoSuchProviderException e) {
            LOG.error("Exception occurred in Pop3Mail()", e);
        }
    }

    private Folder getFolder(String user) {
        Folder folder = null;
        try {
            if (store.isConnected()) {
                store.close();
            }

            store.connect(user, new CryptoUtils().decrypt(TestProperties.getInstance().getMailPassword()));
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            LOG.error("Exception occurred in Pop3Mail.getFolder", e);
        }

        return folder;
    }

    @SuppressWarnings("java:S112")
    private Folder getFolderForUser(String user) {
        Folder folder;
        long time_out = System.currentTimeMillis() + mailTimeOut;
        do {
            folder = getFolder(user);
            if (folder == null) {
                Utils.sleep(200);
            }
        } while (folder == null && System.currentTimeMillis() < time_out);

        if (folder == null) {
            throw new RuntimeException("Timeout reached on email store connection:  " + user);
        }

        return folder;
    }

    public List<MailMessage> searchForEmail(String recipient, boolean waitForEmail, boolean deleteEmails) {
        return searchForEmail(recipient, "", "", waitForEmail, deleteEmails);
    }

    public List<MailMessage> searchForEmail(String recipient, String subject, boolean waitForEmail, boolean deleteEmails) {
        return searchForEmail(recipient, subject, "", waitForEmail, deleteEmails);
    }

    private SearchTerm getSearchTerm(String subject, String text) {
        return new SearchTerm() {
            public boolean match(Message message) {
                try {
                    //
                    // This should be customized based on how emails are different between environments
                    //
                    Environment env = TestProperties.getInstance().getEnvironment();
                    String[] headerArray = message.getHeader("X-Env-Flag");
                    String headers = (headerArray != null) ? Arrays.asList(headerArray).toString().toLowerCase() : "";
                    if (!env.isProdEnv() && !headers.contains(env.toString())) {
                        return false;
                    }

                    if (StringUtils.defaultString(message.getSubject()).contains(subject)) {
                        String content = new MailMessage(message).getTextBody();
                        if (content.contains(text)) {
                            message.setFlag(Flags.Flag.DELETED, true);
                            return true;
                        }
                    }
                } catch (MessagingException | IOException e) {
                    LOG.error("Exception occurred in Pop3Mail.getSearchTerm", e);
                }

                return false;
            }
        };
    }

    @SuppressWarnings("java:S112")
    public List<MailMessage> searchForEmail(
            String recipient,
            String subject,
            String text,
            boolean waitForEmail,
            boolean deleteEmails
    ) {
        List<MailMessage> mailMessages = new ArrayList<>();
        SearchTerm term = getSearchTerm(subject, text);

        try {
            long time_out = System.currentTimeMillis();
            if (waitForEmail) {
                time_out += mailTimeOut;
            }

            Folder folder = getFolderForUser(recipient);
            Message[] messages;
            do {
                messages = folder.search(term);
                if (waitForEmail && messages.length == 0) {
                    folder.close(false);
                    folder.open(Folder.READ_WRITE);
                }
            } while (messages.length == 0 && System.currentTimeMillis() < time_out);

            if (waitForEmail && messages.length == 0) {
                throw new RuntimeException("No emails where received for " + recipient + " in " + mailTimeOut + " milliseconds!");
            }

            for (Message msg : messages) {
                mailMessages.add(new MailMessage(msg));
            }

            folder.close(deleteEmails);
        } catch (MessagingException | IOException e) {
            LOG.error("Exception occurred in Pop3Mail.searchForEmail", e);
        }

        return mailMessages;
    }

    /**
     * Get all messages for the recipient regardless of the header information which specifies the environment
     *
     * @param recipient - Recipient to get all messages
     * @return null if MessagingException occurs else all the messages
     */
    public List<MailMessage> getAllMessages(String recipient) {
        List<MailMessage> mailMessages = new ArrayList<>();

        try {
            Folder folder = getFolderForUser(recipient);
            Message[] messages = folder.getMessages();
            for (Message msg : messages) {
                mailMessages.add(new MailMessage(msg));
            }
        } catch (MessagingException | IOException e) {
            LOG.error("Exception occurred in Pop3Mail.getAllMessages", e);
        }

        return mailMessages;
    }

    public void deleteAll(String recipient) {
        if (getFolder(recipient) != null) {
            List<MailMessage> msgs = searchForEmail(recipient, false, true);
            LOG.info("Deleted {} mail messages", msgs.size());
        }
    }

    public void close() {
        try {
            store.close();
        } catch (MessagingException e) {
            LOG.error("Exception occurred in Pop3Mail.close()", e);
        }

        if (sshSession != null) {
            sshSession.close();
        }
    }

}
