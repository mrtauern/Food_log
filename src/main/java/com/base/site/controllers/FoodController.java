package com.base.site.controllers;

import com.base.site.models.Food;
import com.base.site.repositories.FoodRepo;
import com.base.site.security.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.logging.Logger;

@Controller
public class FoodController {
    Logger log = Logger.getLogger(FoodController.class.getName());

    @Autowired
    FoodRepo foodRepo;
    @Autowired
    FoodService foodService;


    @GetMapping("/food")
    public String food( Model model) {
        model.addAttribute("foodlist", foodService.findAll());
        log.info("  get mapping food is called");

            return "food";
    }

    @GetMapping("/createFood")
    public String createStudentForm(Model model) {

            Food food = new Food();
            model.addAttribute("food", food);
            log.info("  createFood is called ");

            return "createFood";
    }

    @PostMapping("/saveFood")
    public String saveFood(@ModelAttribute("food") Food food, Model model) {
            foodService.save(food);
            log.info("  PostMapping saveFood is called ");

            return  "redirect:/" + "food";
    }

    @GetMapping("/showUpdateFood/{id}")
    public String showUpdateFood(@PathVariable(value = "id") Long id, Model model) {
            Food food = foodService.findById(id);
            model.addAttribute("food", food);
            log.info("  GetMapping showUpdateFood is called ");

            return "updateFood";
    }

    @GetMapping("/deleteFood/{id}")
    public String deleteFood(@PathVariable(value = "id") Long id, Model model) {
            this.foodService.deleteById(id);
            log.info("  GetMapping deleteFoodbyId is called ");

            return "redirect:/" + "food";
    }

}
