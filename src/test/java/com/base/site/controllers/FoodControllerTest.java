package com.base.site.controllers;

import com.base.site.models.DailyLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.base.site.models.Food;
import com.base.site.repositories.FoodRepo;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(FoodController.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FoodServiceImpl foodService;
    @MockBean
    private FoodRepo foodRepo;
    @MockBean
    private PrivateFoodServiceImpl privateFoodService;
    @MockBean
    private DailyLogServiceImpl dailyLogService;
    @MockBean
    private UsersServiceImpl usersService;
    @MockBean
    private UsersRepo usersRepo;
    @MockBean
    private LogTypeServiceImpl logTypeService;

    @InjectMocks
    private FoodController foodController;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void food() throws Exception {
        mockMvc.perform(
                        get("/food").with(user("user@user.dk").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void createFood() throws Exception {
        mockMvc.perform(
                        get("/createFood").with(user("user@user.dk").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void saveFood() throws Exception {

    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    void updateFood() throws Exception {
        Food food = new Food();
        food.setId(1L);
        food.setName("trasd");

        Food spyFood = Mockito.spy(food);

        Mockito.when(foodService.findById(anyLong())).thenReturn(spyFood);

        mockMvc.perform(get("/updateFood/1").with(user("user@user.dk").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFood() throws Exception {

        mockMvc.perform(get("/deleteFood/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void addFoodToDailyLog() throws Exception {
        mockMvc.perform(get("/addFoodToDailyLog").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }


    @Test
    void createDailyLog() throws Exception {
        mockMvc.perform(
                        get("/createDailyLog/food/1").with(user("user@user.dk").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void saveDailyLog() throws Exception {

    }

    @Test
    void updateDailyLog() throws Exception {
        DailyLog dailyLog = new DailyLog();
        dailyLog.setId(1L);
        dailyLog.setAmount(222.0);

        DailyLog spyDailyLog = Mockito.spy(dailyLog);

        Mockito.when(dailyLogService.findById(anyLong())).thenReturn(spyDailyLog);

        mockMvc.perform(get("/updateDailyLog/food/1").with(user("user@user.dk").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDailyLog() throws Exception {
        mockMvc.perform(get("/deleteDailyLog/1"))
                .andExpect(status().is3xxRedirection());
    }
}