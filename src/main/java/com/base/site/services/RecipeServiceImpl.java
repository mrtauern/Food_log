package com.base.site.services;

import com.base.site.models.Recipe;
import com.base.site.models.RecipeFood;
import com.base.site.models.Users;
import com.base.site.repositories.RecipeRepository;
import com.base.site.repositories.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service("RecipeService")
public class RecipeServiceImpl implements RecipeService{
    Logger log = Logger.getLogger(RecipeServiceImpl.class.getName());

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeFoodService recipeFoodService;

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id).get();
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    @Override
    public List<Recipe> findAllFkUser(Users loggedInUser) {
        return recipeRepository.findAllByFkUser(loggedInUser);
    }

    @Override
    public Recipe findRecipeById(long id) {
        Recipe recipe = findById(id);

        return recipe;
    }

    @Override
    public double calculateCaloriesInRecipe(List<RecipeFood> recipeFoods, Recipe recipe) {
        double total = 0;
        for (RecipeFood recipeFood : recipeFoods) {
            total += (recipeFood.getFood().getEnergy_kcal()*recipeFood.getAmount()) / 100;
        }
        total = (total/recipe.getTotal_weight())*100;
        return total;
    }
}
