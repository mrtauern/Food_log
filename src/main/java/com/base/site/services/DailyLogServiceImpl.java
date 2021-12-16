package com.base.site.services;

import com.base.site.models.*;
import com.base.site.repositories.DailyLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service("DailyLogService")
public class DailyLogServiceImpl implements DailyLogService {

    Logger log = Logger.getLogger(DailyLogServiceImpl.class.getName());

    @Autowired
    DailyLogRepo dailyLogRepo;

    @Autowired
    FoodService foodService;

    @Autowired
    PrivateFoodService privateFoodService;

    @Autowired
    UsersService usersService;

    @Autowired
    WeightGraphService weightGraphService;

    @Override
    public List<DailyLog> findAll() {
        return dailyLogRepo.findAll();
    }

    @Override
    public List<DailyLog> findAllByKeyword(String keyword) {
        if (keyword != null) {
            return dailyLogRepo.search(keyword);
        }
        return (List<DailyLog>) dailyLogRepo.findAll();
    }

    @Override
    public DailyLog save(DailyLog dailyLog) {
        return dailyLogRepo.save(dailyLog);
    }

    @Override
    public DailyLog findById(long id) {

        return dailyLogRepo.findById(id).get();
    }

    @Override
    public void deleteById(Long Id) {
        this.dailyLogRepo.deleteById(Id);
    }

    @Override
    public int getKcalUsed(LocalDate date, Users user) {
        List<DailyLog> logList = findAll();
        int kcalUsed = 0;

        if (!logList.isEmpty()) {
            for (DailyLog dailyLog : logList) {

                if (dailyLog.getDatetime().equals(date) && user.getId().equals(dailyLog.getFkUser().getId()) && dailyLog.getFood() != null) {
                    kcalUsed += (int) ((dailyLog.getAmount() / 100) * dailyLog.getFood().getEnergy_kcal());
                }else if (dailyLog.getDatetime().equals(date) && user.getId().equals(dailyLog.getFkUser().getId()) && dailyLog.getPrivateFood() != null) {
                    kcalUsed += (int) ((dailyLog.getAmount() / 100) * dailyLog.getPrivateFood().getEnergy_kcal());
                }else if (dailyLog.getDatetime().equals(date) && user.getId().equals(dailyLog.getFkUser().getId()) && dailyLog.getRecipe() != null) {
                    kcalUsed += (int) ((dailyLog.getAmount() / 100)* dailyLog.getRecipe().getCalculateCaloriesInRecipe());
                }
            }
        }

        return kcalUsed;
    }

    @Override
    public int getKcalLeft(LocalDate date, Users user) {
        int kcalLeft = 0;
        int totalKcal = user.getBMR(user.getCurrentWeight());
        int usedKcal = getKcalUsed(date, user);
        kcalLeft = totalKcal - usedKcal;
        return kcalLeft;
    }

