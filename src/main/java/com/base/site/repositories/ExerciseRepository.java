package com.base.site.repositories;

import com.base.site.models.Exercise;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ExerciseRepository")
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    @Query("SELECT e FROM Exercise e WHERE CONCAT(e.exerciseType) LIKE %?1%")
    List<Exercise> search(String keyword);
}
