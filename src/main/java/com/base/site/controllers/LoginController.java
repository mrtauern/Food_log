package com.base.site.controllers;

import com.base.site.models.Mail;
import com.base.site.models.UserPassResetCode;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UPRCRepository;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.EmailService;
import com.base.site.services.UPRCService;
import com.base.site.services.UserTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class LoginController {

    @Autowired
    UsersService usersService;

    @Autowired
    UserTypeService userTypeService;

    @Autowired
    UPRCService uprcService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    Logger log = Logger.getLogger(LoginController.class.getName());

    @GetMapping({"/login","/login/{message}"})
    public String login(Model model, @PathVariable(required = false, value = "message") String message) {
        log.info("login getmapping called... userExists? "+message);
        model.addAttribute("user", new Users());
        if(message != null) {
            if(message.equals("user_created")) {
                model.addAttribute("message", "User have been created...");
            } else if(message.equals("user_exists")) {
                model.addAttribute("message", "User already exists, please try again..");
            }
        }


        return "login";
    }

    @GetMapping("/logout")
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new Users());
        return "add-user";
    }

    @PostMapping("/signup")
    public String addUser(@Valid Users user, BindingResult result, @RequestParam String userTypeString, RedirectAttributes redAt) throws MessagingException, IOException {
        log.info("signup postmapping called in logincontroller...");
        if (result.hasErrors()) {
            //log.info("creating user failed....");
            //log.info("username: "+user.getUsername());
            //log.info("firstname: "+user.getFirstname());
            //log.info("lastname: "+user.getLastname());
            //log.info("usertype: "+userTypeString);
            //log.info(result.toString());

            return "add-user";
        }

        if(usersService.findUsersByUsername(usersService.returnCreatedUser(user).getUsername()) == null){
            user = usersService.setAndSaveNewUser(user, userTypeString);
            Mail mail = new Mail();
            mail.setRecipient(user.getUsername());
            mail.setContent("Welcome to foodlog "+user.getFullName());
            mail.setTopic("Welcome to foodlog");
            emailService.sendmail(mail);

            redAt.addFlashAttribute("showMessage", true);
            redAt.addFlashAttribute("messageType", "success");
            redAt.addFlashAttribute("message", "User is successfully created");

            return "redirect:/login";
            //return "redirect:/login/user_created";
        }

        redAt.addFlashAttribute("showMessage", true);
        redAt.addFlashAttribute("messageType", "error");
        redAt.addFlashAttribute("message", "User already exist");

        return "redirect:/login";
        //return "redirect:/login/user_exists";
    }

    @GetMapping("/password_reset")
    public String passwordReset(Model model) {
        model.addAttribute("reset", new UserPassResetCode());
        return "password-reset";
    }

    @PostMapping("/password_reset")
    public String confirmReset(UserPassResetCode resetCode) throws MessagingException, IOException {
        log.info("username: "+resetCode.getUsername());

        resetCode = uprcService.generateAndSaveCode(resetCode);
        emailService.sendResetPasswordMail(resetCode);
        return "redirect:/password_reset_code";
    }

    @GetMapping("/password_reset_code")
    public String passwordResetCode(Model model) {
        model.addAttribute("code", new UserPassResetCode());
        return "password-reset-code";
    }

    @PostMapping("/password_reset_code")
    public String passwordResetCodeCheck(UserPassResetCode resetCode) throws MessagingException, IOException {
       return usersService.updateUserPassword(resetCode);

    }

}
