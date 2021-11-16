package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.ExerciseRepository;
import com.base.site.services.DailyLogService;
import com.base.site.services.ExerciseService;
import com.base.site.services.ExerciseTypeService;
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
    ExerciseTypeService exerciseTypeService;
    @Autowired
    UsersService usersService;
    @Autowired
    DailyLogService dailyLogService;

    Logger log = Logger.getLogger(ExerciseController.class.getName());

    public ExerciseController() {}

    @GetMapping("/exercise")
    public String exercise(Model model, Exercise exercise, @Param("keyword") String keyword) {
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);
        List<DailyLog> edList = dailyLogService.findAllByKeyword(keyword);

        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("edList", edList);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping exercise is called");

        return "exercise";
    }

    @GetMapping("/addExercise")
    public String addExercise(Model model, Exercise exercise, DailyLog dailyLog, @Param("keyword") String keyword) {
        //List<ExerciseType> exerciseTypeList = exerciseTypeService.findAllByKeyword(keyword);
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);

        //model.addAttribute("exerciseTypeList", exerciseTypeList);
        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping addExercise is called");

        return "addExercise";
    }

    @GetMapping("/createExercise/{id}")
    public String createExercise(@PathVariable(value = "id") Long id, Model model, DailyLog dailyLog) {
        ExerciseType exerciseTypes = exerciseTypeService.findById(id);
        model.addAttribute("dailyLog", dailyLog);
        model.addAttribute("exerciseTypes", exerciseTypes);
        log.info("  Get mapping createExercise is called ");

        return "createExercise";
    }
    @PostMapping("/saveExercise")
    public String saveExercise(@ModelAttribute("dailyLog") DailyLog dailyLog, Exercise exercise, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Exercise exerciseId = exerciseService.findById(exercise.getId());
        dailyLog.setFkExercise(exerciseId);
        //ExerciseType exerciseTypeId = exerciseTypeService.findById(exercise.getId());
        //exercise.setExerciseType(exerciseTypeId);
        //dailyLog.setFkUser(loggedInUser);

        //exerciseService.save(exercise);
        dailyLogService.save(dailyLog);

        log.info("  Post Mapping saveExercise is called ");
        return  "redirect:/" + "dailyLog";

    }


    @GetMapping("/updateExerciseInDailyLog/{id}")
    public String updateExerciseInDailyLog(@PathVariable(value = "id") Long id, Model model) {
        Exercise exercise= exerciseService.findById(id);
        //DailyLog dailyLog= dailyLogService.findById(id);
        model.addAttribute("exercise", exercise);
        model.addAttribute("eType", exerciseTypeService.findAll());
        log.info("  GetMapping updateDailyLog is called ");

        return "updateExerciseInDailyLog";
    }

    @GetMapping("/deleteExerciseFromDailyLog/{id}")
    public String deleteEExerciseFromDailyLog(@PathVariable(value = "id") Long id, Model model) {
        this.exerciseService.deleteById(id);
        log.info("  GetMapping deleteEExerciseFromDailyLog is called ");

        return "redirect:/" + "dailyLog";
    }

}
