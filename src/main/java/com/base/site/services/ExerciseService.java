package com.base.site.services;

import com.base.site.models.Exercise;
import com.base.site.models.LogType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ExerciseService")
public interface ExerciseService {

    List<Exercise> findAll();
    //List<Exercise> findAllByKeyword(String keyword);
    Exercise save(Exercise  exercise);
    Exercise findById(long id);
    void deleteById(Long Id);

}
