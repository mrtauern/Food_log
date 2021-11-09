package com.base.site.services;

import com.base.site.models.Mail;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

@Service("EmailService")
public interface EmailService {
    void sendmail(Mail mail) throws AddressException, MessagingException, IOException;
}
