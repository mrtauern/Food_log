package com.base.site.services;

import com.base.site.models.UserPassResetCode;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service("UPRCService")
public interface UPRCService {
    UserPassResetCode findByUsername(String username);
    void save(UserPassResetCode userPassResetCode);
    void delete(UserPassResetCode userPassResetCode);

    UserPassResetCode generateAndSaveCode(UserPassResetCode resetCode);

    void adminResetUserPassword(long id, HttpSession session) throws MessagingException, IOException;
}
