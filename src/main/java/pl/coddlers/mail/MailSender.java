package pl.coddlers.mail;


import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

// TODO: 26.07.18 I would like this module logs to be stored in different file
@Slf4j
public class MailSender extends AbstractMailSender {
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
    private final Session session;

    MailSender(String smtpHost, String smtpPort, String smtpSsl) {
        Properties properties = System.getProperties();
        properties.setProperty(MAIL_SMTP_HOST, smtpHost);
        properties.setProperty(MAIL_SMTP_PORT, smtpPort);
        properties.setProperty(MAIL_SMTP_SSL_ENABLE, smtpSsl);
        session = Session.getInstance(properties);
    }

    @Override
    void sendMail(Mail mail) {
        try {
            MimeMessage mimeMessage = mail.createMimeMessage(session);
            Transport.send(mimeMessage);
        } catch (MessagingException mex) {
            log.error("Couldn't send e-mail cause: " + mex.getCause().getMessage());
        }
    }
}