package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(RecipeController.class)
class RecipeControllerTest  {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UsersServiceImpl usersService;
    @MockBean
    private RecipeServiceImpl recipeService;
    @MockBean
    private RecipeFoodServiceImpl recipeFoodService;
    @MockBean
    private FoodServiceImpl foodService;
    @MockBean
    private PrivateFoodServiceImpl privateFoodService;
    @MockBean
    private AccountController accountController;
    @MockBean
    private UsersRepo usersRepo;
    @MockBean
    LogTypeServiceImpl logTypeService;
    @MockBean
    DailyLogServiceImpl dailyLogService;

    private Users user = new Users();

    @Before
    void setUp() {

        Users user = new Users();
        //usersservice

        MockitoAnnotations.initMocks(this);
        Mockito.mock(Users.class);

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
    }

    Users userSetup(Users user) {
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
        return user;
    }

    List<Recipe> recipesList(Users user) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        //Recipe recipe = new Recipe();
        //recipe.setFkUser(user);
        //recipe.setName("testRecipe");
        //recipe.setArchived(false);
        //recipe.setTotal_weight(1200);
        //recipe.setId(1L);
        //recipes.add(recipe);
        return recipes;
    }

    @AfterEach
    void tearDown() {

    }



    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"USER"})
    void recipes() throws Exception {

        HttpSession session = Mockito.mock(HttpSession.class);
        List<Recipe> recipes = recipesList(userSetup(new Users()));
        List<Recipe> mockRecipes = Mockito.spy(recipes);
        mockRecipes = recipesList(new Users());

        Mockito.when(recipeService.findAllFkUser(usersService.getLoggedInUser(session))).thenReturn(mockRecipes);
        //Mockito.when(recipeService.getRecipesForUserAndSearch(usersService.getLoggedInUser(session),String keyword)).thenReturn(mockRecipes);

        ResultActions resultActions = mockMvc.perform(get("/recipes").with(user("user@user.dk")))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void recipeInfo() throws Exception {

        Mockito.when(recipeService.findRecipeById(anyLong())).thenReturn(Mockito.mock(Recipe.class));
        mockMvc.perform(get("/recipeInfo/1").with(user("user@user.dk")))
            .andExpect(status().isOk());
    }

    @Test
    void editRecipe() throws Exception {

        Mockito.when(recipeService.findRecipeById(anyLong())).thenReturn(Mockito.mock(Recipe.class));
        mockMvc.perform(get("/editRecipe/1").with(user("user@user.dk")))
            .andExpect(status().isOk());
    }

    @Test
    void saveRecipe() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/editRecipe")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void removeFoodFromRecipe() throws Exception {
        mockMvc.perform(get("/removeFoodFromRecipe/1/1").with(user("user@user.dk")))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    void saveFoodInRecipe() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/saveFoodInRecipe/1/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void createRecipe() throws Exception {
        mockMvc.perform(get("/createRecipe").with(user("user@user.dk")))
            .andExpect(status().isOk());
    }


    @Test
    void addFoodToRecipe() throws Exception {
        mockMvc.perform(get("/addFoodToRecipe/1").with(user("user@user.dk")))
            .andExpect(status().isOk());
    }

    @Test
    void saveRecipeFood() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/saveRecipeFood/food/1/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"USER"})
    void archiveRecipe() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Users loggedInUser = Mockito.spy(user);
        loggedInUser.setId(1L);

        Mockito.when(usersService.getLoggedInUser(session)).thenReturn(loggedInUser);
        Mockito.when(usersService.getLoggedInUser(session).getId()).thenReturn(1L);

        mockMvc.perform(get("/archiveRecipe/true/1").with(user("user@user.dk")).session(session))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    void showRecipeArchive() throws Exception {

        mockMvc.perform(get("/showRecipeArchive").with(user("user@user.dk")))
            .andExpect(status().isOk());
    }

    @Test
    void addRecipeToDailyLog() throws Exception {

        mockMvc.perform(get("/addRecipeToDailyLog").with(user("user@user.dk")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    public void saveRecipeInDailyLog() throws Exception{
        Users user = new Users();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        DailyLog dailyLog = new DailyLog(1, 1, 1);
        DailyLog spyDailyLog = Mockito.spy(dailyLog);

        LogType logType = new LogType();
        logType.setType("Dinner");
        //dailyLog.setFkLogType(logType);

        ResultActions resultActions = mockMvc.perform(post("/saveRecipeInDailyLog/")

                        .flashAttr("dailyLog", dailyLog)
                        .param("log_type", logType.getType())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    void updateRecipeInDailyLog() throws Exception {

        DailyLog dailyLog = new DailyLog();
        dailyLog.setId(1L);
        dailyLog.setAmount(27.0);
        LogType logType = new LogType();
        logType.setType("Dinner");
        dailyLog.setFkLogType(logType);

        DailyLog spyDailyLog = Mockito.spy(dailyLog);

        Mockito.when(dailyLogService.findById(anyLong())).thenReturn(spyDailyLog);

        mockMvc.perform(get("/updateRecipeInDailyLog/1").with(user("user@user.dk").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    public void deleteRecipeFromDailyLog() throws Exception{
        DailyLog dailyLog = new DailyLog();
        dailyLog.setId(1L);
        dailyLog.setAmount(27.0);

        DailyLog spyDailyLog = Mockito.spy(dailyLog);

        ResultActions resultActions = mockMvc.perform(get("/deleteRecipeFromDailyLog/1"))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }
}