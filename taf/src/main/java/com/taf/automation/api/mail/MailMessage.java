package com.taf.automation.api.mail;

import org.apache.commons.text.StringEscapeUtils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract information from an e-mail message
 */
public class MailMessage {
    private static final Pattern USER_ACTIVATION_PATTERN = Pattern.compile("<a href=\"(.*?)\".*>Click here to confirm your email address.</a>");
    private static final String EMPTY_STRING = "";

    private final String to;
    private final String from;
    private final String subject;
    private final String htmlBody;
    private final String textBody;
    private final Date sentDate;

    /**
     * Constructor<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Only the first recipient is stored<BR>
     *
     * @param message - Email message
     * @throws MessagingException
     * @throws IOException
     */
    public MailMessage(Message message) throws MessagingException, IOException {
        this.from = message.getFrom()[0].toString();
        this.to = message.getRecipients(Message.RecipientType.TO)[0].toString();
        this.subject = message.getSubject();
        this.htmlBody = extractContent((MimeMultipart) message.getContent(), "text/html");
        this.textBody = extractContent((MimeMultipart) message.getContent(), "plain/text");
        this.sentDate = message.getSentDate();
    }

    /**
     * Extract the e-mail content
     *
     * @param multipart - Content
     * @param mimeType  - Mime Type of content to extract
     * @return String
     */
    private String extractContent(MimeMultipart multipart, String mimeType) {
        StringBuilder content = new StringBuilder();
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.getContentType().startsWith(mimeType)) {
                    content.append((String) bodyPart.getContent());
                } else if (bodyPart.getContent() instanceof MimeMultipart) {
                    content.append(extractContent((MimeMultipart) bodyPart.getContent(), mimeType));
                }
            }

            return content.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to extract message", e);
        }
    }

    @Override
    public String toString() {
        return String.format("(Mail from %s to %s with subject \"%s\" at %s)", from, to, subject, formatDate(sentDate));
    }

    /**
     * Format date to "dd-MM-yyyy HH:mm:ss"
     *
     * @param date - Date to be formated
     * @return String
     */
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * Get the 'To' recipient (1st)
     *
     * @return String
     */
    public String getTo() {
        return to;
    }

    /**
     * Get the HTML Body content
     *
     * @return String
     */
    public String getHtmlBody() {
        return htmlBody;
    }

    /**
     * Get the Text Body content
     *
     * @return String
     */
    public String getTextBody() {
        return textBody;
    }

    /**
     * Get the Subject
     *
     * @return String
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get the 'From' recipient
     *
     * @return String
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get the Sent Date
     *
     * @return Date
     */
    public Date getSentDate() {
        return sentDate;
    }

    /**
     * Example method to extract information from an activation email
     *
     * @return String
     */
    public String getUserActivationUrl() {
        Matcher m = USER_ACTIVATION_PATTERN.matcher(getHtmlBody());

        String userActivationURL = EMPTY_STRING;
        if (m.find()) {
            userActivationURL = m.group(1);
            userActivationURL = StringEscapeUtils.unescapeHtml4(userActivationURL);
        }

        return userActivationURL;
    }

}
