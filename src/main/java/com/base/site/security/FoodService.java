package com.base.site.security;

import com.base.site.models.Food;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FoodService")
public interface FoodService {

    List<Food> getAllFood();
    Food saveFood(Food food);
    Food getFoodById(int id);
    void deleteFoodById(int id);
}
