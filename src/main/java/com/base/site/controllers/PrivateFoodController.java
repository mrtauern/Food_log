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

    @GetMapping("/privateFood")
    public String privateFood(Model model, PrivateFood privateFood, @Param("keyword") String keyword) {
        List<PrivateFood> pfood = privateFoodService.findAllByKeyword(keyword);

        model.addAttribute("pfood", pfood);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping private food is called");

        return "privateFood";
    }

    @GetMapping("/createPrivateFood")
    public String createPrivateFood(Model model) {

        PrivateFood privateFood = new PrivateFood();
        model.addAttribute("privateFood", privateFood);
        log.info("  createPrivateFood is called ");

        return "createPrivateFood";
    }

    @PostMapping("/savePrivateFood")
    public String savePrivateFood(@ModelAttribute("privateFood") PrivateFood privateFood, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        privateFood.setFkUser(loggedInUser);
        privateFoodService.save(privateFood);
        log.info("  PostMapping savePrivateFood is called ");

        return  "redirect:/" + "privateFood";
    }

    @GetMapping("/updatePrivateFood/{id}")
    public String updatePrivateFood(@PathVariable(value = "id") Long id, Model model) {
        PrivateFood privateFood = privateFoodService.findById(id);
        model.addAttribute("privateFood", privateFood);
        log.info("  GetMapping updatePrivateFood is called ");

        return "updatePrivateFood";
    }

    @GetMapping("/deletePrivateFood/{id}")
    public String deletePrivateFood(@PathVariable(value = "id") Long id, Model model) {
        this.privateFoodService.deleteById(id);
        log.info("  GetMapping deletePrivateFood by id is called ");

        return "redirect:/" + "privateFood";
    }

}
