package com.base.site.services;

import com.base.site.models.Food;
import com.base.site.models.LogType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("LogTypeService")
public interface LogTypeService {
    List<LogType> findAll();
    LogType save(LogType logType);
    LogType findById(Long id);
    void deleteById(Long id);
    LogType findByType(String type);
}
