package com.base.site.services;

import com.base.site.models.Mail;
import com.base.site.models.UserPassResetCode;
import com.base.site.repositories.UPRCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@Service("UPRCService")
public class UPRCServiceImpl implements UPRCService{
    Logger log = Logger.getLogger(UPRCServiceImpl.class.getName());

    @Autowired
    UPRCRepository uprcRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    EmailService emailService;

    @Override
    public UserPassResetCode findByUsername(String username) {
        return uprcRepository.findByUsername(username);
    }

    @Override
    public void save(UserPassResetCode userPassResetCode) {
        uprcRepository.save(userPassResetCode);
    }

    @Override
    public void delete(UserPassResetCode userPassResetCode) {
        uprcRepository.delete(userPassResetCode);
    }

    @Override
    public UserPassResetCode generateAndSaveCode(UserPassResetCode resetCode) {
        UserPassResetCode foundResetCode = uprcRepository.findByUsername(resetCode.getUsername());
        if(foundResetCode == null) {
            resetCode.setCode(resetCode.generateCode());
            resetCode.setUsed(false);
            save(resetCode);
        } else {
            foundResetCode.setCode(foundResetCode.generateCode());
            foundResetCode.setUsed(false);
            save(foundResetCode);
            resetCode = foundResetCode;
        }


        return resetCode;
    }

    @Override
    public void adminResetUserPassword(long id, HttpSession session) throws MessagingException, IOException {
        if(usersService.getLoggedInUser(session).getId() != id && usersService.getLoggedInUser(session).getRoles().equals("ADMIN")) {
            UserPassResetCode resetCode = new UserPassResetCode();

            if (usersService.findById(id) != null) {
                resetCode.setUsername(usersService.findById(id).getUsername());
                resetCode.setCode(resetCode.generateCode());
                resetCode.setUsed(false);
                save(resetCode);

                log.info("mail with link and code being sent to user with email: " + resetCode.getUsername());
                Mail mail = new Mail();
                mail.setRecipient(resetCode.getUsername());
                mail.setTopic("Your password on FoodLog.dk has been requested to be reset");
                mail.setContent("To complete the password reset click on the link Http://localhost:8080/password_reset_code and type in the username and code: " + resetCode.getCode());

                emailService.sendmail(mail);
            } else {
                log.info("user not found or code already sent");
            }
        } else {
            log.info("user requesting password reset is not admin");
        }
    }
}
