package com.base.site.repositories;

import com.base.site.models.Recipe;
import com.base.site.models.RecipeFood;
import com.base.site.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RecipeRepository")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByFkUser(Users loggedInUser);
}
