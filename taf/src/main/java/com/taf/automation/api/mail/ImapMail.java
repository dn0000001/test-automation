package com.taf.automation.api.mail;

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
import javax.mail.Store;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ImapMail {
    private static final Logger LOG = LoggerFactory.getLogger(ImapMail.class);
    private SSHSession sshSession;
    private Store store;
    private final String mailServer = TestProperties.getInstance().getMailServer();
    private final long mailTimeOut = TestProperties.getInstance().getMailTimeout();
    private boolean headerMatchesEnv;

    public ImapMail() {
        withHeaderMatchesEnv(true);

        TestProperties tProps = TestProperties.getInstance();
        Properties props = System.getProperties();
        int port = (tProps.getMailServerPort() > 0) ? tProps.getMailServerPort() : 993;

        props.setProperty("mail.imap.ssl.trust", "*");
        props.setProperty("mail.imap.port", "" + port);
        Session session = Session.getDefaultInstance(props);
        try {
            store = session.getStore("imaps");
        } catch (NoSuchProviderException e) {
            LOG.error("Exception occurred in ImapMail()", e);
        }
    }

    /**
     * Set flag that indicates the header on the emails is needed for a match when searching
     *
     * @param headerMatchesEnv - true to consider the header matches the environment when search
     * @return ImapMail
     */
    public ImapMail withHeaderMatchesEnv(boolean headerMatchesEnv) {
        this.headerMatchesEnv = headerMatchesEnv;
        return this;
    }

    private Folder getFolder(String user, String encodedPassword) {
        Folder folder = null;
        try {
            if (store.isConnected()) {
                store.close();
            }

            store.connect(mailServer, user, new CryptoUtils().decrypt(encodedPassword));
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            LOG.error("Exception occurred in ImapMail.getFolder", e);
        }

        return folder;
    }

    @SuppressWarnings("java:S112")
    private Folder getFolderForUser(String user, String encodedPassword) {
        Folder folder;
        long timeout = System.currentTimeMillis() + mailTimeOut;
        do {
            folder = getFolder(user, encodedPassword);
            if (folder == null) {
                Utils.sleep(200);
            }
        } while (folder == null && System.currentTimeMillis() < timeout);

        if (folder == null) {
            throw new RuntimeException("Timeout reached on email store connection:  " + user);
        }

        return folder;
    }

    public List<MailMessage> searchForEmail(String recipient, boolean waitForEmail, boolean deleteEmails) {
        String encodedPassword = TestProperties.getInstance().getMailPassword();
        return searchForEmail(recipient, encodedPassword, waitForEmail, deleteEmails);
    }

    public List<MailMessage> searchForEmail(String recipient, String encodedPassword, boolean waitForEmail, boolean deleteEmails) {
        return searchForEmail(recipient, encodedPassword, "", "", waitForEmail, deleteEmails);
    }

    public List<MailMessage> searchForEmail(String recipient, boolean waitForEmail, boolean deleteEmails, String subject) {
        String encodedPassword = TestProperties.getInstance().getMailPassword();
        return searchForEmail(recipient, encodedPassword, waitForEmail, deleteEmails, subject);
    }

    public List<MailMessage> searchForEmail(String recipient, String encodedPassword, boolean waitForEmail, boolean deleteEmails, String subject) {
        return searchForEmail(recipient, encodedPassword, subject, "", waitForEmail, deleteEmails);
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
                    if (headerMatchesEnv && !env.isProdEnv() && !headers.contains(env.toString())) {
                        return false;
                    }

                    if (StringUtils.defaultString(message.getSubject()).contains(subject)) {
                        String content = new MailMessage(message).getTextBody();
                        if (content.contains(text)) {
                            return true;
                        }
                    }
                } catch (MessagingException | IOException e) {
                    LOG.error("Exception occurred in ImapMail.getSearchTerm", e);
                }

                return false;
            }
        };
    }

    public List<MailMessage> searchForEmail(
            String recipient,
            String subject,
            String text,
            boolean waitForEmail,
            boolean deleteEmails
    ) {
        String encodedPassword = TestProperties.getInstance().getMailPassword();
        return searchForEmail(recipient, encodedPassword, subject, text, waitForEmail, deleteEmails);
    }

    @SuppressWarnings("java:S112")
    public List<MailMessage> searchForEmail(
            String recipient,
            String encodedPassword,
            String subject,
            String text,
            boolean waitForEmail,
            boolean deleteEmails
    ) {
        List<MailMessage> mailMessages = new ArrayList<>();
        SearchTerm term = getSearchTerm(subject, text);

        try {
            long timeout = System.currentTimeMillis();
            if (waitForEmail) {
                timeout += mailTimeOut;
            }

            Folder folder = getFolderForUser(recipient, encodedPassword);
            Message[] messages;
            do {
                messages = folder.search(term);
                if (waitForEmail && messages.length == 0) {
                    folder.close(false);
                    folder.open(Folder.READ_WRITE);
                }
            } while (messages.length == 0 && System.currentTimeMillis() < timeout);

            if (waitForEmail && messages.length == 0) {
                throw new RuntimeException("No emails where received for " + recipient + " in " + mailTimeOut + " milliseconds!");
            }

            for (Message msg : messages) {
                mailMessages.add(new MailMessage(msg));
                msg.setFlag(Flags.Flag.DELETED, deleteEmails);
            }

            folder.close(deleteEmails);
        } catch (MessagingException | IOException e) {
            LOG.error("Exception occurred in ImapMail.searchForEmail", e);
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
        return getAllMessages(recipient, TestProperties.getInstance().getMailPassword());
    }

    /**
     * Get all messages for the recipient regardless of the header information which specifies the environment
     *
     * @param recipient       - Recipient to get all messages
     * @param encodedPassword - Encoded Password for the Recipient
     * @return null if MessagingException occurs else all the messages
     */
    public List<MailMessage> getAllMessages(String recipient, String encodedPassword) {
        List<MailMessage> mailMessages = new ArrayList<>();

        try {
            Folder folder = getFolderForUser(recipient, encodedPassword);
            Message[] messages = folder.getMessages();
            for (Message msg : messages) {
                mailMessages.add(new MailMessage(msg));
            }
        } catch (MessagingException | IOException e) {
            LOG.error("Exception occurred in ImapMail.getAllMessages", e);
        }

        return mailMessages;
    }

    public void deleteAll(String recipient) {
        deleteAll(recipient, TestProperties.getInstance().getMailPassword());
    }

    public void deleteAll(String recipient, String encodedPassword) {
        if (getFolder(recipient, encodedPassword) != null) {
            List<MailMessage> msgs = searchForEmail(recipient, false, true);
            LOG.info("Deleted {} mail messages", msgs.size());
        }
    }

    public void close() {
        try {
            store.close();
        } catch (MessagingException e) {
            LOG.error("Exception occurred in ImapMail.close()", e);
        }

        if (sshSession != null) {
            sshSession.close();
        }
    }

}
