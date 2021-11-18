package com.base.site.repositories;

import com.base.site.models.Exercise;
import com.base.site.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ExerciseRepository")
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT e FROM Exercise e WHERE CONCAT(e.name) LIKE %?1%")
    List<Exercise> search(String keyword);

}
