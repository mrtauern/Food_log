package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.services.DailyLogService;
import com.base.site.services.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class DailyLogController {
    Logger log = Logger.getLogger(DailyLogController.class.getName());

    @Autowired
    DailyLogService dailyLogService;
    @Autowired
    ExerciseService exerciseService;


    @RequestMapping("dailyLog")
    public String dailyLog(DailyLog dailyLog, Exercise exercise, Model model, @Param("keyword") String keyword) {
        List<DailyLog> list = dailyLogService.findAllByKeyword(keyword);
        List<Exercise> exerciseList = exerciseService.findAllByKeyword(keyword);
            //List<DailyLog> list = dailyLogService.findAll();
        model.addAttribute("list", list);
        model.addAttribute("exerciseList", exerciseList);
            model.addAttribute("keyword", keyword);
        log.info("  get mapping DailyLog is called");
        return "dailyLog";
    }




 }
