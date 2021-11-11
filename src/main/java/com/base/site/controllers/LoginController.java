package com.base.site.controllers;

import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class LoginController {
    @Autowired
    UsersRepo usersRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public String addUser(@Valid Users user, BindingResult result) {
        if (result.hasErrors()) {
            return "add-user";
        }
        Users test = usersRepo.findUsersByUsername(user.getUsername());
        if(test == null){
            user.setFkUserTypeId(1);
            user.setRoles("USER");
            String pass = passwordEncoder.encode(user.getPassword());
            user.setPassword(pass);
            usersRepo.save(user);
        }

        return "redirect:/index";
    }

    /*@GetMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("user", new Users());
        return "login";
    }*/

}
