package com.base.site.services;

import com.base.site.models.AllFoods;
import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import com.base.site.repositories.AllFoodsRepository;
import com.base.site.models.*;
import com.base.site.repositories.FoodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


import static org.springframework.data.domain.PageRequest.of;

@Service("FoodService")
public class FoodServiceImpl implements FoodService {
    Logger log = Logger.getLogger(FoodServiceImpl.class.getName());


    @Autowired
    FoodRepo foodRepo;
    @Autowired
    UsersService usersService;
    @Autowired
    PrivateFoodService privateFoodService;
    @Autowired
    RecipeServiceImpl recipeService;
    @Autowired
    LogTypeServiceImpl logTypeService;

    @Autowired
    AllFoodsRepository allFoodsRepository;

    @Autowired
    AllFoodsService allFoodsService;

    @Override
    public List<Food> findAll() {
        return (List<Food>) foodRepo.findAll();
    }

    @Override
    public List<Food> findAllByKeyword(String keyword) {
        if (keyword != null) {
            //return foodRepo.search(keyword);
            return foodRepo.findAll();
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
    public Food setAddFoodNutritionFromDailylog(Food nutrition, DailyLog dailyLog, String type) {
        double fat = type.equals("food") ? (dailyLog.getFood().getFat()*dailyLog.getAmount())/100 : (dailyLog.getPrivateFood().getFat()*dailyLog.getAmount())/100;
        double carbs = type.equals("food") ? (dailyLog.getFood().getCarbohydrates()*dailyLog.getAmount())/100 : (dailyLog.getPrivateFood().getCarbohydrates()*dailyLog.getAmount())/100;
        double protein = type.equals("food") ? (dailyLog.getFood().getProtein()*dailyLog.getAmount())/100 : (dailyLog.getPrivateFood().getProtein()*dailyLog.getAmount())/100;
        double kj = type.equals("food") ? (dailyLog.getFood().getEnergy_kilojoule()*dailyLog.getAmount())/100 : (dailyLog.getPrivateFood().getEnergy_kilojoule()*dailyLog.getAmount())/100;
        double kcal = type.equals("food") ? (dailyLog.getFood().getEnergy_kcal()*dailyLog.getAmount())/100 : (dailyLog.getPrivateFood().getEnergy_kcal()*dailyLog.getAmount())/100;

        nutrition.setFat(nutrition.getFat()+fat);
        nutrition.setCarbohydrates(nutrition.getCarbohydrates()+carbs);
        nutrition.setProtein(nutrition.getProtein()+protein);
        nutrition.setEnergy_kilojoule(nutrition.getEnergy_kilojoule()+kj);
        nutrition.setEnergy_kcal(nutrition.getEnergy_kcal()+kcal);

        return nutrition;

    }

    @Override
    public Food setAddFoodNutritionFromRecipe(Food nutrition, DailyLog dailyLog) {
        Food tempNutrition = dailyLog.getRecipe().getNutritionFromRecipe();
        double fat = tempNutrition.getFat()*dailyLog.getAmount();
        double carbs = tempNutrition.getCarbohydrates()*dailyLog.getAmount();
        double protein = tempNutrition.getProtein()*dailyLog.getAmount();
        double kj = tempNutrition.getEnergy_kilojoule()*dailyLog.getAmount();
        double kcal = tempNutrition.getEnergy_kcal()*dailyLog.getAmount();

        nutrition.setFat(fat+nutrition.getFat());
        nutrition.setCarbohydrates(carbs+nutrition.getCarbohydrates());
        nutrition.setProtein(protein+nutrition.getProtein());
        nutrition.setEnergy_kilojoule(kj+nutrition.getEnergy_kilojoule());
        nutrition.setEnergy_kcal(kcal+nutrition.getEnergy_kcal());

        return nutrition;
    }

    @Override
    public Page<AllFoods> findPaginatedFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();

        Pageable pageable = of(pageNo - 1, pageSize, sort);

        if (keyword != null) {
            return allFoodsRepository.findAll(keyword, pageable);
        }
        return this.allFoodsRepository.findAll(pageable);
    }


    @Override
    public Model getPaginatedModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session) {
        int pageSize = 15;

        Page<AllFoods> page = findPaginatedFood(pageNo,pageSize, sortField, sortDir, keyword);
        List<AllFoods> listFood = page.getContent();
        List<AllFoods> foodlistSearched =allFoodsService.findAllByKeyword(keyword);

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalFood", page.getTotalElements());

        model.addAttribute("listFood", listFood);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("foodlistSearched", foodlistSearched);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return model;
    }

    @Override
    public Model getPaginatedAddFoodModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword, HttpSession session) {

        int pageSize = 10;

        Page<AllFoods> page = findPaginatedFood(pageNo,pageSize, sortField, sortDir, keyword);
        //Page<PrivateFood> privateFoodPage = privateFoodService.findPaginatedAddFood(pageNo,pageSize, sortField, sortDir, keyword);
        List<AllFoods> listFood = page.getContent();
        //List<PrivateFood> listPrivateFood = privateFoodPage.getContent();
        List<AllFoods> foodlistSearched = allFoodsService.findAllByKeyword(keyword);
        //List<PrivateFood> privateFoodlistSearched = privateFoodService.findAllByKeyword(keyword);

        List<Recipe> recipelist = recipeService.findAllByKeyword(keyword);
        model.addAttribute("recipelist", recipelist);
        model.addAttribute("logType", logTypeService.findAll());

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalFood", page.getTotalElements());

        model.addAttribute("listFood", listFood);
        //model.addAttribute("listPrivateFood", listPrivateFood);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("foodlistSearched", foodlistSearched);
        //model.addAttribute("privateFoodlistSearched", privateFoodlistSearched);
        model.addAttribute("loggedInUser", usersService.getLoggedInUser(session));

        return model;
    }
}


