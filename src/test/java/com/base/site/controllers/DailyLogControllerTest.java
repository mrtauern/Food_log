package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DailyLogController.class)
class DailyLogControllerTest {
    Logger log = Logger.getLogger(DailyLogControllerTest.class.getName());
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyLogServiceImpl dailyLogService;

    @MockBean
    private ExerciseServiceImpl exerciseService;
    @MockBean
    private UsersServiceImpl usersService;
    @MockBean
    private UsersRepo usersRepo;
    @MockBean
    private LogTypeServiceImpl logTypeService;
    @MockBean
    private FoodServiceImpl foodService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void dailyLog() throws Exception {
        mockMvc.perform(
                        get("/dailyLog").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "pa$$", roles = {"USER"})
    void addCurrentWeight() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/createCurrentWeight").with(user("user")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void createCurrentWeight() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/createCurrentWeight").with(user("user")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void saveCurrentWeight() {
    }

    @Test
    void updateCurrentWeight() throws Exception {
        DailyLog dailyLog = new DailyLog();
        dailyLog.setId(1L);
        dailyLog.setCurrentWeight(112.0);

        DailyLog spyDailyLog = Mockito.spy(dailyLog);

        Mockito.when(dailyLogService.findById(anyLong())).thenReturn(spyDailyLog);
        mockMvc.perform(get("/updateCurrentWeight/1").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCurrentWeight() throws Exception {
        mockMvc.perform(get("/deleteDailyLog/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void weightOptions() throws Exception{
        mockMvc.perform(get("/weightOptions").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void saveWeightOptions() {
    }

    @Test
    void weightGraph() throws Exception {
        mockMvc.perform(get("/weightGraph").with(user("user")))
                .andExpect(status().isOk());
    }
}