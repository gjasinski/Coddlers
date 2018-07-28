package pl.coddlers.mail;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

// TODO: 26.07.18 I would like this module logs to be stored in different file
@Slf4j
class MailQueue implements Runnable {
    private final AbstractMailSender mailSender;
    private final BlockingDeque<Mail> queue = new LinkedBlockingDeque<>();

    MailQueue(AbstractMailSender mailSender) {
        this.mailSender = mailSender;
    }

    boolean scheduleMail(Mail mail) {
        return this.queue.add(mail);
    }

    String queueStateDebug(){
        return "Queue size: " + queue.size() + ", queue remaining capacity: " + queue.remainingCapacity();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Mail mail = queue.take();
                mailSender.sendMail(mail);
            } catch (InterruptedException e) {
                log.error("Error while retrieving mail from queue " + e.getMessage());
            }
        }
    }
}
