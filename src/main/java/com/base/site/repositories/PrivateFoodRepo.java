package com.base.site.repositories;

import com.base.site.models.PrivateFood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository ("PrivateFoodRepo")
public interface PrivateFoodRepo extends JpaRepository <PrivateFood, Long> {

    @Query("SELECT pf FROM PrivateFood pf WHERE CONCAT(f.name) LIKE %?1%")
    public Page<PrivateFood> findAll(String key, Pageable pageable);
}
