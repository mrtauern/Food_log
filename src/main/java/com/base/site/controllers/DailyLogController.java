package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.models.ExerciseType;
import com.base.site.models.Users;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.services.DailyLogService;
import com.base.site.services.ExerciseService;

import com.base.site.services.LogTypeService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class DailyLogController {
    Logger log = Logger.getLogger(DailyLogController.class.getName());

    @Autowired
    DailyLogService dailyLogService;
    @Autowired
    ExerciseService exerciseService;
    @Autowired
    UsersService usersService;
    @Autowired
    LogTypeService logTypeService;

    private final String DAILY_LOG = "dailyLog";
    private final String REDIRECT = "redirect:/";

    @RequestMapping({"/dailyLog", "/dailyLog/{date}"})
    public String dailyLog(DailyLog dailyLog, Model model,@Param("keyword") String keyword, @PathVariable(required = false, value = "date") String dateString) {
        log.info("  get mapping DailyLog is called");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        List<DailyLog> list = dailyLogService.findAllByKeyword(keyword);
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        List<DailyLog> logList = dailyLogService.findAll();

        ArrayList<DailyLog> logListUserDate = new ArrayList<DailyLog>();

        for (DailyLog logdate: logList) {
            if(logdate.getDatetime().equals(date) && loggedInUser.getId() == logdate.getFkUser().getId() && logdate.getFood() != null) {
                logListUserDate.add(logdate);
            }
        }

        model.addAttribute("bmr", loggedInUser.getBMR(usersService.getLatestWeight(date).getAmount()));
        model.addAttribute("kcalUsed", dailyLogService.getKcalUsed(date, loggedInUser));
        model.addAttribute("kcalLeft", dailyLogService.getKcalLeft(date, loggedInUser));
        model.addAttribute("weight",usersService.getLatestWeight(date));
        model.addAttribute("list", logListUserDate);
        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("keyword", keyword);
        return DAILY_LOG;
    }



    @GetMapping("/addCurrentWeight")
    public String addCurrentWeight(Model model, @Param("keyword") String keyword) {
        //List<ExerciseType> exerciseTypeList = exerciseTypeService.findAllByKeyword(keyword);
        List<DailyLog> dailyLog = dailyLogService.findAllByKeyword(keyword);

        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping addCurrentWeight is called");

        return "addCurrentWeight";
    }

    @GetMapping("/createCurrentWeight")
    public String createCurrentWeight( Model model, DailyLog dailyLog) {
        //DailyLog dailyLog1 = dailyLogService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        //model.addAttribute("dailyLog1", dailyLog1);
        log.info("  Get mapping createCurrentWeight is called ");

        return "createCurrentWeight";
    }
    @PostMapping("/saveCurrentWeight")
    public String saveCurrentWeight(@ModelAttribute("dailyLog") DailyLog dailyLog, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        log.info("   _______-----Post Mapping saveCurrentWeight is called ");
        dailyLog.setFkLogType(logTypeService.findByType("Weight"));
        dailyLog.setFkUser(loggedInUser);
        log.info("weight"+dailyLog.getAmount());
        dailyLogService.save(dailyLog);

        log.info("  Post Mapping saveCurrentWeight is called ");
        return  "redirect:/" + "dailyLog";

    }

    @GetMapping("/updateCurrentWeight/{id}")
    public String updateCurrentWeight(@PathVariable(value = "id") Long id, Model model) {
        DailyLog dailyLog = dailyLogService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        log.info("  GetMapping updateCurrentWeight is called ");

        return "updateCurrentWeight";
    }

    @GetMapping("/deleteCurrentWeight/{id}")
    public String deleteCurrentWeight(@PathVariable(value = "id") Long id, Model model) {
        this.dailyLogService.deleteById(id);
        log.info("  GetMapping deleteCurrentWeight is called ");

        return "redirect:/" + "dailyLog";
    }

 }
