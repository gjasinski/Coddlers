package pl.coddlers.mail;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Mail implements IMail {
    // TODO: 26.07.18 here should be abstract class with common impl
    private InternetAddress from;
    private List<InternetAddress> to = new ArrayList<>();
    private List<InternetAddress> bcc = new ArrayList<>();
    private List<InternetAddress> cc = new ArrayList<>();
    private List<DataHandler> attachments = new ArrayList<>();
    private String title;
    private String message;


    public Mail(InternetAddress from, InternetAddress to, String title, String message) {
        this(from, Lists.newArrayList(to), title, message);
    }

    public Mail(InternetAddress from, List<InternetAddress> to, String title, String message) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.message = message;
    }

    public void addCcReceiver(InternetAddress receiver) {
        this.cc.add(receiver);
    }

    public void addCcReceivers(List<InternetAddress> receivers) {
        this.cc.addAll(receivers);
    }


    public void addBccReceiver(InternetAddress receiver) {
        this.bcc.add(receiver);
    }

    public void addBccReceivers(List<InternetAddress> receivers) {
        this.bcc.addAll(receivers);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addAttachment(String file){
        DataSource source = new FileDataSource(file);
        this.attachments.add(new DataHandler(source));
    }

    MimeMessage createMimeMessage(Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(from);
        message.addRecipients(Message.RecipientType.TO, convertListToArray(to));
        message.addRecipients(Message.RecipientType.CC, convertListToArray(cc));
        message.addRecipients(Message.RecipientType.BCC, convertListToArray(bcc));
        message.setSubject(title);
        message.setContent(createContent());
        return message;
    }

    private Multipart createContent() throws MessagingException {
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(createBody());
        for (DataHandler dataHandler : attachments) {
            multipart.addBodyPart(createAttachment(dataHandler));
        }
        return multipart;
    }

    private BodyPart createAttachment(DataHandler dataHandler) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(dataHandler);
        messageBodyPart.setFileName(dataHandler.getName());
        return messageBodyPart;
    }

    private BodyPart createBody() throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");
        return messageBodyPart;
    }

    private InternetAddress[] convertListToArray(List<InternetAddress> list) {
        return list.toArray(new InternetAddress[list.size()]);
    }


}
