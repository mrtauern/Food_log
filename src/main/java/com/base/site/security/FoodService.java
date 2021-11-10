package com.base.site.security;

import com.base.site.models.Food;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FoodService")
public interface FoodService {

    List<Food> findAll();
    Food save(Food food);
    Food findById(Long id);
    void deleteById(Long id);
}
