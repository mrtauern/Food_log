package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.DailyLogWrapper;
import com.base.site.models.Users;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

@Service("DailyLogService")
public interface DailyLogService {

    List<DailyLog> findAll();
    List<DailyLog> findAllByKeyword(String keyword);
    DailyLog save(DailyLog dailyLog);
    DailyLog findById(long id);
    void deleteById(Long Id);
    int getKcalUsed(LocalDate date, Users user);
    int getKcalLeft(LocalDate date, Users user);

    DailyLogWrapper getLogs(Users loggedInUser, LocalDate date);
    Model getDailyLogModels(Users loggedInUser, String dateString, Model model, String keyword);
}
