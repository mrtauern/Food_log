package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.services.DailyLogService;
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
    DailyLogRepo dailyLogRepo;

    @Autowired
    DailyLogService dailyLogService;


    @RequestMapping("dailyLog")
    public String dailyLog(DailyLog dailyLog, Model model,@Param("keyword") String keyword) {
            List<DailyLog> list = dailyLogService.findAllByKeyword(keyword);
            model.addAttribute("list", list);
            model.addAttribute("keyword", keyword);
        log.info("  get mapping DailyLog is called");
        return "dailyLog";
    }




 }
