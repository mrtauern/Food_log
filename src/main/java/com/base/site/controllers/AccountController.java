package com.base.site.controllers;

import com.base.site.models.Mail;
import com.base.site.models.UserPassResetCode;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UPRCRepository;
import com.base.site.services.EmailService;
import com.base.site.services.UserTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AccountController {

    public AccountController(){

    }

    Logger log = Logger.getLogger(AccountController.class.getName());

    private final String USER_LIST = "userList";
    private final String USER_INFO = "userInfo";
    private final String CREATE_USER = "createUser";
    private final String EDIT_USER = "editUser";
    private final String EDIT_USER_PROFILE = "editUserProfile";
    private final String DELETE_USER_CONFIRM = "delete_user_confirm";
    private final String DASHBOARD = "dashboard";
    private final String REDIRECT = "redirect:/";

    @Autowired
    UsersService usersService;

    @Autowired
    UserTypeService userTypeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailController emailController;

    @Autowired
    UPRCRepository uprcRepository;

    @Autowired
    EmailService emailService;

    @GetMapping("/userList")
    public String userList(Model model){
        log.info("userList called");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("users", usersService.findAll());
        model.addAttribute("pageTitle", "User list");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

        return USER_LIST;
    }

    @GetMapping("/createUser")
    public String createUser(Model model){
        log.info("createUser get called");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("users", new Users());
        //model.addAttribute("userTypes", userTypeService.findAll());
        model.addAttribute("pageTitle", "Create user");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

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
        //created by Niklas to fit with change to LocalDate in users
        LocalDate bday = Instant.ofEpochMilli(birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        user.setBirthday(bday);

        String emailMessage = "We have created a new user for you.\n\n";
        emailMessage += "Your new password is: " + genPass;

        try {
            //niklas... temporary till users is correctly mapped
            user.setUserType(userTypeService.findById((long)4));

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        Users user = usersService.findById(id);

        //String sBirthday = new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday());
        //created by Niklas to fit with change to LocalDate in users
        String sBirthday = user.getBirthday().toString();
        log.info("sBirthday: "+sBirthday);

        user.setSBirthday(sBirthday);

        model.addAttribute("users", user);
        //model.addAttribute("userTypes", userTypeService.findAll());
        model.addAttribute("pageTitle", "Edit user");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

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
        //created by Niklas to fit with change to LocalDate in users
        LocalDate bday = Instant.ofEpochMilli(birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        user.setBirthday(bday);

        try {
            //niklas... temporary till users is correctly mapped
            user.setUserType(userTypeService.findById((long)4));
            Users userData = usersService.findById(user.getId());
            user.setPassword(userData.getPassword());
            user.setRegisterDate(userData.getRegisterDate());
            user.setKcal_modifier(userData.getKcal_modifier());
            usersService.save(user);
        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        return REDIRECT + USER_LIST;
    }
  
    @GetMapping("/password_reset_user/{id}")
    public String passwordResetUser(@PathVariable(value = "id") long id, Model model) throws MessagingException, IOException {
        //UserPassResetCode foundResetCode = uprcRepository.findByUsername(resetCode.getUsername());
        Users foundUser = usersService.findById(id);
        UserPassResetCode resetCode = new UserPassResetCode();

        if (foundUser != null) {
            resetCode.setUsername(foundUser.getUsername());
            resetCode.setCode(resetCode.generateCode());
            resetCode.setUsed(false);
            uprcRepository.save(resetCode);

            log.info("mail with link and code being sent to user with email: " + resetCode.getUsername());
            Mail mail = new Mail();
            mail.setRecipient(resetCode.getUsername());
            mail.setTopic("Your password on Food Log have been requested to be reset");
            mail.setContent("To complete the password reset click the link Http://localhost:8080/password_reset_code and type in the username and code: " + resetCode.getCode());

            emailService.sendmail(mail);

            return REDIRECT + USER_LIST;
        } else {
            log.info("user not found or code already sent");
            return REDIRECT + USER_LIST;
        }
    }
  
    @GetMapping("/delete_user_confirm/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        log.info("delete_user_confirm called userId: "+id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        Users user = usersService.findById(id);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Delete user");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

        return DELETE_USER_CONFIRM;
    }

    @PostMapping("/delete_user_confirm/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        log.info("delete_user_confirm confirmed... deleting user with  userId: "+id);
        usersService.deleteById(id);
        return REDIRECT + USER_LIST;
    }

    @GetMapping("/dashboard")
    public String admin_dashboard(Model model) {
        log.info("dashboard getmapping called...");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("pageTitle", "User list");
        model.addAttribute("selectedPage", "dashboard");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

        return DASHBOARD;
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model){
        log.info("userInfo called");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("users", usersService.findAll());
        model.addAttribute("pageTitle", "User Personal Info");
        model.addAttribute("loggedIn", loggedInUser);
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

        return USER_INFO;
    }

    @GetMapping("/editUserProfile/{id}")
    public String editUserProfile(@PathVariable( value ="id") Long id, Model model) {
        log.info("editUserProfile get called");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        Users user = usersService.findById(id);

        String sBirthday = user.getBirthday().toString();
        log.info("sBirthday: "+sBirthday);

        user.setSBirthday(sBirthday);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Edit user Profile");
        model.addAttribute("selectedPage", "user");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

        return EDIT_USER_PROFILE;
    }
    @PostMapping("/editUserProfile")
    public String editUserProfile(@ModelAttribute("users") Users user){
        log.info("editUserProfile post called");

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
        LocalDate bday = Instant.ofEpochMilli(birthday.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        user.setBirthday(bday);

        try {
            user.setUserType(userTypeService.findById((long)4));
            Users userData = usersService.findById(user.getId());
            user.setPassword(userData.getPassword());
            user.setRegisterDate(userData.getRegisterDate());
            user.setKcal_modifier(userData.getKcal_modifier());
            user.setRoles(userData.getRoles());
            usersService.save(user);
        } catch (Exception e){
            log.info("Something went wrong with crating an user");
            log.info(e.toString());
        }

        return REDIRECT + USER_INFO;
    }
}
