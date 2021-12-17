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
import org.springframework.web.bind.annotation.RequestParam;

@Repository("AllFoodsRepository")
public interface AllFoodsRepository extends JpaRepository<AllFoods, Long> {
    @Query(value = "SELECT Foods.id, Foods.name, Foods.protein, Foods.carbohydrates, Foods.fat, Foods.energy_kilojoule, Foods.energy_kcal, Foods.fk_user_id FROM Foods WHERE CONCAT(Foods.name) LIKE %:keyword%", nativeQuery = true)
    public Page<AllFoods> findAll(@RequestParam(required = false, value="keyword")String keyword, Pageable pageable);
}
