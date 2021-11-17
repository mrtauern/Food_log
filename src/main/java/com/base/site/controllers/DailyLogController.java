package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.models.ExerciseType;
import com.base.site.models.Users;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.services.DailyLogService;
import com.base.site.services.ExerciseService;

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
    UsersService usersService;

    private final String DAILY_LOG = "dailyLog";
    private final String REDIRECT = "redirect:/";

    @RequestMapping("/dailyLog")
    public String dailyLog(DailyLog dailyLog, Model model,@Param("keyword") String keyword) {
        List<DailyLog> list = dailyLogService.findAllByKeyword(keyword);
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        List<DailyLog> logList = dailyLogService.findAll();
        LocalDate date = LocalDate.now();

        ArrayList<DailyLog> logListUserDate = new ArrayList<DailyLog>();

        for (DailyLog logdate: logList) {
            if(logdate.getDatetime().equals(date) && loggedInUser.getId() == logdate.getFkUser().getId() && logdate.getFood() != null) {
                logListUserDate.add(logdate);
            }
        }

        model.addAttribute("bmr", loggedInUser.getBMR(usersService.getLatestWeight(date)));
        model.addAttribute("kcalUsed", dailyLogService.getKcalUsed(date, loggedInUser));
        model.addAttribute("kcalLeft", dailyLogService.getKcalLeft(date, loggedInUser));
        model.addAttribute("list", logListUserDate);
        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("keyword", keyword);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        log.info("  get mapping DailyLog is called");
        //return REDIRECT + DAILY_LOG + "/" + dateFormat.format(date);
        return DAILY_LOG;
    }

    @GetMapping("dailyLog/{date}")
    public String dailyLog(@PathVariable(value = "date") String date, Model model, @Param("keyword") String keyword) throws ParseException {
        log.info("Getmapping called for dailylog for specific date: "+date);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        List<DailyLog> dailyLogs = dailyLogService.findAll();
        List<DailyLog> dailyLogsView = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date enteredDate = dateFormat.parse(date);

        for (DailyLog dailyLog: dailyLogs) {
            String timeStamp = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if(timeStamp.equals(date) && loggedInUser.getId() == dailyLog.getFkUser().getId()) {
                String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                dailyLog.setSDatetime(sDatetime);
                dailyLogsView.add(dailyLog);
            }
        }

        LocalDate localDate = enteredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date currentDate = new Date();
        LocalDate today = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        model.addAttribute("list", dailyLogsView);
        model.addAttribute("keyword", keyword);


        // +/- Day
        model.addAttribute("tomorrow", localDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("yesterday", localDate.minusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // +/- Week
        model.addAttribute("nextWeek", localDate.plusWeeks(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("previousWeek", localDate.minusWeeks(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        // +/- Month
        model.addAttribute("nextMonth", localDate.plusMonths(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("previousMonth", localDate.minusMonths(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

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
    public String saveExercise(@ModelAttribute("dailyLog") DailyLog dailyLog, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        log.info("   _______-----Post Mapping saveCurrentWeight is called ");

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
