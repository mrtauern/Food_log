package com.base.site.services;

import com.base.site.models.Recipe;
import com.base.site.models.RecipeFood;
import com.base.site.models.Users;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("RecipeService")
public interface RecipeService {
    List<Recipe> findAll();

    Recipe findById(Long id);

    Recipe save(Recipe recipe);

    void deleteById(Long id);

    void delete(Recipe recipe);

    List<Recipe> findAllFkUser(Users loggedInUser);

    Recipe findRecipeById(long id);

    List<Recipe> getRecipesForUser(Users loggedInUser);
}

