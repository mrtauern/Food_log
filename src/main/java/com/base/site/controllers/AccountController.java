package com.base.site.controllers;

import com.base.site.models.Users;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class AccountController {
    Logger log = Logger.getLogger(AccountController.class.getName());

    @Autowired
    UsersService usersService;

    public AccountController(){

    }



    @GetMapping("/userList")
    public String userList(Model model){
        log.info("userList called");

        List<Users> users = usersService.findAll();
        model.addAttribute("users", users);

        return "userList";
    }
}
