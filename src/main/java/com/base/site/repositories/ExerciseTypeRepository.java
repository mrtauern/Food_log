package com.base.site.repositories;

import com.base.site.models.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("ExerciseTypeRepository")
public interface ExerciseTypeRepository extends CrudRepository<ExerciseType, Long> {
}
