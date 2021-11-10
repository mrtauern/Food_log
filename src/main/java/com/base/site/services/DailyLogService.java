package com.base.site.services;

import com.base.site.models.DailyLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DailyLogService")
public interface DailyLogService {

    List<DailyLog> findAll();
    List<DailyLog> findAll1(String keyword);
    //List<DailyLog> findByKeyword(String keyword);
    DailyLog save(DailyLog dailyLog);
    DailyLog findById(Long id);
    void deleteById(Long id);
}
