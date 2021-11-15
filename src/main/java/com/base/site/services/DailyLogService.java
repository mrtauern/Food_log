package com.base.site.services;

import com.base.site.models.DailyLog;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service("DailyLogService")
public interface DailyLogService {

    List<DailyLog> findAll();
    List<DailyLog> findAllByKeyword(String keyword);
    DailyLog save(DailyLog dailyLog);
    DailyLog findById(long id);
    void deleteById(Long Id);
}
