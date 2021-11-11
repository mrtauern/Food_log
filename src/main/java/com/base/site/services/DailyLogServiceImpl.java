package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.repositories.DailyLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("DailyLogService")
public class DailyLogServiceImpl implements DailyLogService {

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
    public DailyLog findById(Long id) {
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

}






