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
        public List<Food> findAll() {
            return (List<Food>) foodRepo.findAll();
        }

        @Override
        public Food save(Food food) {
            return foodRepo.save(food);
        }

        @Override
        public Food findById(Long id) {
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
        public void deleteById(Long Id) {
            this.foodRepo.deleteById(Id);
        }

    }


