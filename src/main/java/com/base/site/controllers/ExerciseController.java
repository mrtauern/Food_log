package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.ExerciseRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ExerciseController {
    @Autowired
    ExerciseService exerciseService;
    @Autowired
    UsersService usersService;
    @Autowired
    DailyLogService dailyLogService;
    @Autowired
    LogTypeService logTypeService;

    Logger log = Logger.getLogger(ExerciseController.class.getName());

    public ExerciseController() {}

    @GetMapping("/exercise")
    public String exercise(Model model, Exercise exercise, @Param("keyword") String keyword) {
        log.info("  get mapping exercise is called");
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);
        List<DailyLog> edList = dailyLogService.findAllByKeyword(keyword);

        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("edList", edList);
        model.addAttribute("keyword", keyword);

        return "exercise";
    }



    @GetMapping("/createExercise")
    public String createExercise(Model model) {

        Exercise exercise = new Exercise();
        model.addAttribute("exercise", exercise);
        log.info("  createExercise is called ");

        return "createExercise";
    }

    @PostMapping("/saveExercise")
    public String saveExercise(@ModelAttribute("food") Exercise exercise, Model model) {
        exerciseService.save(exercise);
        log.info("  PostMapping saveExercise is called ");

        return  "redirect:/" + "exercise";
    }

    @GetMapping("/updateExercise/{id}")
    public String updateExercise(@PathVariable(value = "id") Long id, Model model) {
        Exercise exercise = exerciseService.findById(id);
        model.addAttribute("exercise", exercise);
        log.info("  GetMapping updateExercise is called ");

        return "updateExercise";
    }

    @GetMapping("/deleteExercise/{id}")
    public String deleteExercise(@PathVariable(value = "id") Long id, Model model) {
        this.exerciseService.deleteById(id);
        log.info("  GetMapping deleteExercise is called ");

        return "redirect:/" + "exercise";
    }


    @GetMapping({"/addExerciseToDailyLog", "/addExerciseToDailyLog/{date}"})
    public String addExerciseToDailyLog(Model model, @Param("keyword") String keyword,
                                        @PathVariable(required = false, value = "date") String dateString) {
        log.info("  get mapping addExerciseToDailyLog is called");
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        model.addAttribute("date", date.toString());


        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("keyword", keyword);

        return "addExerciseToDailyLog";
    }
    @GetMapping({"/createExerciseInDailyLog/{id}", "/createExerciseInDailyLog/{id}/{date}"})
    public String createExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model, DailyLog dailyLog,
                                            @PathVariable(required = false, value = "date") String dateString) {
        log.info("  Get mapping createExerciseInDailyLog is called ");

        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        Exercise exercise = exerciseService.findById(id);

        model.addAttribute("date", date.toString());

        model.addAttribute("logType", logTypeService.findAll());
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("exercise", exercise);

        return "createExerciseInDailyLog";
    }
    @PostMapping({"/saveExerciseInDailyLog", "/saveExerciseInDailyLog/{date}"})
    public String saveExerciseInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog, Exercise exercise, Model model,
                                         @PathVariable(required = false, value = "date") String dateString) {
        log.info("  Post Mapping saveExerciseInDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Exercise exerciseId = exerciseService.findById(exercise.getId());
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);
        dailyLog.setDatetime(date);
        String sDatetime = dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        dailyLog.setFkExercise(exerciseId);
        dailyLog.setFkUser(loggedInUser);
        dailyLog.setFkLogType(logTypeService.findByType("Exercise"));

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog/"+sDatetime;

    }
    @GetMapping({"/updateExerciseInDailyLog/{id}", "/updateExerciseInDailyLog/{id}/{date}"})
    public String updateExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model,
                                           @PathVariable(required = false, value = "date") String dateString) {
        log.info("  GetMapping updateExerciseInDailyLog is called ");
        DailyLog dailyLog= dailyLogService.findById(id);
        model.addAttribute("date", dateString);

        model.addAttribute("dailyLog", dailyLog);

        return "updateExerciseInDailyLog";
    }

    @PostMapping({"/updateExerciseInDailyLog", "/updateExerciseInDailyLog/{date}"})
    public String updateExerciseInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog,
                                           @PathVariable(required = false, value = "date") String dateString) {
        log.info("  Post Mapping updateExerciseInDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        dailyLog.setDatetime(date);

        dailyLog.setFkUser(loggedInUser);
        dailyLog.setFkLogType(logTypeService.findByType("Exercise"));

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog/"+dailyLog.getDatetime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    }

    @GetMapping({"/deleteExerciseFromDailyLog/{id}", "/deleteExerciseFromDailyLog/{id}/{date}"})
    public String deleteExerciseFromDailyLog(@PathVariable(value = "id") Long id,
                                             @PathVariable(required = false, value = "date") String dateString) {
        log.info("  GetMapping deleteEExerciseFromDailyLog is called ");
        LocalDate date = dateString == null ? LocalDate.now() : LocalDate.parse(dateString);

        this.dailyLogService.deleteById(id);

        return "redirect:/" + "dailyLog/"+date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
