package com.base.site.services;

import com.base.site.models.Recipe;
import com.base.site.models.RecipeFood;
import com.base.site.models.Users;
import com.base.site.repositories.RecipeFoodRepository;
import com.base.site.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service("RecipeFoodService")
public class RecipeFoodServiceImpl implements RecipeFoodService{

    Logger log = Logger.getLogger(RecipeServiceImpl.class.getName());

    @Autowired
    RecipeFoodRepository recipeFoodRepository;

    @Override
    public List<RecipeFood> findAll() {
        return recipeFoodRepository.findAll();
    }

    @Override
    public List<RecipeFood> findByRecipe(Recipe recipe) {
        //return recipeFoodRepository.findAllByRecipe(recipe);
        return recipeFoodRepository.findRecipeFoodsByRecipe(recipe);
    }

    @Override
    public RecipeFood findById(Long id) {
        return recipeFoodRepository.findById(id).get();
    }

    @Override
    public RecipeFood save(RecipeFood recipeFood) {
        return recipeFoodRepository.save(recipeFood);
    }

    @Override
    public void deleteById(Long id) {
        recipeFoodRepository.deleteById(id);
    }

    @Override
    public void delete(RecipeFood recipeFood) {
        recipeFoodRepository.delete(recipeFood);
    }
}
