package pl.coddlers.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final MailInitializer mailInitializer;

    @Autowired
    public TestController(MailInitializer mailInitializer) {
        this.mailInitializer = mailInitializer;
    }

    @GetMapping("/send")
    public String send(@RequestParam("email") String email) {
        MailQueue initialize = mailInitializer.initialize();
        try {
            for (int i = 0; i < 100; i++) {
                Mail message = new Mail(new InternetAddress("admin@coddlers.pl"), new InternetAddress(email), "title" + i, "message");
                initialize.scheduleMail(message);
            }
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @GetMapping("/sendMore")
    public String sendMore(@RequestParam("email") String email, @RequestParam("cc") String cc, @RequestParam("bcc") String bcc) {
        MailQueue initialize = mailInitializer.initialize();
        try {
            for (int i = 0; i < 100; i++) {
                Mail message = new Mail(new InternetAddress("admin@coddlers.pl"), new InternetAddress(email), "title" + i, "message");
                message.addBccReceiver(new InternetAddress(bcc));
                message.addCcReceiver(new InternetAddress(cc));
                message.setTitle("with html " + i);
                message.setMessage("<html>\n" +
                        "  <body>\n" +
                        "    <h1>HelloWorld Tutorial</h1>\n" +
                        "  </body>\n" +
                        "</html>");
                message.addAttachment("/home/develop/start_application.sh");
                message.addAttachment("/home/develop/manually_start_application.sh");
                initialize.scheduleMail(message);
            }
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return "OK";
    }
}
