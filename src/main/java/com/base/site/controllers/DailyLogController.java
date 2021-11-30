package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.models.Food;
import com.base.site.models.Users;
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
        //return null;
    }

    @GetMapping({"/dailyLog", "/dailyLog/{date}"})

    public String dailyLog(Model model,@Param("keyword") String keyword, @PathVariable(required = false, value = "date") String dateString) throws ParseException {
        log.info("Getmapping called for dailylog for specific date: "+dateString);

        DailyLog weightLog = new DailyLog();

        Food nutrition = new Food("nutrition",0.0,0.0,0.0,0.0,0.0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString, formatter);

        List<DailyLog> dailyLogs = dailyLogService.findAll();
        List<DailyLog> dailyLogsFoods = new ArrayList<>();
        List<DailyLog> dailyLogsBreakfast = new ArrayList<>();
        List<DailyLog> dailyLogsLunch = new ArrayList<>();
        List<DailyLog> dailyLogsDinner = new ArrayList<>();
        List<DailyLog> dailyLogsMiscellaneous = new ArrayList<>();
        List<DailyLog> dailyLogsPrivateFoods = new ArrayList<>();
        List<DailyLog> dailyLogsExercises = new ArrayList<>();

        for (DailyLog dailyLog: dailyLogs) {
            if(dailyLog.getDatetime().equals(date) && usersService.getLoggedInUser().getId() == dailyLog.getFkUser().getId()) {

                String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                dailyLog.setSDatetime(sDatetime);

                if(dailyLog.getFood() != null || dailyLog.getPrivateFood() != null) {
                    dailyLogsFoods.add(dailyLog);
                    String logType = dailyLog.getFkLogType().getType();

                    switch (logType){
                        case "Breakfast":
                            dailyLogsBreakfast.add(dailyLog);
                            nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog);
                            break;
                        case "Lunch":
                            dailyLogsLunch.add(dailyLog);
                            nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog);
                            break;
                        case "Dinner":
                            dailyLogsDinner.add(dailyLog);
                            nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog);
                            break;
                        case "Miscellaneous":
                            dailyLogsMiscellaneous.add(dailyLog);
                            nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog);
                            break;
                        case "Weight":
                            weightLog = dailyLog;
                            break;
                        default:
                            log.info("UPS... Something went wrong!");
                    }

                }else if (dailyLog.getFkExercise() != null){
                    dailyLogsExercises.add(dailyLog);
                }else if(dailyLog.getFkLogType().getType().equals("Weight")) {
                    weightLog = dailyLog;
                }
            }
        }

        Date currentDate = new Date();
        LocalDate today = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("sSelectedDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        model.addAttribute("foods", dailyLogsFoods);
        model.addAttribute("breakfasts", dailyLogsBreakfast);
        model.addAttribute("lunches", dailyLogsLunch);
        model.addAttribute("dinners", dailyLogsDinner);
        model.addAttribute("miscellaneous", dailyLogsMiscellaneous);
        //model.addAttribute("pfoods", dailyLogsPrivateFoods);
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

        model.addAttribute("bmr", usersService.getLoggedInUser().getBMR(usersService.getLatestWeight(date).getAmount()));
        model.addAttribute("kcalUsed", dailyLogService.getKcalUsed(date, usersService.getLoggedInUser()));
        model.addAttribute("kcalLeft", dailyLogService.getKcalLeft(date, usersService.getLoggedInUser()));
        model.addAttribute("nutrition", nutrition);
        log.info("nutrition ------ fat: "+nutrition.getFat()+" carbs: "+nutrition.getCarbohydrates()+" protein: "+nutrition.getProtein());

        model.addAttribute("weight", weightLog);
        //model.addAttribute("date", dateString);

        model.addAttribute("selectedPage", "dailyLog");
        model.addAttribute("user_name", usersService.getLoggedInUser().getFirstname() + " " + usersService.getLoggedInUser().getLastname());
        model.addAttribute("user_gender", usersService.getLoggedInUser().getUserType().getType());

        return DAILY_LOG;
    }

    @GetMapping("/addCurrentWeight")
    public String addCurrentWeight(Model model, @Param("keyword") String keyword) {
        List<DailyLog> dailyLog = dailyLogService.findAllByKeyword(keyword);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("keyword", keyword);
        log.info("  get mapping addCurrentWeight is called");

        return ADD_WEIGHT;
    }


    @GetMapping({"/createCurrentWeight", "/createCurrentWeight/{date}"})
    public String createCurrentWeight( Model model, DailyLog dailyLog,
                                       @PathVariable(required = false, value = "date") String dateString) {
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        model.addAttribute("date", date.toString());
        model.addAttribute("dailyLog", dailyLog);
        log.info("  Get mapping createCurrentWeight is called ");

        return CREATE_WEIGHT;
    }
    @PostMapping({"/saveCurrentWeight", "/saveCurrentWeight/{date}"})
    public String saveCurrentWeight(@ModelAttribute("dailyLog") DailyLog dailyLog,
                                    @PathVariable(required = false, value = "date") String dateString) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        dailyLog.setFkLogType(logTypeService.findByType("Weight"));
        dailyLog.setFkUser(loggedInUser);
        log.info("weight"+dailyLog.getAmount());
        dailyLogService.save(dailyLog);
        log.info("  Post Mapping saveCurrentWeight is called ");

        return  REDIRECT + DAILY_LOG+"/"+ sDatetime;
    }

    @GetMapping({"/updateCurrentWeight/{id}", "/updateCurrentWeight/{id}/{date}"})
    public String updateCurrentWeight(@PathVariable(value = "id") Long id, Model model,
                                      @PathVariable(required = false, value = "date") String dateString) {
        DailyLog dailyLog = dailyLogService.findById(id);

        model.addAttribute("date", dateString);
        model.addAttribute("dailyLog", dailyLog);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        model.addAttribute("user", loggedInUser);
        return WEIGHT_OPTIONS;
    }

    @PostMapping("/saveWeightOption")
    public String saveWeightOptions(@ModelAttribute("user") Users user){
        log.info("Postmappting saveWeightOption called in DailylogController"+user.getKcal_modifier());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        loggedInUser.setKcal_modifier(user.getKcal_modifier());
        usersService.save(loggedInUser);
        return REDIRECT + DAILY_LOG;
    }


 }
