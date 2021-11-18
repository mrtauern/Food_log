package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.models.Users;
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



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    @PostMapping("/selectedDate")
    public String selectedDate(@RequestParam("selectedDate") String sSelectedDate) {
        log.info("Post mapping selectedDate is called " + sSelectedDate);

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = new Date();

        try {
            selectedDate = inputFormat.parse(sSelectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return REDIRECT + DAILY_LOG + "/" + outputFormat.format(selectedDate);
        //return null;
    }

    @GetMapping({"/dailyLog", "/dailyLog/{date}"})
    public String dailyLog(Model model,@Param("keyword") String keyword, @PathVariable(required = false, value = "date") String dateString) throws ParseException {
        log.info("Getmapping called for dailylog for specific date: "+dateString);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString, formatter);

        List<DailyLog> dailyLogs = dailyLogService.findAll();
        List<DailyLog> dailyLogsFoods = new ArrayList<>();
        List<DailyLog> dailyLogsExercises = new ArrayList<>();
        //List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        //Date enteredDate = dateFormat.parse(date);

        for (DailyLog dailyLog: dailyLogs) {
            //String timeStamp = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if(dailyLog.getDatetime().equals(date) && loggedInUser.getId() == dailyLog.getFkUser().getId()) {

                String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                dailyLog.setSDatetime(sDatetime);

                if(dailyLog.getFood() != null) {
                    dailyLogsFoods.add(dailyLog);
                } else if (dailyLog.getFkExercise() != null){
                    dailyLogsExercises.add(dailyLog);
                }
            }
        }

        //LocalDate localDate = enteredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date currentDate = new Date();
        LocalDate today = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("sSelectedDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        model.addAttribute("foods", dailyLogsFoods);
        model.addAttribute("exercises", dailyLogsExercises);
        model.addAttribute("keyword", keyword);


        // +/- Day
        model.addAttribute("tomorrow", date.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("yesterday", date.minusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // +/- Week
        model.addAttribute("nextWeek", date.plusWeeks(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("previousWeek", date.minusWeeks(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // +/- Month
        model.addAttribute("nextMonth", date.plusMonths(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("previousMonth", date.minusMonths(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        model.addAttribute("bmr", loggedInUser.getBMR(usersService.getLatestWeight(date).getAmount()));
        model.addAttribute("kcalUsed", dailyLogService.getKcalUsed(date, loggedInUser));
        model.addAttribute("kcalLeft", dailyLogService.getKcalLeft(date, loggedInUser));
        //model.addAttribute("list", logListUserDate);
        //model.addAttribute("exerciseList", exerciseList);
        //model.addAttribute("keyword", keyword);

        model.addAttribute("weight", usersService.getLatestWeight(date));

        return DAILY_LOG;
    }

    @GetMapping("/addCurrentWeight")
    public String addCurrentWeight(Model model, @Param("keyword") String keyword) {
        List<DailyLog> dailyLog = dailyLogService.findAllByKeyword(keyword);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("keyword", keyword);
        log.info("  get mapping addCurrentWeight is called");

        return "addCurrentWeight";
    }

    @GetMapping("/createCurrentWeight")
    public String createCurrentWeight( Model model, DailyLog dailyLog) {
        model.addAttribute("dailyLog", dailyLog);
        log.info("  Get mapping createCurrentWeight is called ");

        return "createCurrentWeight";
    }
    @PostMapping("/saveCurrentWeight")
    public String saveCurrentWeight(@ModelAttribute("dailyLog") DailyLog dailyLog, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
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
