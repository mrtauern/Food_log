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

        return foodRepo.findById(id).get();
    }

    @Override
    public void deleteById(Long Id) {
        this.foodRepo.deleteById(Id);
    }

    @Override
    public Food setAddFoodNutritionFromDailylog(Food nutrition, DailyLog dailyLog) {
        double fat = (dailyLog.getFood().getFat()*dailyLog.getAmount())/100;
        double carbs = (dailyLog.getFood().getCarbohydrates()*dailyLog.getAmount())/100;
        double protein = (dailyLog.getFood().getProtein()*dailyLog.getAmount())/100;
        double kj = (dailyLog.getFood().getEnergy_kilojoule()*dailyLog.getAmount())/100;
        double kcal = (dailyLog.getFood().getEnergy_kcal()*dailyLog.getAmount())/100;
        nutrition.setFat(nutrition.getFat()+fat);
        nutrition.setCarbohydrates(nutrition.getCarbohydrates()+carbs);
        nutrition.setProtein(nutrition.getProtein()+protein);
        nutrition.setEnergy_kilojoule(nutrition.getEnergy_kilojoule()+kj);
        nutrition.setEnergy_kcal(nutrition.getEnergy_kcal()+kcal);

        return nutrition;

    }

        /*
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


        return foodNotInDailyLog;
    }

         */



}


