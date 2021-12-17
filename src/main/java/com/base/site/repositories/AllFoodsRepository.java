package com.base.site.repositories;

import com.base.site.models.AllFoods;
import com.base.site.models.DailyLog;
import com.base.site.models.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("AllFoodsRepository")
public interface AllFoodsRepository extends JpaRepository<AllFoods, Long> {
    @Query(value = "SELECT f.id, f.name, f.protein, f.carbohydrates, f.fat, f.energy_kilojoule, f.energy_kcal, f.fk_user_id, f.archived FROM Foods f WHERE f.name LIKE %:key%",
            countQuery = "select count(f.id) FROM Foods f WHERE f.name LIKE %:key%",
            nativeQuery = true)
    public Page<AllFoods> findAll(@Param("key") String key, Pageable pageable);
}

//countQuery = "select count(f.id) FROM Foods f WHERE CONCAT(f.name) LIKE %:key%",