package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
