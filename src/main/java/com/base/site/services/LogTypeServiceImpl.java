package com.base.site.services;

import com.base.site.models.Food;
import com.base.site.models.LogType;
import com.base.site.repositories.LogTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("LogTypeService")
public class LogTypeServiceImpl implements LogTypeService{
    @Autowired
    LogTypeRepository logTypeRepository;

    @Override
    public List<LogType> findAll() {
        return logTypeRepository.findAll();
    }

    @Override
    public LogType save(LogType logType) {
        return logTypeRepository.save(logType);
    }

    @Override
    public LogType findById(Long id) {

        return logTypeRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        logTypeRepository.deleteById(id);
    }

    @Override
    public LogType findByType(String type) {
        return logTypeRepository.findByType(type);
    }
}
