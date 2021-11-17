package com.base.site.services;

import com.base.site.controllers.DailyLogController;
import com.base.site.models.DailyLog;
import com.base.site.models.Users;
import com.base.site.repositories.DailyLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service("DailyLogService")
public class DailyLogServiceImpl implements DailyLogService {

    Logger log = Logger.getLogger(DailyLogServiceImpl.class.getName());

    @Autowired
    DailyLogRepo dailyLogRepo;

   @Override
    public List<DailyLog> findAll() {
        return (List<DailyLog>) dailyLogRepo.findAll();
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
        Optional<DailyLog> optional = dailyLogRepo.findById(id);
        DailyLog dailyLog = null;
        if (optional.isPresent()){
            dailyLog = optional.get();
        }else {
            throw new RuntimeException("DailyLog is not found for id ::" + id);
        }
        return dailyLog;
    }

     @Override
    public void deleteById(Long Id) {
        this.dailyLogRepo.deleteById(Id);
    }

    @Override
    public int getKcalUsed(LocalDate date, Users user) {
        List<DailyLog> logList = findAll();
        int kcalUsed = 0;
        if(!logList.isEmpty()) {
            for (DailyLog dailyLog: logList) {
                log.info("hello");
                if(dailyLog.getDatetime().equals(date) && user.getId() == dailyLog.getFkUser().getId() && dailyLog.getFood() != null) {
                    kcalUsed += (int)((dailyLog.getAmount()/100)*dailyLog.getFood().getEnergy_kcal());
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
       kcalLeft = totalKcal-usedKcal;
       return kcalLeft;
    }

}






