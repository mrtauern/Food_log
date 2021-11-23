package com.base.site.security;

import com.base.site.controllers.AccountController;
import com.base.site.models.*;
import com.base.site.repositories.*;
import org.apache.catalina.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class DbInitializer implements CommandLineRunner {
    private static final Logger log = Logger.getLogger(DbInitializer.class.getName());

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    UserTypeRepo userTypeRepo;

    @Autowired
    FoodRepo foodRepo;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeFoodRepository recipeFoodRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        /*
        log.info("Inside the DbInitializer");

        //----------------------------------USERS---------------------------------
        UserType userType = userTypeRepo.findByType("User_male");
        LocalDate date = LocalDate.parse("1998-02-14");

        List<Users> userList = new ArrayList<>();
        userList.add(new Users("User1", "user1", "user@user.dk", passwordEncoder.encode("user1"),userType,"USER",1,date));
        userList.add(new Users("User2", "user2", "user2@user2.dk", passwordEncoder.encode("user2"),userType,"USER",1,date));
        userList.add(new Users("admin1", "admin1", "admin1@admin1.dk", passwordEncoder.encode("admin1"),userType,"ADMIN",1,date));
        userList.add(new Users("admin2", "admin2", "admin2@admin2.dk", passwordEncoder.encode("admin2"),userType,"",1,date));

        usersRepo.saveAll(userList);
        log.info("Successfully inserted record inside user table!");
        //------------------------------------------------------------------------
        */

        //----------------------------------RECIPES-------------------------------
        //Do not enable wont work yet
        /*
        log.info("Inside the DbInitializer");

        Users user = usersRepo.findUsersByUsername("User@user.dk");
        Recipe recipe = new Recipe();
        recipe.setTotal_weight(2000);
        recipe.setFkUser(user);
        Food food1 = foodRepo.findByName("Surbrød");
        Food food2 = foodRepo.findByName("Grahamsbrød");
        Food food3 = foodRepo.findByName("Sigtebrød");
        Food food4 = foodRepo.findByName("Pølsebrød");
        Food food5 = foodRepo.findByName("Grovfranskbrød");
        Set<Food> foods = new HashSet<>();
        Set<Recipe> recipes = new HashSet<>();
        foods.add(food1);
        foods.add(food2);
        foods.add(food3);
        foods.add(food4);
        foods.add(food5);
        Food foodRecipe = new Food();

        recipeRepository.save(recipe);
        foodRepo.save(foodRecipe);

        Users user = usersRepo.findUsersByUsername("User@user.dk");
        Recipe recipe = new Recipe();
        recipe.setFkUser(user);
        recipe.setTotal_weight(450);
        recipeRepository.save(recipe);
*/
/*
        Recipe recipe = recipeRepository.getById(10L);
        RecipeFood recipeFood = new RecipeFood();
        Food food1 = foodRepo.findByName("Sigtebrød");

        recipeFood.setFood(food1);
        recipeFood.setRecipe(recipe);
        recipeFood.setAmount(150);


        recipeFoodRepository.save(recipeFood);
*/
        /*
        ArrayList<RecipeFood> recipeFoods = (ArrayList<RecipeFood>) recipeFoodRepository.findAll();
        for (RecipeFood rf: recipeFoods) {
            log.info("Happy days amount: "+rf.getAmount()+" food: "+rf.getFood().getName()+" recipe weight: "+rf.getRecipe().getTotal_weight());
        }
        */
        //Recipe recipe = recipeRepository.findById(5L).get();
        //log.info("Recipe weight: "+recipe.getTotal_weight());
        //log.info("Recipe size: "+recipe.getFoods().size());

        //------------------------------------------------------------------------
    }
}
