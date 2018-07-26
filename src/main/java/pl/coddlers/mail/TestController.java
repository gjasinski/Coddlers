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

    @Autowired
    private MailInitializer mailInitializer;

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
}
