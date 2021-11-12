package com.base.site.controllers;

import com.base.site.models.PrivateFood;
import com.base.site.services.PrivateFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class PrivateFoodController {
    Logger log = Logger.getLogger(PrivateFoodController.class.getName());

    @Autowired
    PrivateFoodService privateFoodService;

    @GetMapping("/privateFood")
    public String privateFood(Model model, PrivateFood privateFood, @Param("keyword") String keyword) {
        List<PrivateFood> pFood = privateFoodService.findAllByKeyword(keyword);

        model.addAttribute("pfood", pFood);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping private food is called");

        return "privateFood";
    }
}
