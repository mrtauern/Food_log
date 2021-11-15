package com.base.site.controllers;

import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.services.UserTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AccountController {

    public AccountController(){

    }

    Logger log = Logger.getLogger(AccountController.class.getName());

    private final String USER_LIST = "userList";
    private final String CREATE_USER = "createUser";
    private final String EDIT_USER = "editUser";

    private final String REDIRECT = "redirect:/";

    @Autowired
    UsersService usersService;

    @Autowired
    UserTypeService userTypeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailController emailController;

    @GetMapping("/userList")
    public String userList(Model model){
        log.info("userList called");

        model.addAttribute("users", usersService.findAll());
        model.addAttribute("pageTitle", "User list");

        return USER_LIST;
    }

    @GetMapping("/createUser")
    public String createUser(Model model){
        log.info("createUser get called");

        model.addAttribute("users", new Users());
        //model.addAttribute("userTypes", userTypeService.findAll());
        model.addAttribute("pageTitle", "Create user");

        return CREATE_USER;
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute("users") Users user){
        log.info("createUser post called");

        String genPass = usersService.generatePassword();
        String encPass = passwordEncoder.encode(genPass);
        user.setPassword(encPass);

        Date birthday = new Date();

        String sBirthday = user.getSBirthday();

        log.info("New birthday: " + sBirthday);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday = dateFormat.parse(sBirthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timestamp ts=new Timestamp(birthday.getTime());
        user.setBirthday(ts);

        String emailMessage = "We have created a new user for you.\n\n";
        emailMessage += "Your new password is: " + genPass;

        try {
            usersService.save(user);
            emailController.sendEmail(user.getUsername(), "custom", emailMessage);
        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        return REDIRECT + USER_LIST;
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable(value = "id") Long id, Model model){
        log.info("editUser get called");

        Users user = usersService.findById(id);

        String sBirthday = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        user.setSBirthday(sBirthday);

        model.addAttribute("users", user);
        //model.addAttribute("userTypes", userTypeService.findAll());
        model.addAttribute("pageTitle", "Edit user");

        return EDIT_USER;
    }

    @PostMapping("/editUser")
    public String editUser(@ModelAttribute("users") Users user){
        log.info("editUser post called");

        Date birthday = new Date();

        String sBirthday = user.getSBirthday();

        log.info("New birthday: " + sBirthday);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday = dateFormat.parse(sBirthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Timestamp ts=new Timestamp(birthday.getTime());
        user.setBirthday(ts);

        try {
            usersService.save(user);
        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        return REDIRECT + USER_LIST;
    }
}
