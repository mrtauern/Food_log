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
    //@Query("SELECT l FROM DailyLog l WHERE CONCAT(l.foodAmount, '') LIKE %?1%")
    public List<DailyLog> search(String keyword);

    /*
    //Custom query
    @Query(value = "select * from DailyLog s where s.food_amount like %:keyword% or s.id like %:keyword%", nativeQuery = true)
    List<DailyLog> findByKeyword(@Param("keyword") String keyword); */
}

