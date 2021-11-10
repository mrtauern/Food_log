package com.base.site.repositories;

import com.base.site.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("ExerciseRepository")
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}
