package com.base.site.controllers;

import com.base.site.models.UserPassResetCode;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.FoodRepo;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private DailyLogServiceImpl dailyLogService;
    @MockBean
    private UsersServiceImpl usersService;
    @MockBean
    private UserTypeServiceImpl userTypeService;
    @MockBean
    private UPRCServiceImpl uprcService;
    @MockBean
    private EmailServiceImpl emailService;
    @MockBean
    private UsersRepo usersRepo;
    @MockBean
    private LogTypeServiceImpl logTypeService;

    @InjectMocks
    private LoginController loginController;


    private Users adminUser = new Users();
    private Users user = new Users();
    private List<Users> usersList = new ArrayList<>();

    private Pageable pageable = of(1, 1);
    @Mock
    private Page<Users> pageMock;

    @BeforeEach
    void setUp() {
        //usersservice

        MockitoAnnotations.initMocks(this);
        Mockito.mock(Users.class);
        pageMock = Mockito.mock(Page.class);

        LocalDate date = LocalDate.parse("1992-01-22");
        user.setBirthday(date);
        user.setId(2L);
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

        adminUser.setBirthday(date);
        adminUser.setUsername("admin@admin.dk");
        adminUser.setFirstname("admin");
        adminUser.setLastname("admin");
        adminUser.setPassword("$2a$10$wFOFdv1jFQzYKhc0c/gT2Oa/Kk9dKw8PU0VWChpkp0ZVUfQ3avXlG");
        adminUser.setStartWeight(120);
        adminUser.setGoalWeight(85);
        adminUser.setHeight(182);
        adminUser.setUserType(userType);
        adminUser.setRoles("ADMIN");
        adminUser.setKcal_modifier(-1000);

        usersList.add(user);
        usersList.add(adminUser);
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(
                        get("/login").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    void fetchSignoutSite() throws Exception  {
        mockMvc.perform(
                        get("/logout").with(user("user@user.dk")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void showSignUpForm() throws Exception    {
        mockMvc.perform(
                        get("/login").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    /*
    @Test
    void addUser() throws Exception   {
        Users user = Mockito.mock(Users.class);
        user.setUsername("test@test.dk");

        Mockito.when(usersService.returnCreatedUser(any(Users.class))).thenReturn(user);
        Mockito.when(user.getUsername()).thenReturn("test@test.dk");
        String userTypeString = "User_male";
        String password = "pass";
        String username = "test@test.dk";
        String lastname = "test";
        String firstname = "test";
        ResultActions resultActions = mockMvc.perform(post("/signup")
                        .param("userTypeString", userTypeString)
                        .param("password", password)
                        .param("username", username)
                        .param("lastname", lastname)
                        .param("firstname", firstname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }*/

    @Test
    void passwordReset() throws Exception   {
        mockMvc.perform(
                        get("/password_reset").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    void confirmReset() throws Exception   {
        ResultActions resultActions = mockMvc.perform(post("/signup/User_male")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void passwordResetCode() throws Exception   {
        mockMvc.perform(
                        get("/password_reset_code").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }
}