package com.base.site.services;

import com.base.site.models.DailyLog;
import com.base.site.models.Exercise;
import com.base.site.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ExerciseService")
public class ExerciseServiceImpl implements ExerciseService{
    @Autowired
    ExerciseRepository exerciseRepository;


    @Override
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> findAllByKeyword(String keyword) {
        if (keyword != null) {
            return exerciseRepository.search(keyword);
        }
        return (List<Exercise>) exerciseRepository.findAll();
    }

    @Override
    public Exercise save(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public Exercise findById(long id) {
        /*Optional<Exercise> optional = exerciseRepository.findById(id);
        Exercise exercise = null;
        if (optional.isPresent()){
            exercise = optional.get();
        }else {
            throw new RuntimeException("Exercise is not found for id ::" + id);
        }*/
        return exerciseRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long Id) {
        this.exerciseRepository.deleteById(Id);
    }
}
