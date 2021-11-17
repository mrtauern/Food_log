package com.base.site.controllers;

import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class UserController {

    Logger log = Logger.getLogger(UserController.class.getName());

    private final String INDEX = "index";
    private final String UPDATE_USER = "update-user";
    private final String EDIT_USER = "editUser";
    private final String DELETE_USER_CONFIRM = "delete_user_confirm";
    private final String REDIRECT = "redirect:/";

    @Autowired
    UsersRepo usersRepo;

    @GetMapping("/index")
    public String showUserList(Model model) {
        log.info("Usercontroller /index getmapping called...");
        model.addAttribute("users", usersRepo.findAll());
        return INDEX;
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        log.info("Usercontroller /edit/id getmapping called... id: "+id);
        Users user = usersRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return UPDATE_USER;
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Users user,
                             BindingResult result, Model model) {
        log.info("Usercontroller /update/id postmapping called... id: "+id);
        if (result.hasErrors()) {
            user.setId(id);
            return UPDATE_USER;
        }

        usersRepo.save(user);
        return REDIRECT+INDEX;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        log.info("Usercontroller /delete/id getmapping called... id: "+id);
        Users user = usersRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        usersRepo.delete(user);
        return REDIRECT+INDEX;
    }
}
