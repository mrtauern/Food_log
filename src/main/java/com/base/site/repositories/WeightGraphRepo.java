package com.base.site.repositories;

import com.base.site.models.DailyLog;
import com.base.site.models.WeightGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("WeightGraphRepo")
public interface WeightGraphRepo extends JpaRepository<WeightGraph, Long> {

    @Query(value = "{call get_weight_graph(:user_id, :selected_date)}", nativeQuery = true)
    public List<WeightGraph> getWeightGraph(@Param("user_id") Long user_id, @Param("selected_date") String selected_date);
}
