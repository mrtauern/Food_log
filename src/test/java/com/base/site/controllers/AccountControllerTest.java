package com.base.site.controllers;

import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UPRCRepository;
import com.base.site.repositories.UsersRepo;
import com.base.site.repositories.UsersRepoImpl;
import com.base.site.security.UserDetailsServiceImpl;
import com.base.site.services.*;
import org.hibernate.mapping.Any;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AccountController.class)
//@AutoConfigureMockMvc
class AccountControllerTest {
    Logger log = Logger.getLogger(AccountControllerTest.class.getName());

    @Autowired
    private MockMvc mockMvc;
    //@Autowired
    //private userMock userMock;

    @Spy
    private AccountController accountControllerSpy;

    @MockBean
    private UsersServiceImpl usersService;
    @MockBean
    private UsersRepoImpl usersRepo;
    @MockBean
    private UserTypeServiceImpl userTypeService;
    @MockBean
    private UPRCRepository uprcRepository;
    @MockBean
    private EmailServiceImpl emailService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private UPRCService uprcService;
    @MockBean
    private LoginController loginController;
    @MockBean
    private DailyLogService dailyLogService;

    private Users adminUser = new Users();
    private Users user = new Users();
    private List<Users> usersList = new ArrayList<>();

    private Pageable pageable = of(1, 1);
    @Mock
    private Page<Users> pageMock;


    @Before
    void setUp() {
        //usersservice

        MockitoAnnotations.initMocks(this);
        Mockito.mock(Users.class);
        pageMock = Mockito.mock(Page.class);

        LocalDate date = LocalDate.parse("1992-01-22");
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

    @AfterEach
    void tearDown() {
        usersList.remove(user);
        usersList.remove(adminUser);
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    void userList() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/userList").with(user("user@user.dk")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void userListErrorTest() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/userList"))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void findPaginated() {
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    void accountLockUnlock() throws Exception {

        Users loggedInUser = Mockito.spy(user);
        loggedInUser.setId(1L);

        Mockito.when(usersService.getLoggedInUser()).thenReturn(loggedInUser);

        ResultActions resultActions = mockMvc.perform(get("/adminActionAccountNonLocked/?id=1").with(user("user@user.dk")))
                .andExpect(status().is3xxRedirection());


        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void createUser() {
    }

    @Test
    void testCreateUser() {
    }

    @Test
    void editUser() {
    }

    @Test
    void testEditUser() {
    }

    @Test
    void passwordResetUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void testDeleteUser() {
    }

    @Test
    void admin_dashboard() {
    }

    @Test
    void userInfo() {
    }

    @Test
    void editUserProfile() {
    }

    @Test
    void testEditUserProfile() {
    }
}


