package com.base.site.security;

import com.base.site.controllers.TestController;
import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service("SecurityUserDetailsService")
public class SecurityUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;
    private Object UsernameNotFoundException;
    Logger log = Logger.getLogger(TestController.class.getName());
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> optional_user = usersRepo.findUserByEmail(email);
        Users user = optional_user.get();
        log.info("Hhihi" + user.getPassword());
        return user;
    }
    public void createUser(UserDetails user) {
        usersRepo.save((Users) user);
    }
}
