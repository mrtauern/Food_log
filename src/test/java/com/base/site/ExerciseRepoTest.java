package com.base.site;

import com.base.site.models.Exercise;
import com.base.site.repositories.ExerciseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*
@RunWith(SpringRunner.class)
@DataJpaTest*/
public class ExerciseRepoTest {

    /*@Autowired
    private ExerciseRepository exerciseRepository;
    @Mock
    private ExerciseRepository exerciseRepository;

    @Before
    public void  setUp() throws Exception{
        List<Exercise> exercises = Arrays.asList(
                new Exercise(1l, 10,"run"),
                new Exercise(2l, 15,"run1"),
                new Exercise(3l, 20,"run2")
                );
        exerciseRepository.saveAll(exercises);
    }


    @After
    public void tearDown() throws Exception{
        exerciseRepository.deleteAll();
    }

    @Test
    public void testFindAll(){
        //Action
        List<Exercise> all = exerciseRepository.findAll();

        //Assert
        assertEquals(3, all.size());
    }*/
}
