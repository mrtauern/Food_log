package com.base.site.services;

import com.base.site.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ExerciseService")
public class ExerciseServiceImpl implements ExerciseService{
    @Autowired
    ExerciseRepository exerciseRepository;
}
