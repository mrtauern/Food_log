package com.base.site.services;

import com.base.site.models.UserPassResetCode;
import org.springframework.stereotype.Service;

@Service("UPRCService")
public interface UPRCService {
    UserPassResetCode findByUsername(String username);
    void save(UserPassResetCode userPassResetCode);
    void delete(UserPassResetCode userPassResetCode);

    UserPassResetCode generateAndSaveCode(UserPassResetCode resetCode);
}
