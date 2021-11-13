package com.base.site.repositories;

import com.base.site.models.Food;
import com.base.site.models.PrivateFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateFoodRepo extends JpaRepository <PrivateFood, Long> {

    @Query("SELECT pf FROM PrivateFood pf WHERE CONCAT(pf.name) LIKE %?1%")
    public List<PrivateFood> search(String keyword);
}
