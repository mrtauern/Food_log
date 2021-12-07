package com.base.site.controllers;

import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.UPRCRepository;
import com.base.site.repositories.UsersRepo;
import com.base.site.repositories.UsersRepoImpl;
import com.base.site.security.UserDetailsServiceImpl;
import com.base.site.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc
class AccountControllerTest {
    Logger log = Logger.getLogger(AccountControllerTest.class.getName());

    @Autowired
    private MockMvc mockMvc;

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

    private Users adminUser = new Users();
    private Users user = new Users();
    private List<Users> usersList = new ArrayList<>();

    private Pageable pageable = of(1, 1);
    @MockBean
    private Page<Users> pageMock;


    @BeforeEach
    void setUp() {
        //usersservice

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
        //Page<Users> page = usersService.findPaginated(1,5,"firstname","asc","");

        //Users user = usersService.findUsersByUsername("user@user.dk");
        for (Users test: usersList) {
            log.info(test.getFullName());
        }
        //page = new PageImpl<>(usersList, pageable,usersList.size());
        //usersList = page.getContent();

        Users loggedInUser = Mockito.spy(user);

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);
        Page<Users> page = Mockito.mock(Page.class);
        //List<Users> usersTest = Mockito.mock(List.class);
        //AccountController accountController = Mockito.mock(AccountController.class);
        //Model model = Mockito.mock(Model.class);
        //spyPage = Mockito.spy(page);
        //Page<Users> pageTestSpy = Mockito.spy(pageTest);
        //List<Users> usersTestSpy = Mockito.spy(usersTest);

        //ReflectionTestUtils.setField(usersList,spyPage);

        Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.getName()).thenReturn("user@user.dk");
        Mockito.when(usersService.findPaginated(1,5,"firstname","asc","")).thenReturn(pageMock);
        //Mockito.when(accountController.findPaginated(model, 1,"firstname","asc","")).thenReturn("userList");
        Mockito.when(pageMock.getContent()).thenReturn(usersList);
        //Mockito.doReturn(usersList).when(page.getContent());
        //Mockito.when(usersService.findPaginated(1,5,"firstname","asc","").getContent()).thenReturn(page.getContent());
        Mockito.when(usersService.findUsersByUsername(auth.getName())).thenReturn(loggedInUser);

        ResultActions resultActions = mockMvc.perform(get("/userList").with(user("user@user.dk")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void findPaginated() {
    }

    @Test
    void accountLockUnlock() {
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