    public DailyLogWrapper getLogs(Users loggedInUser, LocalDate date) {
        log.info("getLogs called in dailylogServiceImpl");
        DailyLogWrapper dailyLogWrapper = new DailyLogWrapper(findAll());
        Food nutrition = new Food("nutrition",0.0,0.0,0.0,0.0,0.0);
        for (DailyLog dailyLog: dailyLogWrapper.getDailyLogs()) {
            if(dailyLog.getDatetime().equals(date) && loggedInUser.getId().equals(dailyLog.getFkUser().getId())) {

                String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                dailyLog.setSDatetime(sDatetime);

                if(dailyLog.getFood() != null || dailyLog.getPrivateFood() != null || dailyLog.getRecipe() != null) {
                    dailyLogWrapper.addToDailyLogsFoods(dailyLog);
                    String logType = dailyLog.getFkLogType().getType();


                    switch (logType){
                        case "Breakfast":
                            dailyLogWrapper.addToDailyLogsBreakfast(dailyLog);
                            log.info("should add food");
                            if(dailyLog.getFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "food");
                            }
                            if(dailyLog.getPrivateFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "pfood");
                            }
                            if (dailyLog.getRecipe() != null){
                                nutrition = foodService.setAddFoodNutritionFromRecipe(nutrition, dailyLog);
                            }
                            break;
                        case "Lunch":
                            dailyLogWrapper.addToDailyLogsLunch(dailyLog);
                            if(dailyLog.getFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "food");
                            }
                            if(dailyLog.getPrivateFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "pfood");
                            }
                            if (dailyLog.getRecipe() != null){
                                nutrition = foodService.setAddFoodNutritionFromRecipe(nutrition, dailyLog);
                            }
                            break;
                        case "Dinner":
                            dailyLogWrapper.addToDailyLogsDinner(dailyLog);
                            if(dailyLog.getFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "food");
                            }
                            if(dailyLog.getPrivateFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "pfood");
                            }
                            if (dailyLog.getRecipe() != null){
                                nutrition = foodService.setAddFoodNutritionFromRecipe(nutrition, dailyLog);
                            }
                            break;
                        case "Miscellaneous":
                            dailyLogWrapper.addToDailyLogsMiscellaneous(dailyLog);
                            if(dailyLog.getFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "food");
                            }
                            if(dailyLog.getPrivateFood()!=null) {
                                nutrition = foodService.setAddFoodNutritionFromDailylog(nutrition, dailyLog, "pfood");
                            }
                            if (dailyLog.getRecipe() != null){
                                nutrition = foodService.setAddFoodNutritionFromRecipe(nutrition, dailyLog);
                            }
                            break;
                        case "Weight":
                            dailyLogWrapper.setWeight(dailyLog);
                            break;
                        default:
                            log.info("UPS... Something went wrong!");
                    }

                }else if (dailyLog.getFkExercise() != null){
                    dailyLogWrapper.addToDailyLogsExercises(dailyLog);
                }else if(dailyLog.getFkLogType().getType().equals("Weight")) {
                    dailyLogWrapper.setWeight(dailyLog);
                }
            }
        }
        dailyLogWrapper.setNutrition(nutrition);
        if(dailyLogWrapper.getWeight() == null) {
            DailyLog weight = new DailyLog();
            weight.setId(0L);
            weight.setAmount(0.0);
            dailyLogWrapper.setWeight(weight);
        }
        return dailyLogWrapper;
    }

    public Model getDailyLogModels(Users loggedInUser, String dateString, Model model, String keyword, HttpSession session) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString, formatter);

        Date currentDate = new Date();
        LocalDate today = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        DailyLogWrapper dailyLogWrapper = getLogs(loggedInUser, date);

        List<WeightGraph> weightGraphs = weightGraphService.getWeightGraph(loggedInUser.getId(), String.valueOf(date));

        List<Double> weights = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        for (WeightGraph weightGraph: weightGraphs) {
            if(weightGraph.getUserId().equals(loggedInUser.getId())){
                //X-axe
                dates.add(weightGraph.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                //Y-axe
                weights.add(weightGraph.getWeight());
            }
        }

        for (Double weight: weights){
            log.info("Weights: "+weight);
        }

        log.info("LoggedinUserID----------------"+loggedInUser.getId());

        model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        model.addAttribute("sSelectedDate", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        model.addAttribute("foods", dailyLogWrapper.getDailyLogsFoods());
        model.addAttribute("breakfasts", dailyLogWrapper.getDailyLogsBreakfast());
        model.addAttribute("lunches", dailyLogWrapper.getDailyLogsLunch());
        model.addAttribute("dinners", dailyLogWrapper.getDailyLogsDinner());
        model.addAttribute("miscellaneous", dailyLogWrapper.getDailyLogsMiscellaneous());
        //model.addAttribute("pfoods", dailyLogsPrivateFoods);
        model.addAttribute("exercises", dailyLogWrapper.getDailyLogsExercises());
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

        model.addAttribute("bmr", usersService.getLoggedInUser(session).getBMR(usersService.getLatestWeight(date).getAmount()));
        model.addAttribute("kcalUsed", getKcalUsed(date, usersService.getLoggedInUser(session)));
        model.addAttribute("kcalLeft", getKcalLeft(date, usersService.getLoggedInUser(session)));
        model.addAttribute("nutrition", dailyLogWrapper.getNutrition());

        model.addAttribute("weight", dailyLogWrapper.getWeight());

        model.addAttribute("selectedPage", "dailyLog");

        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        model.addAttribute("weights", weights);
        model.addAttribute("dates", dates);
        model.addAttribute("goal", usersService.getLoggedInUser(session).getGoalWeight());

        return model;
    }

    @Override
    public Model getWeightGraphModels(Model model, HttpSession session) {
        Users loggedInUser = usersService.getLoggedInUser(session);

        List<DailyLog> dailyLogs = findAll();

        List<Double> weights = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        for (DailyLog dailyLog: dailyLogs) {
            if(dailyLog.getFkUser().getId().equals(loggedInUser.getId()) && dailyLog.getFkLogType().getType().equals("Weight")){
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
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));
        model.addAttribute("weights", weights);
        model.addAttribute("dates", dates);
        model.addAttribute("goal", usersService.getLoggedInUser(session).getGoalWeight());
        return model;
    }


}






