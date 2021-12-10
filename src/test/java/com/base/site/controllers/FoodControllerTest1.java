package com.base.site.controllers;

import com.base.site.models.Food;
import com.base.site.models.UserType;
import com.base.site.models.Users;
import com.base.site.repositories.FoodRepo;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(FoodController.class)
class FoodControllerTest1 {

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


    @Mock
    private Page<Food> pageMock;
    private Food food = new Food();
    private List<Food> foodList = new ArrayList<>();
    //private Pageable pageable = of(1, 1);
//-----------------
    private Users adminUser = new Users();
    private Users user = new Users();
    private List<Users> usersList = new ArrayList<>();

    private Pageable pageable = of(1, 1);
    @Mock
    private Page<Users> pageMockUser;

    @BeforeEach
    void setUp() throws Exception {
        initMocks(this);
        FoodController controller = new FoodController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Mockito.mock(Food.class);
        pageMock = Mockito.mock(Page.class);

        food.setId(1l);
        food.setName("julekage");
        food.setProtein(22);
        food.setCarbohydrates(22);
        food.setFat(11);
        food.setEnergy_kilojoule(11);
        food.setEnergy_kcal(120);

        foodList.add(food);
        //------------

        initMocks(this);
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
        foodList.remove(food);
    }


    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    void food() throws Exception {
        mockMvc.perform(get("/food").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    void findPaginatedFood() {
    }

    @Test
    void createFood() throws Exception {
        mockMvc.perform(get("/createFood").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    void saveFood() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/saveFood");
        request.param("test", "1");
        //request.param("selectedEventStatusId", "1");

        ResultActions result = mockMvc.perform(request);

        result.andExpect(MockMvcResultMatchers.status().isOk());
        //result.andExpect(MockMvcResultMatchers.forwardedUrl("eventDetailsAdd"));
    }

    @Test
    void updateFood() throws Exception{

        Food foods = Mockito.spy(food);
        food.setId(1L);

        Mockito.when(foodService.save(food)).thenReturn(foods);

        mockMvc.perform(get("/saveFood/1").with(user("user@user.dk")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteFood() throws Exception {
        mockMvc.perform(get("/deleteFood/1").with(user("user@user.dk")))
                .andExpect(status().isOk());

         /*
        Mockito.when(foodService.deleteById(1l))
        MvcResult requestResult = mockMvc.perform(get("/deleteFood/1")
                .andExpect(status().isOk());
        String result = requestResult.getResponse().getContentAsString();
        assertEquals(result, "Student is deleted");



        Mockito.when(foodService.deleteById(1L)).thenReturn("ddddd");
        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteFood/1", 1L))
                .andExpect(status().isOk());

         */
    }

    @Test
    void addFoodToDailyLog() throws Exception {
        mockMvc.perform(get("/addFoodToDailyLog").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    void findPaginatedAddFood() throws Exception {
    }

    @Test
    void createDailyLog() throws Exception {
    }

    @Test
    void saveDailyLog() throws Exception {
    }

    @Test
    void updateDailyLog() throws Exception {
    }

    @Test
    void testUpdateDailyLog() throws Exception {
    }

    @Test
    void deleteDailyLog() throws Exception {
    }
}