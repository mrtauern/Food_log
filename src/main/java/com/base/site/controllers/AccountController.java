package com.base.site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class AccountController {

    public AccountController(){

    }

    Logger log = Logger.getLogger(com.base.site.restControllers.UsersController.class.getName());

    @GetMapping("/userList")
    public String index(Model model){
        log.info("userList called");

        return "userList";
    }
}
