package com.base.site.security;

import com.base.site.controllers.TestController;
import com.base.site.models.Attempts;
import com.base.site.models.Users;
import com.base.site.repositories.AttemptsRepository;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

/*
@Component
public class AuthProvider implements AuthenticationProvider {
    private static final int ATTEMPTS_LIMIT = 3;

    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AttemptsRepository attemptsRepository;
    @Autowired
    private UsersRepo userRepository;

@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();*/


    @Component
    public class AuthProvider implements AuthenticationProvider {
        private static final int ATTEMPTS_LIMIT = 3;
        @Autowired
        private SecurityUserDetailsService userDetailsService;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private AttemptsRepository attemptsRepository;
        @Autowired
        private UsersRepo userRepository;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username = (String)authentication.getPrincipal();
            String password = (String)authentication.getCredentials();
            Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByEmail(username);
            if (userAttempts.isPresent()) {
                Attempts attempts = userAttempts.get();
                attempts.setAttempts(0);
                attemptsRepository.save(attempts);
            }
            return new UsernamePasswordAuthenticationToken(username,password);
        }

        private void processFailedAttempts(String username, Users user) {
            Optional<Attempts>
                    userAttempts = attemptsRepository.findAttemptsByEmail(username);
            if (userAttempts.isEmpty()) {
                Attempts attempts = new Attempts();
                attempts.setEmail(username);
                attempts.setAttempts(1);
                attemptsRepository.save(attempts);
            } else {
                Attempts attempts = userAttempts.get();
                attempts.setAttempts(attempts.getAttempts() + 1);
                attemptsRepository.save(attempts);

                if (attempts.getAttempts() + 1 >
                        ATTEMPTS_LIMIT) {
                    user.setAccountNonLocked(false);
                    userRepository.save(user);
                    throw new LockedException("Too many invalid attempts. Account is locked!!");
                }
            }
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
            //return true;
        }

    }



    /*
    private static final int ATTEMPTS_LIMIT = 3;
    Logger log = Logger.getLogger(AuthProvider.class.getName());
    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AttemptsRepository attemptsRepository;

    @Autowired
    private UsersRepo userRepository;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();

        Optional<Attempts> userAttempts = attemptsRepository.findAttemptsByEmail(email);


        if (userAttempts.isPresent()) {
            Attempts attempts = userAttempts.get();
            attempts.setAttempts(0);
            attemptsRepository.save(attempts);
        }

        log.info("email: "+authentication.getName());

        return authentication;
    }
    private void processFailedAttempts(String email, Users user) {
        Optional<Attempts>
                userAttempts = attemptsRepository.findAttemptsByEmail(email);
        if (userAttempts.isEmpty()) {
            Attempts attempts = new Attempts();
            attempts.setEmail(email);
            attempts.setAttempts(1);
            attemptsRepository.save(attempts);
        } else {
            Attempts attempts = userAttempts.get();
            attempts.setAttempts(attempts.getAttempts() + 1);
            attemptsRepository.save(attempts);

            if (attempts.getAttempts() + 1 >
                    ATTEMPTS_LIMIT) {
                user.setAccountNonLocked(false);
                userRepository.save(user);
                throw new LockedException("Too many invalid attempts. Account is locked!!");
            }
        }
    }
    @Override
    public boolean supports(Class<?> authentication) {

        return true;
    }
}*/
