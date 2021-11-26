package com.base.site.controllers;

import com.base.site.models.DailyLog;
import com.base.site.models.LogType;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


import java.security.Timestamp;
import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

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

    @BeforeEach
    void setUp() {
        //usersservice
        LocalDate date = LocalDate.parse("1992-01-22");
        Users user = new Users();
        user.setBirthday(date);
        user.setUsername("user@user.dk");
        user.setFirstname("user");
        user.setLastname("user");
        user.setPassword("$2a$10$wFOFdv1jFQzYKhc0c/gT2Oa/Kk9dKw8PU0VWChpkp0ZVUfQ3avXlG");
        user.setStartWeight(120);
        user.setGoalWeight(85);
        user.setHeight(182);
        UserType userType = new UserType();
        userType.setType("User_male");
        user.setUserType(userType);
        user.setRoles("USER");
        user.setKcal_modifier(-1000);
        usersService.save(user);

        //DailyLog
        DailyLog dailyLog = new DailyLog();
        LogType logType = new LogType();
            logType.setType("Weight");
        dailyLog.setDatetime(LocalDate.now());
        dailyLog.setFkLogType(logType);
        dailyLog.setFkUser(user);
        dailyLog.setAmount((double)115);
        dailyLog.setId(1);
        dailyLogService.save(dailyLog);

        //auth


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void selectedDate() {
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"USER"})
    void dailyLog() throws Exception {

        LocalDate date = LocalDate.parse("1992-01-22");
        Users user = new Users();
        user.setBirthday(date);
        user.setUsername("user@user.dk");
        user.setFirstname("user");
        user.setLastname("user");
        user.setPassword("pa$$");
        user.setStartWeight(120);
        user.setGoalWeight(85);
        user.setHeight(182);
        UserType userType = new UserType();
        userType.setType("User_male");
        user.setUserType(userType);
        user.setRoles("USER");
        user.setKcal_modifier(-1000);
        user.setId(1L);

        DailyLog dailyLog = new DailyLog();
        LogType logType = new LogType();
        logType.setType("Weight");
        dailyLog.setDatetime(LocalDate.now());
        dailyLog.setFkLogType(logType);
        dailyLog.setFkUser(user);
        dailyLog.setAmount((double)115);
        dailyLog.setId(1);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);

        DailyLog dailyLog1 = Mockito.spy(dailyLog);
        Users loggedInUser = Mockito.spy(user);

        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.getName()).thenReturn("user@user.dk");
        Mockito.when(usersService.findUsersByUsername(auth.getName())).thenReturn(loggedInUser);
        Mockito.when(usersService.getLatestWeight(LocalDate.now())).thenReturn(dailyLog1);
        Mockito.when(usersService.getLatestWeight(LocalDate.now()).getAmount()).thenReturn((double)115);
        Mockito.when(usersService.getLoggedInUser()).thenReturn(loggedInUser);

        Mockito.when(loggedInUser.getBMR(usersService.getLatestWeight(LocalDate.now()).getAmount())).thenReturn(3000);

       ResultActions resultActions = mockMvc.perform(get("/dailyLog").with(user("user@user.dk")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    //add current weight not even used??
    /*
    @Test
    @WithMockUser(username = "user", password = "pa$$", roles = {"USER"})
    void addCurrentWeight() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/createCurrentWeight").with(user("user")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }*/

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
    void updateCurrentWeight() {
    }

    @Test
    void deleteCurrentWeight() {
    }

    @Test
    void weightOptions() {
    }

    @Test
    void saveWeightOptions() {
    }
}