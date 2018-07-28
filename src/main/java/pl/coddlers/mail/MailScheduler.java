package pl.coddlers.mail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailScheduler {
    private final MailQueue mailQueue;

    public MailScheduler(MailQueue mailQueue) {
        this.mailQueue = mailQueue;
    }

    public boolean scheduleMail(Mail mail) {
        boolean result = mailQueue.scheduleMail(mail);
        if(!result){
            log.error("Couldn't schedule mail " + mailQueue.queueStateDebug());
        }
        return result;
    }

}
