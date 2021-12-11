package com.base.site.controllers;

import com.base.site.models.PrivateFood;
import com.base.site.models.Users;
import com.base.site.repositories.PrivateFoodRepo;
import com.base.site.repositories.UsersRepo;
import com.base.site.services.*;
import org.hamcrest.BaseMatcher;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpRequest;
import java.util.Optional;
import javax.servlet.Filter;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PrivateFoodController.class)
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration*/
public class PrivateFoodControllerTest {

    Logger log = Logger.getLogger(PrivateFoodControllerTest.class.getName());

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

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
    PrivateFoodService privateFoodService;

    @Mock
    PrivateFoodRepo mockedPrivateFoodRepo;


    //Set<DailyLog> dailyLogs;

    /*@Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }*/

    @AfterEach
    void Teardown(){
        mockedPrivateFoodRepo.deleteAll();
    }

    @Test
    public void testRepoIsNotNull()  throws Exception  {
        assertNotNull(mockedPrivateFoodRepo);
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    void PrivateFood() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/privateFood"))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    void createPrivateFood() throws Exception{
        ResultActions resultActions = mockMvc.perform(get("/createPrivateFood"))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    /*@Test
    public void save_privateFood_OK() throws Exception {
        Users user = new Users();
        PrivateFood newPrivateFood = new PrivateFood(1L, "Flæskesteg", 30, 10, 20, 50.0, 40.0, user, null);
        when(mockedPrivateFoodRepo.save(any(PrivateFood.class))).thenReturn(newPrivateFood);
    }*/

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    public void updatePrivateFood() throws Exception{
        Long id = Long.valueOf(1);
        Users user = new Users();
        PrivateFood privateFood = new PrivateFood(id, "Bacon", 30, 10, 20, 50.0, 40.0, user, null);
        PrivateFood spyPrivateFood = Mockito.spy(privateFood);

        Mockito.when(privateFoodService.findById(anyLong())).thenReturn(spyPrivateFood);

        ResultActions resultActions = mockMvc.perform(get("/updatePrivateFood/{id}", id))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    public void savePrivateFood() throws Exception{
        //Long id = Long.valueOf(1);
        Users user = new Users();
        user.setId(1L);
        PrivateFood privateFood = new PrivateFood(null, "Bacon", 30, 10, 20, 50.0, 40.0, user, null);
        PrivateFood spyPrivateFood = Mockito.spy(privateFood);

        //Mockito.when(privateFood).thenReturn(spyPrivateFood);

        /*HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken csrfToken2 = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        log.info("CSRF: "+csrfToken2);*/

        //You can also specify it with params instead of flashAttr...
        /*.param("id", ""+privateFood.getId())
        .param("name", privateFood.getName())
        .param("protein", ""+privateFood.getProtein())
        .param("carbohydrates", ""+privateFood.getCarbohydrates())
        .param("fat", ""+privateFood.getFat())
        .param("energy_kilojoule", ""+privateFood.getEnergy_kilojoule())
        .param("energy_kcal", ""+privateFood.getEnergy_kcal())
        .param("fkUser.id", ""+privateFood.getFkUser().getId())*/

        ResultActions resultActions = mockMvc.perform(post("/savePrivateFood")
                .flashAttr("privateFood", privateFood)
                .with(csrf()))
                .andExpect(status().is3xxRedirection());

        //assertEquals(privateFoodService.findAll().size(), 1);
        //assertEquals(privateFoodService.findById(1L).getName(), "Bacon");


        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    @Test
    @WithMockUser(username = "user@user.dk", password = "pa$$", roles = {"ADMIN"})
    public void deletePrivateFood() throws Exception{
        Long id = Long.valueOf(1);
        Users user = new Users();
        PrivateFood privateFood = new PrivateFood(id, "Bacon", 30, 10, 20, 50.0, 40.0, user, null);
        PrivateFood spyPrivateFood = Mockito.spy(privateFood);

        //Mockito.when(privateFoodService.deleteById(id));

        ResultActions resultActions = mockMvc.perform(get("/deletePrivateFood/{id}", id))
                .andExpect(status().is3xxRedirection());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mv = mvcResult.getModelAndView();
    }

    /*@Test
    public void checkIfPrivateFoodExist() throws Exception {
        //Given
        //Users user = new Users();
        //PrivateFood newPrivateFood = new PrivateFood(1L, "Flæskesteg", 30, 10, 20, 50.0, 40.0, user, null);

        //When
        Optional<PrivateFood> exist = mockedPrivateFoodRepo.findById(1l);

        //Then
        assertThat(exist).isNotNull();
    }*/

}
