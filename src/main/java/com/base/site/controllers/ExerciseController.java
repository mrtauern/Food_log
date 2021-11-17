package com.base.site.controllers;

import com.base.site.models.*;
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
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);
        List<DailyLog> edList = dailyLogService.findAllByKeyword(keyword);

        model.addAttribute("exerciseList", exerciseList);
        model.addAttribute("edList", edList);
        model.addAttribute("keyword", keyword);

        log.info("  get mapping exercise is called");

        return "exercise";
    }

}
