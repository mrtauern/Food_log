package com.base.site;

import com.base.site.controllers.ExerciseController;
import com.base.site.models.Exercise;
import com.base.site.services.ExerciseServiceImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired //to mocking http request
    private MockMvc mockMvc;

    @MockBean
    private ExerciseServiceImpl exerciseService;

    @Test
    public void findAllExercise() throws Exception{
        when(exerciseService.findAll()).thenReturn(Arrays.asList(
                new Exercise(1l,22,"run"),
                new Exercise(1l,22,"run"),
                new Exercise(1l,22,"run")
        ));

        //Action and Assert at once
        mockMvc.perform(MockMvcRequestBuilders.get("/exercise"))
                .andExpect(status().isOk());
                //.andExpect(content().contentType())
    }
}
