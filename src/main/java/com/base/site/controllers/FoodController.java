package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import com.base.site.models.FoodDailylog;
import com.base.site.repositories.FoodRepo;
import com.base.site.services.DailyLogService;
import com.base.site.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    FoodRepo foodRepo;
    @Autowired
    FoodService foodService;
    @Autowired
    DailyLogService dailyLogService;


    @GetMapping("/food")
    public String food(Model model, Food food, @Param("keyword") String keyword) {
        List<Food> foodlist = foodService.findAllByKeyword(keyword);
        model.addAttribute("foodlist", foodlist);
        model.addAttribute("keyword", keyword);

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


    @GetMapping("/addFoodToDailyLog/{id}")
    public String addFoodToDailyLog(@PathVariable(value = "id") long id, Model model) {
        log.info("addFoodToDailyLog getmapping called with id=" + id);

        DailyLog dailyLog = dailyLogService.findById((int) id);
        List<Food> foodInDailyLog = dailyLog.getFood();

        List<Food> foodNotInDailyLog = foodService.findAllNotInList(dailyLog);

        for (Food food: foodInDailyLog) {
            log.info("Food: "+food.getName());
        }
         dailyLogService.save()
        FoodDailylog newFoodDailylog = new FoodDailylog();

        model.addAttribute("foodlist", foodService.findAll());
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("dailyLogId", "" + id);
        model.addAttribute("foodInDailyLog", foodInDailyLog);
        model.addAttribute("foodNotInDailyLog", foodNotInDailyLog);
        model.addAttribute("newFoodDailylog", newFoodDailylog);

        return "addFoodToDailyLog";

    }

    @PostMapping("/addFoodToDailyLog")
    public String addFoodToDailyLog(@ModelAttribute("foodDailyLog") FoodDailylog newFoodDailyLog) {
        log.info("saving foodDailyLog DailyLogId=" + newFoodDailyLog.getFoodIdFk() + " dailyLogId=" + newFoodDailyLog.getDailylogIdFk());
            DailyLog newDailyLog = dailyLogService.findById(newFoodDailyLog.getDailylogIdFk());
            Food test = foodService.findById((long) newFoodDailyLog.getFoodIdFk());

            newDailyLog.getFood().add(test);

            dailyLogService.save(newDailyLog);

            return "redirect:/" + "food" + newFoodDailyLog.getDailylogIdFk();
    }



}
