package pl.coddlers.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailInitializer {
    private final static String HOST = "localhost";
    private final static String PORT = "25";
    private final static String SSL_ENABLED = "false";

    @Value("${pl.coddlers.mail.mailsender.fake}")
    private boolean fakeMailSender;

    private MailQueue mailQueue;

    public synchronized MailScheduler initialize() {
        if (mailQueue == null) {
            createMailSender();
            Thread mailer = new Thread(mailQueue);
            mailer.start();
        }
        return new MailScheduler(mailQueue);
    }

    private void createMailSender() {
        AbstractMailSender mailSender;
        if (fakeMailSender) {
            mailSender = new FakeMailSender();
        } else {
            mailSender = new MailSender(HOST, PORT, SSL_ENABLED);
        }
        mailQueue = new MailQueue(mailSender);
    }
}
