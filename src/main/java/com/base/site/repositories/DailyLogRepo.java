package com.base.site.repositories;

import com.base.site.models.DailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("DailyLogRepo")
public interface DailyLogRepo extends JpaRepository<DailyLog, Long> {

    @Query("SELECT p FROM DailyLog p WHERE CONCAT(p.id) LIKE %?1%")
    List<DailyLog> search(String keyword);

    @Query(value = "{call get_weight_graph(:user_id, :selected_date)}", nativeQuery = true)
    public List<DailyLog> getWeightGraph(@Param("user_id") Long user_id, @Param("selected_date") String selected_date);

}

