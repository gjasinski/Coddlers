package pl.coddlers.mail;

import org.junit.Test;
import org.mockito.InOrder;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MailIntegrationTest {
    private static final int NUMBER_OF_THREAD_CREATORS = 10;
    private static final int NUMBER_OF_MAILS_PER_THREAD_CREATOR = 1000;

    @Test
    public void testSchedulingSequenceMail() throws AddressException, InterruptedException {
        //given
        MailSender mailSender = mock(MailSender.class);
        doNothing().when(mailSender).sendMail(any());
        Mail mail1 = new Mail(new InternetAddress("test1@test.pl"), new InternetAddress("test1@test.pl"), "title1", "htmlmessage1");
        Mail mail2 = new Mail(new InternetAddress("test2@test.pl"), new InternetAddress("test3@test.pl"), "title2", "htmlmessage2");
        Mail mail3 = new Mail(new InternetAddress("test3@test.pl"), new InternetAddress("test3@test.pl"), "title3", "htmlmessage3");

        //when
        MailQueue mailQueue = new MailQueue(mailSender);
        mailQueue.scheduleMail(mail1);
        mailQueue.scheduleMail(mail2);
        mailQueue.scheduleMail(mail3);
        Thread t = new Thread(mailQueue);
        t.start();

        //then
        Thread.sleep(200);
        InOrder inOrder = inOrder(mailSender);
        inOrder.verify(mailSender).sendMail(mail1);
        inOrder.verify(mailSender).sendMail(mail2);
        inOrder.verify(mailSender).sendMail(mail3);
    }

    @Test
    public void afterSchedulingInParallel10KMailAllShouldBeScheduled() {
        //given
        MailSender mailSender = mock(MailSender.class);
        doNothing().when(mailSender).sendMail(any());
        MailQueue mailQueue = new MailQueue(mailSender);
        List<Thread> testMailSchedulers = new LinkedList<>();
        for (int i = 0; i < NUMBER_OF_THREAD_CREATORS; i++) {
            testMailSchedulers.add(new Thread(new TestMailScheduler(mailSender, NUMBER_OF_MAILS_PER_THREAD_CREATOR)));
        }

        //when
        Thread mailQueueThread = new Thread(mailQueue);
        mailQueueThread.start();
        testMailSchedulers.forEach(Thread::start);

        //then
        testMailSchedulers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        verify(mailSender, times(NUMBER_OF_MAILS_PER_THREAD_CREATOR * NUMBER_OF_THREAD_CREATORS)).sendMail(any());
    }

    private class TestMailScheduler implements Runnable {
        private MailSender mailSender;
        private int numberOfMails;

        TestMailScheduler(MailSender mailSender, int numberOfMails) {
            this.mailSender = mailSender;
            this.numberOfMails = numberOfMails;
        }

        @Override
        public void run() {
            for (int i = 0; i < numberOfMails; i++) {
                mailSender.sendMail(mailCreator(i));
            }
        }

        private Mail mailCreator(int i) {
            try {
                return new Mail(new InternetAddress("test1" + this.hashCode() + "@test.pl"), new InternetAddress("test1" + this.hashCode() + "@test.pl"), "title1", "htmlmessage1");
            } catch (AddressException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}