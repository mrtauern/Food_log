package com.base.site;

import com.base.site.models.Exercise;
import com.base.site.repositories.ExerciseRepository;
import com.base.site.services.ExerciseService;
import com.base.site.services.ExerciseServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
//import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ExerciseServiceTest {

    @InjectMocks
    private ExerciseServiceImpl exerciseService;
    @Mock
    private ExerciseRepository exerciseRepository;

    @Test
    public void findAllExerciseTest(){
        //Preparation / Stub
        Mockito.when(exerciseRepository.findAll()).thenReturn(Arrays.asList(
                new Exercise(1l,20,"run"),
                new Exercise(2l,22,"run2"),
                new Exercise(3l,23,"run3")
        ));
        //Action
        List<Exercise> allExercise = exerciseService.findAll();

        //Assertion
        assertEquals(20,allExercise.get(0).getKcalBurnedPerMin());
        assertEquals(22,allExercise.get(1).getKcalBurnedPerMin());
        assertEquals(23,allExercise.get(2).getKcalBurnedPerMin());
    }
}
