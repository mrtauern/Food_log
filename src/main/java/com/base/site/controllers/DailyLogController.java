package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    FoodService foodService;

    private final String DAILY_LOG = "dailyLog";
    private final String WEIGHT_OPTIONS = "editWeightOptions";
    private final String ADD_WEIGHT = "addCurrentWeight";
    private final String CREATE_WEIGHT = "createCurrentWeight";
    private final String UPDATE_WEIGHT = "updateCurrentWeight";
    private final String WEIGHT_GRAPH = "weightGraph";
    private final String ADD_EXERCISE = "addExercise";
    private final String CREATE_EXERCISE = "createExercise";
    private final String UPDATE_EXERCISE = "updateExerciseInDailyLog";
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
    }

    @GetMapping({"/dailyLog", "/dailyLog/{date}"})

    public String dailyLog(Model model,@Param("keyword") String keyword, @PathVariable(required = false, value = "date") String dateString) throws ParseException {
        log.info("Getmapping called for dailylog for specific date: "+dateString);

        model = dailyLogService.getDailyLogModels(usersService.getLoggedInUser(), dateString, model, keyword);

        return DAILY_LOG;
    }

    @GetMapping("/addCurrentWeight")
    public String addCurrentWeight(Model model, @Param("keyword") String keyword) {
        log.info("  get mapping addCurrentWeight is called");

        List<DailyLog> dailyLog = dailyLogService.findAllByKeyword(keyword);

        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("keyword", keyword);

        return ADD_WEIGHT;
    }

    @GetMapping({"/createCurrentWeight", "/createCurrentWeight/{date}"})
    public String createCurrentWeight( Model model, DailyLog dailyLog,
                                       @PathVariable(required = false, value = "date") String dateString) {
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        model.addAttribute("date", date.toString());
        model.addAttribute("dailyLog", dailyLog);

        log.info("  Get mapping createCurrentWeight is called ");

        model.addAttribute("dailyLog", dailyLog);

        return CREATE_WEIGHT;
    }

    @PostMapping({"/saveCurrentWeight", "/saveCurrentWeight/{date}"})
    public String saveCurrentWeight(@ModelAttribute("dailyLog") DailyLog dailyLog,
                                    @PathVariable(required = false, value = "date") String dateString) {
        log.info("  Post Mapping saveCurrentWeight is called ");

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        dailyLog.setFkLogType(logTypeService.findByType("Weight"));
        dailyLog.setFkUser(usersService.getLoggedInUser());

        dailyLogService.save(dailyLog);

        return  REDIRECT + DAILY_LOG+"/"+ sDatetime;
    }

    @GetMapping({"/updateCurrentWeight/{id}", "/updateCurrentWeight/{id}/{date}"})
    public String updateCurrentWeight(@PathVariable(value = "id") Long id, Model model,
                                      @PathVariable(required = false, value = "date") String dateString) {

        log.info("  GetMapping updateCurrentWeight is called with id: "+id);

        model.addAttribute("date", dateString);
        model.addAttribute("dailyLog", dailyLogService.findById(id));
        log.info("  GetMapping updateCurrentWeight is called ");

        return UPDATE_WEIGHT;
    }
    @GetMapping({"/deleteCurrentWeight/{id}", "/deleteCurrentWeight/{id}/{date}"})
    public String deleteCurrentWeight(@PathVariable(value = "id") Long id,
                                      @PathVariable(required = false, value = "date") String dateString) {

        log.info("  GetMapping deleteCurrentWeight is called ");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        this.dailyLogService.deleteById(id);

        return  REDIRECT + DAILY_LOG+ "/"+date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    }

    @GetMapping("/weightOptions")
    public String weightOptions(Model model) {
        log.info("Getmapping weightOptions called in DailylogController");

        model.addAttribute("user", usersService.getLoggedInUser());
        return WEIGHT_OPTIONS;
    }

    @PostMapping("/saveWeightOption")
    public String saveWeightOptions(@ModelAttribute("user") Users user){
        log.info("Postmappting saveWeightOption called in DailylogController"+user.getKcal_modifier());

        usersService.getLoggedInUser().setKcal_modifier(user.getKcal_modifier());
        usersService.save(usersService.getLoggedInUser());

        return REDIRECT + DAILY_LOG;
    }

    @GetMapping("/weightGraph")
    public String weightGraph(Model model){
        log.info("Weight graph get called");

        Users loggedInUser = usersService.getLoggedInUser();

        List<DailyLog> dailyLogs = dailyLogService.findAll();

        List<Double> weights = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        for (DailyLog dailyLog: dailyLogs) {
            if(dailyLog.getFkUser().getId() == loggedInUser.getId() && dailyLog.getFkLogType().getType().equals("Weight")){
                //X-axe
                dates.add(dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                //Y-axe
                weights.add(dailyLog.getAmount());
            }
        }

        for(String date: dates){
            log.info(date);
        }

        model.addAttribute("pageTitle", "Weight graph");
        model.addAttribute("loggedInUser", usersService.getLoggedInUser());
        model.addAttribute("weights", weights);
        model.addAttribute("dates", dates);
        model.addAttribute("goal", usersService.getLoggedInUser().getGoalWeight());

        return WEIGHT_GRAPH;
    }
 }
