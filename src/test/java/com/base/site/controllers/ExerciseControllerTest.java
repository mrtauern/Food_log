package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.ExerciseRepository;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;


import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ExerciseController.class)
class ExerciseControllerTest {

    Logger log = Logger.getLogger(ExerciseControllerTest.class.getName());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyLogServiceImpl dailyLogService;
    @MockBean
    private UsersServiceImpl usersService;
    @MockBean
    private LogTypeServiceImpl logTypeService;
    @MockBean
    private ExerciseServiceImpl exerciseService;
    @MockBean
    private UsersRepo usersRepo;
    @MockBean
    private ExerciseRepository exerciseRepository;
    @Mock
    private ExerciseRepository mockedExerciseRepo;

    @AfterEach
    void tearDown() {
        mockedExerciseRepo.deleteAll();
    }

    @Test
    public void testRepoIsNotNull()  throws Exception  {
        assertNotNull(mockedExerciseRepo);
    }


    @Test
    void exercise() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/exercise").with(user("user").roles("ADMIN")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void createExercise() throws Exception{
        ResultActions resultActions = mockMvc.perform(get("/createExercise").with(user("user")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    public void updateExerciseInDailyLogTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/updateExerciseInDailyLog")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    public void save_exercise_OK() throws Exception {
        Exercise newExercise = new Exercise(1L, 22, "run");
        when(mockedExerciseRepo.save(any(Exercise.class))).thenReturn(newExercise);

    }

    @Test
    public void checkIfExerciseExist() throws Exception {
        //Given
        Exercise newExercise = new Exercise(1L, 22, "run");

        //When
        Optional<Exercise> exist = mockedExerciseRepo.findById(1l);

        //Then
        assertThat(exist).isNotNull();
    }
}