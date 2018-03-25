package com.taf.automation.api.mail;

import com.taf.automation.ui.support.TestProperties;
import com.sun.mail.pop3.POP3Store;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static java.lang.String.format;

/**
 * This class contains methods to work with GMail
 */
public class GMailUtils {
    public static final Logger LOG = LoggerFactory.getLogger(GMailUtils.class);

    /**
     * Get Last Messages
     *
     * @param userName             - User Name
     * @param password             - Password
     * @param numberOfLastMessages - Number of messages
     * @return List&lt;MailMessage&gt;
     * @throws MessagingException
     * @throws IOException
     */
    public static List<MailMessage> getLastMessages(String userName, String password, int numberOfLastMessages) throws MessagingException, IOException {
        POP3Store emailStore = createEmailStore();
        emailStore.connect(userName, password);

        Folder emailFolder = emailStore.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        return getLastMessages(numberOfLastMessages, emailFolder);
    }

    /**
     * Get Last Messages
     *
     * @param numberOfLastMessages - Number of messages
     * @param emailFolder          - Email Folder
     * @return List&lt;MailMessage&gt;
     * @throws MessagingException
     * @throws IOException
     */
    private static List<MailMessage> getLastMessages(int numberOfLastMessages, Folder emailFolder) throws MessagingException, IOException {
        int totalMessages = emailFolder.getMessageCount();
        if (totalMessages > 0) {
            Message[] messages = emailFolder.getMessages(Math.max(1, totalMessages - numberOfLastMessages), totalMessages);
            List<MailMessage> list = new LinkedList<>();

            for (int i = messages.length - 1; i >= 0; i--) {
                list.add(new MailMessage(messages[i]));
            }

            return list;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Create Email Store
     *
     * @return POP3Store
     * @throws NoSuchProviderException
     */
    private static POP3Store createEmailStore() throws NoSuchProviderException {
        String proxy = TestProperties.getInstance().getMailProxy();
        Properties properties = new Properties();
        if (proxy != null) {
            LOG.info("Using proxy to access gmail");
            String proxyParts[] = proxy.split(":");
            properties.put("mail.pop3.host", proxyParts[0].trim());
            properties.put("mail.pop3.port", Integer.parseInt(proxyParts[1].trim()));
        } else {
            LOG.info("Direct access to pop.gmail.com");
            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", 995);
            properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.pop3.socketFactory.fallback", "false");
            properties.setProperty("mail.pop3.socketFactory.port", "995");
        }

        Session session = Session.getInstance(properties);
        return (POP3Store) session.getStore("pop3");
    }

    /**
     * Delete all messages
     *
     * @param userName - UserName
     * @param password - Password
     */
    public static void deleteAllMessages(String userName, String password) {
        try {
            LOG.info("Email Address={}", userName);
            POP3Store emailStore = createEmailStore();
            emailStore.connect(userName, password);

            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            Message[] messages = emailFolder.getMessages();
            int totalMessage = emailFolder.getMessageCount();
            LOG.info("messages.length={}", totalMessage);
            if (totalMessage == 0) {
                LOG.info("No emails");
            } else {
                for (int i = 0; i < totalMessage; i++) {
                    Message message = messages[i];
                    LOG.info("---------------------------------");
                    LOG.info("Email Number {}", i);
                    LOG.info("Subject: {}", message.getSubject());
                    LOG.info("From: {}", message.getFrom()[0]);

                    String subject = message.getSubject();
                    message.setFlag(Flags.Flag.DELETED, true);
                    LOG.info("Marked DELETE for message: {}", subject);
                }

                emailFolder.close(true);
                emailStore.close();
            }
        } catch (MessagingException e) {
            LOG.warn("Could not get the message successfully");
            e.printStackTrace();
        }
    }

    /**
     * Get TypeSafeMatcher for an example activation mail for
     *
     * @param email   - Email
     * @param subject - Subject
     * @return org.hamcrest.Matcher&lt;MailMessage&gt;
     */
    public static org.hamcrest.Matcher<MailMessage> getActivationMailFor(final String email, final String subject) {
        return new TypeSafeMatcher<MailMessage>() {
            @Override
            protected boolean matchesSafely(MailMessage message) {
                return message.getTo().contains(email) && message.getSubject().equals(subject);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(format("Mail from user '%s' with subject \"%s\"", email, subject));
            }
        };
    }

    /**
     * Example to get Activation Email String
     *
     * @param email   - Email
     * @param subject - Subject
     * @return String
     */
    public static String getActivationEmailStringFor(final String email, final String subject) {
        return format("<donot-reply@yourcompany.ca> to %s with subject \"%s\"", email, subject);
    }

    /**
     * Example to get Retrieve Email
     *
     * @param email       - Email
     * @param uniqueEmail - Unique Email
     * @param password    - Password
     * @param emailTitle  - Email Title
     * @return List&lt;MailMessage&gt;
     */
    public static List<MailMessage> retrieveActivationEmail(String email, String uniqueEmail, String password, String emailTitle) {
        List<MailMessage> messages = null;
        long startTime = System.currentTimeMillis();
        long t_o = startTime + 60000;
        boolean condition = false;
        do {
            try {
                messages = GMailUtils.getLastMessages(email, password, 10);
                condition = messages.toString().contains(GMailUtils.getActivationEmailStringFor(uniqueEmail, emailTitle));
                if (!condition) {
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof AuthenticationFailedException) {
                    break;
                }
            }
        }
        while (System.currentTimeMillis() < t_o && !condition);

        Assert.assertTrue(condition, "did not receive the email");
        LOG.info("It took {} milliseconds to receive email", (System.currentTimeMillis() - startTime));
        List<MailMessage> msgs = new ArrayList<>();
        for (MailMessage message : messages) {
            if (message.getSubject().contains(emailTitle)) {
                msgs.add(message);
            }
        }

        return msgs;
    }

}
