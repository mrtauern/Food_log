package com.base.site.repositories;

import com.base.site.models.Food;
import com.base.site.models.Recipe;
import com.base.site.models.RecipeFood;
import com.base.site.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RecipeFoodRepository")
public interface RecipeFoodRepository extends JpaRepository<RecipeFood, Long> {
    List<RecipeFood> findRecipeFoodsByRecipe(Recipe recipe);
}
