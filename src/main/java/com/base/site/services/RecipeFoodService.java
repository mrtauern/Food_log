package com.base.site.services;

import com.base.site.models.Recipe;
import com.base.site.models.RecipeFood;
import com.base.site.models.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("RecipeFoodService")
public interface RecipeFoodService {

    List<RecipeFood> findAll();

    List<RecipeFood> findByRecipe(Recipe recipe);

    RecipeFood findById(Long id);

    RecipeFood save(RecipeFood recipeFood);

    void deleteById(Long id);

    void delete(RecipeFood recipeFood);

}
