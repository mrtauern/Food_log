package com.base.site.services;

import com.base.site.models.UserPassResetCode;
import com.base.site.repositories.UPRCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UPRCService")
public class UPRCServiceImpl implements UPRCService{
    @Autowired
    UPRCRepository uprcRepository;

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
}
