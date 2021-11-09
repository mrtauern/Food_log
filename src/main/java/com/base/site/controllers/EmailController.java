package com.base.site.controllers;
import com.base.site.models.Mail;
import com.base.site.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;

//---------------------------------This controller might not be needed anymore------------------------------------------
@RestController
public class EmailController {
    @Autowired
    EmailService emailService;

    @RequestMapping(value = "/sendemail/{address}/{type}")
    public String sendEmail(@PathVariable String address, @PathVariable String type, @RequestParam(defaultValue="nothing") String content) throws AddressException, MessagingException, IOException {
        Mail mail = new Mail();

        switch (type) {
            case "reset_pass":
                // http://localhost:8080/sendemail/email@gmail.com/reset_pass
                mail.setRecipient(address);
                mail.setTopic("Your password on Food Log have been reset");
                mail.setContent("Password have been reset to a random password, your new password is S2ssV2as23!");
            break;
            case "welcome":
                // http://localhost:8080/sendemail/email@gmail.com/welcome
                mail.setRecipient(address);
                mail.setTopic("Welcome to Food Log!");
                mail.setContent("Welcome to Food Log...<br> We hope you enjoy your stay please click the link below to confirm your account.<br> https://test/asd2231gsdfa");
            break;
            case "custom":
                // http://localhost:8080/sendemail/email@gmail.com/custom?content=Hello%20there%20food%20logger!
                mail.setRecipient(address);
                mail.setTopic("Message from Food Log");
                mail.setContent(content);
            break;
            default:
                mail.setRecipient(address);
                mail.setTopic("something went wrong");
                mail.setContent("something went wrong");
            break;
        }

        emailService.sendmail(mail);
        return "Email sent successfully";
    }


}

