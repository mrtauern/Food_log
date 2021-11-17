package com.base.site.services;

import com.base.site.models.Exercise;
import com.base.site.models.ExerciseType;
import com.base.site.repositories.ExerciseRepository;
import com.base.site.repositories.ExerciseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ExerciseTypeService")
public class ExerciseTypeServiceImpl implements ExerciseTypeService {
    @Autowired
    ExerciseTypeRepository exerciseTypeRepository;

    @Override
    public List<ExerciseType> findAll() {
        return (List<ExerciseType>) exerciseTypeRepository.findAll();
    }

    @Override
    public List<ExerciseType> findAllByKeyword(String keyword) {
        if (keyword != null) {
            return exerciseTypeRepository.search(keyword);
        }
        return (List<ExerciseType>) exerciseTypeRepository.findAll();
    }

    @Override
    public ExerciseType save(ExerciseType exerciseType) {
        return exerciseTypeRepository.save(exerciseType);
    }

    @Override
    public ExerciseType findById(long id) {
        Optional<ExerciseType> optional = exerciseTypeRepository.findById(id);
        ExerciseType exerciseType = null;
        if (optional.isPresent()){
            exerciseType = optional.get();
        }else {
            throw new RuntimeException("exerciseType is not found for id ::" + id);
        }
        return exerciseType;
    }

    @Override
    public void deleteById(Long Id) {
        this.exerciseTypeRepository.deleteById(Id);
    }

}
