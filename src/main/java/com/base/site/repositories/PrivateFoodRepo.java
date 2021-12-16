package com.base.site.repositories;

import com.base.site.models.PrivateFood;
import com.base.site.models.Recipe;
import com.base.site.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository ("PrivateFoodRepo")
public interface PrivateFoodRepo extends JpaRepository <PrivateFood, Long> {
    List<PrivateFood> findAllByFkUser(Users loggedInUser);


    @Query("SELECT pf FROM PrivateFood pf WHERE CONCAT(pf.name) LIKE %?1%")
    public Page<PrivateFood> findAll(String key, Pageable pageable);

    @Query("SELECT pf FROM PrivateFood pf WHERE CONCAT(pf.name) LIKE %?1%")
    List<PrivateFood> findAllBySearch(String keyword);
}
