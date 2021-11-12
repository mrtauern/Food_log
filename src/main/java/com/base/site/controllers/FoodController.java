package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.FoodRepo;
import com.base.site.services.DailyLogService;
import com.base.site.services.FoodService;
import com.base.site.services.LogTypeService;
import com.base.site.services.UsersService;
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

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class FoodController {
    Logger log = Logger.getLogger(FoodController.class.getName());

    //@Autowired
    //FoodRepo foodRepo;
    @Autowired
    FoodService foodService;
    @Autowired
    DailyLogService dailyLogService;
    @Autowired
    UsersService usersService;
    @Autowired
    LogTypeService logTypeService;


    @GetMapping("/food")
    public String food(Model model, Food food, @Param("keyword") String keyword) {
        List<Food> foodlist = foodService.findAllByKeyword(keyword);
        model.addAttribute("foodlist", foodlist);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping food is called");

            return "food";
    }

    @GetMapping("/createFood")
    public String createFood(Model model) {

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

    @GetMapping("/updateFood/{id}")
    public String updateFood(@PathVariable(value = "id") Long id, Model model) {
            Food food = foodService.findById(id);
            model.addAttribute("food", food);
            log.info("  GetMapping updateFood is called ");

            return "updateFood";
    }

    @GetMapping("/deleteFood/{id}")
    public String deleteFood(@PathVariable(value = "id") Long id, Model model) {
            this.foodService.deleteById(id);
            log.info("  GetMapping deleteFoodbyId is called ");

            return "redirect:/" + "food";
    }

    @GetMapping("/addFoodToDailyLog")
    public String addFoodToDailyLog(Model model,Food food, DailyLog dailyLog, @Param("keyword") String keyword) {
        List<Food> foodlist = foodService.findAllByKeyword(keyword);

        //model.addAttribute("foodlist", dailyLogList);
        model.addAttribute("foodlist", foodlist);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping addFoodToDailyLog is called");

        return "addFoodToDailyLog";
    }

    @GetMapping("/createDailyLog/{id}")
    public String createDailyLog(@PathVariable(value = "id") Long id,Model model,DailyLog dailyLog) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Food foods = foodService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("foods", foods);
        model.addAttribute("logType", logTypeService.findAll());
        log.info("  createDailyLog is called ");

        return "createDailyLog";
    }
    @PostMapping("/saveDailyLog")
    public String saveDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog,Food food, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Food foodId = foodService.findById(food.getId());
        dailyLog.setFkUser(loggedInUser);
        dailyLog.setFood(foodId);

        dailyLogService.save(dailyLog);

        log.info("  PostMapping saveDailyLog is called ");
        return  "redirect:/" + "addFoodToDailyLog";
    }
/*
    @GetMapping("/updateDailyLog/{id}")
    public String updateDailyLog(@PathVariable(value = "id") Long id, Model model) {
        DailyLog dailyLog = dailyLogService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        log.info("  GetMapping updateDailyLog is called ");

        return "updateDailyLog";
    }

 */


}
