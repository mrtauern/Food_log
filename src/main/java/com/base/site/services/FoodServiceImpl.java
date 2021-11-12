package com.base.site.services;

import com.base.site.models.DailyLog;
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
        public List<Food> findAllByKeyword(String keyword) {
            if (keyword != null) {
                return foodRepo.search(keyword);
            }
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

    @Override
    public List<Food> findAllNotInList(DailyLog dailyLog) {
        List<Food> allFood = findAll();
        List<Food> foodNotInDailyLog = new ArrayList<>();
        List<Long> usedIds = new ArrayList<>();
        Boolean used = false;

        for (Food f: dailyLog.getFood()) {
            usedIds.add(f.getId());
        }

        for (Food f: allFood) {
            for (long id: usedIds) {
                if(f.getId() == id) {
                    used = true;
                }
            }

            if(used != true) {
                foodNotInDailyLog.add(f);
                usedIds.add(f.getId());
            }
            used = false;
        }


        return foodNotInDailyLog;    }

}


