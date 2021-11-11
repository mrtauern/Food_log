package com.base.site.repositories;

import com.base.site.models.DailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("DailyLogRepo")
public interface DailyLogRepo extends JpaRepository<DailyLog, Long> {

   @Query("SELECT p FROM DailyLog p WHERE CONCAT(p.id) LIKE %?1%")
    List<DailyLog> search(String keyword);

}

