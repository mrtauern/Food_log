package com.base.site.services;

import com.base.site.controllers.EmailController;
import com.base.site.models.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

@Service("EmailService")
public class EmailServiceImpl implements EmailService {
    Logger log = Logger.getLogger(EmailServiceImpl.class.getName());
    @Value("${emailProperties.email}")
    private String userName;

    @Value("${emailProperties.pass}")
    private String pass;

    public void sendmail(Mail mail) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, pass);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userName, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getRecipient()));
        msg.setSubject(mail.getTopic());
        msg.setContent("test email", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mail.getContent(), "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        msg.setContent(multipart);
        Transport.send(msg);
    }
}
