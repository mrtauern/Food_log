package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.Users;
import com.base.site.repositories.DailyLogRepo;
import com.base.site.services.DailyLogService;
import com.base.site.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class DailyLogController {
    Logger log = Logger.getLogger(DailyLogController.class.getName());

    @Autowired
    DailyLogRepo dailyLogRepo;

    @Autowired
    DailyLogService dailyLogService;

    @Autowired
    UsersService usersService;

    private final String DAILY_LOG = "dailyLog";
    private final String REDIRECT = "redirect:/";

    @RequestMapping("dailyLog")
    public String dailyLog(DailyLog dailyLog, Model model,@Param("keyword") String keyword) {
            List<DailyLog> list = dailyLogService.findAllByKeyword(keyword);
            //List<DailyLog> list = dailyLogService.findAll();
            model.addAttribute("list", list);
            model.addAttribute("keyword", keyword);
        log.info("  get mapping DailyLog is called");
        return DAILY_LOG;
    }

    @GetMapping("dailyLog/{date}")
    public String dailyLog(@PathVariable(value = "date") String date, Model model, @Param("keyword") String keyword) {
        log.info("Getnapping called for dailylog for specific date: "+date);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        List<DailyLog> logList = dailyLogService.findAll();
        ArrayList<DailyLog> logListUserDate = new ArrayList<DailyLog>();

        LocalDate enteredDate = LocalDate.parse(date);

        for (DailyLog logdate: logList) {
            if(logdate.getDatetime().equals(enteredDate) && loggedInUser.getId() == logdate.getFkUser().getId()) {
                logListUserDate.add(logdate);
            }
        }

        model.addAttribute("list",logListUserDate);
        model.addAttribute("keyword", keyword);

        return DAILY_LOG;
    }




 }
