package com.base.site.repositories;

import com.base.site.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository ("FoodRepo")
public interface FoodRepo extends JpaRepository<Food, Integer> {
}
