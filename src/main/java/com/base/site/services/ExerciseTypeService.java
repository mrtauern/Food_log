package com.base.site.services;

import com.base.site.models.Exercise;
import com.base.site.models.ExerciseType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ExerciseTypeService")
public interface ExerciseTypeService {
    List<ExerciseType> findAll();
    List<ExerciseType> findAllByKeyword(String keyword);
    ExerciseType save(ExerciseType  exerciseType);
    ExerciseType findById(long id);
    void deleteById(Long Id);
}
