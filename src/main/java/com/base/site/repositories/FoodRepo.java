package com.base.site.repositories;

import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository ("FoodRepo")
public interface FoodRepo extends JpaRepository<Food, Long> {

    @Query("SELECT f FROM Food f WHERE CONCAT(f.name) LIKE %?1%")
    public List<Food> search(String keyword);

}
