package com.base.site.repositories;

import com.base.site.models.ExerciseType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ExerciseTypeRepository")
public interface ExerciseTypeRepository extends CrudRepository<ExerciseType, Long> {
    @Query("SELECT et FROM ExerciseType et WHERE CONCAT(et.name) LIKE %?1%")
    List<ExerciseType> search(String keyword);
}
