package com.base.site.controllers;

import com.base.site.models.*;
import com.base.site.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class RecipeController {
    Logger log = Logger.getLogger(DailyLogController.class.getName());

    private final String RECIPES = "recipes";
    private final String CREATE_RECIPE = "createRecipe";
    private final String ADD_FOOD_TO_RECIPE = "addFoodToRecipe";
    private final String SAVE_FOOD_TO_RECIPE = "saveFoodToRecipe";
    private final String REDIRECT = "redirect:/";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeFoodService recipeFoodService;

    @Autowired
    UsersService usersService;

    @Autowired
    FoodService foodService;

    @Autowired
    PrivateFoodService privateFoodService;


    @GetMapping("/recipes")
    public String recipes(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        log.info("Recipes getmapping called for user: "+loggedInUser.getUsername()+"...");
        List<Recipe> recipes = recipeService.findAllFkUser(loggedInUser);

        if(recipes.size() > 0) {
            for (Recipe recipe: recipes) {
                log.info("Recipe name: "+recipe.getName()+" Total weight: "+recipe.getTotal_weight());
                for (RecipeFood recipeFood: recipe.getAmounts()) {
                    log.info("Amount: "+recipeFood.getAmount()+" foodname: "+recipeFood.getFood().getName());
                }
            }
        }else {
            log.info("no recipes found for user "+loggedInUser.getUsername());
            Recipe noRecipe = new Recipe();
            noRecipe.setName("You have no recipe's, you can create one by clicking the create recipe button.");
            noRecipe.setTotal_weight(0);
            recipes.add(noRecipe);
        }

        model.addAttribute("recipes", recipes);


        return RECIPES;
    }

    @GetMapping("/createRecipe")
    public String createRecipe(Model model) {
        log.info("  createRecipe getmapping is called...");

        model.addAttribute("recipe", new Recipe());

        return CREATE_RECIPE;
    }

    @PostMapping("/createRecipe")
    public String createRecipe(@ModelAttribute("recipe") Recipe recipe) {
        log.info("  PostMapping createRecipe is called...");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());

        recipe.setFkUser(loggedInUser);
        recipe = recipeService.save(recipe);

        log.info("Recipe id: "+recipe.getId());

        return REDIRECT+ADD_FOOD_TO_RECIPE+"/"+recipe.getId();
    }

    //Might need to be moved to food controller
    @GetMapping("/addFoodToRecipe/{recipeId}")
    public String addFoodToRecipe(@PathVariable("recipeId")long recipeId, @Param("keyword") String keyword, Model model) {
        log.info("addFoodToRecipe Getmapping is called with recipeId: "+recipeId);

        List<Food> foodlist = foodService.findAllByKeyword(keyword);
        List<PrivateFood> pfoodlist = privateFoodService.findAllByKeyword(keyword);

        model.addAttribute("recipeId", recipeId);
        model.addAttribute("foodlist", foodlist);
        model.addAttribute("pfoodlist", pfoodlist);
        model.addAttribute("keyword", keyword);
        model.addAttribute("recipeFood", new RecipeFood());

        return ADD_FOOD_TO_RECIPE;
    }

    @PostMapping("/saveRecipeFood/{foodId}/{recipeId}")
    public String saveRecipeFood(@PathVariable("foodId")long foodId, @PathVariable("recipeId")long recipeId, RecipeFood recipeFood) {
        log.info("saveRecipeFood Postmapping is called with recipeId: "+recipeId+" and foodId: "+foodId+" recipeFood:::"+recipeFood.getAmount());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users loggedInUser = usersService.findByUserName(auth.getName());
        Recipe recipe = recipeService.findById(recipeId);


        if(loggedInUser.getId() == recipe.getFkUser().getId()) {
            recipeFood.setRecipe(recipe);
            recipeFood.setFood(foodService.findById(foodId));
            recipeFoodService.save(recipeFood);

            return REDIRECT+ADD_FOOD_TO_RECIPE+"/"+recipeId;
        } else {
            return REDIRECT+RECIPES;
        }

    }
}
