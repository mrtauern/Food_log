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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/addExerciseToDailyLog")
    public String addExerciseToDailyLog(Model model, Exercise exercise, DailyLog dailyLog, @Param("keyword") String keyword) {
        log.info("  get mapping addExerciseToDailyLog is called");
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("keyword", keyword);

        return "addExerciseToDailyLog";
    }

    @GetMapping("/createExerciseInDailyLog/{id}")
    public String createExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model, DailyLog dailyLog) {
        log.info("  Get mapping createExerciseInDailyLog is called ");
        Exercise exercise = exerciseService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("exercise", exercise);

        return "createExerciseInDailyLog";
    }
    @PostMapping("/saveExerciseInDailyLog")
    public String saveExerciseInDailyLog(@ModelAttribute("dailyLog") DailyLog dailyLog, Exercise exercise, Model model) {
        log.info("  Post Mapping saveExerciseInDailyLog is called ");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Exercise exerciseId = exerciseService.findById(exercise.getId());
        dailyLog.setFkExercise(exerciseId);
        dailyLog.setFkUser(loggedInUser);
        dailyLog.setFkLogType(logTypeService.findByType("Exercise"));

        dailyLogService.save(dailyLog);

        return  "redirect:/" + "dailyLog";

    }

    @GetMapping("/updateExerciseInDailyLog/{id}")
    public String updateExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping updateExerciseInDailyLog is called ");
        DailyLog dailyLog= dailyLogService.findById(id);
        model.addAttribute("dailyLog", dailyLog);

        return "updateExerciseInDailyLog";
    }

    @GetMapping("/deleteExerciseFromDailyLog/{id}")
    public String deleteEExerciseFromDailyLog(@PathVariable(value = "id") Long id, Model model) {
        log.info("  GetMapping deleteEExerciseFromDailyLog is called ");
        this.dailyLogService.deleteById(id);

        return "redirect:/" + "dailyLog";
    }

}
