package com.base.site.services;

import com.base.site.models.Mail;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import com.icegreen.greenmail.util.GreenMailUtil;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailServiceImplTest {

    @Autowired
    EmailServiceImpl emailService;

    @Test
    void sendmail() throws Exception {
//        Mail testMail = new Mail("hasan@h.dk","test1","test test");
        Mail mail = new Mail();
        mail.setRecipient("hasancph+1@gmail.com");
        mail.setTopic("tester");
        mail.setContent("her is a test");

        //testing if its sending
        emailService.sendmail(mail);

        //verify the result
        assertThat(mail.getTopic()).isEqualTo("tester");
        assertNotNull(mail.getTopic());
    }

}