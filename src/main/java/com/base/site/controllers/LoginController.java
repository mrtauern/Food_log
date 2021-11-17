package com.base.site.controllers;

import com.base.site.models.Mail;
import com.base.site.models.UserPassResetCode;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UPRCRepository;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.EmailService;
import com.base.site.services.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class LoginController {
    @Autowired
    UsersRepo usersRepo;

    @Autowired
    UserTypeService userTypeService;

    @Autowired
    UPRCRepository uprcRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    Logger log = Logger.getLogger(LoginController.class.getName());

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new Users());
        return "add-user";
    }

    @PostMapping("/signup")
    public String addUser(@Valid Users user, BindingResult result, @RequestParam String userType) {
        log.info("signup postmapping called in logincontroller...");
        if (result.hasErrors()) {
            return "add-user";
        }
        Users foundUser = usersRepo.findUsersByUsername(user.getUsername());
        if(foundUser == null){
            UserType userTypeObject = userTypeService.findByType(userType);
            user.setFkUserTypeId(userTypeObject.getId());
            user.setRoles("USER");
            String pass = passwordEncoder.encode(user.getPassword());
            user.setPassword(pass);
            usersRepo.save(user);
        }

        return "redirect:/index";
    }

    @GetMapping("/password_reset")
    public String passwordReset(Model model) {
        model.addAttribute("reset", new UserPassResetCode());
        return "password-reset";
    }

    @PostMapping("/password_reset")
    public String confirmReset(UserPassResetCode resetCode) throws MessagingException, IOException {
        UserPassResetCode foundResetCode = uprcRepository.findByUsername(resetCode.getUsername());
        Users foundUser = usersRepo.findUsersByUsername(resetCode.getUsername());

        if(foundResetCode == null && foundUser != null) {
            resetCode.setCode(resetCode.generateCode());
            resetCode.setUsed(false);
            uprcRepository.save(resetCode);

            log.info("mail with link and code being sent to user with email: "+resetCode.getUsername());
            Mail mail = new Mail();
            mail.setRecipient(resetCode.getUsername());
            mail.setTopic("Your password on Food Log have been requested to be reset");
            mail.setContent("To complete the password reset click the link Http://localhost:8080/password_reset_code and type in the username and code: "+resetCode.getCode());

            emailService.sendmail(mail);

            return "redirect:/password_reset_code";
        } else {
            log.info("user not found or code already sent");
            return "password-reset";
        }
        //return "redirect:/index";
    }

    @GetMapping("/password_reset_code")
    public String passwordResetCode(Model model) {
        model.addAttribute("code", new UserPassResetCode());
        return "password-reset-code";
    }

    @PostMapping("/password_reset_code")
    public String passwordResetCodeCheck(UserPassResetCode resetCode) throws MessagingException, IOException {
        UserPassResetCode foundResetCode = uprcRepository.findByUsername(resetCode.getUsername());
        Users foundUser = usersRepo.findUsersByUsername(resetCode.getUsername());

        if(resetCode.getCode().equals(foundResetCode.getCode()) && foundResetCode != null && foundUser != null && foundResetCode.isUsed() == false) {
            log.info("Username and code checks out, saving new data...");
            foundUser.setPassword(passwordEncoder.encode(resetCode.getPassword()));
            usersRepo.save(foundUser);
            uprcRepository.delete(foundResetCode);
            log.info("Sending email to user informing that password have been changed...");
            Mail mail = new Mail();

            mail.setRecipient(resetCode.getUsername());
            mail.setTopic("Your password on Food Log have been changed!");
            mail.setContent("If you didnt request this change please contact up emidially on email: foodlog.dk@gmail.com");

            emailService.sendmail(mail);
            return "login";
        }

        return "predirect:/assword-reset";
    }

}
