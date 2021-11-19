package com.base.site.security;

import com.base.site.controllers.AccountController;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UserTypeRepo;
import com.base.site.repositories.UsersRepo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DbInitializer implements CommandLineRunner {
    private static final Logger log = Logger.getLogger(DbInitializer.class.getName());

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    UserTypeRepo userTypeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        /*
        log.info("Inside the DbInitializer");

        //----------------------------------USERS---------------------------------
        UserType userType = userTypeRepo.findByType("User_male");
        LocalDate date = LocalDate.parse("1998-02-14");

        List<Users> userList = new ArrayList<>();
        userList.add(new Users("User1", "user1", "user@user.dk", passwordEncoder.encode("user1"),userType,"USER",1,date));
        userList.add(new Users("User2", "user2", "user2@user2.dk", passwordEncoder.encode("user2"),userType,"USER",1,date));
        userList.add(new Users("admin1", "admin1", "admin1@admin1.dk", passwordEncoder.encode("admin1"),userType,"ADMIN",1,date));
        userList.add(new Users("admin2", "admin2", "admin2@admin2.dk", passwordEncoder.encode("admin2"),userType,"",1,date));

        usersRepo.saveAll(userList);
        log.info("Successfully inserted record inside user table!");
        //------------------------------------------------------------------------
        */

    }
}
