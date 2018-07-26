package pl.coddlers.mail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// TODO: 26.07.18 I would like this logs to be stored in different file
class FakeMailSender extends AbstractMailSender {
    @Override
    void sendMail(Mail mail) {
        log.debug("SENDING FAKE MAIL" + mail.toString());
    }
}
