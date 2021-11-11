package com.base.site.security;

import com.base.site.controllers.AccountController;
import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DbInitializer implements CommandLineRunner {
    private static final Logger log = Logger.getLogger(DbInitializer.class.getName());

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        /*
        log.info("Inside the DbInitializer");
        //usersRepo.deleteAll();
        List<Users> userList = new ArrayList<>();
        userList.add(new Users("User1", "user1", "user@user.dk", passwordEncoder.encode("user1"),1,"USER",1));
        userList.add(new Users("User2", "user2", "user2@user2.dk", passwordEncoder.encode("user2"),1,"USER",1));
        userList.add(new Users("admin1", "admin1", "admin1@admin1.dk", passwordEncoder.encode("admin1"),1,"ADMIN",1));
        userList.add(new Users("admin2", "admin2", "admin2@admin2.dk", passwordEncoder.encode("admin2"),1,"",1));

        usersRepo.saveAll(userList);
        log.info("Successfully inserted record inside user table!");
        */
    }
}
