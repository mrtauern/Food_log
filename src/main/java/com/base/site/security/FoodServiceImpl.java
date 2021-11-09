package com.base.site.security;

import com.base.site.models.Food;
import com.base.site.repositories.FoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("FoodService")
public class FoodServiceImpl implements FoodService {

    @Autowired
    FoodRepo foodRepo;

        @Override
        public List<Food> getAllFood() {
            return (List<Food>) foodRepo.findAll();
        }

        @Override
        public Food saveFood(Food food) {
            return foodRepo.save(food);
        }

        @Override
        public Food getFoodById(int id) {
            Optional<Food> optional = foodRepo.findById(id);
            Food food = null;
            if (optional.isPresent()){
                food = optional.get();
            }else {
                throw new RuntimeException("Food is not found for id ::" + id);
            }
            return food;
        }

        @Override
        public void deleteFoodById(int Id) {
            this.foodRepo.deleteById(Id);
        }

    }


