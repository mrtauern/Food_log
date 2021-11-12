package com.base.site.security;

import com.base.site.controllers.AccountController;
import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.logging.Logger;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;

    Logger log = Logger.getLogger(UserDetailsServiceImpl.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepo.findUsersByUsername(username);
        //log.info(user.getEmail());
        if(user == null) {
            throw new UsernameNotFoundException("User not found!!");
        }
        return new UserDetailsImpl(user);
    }
}
