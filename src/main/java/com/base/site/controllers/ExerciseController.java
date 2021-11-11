package com.base.site.controllers;

import com.base.site.repositories.ExerciseRepository;
import com.base.site.services.ExerciseService;
import com.base.site.services.ExerciseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.logging.Logger;

@Controller
public class ExerciseController {
    @Autowired
    ExerciseService exerciseService;

    @Autowired
    ExerciseTypeService exerciseTypeService;

    Logger log = Logger.getLogger(ExerciseController.class.getName());

    public ExerciseController() {}


}
