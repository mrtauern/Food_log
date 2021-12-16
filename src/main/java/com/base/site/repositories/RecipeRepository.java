package com.base.site.repositories;

import com.base.site.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RecipeRepository")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByFkUser(Users loggedInUser);

    @Query("SELECT r FROM Recipe r WHERE CONCAT(r.name) LIKE %?1%")
    List<Recipe> search(String keyword);


    @Query("SELECT pf FROM Recipe pf WHERE CONCAT(pf.name) LIKE %:keyword% AND pf.fkUser.id LIKE :userId" )
    List<Recipe> findAllByFkUserAndSearch(@Param("userId") Long userId, @Param("keyword") String keyword);
}
