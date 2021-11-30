package com.base.site.controllers;

import com.base.site.models.Food;
import com.base.site.models.PrivateFood;
import com.base.site.models.Users;
import com.base.site.services.PrivateFoodService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class PrivateFoodController {
    Logger log = Logger.getLogger(PrivateFoodController.class.getName());

    @Autowired
    PrivateFoodService privateFoodService;

    @Autowired
    UsersService usersService;

    private final String REDIRECT = "redirect:/";
    private final String PRIVATEFOOD = "privateFood";
    private final String UPDATE_PRIVATEFOOD = "updatePrivateFood";
    private final String CREATE_PRIVATEFOOD = "createPrivateFood";

    @GetMapping("/privateFood")
    public String privateFood(Model model, PrivateFood privateFood, @Param("keyword") String keyword) {
        log.info("  get mapping private food is called");

        model.addAttribute("pfood", privateFoodService.findAllByKeyword(keyword));
        model.addAttribute("keyword", keyword);

        return PRIVATEFOOD;
    }

    @GetMapping("/createPrivateFood")
    public String createPrivateFood(Model model) {
        log.info("  createPrivateFood is called ");

        model.addAttribute("privateFood", new PrivateFood());

        return CREATE_PRIVATEFOOD;
    }

    @PostMapping("/savePrivateFood")
    public String savePrivateFood(@ModelAttribute("privateFood") PrivateFood privateFood) {
        log.info("  PostMapping savePrivateFood is called ");

        privateFood.setFkUser(usersService.getLoggedInUser());
        privateFoodService.save(privateFood);

        return REDIRECT + PRIVATEFOOD;
    }

    @GetMapping("/updatePrivateFood/{id}")
    public String updatePrivateFood(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping updatePrivateFood is called ");

        model.addAttribute("privateFood", privateFoodService.findById(id));

        return UPDATE_PRIVATEFOOD;
    }

    @GetMapping("/deletePrivateFood/{id}")
    public String deletePrivateFood(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping deletePrivateFood by id is called ");

        this.privateFoodService.deleteById(id);

        return REDIRECT + PRIVATEFOOD;
    }

}
