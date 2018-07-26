package pl.coddlers;


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;

public class Mail {

/*

    public static class EmailUtil {

        */
/**
         * Utility method to send simple HTML email
         * @param session
         * @param toEmail
         * @param subject
         * @param body
         *//*

        public void sendEmail(Session session, String toEmail, String subject, String body){
            try
            {
                MimeMessage msg = new MimeMessage(session);
                //set message headers
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
                msg.addHeader("format", "flowed");
                msg.addHeader("Content-Transfer-Encoding", "8bit");

                msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

                msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

                msg.setSubject(subject, "UTF-8");

                msg.setText(body, "UTF-8");

                msg.setSentDate(new Date());

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
                System.out.println("Message is ready");
                Transport.send(msg);

                System.out.println("EMail Sent Successfully!!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

*/

     public static void main(String [] args) {
        // Recipient's email ID needs to be mentioned.
        String to = "gjasinski@hotmail.com";

        // Sender's email ID needs to be mentioned
        String from = "test@coddlers.pl";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        //properties.setProperty("mail.smtp.port", "9000");
        properties.setProperty("mail.smtp.ssl.enable", "false");

        // Get the default Session object.
        Session session = Session.getInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
/*    public static void main(String[] args) {

        System.out.println("SimpleEmail Start");

        String smtpHostServer = "smtp.example.com";
        String emailID = "email_me@example.com";

        Properties props = System.getProperties();

        props.put("mail.smtp.host", smtpHostServer);

        Session session = Session.getInstance(props, null);

        (new EmailUtil()).sendEmail(session, emailID,"SimpleEmail Testing Subject", "SimpleEmail Testing Body");
    }*/
}