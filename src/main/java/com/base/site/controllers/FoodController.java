package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.FoodRepo;
import com.base.site.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public String food(Model model , @Param("keyword") String keyword) {
        log.info("  get mapping food is called");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "User list");
        model.addAttribute("selectedPage", "food");
        model.addAttribute("user_name", loggedInUser.getFirstname() + " " + loggedInUser.getLastname());
        model.addAttribute("user_gender", loggedInUser.getUserType().getType());


        return findPaginated(model,1 ,"name", "asc", keyword );
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(Model model, @PathVariable(value = "pageNo")int pageNo,
                                @RequestParam("sortField")String sortField,
                                @RequestParam("sortDir")String sortDir,
                                @Param("keyword") String keyword
                                ){
        int pageSize = 15;

        Page<Food> page = foodService.findPaginated(pageNo,pageSize, sortField, sortDir, keyword);
        List<Food> listFood = page.getContent();
        List<Food> foodlistSearched = foodService.findAllByKeyword(keyword);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalFood", page.getTotalElements());

        model.addAttribute("listFood", listFood);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("foodlistSearched", foodlistSearched);


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

    @GetMapping({"/addFoodToDailyLog", "/addFoodToDailyLog/{date}"})
    public String addFoodToDailyLog(Model model, @Param("keyword") String keyword, @PathVariable(required = false, value = "date") String dateString) {
        log.info("  get mapping addFoodToDailyLog is called");
        List<Food> foodlist = foodService.findAllByKeyword(keyword);
        List<PrivateFood> pfoodlist = privateFoodService.findAllByKeyword(keyword);

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        model.addAttribute("foodlist", foodlist);
        model.addAttribute("date", date.toString());
        model.addAttribute("pfoodlist", pfoodlist);
        model.addAttribute("keyword", keyword);

        return "addFoodToDailyLog";
    }

    @GetMapping({"/createDailyLog/{id}", "/createDailyLog/{id}/{date}"})
    public String createDailyLog(@PathVariable(value = "id") Long id,@PathVariable(required = false, value = "date") String dateString,Model model,DailyLog dailyLog) {
        log.info("  createDailyLog is called ");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);

        Food foods = foodService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("foods", foods);
        model.addAttribute("date", date.toString());
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

    @PostMapping({"/saveDailyLog", "/saveDailyLog/{date}"})
    public String saveDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog,Food food, @PathVariable(required = false, value = "date") String dateString, Model model) {
        log.info("  PostMapping saveDailyLog is called ");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Food foodId = foodService.findById(food.getId());
        dailyLog.setFkUser(loggedInUser);
        dailyLog.setFood(foodId);
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog/"+sDatetime;
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

    @GetMapping({"/updateDailyLog/{id}", "/updateDailyLog/{id}/{date}"})
    public String updateDailyLog(@PathVariable(value = "id") Long id, Model model, @PathVariable(required = false, value = "date") String dateString) {
        log.info("  GetMapping updateDailyLog is called ");
        DailyLog dailyLog = dailyLogService.findById(id);

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString, formatter);

        //LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("date", dateString);
        model.addAttribute("logType", logTypeService.findAll());

        return "updateDailyLog";
    }

    @PostMapping({"/updateDailyLog", "/updateDailyLog/{date}"})
    public String updateDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog, Model model, @PathVariable(required = false, value = "date") String dateString) {
        log.info("  PostMapping updateDailyLog is called ");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString, formatter);
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        dailyLog.setFkUser(loggedInUser);
        dailyLog.setDatetime(date);
        dailyLogService.save(dailyLog);


        return  "redirect:/" + "dailyLog/"+dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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

    @GetMapping({"/deleteDailyLog/{id}", "/deleteDailyLog/{id}/{date}"})
    public String deleteDailyLog(@PathVariable(value = "id") Long id, @PathVariable(required = false, value = "date") String dateString) {
        log.info("  GetMapping deleteDailyLog is called ");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        this.dailyLogService.deleteById(id);

        return "redirect:/" + "dailyLog/"+date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }


}
