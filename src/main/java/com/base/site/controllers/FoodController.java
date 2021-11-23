package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.FoodRepo;
import com.base.site.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
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
public class FoodController {
    Logger log = Logger.getLogger(FoodController.class.getName());

    @Autowired
    FoodService foodService;
    @Autowired
    PrivateFoodService privateFoodService;
    @Autowired
    DailyLogService dailyLogService;
    @Autowired
    UsersService usersService;
    @Autowired
    LogTypeService logTypeService;


    @GetMapping("/food")
    public String food(Model model, Food food, @Param("keyword") String keyword) {
        log.info("  get mapping food is called");

        List<Food> foodlist = foodService.findAllByKeyword(keyword);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("foodlist", foodlist);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "User list");
        model.addAttribute("selectedPage", "food");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());

            return "food";
    }

    @GetMapping("/createFood")
    public String createFood(Model model) {
        log.info("  createFood is called ");

            Food food = new Food();
            model.addAttribute("food", food);

            return "createFood";
    }

    @PostMapping("/saveFood")
    public String saveFood(@ModelAttribute("food") Food food, Model model) {
        log.info("  PostMapping saveFood is called ");
            foodService.save(food);

            return  "redirect:/" + "food";
    }

    @GetMapping("/updateFood/{id}")
    public String updateFood(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping updateFood is called ");
            Food food = foodService.findById(id);
            model.addAttribute("food", food);

            return "updateFood";
    }

    @GetMapping("/deleteFood/{id}")
    public String deleteFood(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping deleteFoodbyId is called ");
            this.foodService.deleteById(id);

            return "redirect:/" + "food";
    }

    @GetMapping("/addFoodToDailyLog")
    public String addFoodToDailyLog(Model model,Food food, DailyLog dailyLog, PrivateFood privateFood, @Param("keyword") String keyword) {
        log.info("  get mapping addFoodToDailyLog is called");
        List<Food> foodlist = foodService.findAllByKeyword(keyword);
        List<PrivateFood> pfoodlist = privateFoodService.findAllByKeyword(keyword);

        model.addAttribute("foodlist", foodlist);
        model.addAttribute("pfoodlist", pfoodlist);
        model.addAttribute("keyword", keyword);


        return "addFoodToDailyLog";
    }

    @GetMapping("/createDailyLog/{id}")
    public String createDailyLog(@PathVariable(value = "id") Long id,Model model,DailyLog dailyLog) {
        log.info("  createDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Food foods = foodService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("foods", foods);
        model.addAttribute("logType", logTypeService.findAll());

        return "createDailyLog";
    }

    @GetMapping("/createDailyLogPfood/{id}")
    public String createDailyLogPfood(@PathVariable(value = "id") Long id,Model model,DailyLog dailyLog) {
        log.info("  createDailyLogPfood is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        PrivateFood pfoods = privateFoodService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("foods", pfoods);
        model.addAttribute("logType", logTypeService.findAll());

        return "createDailyLogPfood";
    }
    @PostMapping("/saveDailyLog")
    public String saveDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog,Food food, Model model) {
        log.info("  PostMapping saveDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Food foodId = foodService.findById(food.getId());
        dailyLog.setFkUser(loggedInUser);
        dailyLog.setFood(foodId);

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog";
    }

    @PostMapping("/saveDailyLogPfood")
    public String saveDailyLogPfood(@ModelAttribute("dailyLog") DailyLog dailyLog,PrivateFood privateFood, Model model) {
        log.info("  PostMapping saveDailyLogPfood is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        PrivateFood pfoodId = privateFoodService.findById(privateFood.getId());
        dailyLog.setFkUser(loggedInUser);
        dailyLog.setPrivateFood(pfoodId);

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog";
    }

    @GetMapping("/updateDailyLog/{id}")
    public String updateDailyLog(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping updateDailyLog is called ");
        DailyLog dailyLog = dailyLogService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("logType", logTypeService.findAll());

        return "updateDailyLog";
    }

    @PostMapping("/updateDailyLog")
    public String updateDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog, Model model) {
        log.info("  PostMapping updateDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        dailyLog.setFkUser(loggedInUser);

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog";
    }

    @GetMapping("/updateDailyLogPfood/{id}")
    public String updateDailyLogPfood(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping updateDailyLog is called ");
        DailyLog dailyLog = dailyLogService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("logType", logTypeService.findAll());

        return "updateDailyLogPfood";
    }

    @PostMapping("/updateDailyLogPfood")
    public String updateDailyLogPfood(@ModelAttribute("dailyLog") DailyLog dailyLog, Model model) {
        log.info("  PostMapping updateDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        dailyLog.setFkUser(loggedInUser);

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog";
    }

    @GetMapping("/deleteDailyLog/{id}")
    public String deleteDailyLog(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping deleteDailyLog is called ");
        this.dailyLogService.deleteById(id);

        return "redirect:/" + "dailyLog";
    }


}
