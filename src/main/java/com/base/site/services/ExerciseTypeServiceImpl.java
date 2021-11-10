package com.base.site.services;

import com.base.site.repositories.ExerciseRepository;
import com.base.site.repositories.ExerciseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ExerciseTypeService")
public class ExerciseTypeServiceImpl implements ExerciseTypeService {
    @Autowired
    ExerciseTypeRepository exerciseTypeRepository;
}
