package com.base.site.controllers;

import com.base.site.models.Food;
import com.base.site.repositories.FoodRepo;
import com.base.site.security.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class FoodController {
    Logger log = Logger.getLogger(FoodController.class.getName());

    @Autowired
    FoodRepo foodRepo;
    //@Qualifier("FoodService")
    @Autowired
    FoodService foodService;


    @GetMapping("/food")
    public String food( Model model) {
        model.addAttribute("foodlist", foodService.getAllFood());
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
            foodService.saveFood(food);
            log.info("  PostMapping saveFood is called ");

            return  "redirect:/" + "food";
    }

    @GetMapping("/showUpdateFood/{id}")
    public String showUpdateFood(@PathVariable(value = "id") int id, Model model) {
            Food food = foodService.getFoodById(id);
            model.addAttribute("food", food);
            log.info("  GetMapping showUpdateFood is called ");

            return "updateFood";
    }

    @GetMapping("/deleteFood/{id}")
    public String deleteFood(@PathVariable(value = "id") int id, Model model) {
            this.foodService.deleteFoodById(id);
            log.info("  GetMapping deleteFoodbyId is called ");

            return "redirect:/" + "food";
    }



/*
    @GetMapping("/foodlist")
    public String classes( Model model) {
            model.addAttribute("foodlist", foodService.getAllFood());
            log.info("  get mapping food-list is called");
            return "food";
        }


    @GetMapping("/addfood")
    public String showAddFood(Food food) {
        log.info("Get Addfod is called ");

        return "createFood";
    }

    @PostMapping("/addfood")
    public String addFood(@Valid Food food, BindingResult result, Model model) {
        log.info("Post Addfood is called ");

        if (result.hasErrors()) {
            log.info("Post Addfood had error, therfore retried ");

            return "createFood";
        }

        //foodRepo.save(food);
        //foodServiceImpl.save(food);
        log.info("Post Addfood has saved ");

        return "redirect:/food-list";
    }
*/
    /*
    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/index";
    }
    */


}
