package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import com.base.site.models.RecipeFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service("FoodService")
public interface FoodService {

    List<Food> findAll();
    List<Food> findAllByKeyword(String keyword);
    Food save(Food food);
    Food findById(Long id);
    void deleteById(Long id);
    Page<Food> findPaginatedFood(int pageNo, int pageSize, String sortField, String sortDirection, String keyword);

    Food setAddFoodNutritionFromDailylog(Food nutrition, DailyLog dailyLog, String type);

    Food setAddFoodNutritionFromRecipe(Food nutrition, RecipeFood recipeFood, String type);

    Model getPaginatedModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword);

    Model getPaginatedAddFoodModelAttributes(Model model, int pageNo, String sortField, String sortDir, String keyword);

}
