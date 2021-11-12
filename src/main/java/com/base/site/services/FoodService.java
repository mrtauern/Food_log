package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FoodService")
public interface FoodService {

    List<Food> findAll();
    List<Food> findAllByKeyword(String keyword);
    Food save(Food food);
    Food findById(Long id);
    void deleteById(Long id);

    List<Food> findAllNotInList (DailyLog dailyLog);
}
