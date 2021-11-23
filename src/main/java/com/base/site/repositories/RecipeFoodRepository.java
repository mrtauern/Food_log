package com.base.site.repositories;

import com.base.site.models.RecipeFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeFoodRepository extends JpaRepository<RecipeFood, Long> {
}
